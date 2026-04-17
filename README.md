# 🌍 Cyclone Technology I18n


[![Build](https://github.com/CycloneTechnology/i18n/actions/workflows/build.yml/badge.svg)](https://github.com/CycloneTechnology/i18n/actions/workflows/build.yml)

[![Release](https://img.shields.io/github/v/release/CycloneTechnology/i18n?display_name=tag&include_prereleases=true&cache_seconds=300)](https://github.com/CycloneTechnology/i18n/releases)

[![Maven](https://img.shields.io/badge/maven-GitHub%20Packages-blue)](https://github.com/CycloneTechnology/i18n/packages)

[![Java](https://img.shields.io/badge/Java-17%2B-blue)](https://adoptium.net/)

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)](https://spring.io/projects/spring-boot)

Internationalization for projects via Spring MessageSource.

---

## For Developers

Current Status:

---

### Semantic Versioning Rules

MAJOR.MINOR.PATCH

| Change type                     | Version bump | Examples                                      |
|:--------------------------------| :--------------: |:----------------------------------------------|
| Breaking API                    | MAJOR | Removing methods, changing config keys        |
| Backward‑compatible new feature | MINOR | New APIs, new config options                  |
| Bug fixes only                  | PATCH | Fixes, internal refactors                     |

### Git Is the Source of Truth

The version lives in Git, not Maven

| Rules |                            |
|:------|:---------------------------|
| main branch | releasable |
| Git tags | define releases |
| CI | builds only tagged releases |

---

### Repository Branch Model

| Branch | Use                 |
|:-------|:--------------------|
| main   | Stable, realeasable |
| feature/* | Work in progress |
| fix/* | Bug fixes           |

---

### Commit Message Format

  - feat: add locale fallback support
  - fix: correct ICU message parsing
  - docs: improve README examples
  - BREAKING CHANGE: changed message resolver API

---

### How versions are derived from commit messages
| Commit content   |   Version bump    |                                                |
|:-----------------|:-----------------:|:-----------------------------------------------|
| fix              |       PATCH       | Bug fixes that do not affect the public API    |
| feature          |       MINOR       | New features that are backwards compatible     |
| BREAKING CHANGE  |       MAJOR       | Breaking changes that affect the public API    |
