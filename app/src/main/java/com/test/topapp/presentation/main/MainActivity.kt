package com.test.topapp.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.test.core.domain.User
import com.test.topapp.databinding.ActivityMainBinding
import com.test.topapp.presentation.user.admin.UserAdminActivity
import com.test.topapp.presentation.user.list.UserListAdapter
import com.test.topapp.presentation.user.login.LoginActivity
import com.test.topapp.presentation.user.owner.UserOwnerActivity
import com.test.topapp.presentation.user.regular.UserRegularActivity
import com.test.topapp.utils.Event
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private val viewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        initView()
    }

    private fun initView() {
        val binding = requireNotNull(binding)

        binding.activityMainAddNewAccount.setOnClickListener {
            viewModel.setViewEvent(Event.Info(MainActivityViewModel.requestRouteToLogin))
        }

        viewModel
            .onInfo {
                when (it.type) {
                    MainActivityViewModel.usersLoaded -> setupList()
                    MainActivityViewModel.routeToLogin ->
                        startActivity(Intent(this, LoginActivity::class.java))
                    MainActivityViewModel.routeToUserRegular -> startActivity(
                        Intent(this, UserRegularActivity::class.java)
                            .putExtra("userId", (it.data as User).id)
                    )
                    MainActivityViewModel.routeToUserOwner -> startActivity(
                        Intent(this, UserOwnerActivity::class.java)
                            .putExtra("userId", (it.data as User).id)
                    )
                    MainActivityViewModel.routeToUserAdmin -> startActivity(
                        Intent(this, UserAdminActivity::class.java)
                            .putExtra("userId", (it.data as User).id)
                    )
                }
            }

        lifecycleScope.launchWhenStarted { viewModel.eventListener() }
        lifecycleScope.launchWhenStarted { viewModel.start() }
    }

    override fun onPause() {
        lifecycleScope.launchWhenStarted { viewModel.resetEvents() }
        super.onPause()
    }

    private fun setupList() {
        val adapter = UserListAdapter { viewModel.setViewEvent(it) }
        binding!!.activityMainList.adapter = adapter
        lifecycleScope.launchWhenStarted { viewModel.users.collect { adapter.submitList(it) } }
    }
}