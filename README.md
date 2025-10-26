# MyPoop-Plugin

[![Gradle CI](https://github.com/mirkormk/MyPoop-Plugin/actions/workflows/gradle.yml/badge.svg)](https://github.com/mirkormk/MyPoop-Plugin/actions/workflows/gradle.yml)

Visit my plugin on SpigotMC at this link
https://www.spigotmc.org/resources/mypoop.77372/

## Support Window
- Primary (baseline for new features): Paper 1.21.x (Java 21). Baseline migration in progress (see ADR-003/005).
- Transitional support: Paper 1.20.6 and 1.19.4 (Java 17 runtime). Code remains source-compatible across this window.
- No NMS modules for new versions; Paper API first, graceful degradation where needed.

See ADRs for details:
- docs/adr/adr-003-migrate-to-paper.md
- docs/adr/adr-005-multi-version-strategy.md

## Development

Local dev helpers have been added (Gradle tasks + shared IntelliJ run configurations under `.run/`).

### Gradle Tasks (module `:mypoop-plugin`)
- `copyPlugin` / `deployPlugin` : Build and copy `MyPoop.jar` into `run/server/plugins`
- `runServer` : Build + copy then start the Paper server
- `runServerNoRebuild` : Start the server without rebuilding/copying (fast restarts)
- `cleanDeployedPlugin` : Remove the deployed jar from plugins folder
- `cleanWorlds` : Delete `world`, `world_nether`, `world_the_end` (CAUTION)
- `devHelp` : Print a short help summary

Examples:
```bash
./gradlew :mypoop-plugin:runServer
./gradlew :mypoop-plugin:runServerNoRebuild
./gradlew :mypoop-plugin:copyPlugin
```

### IntelliJ Run Configurations
Shared run configs (auto-detected by IntelliJ):
- `MyPoop - Deploy Plugin`
- `MyPoop - Run Server`
- `MyPoop - Run Server (Fast)`
- `MyPoop - Clean Worlds`

If you still see old, broken configurations:
1. Open Run/Debug Configurations
2. Remove the outdated "MyPoop" ones
3. Click the Gradle tool window > refresh; the new ones appear automatically

### Folder Layout Notes
- Source code still uses transitional folders (`MyPoopPlugin/`, `VersionsInterfaces/`) wired via `sourceSets` while the migration progresses.
- The server folder lives in `run/server` (ignored by git). Drop the correct Paper jar there (currently `paper-1.19.4.jar`).

### Upgrading Paper Version
1. Replace `run/server/paper-1.19.4.jar` with the new jar
2. Update `val paperJar = "paper-<new>.jar"` in `mypoop-plugin/build.gradle.kts`
3. Re-run `./gradlew :mypoop-plugin:runServer`

### Fast Dev Loop
1. Make code changes
2. `./gradlew :mypoop-plugin:runServerNoRebuild` (if plugin already copied and you just stopped the server)
3. For code changes requiring rebuild, use `runServer` or `copyPlugin` then `/reloadmypoop` in-game (safe reload logic implemented)

---
