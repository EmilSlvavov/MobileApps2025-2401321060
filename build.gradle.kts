plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.ksp) apply false
}

val ktlint: Configuration by configurations.creating
val detektCli: Configuration by configurations.creating

dependencies {
    ktlint(libs.ktlint.cli) {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }
    detektCli(libs.detekt.cli)
}

val kotlinSources = listOf("app/src/**/*.kt", "*.kts", "app/*.kts")

tasks.register<JavaExec>("ktlintCheck") {
    group = "verification"
    description = "Checks Kotlin code style."
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args(kotlinSources)
}

tasks.register<JavaExec>("ktlintFormat") {
    group = "formatting"
    description = "Fixes Kotlin code style violations."
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
    args(listOf("-F") + kotlinSources)
}

tasks.register<JavaExec>("detekt") {
    group = "verification"
    description = "Runs static analysis on the Kotlin sources."
    classpath = detektCli
    mainClass.set("io.gitlab.arturbosch.detekt.cli.Main")
    args(
        "--input",
        "app/src",
        "--config",
        "config/detekt/detekt.yml",
        "--build-upon-default-config",
    )
}
