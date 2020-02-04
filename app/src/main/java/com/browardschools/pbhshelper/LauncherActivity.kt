package com.browardschools.pbhshelper

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
class LauncherActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Make sure this is before calling super.onCreate
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        // getSharedPreferences("GradesSettings", 0).edit().clear().apply()
        val settings = getSharedPreferences("Login", 0)
        val username : String? = settings.getString("user", "")
        val password : String? = settings.getString("pass", "")
        if (username != "" && password != "" && username != null && password != null)
            startActivity(Intent(this, MainActivity::class.java))
        else {
            getSharedPreferences("GradesSettings", 0).edit().clear().apply()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}