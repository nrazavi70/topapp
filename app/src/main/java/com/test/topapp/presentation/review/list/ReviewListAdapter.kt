package com.test.topapp.presentation.review.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.test.core.domain.Review
import com.test.core.domain.User
import com.test.topapp.databinding.ReviewListItemBinding
import com.test.topapp.utils.Event

class ReviewListAdapter(private val user: User, private val eventGate: (Event.Info) -> Unit) :
    ListAdapter<Review, ReviewListViewHolder>(ReviewListDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ReviewListViewHolder(
        ReviewListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ReviewListViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.review = item

        var user = ""
        if (item.userFirstName != null) user = item.userFirstName!!
        if (item.userLastName != null) {
            if (user.isNotEmpty()) user += " "
            user += item.userLastName
        }
        holder.binding.user = if (user.isEmpty()) "Guest" else user

        holder.binding.reviewListItem.setOnClickListener {
            eventGate(Event.Info(reviewClicked, item))
        }
    }

    companion object {
        const val reviewClicked = 3000
    }
}