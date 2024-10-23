package com.example.utsmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var editButton: Button
    private lateinit var currentUser: FirebaseUser
    private val firestore = FirebaseFirestore.getInstance()
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentUser = FirebaseAuth.getInstance().currentUser!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailEditText = view.findViewById(R.id.email_edit_text)
        usernameEditText = view.findViewById(R.id.username_edit_text)
        saveButton = view.findViewById(R.id.save_button)
        editButton = view.findViewById(R.id.edit_button)

        // Load current user's email and username from Firestore
        if (currentUser != null) {
            emailEditText.setText(currentUser.email)
            loadUserProfile()
        } else {
            emailEditText.setText("No user is logged in.")
        }

        // Set click listener for save button
        saveButton.setOnClickListener {
            saveProfile()
        }

        // Set click listener for edit button
        editButton.setOnClickListener {
            toggleEditMode()
        }
    }

    private fun loadUserProfile() {
        currentUser?.let { user ->
            val userId = user.uid
            firestore.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val username = document.getString("username")
                        usernameEditText.setText(username)
                    } else {
                        usernameEditText.setText("YourUsername") // Default value if no username is set
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to load profile: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun saveProfile() {
        val newEmail = emailEditText.text.toString()
        val newUsername = usernameEditText.text.toString()

        // Validate the inputs
        if (newEmail.isEmpty() || newUsername.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Update the user's email in Firebase Authentication
        currentUser?.updateEmail(newEmail)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Save the username to Firestore
                    saveUsernameToFirestore(newUsername)
                } else {
                    Toast.makeText(requireContext(), "Error updating email: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUsernameToFirestore(username: String) {
        currentUser?.let { user ->
            val userId = user.uid
            val userData = hashMapOf("username" to username)

            firestore.collection("users").document(userId)
                .set(userData)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Error updating profile: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
        // Exit edit mode after saving
        isEditMode = false
        toggleEditMode()
    }

    private fun toggleEditMode() {
        isEditMode = !isEditMode

        if (isEditMode) {
            // Enable the EditText fields and show the save button
            usernameEditText.isEnabled = true
            emailEditText.isEnabled = true
            saveButton.visibility = View.VISIBLE
            editButton.text = "Cancel" // Change button text to 'Cancel'
        } else {
            // Disable the EditText fields and hide the save button
            usernameEditText.isEnabled = false
            emailEditText.isEnabled = false
            saveButton.visibility = View.GONE
            editButton.text = "Edit" // Change button text back to 'Edit'
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}
