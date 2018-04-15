import org.gradle.jvm.tasks.Jar
group = "org.example"
version = "0.0.1"

plugins {
    `build-scan`
    `maven-publish`
    kotlin("jvm") version "1.2.31"
    id("org.jetbrains.dokka") version "0.9.16"
}

repositories {
    jcenter() 
}

dependencies {
    implementation(kotlin("stdlib", "1.2.31"))
    testImplementation("junit:junit:4.12")
}

buildScan {
    setLicenseAgreementUrl("https://gradle.com/terms-of-service")
    setLicenseAgree("yes")

    publishAlways()
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

publishing {
    publications {
        create("default", MavenPublication::class.java) {
            from(components["java"])
            artifact(dokkaJar)
        }
    }
    repositories {
        maven {
            url = uri("$buildDir/repository")
        }
    }
}