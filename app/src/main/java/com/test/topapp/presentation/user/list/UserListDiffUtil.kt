package com.test.topapp.presentation.user.list

import androidx.recyclerview.widget.DiffUtil
import com.test.core.domain.User

class UserListDiffUtil : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User) = newItem.id == oldItem.id

    override fun areContentsTheSame(oldItem: User, newItem: User) = newItem == oldItem
}