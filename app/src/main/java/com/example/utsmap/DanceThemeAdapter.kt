package com.example.utsmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

class DanceThemeAdapter(private val danceThemes: List<DanceTheme>) : RecyclerView.Adapter<DanceThemeAdapter.DanceThemeViewHolder>() {

    class DanceThemeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val danceThemeTextView: TextView = itemView.findViewById(R.id.dance_theme_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DanceThemeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dance_theme, parent, false)
        return DanceThemeViewHolder(view)
    }

    override fun onBindViewHolder(holder: DanceThemeViewHolder, position: Int) {
        val danceTheme = danceThemes[position]
        holder.danceThemeTextView.text = danceTheme.title

        // Set click listener to navigate to DetailFragment with the selected dance theme data
        holder.itemView.setOnClickListener { view ->
            val bundle = Bundle().apply {
                putString("dance_theme", danceTheme.title) // Pass dance theme title
            }
            view.findNavController().navigate(R.id.detailFragment2, bundle)
        }
    }

    override fun getItemCount(): Int {
        return danceThemes.size
    }
}
