package org.bundleproject.launchwrapper

import java.io.File
import java.net.URLClassLoader

fun load(gameDir: File, version: String): URLClassLoader {
    val bundleFolder = File(gameDir, "bundle")
    val jarsFolder = File(bundleFolder, "jars")

    val bundleJar = File(jarsFolder, "bundle-$version.jar")
    val bundleJarUrl = bundleJar.toURI().toURL()

    return URLClassLoader(arrayOf(bundleJarUrl))
}