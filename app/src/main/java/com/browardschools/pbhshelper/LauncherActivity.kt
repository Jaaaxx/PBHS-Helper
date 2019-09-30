package com.browardschools.pbhshelper

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.net.URL

class LauncherActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Make sure this is before calling super.onCreate
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        val settings = getSharedPreferences("Login", 0)
        // settings.edit().clear().apply()
        val username : String? = settings.getString("user", "")
        val password : String? = settings.getString("pass", "")
        if (username != "" && password != "" && username != null && password != null) {
            startActivity(Intent(this, MainActivity::class.java))
            AsyncTask.execute {
                val verifyJSON : String = URL("https://pinnacle-scraper.herokuapp.com/verify?un=${username}&pw=${password}").readText()
                if(verifyJSON != "True") {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }
        } else
            startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}