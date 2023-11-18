package com.example.setmycartassignment.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.setmycartassignment.R

class ShowAllUsersAdapter(
    private val usersList: ArrayList<UsersModel>
) : RecyclerView.Adapter<ShowAllUsersAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView = view.findViewById(R.id.users_name)
        val totalBorrowBooks: TextView = view.findViewById(R.id.user_borrow_quantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.users_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usersModel: UsersModel = usersList[position]

        holder.userName.text = "User Name: ${usersModel.userName}"
        holder.totalBorrowBooks.text = "Total Borrow Books: ${usersModel.totalBorrowBooks}"
    }

    override fun getItemCount(): Int {
        return usersList.size
    }
}
