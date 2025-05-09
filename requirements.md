Potrzebne dyrektywy checkstyle:
1) Niewykorzystywany import, zmienna
2) Brak formatowania (1 tab = 4 znaki)
3) Brak niepotrzebnych whitespaces
4) Zakazane przerwy: między polami, między importami, przed/po bloku, 2x przerwa
5) Wymagane przerwy: między metodami
6) lowerCamelCase dla zmiennych, Single Underscore (UPPER CASE) dla static
7) Pole musi być final jeśli nie zostaje przekazywana mu nowa referencja
8) Brak sout, wskazówka: używaj loggera
9) Brak komentarzy single-line (oprócz tych testowych), wskazówka: używaj javadoc
10) Limit kolumny: 120 znaków
11) Wymagane są klamry (np. w przypadku jednoliniowych if)

Wymagane pokrycie kodu min. 80% z wykorzystaniem jacoco
