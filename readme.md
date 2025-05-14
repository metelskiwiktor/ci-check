# Java CI-check

## 1. O instrukcji

Ten przewodnik ma na celu przedstawienie narzędzi do analizy jakości kodu Java, ze szczególnym uwzględnieniem Checkstyle
jako kluczowego narzędzia do wymuszania standardów kodowania. Instrukcja skierowana jest do osób, które chcą poprawić
jakość, czytelność i utrzymywalność swojego kodu poprzez wprowadzenie zautomatyzowanych mechanizmów
weryfikacji.

Standaryzacja stylu kodu ułatwia pracę zespołową, redukuje błędy oraz przyspiesza wdrażanie nowych członków zespołu, a
zautomatyzowane narzędzia kontroli eliminują potrzebę ręcznych przeglądów kodu pod kątem formatowania i konwencji.

## 2. Analiza kodu

Analiza statyczna kodu to proces badania oprogramowania bez jego uruchamiania, w celu identyfikacji potencjalnych
błędów, luk w bezpieczeństwie i problemów z jakością.

### Główne narzędzia analizy kodu dla Javy:

- **Checkstyle**: sprawdza zgodność kodu ze standardami formatowania i konwencjami nazewnictwa
- **PMD**: wykrywa potencjalne błędy, nieoptymalne lub nieużywane fragmenty kodu
- **FindBugs/SpotBugs**: analizuje bytecode w poszukiwaniu typowych błędów lub podejrzanych wzorców
- **SonarQube**: kompleksowa platforma do analizy jakości kodu z metrykami i śledzeniem postępów
- **JaCoCo**: mierzy pokrycie kodu przez testy

Używanie tych narzędzi pozwala:

- Wcześnie wykrywać błędy i problemy z kodem
- Utrzymywać spójny styl kodu w całym projekcie
- Zapewnić zgodność z najlepszymi praktykami
- Redukować techniczny dług
- Poprawiać bezpieczeństwo aplikacji
- Ułatwiać wprowadzanie nowych osób do projektu

## 3. Checkstyle

Checkstyle to narzędzie zapewniające przestrzeganie standardów kodowania w projektach Java. Umożliwia definiowanie i
egzekwowanie spójnych konwencji kodowania, co znacząco wpływa na czytelność i utrzymywalność kodu.

### Jak działa Checkstyle:

1. Analizuje kod źródłowy Java
2. Sprawdza zgodność z regułami zdefiniowanymi w pliku konfiguracyjnym XML
3. Generuje raport o wykrytych naruszeniach
4. Może być zintegrowany z procesem budowania (np. przez Maven), IDE lub CI

### Typowe reguły Checkstyle:

- **Formatowanie**: wcięcia, białe znaki, długość linii, puste linie
- **Konwencje nazewnictwa**: dla klas, metod, zmiennych, stałych
- **Dokumentacja**: wymagania dotyczące Javadoc
- **Importy**: nieużywane lub nadmiarowe importy
- **Struktura kodu**: złożoność metod, wielkość klas, używanie klamer
- **Dobre praktyki**: zakaz używania System.out/err, zakaz komentarzy jednoliniowych

### Standardowe zestawy reguł:

- **Google Java Style**: oficjalny styl kodowania Google dla Javy
- **Sun Code Conventions**: klasyczny standard Sun/Oracle
- **Checkstyle Standard**: domyślny zestaw reguł
- **Custom**: własne reguły zdefiniowane według potrzeb projektu

W tym projekcie używamy niestandardowej konfiguracji Checkstyle wzorowanej na Google Java Style:

