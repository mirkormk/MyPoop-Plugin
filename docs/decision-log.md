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

## 2025-09-16: Primo servizio di dominio nel core
- Aggiunto PoopRulesService in `:mypoop-core` (stateless, puro Java).
- Creati test JUnit con coverage 100% su questo servizio (report JaCoCo attivo).
- Nessuna dipendenza Bukkit/Paper nel core.

---

## 2025-09-18: Policy linguistica (chat vs repository)
- Chat operativa: Italiano (questo canale resta in italiano).
- Artefatti repository (commit messages, PR titles/descriptions, commenti nel codice): Inglese.
- Motivazione: coerenza e accessibilità per contributori open-source; separazione tra comunicazione operativa e storicizzazione tecnica.

---

## 2025-09-24: Repo hygiene (rimozione artefatti compilati; enforcement .gitignore)
- Rimossi dal VCS artefatti di build storicamente tracciati (cartelle `target/` e `.class`).
- Confermata configurazione `.gitignore` per ignorare: `**/target/`, `**/build/`, `.gradle/`, `.idea/`, `*.iml`, `*.log`, `out/`.
- Effetto: working tree pulito dopo build; ridotta confusione nei diff e nei branch. Nessun impatto sui sorgenti.
- Raccomandazione: abilitare "Automatically delete head branches" su GitHub per evitare branch remoti obsoleti dopo merge.
