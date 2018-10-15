import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.config.AnalysisFlag.Flags.experimental
import org.jetbrains.kotlin.gradle.dsl.Coroutines
import java.net.URI


group = "org.littlegit.core"
version = "0.2.0"

plugins {
    `maven-publish`
    jacoco
    kotlin("jvm") version "1.2.31"
    id("org.jetbrains.dokka") version "0.9.16"
}

repositories {
    jcenter() 
}

dependencies {
    implementation(kotlin("stdlib", "1.2.31"))
    testImplementation("junit:junit:4.12")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:0.21")
}

val dokka by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class) {
    outputFormat = "html"
    outputDirectory = "$buildDir/javadoc"
}


val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    classifier = "javadoc"
    from(dokka)
}

val mavenWriteUrl: URI?; get() {
    val raw = System.getenv("LITTLEGIT_MAVEN_WRITE_URL")
    
    if (raw.isNullOrBlank()) {
        return null
    }

    return uri(raw)
}

jacoco {
    toolVersion = "0.7.9"
    reportsDir = file("$buildDir/jacoco")
}

publishing {
    publications {
        create("default", MavenPublication::class.java) {
            from(components["java"])
            artifact(dokkaJar)
        }
    }
    repositories {

        maven {
            url = mavenWriteUrl!!
        }
    }
}

kotlin {
    experimental.coroutines = Coroutines.ENABLE
}