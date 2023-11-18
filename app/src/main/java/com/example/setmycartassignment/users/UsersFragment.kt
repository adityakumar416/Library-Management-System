package com.example.setmycartassignment.users

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
import com.example.setmycartassignment.books.BooksModel
import com.example.setmycartassignment.books.ShowAllBooksAdapter
import com.example.setmycartassignment.databinding.FragmentUsersBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UsersFragment : Fragment() {
    private lateinit var binding: FragmentUsersBinding
    private lateinit var usersList: ArrayList<UsersModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsersBinding.inflate(layoutInflater, container, false)

        (activity as MainActivity).supportActionBar?.title = "Users"

        binding.progressBar.visibility = View.VISIBLE

        binding.addUsers.setOnClickListener {
            findNavController().navigate(R.id.action_usersFragment_to_addUsersFragment)
        }

        binding.recyclerview.layoutManager = LinearLayoutManager(context)
        usersList = arrayListOf()



        val databaseReference = FirebaseDatabase.getInstance().getReference("users")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                usersList.clear()
                Log.i(ContentValues.TAG, "User Books $snapshot")
                for (dataSnapshot in snapshot.children) {

                    val userModel: UsersModel? = dataSnapshot.getValue(UsersModel::class.java)

                    if (userModel != null) {
                        binding.progressBar.visibility = View.GONE

                        usersList.add(userModel)
                    }

                }


                binding.recyclerview.layoutManager = LinearLayoutManager(context,
                    RecyclerView.VERTICAL,false)

                binding.recyclerview.adapter = ShowAllUsersAdapter(usersList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,error.toString(), Toast.LENGTH_SHORT).show()
            }


        })



        return binding.root
    }

}
