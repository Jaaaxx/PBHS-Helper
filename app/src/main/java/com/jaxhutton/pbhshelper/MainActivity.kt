package com.jaxhutton.pbhshelper

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        @SuppressLint("SimpleDateFormat")
        val formatter = SimpleDateFormat("MM/dd")
        val date: String = formatter.format(Date())
        val mLayout = findViewById<ConstraintLayout>(R.id.main_layout)
        val settings = getSharedPreferences("Login", 0)
        val username = settings.getString("user", "")
        val password = settings.getString("pass", "")

        when (datesDays[date]) {
            1 -> mLayout.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.straw, null))
            0 -> mLayout.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.water, null))
            else -> mLayout.setBackgroundColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.blandBackground,
                    null
                )
            )
        }

        interchange.text = getString(R.string.grades)
        interchange.setOnClickListener { startActivity(Intent(this, GradesActivity::class.java)) }
        if (username != null) {
            if (username[0].toLowerCase() == "p".toCharArray()[0]) {
                interchange.text = getString(R.string.teacher)
                interchange.setOnClickListener {
                    startActivity(
                        Intent(
                            this,
                            TeacherActivity::class.java
                        )
                    )
                }
            }
        }
        calender.setOnClickListener {
            startActivity(Intent(this, CalenderActivity::class.java))
        }
        logout.setOnClickListener {
            getSharedPreferences("Login", 0).edit().clear().apply()
            getSharedPreferences("GradesSettings", 0).edit().clear().apply()
            startActivity(Intent(this, LoginActivity::class.java))
        }
        counselor.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://bcps.browardschools.com/VirtualCounselor/")
            )
            startActivity(browserIntent)
        }

        AsyncTask.execute {
            try {
                val verifyJSON: String =
                    URL("https://pinnacle-scraper.herokuapp.com/verify?un=${username}&pw=${password}").readText()
                if (verifyJSON != "True")
                    startActivity(Intent(this, LoginActivity::class.java))
            } catch (e: Exception) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Network error. Try again later!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onBackPressed() {}
}
