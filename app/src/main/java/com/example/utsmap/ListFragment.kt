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

    private lateinit var danceThemeEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var danceThemeAdapter: DanceThemeAdapter
    private val danceThemes = mutableListOf<DanceTheme>() // List to hold dance theme data

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

        danceThemeEditText = view.findViewById(R.id.dance_theme_edit_text)
        submitButton = view.findViewById(R.id.submit_button)
        recyclerView = view.findViewById(R.id.recycler_view)

        // Set up the RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        danceThemeAdapter = DanceThemeAdapter(danceThemes)
        recyclerView.adapter = danceThemeAdapter

        // Load dance themes from Firestore
        loadDanceThemesFromFirestore()

        // Set up click listener for the submit button
        submitButton.setOnClickListener {
            val danceTheme = danceThemeEditText.text.toString()

            if (danceTheme.isNotEmpty()) {
                // Create a new dance theme object
                val newDanceTheme = DanceTheme(danceTheme)

                // Store the dance theme in Firestore
                storeDanceThemeInFirestore(newDanceTheme)

                // Clear the input field
                danceThemeEditText.text.clear()
            }
        }
    }

    private fun storeDanceThemeInFirestore(danceTheme: DanceTheme) {
        firestore.collection("danceThemes")
            .add(danceTheme)
            .addOnSuccessListener {
                // Dance theme stored successfully
                println("Dance theme added with ID: ${it.id}")

                // Add the dance theme to the list and notify the adapter
                danceThemes.add(danceTheme)
                danceThemeAdapter.notifyItemInserted(danceThemes.size - 1)

                // Scroll to the bottom of the RecyclerView
                recyclerView.scrollToPosition(danceThemes.size - 1)
            }
            .addOnFailureListener { e ->
                // Handle the error
                println("Error adding dance theme: ${e.message}")
            }
    }

    private fun loadDanceThemesFromFirestore() {
        firestore.collection("danceThemes")
            .get()
            .addOnSuccessListener { result ->
                danceThemes.clear() // Clear existing dance themes before loading
                for (document in result) {
                    val danceTheme = document.toObject<DanceTheme>()
                    danceThemes.add(danceTheme)
                }
                danceThemeAdapter.notifyDataSetChanged() // Notify adapter of data changes
            }
            .addOnFailureListener { e ->
                // Handle the error
                println("Error fetching dance themes: ${e.message}")
            }
    }
}