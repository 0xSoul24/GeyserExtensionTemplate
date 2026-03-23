plugins {
    kotlin("jvm") version "2.3.20"
    // Shadow plugin for creating fat/uber jars
    id("com.gradleup.shadow") version "9.4.0"
}
val id = project.property("id") as String
val extensionName = project.property("name") as String
val author = project.property("author") as String
val version = project.version as String
val geyserApiVersion = "2.9.5"

repositories {
    // Repo for the Geyser API artifact
    maven("https://repo.opencollab.dev/main/")

    // Add other repositories here
    mavenCentral()
}

dependencies {
    // Geyser API - needed for all extensions
    compileOnly("org.geysermc.geyser:api:$geyserApiVersion-SNAPSHOT")

    // Include other dependencies here - e.g. configuration libraries.
    implementation(kotlin("stdlib-jdk8"))
}

afterEvaluate {
    val idRegex = Regex("[a-z][a-z0-9-_]{0,63}")
    if (idRegex.matches(id).not()) {
        throw IllegalArgumentException(
            "Invalid extension id $id! Must only contain lowercase letters, " +
                    "and cannot start with a number."
        )
    }

    val nameRegex = Regex("^[A-Za-z_.-]+$")
    if (nameRegex.matches(extensionName).not()) {
        throw IllegalArgumentException("Invalid extension name $extensionName! Must fit regex: ${nameRegex.pattern})")
    }
}

tasks {
    // This automatically fills in the extension.yml file.
    processResources {
        filesMatching("extension.yml") {
            expand(
                "id" to id,
                "name" to extensionName,
                "api" to geyserApiVersion,
                "version" to version,
                "author" to author
            )
        }
    }
    // Disable the default plain jar so we only output a single jar file
    // and configure the shadowJar (fat jar) to not use the "-all" classifier
    // so the produced artifact matches the normal jar naming.
    named("jar") {
        enabled = false
    }

    // Ensure the shadow/fat jar task produces an artifact without the "-all" suffix
    // and make assemble depend on it so `./gradlew build`/`assemble` will produce
    // the single shadow jar.
    named<Jar>("shadowJar") {
        // Ensure the shadow/fat jar has no "-all" classifier so the artifact
        // matches the standard jar name (single output file).
        archiveClassifier.set("")
    }

    // Make assemble produce the shadow jar
    named("assemble") {
        dependsOn(named("shadowJar"))
    }
}
kotlin {
    jvmToolchain(21)
}

