package com.test.topapp.presentation.review.response

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.test.core.domain.Review
import com.test.topapp.databinding.ActivityReviewResponseBinding
import com.test.topapp.presentation.restaurant.page.RestaurantPageActivity
import com.test.topapp.utils.Event
import com.test.topapp.utils.snackbar.SnackBarModel
import com.test.topapp.utils.snackbar.snackBarBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewResponseActivity : AppCompatActivity() {
    private var binding: ActivityReviewResponseBinding? = null
    private val viewModel: ReviewResponseActivityViewModel by viewModel()
    private val userId by lazy { intent.getIntExtra("userId", -1) }
    private val reviewId by lazy { intent.getIntExtra("reviewId", -1) }
    private val restaurantId by lazy { intent.getIntExtra("restaurantId", -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewResponseBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        initView()
    }

    private fun initView() {
        val binding = requireNotNull(binding)

        binding.activityReviewResponseAction.setOnClickListener {
            val response = binding.activityReviewResponseResponse.editText?.text.toString()
            viewModel.setViewEvent(
                Event.Info(
                    ReviewResponseActivityViewModel.requestResponseReview,
                    Review(reviewId, userId, restaurantId, -1, "", response)
                )
            )
        }

        viewModel
            .onInfo {
                when (it.type) {
                    ReviewResponseActivityViewModel.setLoading -> setLoadingVisibility(it.data as Boolean)
                    ReviewResponseActivityViewModel.reviewReady -> setupView()
                    ReviewResponseActivityViewModel.routeToRestaurant -> startActivity(
                        Intent(this, RestaurantPageActivity::class.java)
                            .putExtra("userId", userId)
                            .putExtra("restaurantId", restaurantId)
                    )
                    ReviewResponseActivityViewModel.backToRestaurant -> startActivity(
                        Intent(this, RestaurantPageActivity::class.java)
                            .putExtra("userId", userId)
                            .putExtra("restaurantId", restaurantId)
                    )
                }
            }
            .onError {
                when (it.type) {
                    ReviewResponseActivityViewModel.setResponseInputError ->
                        binding.activityReviewResponseResponse.error = it.exception.message
                    ReviewResponseActivityViewModel.setErrorAlert -> if (it.data is SnackBarModel)
                        snackBarBuilder(binding.root, it.data)
                    else
                        snackBarBuilder(binding.root, SnackBarModel(it.exception.message!!))
                }
            }

        lifecycleScope.launchWhenStarted { viewModel.eventListener() }
        lifecycleScope.launchWhenStarted { viewModel.start(userId, reviewId, restaurantId) }
    }

    override fun onPause() {
        lifecycleScope.launchWhenStarted { viewModel.resetEvents() }
        super.onPause()
    }

    private fun setupView() {
        val binding = requireNotNull(binding)
        val review = requireNotNull(viewModel.review)
        var user = ""
        if (review.userFirstName != null) user = review.userFirstName!!
        if (review.userLastName != null) {
            if (user.isNotEmpty()) user += " "
            user += review.userLastName
        }
        binding.review = review
        binding.user = if (user.isEmpty()) "Guest" else user
    }

    private fun setLoadingVisibility(visibility: Boolean) {
        binding!!.activityReviewResponseLoading.visibility =
            if (visibility) View.VISIBLE else View.INVISIBLE
    }
}