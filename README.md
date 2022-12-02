# O.CLOUD Metrics Scraper

## Descrizione

Descrivi brevemente cosa fa la tua applicazione

## Requisiti

L'applicazione richiede un Engine DB Postgres contenente un database chiamato ocloud_metrics.
Questo, a scopo di sviluppo è già configurato all'interno della directory "containers" è avviabile mediante il comando `docker-compose up -d`.

Lo stack che verrà creato include anche un pgAdmin4 raggiungibile all'indirzzo `http://localhost:5050`.
pgAdmin4 importa all'avvio il database creato sull'Engine DB così da renderdo visualizzabile e modificabile da interfaccia.
Per modificare come pgAdmin importa il database agire sul file `containers/init/database.json`

Tutti i parametri di configurazione dello stack sono esternalizzati ed editabili all'interno della directory `containers/env`.

### N.B: quando si modificano i parametri esterni dello stack è necessario riavviarlo con i comandi

- `docker-compose stop && docker-compose rm`
- `docker-compose up -d`

## AS IS

Allo stato attuale l'applicazione al boot distrugge e ricrea le tabelle del DB generate durante l'avvio precedente.
Per modificare questo comportamento cambiare il parametro `ddl-auto` in `application.yaml` da `create-drop` ad `update`.

Le tabelle sono create sulla base delle `Entity` definite all'interno del package `models`.
