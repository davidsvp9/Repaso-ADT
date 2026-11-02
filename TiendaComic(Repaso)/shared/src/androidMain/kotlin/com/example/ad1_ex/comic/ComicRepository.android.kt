package com.example.ad1_ex.comic

import com.example.ad1_ex.MyLog
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class AndroidComicRepository : ComicRepository {

    private val db = Firebase.firestore
    private val comicsRef = db.collection("Comics")

    // SELECT ALL
    override suspend fun obtenerTodos(): List<Comic> {
        MyLog.d("AndroidComicRepository.obtenerTodos")
        return try {
            val snapshot = comicsRef.get().await()
            snapshot.documents.mapNotNull { doc ->
                val titulo = doc.getString("titulo") ?: ""
                val precio = doc.getDouble("precio") ?: 0.0
                val stock = doc.getLong("stock")?.toInt() ?: 0
                Comic(
                    id = doc.id,
                    titulo = titulo,
                    precio = precio,
                    stock = stock
                )
            }
        } catch (e: Exception) {
            MyLog.e("Error en obtenerTodos Android", e)
            emptyList()
        }
    }

    // INSERT (CREATE)
    override suspend fun insertar(comic: Comic): List<Comic> {
        MyLog.d("AndroidComicRepository.insertar: $comic")
        return try {
            val datosMap = mapOf(
                "titulo" to comic.titulo,
                "precio" to comic.precio,
                "stock" to comic.stock
            )
            comicsRef.add(datosMap).await()
            obtenerTodos()
        } catch (e: Exception) {
            MyLog.e("Error en insertar Android", e)
            emptyList()
        }
    }

    // UPDATE (SET)
    override suspend fun sobrescribir(comic: Comic): List<Comic> {
        MyLog.d("AndroidComicRepository.sobrescribir: $comic")
        return try {
            if (comic.id.isNotBlank()) {
                val datosMap = mapOf(
                    "titulo" to comic.titulo,
                    "precio" to comic.precio,
                    "stock" to comic.stock
                )
                comicsRef.document(comic.id).set(datosMap).await()
            }
            obtenerTodos()
        } catch (e: Exception) {
            MyLog.e("Error en sobrescribir Android", e)
            emptyList()
        }
    }

    // DELETE (Borrar por ID)
    override suspend fun eliminar(id: String): List<Comic> {
        MyLog.d("AndroidComicRepository.eliminar: $id")
        return try {
            if (id.isNotBlank()) {
                comicsRef.document(id).delete().await()
            }
            obtenerTodos()
        } catch (e: Exception) {
            MyLog.e("Error en eliminar Android", e)
            emptyList()
        }
    }

    override suspend fun guardarComic(comic: Comic) {
        if (comic.id.isBlank()) {
            insertar(comic)
        } else {
            sobrescribir(comic)
        }
    }

    override suspend fun buscarListaComic(comic: Comic): List<Comic> {
        return obtenerTodos().filter { item ->
            comic.titulo.isBlank() || item.titulo.contains(comic.titulo, ignoreCase = true)
        }
    }
}

actual fun getComicRepository(): ComicRepository = AndroidComicRepository()