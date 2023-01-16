_Poznámka: README-čka v repozitároch sa väčšinou píšu po anglicky, ale keďže chceme, aby toto repo slúžilo aj na edukačné účely, píšeme README v slovenčine. Ak ti to vadí, prepáč, skús nas pochopiť. 🙈 Ak ťa to teší, nemáš začo. 😉_

V tomto repozitári nájdeš backend k našej fantastickej stránke [streetofcode.sk] (https://streetofcode.sk). Frontend nájdeš [tu](https://github.com/StreetOfCode/streetofcode-web).

## Technológie

- Jazyk: Kotlin
- Framework: [Spring Boot](https://reactjs.org/)
- Monitoring: [Sentry](https://sentry.io/)
- Autentifikácia používateľov (login): [Firebase Authentication](https://firebase.google.com/docs/auth)
- DB: H2 lokálne, postgres na produkcii
- DB versioning: [Flyway](https://flywaydb.org/)
- IDE: [IntelliJ IDEA]()
- Formátovanie a lintovanie: [ktlint](https://github.com/pinterest/ktlint)
- Testy: [kotest](https://kotest.io/)
- CI: [GitHub Actions](https://github.com/features/actions)
- Deployment: [Railway.app](https://railway.app?referralCode=z8Ptaa) via GitHub
  hooks (referral link)

## Štruktúra projektu

Spring Boot nám viac menej nadiktoval celú štruktúru projektu a Jakub ju trochu ešte "zenterprisoval". V `model` zložke nájdeš modely pre databázu. V `rest` zložke nájdeš controlleri, ktoré spracúvajú requesty. `service` zložka obsahuje servisy, ktoré obsahujú hlavnú logiku a prepájajú controlleri s repositories. `repositories` nájdeš v zložke `db.repository` a pomocou nich komunikujeme s db. `api` zložka obsahuje iba interfaces pre services. `client` zložka obsahuje komunikačných klientov pre tretie strany. No a v `configuration` zložke nájdeš všetky runtime-ové konfigurácie.

Schému k databáze ako aj migrácie nájdeš v `resources/db.specific/`. Ak by si upravoval/a model, tak je potrebné vytvoriť aj migráciu (robíme to manuálne).

## Ako si to môžeš spustiť?

Otvor proejkt v IntelliJ IDEA. Naviguj sa do `src/main/kotlin/sk/streetofcode/webapi/StreetOfCodeWebApiApplication.kt` a vedľa mainu by sa ti mala ukázať zelená "play" šípka, na ktorú keď klikneš, tak by sa ti mala spustiť appka.

To by malo byť všetko. Prípadne ak si to chceš spustiť cez terminál, tak to vieš urobiť pomocou príkazu:

```
./mvnw spring-boot:run
```

API beží na porte 8080. Bez frontentdu si môžeš napr. skúsiť request cez terminál:

```bash
wget localhost:8080/course/slug -qO-
```

alebo

```bash
wget localhost:8080/course/overview -qO-
```

Ak chceš niečo iba rýchlo zmeniť, aby si videl/a, že to vieš editovať, tak kľudne uprav napr. [názov každého kurzu](src/main/kotlin/sk/streetofcode/webapi/model/Course.kt#L134) alebo [si uprav cestu ku kurzom](src/main/kotlin/sk/streetofcode/webapi/rest/CourseController.kt#L24).

**Pre prístup do admin sekcie a k endpointom, ktoré sú chránené `@IsAdmin` anotáciou je potrebné nastaviť `streetofcode.enable-mock-auth=true` v `application.properties`.**

## Ak náhodou chceš niečo nakódiť (Contributing)

Proste vytvor pull request. Ideálne v pull requeste aj popíš, čo daná zmena má robiť a prečo by sme ju mali chcieť. Za každú pomoc budeme radi, ale tiež ber prosím na vedomie, že nie každý PR musím akceptovať.

ktlint si musíš reálne stiahnuť a nainštalovať na svoj comp, potom si ho vieš pridať ako external tool v IDEA a zároveň aj nabindovať. Alebo ho vieš spustiť cez terminál pri commitovaní.

## Ak si našiel alebo našla nejaký problém

Buď nám napíš cez [feedback formulár](https://streetofcode.sk/feedback), na [Discord](https://streetofcode.sk/discord), na mail (info@streetofcode.sk) alebo vytvor issue na GitHub-e.
