package com.example.mercadocarmen.repository

import android.net.Uri
import com.example.mercadocarmen.model.Article
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class ArticleRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val articlesCollection = firestore.collection("articles")
    private val storageRef = storage.reference.child("article_images")

    companion object {
        private const val FIELD_DATE_CREATED = "dateCreated"
    }

    /**
     * Obtener todos los artículos en tiempo real
     */
    fun getAllArticles(): Flow<List<Article>> = callbackFlow {
        val listenerRegistration = articlesCollection
            .orderBy(FIELD_DATE_CREATED, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val articles = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Article::class.java)?.apply {
                        documentId = doc.id
                    }
                } ?: emptyList()

                trySend(articles)
            }

        awaitClose { listenerRegistration.remove() }
    }

    /**
     * Obtener un artículo por ID
     */
    suspend fun getArticleById(articleId: String): Article? {
        return try {
            val document = articlesCollection.document(articleId).get().await()
            document.toObject(Article::class.java)?.apply {
                documentId = document.id
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Agregar un nuevo artículo
     */
    suspend fun addArticle(article: Article): Result<String> {
        return try {
            val docRef = articlesCollection.document()
            val articleData = hashMapOf(
                "title" to article.title,
                "description" to article.description,
                "price" to article.price,
                "imageUrl" to article.imageUrl,
                "dateCreated" to article.dateCreated
            )
            docRef.set(articleData).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Actualizar un artículo existente
     */
    suspend fun updateArticle(articleId: String, article: Article): Result<Unit> {
        return try {
            val articleData = hashMapOf(
                "title" to article.title,
                "description" to article.description,
                "price" to article.price,
                "imageUrl" to article.imageUrl,
                "dateCreated" to article.dateCreated
            )
            articlesCollection.document(articleId).set(articleData).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Eliminar un artículo
     */
    suspend fun deleteArticle(articleId: String): Result<Unit> {
        return try {
            // Primero obtener el artículo para eliminar su imagen
            val article = getArticleById(articleId)
            article?.imageUrl?.let { imageUrl ->
                if (imageUrl.isNotEmpty() && imageUrl.startsWith("https://firebasestorage")) {
                    deleteImageFromUrl(imageUrl)
                }
            }

            // Eliminar el documento
            articlesCollection.document(articleId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Subir imagen a Firebase Storage
     */
    suspend fun uploadImage(imageUri: Uri): Result<String> {
        return try {
            val fileName = "${System.currentTimeMillis()}_${UUID.randomUUID()}.jpg"
            val imageRef = storageRef.child(fileName)

            imageRef.putFile(imageUri).await()
            val downloadUrl = imageRef.downloadUrl.await()

            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Eliminar imagen de Firebase Storage usando la URL
     */
    private suspend fun deleteImageFromUrl(imageUrl: String) {
        try {
            val imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
            imageRef.delete().await()
        } catch (e: Exception) {
            // Ignorar errores al eliminar imagen
        }
    }

    /**
     * Eliminar imagen anterior al actualizar
     */
    suspend fun deleteOldImageIfNeeded(oldImageUrl: String?, newImageUrl: String?) {
        if (oldImageUrl != null &&
            oldImageUrl.isNotEmpty() &&
            oldImageUrl != newImageUrl &&
            oldImageUrl.startsWith("https://firebasestorage")) {
            deleteImageFromUrl(oldImageUrl)
        }
    }
}
