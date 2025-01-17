import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.serialization")
    id("com.github.gmazzo.buildconfig")
    id("suspend-transform.jvm-maven-publish")
}

//testWithEmbedded0()

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly(kotlin("compiler"))
    compileOnly(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.serialization.json)

//    compileOnly(kotlin("compiler-embeddable"))

    kapt(libs.google.auto.service)
    compileOnly(libs.google.auto.service.annotations)

    testImplementation(kotlin("stdlib"))
    testImplementation(kotlin("test-junit"))
    testImplementation(kotlin("compiler-embeddable"))
    testImplementation(kotlin("reflect"))
//    testImplementation("org.jetbrains.kotlin:kotlin-compiler-embeddable")

    testImplementation(project(":runtime:suspend-transform-annotation"))
    testImplementation(project(":runtime:suspend-transform-runtime"))

    testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.4.9")
//    testImplementation("org.bitbucket.mstrobel:procyon-compilertools:0.6.0")
//    testImplementation("com.bennyhuo.kotlin:kotlin-compile-testing-extensions:1.7.10.2-SNAPSHOT")

    testImplementation(libs.kotlinx.coroutines.core)
    // testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.6.4")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.freeCompilerArgs += listOf("-Xjvm-default=all", "-opt-in=kotlin.RequiresOptIn")

repositories {
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        mavenContent {
            snapshotsOnly()
        }
    }
}

buildConfig {
    useKotlinOutput {
        internalVisibility = true
    }
    withoutPackage()
    buildConfigField("String", "KOTLIN_PLUGIN_ID", "\"${rootProject.extra["kotlin_plugin_id"]}\"")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
