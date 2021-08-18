package com.test.topapp.presentation.user.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.test.core.domain.User
import com.test.topapp.databinding.UserListItemBinding
import com.test.topapp.utils.Event

class UserListAdapter(private val eventGate: (Event.Info) -> Unit) :
    ListAdapter<User, UserListViewHolder>(UserListDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserListViewHolder(
        UserListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.user = item
        holder.binding.role = when (item.role) {
            1 -> "Owner"
            2 -> "Admin"
            else -> ""
        }

        holder.binding.userListItem.setOnClickListener { eventGate(Event.Info(userClicked, item)) }
    }

    companion object {
        const val userClicked = 1000
    }
}