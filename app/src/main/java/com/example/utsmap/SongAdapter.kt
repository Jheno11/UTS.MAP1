package com.example.utsmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

class SongAdapter(private val songs: List<Song>) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    // ViewHolder class for holding the views for each item
    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songTitleTextView: TextView = itemView.findViewById(R.id.song_title_text_view)
        val songArtistTextView: TextView = itemView.findViewById(R.id.song_artist_text_view)
    }

    // Creates a new ViewHolder when there's no existing one to recycle
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    // Binds the data to the ViewHolder
    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.songTitleTextView.text = song.title
        holder.songArtistTextView.text = song.artist

        // Set click listener to navigate to DetailFragment with the selected song data
        holder.itemView.setOnClickListener { view ->
            // Create a bundle to hold the song data
            val bundle = Bundle().apply {
                putString("song_title", song.title)
                putString("song_artist", song.artist)
            }
            view.findNavController().navigate(R.id.detailFragment2, bundle)
        }
    }


    // Returns the total number of items in the adapter
    override fun getItemCount(): Int {
        return songs.size
    }
}
