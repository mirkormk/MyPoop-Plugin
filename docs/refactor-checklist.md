# Refactor Checklist

Checklist viva per il refactor incrementale.

## Roadmap macro
- [x] Baseline: creare file docs (decision-log, metrics, checklist, adr, questions)
- [x] Tag `pre-refactor-baseline`
- [x] Migrazione iniziale Gradle (singolo modulo)
  - [x] Gradle wrapper
  - [x] settings.gradle.kts
  - [x] build.gradle.kts (collega MyPoopPlugin src e resources)
  - [x] Espansione versione in plugin.yml (processResources)
  - [x] Build Gradle verde in parallelo a Maven
  - [ ] Valutare packaging/shading (rimandato a Shadow plugin in step successivi)
- [x] Tool qualità: Spotless, Checkstyle, JaCoCo
  - [x] Aggiunto Spotless (enforcement disabilitato inizialmente)
  - [x] Aggiunto Checkstyle (ignoreFailures=true)
  - [x] Aggiunto JaCoCo (baseline, senza soglie)
  - [ ] Abilitare enforcement Spotless/Checkstyle gradualmente
- [x] Multi-modulo: `mypoop-core`, `mypoop-plugin`
  - [x] Root come aggregatore
  - [x] Modulo `mypoop-core` vuoto (nessun import Bukkit)
  - [x] Modulo `mypoop-plugin` compila dalle cartelle esistenti (MyPoopPlugin/src)
- [x] Estrarre primo servizio di dominio + primo test
  - [x] Servizio PoopRulesService nel core
  - [x] Test JUnit su PoopRulesService
  - [x] Report JaCoCo core
- [ ] Definire porte e adapter minimi
  - [x] Porta PlayerMessagingPort nel core
  - [x] Adapter BukkitPlayerMessagingAdapter nel plugin
  - [ ] Wiring: inizializzare l'adapter nel bootstrap del plugin e fornirlo ai servizi core
  - [ ] Definire porta per configurazione (ConfigPort) e relativo adapter Bukkit/file
  - [ ] Definire porta per logging (LoggingPort) e relativo adapter Bukkit/SLF4J
- [ ] Refactor comandi (un file per comando, dispatcher)
  - [ ] Mappare comandi esistenti e output
  - [ ] Introdurre CommandDispatcher e classi per comando
  - [ ] Delegare la logica al core (via porte)
- [ ] Listener snelli → delega a servizi core
  - [ ] Inventariare listener
  - [ ] Estrarre logica > 30 righe in servizi core
- [ ] Migrazione a Paper API (nuova ADR)
- [ ] Introduzione Adventure + catalogo messaggi centralizzato
- [ ] Persistenza astratta (in-memory + file)
- [ ] Hardening: config validation, logging coerente, rimozione codice morto
- [ ] Release `v1.0.0-refactored`

Aggiornare lo stato e aggiungere sotto-task man mano.
