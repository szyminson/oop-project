plugins {
    java
    application
}


repositories {
    jcenter()
    maven { setUrl("https://oss.sonatype.org/content/repositories/public/") }
}

dependencies {
    implementation("com.google.guava:guava:27.0.1-jre")
    implementation("com.flowpowered:flow-noise:1.0.1-SNAPSHOT")

    testImplementation("junit:junit:4.12")
}

application {
    mainClassName = "oop.project.Main"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = application.mainClassName
    }
}