# tiko_2020
Tietokantaohjelmoinnin harjoitustyö

## Tärskyn Tietokanta

Kääntäminen:
Avaa komentoriviltä projektin juurikansio.
Anna komennot:
```javac src/invoicesystem/*.java```
```javac src/invoicesystem/*/*.java```

Lataa postgresql-42.2.5.jar ajuri ja aseta se src-kansioon.

Käynnistä ohjelma:

```java -classpath src/postgreql-42.2.5.jar:. invoicesystem.Main {tietokannan nimi} {tietokannan käyttäjä} {tietokannan salasana}```