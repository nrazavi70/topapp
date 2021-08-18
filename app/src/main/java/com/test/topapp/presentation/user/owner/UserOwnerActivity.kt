package com.test.topapp.presentation.user.owner

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.test.core.domain.Restaurant
import com.test.topapp.databinding.ActivityUserOwnerBinding
import com.test.topapp.presentation.restaurant.list.RestaurantListAdapter
import com.test.topapp.presentation.restaurant.manage.RestaurantManageActivity
import com.test.topapp.presentation.restaurant.page.RestaurantPageActivity
import com.test.topapp.presentation.review.list.ReviewListAdapter
import com.test.topapp.utils.Event
import com.test.topapp.utils.snackbar.SnackBarModel
import com.test.topapp.utils.snackbar.snackBarBuilder
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserOwnerActivity : AppCompatActivity() {
    private var binding: ActivityUserOwnerBinding? = null
    private val viewModel: UserOwnerActivityViewModel by viewModel()
    private val userId by lazy { intent.getIntExtra("userId", -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserOwnerBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        initView()
    }

    private fun initView() {
        val binding = requireNotNull(binding)

        binding.activityUserOwnerTab.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> viewModel.setViewEvent(Event.Info(UserOwnerActivityViewModel.requestShowRestaurant))
                    1 -> viewModel.setViewEvent(Event.Info(UserOwnerActivityViewModel.requestShowReviews))
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.activityUserOwnerRestaurantAddNewBtn.setOnClickListener {
            viewModel.setViewEvent(Event.Info(UserOwnerActivityViewModel.requestRouteToManageRestaurant))
        }

        viewModel
            .onInfo {
                when (it.type) {
                    UserOwnerActivityViewModel.showRestaurant -> showTab(0)
                    UserOwnerActivityViewModel.showReviews -> showTab(1)
                    UserOwnerActivityViewModel.userLoaded -> setupRestaurants()
                    UserOwnerActivityViewModel.routeToManageRestaurant -> startActivity(
                        Intent(this, RestaurantManageActivity::class.java)
                            .putExtra("userId", userId)
                    )
                    UserOwnerActivityViewModel.routeToRestaurant -> startActivity(
                        Intent(this, RestaurantPageActivity::class.java)
                            .putExtra("userId", userId)
                            .putExtra("restaurantId", (it.data as Restaurant).id)
                    )
                }
            }
            .onError {
                when (it.type) {
                    UserOwnerActivityViewModel.setErrorAlert -> if (it.data is SnackBarModel)
                        snackBarBuilder(binding.root, it.data)
                    else
                        snackBarBuilder(binding.root, SnackBarModel(it.exception.message!!))
                }
            }

        lifecycleScope.launchWhenStarted { viewModel.eventListener() }
        lifecycleScope.launchWhenStarted { viewModel.start(userId) }
    }

    override fun onPause() {
        lifecycleScope.launchWhenStarted { viewModel.resetEvents() }
        super.onPause()
    }

    private fun showTab(position: Int) {
        val binding = requireNotNull(binding)
        binding.activityUserOwnerRestaurantLayout.visibility =
            if (position == 0) View.VISIBLE else View.INVISIBLE
        binding.activityUserOwnerReviewsLayout.visibility =
            if (position == 1) View.VISIBLE else View.INVISIBLE
    }

    private fun setupRestaurants() {
        val binding = requireNotNull(binding)

        val restaurantAdapter = RestaurantListAdapter { viewModel.setViewEvent(it) }
        val restaurantList = binding.activityUserOwnerRestaurantList
        restaurantList.adapter = restaurantAdapter
        restaurantList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!restaurantList.canScrollVertically(1))
                    viewModel.setViewEvent(Event.Info(UserOwnerActivityViewModel.requestFetchNewRestaurant))
            }
        })
        lifecycleScope.launchWhenStarted {
            viewModel.restaurants.list.collect { restaurantAdapter.submitList(it) }
        }

        val reviewAdapter = ReviewListAdapter(viewModel.user!!) { viewModel.setViewEvent(it) }
        val reviewList = binding.activityUserOwnerReviewList
        reviewList.adapter = reviewAdapter
        reviewList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!reviewList.canScrollVertically(1))
                    viewModel.setViewEvent(Event.Info(UserOwnerActivityViewModel.requestFetchNewReview))
            }
        })
        lifecycleScope.launchWhenStarted {
            viewModel.reviews.list.collect { reviewAdapter.submitList(it) }
        }
    }
}