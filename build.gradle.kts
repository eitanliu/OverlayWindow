// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
}

allprojects {
    configurations.all {
        resolutionStrategy {
            force(libs.kotlin.stdlib)
            force(libs.kotlin.stdlib.jdk7)
            force(libs.kotlin.stdlib.jdk8)
            force(libs.kotlin.reflect)
            force(libs.kotlinx.metadata)
        }
    }
}
