plugins {
    kotlin("jvm") version "1.9.0"
    application
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":common"))
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("Main2023Kt")
}
