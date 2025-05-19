# CHANGELOG

Log zmian w projekcie — każda sekcja odpowiada dacie `[YYYY-mm-dd]` wprowadzenia zmian do głównej gałęzi `main`.

---

## 2025-05-19

### Zmienione

- Zaktualizowano reguły Checkstyle:
    - `Indentation.lineWrappingIndentation` zmieniono z `8` na `4`
    - Dodano `forceStrictCondition = false`, by umożliwić zgodność z domyślnym formatowaniem IntelliJ
- Poprawiono zgodność kodu z nowym stylem `try-with-resources`
- Zmieniono lokalizację `README.md`, aby ścieżka do pliku `checkstyle.xml` była zgodna z rzeczywistą lokalizacją w repozytorium