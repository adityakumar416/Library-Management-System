package com.example.setmycartassignment.books

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.setmycartassignment.MainActivity
import com.example.setmycartassignment.R
import com.example.setmycartassignment.databinding.FragmentBooksBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BooksFragment : Fragment() {

    private lateinit var binding: FragmentBooksBinding
    private lateinit var booksList: ArrayList<BooksModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentBooksBinding.inflate(layoutInflater, container, false)


        binding.addBooks.setOnClickListener {

            findNavController().navigate(R.id.action_booksFragment_to_addBooksFragment)
        }


        binding.recyclerview.layoutManager = LinearLayoutManager(context)
        booksList = arrayListOf()

        (activity as MainActivity).supportActionBar?.title = "Books"



        val databaseReference = FirebaseDatabase.getInstance().getReference("books")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                booksList.clear()
                Log.i(ContentValues.TAG, "User Books $snapshot")
                for (dataSnapshot in snapshot.children) {

                    val booksModel: BooksModel? = dataSnapshot.getValue(
                        BooksModel::class.java)
                    if (booksModel != null) {
                        booksList.add(booksModel)
                    }

                }


                binding.recyclerview.layoutManager = LinearLayoutManager(context,
                    RecyclerView.VERTICAL,false)

                binding.recyclerview.adapter = ShowAllBooksAdapter(booksList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,error.toString(), Toast.LENGTH_SHORT).show()
            }


        })



        return binding.root
    }

}