package com.example.setmycartassignment.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.setmycartassignment.databinding.FragmentAddUsersBinding
import com.google.firebase.database.*

class AddUsersFragment : Fragment() {
    private lateinit var binding: FragmentAddUsersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddUsersBinding.inflate(layoutInflater, container, false)

        binding.uploadUserBtn.setOnClickListener {
            val userName = binding.userName.text.toString().trim()
            val borrowQuantity = binding.borrowQuantity.text.toString().trim()

            if (userName.isEmpty()) {
                binding.userName.requestFocus()
                Toast.makeText(context, "User Name is Mandatory", Toast.LENGTH_SHORT).show()
            } else if (borrowQuantity.isEmpty()) {
                binding.borrowQuantity.requestFocus()
                Toast.makeText(context, "Borrow Quantity is Mandatory", Toast.LENGTH_SHORT).show()
            } else {
                addDataToFirebase(userName, borrowQuantity.toInt())
            }
        }

        return binding.root
    }

    private fun addDataToFirebase(userName: String, borrowQuantity: Int) {
        val firebaseDatabase = FirebaseDatabase.getInstance().getReference("users")

        firebaseDatabase.child(userName).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // User already exists, update borrowQuantity
                    val existingQuantity = snapshot.child("borrowQuantity").getValue(Int::class.java) ?: 0
                    firebaseDatabase.child(userName).child("borrowQuantity").setValue(existingQuantity + borrowQuantity)
                } else {
                    // User does not exist, add new user
                    val usersModel = UsersModel(userName, borrowQuantity)
                    firebaseDatabase.child(userName).setValue(usersModel)
                }

                Toast.makeText(context, "User added", Toast.LENGTH_SHORT).show()

                // Clear input fields after adding data
                binding.userName.text = null
                binding.borrowQuantity.text = null
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to Upload User", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
