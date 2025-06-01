package io.github.techtastic.hexweb

import com.google.gson.JsonObject
import com.mojang.datafixers.util.Either
import io.github.techtastic.hexweb.utils.HexWebOperatorUtils
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*

object HTTPRequestsHandler {
    var client: HttpClient = HttpClient.newHttpClient()
    val responses = mutableMapOf<UUID, Either<HttpResponse<String>, Throwable>>()

    fun makeAndQueueRequest(uuid: UUID, url: String, headers: List<String>, method: String, body: JsonObject) {
        HexWebOperatorUtils.checkBlacklist(url)

        queueRequest(uuid, HttpRequest
            .newBuilder(URI.create(url))
            .method(method, HttpRequest.BodyPublishers.ofString(body.toString()))
            .headers(*headers.toTypedArray())
            .build()
        )
    }

    private fun queueRequest(uuid: UUID, req: HttpRequest) {
        client.sendAsync(req, HttpResponse.BodyHandlers.ofString()).whenComplete { res, err ->
            if (err != null)
                responses[uuid] = Either.right(err)
            else if (res != null)
                responses[uuid] = Either.left(res)
        }
    }

    fun getResponse(uuid: UUID) = this.responses[uuid]

    fun clearResponse(uuid: UUID) = this.responses.remove(uuid)
}