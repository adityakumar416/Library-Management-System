package com.example.setmycartassignment.books

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.setmycartassignment.R
import kotlin.Int
import kotlin.toString

class ShowAllBooksAdapter(
    private val courseList: ArrayList<BooksModel>): RecyclerView.Adapter<ShowAllBooksAdapter.ViewHolder>() {

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){

        val bookName : TextView = view.findViewById(R.id.book_name)
        val quantity : TextView = view.findViewById(R.id.quantity)
        val bookItem: CardView = view.findViewById(R.id.bookItem)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.books_item,parent,false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val booksModel: BooksModel = courseList[position]

            holder.bookName.setText("Book Name : "+booksModel.bookName)
        holder.quantity.setText("Book Quantity : "+booksModel.bookQuantity)

        holder.bookItem.setOnClickListener {
            holder.itemView.findNavController().navigate(R.id.action_booksFragment_to_libraryFragment)
        }



    }



    override fun getItemCount(): Int {

            return courseList.size
    }
}



