package com.example.ad1_ex.libro

import com.example.ad1_ex.MyLog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import kotlin.collections.copy

class AndroidLibroRepository : LibroRepository {

    private val db: FirebaseFirestore
        get() = FirebaseFirestore.getInstance()
    private val librosCollection = db.collection("libros") // Nombre de la colección en Firestore

    override suspend fun obtenerTodos(): List<Libro> {
        MyLog.d("AndroidLibroRepository.obtenerTodos")
        return try {
            val snapshot = librosCollection.get().await()
            snapshot.documents.mapNotNull { doc ->
                // Mapeamos el documento a la clase Coche e inyectamos el ID del documento
                doc.toObject(Libro::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            MyLog.e("Error al obtener todos los coches", e)
            emptyList()
        }
    }

    override suspend fun guardarLibro(libro: Libro) {
        MyLog.d("AndroidCocheRepository.guardarCoche: $libro")
        try {
            if (libro.id.isBlank()) {
                // Si no tiene ID, Firestore genera uno automático
                librosCollection.add(libro).await()
            } else {
                // Si tiene ID, sobrescribe el documento existente
                librosCollection.document(libro.id).set(libro).await()
            }
        } catch (e: Exception) {
            MyLog.e("Error al guardar coche", e)
        }
    }

    override suspend fun buscarListaLibros(libro: Libro): List<Libro> {
        MyLog.d("AndroidCocheRepository.buscarListaCoche: $libro")
        return try {
            // Ejemplo de búsqueda básica por matrícula si se especifica
            var query: Query = librosCollection
            if (libro.autor.isNotBlank()) {
                query = query.whereEqualTo("autor", libro.autor)
            }

            val snapshot = query.get().await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Libro::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            MyLog.e("Error al buscar lista de coches", e)
            emptyList()
        }
    }

    override suspend fun insertar(libro: Libro): List<Libro> {
        MyLog.d("AndroidCocheRepository.insertar: $libro")
        try {
            // Añadimos el coche (Firestore genera el ID automáticamente)
            // Si tu clase Coche tiene el campo id, puedes excluirlo o dejar que Firestore lo maneje

            librosCollection.add(libro).await()
        } catch (e: Exception) {
            MyLog.e("Error al insertar coche", e)
        }
        // Devuelve la lista actualizada tal como exige la interfaz
        return obtenerTodos()
    }

    override suspend fun sobrescribir(libro: Libro): List<Libro> {
        MyLog.d("AndroidCocheRepository.sobrescribir: $libro")
        try {
            if (libro.id.isNotBlank()) {
                /*val datosCoche = hashMapOf(
                    "matricula" to coche.matricula,
                    "modelo" to coche.modelo,
                    "color" to coche.color
                )*/
                // Sobrescribe el documento existente usando su ID de Firestore
                librosCollection.document(libro.id).set(libro).await()
            }
        } catch (e: Exception) {
            MyLog.e("Error al sobrescribir coche", e)
        }
        // Devuelve la lista actualizada
        return obtenerTodos()
    }
    override suspend fun eliminar(id: String): List<Libro> {
        MyLog.d("AndroidCocheRepository.eliminar ID: $id")
        try {
            if (id.isNotBlank()) {
                // Borra el documento en la colección "coches" usando su ID de Firestore
                librosCollection.document(id).delete().await()
            }
        } catch (e: Exception) {
            MyLog.e("Error al eliminar coche con ID: $id", e)
        }
        // Devuelve la lista actualizada para que la UI se refresque automáticamente
        return obtenerTodos()
    }
}

actual fun getLibroRepository(): LibroRepository = AndroidLibroRepository()