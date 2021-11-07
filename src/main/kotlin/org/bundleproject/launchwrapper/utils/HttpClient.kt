package org.bundleproject.launchwrapper.utils

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import java.io.File

val http = HttpClient(Apache) {
    install(JsonFeature)
}

suspend fun HttpClient.downloadFile(file: File, url: String): Boolean {
    val call = request<HttpResponse> {
        url(url)
        method = HttpMethod.Get
    }

    if (!call.status.isSuccess())
        return false

    call.content.copyAndClose(file.writeChannel())

    return true
}