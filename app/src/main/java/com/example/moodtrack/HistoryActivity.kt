package com.example.moodtrack

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MoodAdapter
    private lateinit var dbHelper: DatabaseHelper

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        // Ініціалізуємо RecyclerView
        recyclerView = findViewById(R.id.recycler_view_history)
        recyclerView.layoutManager = LinearLayoutManager(this)

        dbHelper = DatabaseHelper(this)

        // Отримуємо дані з бази даних
        val moodsList = dbHelper.getAllMoods()

        // Ініціалізуємо адаптер і передаємо йому список даних
        adapter = MoodAdapter(moodsList)
        recyclerView.adapter = adapter

        val back = findViewById<Button>(R.id.arrowBack)

        back.setOnClickListener {
            val intent = Intent (this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
