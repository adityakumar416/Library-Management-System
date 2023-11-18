package com.example.setmycartassignment



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.setmycartassignment.databinding.FragmentLibraryBinding
import com.google.firebase.database.*

class LibraryFragment : Fragment() {

    private lateinit var binding: FragmentLibraryBinding
    private lateinit var usersAdapter: ArrayAdapter<String>
    private lateinit var booksAdapter: ArrayAdapter<String>
    private lateinit var userNamesList: ArrayList<String>
    private lateinit var bookNamesList: ArrayList<String>
    private var selectedUserId: String? = null
    private var selectedBookId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryBinding.inflate(inflater, container, false)


        binding.borrowButton.setOnClickListener {

                // Display a toast with the selected user and book
                showToast("Borrowing ${selectedBookId} for ${selectedUserId}")
            updateBorrowedBooksForUser(selectedUserId!!)

            decrementBookQuantity(selectedBookId!!)

            }





        userNamesList = ArrayList()
        bookNamesList = ArrayList()


        usersAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, userNamesList)
        usersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        booksAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, bookNamesList)
        booksAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.userSpinner.adapter = usersAdapter
        binding.bookSpinner.adapter = booksAdapter

        binding.userSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position != 0) {
                    selectedUserId = userNamesList[position]
                    showToast("Selected user: $selectedUserId")
                } else {
                    selectedUserId = null
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle nothing selected if needed
            }
        }

        binding.bookSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position != 0) {
                    selectedBookId = bookNamesList[position]
                    showToast("Selected book: $selectedBookId")
                } else {
                    selectedBookId = null
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle nothing selected if needed
            }
        }

        // Fetch data from Firebase
        getFirebaseUsers()
        getFirebaseBooks()



        return binding.root
    }

    // Function to decrement the quantity of the selected book
    private fun decrementBookQuantity(bookName: String) {
        val bookDatabaseReference = FirebaseDatabase.getInstance().getReference("books")

        bookDatabaseReference.orderByChild("bookName").equalTo(bookName)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapshot in snapshot.children) {
                        val bookKey = dataSnapshot.key
                        val currentQuantity = dataSnapshot.child("bookQuantity").getValue(Int::class.java) ?: 0

                        val newQuantity = maxOf(0, currentQuantity - 1)

                        bookDatabaseReference.child(bookKey!!).child("bookQuantity").setValue(newQuantity)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error
                    showToast("Failed to update book quantity")
                }
            })
    }


    private fun updateBorrowedBooksForUser(userId: String) {
        val userDatabaseReference = FirebaseDatabase.getInstance().getReference("users")

        userDatabaseReference.orderByChild("userName").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapshot in snapshot.children) {
                        val userKey = dataSnapshot.key
                        val currentBooksBorrowed =
                            dataSnapshot.child("totalBorrowBooks").getValue(Int::class.java) ?: 0

                        // Update the user's totalBorrowBooks field
                        userDatabaseReference.child(userKey!!).child("totalBorrowBooks")
                            .setValue(currentBooksBorrowed + 1)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error
                    showToast("Failed to update totalBorrowBooks")
                }
            })
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun getFirebaseUsers() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("users")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userNamesList.clear()
                userNamesList.add("Select User") // Add hint
                for (dataSnapshot in snapshot.children) {
                    val userName = dataSnapshot.child("userName").getValue(String::class.java)
                    userName?.let {
                        userNamesList.add(it)
                    }
                }
                usersAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                showToast(error.toString())
            }
        })
    }

    private fun getFirebaseBooks() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("books")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookNamesList.clear()
                bookNamesList.add("Select Book") // Add hint

                for (dataSnapshot in snapshot.children) {
                    val bookName = dataSnapshot.child("bookName").getValue(String::class.java)
                    bookName?.let {
                        bookNamesList.add(it)
                    }
                }
                booksAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                showToast(error.toString())
            }
        })
    }
}
