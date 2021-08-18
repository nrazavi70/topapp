package com.test.topapp.presentation.review.manage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.test.core.domain.Review
import com.test.topapp.databinding.ActivityReviewManageBinding
import com.test.topapp.presentation.restaurant.page.RestaurantPageActivity
import com.test.topapp.utils.Event
import com.test.topapp.utils.modalalert.ModalAlertModel
import com.test.topapp.utils.modalalert.modalAlertBuilder
import com.test.topapp.utils.snackbar.SnackBarModel
import com.test.topapp.utils.snackbar.snackBarBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewManageActivity : AppCompatActivity() {
    private var binding: ActivityReviewManageBinding? = null
    private val viewModel: ReviewManageActivityViewModel by viewModel()
    private val userId by lazy { intent.getIntExtra("userId", -1) }
    private val restaurantId by lazy { intent.getIntExtra("restaurantId", -1) }
    private val reviewId by lazy { intent.getIntExtra("reviewId", -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewManageBinding.inflate(layoutInflater)
        binding?.isEdit = reviewId != -1
        setContentView(binding!!.root)
        initView()
    }

    private fun initView() {
        val binding = requireNotNull(binding)

        binding.activityReviewManageRateEmpty1.setOnClickListener { requestChangeRate(1) }
        binding.activityReviewManageRateEmpty2.setOnClickListener { requestChangeRate(2) }
        binding.activityReviewManageRateEmpty3.setOnClickListener { requestChangeRate(3) }
        binding.activityReviewManageRateEmpty4.setOnClickListener { requestChangeRate(4) }
        binding.activityReviewManageRateEmpty5.setOnClickListener { requestChangeRate(5) }

        binding.activityReviewManageAction.setOnClickListener {
            viewModel.setViewEvent(
                Event.Info(ReviewManageActivityViewModel.requestSaveReview, createReviewDomain())
            )
        }

        binding.activityReviewManageDelete.setOnClickListener {
            viewModel.setViewEvent(Event.Info(ReviewManageActivityViewModel.requestDeleteReview))
        }

        viewModel
            .onInfo {
                when (it.type) {
                    ReviewManageActivityViewModel.setLoading -> setLoadingVisibility(it.data as Boolean)
                    ReviewManageActivityViewModel.reviewReady -> binding.review = it.data as Review
                    ReviewManageActivityViewModel.createModalAlert ->
                        modalAlertBuilder(binding.root.context, it.data as ModalAlertModel)
                    ReviewManageActivityViewModel.routeToRestaurant -> startActivity(
                        Intent(this, RestaurantPageActivity::class.java)
                            .putExtra("userId", userId)
                            .putExtra("restaurantId", restaurantId)
                    )
                }
            }
            .onError {
                when (it.type) {
                    ReviewManageActivityViewModel.setMessageInputError ->
                        binding.activityReviewManageReview.error = it.exception.message
                    ReviewManageActivityViewModel.setErrorAlert -> if (it.data is SnackBarModel)
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

    private fun setLoadingVisibility(visibility: Boolean) {
        binding!!.activityReviewManageLoading.visibility =
            if (visibility) View.VISIBLE else View.INVISIBLE
    }

    private fun requestChangeRate(rate: Int) = viewModel.setViewEvent(
        Event.Info(ReviewManageActivityViewModel.requestChangeRate, createReviewDomain() to rate)
    )

    private fun createReviewDomain(): Review {
        val binding = requireNotNull(binding)
        val review = binding.activityReviewManageReview.editText?.text.toString()
        val response = binding.activityReviewManageResponse.editText?.text.toString()
        val rate = when (View.VISIBLE) {
            binding.activityReviewManageRateFill5.visibility -> 5
            binding.activityReviewManageRateFill4.visibility -> 4
            binding.activityReviewManageRateFill3.visibility -> 3
            binding.activityReviewManageRateFill2.visibility -> 2
            else -> 1
        }

        return Review(
            if (reviewId >= 0) reviewId else 0,
            userId,
            restaurantId,
            rate,
            review,
            if (response.isEmpty()) null else response
        )
    }
}