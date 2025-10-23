import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.Delete
import org.gradle.jvm.tasks.Jar
import org.gradle.language.jvm.tasks.ProcessResources

plugins {
    java
    checkstyle
    jacoco
    id("com.diffplug.spotless") version "6.25.0"
}

group = "me.spighetto"
version = "2.2.1"

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(17)) }
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    implementation(project(":mypoop-core"))

    compileOnly("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly("javax.validation:validation-api:1.1.0.Final")
}

sourceSets {
    val main by getting {
        java.setSrcDirs(
            listOf(
                // Plugin sources (legacy layout kept during migration)
                rootProject.file("MyPoopPlugin/src/main/java").path,
                // Provide IPoop / IMessages interfaces at compile-time
                rootProject.file("VersionsInterfaces/src/main/java").path,
            ),
        )
        resources.setSrcDirs(
            listOf(
                // Only plugin resources (avoid duplicate plugin.yml)
                rootProject.file("MyPoopPlugin/src/main/resources").path,
            ),
        )
    }
}

// Expand version in plugin.yml
tasks.named<ProcessResources>("processResources") {
    filesMatching("plugin.yml") { expand("project" to project) }
}

// Packaging: build a single plugin jar including core classes (fat jar)
tasks.named<Jar>("jar") {
    archiveFileName.set("MyPoop.jar")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    // Unpack core jar into plugin jar so Paper sees a single artifact
    dependsOn(":mypoop-core:jar")
    val coreJar = project(":mypoop-core").tasks.named<Jar>("jar").flatMap { it.archiveFile }
    from(coreJar.map { zipTree(it) }) {
        exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
    }
}

// -------------------------------------------------
// Development helper tasks (local Paper test server)
// -------------------------------------------------
val pluginJarName = "MyPoop.jar"
val serverDir = rootProject.layout.projectDirectory.dir("run/server")
val pluginsDir = serverDir.dir("plugins")
val paperJar = "paper-1.19.4.jar" // Update when upgrading Paper

// Ensures plugin jar exists before copy tasks depend on it
val pluginJar = tasks.named<Jar>("jar")

tasks.register<Delete>("cleanDeployedPlugin") {
    group = "dev"
    description = "Delete deployed plugin jar from run/server/plugins"
    delete(pluginsDir.file(pluginJarName))
}

tasks.register<Copy>("copyPlugin") {
    group = "dev"
    description = "Copy freshly built plugin jar into local Paper server plugins directory"
    dependsOn(pluginJar)
    from(layout.buildDirectory.file("libs/$pluginJarName"))
    into(pluginsDir)
    doFirst { println("[dev] Copying $pluginJarName -> ${pluginsDir.asFile.absolutePath}") }
}

tasks.register("deployPlugin") {
    group = "dev"
    description = "Build (jar) and deploy (copy) the plugin into the local server"
    dependsOn("copyPlugin")
}

tasks.register<Exec>("runServer") {
    group = "dev"
    description = "Build+copy then start local Paper server"
    dependsOn("copyPlugin")
    workingDir(serverDir)
    commandLine("java", "-Xms1G", "-Xmx1G", "-jar", paperJar, "nogui")
    doFirst {
        val jarFile = serverDir.file(paperJar).asFile
        require(jarFile.exists()) { "Paper jar '$paperJar' not found in ${serverDir.asFile}. Place it there." }
        println("[dev] Starting Paper in ${serverDir.asFile.absolutePath}")
    }
}

tasks.register<Exec>("runServerNoRebuild") {
    group = "dev"
    description = "Start local Paper server without rebuilding/copying (faster restarts)"
    workingDir(serverDir)
    commandLine("java", "-Xms1G", "-Xmx1G", "-jar", paperJar, "nogui")
    doFirst { println("[dev] Starting Paper (no rebuild) in ${serverDir.asFile.absolutePath}") }
}

tasks.register<Delete>("cleanWorlds") {
    group = "dev"
    description = "Delete world folders (world, world_nether, world_the_end)"
    delete(
        serverDir.dir("world").asFile,
        serverDir.dir("world_nether").asFile,
        serverDir.dir("world_the_end").asFile,
    )
}

tasks.register("devHelp") {
    group = "dev"
    description = "List development helper tasks"
    doLast {
        println(
            """
            Dev tasks:
              * copyPlugin / deployPlugin : Build & copy plugin jar into server
              * runServer                : Build+copy then start Paper
              * runServerNoRebuild       : Start Paper without rebuild (fast)
              * cleanDeployedPlugin      : Remove deployed plugin jar
              * cleanWorlds              : Delete world folders (CAUTION)
            Typical usage:
              ./gradlew :mypoop-plugin:runServer
              ./gradlew :mypoop-plugin:runServerNoRebuild
            """.trimIndent(),
        )
    }
}

// ----------------------
// Quality tooling (non blocking)
// ----------------------
spotless {
    java {
        googleJavaFormat("1.17.0")
        target(
            rootProject.file("MyPoopPlugin/src").path + "/**/*.java",
        )
    }
    kotlinGradle {
        ktlint("1.2.1")
        target("**/*.gradle.kts")
    }
}

checkstyle {
    toolVersion = "10.12.4"
    configFile = rootProject.file("config/checkstyle/checkstyle.xml")
    isIgnoreFailures = true
}

jacoco { toolVersion = "0.8.10" }

// Do not fail the build on style violations (formatting is advisory)
tasks.matching { it.name.startsWith("spotless") && it.name.endsWith("Check") }.configureEach { enabled = false }
