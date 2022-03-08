package com.example.news.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class Response(
	val totalResults: Int,
	val articles: MutableList<ArticlesItem>? = null,
	val status: String? = null
)
@Entity
	(
	tableName = "articles"
			)
data class ArticlesItem(
	@PrimaryKey(autoGenerate = true)
	var id: Int?=null,
	val publishedAt: String,
	val author: String? = null,
	val urlToImage: String? = null,
	val description: String? = null,
	val source: Source? = null,
	val title: String? = null,
	val url: String? = null,
	val content: String? = null
) :Serializable

data class Source(
	val name: String? = null,
	val id: Any? = null
)

