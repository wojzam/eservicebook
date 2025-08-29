**Najważniejsze klasy to:**
- **DataManager** - przechowywuje pojazdy sessionID przekazywane do wszystkich funkcji API, zapis i odczyt danych z pamięci
  jest podmiotem obserwacji (powiadomienie w momencie załadowania nowych pojazdów)
- **APIFunctions** - wywołuje wszystkie funkcje API, wysyła komunikat do przekazanego Handlera
- **MainScreenActivity** - główny ekran aplikacji, centrum większości funkcjonalności

**Do zrobienia:**
- w całym projekcie oznaczono kilka miejsc do ulepszenia przez TODO
- ujednolicenie obsługi funkcji API, np. APIFunctions mogłoby interpretować odpowiedzi funkcji i przesyłać dalej tylko jakieś krótkie info jaką akcje podjąć
- logowanie wywoływanie jest w wielu miejscach - może powinno odbywać się jakoś wewnętrznie zawsze w tej samej klasie
- obsługa wielowątkowości dodać np. semafory do dostępu do sessionID oraz przy wywołaniu funkcji API
- przerobienie główniego ekranu, aby było możliwe dodanie nawigacji dolnej i operowanie na fragmentach (na razie były problemy z kontekstem fragmentów i zdarzało się np. nieskończone odświerzanie ekranu, ponieważ komunikat nigdy nie dotarł przez zmiane ekranu)
- można poprawić kafelki z historią napraw, aby nie używać wielu obiektów TextView tylko np. zastosować tekst w formacie html
- dodać opcje usuwania pojazdów (poprzez przesunięcie kafelka)
- dane użytkownika są przechowywane w osobnych zmiennych (utworzyć klase User) i są wczytywane w kilku miejscach zamiast raz się wczytać i przechować
- istnieją lepsze sposoby implementacji SplashScreen
