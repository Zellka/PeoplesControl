package com.example.peoplesontrol.ui.view.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.peoplesontrol.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val navController = findNavController(R.id.nav_host_fragment_activity_login)
        setupActionBarWithNavController(navController)
    }
}