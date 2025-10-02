# ADR 001: Adopt Hexagonal Architecture (Ports & Adapters)

## Status
Accepted

## Context
The project started as a monolithic Bukkit plugin. We want to refactor towards a testable, modular design that separates domain logic from infrastructure concerns (Bukkit/Paper APIs, IO, etc.). This enables unit testing of core logic without server dependencies and supports future migrations (e.g., Paper, Adventure, storage backends).

## Decision
Adopt Hexagonal Architecture:
- Core module (mypoop-core): pure Java; contains domain logic and ports (interfaces) to external world. No Bukkit/Paper dependencies.
- Plugin module (mypoop-plugin): implements adapters for Bukkit/Paper and wires the application.
- Communication via well-defined ports; adapters translate between Bukkit types and core needs.

## Consequences
- Improved testability: core can be fully tested with JUnit and no server runtime.
- Clear boundaries: domain code remains independent from infrastructure.
- Slight overhead: need to define ports and adapters; initial wiring effort.
- Future-friendly: easier migration to Paper and adoption of Adventure messaging.

