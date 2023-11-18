package com.example.setmycartassignment.books

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.setmycartassignment.databinding.FragmentAddBooksBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddBooksFragment : Fragment() {

    private lateinit var binding: FragmentAddBooksBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentAddBooksBinding.inflate(layoutInflater, container, false)

        binding.uploadBookBtn.setOnClickListener {

            val bookName = binding.bookName.text.toString()
            val quantity = binding.quintity.text.toString()

            if (binding.bookName.text!!.isEmpty()) {
                binding.bookName.requestFocus()
                Toast.makeText(context, "Book is Mandatory", Toast.LENGTH_SHORT).show()
            } else if (binding.quintity.text!!.isEmpty()) {
                binding.quintity.requestFocus()
                Toast.makeText(context, "Book Quantity is Mandatory", Toast.LENGTH_SHORT).show()
            } else {
                addDataToFirebase(bookName, quantity.toInt())
            }
        }

        return binding.root
    }

    private fun addDataToFirebase(bookName: String, quantity: Int) {
        val firebaseDatabase = FirebaseDatabase.getInstance().getReference("books")

        firebaseDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i(ContentValues.TAG, "onSuccess Main: $snapshot")
                Toast.makeText(context, "data added", Toast.LENGTH_SHORT).show()

                val booksModel = BooksModel(
                    bookName,
                    quantity
                )
                val uploadId = booksModel.bookName
                firebaseDatabase.child(uploadId.toString()).setValue(booksModel)

                binding.bookName.text = null
                binding.quintity.text = null
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    context,
                    "Failed to Upload Book",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
