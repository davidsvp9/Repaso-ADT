package com.example.ad1_ex.coche

import com.example.ad1_ex.MyLog
import com.example.ad1_ex.Secrets

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*

class DesktopCocheRepository(
    private val client: HttpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }
) : CocheRepository {

    private val baseUrl = "https://firestore.googleapis.com/v1/projects/${Secrets.FIREBASE_KEY}/databases/(default)/documents/coches"

    //private val apiKeyParam = "?key=${Secrets.FIREBASE_KEY}"

    override suspend fun obtenerTodos(): List<Coche> {
        MyLog.d("DesktopCocheRepository.obtenerTodos")
        return try {
            val response: JsonObject = client.get(baseUrl).body()
            val documents = response["documents"]?.jsonArray ?: return emptyList()
            documents.map { doc -> doc.jsonObject.toCoche() }
        } catch (e: Exception) {
            MyLog.e("Error en obtenerTodos", e)
            emptyList()
        }
    }

    override suspend fun guardarCoche(coche: Coche) {
        MyLog.d("DesktopCocheRepository.guardarCoche: $coche")
        if (coche.id.isBlank()) {
            insertar(coche)
        } else {
            sobrescribir(coche)
        }
    }

    override suspend fun buscarListaCoche(coche: Coche): List<Coche> {
        MyLog.d("DesktopCocheRepository.buscarListaCoche: $coche")
        return obtenerTodos().filter { item ->
            (coche.matricula.isBlank() || item.matricula.contains(coche.matricula, ignoreCase = true)) &&
                    (coche.color.isBlank() || item.color.contains(coche.color, ignoreCase = true)) &&
                    (coche.modelo.isBlank() || item.modelo.contains(coche.modelo, ignoreCase = true))
        }
    }

    override suspend fun insertar(coche: Coche): List<Coche> {
        MyLog.d("DesktopCocheRepository.insertar: $coche")
        try {
            val bodyJson = cocheToJsonPayload(coche)
            client.post(baseUrl) {
                contentType(ContentType.Application.Json)
                setBody(bodyJson)
            }
        } catch (e: Exception) {
            MyLog.e("Error en insertar", e)
        }
        return obtenerTodos()
    }

    override suspend fun sobrescribir(coche: Coche): List<Coche> {
        MyLog.d("DesktopCocheRepository.sobrescribir: $coche")
        try {
            val bodyJson = cocheToJsonPayload(coche)
            client.patch("$baseUrl/${coche.id}") {
                contentType(ContentType.Application.Json)
                setBody(bodyJson)
            }
        } catch (e: Exception) {
            MyLog.e("Error en sobrescribir", e)
        }
        return obtenerTodos()
    }

    // --- JSON Builders y Mappers ---

    // JSON enviado a Firestore: { "fields": { "color": {"stringValue": "..."}, ... } }
    private fun cocheToJsonPayload(coche: Coche): JsonObject {
        return buildJsonObject {
            put("fields", buildJsonObject {
                put("color", buildJsonObject { put("stringValue", coche.color) })
                put("modelo", buildJsonObject { put("stringValue", coche.modelo) })
                put("matricula", buildJsonObject { put("stringValue", coche.matricula) })
            })
        }
    }

    // Convierte la respuesta JSON de Firestore a objeto Coche
    private fun JsonObject.toCoche(): Coche {
        val name = this["name"]?.jsonPrimitive?.content ?: ""
        val id = name.substringAfterLast("/")
        val fields = this["fields"]?.jsonObject

        val color = fields?.get("color")?.jsonObject?.get("stringValue")?.jsonPrimitive?.content ?: ""
        val modelo = fields?.get("modelo")?.jsonObject?.get("stringValue")?.jsonPrimitive?.content ?: ""
        val matricula = fields?.get("matricula")?.jsonObject?.get("stringValue")?.jsonPrimitive?.content ?: ""

        return Coche(id = id, color = color, modelo = modelo, matricula = matricula)
    }
}
actual fun getCocheRepository(): CocheRepository = DesktopCocheRepository()