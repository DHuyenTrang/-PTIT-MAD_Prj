//import org.jetbrains.kotlin.ir.backend.js.compile

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        flatDir {
            dirs("libs")
        }
    }
    dependencies {
        val navVersion = "2.9.7"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
    }
}
plugins {
    id("com.android.application") version "9.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
    id("com.android.library") version "9.1.0" apply false
}

//rootProject.allprojects {
//    def dir = getCurrentProjectDir()
//    repositories {
//        google()
//        jcenter()
//        maven {
//            url "$dir/libs"
//        }
//    }
//}