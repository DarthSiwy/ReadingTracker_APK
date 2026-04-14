package com.example.booker

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AddProgressActivity : AppCompatActivity() {

    private lateinit var books: MutableList<Book>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_progress)

        val spinner = findViewById<Spinner>(R.id.spinnerBooks)
        val etPages = findViewById<EditText>(R.id.etPagesRead)
        val btnSave = findViewById<Button>(R.id.btnSaveProgress)

        books = Storage.loadBooks(this)

        if (books.isEmpty()) {
            Toast.makeText(this, "No books available", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val titles = books.map { it.title }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            titles
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        btnSave.setOnClickListener {

            val selectedIndex = spinner.selectedItemPosition
            val pagesToAdd = etPages.text.toString().toIntOrNull() ?: 0

            val book = books[selectedIndex]

            book.currentPage += pagesToAdd

            if (book.currentPage > book.totalPages) {
                book.currentPage = book.totalPages
            }

            Storage.saveBooks(this, books)

            Toast.makeText(this, "Progress saved!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}