package com.test.topapp.presentation.user.regular

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.test.core.domain.Restaurant
import com.test.topapp.databinding.ActivityUserRegularBinding
import com.test.topapp.presentation.restaurant.list.RestaurantListAdapter
import com.test.topapp.presentation.restaurant.page.RestaurantPageActivity
import com.test.topapp.utils.Event
import com.test.topapp.utils.snackbar.SnackBarModel
import com.test.topapp.utils.snackbar.snackBarBuilder
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserRegularActivity : AppCompatActivity() {
    private var binding: ActivityUserRegularBinding? = null
    private val viewModel: UserRegularActivityViewModel by viewModel()
    private val userId by lazy { intent.getIntExtra("userId", -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserRegularBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        initView()
    }

    private fun initView() {
        val binding = requireNotNull(binding)

        viewModel
            .onInfo {
                when (it.type) {
                    UserRegularActivityViewModel.userLoaded -> setupList()
                    UserRegularActivityViewModel.routeToRestaurant -> startActivity(
                        Intent(this, RestaurantPageActivity::class.java)
                            .putExtra("userId", userId)
                            .putExtra("restaurantId", (it.data as Restaurant).id)
                    )
                }
            }
            .onError {
                when (it.type) {
                    UserRegularActivityViewModel.setErrorAlert -> if (it.data is SnackBarModel)
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

    private fun setupList() {
        val adapter = RestaurantListAdapter { viewModel.setViewEvent(it) }
        val list = binding!!.activityUserRegularList
        list.adapter = adapter
        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!list.canScrollVertically(1))
                    viewModel.setViewEvent(Event.Info(UserRegularActivityViewModel.requestFetchNewRestaurant))
            }
        })
        lifecycleScope.launchWhenStarted {
            viewModel.restaurants.list.collect { adapter.submitList(it) }
        }
    }
}