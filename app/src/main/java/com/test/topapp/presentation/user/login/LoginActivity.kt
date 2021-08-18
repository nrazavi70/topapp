package com.test.topapp.presentation.user.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.test.topapp.databinding.ActivityLoginBinding
import com.test.topapp.presentation.main.MainActivity
import com.test.topapp.presentation.user.register.RegisterActivity
import com.test.topapp.utils.Event
import com.test.topapp.utils.snackbar.SnackBarModel
import com.test.topapp.utils.snackbar.snackBarBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private var binding: ActivityLoginBinding? = null
    private val viewModel: LoginActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        initView()
    }

    private fun initView() {
        val binding = requireNotNull(binding)

        binding.activityLoginRegisterBtn.setOnClickListener {
            viewModel.setViewEvent(Event.Info(LoginActivityViewModel.requestRouteToRegisterActivity))
        }

        binding.activityLoginAction.setOnClickListener {
            val email = binding.activityLoginEmail.editText?.text.toString()
            val password = binding.activityLoginPassword.editText?.text.toString()
            viewModel.setViewEvent(
                Event.Info(LoginActivityViewModel.requestLoginUser, email to password)
            )
        }

        viewModel
            .onInfo {
                when (it.type) {
                    LoginActivityViewModel.setLoading -> setLoadingVisibility(it.data as Boolean)
                    LoginActivityViewModel.routeToRegisterActivity ->
                        startActivity(Intent(this, RegisterActivity::class.java))
                    LoginActivityViewModel.routeToHome ->
                        startActivity(Intent(this, MainActivity::class.java))
                }
            }
            .onError {
                when (it.type) {
                    LoginActivityViewModel.setEmailInputError ->
                        binding.activityLoginEmail.error = it.exception.message
                    LoginActivityViewModel.setPasswordInputError ->
                        binding.activityLoginPassword.error = it.exception.message
                    LoginActivityViewModel.setErrorAlert -> if (it.data is SnackBarModel)
                        snackBarBuilder(binding.root, it.data)
                    else
                        snackBarBuilder(binding.root, SnackBarModel(it.exception.message!!))
                }
            }

        lifecycleScope.launchWhenStarted { viewModel.eventListener() }
    }

    override fun onPause() {
        lifecycleScope.launchWhenStarted { viewModel.resetEvents() }
        super.onPause()
    }

    private fun setLoadingVisibility(visibility: Boolean) {
        binding!!.activityLoginLoading.visibility =
            if (visibility) View.VISIBLE else View.INVISIBLE
    }
}