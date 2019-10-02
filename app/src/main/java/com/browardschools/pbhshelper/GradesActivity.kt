package com.browardschools.pbhshelper

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_grades.*
import kotlinx.android.synthetic.main.content_grades.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileNotFoundException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class GradesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grades)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        @SuppressLint("SimpleDateFormat")
        val formatter = SimpleDateFormat("MM/dd")
        val date: String = formatter.format(Date())
        findViewById<RelativeLayout>(R.id.loadingPanel).visibility = View.GONE
        val mLayout = findViewById<CoordinatorLayout>(R.id.grades_layout)
        val settings = getSharedPreferences("Login", 0)
        val username = settings.getString("user", "")
        val password = settings.getString("pass", "")
        val density = this.resources.displayMetrics.density
        val dp = { p: Int -> (p * density).toInt() }
        AsyncTask.execute {
            if (getSharedPreferences("GradesSettings", 0).getString("rawGrades", "") == ""
                || getSharedPreferences("GradesSettings", 0).getString("rawGrades", "") == null
            ) {
                runOnUiThread {
                    findViewById<RelativeLayout>(R.id.loadingPanel).visibility = View.VISIBLE
                }
            }
            try {
                val gradesText =
                    URL("https://pinnacle-scraper.herokuapp.com/api?un=$username&pw=$password").readText()
                if (gradesText == "Username or Password was Incorrect") {
                    getSharedPreferences("Login", 0).edit().clear().apply()
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    getSharedPreferences("GradesSettings", 0).edit()
                        .putString("rawGrades", gradesText).apply()
                }
            } catch (e: FileNotFoundException) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Network error. Try again later!",
                    Snackbar.LENGTH_LONG
                ).show()
                getSharedPreferences("GradesSettings", 0).edit().clear().apply()
            }
            if (getSharedPreferences("GradesSettings", 0).getString("rawGrades", "") == ""
                || getSharedPreferences("GradesSettings", 0).getString("rawGrades", "") == null
            ) {
                runOnUiThread {
                    findViewById<RelativeLayout>(R.id.loadingPanel).visibility = View.GONE
                }
            }
            val gradesText = getSharedPreferences("GradesSettings", 0).getString("rawGrades", "")
            if (gradesText != "" && gradesText != null) {
                runOnUiThread { vertParent.removeAllViews() }
                val gradesJSON = JSONArray(gradesText)
                val allCourses = ArrayList<JSONObject>()
                for (i in 0 until gradesJSON.length())
                    allCourses.add(gradesJSON.getJSONObject(i))
                for (i in allCourses) {
                    if (i.getString("Quarter") == "Quarter 1") {
                        val horParent = LinearLayout(this)
                        val lp = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            dp(0),
                            2f
                        )
                        lp.setMargins(dp(10), dp(0), dp(0), dp(0))
                        horParent.layoutParams = lp
                        horParent.orientation = LinearLayout.HORIZONTAL

                        val txtViewChild = TextView(this)
                        txtViewChild.layoutParams = LinearLayout.LayoutParams(
                            dp(0),
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2f
                        )
                        txtViewChild.typeface =
                            Typeface.create("sans-serif-condensed-medium", Typeface.NORMAL)
                        txtViewChild.gravity = Gravity.CENTER_VERTICAL
                        txtViewChild.text = i.getString("Course")
                        txtViewChild.textSize = 30.0f

                        val btnChild = Button(this)
                        btnChild.layoutParams = LinearLayout.LayoutParams(
                            dp(120),
                            LinearLayout.LayoutParams.MATCH_PARENT
                        )
                        btnChild.text = i.getString("Grade")
                        btnChild.textSize = 30.0f
                        btnChild.setOnClickListener {
                            startActivity(Intent(this, AssignmentsActivity::class.java))
                        }
                        horParent.addView(txtViewChild)
                        horParent.addView(btnChild)
                        runOnUiThread { vertParent.addView(horParent) }
                    }
                }
            }
        }
        val gradesText = getSharedPreferences("GradesSettings", 0).getString("rawGrades", "")
        if (gradesText != "" && gradesText != null) {
            runOnUiThread { vertParent.removeAllViews() }
            val gradesJSON = JSONArray(gradesText)
            val allCourses = ArrayList<JSONObject>()
            for (i in 0 until gradesJSON.length())
                allCourses.add(gradesJSON.getJSONObject(i))
            for (i in allCourses) {
                if (i.getString("Quarter") == "Quarter 1") {
                    val horParent = LinearLayout(this)
                    val lp = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(0),
                        2f
                    )
                    lp.setMargins(dp(10), dp(0), dp(0), dp(0))
                    horParent.layoutParams = lp
                    horParent.orientation = LinearLayout.HORIZONTAL

                    val txtViewChild = TextView(this)
                    txtViewChild.layoutParams = LinearLayout.LayoutParams(
                        dp(0),
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        2f
                    )
                    txtViewChild.typeface =
                        Typeface.create("sans-serif-condensed-medium", Typeface.NORMAL)
                    txtViewChild.gravity = Gravity.CENTER_VERTICAL
                    txtViewChild.text = i.getString("Course")
                    txtViewChild.textSize = 30.0f

                    val btnChild = Button(this)
                    btnChild.layoutParams = LinearLayout.LayoutParams(
                        dp(120),
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )
                    btnChild.text = i.getString("Grade")
                    btnChild.textSize = 30.0f
                    btnChild.setOnClickListener {
                        startActivity(Intent(this, AssignmentsActivity::class.java))
                    }
                    horParent.addView(txtViewChild)
                    horParent.addView(btnChild)
                    runOnUiThread { vertParent.addView(horParent) }
                }
            }
        }
        when (datesDays[date]) {
            1 -> mLayout.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.straw, null))
            0 -> mLayout.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.water, null))
            else -> mLayout.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.shiracha, null))
        }
    }

}
