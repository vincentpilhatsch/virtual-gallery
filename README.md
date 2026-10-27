# Bachelorarbeit: Kunstwebsite für Olivier Keros

Dieses Repository enthält die Kunstwebsite des Berliner Künstlers Olivier Keros, die im Rahmen meiner Bachelorarbeit an der Berliner Hochschule für Technik entwickelt wurde. Die Webanwendung kombiniert eine moderne Webpräsenz, welche die Werke und den Künstler darstellt, mit einem immersiven 360-Grad-Rundgang durch die Galerien des Künstlers.

## Installation & Ausführung 

#### Voraussetzungen

- Git [Link](https://git-scm.com/)
- Node.js 23.7 [Link](https://nodejs.org/en/download)
- Java 17 [Link](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- Postgres 16.6 [Link](https://www.postgresql.org/download/)

#### Webanwendung starten

Projekt herunterladen
```sh
git clone https://gitlab.bht-berlin.de/s85807/gallery-website
cd gallery-website
```

Backend starten
```sh
cd gallery-backend
chmod +x mvnw (Falls mvnw nicht ausführbar)
mvnw spring-boot:run
```

Frontend starten
```sh
cd gallery-frontend
npm install
npm start
```

Die Webanwendung ist im Browser unter der URL: **localhost:4200** erreichbar.

#### Adminzugang
E-Mail: admin@bht.de\
Passwort: admin

### Anmerkung
Dieses Projekt wurde als Teil einer Bachelorarbeit von Vincent Pilhatsch an der Berliner Hochschule für Technik umgesetzt. Die Nutzung des Codes ist ohne Genehmigung nicht gestattet.

