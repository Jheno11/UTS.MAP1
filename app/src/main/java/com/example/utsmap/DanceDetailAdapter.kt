package com.example.utsmap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DanceDetailAdapter(private val danceDetails: List<DanceDetail>) :
    RecyclerView.Adapter<DanceDetailAdapter.DanceDetailViewHolder>() {

    class DanceDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.dance_title_text_view)
        val artistTextView: TextView = itemView.findViewById(R.id.dance_artist_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DanceDetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dance_detail, parent, false)
        return DanceDetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: DanceDetailViewHolder, position: Int) {
        val danceDetail = danceDetails[position]
        holder.titleTextView.text = danceDetail.title
        holder.artistTextView.text = danceDetail.artist
    }

    override fun getItemCount(): Int {
        return danceDetails.size
    }
}
