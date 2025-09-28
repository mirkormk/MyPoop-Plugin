# ADR 003: Migrate to Paper as primary API and define support window

## Status
Accepted

## Context
Spigot NMS and CraftBukkit internals make multi-version maintenance costly and brittle. Paper provides stronger APIs, performance, and community support. The project currently relies on ad-hoc per-version classes and reflection.

## Decision
- Target Paper API as the primary runtime API for the plugin.
- Drop direct NMS dependencies in favor of Paper API wherever possible.
- Define a support window:
  - Primary: latest 1.21.x (baseline for new features and mainline development).
  - Transitional support (final multi-version series): 1.20.6 and 1.19.4.
- For truly version-specific behavior, isolate adapters behind ports and prefer API-based feature flags; reflection only as a temporary bridge.
- Do not introduce new dependencies (e.g., Adventure) until the refactor baseline is complete.

## Consequences
- Simplifies maintenance; reduces breakage across versions.
- Users on very old server versions may no longer be supported (documented in README/CHANGELOG).
- Enables modern tooling if low-level hooks are required, but we’ll avoid NMS by design.
- Build is Gradle-only (Maven deprecation tracked in the checklist); Java toolchains will be aligned per targeted Paper version (e.g., Java 21 for 1.21.x; Java 17 for 1.20.6/1.19.4).

## Implementation notes
- Short term (during refactor): keep current 1.19.4 target while extracting ports/adapters and removing reflection where Paper API exists.
- Post-refactor: move plugin baseline to Paper 1.21.x; provide separate build variants for 1.20.6 and 1.19.4 (no NMS), or gracefully degrade features when not available.
- Messaging (Adventure) will be evaluated after the baseline refactor, per ADR-004.
