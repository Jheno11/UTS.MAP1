package com.example.utsmap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListFragment : Fragment() {

    private lateinit var songTitleEditText: EditText
    private lateinit var songArtistEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var songAdapter: SongAdapter
    private val songs = mutableListOf<Song>() // List to hold song data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false) // Ensure this layout has been updated for the song list
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        songTitleEditText = view.findViewById(R.id.song_title_edit_text)
        songArtistEditText = view.findViewById(R.id.song_artist_edit_text)
        submitButton = view.findViewById(R.id.submit_button)
        recyclerView = view.findViewById(R.id.recycler_view)

        // Set up the RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        songAdapter = SongAdapter(songs)
        recyclerView.adapter = songAdapter

        // Set up click listener for the submit button
        submitButton.setOnClickListener {
            val songTitle = songTitleEditText.text.toString()
            val songArtist = songArtistEditText.text.toString()

            if (songTitle.isNotEmpty() && songArtist.isNotEmpty()) {
                // Add the song to the list
                songs.add(Song(songTitle, songArtist))

                // Notify the adapter that the data has changed
                songAdapter.notifyItemInserted(songs.size - 1)

                // Clear the input fields
                songTitleEditText.text.clear()
                songArtistEditText.text.clear()

                // Scroll to the bottom of the RecyclerView
                recyclerView.scrollToPosition(songs.size - 1)
            }
        }
    }
}
