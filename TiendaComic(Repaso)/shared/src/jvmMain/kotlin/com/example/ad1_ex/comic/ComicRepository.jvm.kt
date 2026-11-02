package com.example.ad1_ex.comic

import com.example.ad1_ex.MyLog
import com.example.ad1_ex.Secrets
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*

class DesktopComicRepository(
    private val client: HttpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }
) : ComicRepository {

    // Endpoint REST de Firestore para la colección "Comics"
    private val baseUrl = "https://firestore.googleapis.com/v1/projects/${Secrets.FIREBASE_KEY}/databases/(default)/documents/Comics"

    // SELECT ALL: Petición GET
    override suspend fun obtenerTodos(): List<Comic> {
        MyLog.d("DesktopComicRepository.obtenerTodos")
        return try {
            val response: JsonObject = client.get(baseUrl).body()
            val documents = response["documents"]?.jsonArray ?: return emptyList()
            documents.map { doc -> doc.jsonObject.toComic() }
        } catch (e: Exception) {
            MyLog.e("Error en obtenerTodos Desktop", e)
            emptyList()
        }
    }

    override suspend fun guardarComic(comic: Comic) {
        MyLog.d("DesktopComicRepository.guardarComic: $comic")
        if (comic.id.isBlank()) {
            insertar(comic)
        } else {
            sobrescribir(comic)
        }
    }

    override suspend fun buscarListaComic(comic: Comic): List<Comic> {
        MyLog.d("DesktopComicRepository.buscarListaComic: $comic")
        return obtenerTodos().filter { item ->
            comic.titulo.isBlank() || item.titulo.contains(comic.titulo, ignoreCase = true)
        }
    }

    // INSERT: Petición POST
    override suspend fun insertar(comic: Comic): List<Comic> {
        MyLog.d("DesktopComicRepository.insertar: $comic")
        try {
            val bodyJson = comicToJsonPayload(comic)
            client.post(baseUrl) {
                contentType(ContentType.Application.Json)
                setBody(bodyJson)
            }
        } catch (e: Exception) {
            MyLog.e("Error en insertar Desktop", e)
        }
        return obtenerTodos()
    }

    // UPDATE: Petición PATCH
    override suspend fun sobrescribir(comic: Comic): List<Comic> {
        MyLog.d("DesktopComicRepository.sobrescribir: $comic")
        try {
            val bodyJson = comicToJsonPayload(comic)
            client.patch("$baseUrl/${comic.id}") {
                contentType(ContentType.Application.Json)
                setBody(bodyJson)
            }
        } catch (e: Exception) {
            MyLog.e("Error en sobrescribir Desktop", e)
        }
        return obtenerTodos()
    }

    // DELETE: Petición DELETE
    override suspend fun eliminar(id: String): List<Comic> {
        MyLog.d("DesktopComicRepository.eliminar: $id")
        try {
            if (id.isNotBlank()) {
                client.delete("$baseUrl/$id")
            }
        } catch (e: Exception) {
            MyLog.e("Error en eliminar Desktop", e)
        }
        return obtenerTodos()
    }

    // --- Mappers de JSON ---

    // Transforma un objeto Comic al formato JSON requerido por la API REST de Firestore
    private fun comicToJsonPayload(comic: Comic): JsonObject {
        return buildJsonObject {
            put("fields", buildJsonObject {
                put("titulo", buildJsonObject { put("stringValue", comic.titulo) })
                put("precio", buildJsonObject { put("doubleValue", comic.precio) })
                put("stock", buildJsonObject { put("integerValue", comic.stock) })
            })
        }
    }

    // Transforma la respuesta JSON de Firestore REST en un objeto Comic
    private fun JsonObject.toComic(): Comic {
        val name = this["name"]?.jsonPrimitive?.content ?: ""
        val id = name.substringAfterLast("/")
        val fields = this["fields"]?.jsonObject

        val titulo = fields?.get("titulo")?.jsonObject?.get("stringValue")?.jsonPrimitive?.content ?: ""
        val precio = fields?.get("precio")?.jsonObject?.let {
            it["doubleValue"]?.jsonPrimitive?.doubleOrNull
                ?: it["integerValue"]?.jsonPrimitive?.doubleOrNull
        } ?: 0.0
        val stock = fields?.get("stock")?.jsonObject?.get("integerValue")?.jsonPrimitive?.intOrNull ?: 0

        return Comic(id = id, titulo = titulo, precio = precio, stock = stock)
    }
}

// Función 'actual' requerida por Kotlin Multiplatform para la plataforma Desktop
actual fun getComicRepository(): ComicRepository = DesktopComicRepository()