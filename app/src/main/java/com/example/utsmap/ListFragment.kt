package com.example.utsmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject

class ListFragment : Fragment() {

    private lateinit var songTitleEditText: EditText
    private lateinit var songArtistEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var songAdapter: SongAdapter
    private val songs = mutableListOf<Song>() // List to hold song data

    private val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
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

        // Load songs from Firestore
        loadSongsFromFirestore()

        // Set up click listener for the submit button
        submitButton.setOnClickListener {
            val songTitle = songTitleEditText.text.toString()
            val songArtist = songArtistEditText.text.toString()

            if (songTitle.isNotEmpty() && songArtist.isNotEmpty()) {
                // Create a new song object
                val newSong = Song(songTitle, songArtist)

                // Store the song in Firestore
                storeSongInFirestore(newSong)

                // Clear the input fields
                songTitleEditText.text.clear()
                songArtistEditText.text.clear()
            }
        }
    }

    private fun storeSongInFirestore(song: Song) {
        // Create a new document with a unique ID
        firestore.collection("songs")
            .add(song)
            .addOnSuccessListener {
                // Song stored successfully
                println("Song added with ID: ${it.id}")

                // Add the song to the list and notify the adapter
                songs.add(song)
                songAdapter.notifyItemInserted(songs.size - 1)

                // Scroll to the bottom of the RecyclerView
                recyclerView.scrollToPosition(songs.size - 1)
            }
            .addOnFailureListener { e ->
                // Handle the error
                println("Error adding song: ${e.message}")
            }
    }

    private fun loadSongsFromFirestore() {
        firestore.collection("songs")
            .get()
            .addOnSuccessListener { result ->
                songs.clear() // Clear existing songs before loading
                for (document in result) {
                    val song = document.toObject<Song>()
                    songs.add(song)
                }
                songAdapter.notifyDataSetChanged() // Notify adapter of data changes
            }
            .addOnFailureListener { e ->
                // Handle the error
                println("Error fetching songs: ${e.message}")
            }
    }
}
