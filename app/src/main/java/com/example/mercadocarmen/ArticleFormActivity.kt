package com.example.mercadocarmen

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.mercadocarmen.model.Article
import com.example.mercadocarmen.repository.ArticleRepository
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class ArticleFormActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var imagePreview: ImageView
    private lateinit var selectImageButton: MaterialButton
    private lateinit var titleInputLayout: TextInputLayout
    private lateinit var titleEditText: TextInputEditText
    private lateinit var descriptionInputLayout: TextInputLayout
    private lateinit var descriptionEditText: TextInputEditText
    private lateinit var priceInputLayout: TextInputLayout
    private lateinit var priceEditText: TextInputEditText
    private lateinit var saveButton: MaterialButton
    private lateinit var cancelButton: MaterialButton

    private val repository = ArticleRepository()
    private var editingArticleId: String? = null
    private var isEditMode = false
    private var selectedImageUri: Uri? = null
    private var currentImageUrl: String? = null
    private var progressDialog: ProgressDialog? = null

    // Image picker launcher
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            imagePreview.load(it) {
                crossfade(true)
                placeholder(R.drawable.controller)
            }
            Toast.makeText(this, R.string.image_selected, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_form)

        // Initialize views
        initializeViews()

        // Setup toolbar
        setupToolbar()

        // Check if editing existing article
        editingArticleId = intent.getStringExtra(ArticleManagementActivity.EXTRA_ARTICLE_ID)
        if (editingArticleId != null) {
            isEditMode = true
            toolbar.title = getString(R.string.edit_article)
            loadArticleData(editingArticleId!!)
        }

        // Setup button listeners
        setupButtons()
    }

    private fun initializeViews() {
        toolbar = findViewById(R.id.toolbar)
        imagePreview = findViewById(R.id.imagePreview)
        selectImageButton = findViewById(R.id.selectImageButton)
        titleInputLayout = findViewById(R.id.titleInputLayout)
        titleEditText = findViewById(R.id.titleEditText)
        descriptionInputLayout = findViewById(R.id.descriptionInputLayout)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        priceInputLayout = findViewById(R.id.priceInputLayout)
        priceEditText = findViewById(R.id.priceEditText)
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupButtons() {
        selectImageButton.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        saveButton.setOnClickListener {
            if (validateInputs()) {
                saveArticle()
            }
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun loadArticleData(articleId: String) {
        showLoading(getString(R.string.loading))

        lifecycleScope.launch {
            val article = repository.getArticleById(articleId)
            article?.let {
                titleEditText.setText(it.title)
                descriptionEditText.setText(it.description)
                priceEditText.setText(it.price.toString())
                currentImageUrl = it.imageUrl

                // Load existing image
                if (it.imageUrl.isNotEmpty()) {
                    imagePreview.load(it.imageUrl) {
                        crossfade(true)
                        placeholder(R.drawable.controller)
                        error(R.drawable.controller)
                    }
                }
            }
            hideLoading()
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        // Clear previous errors
        titleInputLayout.error = null
        descriptionInputLayout.error = null
        priceInputLayout.error = null

        // Validate title
        val title = titleEditText.text?.toString()?.trim()
        if (title.isNullOrEmpty()) {
            titleInputLayout.error = getString(R.string.error_title_required)
            isValid = false
        }

        // Validate description
        val description = descriptionEditText.text?.toString()?.trim()
        if (description.isNullOrEmpty()) {
            descriptionInputLayout.error = getString(R.string.error_description_required)
            isValid = false
        }

        // Validate price
        val priceText = priceEditText.text?.toString()?.trim()
        if (priceText.isNullOrEmpty()) {
            priceInputLayout.error = getString(R.string.error_price_required)
            isValid = false
        } else {
            try {
                val price = priceText.toDouble()
                if (price < 0) {
                    priceInputLayout.error = getString(R.string.error_price_invalid)
                    isValid = false
                }
            } catch (e: NumberFormatException) {
                priceInputLayout.error = getString(R.string.error_price_invalid)
                isValid = false
            }
        }

        return isValid
    }

    private fun saveArticle() {
        showLoading(getString(R.string.uploading_image))

        val title = titleEditText.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()
        val price = priceEditText.text.toString().toDouble()

        lifecycleScope.launch {
            try {
                // Upload image if a new one was selected
                var imageUrl = currentImageUrl ?: ""
                if (selectedImageUri != null) {
                    val uploadResult = repository.uploadImage(selectedImageUri!!)
                    if (uploadResult.isSuccess) {
                        // Delete old image if updating
                        repository.deleteOldImageIfNeeded(currentImageUrl, uploadResult.getOrNull())
                        imageUrl = uploadResult.getOrNull() ?: ""
                    } else {
                        hideLoading()
                        Toast.makeText(
                            this@ArticleFormActivity,
                            R.string.upload_failed,
                            Toast.LENGTH_SHORT
                        ).show()
                        return@launch
                    }
                }

                // Create or update article
                val article = Article(
                    title = title,
                    description = description,
                    price = price,
                    imageUrl = imageUrl
                )

                val result = if (isEditMode && editingArticleId != null) {
                    repository.updateArticle(editingArticleId!!, article)
                } else {
                    repository.addArticle(article)
                }

                hideLoading()

                if (result.isSuccess) {
                    Toast.makeText(
                        this@ArticleFormActivity,
                        if (isEditMode) R.string.article_updated else R.string.article_saved,
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@ArticleFormActivity,
                        "Error: ${result.exceptionOrNull()?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                hideLoading()
                Toast.makeText(
                    this@ArticleFormActivity,
                    "Error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showLoading(message: String) {
        progressDialog = ProgressDialog(this).apply {
            setMessage(message)
            setCancelable(false)
            show()
        }
    }

    private fun hideLoading() {
        progressDialog?.dismiss()
        progressDialog = null
    }

    override fun onDestroy() {
        super.onDestroy()
        hideLoading()
    }
}
