package org.bundleproject.launchwrapper.json.github

import com.google.gson.annotations.SerializedName

data class GithubAsset(
    val name: String,
    @SerializedName("browser_download_url") val browserDownloadUrl: String,
)