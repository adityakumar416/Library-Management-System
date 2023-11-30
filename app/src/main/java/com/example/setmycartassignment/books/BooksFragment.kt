package com.example.setmycartassignment.books

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.setmycartassignment.MainActivity
import com.example.setmycartassignment.R
import com.example.setmycartassignment.databinding.FragmentBooksBinding
import com.google.firebase.database.*

class BooksFragment : Fragment() {

    private lateinit var binding: FragmentBooksBinding
    private lateinit var booksList: ArrayList<BooksModel>
    private lateinit var showAllBooksAdapter: ShowAllBooksAdapter

    private var lastItemId: String? = null
    private val pageSize = 10 // Adjust the page size as needed

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBooksBinding.inflate(layoutInflater, container, false)
        binding.progressBar.visibility = View.VISIBLE

        binding.addBooks.setOnClickListener {
            findNavController().navigate(R.id.action_booksFragment_to_addBooksFragment)
        }

        binding.recyclerview.layoutManager = LinearLayoutManager(context)
        booksList = arrayListOf()
        showAllBooksAdapter = ShowAllBooksAdapter(booksList, requireContext())
        binding.recyclerview.adapter = showAllBooksAdapter

        (activity as MainActivity).supportActionBar?.title = context?.getString(R.string.books)

        loadBooks()

        binding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    // Load the next page when the end of the list is reached
                    loadNextPage()
                }
            }
        })

        return binding.root
    }

    private fun loadBooks() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("books").limitToFirst(pageSize)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                booksList.clear()
                Log.i(ContentValues.TAG, "User Books $snapshot")
                for (dataSnapshot in snapshot.children) {
                    val booksModel: BooksModel? = dataSnapshot.getValue(BooksModel::class.java)
                    if (booksModel != null) {
                        binding.progressBar.visibility = View.GONE
                        booksList.add(booksModel)
                        lastItemId = dataSnapshot.key // Track the last item id
                    }
                }
                showAllBooksAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadNextPage() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("books")
            .startAt(lastItemId) // Start after the last item in the current page
            .limitToFirst(pageSize)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    val booksModel: BooksModel? = dataSnapshot.getValue(BooksModel::class.java)
                    if (booksModel != null) {
                        booksList.add(booksModel)
                        lastItemId = dataSnapshot.key // Track the last item id
                    }
                }
                showAllBooksAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}
