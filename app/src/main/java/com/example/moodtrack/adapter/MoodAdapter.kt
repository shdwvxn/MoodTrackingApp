package com.example.moodtrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moodtrack.mood.Mood

class MoodAdapter(private val moodsList: List<Mood>) :
    RecyclerView.Adapter<MoodAdapter.MoodViewHolder>() {

    class MoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val emotionTextView: TextView = itemView.findViewById(R.id.emotion_text)
        val noteTextView: TextView = itemView.findViewById(R.id.note_text)
        val dateTextView: TextView = itemView.findViewById(R.id.date_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder {
        // Інфікуйте правильний макет елемента (наприклад, item_mood.xml)
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mood, parent, false) // Змінити на ваш макет елемента
        return MoodViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
        val mood = moodsList[position]
        holder.emotionTextView.text = mood.emotion
        holder.noteTextView.text = mood.note
        holder.dateTextView.text = mood.date
    }

    override fun getItemCount() = moodsList.size
}
