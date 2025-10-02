# ADR 005: Multi-version strategy without NMS

## Status
Accepted

## Context
Historically, multi-version support relied on per-version NMS/CraftBukkit internals and reflection. This is brittle and costly. We are migrating to Paper-first (ADR-003) and want a simple, maintainable approach that avoids NMS and extra dependencies during the refactor.

## Decision
- Support window
  - Primary: Paper 1.21.x (baseline for new features).
  - Transitional support (final multi-version series): 1.20.6 and 1.19.4.
- No NMS modules for new versions; prefer Paper API capabilities and graceful degradation.
- During refactor: keep code unified behind ports/adapters; avoid introducing new libraries (e.g., Adventure) until baseline is complete.
- Version handling approach
  - Prefer feature detection via API (e.g., Paper API methods/classes presence) and simple version guards when necessary.
  - Introduce a small `VersionCapabilities` service (infrastructure) to centralize capability checks and minor behavioral differences. Expose only domain-level flags to the core.
  - Minimize reflection; only as a short-lived bridge until a proper API path is in place.
- Build strategy
  - Single codebase, compileOnly against current primary Paper API.
  - Use Java toolchains matching targets (Java 21 for 1.21.x; Java 17 for 1.20.6/1.19.4 execution). The plugin code will remain source-compatible across the support window.
  - Validate older targets via local test runs; no shaded or per-version jars unless a hard blocker appears.

## Consequences
- Reduced maintenance overhead; no per-version NMS code.
- Clear upgrade path: after the transitional series, we can drop 1.19.4/1.20.6 to focus on latest.
- Minor adapter code may be needed to normalize behaviors, encapsulated in `VersionCapabilities`.

## Alternatives considered
- Per-version Gradle modules: rejected for now (adds complexity) unless hard compatibility breaks are found.
- External multi-version libraries: postponed; the current scope avoids adding dependencies until refactor baseline is done.

## Implementation notes
- Document the support window in README and plugin.yml.
- Add a lightweight `VersionCapabilities` and route version-specific checks through it.
- Replace existing reflection bridge incrementally with Paper API usages guarded by capabilities.

