import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val koin_version: String by project

plugins {
    application
    kotlin("jvm") version "1.7.10"
    id("io.ktor.plugin") version "2.1.1"

    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    project.setProperty("mainClassName", mainClass.get())

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

val sshAntTask = configurations.create("sshAntTask")

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-websockets-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-gson-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-default-headers-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-cors-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-host-common-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    //KMongo
    implementation("org.litote.kmongo:kmongo:4.7.2")
    implementation("org.litote.kmongo:kmongo-core:4.7.2")
    implementation("org.litote.kmongo:kmongo-coroutine:4.7.2")

    // Koin for Ktor
    implementation("io.insert-koin:koin-ktor:$koin_version")
    // SLF4J Logger
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
    // Koin Core features
    implementation("io.insert-koin:koin-core:$koin_version")
    // Koin Test features
    testImplementation("io.insert-koin:koin-test:$koin_version")
    implementation(kotlin("stdlib-jdk8"))


    sshAntTask("org.apache.ant:ant-jsch:1.10.12")
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

tasks.withType<ShadowJar> {
    manifest {
        attributes(
            "Main-Class" to application.mainClass.get()
        )
    }
}

ant.withGroovyBuilder {
    "taskdef"(
        "name" to "scp",
        "classname" to "org.apache.tools.ant.taskdefs.optional.ssh.Scp",
        "classpath" to configurations.get("sshAntTask").asPath
    )
    "taskdef"(
        "name" to "ssh",
        "classname" to "org.apache.tools.ant.taskdefs.optional.ssh.SSHExec",
        "classpath" to configurations.get("sshAntTask").asPath
    )
}

task("deploy") {
    dependsOn("clean", "shadowJar")
    ant.withGroovyBuilder {
        doLast {
            val knownHosts = File.createTempFile("knownhosts", "txt")
            val user = project.properties["host_user"]
            val host = project.properties["host"]
            val port = project.properties["port"]
            val key = file("${project.properties["ssh_key"]}")
            val jarFileName = "com.cronos.server-$version-all.jar"
            try {
                "scp"(
                    "file" to file("build/libs/$jarFileName"),
                    "todir" to "$user@$host:${project.properties["host_app_dir"]}",
                    "port" to port,
                    "keyfile" to key,
                    "trust" to true,
                    "knownhosts" to knownHosts
                )
                "ssh"(
                    "host" to host,
                    "port" to port,
                    "username" to user,
                    "keyfile" to key,
                    "trust" to true,
                    "knownhosts" to knownHosts,
                    "command" to "mv ${project.properties["host_app_dir"]}/$jarFileName ${project.properties["host_app_dir"]}/cronos.jar"
                )
                "ssh"(
                    "host" to host,
                    "port" to port,
                    "username" to user,
                    "keyfile" to key,
                    "trust" to true,
                    "knownhosts" to knownHosts,
                    "command" to "sudo systemctl stop cronos"
                )
                "ssh"(
                    "host" to host,
                    "port" to port,
                    "username" to user,
                    "keyfile" to key,
                    "trust" to true,
                    "knownhosts" to knownHosts,
                    "command" to "sudo systemctl start cronos"
                )
            } finally {
                knownHosts.delete()
            }
        }
    }
}

