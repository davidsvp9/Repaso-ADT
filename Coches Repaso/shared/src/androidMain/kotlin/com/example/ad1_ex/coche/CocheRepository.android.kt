package com.example.ad1_ex.coche

import com.example.ad1_ex.MyLog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AndroidCocheRepository : CocheRepository {

    private val db: FirebaseFirestore
        get() = FirebaseFirestore.getInstance()
    private val cochesCollection = db.collection("coches") // Nombre de la colección en Firestore

    override suspend fun obtenerTodos(): List<Coche> {
        MyLog.d("AndroidCocheRepository.obtenerTodos")
        return try {
            val snapshot = cochesCollection.get().await()
            snapshot.documents.mapNotNull { doc ->
                // Mapeamos el documento a la clase Coche e inyectamos el ID del documento
                doc.toObject(Coche::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            MyLog.e("Error al obtener todos los coches", e)
            emptyList()
        }
    }

    override suspend fun guardarCoche(coche: Coche) {
        MyLog.d("AndroidCocheRepository.guardarCoche: $coche")
        try {
            if (coche.id.isBlank()) {
                // Si no tiene ID, Firestore genera uno automático
                cochesCollection.add(coche).await()
            } else {
                // Si tiene ID, sobrescribe el documento existente
                cochesCollection.document(coche.id).set(coche).await()
            }
        } catch (e: Exception) {
            MyLog.e("Error al guardar coche", e)
        }
    }

    override suspend fun buscarListaCoche(coche: Coche): List<Coche> {
        MyLog.d("AndroidCocheRepository.buscarListaCoche: $coche")
        return try {
            // Ejemplo de búsqueda básica por matrícula si se especifica
            var query: com.google.firebase.firestore.Query = cochesCollection
            if (coche.color.isNotBlank()) {
                query = query.whereEqualTo("color", coche.color)
            }

            val snapshot = query.get().await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Coche::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            MyLog.e("Error al buscar lista de coches", e)
            emptyList()
        }
    }

    override suspend fun insertar(coche: Coche): List<Coche> {
        MyLog.d("AndroidCocheRepository.insertar: $coche")
        try {
            // Añadimos el coche (Firestore genera el ID automáticamente)
            // Si tu clase Coche tiene el campo id, puedes excluirlo o dejar que Firestore lo maneje
            val datosCoche = hashMapOf(
                "matricula" to coche.matricula,
                "modelo" to coche.modelo,
                "color" to coche.color
            )
            cochesCollection.add(datosCoche).await()
        } catch (e: Exception) {
            MyLog.e("Error al insertar coche", e)
        }
        // Devuelve la lista actualizada tal como exige la interfaz
        return obtenerTodos()
    }

    override suspend fun sobrescribir(coche: Coche): List<Coche> {
        MyLog.d("AndroidCocheRepository.sobrescribir: $coche")
        try {
            if (coche.id.isNotBlank()) {
                val datosCoche = hashMapOf(
                    "matricula" to coche.matricula,
                    "modelo" to coche.modelo,
                    "color" to coche.color
                )
                // Sobrescribe el documento existente usando su ID de Firestore
                cochesCollection.document(coche.id).set(datosCoche).await()
            }
        } catch (e: Exception) {
            MyLog.e("Error al sobrescribir coche", e)
        }
        // Devuelve la lista actualizada
        return obtenerTodos()
    }
    override suspend fun eliminar(id: String): List<Coche> {
        MyLog.d("AndroidCocheRepository.eliminar ID: $id")
        try {
            if (id.isNotBlank()) {
                // Borra el documento en la colección "coches" usando su ID de Firestore
                cochesCollection.document(id).delete().await()
            }
        } catch (e: Exception) {
            MyLog.e("Error al eliminar coche con ID: $id", e)
        }
        // Devuelve la lista actualizada para que la UI se refresque automáticamente
        return obtenerTodos()
    }
}

actual fun getCocheRepository(): CocheRepository = AndroidCocheRepository()