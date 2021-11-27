package org.bundleproject.launchwrapper

import org.bundleproject.launchwrapper.utils.*
import org.bundleproject.libversion.Version
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.functions
import kotlin.reflect.full.primaryConstructor

val fallbackEntrypoints = arrayOf(
    "net.fabricmc.loader.launch.knot.KnotClient",
    "cpw.mods.bootstraplauncher.BootstrapLauncher",
    "net.minecraft.launchwrapper.Launch",
    "org.bookmc.loader.impl.launch.Quilt",
    "com.github.glassmc.loader.client.GlassClientMain",
    "net.minecraft.client.main.Main", // just in case user installed to vanilla
)

suspend fun launch(args: Array<String>, gameDir: File, classLoader: ClassLoader) {
    (args["bundleMainClass"]?.let { Class.forName(it).kotlin } ?: findEntrypoint())?.run {
        val version = args["version"]
            ?.takeIf { it != "MultiMC5" }
            ?.let(Version::of)
        val modFolderName = "mods"

        val bundleClass = runCatching { Class.forName(bundleClassName, true, classLoader).kotlin }
            .onFailure {
                logger.error(it) { "Bundle is not in the classpath! Cannot launch bundle!" }
                return@launch
            }
            .getOrThrow()

        logger.info("Invoking constructor...")
        bundleClass.primaryConstructor?.call(gameDir, version, modFolderName)
            ?: logger.error("Couldn't invoke constructor!")

        logger.info("Starting Bundle... Goodbye, Launchwrapper...")
        bundleClass.functions.find { it.name == "start" }?.callSuspend()
    }
}

private fun findEntrypoint(): KClass<*>? {
    logger.warn("Relying on fallback entrypoint! Please re-install bundle!")
    fallbackEntrypoints.forEach {
        runCatching { Class.forName(it) }
            .onSuccess { return it.kotlin }
    }

    return null
}