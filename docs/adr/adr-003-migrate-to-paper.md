# ADR 003: Migrate to Paper as primary API and define support window

## Status
Proposed

## Context
Spigot NMS and CraftBukkit internals make multi-version maintenance costly and brittle. Paper provides stronger APIs, performance, and community support. The project currently relies on ad-hoc per-version classes and reflection.

## Decision
- Target Paper API as the primary runtime API for the plugin.
- Drop direct NMS dependencies in favor of Paper API wherever possible.
- Define a support window: latest Paper major + previous minor (e.g., 1 latest LTS or last 2 releases).
- For truly version-specific behavior, isolate adapters behind ports and prefer API-based feature flags; reflection only as a temporary bridge.

## Consequences
- Simplifies maintenance; reduces breakage across versions.
- Users on very old server versions may no longer be supported (documented in README/CHANGELOG).
- Enables modern tooling (paperweight) if low-level hooks are required.

