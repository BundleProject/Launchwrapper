package org.bundleproject.launchwrapper

import java.io.File
import org.bundleproject.launchwrapper.utils.get
import org.bundleproject.launchwrapper.utils.info

suspend fun main(args: Array<String>) {
    val gameDir = File(args["gameDir"] ?: ".")

    info("Updating...")
    val version = update(gameDir)
    info("Loading...")
    val classLoader = load(gameDir, version)
    info("Launching...")
    launch(args, gameDir, classLoader)
}
