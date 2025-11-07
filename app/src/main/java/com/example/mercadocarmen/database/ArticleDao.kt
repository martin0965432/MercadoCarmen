package com.example.mercadocarmen.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mercadocarmen.model.Article

@Dao
interface ArticleDao {

    @Query("SELECT * FROM articles ORDER BY dateCreated DESC")
    fun getAllArticles(): LiveData<List<Article>>

    @Query("SELECT * FROM articles WHERE id = :articleId")
    suspend fun getArticleById(articleId: Long): Article?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article): Long

    @Update
    suspend fun updateArticle(article: Article)

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("DELETE FROM articles WHERE id = :articleId")
    suspend fun deleteArticleById(articleId: Long)
}
