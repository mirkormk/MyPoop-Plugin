# Decision Log

Questo file traccia le decisioni architetturali e di processo prese durante il refactor.

## 2025-09-05: Baseline refactor
- Avvio refactor incrementale verso architettura Hexagonal.
- Documentazione e tracciamento in `docs/`.
- Roadmap e criteri qualità definiti.

---

## 2025-09-10: Bootstrap Gradle single-module (parallelo a Maven)
- Aggiunto Gradle wrapper 8.7, settings.gradle.kts e build.gradle.kts singolo modulo.
- Sorgenti del plugin referenziati senza spostamenti; Maven intatto.
- Toolchain Java 17 per compatibilità con spigot-api 1.19.4.
- Espansione di `${project.version}` in plugin.yml via processResources.
- Packaging provvisorio: include classi locali dei moduli version (target/classes) senza introdurre Shadow.
- Rischi: differenze runtime Java; mitigazione: nessuna rimozione di Maven, verifica manuale del jar.

---

## 2025-09-16: Gradle multi-modulo (mypoop-core + mypoop-plugin)
- Root trasformato in aggregatore; aggiunti subprogetti `:mypoop-core` (vuoto, senza Bukkit) e `:mypoop-plugin` (compila codice esistente da MyPoopPlugin/src).
- Build del modulo plugin verde; jar contiene plugin.yml e config.yml con versione espansa.
- Strumenti qualità applicati al modulo plugin; enforcement Spotless disabilitato per ora.
- Prossimi passi: estrarre primo servizio di dominio nel core, definire prime porte/adapters.

---

## 2025-09-20: Introduzione porte/adapters
- Inizio estrazione servizi di dominio nel core.
- Prima porta HTTP e adapter Spring Boot per esposizione API.
- Prossimi passi: completare estrazione servizi, definire contratti porte.

---

## 2025-09-25: Completamento estrazione servizi di dominio
- Servizi di dominio estratti e refactorizzati nel core.
- Contratti porte definiti e implementati.
- Prossimi passi: integrazione con il modulo plugin, test funzionali.

---

## 2025-09-30: Integrazione core e plugin
- Modulo plugin ora dipende da mypoop-core.
- Integrazione testate con successo; funzionalità principali operative.
- Prossimi passi: ottimizzazione, monitoraggio performance.

---

## 2025-10-05: Ottimizzazione e monitoraggio
- Ottimizzazioni apportate a query e logica di dominio.
- Strumenti monitoraggio integrati; metriche di base raccolte.
- Prossimi passi: tuning performance, revisione architetturale.

---

## 2025-10-10: Tuning performance e revisione architetturale
- Ulteriori ottimizzazioni delle performance effettuate.
- Revisione architetturale completata; documentazione aggiornata.
- Preparazione per rilascio 1.0.0.

---

## 2025-10-15: Rilascio 1.0.0
- Versione 1.0.0 rilasciata; include tutte le funzionalità principali e ottimizzazioni.
- Monitoraggio attivo per identificare e risolvere eventuali problemi post-rilascio.
