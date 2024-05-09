import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("xyz.jpenilla.run-paper") version "2.2.2"
}

group = "dev.piotrulla.crafthype.titles"
version = "1.0.0"

repositories {
    gradlePluginPortal()
    mavenCentral()

    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.eternalcode.pl/releases")
    maven("https://maven.enginehub.org/repo")
    maven("https://jitpack.io")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
}

dependencies {
    // spigot api
    compileOnly("org.spigotmc:spigot-api:1.19-R0.1-SNAPSHOT")

    // messages
    implementation("net.kyori:adventure-platform-bukkit:4.3.0")
    implementation("net.kyori:adventure-text-minimessage:4.17.0")

    // configs
    implementation("net.dzikoysk:cdn:1.14.4") {
        exclude(group = "org.jetbrains.kotlin")
    }

    // database
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("com.j256.ormlite:ormlite-jdbc:6.1")

    // LuckPerms
    compileOnly("net.luckperms:api:5.4")

    // inventory framework
    implementation("dev.triumphteam:triumph-gui:3.1.7")

    // caffeine for ultra fast cache and eliminate google's guava cache bugs.
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")

    // economy provider
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")

    // database
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("com.j256.ormlite:ormlite-jdbc:6.1")

    // placeholders
    compileOnly("me.clip:placeholderapi:2.11.3")
}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

bukkit {
    main = "dev.piotrulla.crafthype.titles.TitlesPlugin"
    apiVersion = "1.13"
    prefix = "chTitles"
    author = "Piotrulla"
    name = "chTitles"
    depend = listOf("Vault", "PlaceholderAPI", "LuckPerms")
    version = "${project.version}"

    commands {
        register("title") {
            description = "Choose title"
            aliases = listOf("tytul", "tytuł")
        }
    }

    commands {
        register("titleadmin") {
            description = "Remove title"
        }
    }
}

tasks.compileJava {
    options.compilerArgs = listOf("-Xlint:deprecation", "-parameters")
    options.encoding = "UTF-8"
    options.javaModuleVersion.set("11")
}

tasks {
    runServer {
        minecraftVersion("1.20")

        downloadPlugins {
            hangar("PlaceholderAPI", "2.11.5")
            github("MilkBowl", "Vault", "1.7.3", "Vault.jar")
            url("https://github.com/akrentz6/Economy/releases/download/2.3/Economy.jar")
        }
    }
}

tasks.withType<ShadowJar> {
    archiveFileName.set("chTitles v${project.version}.jar")

    val prefix = "dev.piotrulla.crafthype.titles.libs"

    listOf(
        "org.bstats",
        "net.kyori",
        "com.j256",
        "dev.triumphteam",
        "com.github.ben-manes",
    ).forEach { pack ->
        relocate(pack, "$prefix.$pack")
    }
}