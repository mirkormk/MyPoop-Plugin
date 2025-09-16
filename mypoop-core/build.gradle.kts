plugins {
    java
}

group = "me.spighetto"
version = "2.2.1"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

// Modulo core: nessuna dipendenza Bukkit/Paper

