package com.example.utsmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class DetailFragment : Fragment() {

    private lateinit var danceTheme: String
    private lateinit var titleEditText: EditText
    private lateinit var artistEditText: EditText
    private lateinit var addButton: Button
    private lateinit var addedDanceDetailTextView: TextView

    private val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    // Listener for Firestore updates
    private lateinit var danceDetailsListener: ListenerRegistration

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the data from the bundle
        arguments?.let {
            danceTheme = it.getString("dance_theme") ?: "Unknown Dance Theme"
        }

        // Set the dance theme in the UI
        view.findViewById<TextView>(R.id.dance_theme_text_view).text = danceTheme

        // Initialize views
        titleEditText = view.findViewById(R.id.title_edit_text)
        artistEditText = view.findViewById(R.id.artist_edit_text)
        addButton = view.findViewById(R.id.add_button)
        addedDanceDetailTextView = view.findViewById(R.id.added_dance_detail_text_view)

        // Set up the add button click listener
        addButton.setOnClickListener {
            val danceTitle = titleEditText.text.toString()
            val artist = artistEditText.text.toString()

            if (danceTitle.isNotEmpty() && artist.isNotEmpty()) {
                // Create a new dance detail object
                val newDanceDetail = DanceDetail(danceTitle, artist)

                // Store the dance title and artist in Firestore
                storeDanceDetailInFirestore(newDanceDetail)

                // Clear the input fields
                titleEditText.text.clear()
                artistEditText.text.clear()
            }
        }

        // Set up the back button
        view.findViewById<Button>(R.id.back_button).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack() // Go back to the previous fragment
        }

        // Fetch and display existing dance details
        fetchDanceDetails()
    }

    private fun storeDanceDetailInFirestore(danceDetail: DanceDetail) {
        firestore.collection("danceDetails")
            .add(danceDetail)
            .addOnSuccessListener { documentReference ->
                // Dance detail stored successfully
                println("Dance detail added with ID: ${documentReference.id}")

                // Display the added dance detail in the UI
                displayAddedDanceDetail(danceDetail)
            }
            .addOnFailureListener { e ->
                // Handle the error
                println("Error adding dance detail: ${e.message}")
            }
    }

    private fun displayAddedDanceDetail(danceDetail: DanceDetail) {
        addedDanceDetailTextView.text = "Added: ${danceDetail.title} by ${danceDetail.artist}"
    }

    private fun fetchDanceDetails() {
        danceDetailsListener = firestore.collection("danceDetails")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    println("Listen failed: ${e.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val details = StringBuilder()
                    for (document in snapshot.documents) {
                        val danceDetail = document.toObject(DanceDetail::class.java)
                        if (danceDetail != null) {
                            details.append("Dance Title: ${danceDetail.title}, Artist: ${danceDetail.artist}\n")
                        }
                    }
                    addedDanceDetailTextView.text = details.toString()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Remove listener to avoid memory leaks
        danceDetailsListener.remove()
    }
}