| **Kategoria**         | **Reguła**                               | **Opis**                                                           | **Uzasadnienie**                                                                     |
|-----------------------|------------------------------------------|--------------------------------------------------------------------|--------------------------------------------------------------------------------------|
| **Formatowanie**      | `Indentation`                            | Wcięcia 4 spacje na poziom; szerokość tabulatora 4                 | Ułatwia czytelność i zachowuje spójność w obrębie zespołu                            |
|                       | `LineLength`                             | Maksymalna długość linii: 120 znaków                               | Poprawa czytelności i zgodność z dobrymi praktykami ekranowymi                       |
|                       | `Blank lines { / }` (`RegexpMultiline`)  | Brak pustych linii bezpośrednio po `{` i przed `}`                 | Eliminacja nadmiarowej przestrzeni, zachowanie zwartości kodu                        |
| **Białe znaki**       | `GenericWhitespace`                      | Kontrola nieprawidłowych spacji przed/po znakach                   | Zapobiega niejednolitemu rozmieszczaniu kodu                                         |
|                       | `NoWhitespaceBefore`, `WhitespaceAround` | Spójność odstępów przy operatorach i separatorach                  | Zachowanie czytelności i jednolitego wyglądu kodu                                    |
| **Importy i zmienne** | `UnusedImports`, `UnusedLocalVariable`   | Zakaz nieużywanych importów i zmiennych                            | Eliminacja martwego kodu, zmniejszenie złożoności                                    |
| **Konwencje nazw**    | `TypeName`                               | UpperCamelCase dla klas/interfejsów                                | Zgodność z konwencją Java                                                            |
|                       | `MethodName`, `LocalVariableName`        | lowerCamelCase dla metod i zmiennych lokalnych                     | Spójność i łatwość identyfikacji elementów kodu                                      |
|                       | `ConstantName`                           | SCREAMING\_SNAKE\_CASE dla stałych                                 | Podkreślenie niezmienności wartości                                                  |
| **Struktura kodu**    | `NeedBraces`                             | Wymagane klamry `{}` dla wszystkich bloków sterujących             | Zapobieganie błędom logicznym w kodzie jednoliniowym                                 |
|                       | `EmptyLineSeparator`                     | Wymuszenie pustych linii między elementami (np. metodami, klasami) | Lepsza separacja logiczna kodu                                                       |
| **Zakazane elementy** | `RegexpSinglelineJava` (System.out/err)  | Zakaz `System.out` i `System.err`                                  | Wymuszenie użycia loggerów – dobra praktyka produkcyjna                              |
|                       | `RegexpSinglelineJava` (komentarze `//`) | Zakaz komentarzy jednoliniowych poza testami                       | Wymuszenie stosowania Javadoc – ujednolicenie dokumentacji, zapobiega komentarzom AI |
| **Testy**             | `SuppressionXpathSingleFilter`           | Dopuszczenie komentarzy `//` tylko w testach                       | Umożliwia nadawanie komentarzy `//given //when //then` w testach                     |

---

## 4. Instrukcja wdrożenia

### 4.1 Konfiguracja z Maven

Maven Plugin dla Checkstyle pozwala na integrację weryfikacji stylu kodu z procesem budowania projektu.

#### Dodanie pluginu do pom.xml:

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
        <configLocation>https://raw.githubusercontent.com/metelskiwiktor/ci-check/main/checkstyle.xml</configLocation>
        <consoleOutput>true</consoleOutput>
        <failOnViolation>true</failOnViolation>
        <includeTestSourceDirectory>true</includeTestSourceDirectory>
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

> Wersja zastosowana poniżej wspiera `JDK 21`.

> Checkstyle jest pobierany z url: `https://raw.githubusercontent.com/metelskiwiktor/ci-check/main/checkstyle.xml`

#### Uruchamianie Checkstyle z Maven:

- **Podczas budowania**: `mvn clean verify`
- **Niezależnie**: `mvn checkstyle:check`

Możesz również używać lokalnego pliku konfiguracyjnego Checkstyle z repozytorium:

```xml

<configuration>
    <configLocation>checkstyle.xml</configLocation>
</configuration>
```

> Pobrany plik `checkstyle.xml` przechowuj w root katalogu projektu.

### 4.2 Integracja z IntelliJ IDEA

Integracja Checkstyle z IntelliJ IDEA pozwala na natychmiastowe wykrywanie naruszeń podczas kodowania.

