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

    private lateinit var inputEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var inputAdapter: InputAdapter
    private val inputs = mutableListOf<Pair<String, Int>>() // Mutable list to hold inputs as pairs of text and image resource ID

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

        inputEditText = view.findViewById(R.id.input_edit_text)
        submitButton = view.findViewById(R.id.submit_button)
        recyclerView = view.findViewById(R.id.recycler_view)

        // Set up the RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        inputAdapter = InputAdapter(inputs)
        recyclerView.adapter = inputAdapter

        // Set up click listener for the submit button
        submitButton.setOnClickListener {
            // Get the text input by the user
            val inputText = inputEditText.text.toString()

            // Check if the input text is not empty
            if (inputText.isNotEmpty()) {
                // Add the input text and an image resource ID to the list
                inputs.add(Pair(inputText, R.drawable.home)) // Replace 'your_image' with your actual drawable resource

                // Notify the adapter that the data has changed
                inputAdapter.notifyItemInserted(inputs.size - 1)

                // Clear the input field
                inputEditText.text.clear()

                // Scroll to the bottom of the RecyclerView
                recyclerView.scrollToPosition(inputs.size - 1)
            }
        }
    }

    companion object {
        const val COFFEE_ID = "COFFEE_ID"
    }
}
