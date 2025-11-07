package com.example.mercadocarmen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mercadocarmen.adapter.ArticleAdapter
import com.example.mercadocarmen.model.Article
import com.example.mercadocarmen.repository.ArticleRepository
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class ArticleManagementActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyStateLayout: LinearLayout
    private lateinit var fabAddArticle: FloatingActionButton
    private lateinit var toolbar: MaterialToolbar
    private lateinit var articleAdapter: ArticleAdapter
    private val repository = ArticleRepository()

    companion object {
        const val EXTRA_ARTICLE_ID = "extra_article_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_management)

        // Initialize views
        recyclerView = findViewById(R.id.articlesRecyclerView)
        emptyStateLayout = findViewById(R.id.emptyStateLayout)
        fabAddArticle = findViewById(R.id.fabAddArticle)
        toolbar = findViewById(R.id.toolbar)

        // Setup toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        // Setup RecyclerView
        setupRecyclerView()

        // Setup FAB
        fabAddArticle.setOnClickListener {
            openArticleForm()
        }

        // Observe articles from Firebase
        observeArticles()
    }

    private fun setupRecyclerView() {
        articleAdapter = ArticleAdapter(
            onEditClick = { article ->
                openArticleForm(article)
            },
            onDeleteClick = { article ->
                showDeleteConfirmation(article)
            }
        )

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ArticleManagementActivity)
            adapter = articleAdapter
        }
    }

    private fun observeArticles() {
        lifecycleScope.launch {
            repository.getAllArticles().collect { articles ->
                if (articles.isEmpty()) {
                    showEmptyState()
                } else {
                    showArticleList()
                }
                articleAdapter.submitList(articles)
            }
        }
    }

    private fun showEmptyState() {
        emptyStateLayout.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun showArticleList() {
        emptyStateLayout.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private fun openArticleForm(article: Article? = null) {
        val intent = Intent(this, ArticleFormActivity::class.java)
        article?.let {
            intent.putExtra(EXTRA_ARTICLE_ID, it.documentId)
        }
        startActivity(intent)
    }

    private fun showDeleteConfirmation(article: Article) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_confirmation_title)
            .setMessage(R.string.delete_confirmation_message)
            .setPositiveButton(R.string.delete_confirm) { _, _ ->
                deleteArticle(article)
            }
            .setNegativeButton(R.string.delete_cancel, null)
            .show()
    }

    private fun deleteArticle(article: Article) {
        lifecycleScope.launch {
            val result = repository.deleteArticle(article.documentId)
            if (result.isSuccess) {
                Toast.makeText(
                    this@ArticleManagementActivity,
                    R.string.article_deleted,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this@ArticleManagementActivity,
                    "Error: ${result.exceptionOrNull()?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
