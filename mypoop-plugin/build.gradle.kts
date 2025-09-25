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
                // Sorgenti del plugin
                rootProject.file("MyPoopPlugin/src/main/java").path,
                // Forniamo IPoop/IMessages a compile-time
                rootProject.file("VersionsInterfaces/src/main/java").path,
            ),
        )
        resources.setSrcDirs(
            listOf(
                // Solo le risorse del plugin (evito duplicati di plugin.yml)
                rootProject.file("MyPoopPlugin/src/main/resources").path,
            ),
        )
    }
}

// Espansione versione in plugin.yml
tasks.named<ProcessResources>("processResources") {
    filesMatching("plugin.yml") { expand("project" to project) }
}

// Packaging: solo classi del plugin (niente moduli NMS per ora)
tasks.named<Jar>("jar") {
    archiveFileName.set("MyPoop.jar")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

// ----------------------
// Quality tooling (non bloccanti)
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

// Non bloccare build con i check di Spotless
tasks.matching { it.name.startsWith("spotless") && it.name.endsWith("Check") }.configureEach { enabled = false }
