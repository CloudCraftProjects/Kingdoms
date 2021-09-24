import com.google.gson.Gson
import com.google.gson.JsonObject
import net.lingala.zip4j.ZipFile
import java.io.FileInputStream
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.nio.file.Files
import java.util.Properties

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("com.google.code.gson:gson:2.8.8")
        classpath("net.lingala.zip4j:zip4j:2.9.0")
    }
}

plugins {
    `java-library`
    `maven-publish`

    id("xyz.jpenilla.run-paper") version "1.0.4"
}

group = "tk.booky"
version = "1.7.0"

repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://libraries.minecraft.net/")
    maven("https://repo1.maven.org/maven2/")
    maven("https://jitpack.io/")
}

dependencies {
    api("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
    api("dev.jorel.commandapi:commandapi-core:6.3.0")
    api("com.mojang:brigadier:1.0.18")
    api("net.luckperms:api:5.3")
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

tasks {
    processResources {
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }

    runServer {
        val serverFile = runDirectory.get().file("server.jar")

        val apiUrl = "https://api.github.com"
        val ghOwner = "CloudCraftProjects"
        val ghProject = "CloudPlane"

        minecraftVersion("1.17.1")
        serverJar(serverFile.asFile)

        if (!serverFile.asFile.exists()) {
            println("Downloading $ghProject jar because it doesn't exists")

            val localProperties = Properties()
            localProperties.load(FileInputStream(project.file("local.properties")))

            var httpRequest = HttpRequest.newBuilder()
                .uri(uri("$apiUrl/repos/$ghOwner/$ghProject/actions/artifacts"))
                .build()

            val httpClient = HttpClient.newHttpClient()
            val json = Gson().fromJson(
                httpClient.send(httpRequest, BodyHandlers.ofString()).body(),
                JsonObject::class.java
            )

            val downloadUrl = json
                .getAsJsonArray("artifacts")[0].asJsonObject
                .getAsJsonPrimitive("archive_download_url").asString

            httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "token ${localProperties["github-pat"]}")
                .uri(uri(downloadUrl))
                .build()

            val url = httpClient.send(httpRequest, BodyHandlers.ofString()).headers().firstValue("location").get()
            httpRequest = HttpRequest.newBuilder(uri(url)).build()

            val serverZip = runDirectory.get().file("server.zip")
            httpClient.send(httpRequest, BodyHandlers.ofFile(serverZip.asFile.toPath()))
            ZipFile(serverZip.asFile).extractAll(runDirectory.get().asFile.absolutePath)

            val extracted = runDirectory.get().file("$ghProject-${minecraftVersion.get()}-R0.1-SNAPSHOT.jar")
            Files.move(extracted.asFile.toPath(), serverFile.asFile.toPath())

            serverZip.asFile.delete()
            println("Successfully downloaded latest $ghProject jar")
        }
    }
}
