package com.example.mercadocarmen.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = true)
    @get:Exclude
    val id: Long = 0,

    @DocumentId
    var documentId: String = "",  // Firestore document ID

    val title: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val dateCreated: Long = System.currentTimeMillis()
) {
    // Constructor sin argumentos requerido por Firestore
    constructor() : this(0, "", "", "", 0.0, "", System.currentTimeMillis())
}
