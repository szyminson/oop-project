plugins {
    java
    application
}

repositories {
    jcenter()
}

dependencies {
    implementation("com.google.guava:guava:27.0.1-jre")

    testImplementation("junit:junit:4.12")
}

application {
    mainClassName = "oop.project.App"
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = application.mainClassName
    }
}