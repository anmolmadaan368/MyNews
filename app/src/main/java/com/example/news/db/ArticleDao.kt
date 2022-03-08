package com.example.news.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.news.models.ArticlesItem

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article:ArticlesItem):Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<ArticlesItem>>

    @Delete
    suspend fun deleteArticle(article: ArticlesItem)

}