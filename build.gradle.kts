plugins {
    java
}

group = "net.tonimatas"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:5.1.0")
    implementation("ch.qos.logback:logback-classic:1.5.7")
}

tasks.jar {
    manifest {
        attributes(mapOf("Main-Class" to "dev.tonimatas.systembot.SystemBot"))
    }

    from({
        configurations.runtimeClasspath.get().filter { it.exists() }.map { if (it.isDirectory) it else zipTree(it) }
    })

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