#### Zalety integracji z IDE:

- Natychmiastowa informacja zwrotna podczas pisania kodu
- Oznaczanie problematycznych fragmentów kodu
- Sugestie napraw
- Możliwość sprawdzenia zgodności przed commitem

#### Instalacja pluginu Checkstyle-IDEA:

1. Otwórz IntelliJ IDEA
2. Przejdź do `File > Settings > Plugins` (Windows/Linux) lub `IntelliJ IDEA > Preferences > Plugins` (macOS)
3. Przejdź do zakładki `Marketplace` i wyszukaj `Checkstyle-IDEA`
4. Kliknij `Install` i uruchom ponownie IDE

#### Konfiguracja pluginu:

1. Przejdź do `File > Settings > Tools > Checkstyle` (Windows/Linux) lub
   `IntelliJ IDEA > Preferences > Tools > Checkstyle` (macOS)
2. Dodaj konfigurację Checkstyle:
    - Kliknij `+` w sekcji "Configuration File"
    - Wybierz "Local File" aby użyć pliku z projektu lub "URL" aby użyć pliku z repozytorium
    - Dla URL: wpisz `https://raw.githubusercontent.com/metelskiwiktor/ci-check/main/checkstyle.xml`
    - Nadaj opis
    - Kliknij `Next` a następnie `Finish`
3. Zaznacz swoją konfigurację jako aktywną
4. Kliknij `Apply` i `OK`
5. Od tej chwili Intellij będzie analizować kod w czasie rzeczywistym

## 5. Continuous Integration (CI)

Continuous Integration to praktyka automatycznego budowania i testowania kodu z repozytorium źródłowego w celu wczesnego
wykrywania i naprawy błędów.

### Popularne narzędzia CI:

- **GitHub Actions**: zintegrowane z GitHub, pozwala na definiowanie workflow w YAML
- **Jenkins**: samodzielnie hostowane, wysoce konfigurowalne narzędzie CI/CD
- **Travis CI**: usługa CI w chmurze, łatwa w konfiguracji
- **CircleCI**: rozwiązanie CI/CD z zaawansowanymi funkcjami i dobrą integracją
- **GitLab CI/CD**: zintegrowane z GitLab
- **Azure DevOps**: kompleksowa platforma CI/CD od Microsoft

### 5.1 GitHub Actions

GitHub Actions pozwala na automatyzację workflow bezpośrednio w repozytorium GitHub. Skonfigurowane akcje są uruchamiane
w odpowiedzi na zdarzenia, takie jak push lub pull request.
Po 

#### Jak działa GitHub Actions:

1. Workflow są definiowane w plikach YAML w katalogu `.github/workflows/`
2. Każdy workflow składa się z jednego lub więcej jobów
3. Joby zawierają kroki (steps), które wykonują określone zadania
4. Workflow są uruchamiane w odpowiedzi na zdarzenia (np. push, pull request)

#### Nasza konfiguracja GitHub Actions (.github/workflows/ci.yml):

```yaml
name: Java CI-check

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
        java: [ '21' ]

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

#### Omówienie konfiguracji:

- **Wyzwalacze (triggers)**: Workflow jest uruchamiany przy każdym push lub pull request do gałęzi `main`
- **Środowisko**: Uruchamiane na najnowszej wersji Ubuntu
- **Matryca**: Testuje kod na Java 21
- **Kroki**:
    1. Pobiera kod z repozytorium
    2. Konfiguruje Java Development Kit (JDK)
    3. Uruchamia `mvn verify`, który wykonuje kompilację, testy oraz analizy kodu (w tym Checkstyle)

> Workflow jest uruchamiany w kontenerowym środowisku GitHub Actions.

> Pamiętaj by zapisać plik `.github/workflows/ci.yml` w swoim repozytorium.

Ta konfiguracja zapewnia, że każda zmiana w kodzie przechodzi przez weryfikację jakości przed scaleniem z główną
gałęzią, utrzymując wysokie standardy kodu w projekcie.