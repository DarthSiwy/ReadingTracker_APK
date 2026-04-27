package com.example.readingtracker

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class StatsActivity : AppCompatActivity() {

    private lateinit var books: MutableList<Book>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        val spinner = findViewById<Spinner>(R.id.spinnerBooks)
        val tvStats = findViewById<TextView>(R.id.tvStats)

        books = Storage.loadBooks(this)

        if (books.isEmpty()) {
            tvStats.text = "No books available"
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

        fun updateStats(index: Int) {
            val book = books[index]

            val sessions = Storage.loadSessions(this)
                .filter { it.bookTitle == book.title }

            val progressPercent = if (book.totalPages > 0)
                (book.currentPage * 100) / book.totalPages
            else 0

            val pagesLeft = book.totalPages - book.currentPage

            val totalPagesRead = sessions.sumOf { it.page }

            val daysRead = sessions.map { it.date }.distinct().size

            val avgPerDay = if (daysRead > 0)
                totalPagesRead / daysRead
            else 0

            // 🔥 streak (prosty)
            val sortedDates = sessions.map { it.date }.distinct().sorted()

            var streak = 0
            var lastDate: java.time.LocalDate? = null

            for (dateStr in sortedDates) {
                val date = java.time.LocalDate.parse(dateStr)

                if (lastDate == null || date.minusDays(1) == lastDate) {
                    streak++
                } else {
                    streak = 1
                }

                lastDate = date
            }

            tvStats.text = """
        📖 Title: ${book.title}
        
        📊 Progress: $progressPercent%
        
        📄 Current page: ${book.currentPage} / ${book.totalPages}
        
        📉 Pages left: $pagesLeft
        
        📅 Days read: $daysRead
        
        📈 Avg pages/day: $avgPerDay
        
        🔥 Reading streak: $streak days
    """.trimIndent()
        }
        updateStats(0)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                updateStats(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}