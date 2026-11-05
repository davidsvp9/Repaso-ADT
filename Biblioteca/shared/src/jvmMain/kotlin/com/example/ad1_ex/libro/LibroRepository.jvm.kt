package com.example.ad1_ex.libro

import com.example.ad1_ex.MyLog
import com.example.ad1_ex.Secrets

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*

class DesktopLibroRepository(
    private val client: HttpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }
) : LibroRepository {

    private val baseUrl = "https://firestore.googleapis.com/v1/projects/${Secrets.FIREBASE_KEY}/databases/(default)/documents/libros"

    //private val apiKeyParam = "?key=${Secrets.FIREBASE_KEY}"

    override suspend fun obtenerTodos(): List<Libro> {
        MyLog.d("DesktopCocheRepository.obtenerTodos")
        return try {
            val response: JsonObject = client.get(baseUrl).body()
            val documents = response["documents"]?.jsonArray ?: return emptyList()
            documents.map { doc -> doc.jsonObject.toLibro() }
        } catch (e: Exception) {
            MyLog.e("Error en obtenerTodos", e)
            emptyList()
        }
    }

    override suspend fun guardarLibro(libro: Libro) {
        MyLog.d("DesktopCocheRepository.guardarCoche: $libro")
        if (libro.id.isBlank()) {
            insertar(libro)
        } else {
            sobrescribir(libro)
        }
    }

    override suspend fun buscarListaLibros(libro: Libro): List<Libro> {
        MyLog.d("DesktopCocheRepository.buscarListaCoche: $libro")
        return obtenerTodos().filter { item ->
            (libro.isbn.isBlank() || item.isbn.contains(libro.isbn, ignoreCase = true)) &&
                    (libro.titulo.isBlank() || item.titulo.contains(libro.titulo, ignoreCase = true)) &&
                    (libro.autor.isBlank() || item.autor.contains(libro.autor, ignoreCase = true))
        }
    }

    override suspend fun insertar(libro: Libro): List<Libro> {
        MyLog.d("DesktopCocheRepository.insertar: $libro")
        try {
            val bodyJson = libroToJsonPayload(libro)
            client.post(baseUrl) {
                contentType(ContentType.Application.Json)
                setBody(bodyJson)
            }
        } catch (e: Exception) {
            MyLog.e("Error en insertar", e)
        }
        return obtenerTodos()
    }

    override suspend fun sobrescribir(libro: Libro): List<Libro> {
        MyLog.d("DesktopCocheRepository.sobrescribir: $libro")
        try {
            val bodyJson = libroToJsonPayload(libro)
            client.patch("$baseUrl/${libro.id}") {
                contentType(ContentType.Application.Json)
                setBody(bodyJson)
            }
        } catch (e: Exception) {
            MyLog.e("Error en sobrescribir", e)
        }
        return obtenerTodos()
    }

    override suspend fun eliminar(id: String): List<Libro> {
        MyLog.d("DesktopCocheRepository.eliminar ID: $id")
        try {
            if (id.isNotBlank()) {
                client.delete("$baseUrl/$id")
            }
        } catch (e: Exception) {
            MyLog.e("Error en eliminar", e)
        }
        return obtenerTodos()
    }

    // --- JSON Builders y Mappers ---

    // JSON enviado a Firestore: { "fields": { "color": {"stringValue": "..."}, ... } }
    private fun libroToJsonPayload(libro: Libro): JsonObject {
        return buildJsonObject {
            put("fields", buildJsonObject {
                put("isbn", buildJsonObject { put("stringValue", libro.isbn) })
                put("titulo", buildJsonObject { put("stringValue", libro.titulo) })
                put("autor", buildJsonObject { put("stringValue", libro.autor) })
                put("paginas", buildJsonObject { put("integerValue", libro.paginas) })
                put("precio", buildJsonObject { put("doubleValue", libro.precio) })
                put("disponible", buildJsonObject { put("booleanValue", libro.disponible) })
            })
        }
    }

    // Convierte la respuesta JSON de Firestore a objeto Coche
    private fun JsonObject.toLibro(): Libro {
        val name = this["name"]?.jsonPrimitive?.content ?: ""
        val id = name.substringAfterLast("/")
        val fields = this["fields"]?.jsonObject

        val isbn = fields?.get("isbn")?.jsonObject?.get("stringValue")?.jsonPrimitive?.content ?: ""
        val titulo = fields?.get("titulo")?.jsonObject?.get("stringValue")?.jsonPrimitive?.content ?: ""
        val autor = fields?.get("autor")?.jsonObject?.get("stringValue")?.jsonPrimitive?.content ?: ""

        val paginasStr = fields?.get("paginas")?.jsonObject?.get("integerValue")?.jsonPrimitive?.content ?: "0"
        val paginas = paginasStr.toIntOrNull() ?: 0

        val precioNum = fields?.get("precio")?.jsonObject?.get("doubleValue")?.jsonPrimitive?.doubleOrNull
            ?: fields?.get("precio")?.jsonObject?.get("integerValue")?.jsonPrimitive?.intOrNull?.toDouble()
            ?: 0.0

        val disponible = fields?.get("disponible")?.jsonObject?.get("booleanValue")?.jsonPrimitive?.booleanOrNull ?: false
        return Libro(id = id, isbn = isbn, titulo = titulo, autor = autor, paginas = paginas, precio = precioNum, disponible = disponible)
    }
}
actual fun getLibroRepository(): LibroRepository = DesktopLibroRepository()