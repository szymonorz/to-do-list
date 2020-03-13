# To-Do-Lista

[Link do aplikacji](https://files.catbox.moe/cg9gvd.apk)

To-do-lista to aplikacja na telefon z systemem Android (minimalna wersja 7.1) pozwalająca zorganizować użytkownikowi dzień.

# Ekran logowania i rejestracji
![alt_text](https://i.imgur.com/jOAoWPEm.png) ![alt_text](https://i.imgur.com/01bNfAnm.png)

# Widok aplikacji
![alt_text](https://i.imgur.com/ZynnhrFm.png) ![alt_text](https://i.imgur.com/Em9xrH0m.png)

# Dialog do wprowadzania danych
![alt_text](https://i.imgur.com/hOJzYQ3m.png)

# Co aplikacja oferuje
  - Przyjazny dla użytkownika interfejs
  - Możliwość pobrania i wgrania swoich danych (plik JSON)
  - Posiadanie własnego konta lub zalogowanie się poprzez konto Google (Firebase API)

# Użyte technologie
  - Kotlin - język programowania działający na JVM przystosowany głównie do programowania na urządzenia mobilne
  - GSON - narzędzie pozwalające konwertować obiekty na JSON oraz vice-versa
  - Firebase API - interfejs aplikacji pozwalający na komunikację z Googlowskim narzędziem Firebase, gdzie odbywa się uwierzytelnienie użytkownia oraz przechowywanie danych użytkownika 
  - Fragmenty - utworzono dwa Fragmenty (logowanie i rejestracja), które się wzajemnie zastępują w LoginActivity
  - Material Design - technologia, która zawiera już gotowe komponenty dla widoku


# Dlaczego nie Java?
- Kotlin oferuje tzw. Null-safety, czyli typy, które nie przyjmują wartości null. Zapobiega to błędom typu NullPointerException
- Java ze względu na swoją składnię potrafi pozostawić bardzo długie linijki kodu, które nie są atrakcyjne w odczycie
- Chęć nauczenia się nowego języka


# Błędy w aplikacji
- Widok nie odświeża się podczas usunięcia ostatniego widocznego elementu. Wymagany jest wtedy restart apki. (Można odświeżyć widok poprzez zmianę stanu checkboxa)
- Zapisywanie stanu checkboxa w bazie danych powodowało błędy. Na chwilę obecną checkbox jest tylko symboliczny i jego stan nie jest zapisywany w bazie danych
-  Przy załadowywaniu danych z pliku JSON aplikacja nie sprawdza czy dane pasują do obiektów aplikacji. Wczytanie błędnych danych może spowodować bezpowrotne usunięcie swoich danych z bazy.

# Błędy napotkane podczas tworzenia aplikacji
Na początku aplikacja miała wykorzystywać ExpandableRecyclerView, lecz ze względu na problem wynikający z brakiem możliwości informowania Adaptera o zwiększeniu się rozmiaru zestawu danych postanowiłem skorzystać ze zwykłego ExpandableListView. Ten pomysł również porzuciłem, ponieważ bo zmianie w zbiorze danych wszystkie listy rozwijane automatycznie się zwijały co uznałem za nieatrakcyjne i nieprzyjacielskie dla użytkownika.
Ostatecznie postanowiłem skorzystać z zagnieżdżonego RecyclerView, który działa według moich oczekiwań.

# Co wypadałoby jeszcze zrobić
- Naprawić wszystkie błędy
- Ulepszyć walidacje danych (jak na chwilę obecną sprawdza tylko czy hasło jest dłuższe bądź równe niż 8 znaków)
- Dodać animacje dla zwiększenia atrakcyjności aplikacji
- Grafika ic_launcher (ikonka aplikacji)