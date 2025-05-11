Potrzebne dyrektywy checkstyle:
1) Niewykorzystywany import, zmienna
2) Brak formatowania (1 tab = 4 znaki)
3) Brak niepotrzebnych whitespaces
4) Zakazane przerwy: między importami (wycofuje się z pomysłu ze względu na grupy importów), przed/po bloku, 2x przerwa
5) Wymagane przerwy: między metodami
6) lowerCamelCase dla zmiennych, Underscore (UPPER CASE) dla static
   lowerCamelCase dla variable/methods
   UpperCamelCase dla classes/interfaces/annotations/enums/record
   Screaming Snake Case dla contants
7) Pole musi być final jeśli nie zostaje przekazywana mu nowa referencja //poki co to omijam, trzeba by bylo swojego checkera zrobic
8) Brak sout, wskazówka: używaj loggera < dodać System.err
9) Brak komentarzy single-line (oprócz tych testowych), wskazówka: używaj javadoc
10) Limit kolumny: 120 znaków
11) Wymagane są klamry (np. w przypadku jednoliniowych if)

Wymagane pokrycie kodu min. 80% z wykorzystaniem jacoco

Dodać weryfikację, że klasy testowe mogą mieć komentarze jednoliniowe