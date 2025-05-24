# CHANGELOG

Log zmian w projekcie — każda sekcja odpowiada dacie `[YYYY-mm-dd]` wprowadzenia zmian do głównej gałęzi `main`.

---
## 2025-05-24
- Zaktualizowano `ci.yml` aby workflow uruchamiał się przy każdym branchu, nie tylko `main`.
- Drobne poprawki w `README.md`.

## 2025-05-22

- Zmieniono uruchamianie pluginu Checkstyle z fazy `validate` na `verify`.
- Dodano instrukcję tymczasowego wyłączenia Checkstyle za pomocą flagi `<skip>true</skip>` w konfiguracji pluginu Maven.
- Wyłączono regułę `MethodName` w testach jednostkowych (`src/test/java`), aby umożliwić stosowanie alternatywnych
  konwencji nazewnictwa (np. z użyciem `_`).

## 2025-05-19

- Zaktualizowano reguły Checkstyle:
    - `Indentation.lineWrappingIndentation` zmieniono z `8` na `4`.
    - Dodano `forceStrictCondition = false`, by umożliwić zgodność z domyślnym formatowaniem IntelliJ.
- Poprawiono zgodność kodu z nowym stylem `try-with-resources`.
- Zmieniono lokalizację `README.md`, aby ścieżka do pliku `checkstyle.xml` była zgodna z rzeczywistą lokalizacją w
  repozytorium.