package com.test.topapp.presentation.user.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.test.core.domain.Restaurant
import com.test.core.domain.User
import com.test.topapp.databinding.ActivityUserAdminBinding
import com.test.topapp.presentation.restaurant.list.RestaurantListAdapter
import com.test.topapp.presentation.restaurant.page.RestaurantPageActivity
import com.test.topapp.presentation.user.list.UserListAdapter
import com.test.topapp.presentation.user.register.RegisterActivity
import com.test.topapp.utils.Event
import com.test.topapp.utils.snackbar.SnackBarModel
import com.test.topapp.utils.snackbar.snackBarBuilder
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserAdminActivity : AppCompatActivity() {
    private var binding: ActivityUserAdminBinding? = null
    private val viewModel: UserAdminActivityViewModel by viewModel()
    private val userId by lazy { intent.getIntExtra("userId", -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserAdminBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        initView()
    }

    private fun initView() {
        val binding = requireNotNull(binding)

        binding.activityUserAdminTab.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> viewModel.setViewEvent(Event.Info(UserAdminActivityViewModel.requestShowUsers))
                    1 -> viewModel.setViewEvent(Event.Info(UserAdminActivityViewModel.requestShowRestaurants))
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        viewModel
            .onInfo {
                when (it.type) {
                    UserAdminActivityViewModel.userLoaded -> setupList()
                    UserAdminActivityViewModel.showUsers -> showTab(0)
                    UserAdminActivityViewModel.showRestaurants -> showTab(1)
                    UserAdminActivityViewModel.routeToRestaurant -> startActivity(
                        Intent(this, RestaurantPageActivity::class.java)
                            .putExtra("userId", userId)
                            .putExtra("restaurantId", (it.data as Restaurant).id)
                    )
                    UserAdminActivityViewModel.routeToUser -> startActivity(
                        Intent(this, RegisterActivity::class.java)
                            .putExtra("adminId", userId)
                            .putExtra("userId", (it.data as User).id)
                    )
                }
            }
            .onError {
                when (it.type) {
                    UserAdminActivityViewModel.setErrorAlert -> if (it.data is SnackBarModel)
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
        binding.activityUserAdminUsersLayout.visibility =
            if (position == 0) View.VISIBLE else View.INVISIBLE
        binding.activityUserAdminRestaurantsLayout.visibility =
            if (position == 1) View.VISIBLE else View.INVISIBLE
    }

    private fun setupList() {
        val binding = requireNotNull(binding)

        val userAdapter = UserListAdapter { viewModel.setViewEvent(it) }
        val userList = binding.activityUserAdminUsersList
        userList.adapter = userAdapter
        userList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!userList.canScrollVertically(1))
                    viewModel.setViewEvent(Event.Info(UserAdminActivityViewModel.requestFetchNewUsers))
            }
        })

        lifecycleScope.launchWhenStarted { viewModel.users.list.collect { userAdapter.submitList(it) } }

        val restaurantAdapter = RestaurantListAdapter { viewModel.setViewEvent(it) }
        val restaurantList = binding.activityUserAdminRestaurantsList
        restaurantList.adapter = restaurantAdapter
        restaurantList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!restaurantList.canScrollVertically(1))
                    viewModel.setViewEvent(Event.Info(UserAdminActivityViewModel.requestFetchNewRestaurants))
            }
        })
        lifecycleScope.launchWhenStarted {
            viewModel.restaurants.list.collect { restaurantAdapter.submitList(it) }
        }
    }
}