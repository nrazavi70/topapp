package com.test.topapp.presentation.restaurant.page

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.test.core.domain.Review
import com.test.topapp.databinding.ActivityRestaurantBinding
import com.test.topapp.presentation.main.MainActivity
import com.test.topapp.presentation.restaurant.manage.RestaurantManageActivity
import com.test.topapp.presentation.review.list.ReviewListAdapter
import com.test.topapp.presentation.review.manage.ReviewManageActivity
import com.test.topapp.presentation.review.response.ReviewResponseActivity
import com.test.topapp.utils.Event
import com.test.topapp.utils.snackbar.SnackBarModel
import com.test.topapp.utils.snackbar.snackBarBuilder
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class RestaurantPageActivity : AppCompatActivity() {
    private var binding: ActivityRestaurantBinding? = null
    private val viewModel: RestaurantPageActivityViewModel by viewModel()
    private val userId by lazy { intent.getIntExtra("userId", -1) }
    private val restaurantId by lazy { intent.getIntExtra("restaurantId", -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        initView()
    }

    private fun initView() {
        val binding = requireNotNull(binding)

        binding.activityRestaurantAction.setOnClickListener {
            viewModel.setViewEvent(Event.Info(RestaurantPageActivityViewModel.requestAction))
        }

        viewModel
            .onInfo {
                when (it.type) {
                    RestaurantPageActivityViewModel.restaurantReady -> setupRestaurant()
                    RestaurantPageActivityViewModel.routeToHome ->
                        startActivity(Intent(this, MainActivity::class.java))
                    RestaurantPageActivityViewModel.routeToResponseReview -> startActivity(
                        Intent(this, ReviewResponseActivity::class.java)
                            .putExtra("userId", userId)
                            .putExtra("restaurantId", restaurantId)
                            .putExtra("reviewId", (it.data as Review).id)
                    )
                    RestaurantPageActivityViewModel.routeToManageReview -> startActivity(
                        Intent(this, ReviewManageActivity::class.java)
                            .putExtra("userId", userId)
                            .putExtra("restaurantId", restaurantId)
                            .putExtra("reviewId", it.data as Int)
                    )
                    RestaurantPageActivityViewModel.routeToManageRestaurant -> startActivity(
                        Intent(this, RestaurantManageActivity::class.java)
                            .putExtra("userId", userId)
                            .putExtra("restaurantId", restaurantId)
                    )
                }
            }
            .onError {
                when (it.type) {
                    RestaurantPageActivityViewModel.setErrorAlert -> if (it.data is SnackBarModel)
                        snackBarBuilder(binding.root, it.data)
                    else
                        snackBarBuilder(binding.root, SnackBarModel(it.exception.message!!))
                }
            }

        lifecycleScope.launchWhenStarted { viewModel.eventListener() }
        lifecycleScope.launchWhenStarted { viewModel.start(userId, restaurantId) }
    }

    override fun onPause() {
        lifecycleScope.launchWhenStarted { viewModel.resetEvents() }
        super.onPause()
    }

    private fun setupRestaurant() {
        val binding = requireNotNull(binding)
        val user = requireNotNull(viewModel.user)
        val restaurant = requireNotNull(viewModel.restaurant)
        binding.user = user
        binding.restaurant = restaurant

        val adapter = ReviewListAdapter(user) { viewModel.setViewEvent(it) }
        val list = binding.activityRestaurantReviewList
        list.adapter = adapter
        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!list.canScrollVertically(1))
                    viewModel.setViewEvent(Event.Info(RestaurantPageActivityViewModel.requestFetchNewReviews))
            }
        })
        lifecycleScope.launchWhenStarted {
            viewModel.reviews.list.collect {
                adapter.submitList((restaurant.highestRev + restaurant.lowestRev + it).distinct())
            }
        }
    }
}