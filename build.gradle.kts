plugins {
    java
    id("org.openjfx.javafxplugin") version ("0.0.10")
    id("org.jetbrains.kotlin.jvm") version "1.5.20"
    application
}

repositories {
    mavenCentral()
    jcenter()
}

var currentOS = org.gradle.internal.os.OperatingSystem.current()
var platform = "win"
if (currentOS.isWindows) {
    platform = "win"
} else if (currentOS.isLinux) {
    platform = "linux"
} else if (currentOS.isMacOsX) {
    platform = "mac"
}

val openjfx by configurations.creating

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.google.guava:guava:29.0-jre")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.6.0")

    implementation("org.openjfx:javafx-base:14:${platform}")
    implementation("org.openjfx:javafx-graphics:14:${platform}")
    implementation("org.openjfx:javafx-controls:14:${platform}")
    implementation("org.openjfx:javafx-fxml:14:${platform}")
    implementation("org.openjfx:javafx-media:14:${platform}")
    implementation("org.openjfx:javafx-web:14:${platform}")

    openjfx("org.openjfx:javafx-base:14:${platform}")
    openjfx("org.openjfx:javafx-graphics:14:${platform}")
    openjfx("org.openjfx:javafx-controls:14:${platform}")
    openjfx("org.openjfx:javafx-fxml:14:${platform}")
    openjfx("org.openjfx:javafx-media:14:${platform}")
    openjfx("org.openjfx:javafx-web:14:${platform}")

}

javafx {
    version = "17.0.2"
    modules("javafx.controls", "javafx.fxml")
}

application {
    mainClassName = "javafx.ktl.coroutines.Main"
}
