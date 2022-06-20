plugins {
  kotlin("multiplatform") // kotlin("jvm") doesn't work well in IDEA/AndroidStudio (https://github.com/JetBrains/compose-jb/issues/22)
  id("org.jetbrains.compose")
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
  jvm {
    withJava()
  }
  sourceSets {
    named("commonMain") {
      dependencies {
      }
    }
    named("jvmMain") {
      dependencies {
        implementation(compose.desktop.currentOs)
      }
    }
    named("jvmTest") {
      dependencies {
        implementation(kotlin("test"))
      }
    }
  }
}

compose.desktop {
  application {
    mainClass = "MainKt"
  }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
  kotlinOptions.jvmTarget = "11"
}
