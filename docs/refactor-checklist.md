# Refactor Checklist

Checklist viva per il refactor incrementale.

## Roadmap macro
- [ ] Baseline: creare file docs (decision-log, metrics, checklist, adr, questions)
- [ ] Tag `pre-refactor-baseline`
- [ ] Migrazione iniziale Gradle (singolo modulo)
- [ ] Tool qualità: Spotless, Checkstyle, JaCoCo
- [ ] Multi-modulo: `mypoop-core`, `mypoop-plugin`
- [ ] Estrarre primo servizio di dominio + primo test
- [ ] Definire porte e adapter minimi
- [ ] Refactor comandi (un file per comando, dispatcher)
- [ ] Listener snelli → delega a servizi core
- [ ] Migrazione a Paper API (nuova ADR)
- [ ] Introduzione Adventure + catalogo messaggi centralizzato
- [ ] Persistenza astratta (in-memory + file)
- [ ] Hardening: config validation, logging coerente, rimozione codice morto
- [ ] Release `v1.0.0-refactored`

Aggiornare lo stato e aggiungere sotto-task man mano.
