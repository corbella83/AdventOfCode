plugins {
    kotlin("jvm") version "1.9.0"
    application
}

kotlin {
    jvmToolchain(17)
}

dependencies{
    implementation(project(":common"))
    implementation("org.jgrapht:jgrapht-core:1.5.2")
}

application {
    mainClass.set("Main2023Kt")
}
