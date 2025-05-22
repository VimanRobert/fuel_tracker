package com.fueltracker.presentation.platform

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fueltracker.presentation.R
import com.fueltracker.presentation.activity.MainActivity
import com.fueltracker.presentation.login.LoginActivity
import com.fueltracker.presentation.utils.SystemHandler.isAutomotive
import com.fueltracker.presentation.utils.SystemHandler.isMobile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlatformActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_platform)

        val isCar = isAutomotive(this)
        val isMobile = isMobile(this)
        //val currentUser = FirebaseAuth.getInstance().currentUser

        when {
            isCar -> startActivity(Intent(this, MainActivity::class.java))

//            isMobile && currentUser != null -> startActivity(Intent(this, MainActivity::class.java))

//            isMobile && currentUser == null -> startActivity(Intent(this, LoginActivity::class.java))
            isMobile -> startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}
