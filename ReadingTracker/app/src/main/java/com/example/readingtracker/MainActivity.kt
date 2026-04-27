package com.example.readingtracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.content.Context

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        seedBooksIfNeeded()
        seedSessionsIfNeeded()

        val btnAddBook = findViewById<Button>(R.id.btnAddBook)
        val btnAddProgress = findViewById<Button>(R.id.btnAddProgress)
        val btnBooksList = findViewById<Button>(R.id.btnBooksList)

        val btnStats = findViewById<Button>(R.id.btnStats)

        btnStats.setOnClickListener {
            startActivity(Intent(this, StatsActivity::class.java))
        }

        btnAddBook.setOnClickListener {
            startActivity(Intent(this, AddBookActivity::class.java))
        }

        btnAddProgress.setOnClickListener {
            startActivity(Intent(this, AddProgressActivity::class.java))
        }

        btnBooksList.setOnClickListener {
            startActivity(Intent(this, BooksListActivity::class.java))
        }
    }


    private fun seedBooksIfNeeded() {
        val prefs = getSharedPreferences(Storage.PREF_NAME, Context.MODE_PRIVATE)
        //val prefs = getSharedPreferences("books_pref", Context.MODE_PRIVATE)

        val isSeeded = prefs.getBoolean("seeded", false)

        if (!isSeeded) {
            val books = listOf(
                Book("The Hobbit", "J.R.R. Tolkien", 1937, 310, 120),
                Book("1984", "George Orwell", 1949, 328, 200),
                Book("Harry Potter and the Philosopher's Stone", "J.K. Rowling", 1997, 223, 223),
                Book("Clean Code", "Robert C. Martin", 2008, 464, 150),
                Book("Atomic Habits", "James Clear", 2018, 320, 80),
                Book("Dune1", "Frank Herbert", 1965, 896, 80)
            )

            Storage.saveBooks(this, books)

            prefs.edit().putBoolean("seeded", true).apply()
        }

    }

    private fun seedSessionsIfNeeded() {
        val prefs = getSharedPreferences(Storage.PREF_NAME, Context.MODE_PRIVATE)
        val isSeeded = prefs.getBoolean("sessions_seeded", false)

        if (!isSeeded) {
            val sessions = listOf(
                ReadingSession("The Hobbit", "2026-04-20", 50),
                ReadingSession("The Hobbit", "2026-04-21", 80),
                ReadingSession("The Hobbit", "2026-04-23", 120),

                ReadingSession("1984", "2026-04-20", 30),
                ReadingSession("1984", "2026-04-21", 70),

                ReadingSession("Clean Code", "2026-04-18", 40),
                ReadingSession("Clean Code", "2026-04-19", 90),
                ReadingSession("Clean Code", "2026-04-20", 150)
            )

            Storage.saveSessions(this, sessions)

            prefs.edit().putBoolean("sessions_seeded", true).apply()
        }
    }
}