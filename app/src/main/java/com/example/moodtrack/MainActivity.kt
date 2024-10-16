package com.example.moodtrack

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.parseAsHtml
import com.example.moodtrack.R.layout.dialog_window
import java.util.Date
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.moodtrack.R.layout.activity_main
import com.example.moodtrack.mood.Mood


class MainActivity : AppCompatActivity() {

    private var selectedEmotion: String = ""
    private var note: String = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val historyButton = findViewById<ImageButton>(R.id.history)

        historyButton.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        val emotions = listOf(
            findViewById<ImageButton>(R.id.emotion1),
            findViewById<ImageButton>(R.id.emotion2),
            findViewById<ImageButton>(R.id.emotion3),
            findViewById<ImageButton>(R.id.emotion4),
            findViewById<ImageButton>(R.id.emotion5)
        )

        emotions.forEachIndexed { index, imageButton ->
            imageButton.setOnClickListener {
                selectedEmotion = when (index) {
                    0 -> "Terrible"
                    1 -> "Not good"
                    2 -> "Neutral"
                    3 -> "Good"
                    4 -> "Awesome"
                    else -> ""
                }
                showNoteDialog()
            }
        }

        }


    @SuppressLint("MissingInflatedId")
// ...

    private fun showNoteDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_window, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)

        val alertDialog = dialogBuilder.create()
        alertDialog.show()

        val saveButton = dialogView.findViewById<Button>(R.id.saveButton)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)
        val noteEditText = dialogView.findViewById<EditText>(R.id.noteEditText)


        val dbHelper = DatabaseHelper(this)

        saveButton.setOnClickListener {
            note = noteEditText.text.toString()
            val date = Date()
            dbHelper.insertData(selectedEmotion, note, date.toString())
            alertDialog.dismiss()
        }

        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }
    }



}
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "mood_tracker.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "moods"
        private const val ID = "id"
        private const val EMOTION = "emotion"
        private const val NOTE = "note"
        private const val DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable =
            "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY AUTOINCREMENT, $EMOTION TEXT, $NOTE TEXT, $DATE TEXT)"
        db?.execSQL(createTable)
        Log.d("DatabaseHelper", "Table created successfully")

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertData(emotion: String, note: String, date: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(EMOTION, emotion)
        contentValues.put(NOTE, note)
        contentValues.put(DATE, date)
        db.insert(TABLE_NAME, null, contentValues)
    }

    @SuppressLint("Range")
    fun getAllMoods(): List<Mood> {
        val moodsList = mutableListOf<Mood>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val emotion = cursor.getString(cursor.getColumnIndex(EMOTION))
                val note = cursor.getString(cursor.getColumnIndex(NOTE))
                val date = cursor.getString(cursor.getColumnIndex(DATE))
                moodsList.add(Mood(emotion, note, date))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return moodsList
    }


}








