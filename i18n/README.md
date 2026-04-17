# 🌍 Cyclone I18n Core library

A Spring Boot 3.1 library for internationalization using YAML message files. 
Supports deeply nested keys, numeric identifiers, runtime interpolation, and automatic locale 
fallback — all wired into Spring’s `MessageSource`.

---

## 🚀 Features

- ✅ Loads all YAML files matching `**/*_{locale}.yaml` inside `/i18n` or below
- ✅ Loads yaml files without locale and uses them as common fallback across all locales
- ✅ Supports nested keys and numeric values (e.g. `error.404.message`)
- ✅ Late interpolation using `${some.key}` resolved dynamically across bundles
- ✅ interpolation also uses locale and common fallback
- ✅ Locale fallback hierarchy: `en-GB → en → common`
- ✅ ignores locale variant i.e. `en_GB_oed` treated as `en_GB`
- ✅ Automatically registered as a Spring `MessageSource`
- ✅ Compatible with Spring Boot 3.1 and beyond
- ✅ requires Java 22+

---

## 📦 File Structure

**File naming convention:** `name_{locale}.yaml` or `name.yaml`
Where `{locale}` is a BCP 47 tag like `en`, `en_GB`, `fr`.

Files that do not contain a locale will be used as a fallback for all locales.

---

## 🧾 Sample YAML Messages

**messages_en.yaml**
```yaml
greeting:
  welcome: "Hello {0}, ${errors.404.message}"

errors:
  404:
    message: "Not Found"
  500:
    message: "Server Error"
```
**messages_en_GB.yaml**
```yaml
greeting:
  welcome: "Cheers {0}, ${errors.404.message}"
```
**common.yaml**
```yaml
errors:
  404:
    message: "Missing resource"
```

## 🧑‍💻 Usage
In any Spring component:
```java
@Autowired MessageSource messageSource;

String msg = messageSource.getMessage("greeting.welcome",
    new Object[]{"Phil"},
    Locale.forLanguageTag("en-GB")
);
```
Result:
```aiignore
"Cheers Phil, Not Found"
```
Fallback path:
- Search en-GB
- If missing, search en
- If missing, search default
  All {key} interpolations are resolved on retrieval — allowing keys to reside in different YAML bundles.

As an alternative if autowiring/injection is not your thing, then you may use the static I18n 
class to resolve messages...

```java
String msg = I18n.get("greeting.welcome",
        Locale.forLanguageTag("en-GB"),
    new Object[]{"Phil"}
);
```
Result:
```aiignore
"Cheers Phil, Not Found"
```

---

## 🧪 Test Coverage
Included test cases validate:
- ✅ Deep nesting and numeric keys
- ✅ Fallback resolution across locale hierarchy
- ✅ Interpolation logic for cross-file references

Example test:
```java
@Test
void resolvesAndInterpolatesAcrossBundles() {
    Locale locale = Locale.forLanguageTag("en-GB");
    String result = messageSource.getMessage("greeting.welcome", new Object[]{"Phil"}, locale);
    assertEquals("Cheers Phil, Not Found", result);
}
```

---

## 🛠️ Integration
Normally no configuration required (may need to add `com.cyclone.i18n` to `@ComponentScan`). 
Include this starter as a dependency and place your YAML bundles in `src\main\resources\i18n` 
following the naming format `any_name.yaml`.

The library will:

- Automatically scan all YAML files in and below the i189n resources folder
- Flatten nested keys into dot notation
- Register a  bean
- Handle fallback and interpolation

