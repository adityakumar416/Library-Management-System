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


        // Fetch data from Firebase
        getFirebaseUsers()
        getFirebaseBooks()




    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}
