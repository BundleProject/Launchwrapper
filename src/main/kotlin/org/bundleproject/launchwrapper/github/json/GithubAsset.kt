package org.bundleproject.launchwrapper.github.json

import com.google.gson.annotations.SerializedName

data class GithubAsset(
    val name: String,
    @SerializedName("browser_download_url") val browserDownloadUrl: String,
)