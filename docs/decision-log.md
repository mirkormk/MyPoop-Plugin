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

## 2025-09-10: Strumenti di qualità iniziali (non bloccanti)
- Aggiunti Spotless, Checkstyle, JaCoCo.
- Spotless enforcement disabilitato inizialmente (si userà spotlessApply manuale).
- Checkstyle attivo con ignoreFailures=true; regole minime (tab, newline EOF).
- JaCoCo configurato senza soglie (coverage report in seguito).
- Obiettivo: introdurre standard senza rompere la build.

Aggiungere nuove decisioni in ordine cronologico.
