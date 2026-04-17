# 🌍 Cyclone i18n – Spring Starter

A Spring Boot 3.x auto‑configuration starter for internationalisation using **YAML‑based message files**.

It provides a drop‑in replacement for Spring’s `MessageSource` with support for:

- ✅ YAML message definitions (instead of `.properties`)
- ✅ Deeply nested message keys
- ✅ Numeric identifiers as message keys
- ✅ Runtime interpolation with named parameters
- ✅ Automatic locale fallback
- ✅ Seamless integration with Spring’s `MessageSource` and locale resolution

This starter is built on top of the core **i18n** library and wires everything together automatically for Spring Boot applications.

- Core library usage → https://github.com/CycloneTechnology/i18n/tree/master/i18n#readme

---

## Requirements

- **Java 17+**
- **Spring Boot 3.x**
- A Spring Boot application using the standard `spring-boot-starter` stack

---

## Installation

Add the dependency to your project.

### Maven

```xml
<dependency>
  <groupId>com.cyclone.i18n</groupId>
  <artifactId>i18n-spring-boot-starter</artifactId>
</dependency>
``
