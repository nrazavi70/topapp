package com.test.topapp.presentation.restaurant.manage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.test.core.domain.Restaurant
import com.test.topapp.databinding.ActivityRestaurantManageBinding
import com.test.topapp.presentation.restaurant.page.RestaurantPageActivity
import com.test.topapp.presentation.user.owner.UserOwnerActivity
import com.test.topapp.utils.Event
import com.test.topapp.utils.modalalert.ModalAlertModel
import com.test.topapp.utils.modalalert.modalAlertBuilder
import com.test.topapp.utils.snackbar.SnackBarModel
import com.test.topapp.utils.snackbar.snackBarBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class RestaurantManageActivity : AppCompatActivity() {
    private var binding: ActivityRestaurantManageBinding? = null
    private val viewModel: RestaurantManageActivityViewModel by viewModel()
    private val userId by lazy { intent.getIntExtra("userId", -1) }
    private val restaurantId by lazy { intent.getIntExtra("restaurantId", -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantManageBinding.inflate(layoutInflater)
        binding?.isEdit = restaurantId != -1
        setContentView(binding!!.root)
        initView()
    }

    private fun initView() {
        val binding = requireNotNull(binding)

        binding.activityRestaurantManageAction.setOnClickListener {
            viewModel.setViewEvent(
                Event.Info(
                    RestaurantManageActivityViewModel.requestSaveRestaurant,
                    createRestaurantDomain()
                )
            )
        }

        binding.activityRestaurantManageDelete.setOnClickListener {
            viewModel.setViewEvent(Event.Info(RestaurantManageActivityViewModel.requestDeleteRestaurant))
        }

        viewModel
            .onInfo {
                when (it.type) {
                    RestaurantManageActivityViewModel.setLoading -> setLoadingVisibility(it.data as Boolean)
                    RestaurantManageActivityViewModel.restaurantReady -> binding.restaurant =
                        (it.data as Restaurant)
                    RestaurantManageActivityViewModel.createModalAlert ->
                        modalAlertBuilder(binding.root.context, it.data as ModalAlertModel)
                    RestaurantManageActivityViewModel.routeToOwner -> startActivity(
                        Intent(this, UserOwnerActivity::class.java)
                            .putExtra("userId", userId)
                    )
                    RestaurantManageActivityViewModel.routeToRestaurant -> startActivity(
                        Intent(this, RestaurantPageActivity::class.java)
                            .putExtra("userId", userId)
                            .putExtra("restaurantId", restaurantId)
                    )
                }
            }
            .onError {
                when (it.type) {
                    RestaurantManageActivityViewModel.setNameInputError ->
                        binding.activityRestaurantManageName.error = it.exception.message
                    RestaurantManageActivityViewModel.setAddressInputError ->
                        binding.activityRestaurantManageAddress.error = it.exception.message
                    RestaurantManageActivityViewModel.setErrorAlert -> if (it.data is SnackBarModel)
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

    private fun setLoadingVisibility(visibility: Boolean) {
        binding!!.activityRestaurantManageLoading.visibility =
            if (visibility) View.VISIBLE else View.INVISIBLE
    }

    private fun createRestaurantDomain(): Restaurant {
        val binding = requireNotNull(binding)
        val name = binding.activityRestaurantManageName.editText?.text.toString()
        val address = binding.activityRestaurantManageAddress.editText?.text.toString()

        return Restaurant(
            if (restaurantId >= 0) restaurantId else 0,
            userId,
            name,
            0.1F,
            0.2F,
            address
        )
    }
}