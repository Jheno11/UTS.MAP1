package com.example.utsmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class DetailFragment : Fragment() {
    private lateinit var songTitleTextView: TextView
    private lateinit var songArtistTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        songTitleTextView = view.findViewById(R.id.song_title_text_view)
        songArtistTextView = view.findViewById(R.id.song_artist_text_view)

        // Retrieve song data from arguments
        val songTitle = arguments?.getString("song_title")
        val songArtist = arguments?.getString("song_artist")

        // Display the song details
        songTitleTextView.text = songTitle
        songArtistTextView.text = songArtist
    }
}
