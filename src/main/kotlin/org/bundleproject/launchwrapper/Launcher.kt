package org.bundleproject.launchwrapper

import org.bundleproject.launchwrapper.utils.bundleClassName
import org.bundleproject.launchwrapper.utils.err
import org.bundleproject.launchwrapper.utils.get
import org.bundleproject.launchwrapper.utils.info
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.createType
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
        val modFolderName = "mods"

        val bundleClass = runCatching { Class.forName(bundleClassName, true, classLoader).kotlin }
            .onFailure {
                err("Bundle is not in the classpath! Cannot launch bundle!")
                return@launch
            }
            .getOrThrow()

        info("Invoking constructor...")
        bundleClass.constructors.find { it.typeParameters[1] == String::class.createType() }
            ?.call(gameDir, version, modFolderName)

        info("Starting Bundle... Goodbye, Launchwrapper...")
        bundleClass.functions.find { it.name == "start" }?.callSuspend()
    }
}

private fun findEntrypoint(): KClass<*>? {
    err("Relying on fallback entrypoint! Please re-install bundle!")
    fallbackEntrypoints.forEach {
        runCatching { Class.forName(it) }
            .onSuccess { return it.kotlin }
    }

    return null
}