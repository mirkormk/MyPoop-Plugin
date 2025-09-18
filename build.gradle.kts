// Root aggregator build: no source compilation here

plugins {
    // keep empty or apply base plugin if needed
}

group = "me.spighetto"
version = "2.2.1"

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/groups/public/")
    }
}

// Subprojects (mypoop-core, mypoop-plugin) contain their own build logic
