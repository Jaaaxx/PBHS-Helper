package com.browardschools.pbhshelper

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import java.io.FileNotFoundException
import java.net.URL


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findViewById<RelativeLayout>(R.id.loadingPanel).visibility = View.GONE
        login_submit.setOnClickListener {
            attemptLogin(username_input, password_input)
        }
    }

    private fun attemptLogin(username : EditText, password : EditText) {
        AsyncTask.execute {
            try {
                runOnUiThread {
                    findViewById<RelativeLayout>(R.id.loadingPanel).visibility = View.VISIBLE
                }
                val verifyJSON: String =
                    URL("https://pinnacle-scraper.herokuapp.com/verify?un=${username.text}&pw=${password.text}").readText()
                runOnUiThread {
                    findViewById<RelativeLayout>(R.id.loadingPanel).visibility = View.GONE
                }
                if (verifyJSON == "True") {
                    val settings = getSharedPreferences("Login", 0)
                    settings.edit().putString("user", "${username.text}").apply()
                    settings.edit().putString("pass", "${password.text}").apply()
                    startActivity(Intent(this, MainActivity::class.java))
                } else
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Username or Password was Incorrect",
                        Snackbar.LENGTH_SHORT
                    ).show()
            } catch (e: FileNotFoundException) {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}
