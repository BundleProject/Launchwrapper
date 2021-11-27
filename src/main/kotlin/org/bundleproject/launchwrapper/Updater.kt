package org.bundleproject.launchwrapper

import com.google.gson.Gson
import com.google.gson.JsonObject
import io.ktor.client.request.*
import org.bundleproject.launchwrapper.json.github.GithubReleases
import org.bundleproject.launchwrapper.utils.*
import java.io.File

suspend fun update(gameDir: File): String {
    val bundleFolder = File(gameDir, "bundle")
    val jarsFolder = File(bundleFolder, "jars")
    jarsFolder.mkdirs()

    logger.info("Fetching latest version from github...")
    val latestRelease = http.get<GithubReleases>("$githubApi/repos/BundleProject/Bundle/releases")
        .firstOrNull()
    val latestTagName = latestRelease?.tagName
    val latestAssets = latestRelease?.assets
    val latestDownloadUrl = latestAssets?.find { it.name == "bundle-$latestTagName.jar" }?.browserDownloadUrl

    val gson = Gson()
    val versionFile = File(bundleFolder, "version.json")

    if (latestDownloadUrl != null && latestTagName != null) {
        logger.info("Found latest version: $latestTagName")

        val latestJar = File(jarsFolder, "bundle-$latestTagName.jar")

        if (!latestJar.exists()) {
            logger.info("Downloading Bundle update...")
            http.downloadFile(latestJar, latestDownloadUrl)

            logger.info("Writing latest version to json...")
            val json = JsonObject()
            json.addProperty("latest_version", latestTagName)

            versionFile.writeText(gson.toJson(json))
        } else {
            logger.info("Bundle is already up to date!")
        }

        return latestTagName
    }

    logger.error("Failed to fetch latest release! Returning current latest version to continue safely.")
    return versionFile.readText().let {
        gson.fromJson(it, JsonObject::class.java).get("latest_version").asString
    }
}