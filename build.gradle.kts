plugins {
    id("fabric-loom") version "0.12-SNAPSHOT"
    id("io.github.juuxel.loom-quiltflower") version "1.7.3"
    id("org.quiltmc.quilt-mappings-on-loom") version "4.2.0"
}

group = "me.obsilabor"
version = "1.0.0+mc1.18.2"

repositories {
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:1.18.2")
    mappings(loom.layered {
        addLayer(quiltMappings.mappings("org.quiltmc:quilt-mappings:1.18.2+build.24:v2"))
        officialMojangMappings()
    })
    modImplementation("net.fabricmc:fabric-loader:0.14.8")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.58.0+1.18.2")
}

tasks {
    compileJava {
        options.release.set(17)
        options.encoding = "UTF-8"
    }
    processResources {
        val props = mapOf(
            "version" to project.version
        )
        inputs.properties(props)
        filesMatching("fabric.mod.json") {
            expand(props)
        }
    }
}