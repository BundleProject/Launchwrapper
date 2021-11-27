package org.bundleproject.launchwrapper

import java.io.File
import org.bundleproject.launchwrapper.utils.get
import org.bundleproject.launchwrapper.utils.logger

suspend fun main(args: Array<String>) {
    try {
        val gameDir = File(args["gameDir"] ?: ".")

        logger.info("Updating...")
        val version = update(gameDir)
        logger.info("Loading...")
        val classLoader = load(gameDir, version)
        logger.info("Launching...")
        launch(args, gameDir, classLoader)
    } catch (e: Throwable) {
        logger.error(e) { "Failed to launch Bundle!" }
    }
}
