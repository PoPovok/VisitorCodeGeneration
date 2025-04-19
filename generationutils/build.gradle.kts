plugins {
    id(libs.plugins.kotlin.jvm.get().pluginId)
    alias(libs.plugins.kotlin.ksp)
}

dependencies {
    implementation(libs.kotlin.reflect)
    implementation(libs.ksp.symbol.processing)
    implementation(libs.kotlinPoet)
    implementation(libs.kotlinPoet.ksp)
}
