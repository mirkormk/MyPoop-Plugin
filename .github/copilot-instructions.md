# GitHub Copilot Instructions

Follow these guardrails when suggesting changes inside this repository:

- Write commits, code, comments, and documentation in English (even if discussions happen in Italian).
- Respect the hexagonal split: keep `mypoop-core` free of Bukkit/Paper dependencies; wire adapters inside `mypoop-plugin` only.
- Prefer ports/services already defined in `mypoop-core` instead of reaching directly into Bukkit APIs from domain logic.
- Maintain compatibility with Paper 1.19.4, 1.20.6, and 1.21.x; use `VersionCapabilities` for feature detection rather than NMS or reflection.
- When adding new adapters, provide graceful fallbacks and structured logging.
- Run `./gradlew :mypoop-plugin:check` before opening a pull request; include new or updated tests for core logic (target >= 80% coverage).
- Document architectural decisions with a new ADR under `docs/adr/` when introducing a significant change.
- Keep configuration files backward compatible and update docs/metrics when behavior or performance characteristics change.
