package com.example.peoplesontrol.ui.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.peoplesontrol.R
import com.example.peoplesontrol.data.api.ApiHelper
import com.example.peoplesontrol.data.api.RetrofitBuilder
import com.example.peoplesontrol.data.db.DatabaseBuilder
import com.example.peoplesontrol.data.db.DatabaseHelperImpl
import com.example.peoplesontrol.data.model.Data
import com.example.peoplesontrol.data.model.Message
import com.example.peoplesontrol.data.model.TokenData
import com.example.peoplesontrol.ui.view.login.LoginActivity
import com.example.peoplesontrol.ui.viewmodel.LoginViewModel
import com.example.peoplesontrol.ui.viewmodel.LoginViewModelFactory
import com.example.peoplesontrol.utils.Status

class SplashActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setupViewModel()
        val btnNext = findViewById<Button>(R.id.btn_next)
        val sharedPreference = getSharedPreferences("REFRESH_TOKEN", Context.MODE_PRIVATE)
        val refreshToken =
            sharedPreference.getString("refreshToken", null)
        refreshToken(Message(refreshToken.toString()), "Bearer " + refreshToken.toString())
        btnNext.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            LoginViewModelFactory(
                ApiHelper(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(this))
            )
        )[LoginViewModel::class.java]
    }

    private fun refreshToken(message: Message, token: String) {
        viewModel.refreshToken(message, token).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { tokens -> setTokens(tokens) }
                        val sharedPreference =
                            getSharedPreferences("REFRESH_TOKEN", MODE_PRIVATE)
                        val editor = sharedPreference.edit()
                        editor.clear()
                        editor.putString(
                            "refreshToken",
                            Data.token.refreshToken
                        )
                        editor.apply()
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
            }
        })
    }

    private fun setTokens(tokens: TokenData) {
        Data.token = tokens
    }
}