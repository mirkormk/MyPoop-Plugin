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

    // Dipendenze locali pre-compilate (Maven non disponibile qui):
    compileOnly(files(rootProject.file("VersionsInterfaces/target/classes")))
    compileOnly(files(rootProject.file("v1_8/target/classes")))
    compileOnly(files(rootProject.file("v1_11/target/classes")))
    compileOnly(files(rootProject.file("v1_13/target/classes")))
    compileOnly(files(rootProject.file("v1_19_4/target/classes")))
}

sourceSets {
    val main by getting {
        java.setSrcDirs(listOf(
            rootProject.file("MyPoopPlugin/src/main/java").path
        ))
        resources.setSrcDirs(listOf(
            rootProject.file("MyPoopPlugin/src/main/resources").path
        ))
    }
}

// Espansione versione in plugin.yml
tasks.named<ProcessResources>("processResources") {
    filesMatching("plugin.yml") { expand("project" to project) }
}

// Packaging: includi classi dei moduli locali (me/**) per simulare shading
tasks.named<Jar>("jar") {
    archiveFileName.set("MyPoop.jar")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(rootProject.file("VersionsInterfaces/target/classes")) { include("me/**") }
    from(rootProject.file("v1_8/target/classes")) { include("me/**") }
    from(rootProject.file("v1_11/target/classes")) { include("me/**") }
    from(rootProject.file("v1_13/target/classes")) { include("me/**") }
    from(rootProject.file("v1_19_4/target/classes")) { include("me/**") }
}

// ----------------------
// Quality tooling (non bloccanti)
// ----------------------
spotless {
    java {
        googleJavaFormat("1.17.0")
        target(
            rootProject.file("MyPoopPlugin/src").path + "/**/*.java"
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

