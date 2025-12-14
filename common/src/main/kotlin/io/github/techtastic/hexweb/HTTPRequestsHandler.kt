package io.github.techtastic.hexweb

import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import com.mojang.datafixers.util.Either
import io.github.techtastic.hexweb.utils.HexWebOperatorUtils
import ram.talia.moreiotas.api.casting.iota.StringIota
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*

object HTTPRequestsHandler {
    var client: HttpClient = HttpClient.newHttpClient()
    val responses = mutableMapOf<UUID, Either<HttpResponse<String>, Throwable>>()

    fun makeAndQueueRequest(uuid: UUID, url: String, headers: Array<String>?, method: String?, body: String?) {
        HexWebOperatorUtils.checkBlacklist(url)

        var builder = HttpRequest
            .newBuilder(URI.create(url))
        val method = method ?: "GET"
        val body = body?.let { HttpRequest.BodyPublishers.ofString(body) } ?: HttpRequest.BodyPublishers.noBody()
        try {
            builder = builder.method(method, body)
        } catch (ignored: IllegalArgumentException) {
            throw MishapInvalidIota.ofType(StringIota.make(method), 1, "method")
        }
        headers?.let { if (it.isNotEmpty()) builder = builder.headers(*headers) }

        queueRequest(uuid, builder.build())
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