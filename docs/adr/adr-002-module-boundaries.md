# ADR 002: Module Boundaries and Dependencies

## Status
Accepted

## Context
We split the project into two Gradle modules: `mypoop-core` and `mypoop-plugin`.
To keep the domain independent and testable, we need clear rules about what goes where and who depends on whom.

## Decision
- `mypoop-core` (domain):
  - Contains business logic, value objects, and port interfaces.
  - No dependencies on Bukkit/Paper or other server APIs.
  - May depend on testing libraries for tests only (JUnit).
- `mypoop-plugin` (infrastructure):
  - Contains adapters implementing core ports using Bukkit/Paper APIs.
  - Wires the application at runtime (plugin enable/disable lifecycle).
  - Depends on `:mypoop-core`.
- Direction of dependencies: only `mypoop-plugin` depends on `:mypoop-core`. Never the opposite.
- Messaging and IO concerns must go behind ports. Adapters translate external types to domain types.

## Consequences
- Enforces separation of concerns and enables unit testing in core.
- Reduces accidental coupling to Bukkit in domain logic.
- Requires some boilerplate (ports + adapters + wiring), but pays off in maintainability and future migrations.

