package com.test.topapp.presentation.user.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.test.core.domain.User
import com.test.topapp.databinding.ActivityRegisterBinding
import com.test.topapp.presentation.main.MainActivity
import com.test.topapp.presentation.user.admin.UserAdminActivity
import com.test.topapp.presentation.user.login.LoginActivity
import com.test.topapp.utils.Event
import com.test.topapp.utils.modalalert.ModalAlertModel
import com.test.topapp.utils.modalalert.modalAlertBuilder
import com.test.topapp.utils.snackbar.SnackBarModel
import com.test.topapp.utils.snackbar.snackBarBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {
    private var binding: ActivityRegisterBinding? = null
    private val viewModel: RegisterActivityViewModel by viewModel()
    private val adminId by lazy { intent.getIntExtra("adminId", -1) }
    private val userId by lazy { intent.getIntExtra("userId", -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        binding?.isEdit = userId != -1
        setContentView(binding!!.root)
        initView()
    }

    private fun initView() {
        val binding = requireNotNull(binding)

        binding.activityRegisterSubtitleAction.setOnClickListener {
            viewModel.setViewEvent(Event.Info(RegisterActivityViewModel.requestRouteToLoginActivity))
        }

        binding.activityRegisterDelete.setOnClickListener {
            viewModel.setViewEvent(Event.Info(RegisterActivityViewModel.requestDeleteUser))
        }

        binding.activityRegisterAction.setOnClickListener {
            viewModel.setViewEvent(
                Event.Info(RegisterActivityViewModel.requestSaveUser, createUserDomain())
            )
        }

        viewModel
            .onInfo {
                when (it.type) {
                    RegisterActivityViewModel.setLoading -> setLoadingVisibility(it.data as Boolean)
                    RegisterActivityViewModel.userLoaded -> binding.user = it.data as User
                    RegisterActivityViewModel.createModalAlert ->
                        modalAlertBuilder(binding.root.context, it.data as ModalAlertModel)
                    RegisterActivityViewModel.routeToLoginActivity ->
                        startActivity(Intent(this, LoginActivity::class.java))
                    RegisterActivityViewModel.routeToHome ->
                        startActivity(Intent(this, MainActivity::class.java))
                    RegisterActivityViewModel.routeToAdmin -> startActivity(
                        Intent(this, UserAdminActivity::class.java)
                            .putExtra("userId", adminId)
                    )
                }
            }
            .onError {
                when (it.type) {
                    RegisterActivityViewModel.setEmailInputError ->
                        binding.activityRegisterEmail.error = it.exception.message
                    RegisterActivityViewModel.setPasswordInputError ->
                        binding.activityRegisterPassword.error = it.exception.message
                    RegisterActivityViewModel.setErrorAlert -> if (it.data is SnackBarModel)
                        snackBarBuilder(binding.root, it.data)
                    else
                        snackBarBuilder(binding.root, SnackBarModel(it.exception.message!!))
                }
            }

        lifecycleScope.launchWhenStarted { viewModel.eventListener() }
        lifecycleScope.launchWhenStarted { viewModel.start(adminId, userId) }
    }

    override fun onPause() {
        lifecycleScope.launchWhenStarted { viewModel.resetEvents() }
        super.onPause()
    }

    private fun setLoadingVisibility(visibility: Boolean) {
        binding!!.activityRegisterLoading.visibility =
            if (visibility) View.VISIBLE else View.INVISIBLE
    }

    private fun createUserDomain(): User {
        val binding = requireNotNull(binding)
        var firstName: String? = binding.activityRegisterFirstName.editText?.text.toString()
        if (firstName.isNullOrBlank()) firstName = null
        var lastName: String? = binding.activityRegisterLastName.editText?.text.toString()
        if (lastName.isNullOrBlank()) lastName = null
        val email = binding.activityRegisterEmail.editText?.text.toString()
        val password = binding.activityRegisterPassword.editText?.text.toString()
        val role = when {
            binding.activityRegisterRoleOwner.isChecked -> 1
            binding.activityRegisterRoleAdmin.isChecked -> 2
            else -> 0
        }

        return User(if (userId >= 0) userId else 0, email, role, firstName, lastName, "", password)
    }
}