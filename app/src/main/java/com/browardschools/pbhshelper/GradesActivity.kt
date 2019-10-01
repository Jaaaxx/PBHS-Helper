package com.browardschools.pbhshelper

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log.d
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.activity_grades.*
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import org.json.JSONObject
import org.json.JSONArray


class GradesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grades)
        setSupportActionBar(toolbar)
        @SuppressLint("SimpleDateFormat")
        val formatter = SimpleDateFormat("MM/dd")
        val date: String = formatter.format(Date())
        val mLayout = findViewById<CoordinatorLayout>(R.id.grades_layout)
        val settings = getSharedPreferences("Login", 0)
        val username = settings.getString("user", "")
        val password = settings.getString("pass", "")
        AsyncTask.execute {
            val gradesText = URL("https://pinnacle-scraper.herokuapp.com/api?un=${username}&pw=${password}").readText()
            if (gradesText == "Username or Password was Incorrect") {
                d("status", "username or password was incorrect")
                getSharedPreferences("Login", 0).edit().clear().apply()
                startActivity(Intent(this, LoginActivity::class.java))
            }
            else {
                val gradesJSON = JSONArray(gradesText)
                val allCourses = ArrayList<JSONObject>()
                for (i in 0 until gradesJSON.length())
                    allCourses.add(gradesJSON.getJSONObject(i))
                for (i in allCourses) {
                    d("course", i.toString())
                }
            }
            runOnUiThread {
                findViewById<RelativeLayout>(R.id.loadingPanel).visibility = View.GONE
            }
        }
        when (datesDays[date]) {
            1 -> mLayout.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.straw, null))
            0 -> mLayout.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.water, null))
            else -> mLayout.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.shiracha, null))
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
