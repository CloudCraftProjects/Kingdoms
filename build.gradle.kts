plugins {
    `java-library`
    `maven-publish`
}

group = "tk.booky"
version = "1.7.0"

repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://jitpack.io/")
}

dependencies {
    api("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
    api("dev.jorel.commandapi:commandapi-core:6.3.0")
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications.create<MavenPublication>("maven") {
        artifactId = project.name.toLowerCase()
        from(components["java"])
    }
}

tasks.processResources {
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}
