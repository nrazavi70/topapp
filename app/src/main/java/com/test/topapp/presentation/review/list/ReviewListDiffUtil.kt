package com.test.topapp.presentation.review.list

import androidx.recyclerview.widget.DiffUtil
import com.test.core.domain.Review

class ReviewListDiffUtil: DiffUtil.ItemCallback<Review>() {
    override fun areItemsTheSame(oldItem: Review, newItem: Review) = newItem.id == oldItem.id

    override fun areContentsTheSame(oldItem: Review, newItem: Review) = newItem == oldItem
}