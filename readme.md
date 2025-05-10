# CI Check - System kontroli jakości kodu Java

## Spis treści
- [Co to jest CI?](#co-to-jest-ci)
- [Co to jest Checkstyle?](#co-to-jest-checkstyle)
- [Jak dodać CI do projektu?](#jak-dodać-ci-do-projektu)
- [Jak dodać Checkstyle?](#jak-dodać-checkstyle)
- [Reguły Checkstyle w projekcie](#reguły-checkstyle-w-projekcie)
- [Struktura projektu](#struktura-projektu)
- [Uruchamianie testów](#uruchamianie-testów)

## Co to jest CI?

CI (Continuous Integration) to praktyka deweloperska polegająca na częstym integrowaniu zmian w kodzie do głównej gałęzi repozytorium. Każda zmiana jest automatycznie weryfikowana przez proces budowania i testowania, co pozwala na wczesne wykrywanie błędów.

Główne korzyści z CI:
- Szybkie wykrywanie problemów
- Automatyczna weryfikacja jakości kodu
- Pewność, że kod działa na różnych środowiskach
- Spójność w zespole deweloperskim

## Co to jest Checkstyle?

Checkstyle to narzędzie do statycznej analizy kodu Java, które sprawdza czy kod jest zgodny z przyjętymi standardami. Pomaga utrzymać spójny styl kodowania w całym projekcie.

Checkstyle sprawdza między innymi:
- Formatowanie kodu
- Nazewnictwo zmiennych, metod i klas
- Organizację importów
- Długość linii
- Obecność niepotrzebnych spacji
- Użycie nawiasów klamrowych

## Jak dodać CI do projektu?

1. **Utwórz plik konfiguracyjny GitHub Actions**:
   ```
   .github/workflows/ci.yml
   ```

2. **Dodaj konfigurację CI** (przykład z projektu):
   ```yaml
   name: Java CI

   on:
     push:
       branches: [ "main" ]
     pull_request:
       branches: [ "main" ]

   jobs:
     build:
       runs-on: ubuntu-latest

       strategy:
         matrix:
           java: [ '17', '21' ]

       steps:
         - name: Checkout repository
           uses: actions/checkout@v4

         - name: Set up JDK
           uses: actions/setup-java@v4
           with:
             distribution: 'temurin'
             java-version: ${{ matrix.java }}

         - name: Verify with Maven
           run: mvn verify
   ```

3. **Zatwierdź zmiany** i wypchnij do repozytorium - CI uruchomi się automatycznie

## Jak dodać Checkstyle?

1. **Dodaj zależność do `pom.xml`**:
   ```xml
   <plugin>
       <groupId>org.apache.maven.plugins</groupId>
       <artifactId>maven-checkstyle-plugin</artifactId>
       <version>3.6.0</version>
       <dependencies>
           <dependency>
               <groupId>com.puppycrawl.tools</groupId>
               <artifactId>checkstyle</artifactId>
               <version>10.20.0</version>
           </dependency>
       </dependencies>
       <configuration>
           <configLocation>checkstyle.xml</configLocation>
           <consoleOutput>true</consoleOutput>
           <failOnViolation>true</failOnViolation>
       </configuration>
       <executions>
           <execution>
               <phase>validate</phase>
               <goals>
                   <goal>check</goal>
               </goals>
           </execution>
       </executions>
   </plugin>
   ```

2. **Utwórz plik konfiguracyjny `checkstyle.xml`** w głównym katalogu projektu

3. **Uruchom Checkstyle**:
   ```bash
   mvn checkstyle:check
   ```

## Reguły Checkstyle w projekcie

Projekt zawiera następujące reguły Checkstyle:

### 1. Niewykorzystane elementy
- **UnusedImports** - wykrywa nieużywane importy
- **UnusedLocalVariable** - wykrywa nieużywane zmienne lokalne

### 2. Formatowanie i wcięcia
- **IndentationCheck** - sprawdza wcięcia (1 tab = 4 spacje)
- **LineLength** - maksymalna długość linii 120 znaków
- **EmptyLineSeparator** - wymaga pustych linii między metodami, zakazuje podwójnych pustych linii

### 3. Białe znaki
- **WhitespaceAround** - wymaga spacji wokół operatorów (`=`, `>`, `<`, itp.)
- **NoWhitespaceBefore** - zakazuje spacji przed przecinkami, średnikami
- **GenericWhitespace** - sprawdza spacje w typach generycznych (`List<String>` nie `List <String>`)

### 4. Nazewnictwo
- **TypeName** - nazwy klas/interfejsów w UpperCamelCase
- **MethodName** - nazwy metod w lowerCamelCase
- **LocalVariableName** - nazwy zmiennych w lowerCamelCase
- **ConstantName** - stałe w SCREAMING_SNAKE_CASE

### 5. Dobre praktyki
- **NeedBraces** - wymaga nawiasów klamrowych nawet dla jednoliniowych bloków if/for/while
- **SystemOutUsage** - zakazuje użycia `System.out`, sugeruje użycie loggera
- **SystemErrUsage** - zakazuje użycia `System.err`, sugeruje użycie loggera
- **SingleLineComments** - zakazuje komentarzy jednoliniowych `//`, sugeruje Javadoc

### 6. Organizacja importów
- **CustomImportOrder** - wymaga separacji między grupami importów
- **RegexpMultiline** - wykrywa niepotrzebne puste linie między importami

## Struktura projektu

```
ci-check/
├── .github/
│   └── workflows/
│       └── ci.yml                    # Konfiguracja GitHub Actions
├── src/
│   ├── main/
│   │   └── java/
│   │       └── pl/wiktor/           # Kod produkcyjny
│   └── test/
│       ├── java/
│       │   └── pl/wiktor/
│       │       └── checkstyle/      # Testy Checkstyle
│       └── resources/
│           └── rules/
│               ├── fail/            # Przykłady błędnego kodu
│               └── success/         # Przykłady poprawnego kodu
├── checkstyle.xml                   # Konfiguracja reguł Checkstyle
└── pom.xml                         # Konfiguracja Maven
```

## Uruchamianie testów

### Lokalne uruchomienie
```bash
# Sprawdzenie Checkstyle
mvn checkstyle:check

# Uruchomienie testów
mvn test

# Pełna weryfikacja (Checkstyle + testy + raport pokrycia)
mvn verify
```

### Automatyczne uruchomienie
CI automatycznie uruchamia się przy:
- Push do gałęzi `main`
- Utworzeniu Pull Request do gałęzi `main`

### Raport pokrycia kodu
Projekt używa JaCoCo do mierzenia pokrycia kodu testami. Wymagane jest minimum 80% pokrycia. Raport generowany jest po wykonaniu:
```bash
mvn verify
```
Raport znajduje się w: `target/site/jacoco/index.html`