package com.cyclone.i18n;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.lang.NonNull;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.*;

@Slf4j
public class I18nMessageSource extends AbstractMessageSource {

    private final Map<Locale, Map<String, Object>> messages = new HashMap<>();
    private final Map<String, Object> commonMessages = new HashMap<>();
    private final String basePath;
    private final String defaultLocale;
    private final I18nMessageInterpolator interpolator = new I18nMessageInterpolator();

    public I18nMessageSource(String basePath, String defaultLocale) {
        this.basePath = basePath;
        this.defaultLocale = defaultLocale;

        loadMessages();
    }

    private void loadMessages() {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        try {
            Resource[] resources = resolver.getResources("classpath*:" + basePath + "/**/*.yaml");

            for (Resource resource : resources) {
                String filename = resource.getFilename();
                if (filename != null) {
                    if (filename.matches(".*_([a-z]{2}(_[A-Z]{2})?)\\.yaml")) {
                        String tag = filename.substring(filename.indexOf('_') + 1, filename.length() - 5); // strip ".yaml"
                        Locale locale = Locale.forLanguageTag(tag.replace('_', '-'));
                        loadResource(resource, messages.computeIfAbsent(locale, l -> new LinkedHashMap<>()));
                    } else if (filename.endsWith(".yaml")) {
                        // Treat as a common file
                        try (InputStream input = resource.getInputStream()) {
                            Yaml yaml = new Yaml();
                            commonMessages.putAll(normalizeKeys(yaml.load(input)));
                        }
                    }
                }
            }
        } catch (IOException _) {
        }
    }

    private void loadResource(@NonNull Resource resource, @NonNull Map<String, Object> merged) throws IOException {
        try (InputStream input = resource.getInputStream()) {
            log.debug("Loading Messages from {} => {}", resource.getFilename(), resource.getDescription());

            Yaml yaml = new Yaml();
            Map<String, Object> normalized = normalizeKeys(yaml.load(input));
            merged.putAll(normalized);
        }
    }

    private Map<String, Object> normalizeKeys(Map<?, ?> raw) {
        Map<String, Object> flat = new HashMap<>();
        flattenMap("", raw, flat);
        return flat;
    }

    private void flattenMap(String prefix, Map<?, ?> source, Map<String, Object> target) {
        for (Map.Entry<?, ?> entry : source.entrySet()) {
            String key = String.valueOf(entry.getKey());
            Object value = entry.getValue();
            String fullKey = prefix.isEmpty() ? key : prefix + "." + key;

            if (value instanceof Map<?, ?> nested) {
                flattenMap(fullKey, nested, target);
            } else {
                target.put(fullKey, value);
            }
        }
    }

    @Override
    protected MessageFormat resolveCode(@lombok.NonNull String code, @lombok.NonNull Locale locale) {
        Object value = resolveMessage(code, locale);
        return switch (value) {
            case Integer num -> new MessageFormat(MessageFormat.format("{0,number,#}", num), locale);
            case String msg -> {
                Map<String, Object> context = buildInterpolationContext(locale);
                String interpolated = interpolator.interpolate(msg, context);

                // If interpolation failed (still contains ${...}), treat as unresolved
                if (interpolated.contains("${")) {
                    yield new MessageFormat("???" + code + "???", locale);
                }

                // Use MessageFormat only if the message contains {n} placeholders
                if (interpolated.matches(".*\\{\\d+}.*")) {
                    yield new MessageFormat(interpolated, locale);
                } else {
                    yield new MessageFormat(MessageFormat.format("{0}", interpolated), locale);
                }
            }
            case Boolean bool -> new MessageFormat(MessageFormat.format("{0}", bool), locale);
            case null, default -> null;
        };
    }

    private Map<String, Object> buildInterpolationContext(Locale locale) {
        // Locale-specific
        Map<String, Object> context =
                new LinkedHashMap<>(messages.getOrDefault(locale, Collections.emptyMap()));

        // Base locale (language + country)
        String localeLanguage = locale.getLanguage();
        if (!locale.getCountry().isEmpty()) {
            localeLanguage += "-" + locale.getCountry();
        }
        Locale localeForLanguageTag = Locale.forLanguageTag(localeLanguage);
        if (!localeForLanguageTag.equals(locale)) {
            context.putAll(messages.getOrDefault(localeForLanguageTag, Collections.emptyMap()));
        }

        // Language-only locale
        Locale localeForLanguageOnly = Locale.forLanguageTag(locale.getLanguage());
        if (!localeForLanguageOnly.equals(locale)) {
            context.putAll(messages.getOrDefault(localeForLanguageOnly, Collections.emptyMap()));
        }

        // Default locale
        Locale defaultLocaleForLanguageTag = Locale.forLanguageTag(defaultLocale);
        if (!defaultLocaleForLanguageTag.equals(locale)) {
            context.putAll(messages.getOrDefault(defaultLocaleForLanguageTag, Collections.emptyMap()));
        }

        // Common messages last (lowest priority)
        context.putAll(commonMessages);

        return context;
    }

    private Object resolveMessage(String code, Locale locale) {
        // Try full locale
        Map<String, Object> localeMessages = messages.get(locale);
        if (localeMessages != null && localeMessages.containsKey(code)) {
            return localeMessages.get(code);
        }

        // Try base locale (language + country, no variant)
        String localeLanguage = locale.getLanguage();
        if (!locale.getCountry().isEmpty()) {
            localeLanguage += "-" + locale.getCountry();
        }
        Locale localeForLanguageTag = Locale.forLanguageTag(localeLanguage);
        if (!localeForLanguageTag.equals(locale)) {
            localeMessages = messages.get(localeForLanguageTag);
            if (localeMessages != null && localeMessages.containsKey(code)) {
                return localeMessages.get(code);
            }
        }


        // Try the language-only locale
        Locale localeForLanguageOnly = Locale.forLanguageTag(locale.getLanguage());
        if (!localeForLanguageOnly.equals(locale)) {
            Map<String, Object> languageMessages = messages.get(localeForLanguageOnly);
            if (languageMessages != null && languageMessages.containsKey(code)) {
                return languageMessages.get(code);
            }
        }

        // Fallback to default locale
        Locale defaultLocaleForLanguageTag = Locale.forLanguageTag(defaultLocale);
        localeMessages = messages.get(defaultLocaleForLanguageTag);
        if (localeMessages != null && localeMessages.containsKey(code)) {
            return localeMessages.get(code);
        }

        return null;
    }
}
