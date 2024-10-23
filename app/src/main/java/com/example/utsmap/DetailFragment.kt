package com.example.utsmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class DetailFragment : Fragment() {

    private lateinit var songTitle: String
    private lateinit var songArtist: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the data from the bundle
        arguments?.let {
            songTitle = it.getString("song_title") ?: "Unknown Title"
            songArtist = it.getString("song_artist") ?: "Unknown Artist"
        }

        // Use songTitle and songArtist in your UI
        // For example, setting it to a TextView
        view.findViewById<TextView>(R.id.song_title_text_view).text = songTitle
        view.findViewById<TextView>(R.id.song_artist_text_view).text = songArtist
    }
}

