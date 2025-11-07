package com.example.mercadocarmen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.mercadocarmen.R
import com.example.mercadocarmen.model.Article
import java.text.NumberFormat
import java.util.Locale

class ArticleAdapter(
    private val onEditClick: (Article) -> Unit,
    private val onDeleteClick: (Article) -> Unit
) : ListAdapter<Article, ArticleAdapter.ArticleViewHolder>(ArticleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(view, onEditClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ArticleViewHolder(
        itemView: View,
        private val onEditClick: (Article) -> Unit,
        private val onDeleteClick: (Article) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val titleTextView: TextView = itemView.findViewById(R.id.articleTitle)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.articleDescription)
        private val priceTextView: TextView = itemView.findViewById(R.id.articlePrice)
        private val imageView: ImageView = itemView.findViewById(R.id.articleImage)
        private val editButton: ImageButton = itemView.findViewById(R.id.btnEdit)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.btnDelete)

        fun bind(article: Article) {
            titleTextView.text = article.title
            descriptionTextView.text = article.description

            // Format price with currency
            val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "MX"))
            priceTextView.text = currencyFormat.format(article.price)

            // Load image using Coil
            imageView.load(article.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.controller)
                error(R.drawable.controller)
            }

            editButton.setOnClickListener {
                onEditClick(article)
            }

            deleteButton.setOnClickListener {
                onDeleteClick(article)
            }
        }
    }

    class ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
}
