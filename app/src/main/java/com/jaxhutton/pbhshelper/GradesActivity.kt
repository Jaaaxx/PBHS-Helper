package com.jaxhutton.pbhshelper

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_grades.*
import kotlinx.android.synthetic.main.content_grades.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class GradesActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
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
        var sessionId = "FALSE"
        try {
            sessionId = intent.getStringExtra("Referrer")!!
            Log.d("sessionID", sessionId)
        } catch (e: NullPointerException) {
        }
        if (sessionId != "AssignmentsActivity") {
            findViewById<RelativeLayout>(R.id.loadingPanel).visibility = View.VISIBLE
        }
        AsyncTask.execute {
            if (sessionId != "AssignmentsActivity") {
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
                } catch (e: Exception) {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Network error. Try again later!",
                        Snackbar.LENGTH_LONG
                    ).show()
                    getSharedPreferences("GradesSettings", 0).edit().clear().apply()
                }
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
                    val horParent = LinearLayout(this)
                    val lp = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(85),
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
                    if (i.getString("Grade") == "") {
                        btnChild.text = getString(R.string.null_val)
                    } else {
                        btnChild.text = """${i.getString("Grade")}%"""
                        when {
                            i.getString("Grade").toInt() >= 100 -> btnChild.background.setColorFilter(
                                ContextCompat.getColor(
                                    this,
                                    R.color.g100
                                ), PorterDuff.Mode.MULTIPLY
                            )
                            i.getString("Grade").toInt() >= 98 -> btnChild.background.setColorFilter(
                                ContextCompat.getColor(
                                    this,
                                    R.color.g98
                                ), PorterDuff.Mode.MULTIPLY
                            )
                            i.getString("Grade").toInt() >= 96 -> btnChild.background.setColorFilter(
                                ContextCompat.getColor(
                                    this,
                                    R.color.g96
                                ), PorterDuff.Mode.MULTIPLY
                            )
                            i.getString("Grade").toInt() >= 94 -> btnChild.background.setColorFilter(
                                ContextCompat.getColor(
                                    this,
                                    R.color.g94
                                ), PorterDuff.Mode.MULTIPLY
                            )
                            i.getString("Grade").toInt() >= 92 -> btnChild.background.setColorFilter(
                                ContextCompat.getColor(
                                    this,
                                    R.color.g92
                                ), PorterDuff.Mode.MULTIPLY
                            )
                            i.getString("Grade").toInt() >= 90 -> btnChild.background.setColorFilter(
                                ContextCompat.getColor(
                                    this,
                                    R.color.g90
                                ), PorterDuff.Mode.MULTIPLY
                            )
                            i.getString("Grade").toInt() >= 88 -> btnChild.background.setColorFilter(
                                ContextCompat.getColor(
                                    this,
                                    R.color.g88
                                ), PorterDuff.Mode.MULTIPLY
                            )
                            i.getString("Grade").toInt() >= 86 -> btnChild.background.setColorFilter(
                                ContextCompat.getColor(
                                    this,
                                    R.color.g86
                                ), PorterDuff.Mode.MULTIPLY
                            )
                            i.getString("Grade").toInt() >= 84 -> btnChild.background.setColorFilter(
                                ContextCompat.getColor(
                                    this,
                                    R.color.g84
                                ), PorterDuff.Mode.MULTIPLY
                            )
                            i.getString("Grade").toInt() >= 82 -> btnChild.background.setColorFilter(
                                ContextCompat.getColor(
                                    this,
                                    R.color.g82
                                ), PorterDuff.Mode.MULTIPLY
                            )
                            i.getString("Grade").toInt() >= 80 -> btnChild.background.setColorFilter(
                                ContextCompat.getColor(
                                    this,
                                    R.color.g80
                                ), PorterDuff.Mode.MULTIPLY
                            )
                            i.getString("Grade").toInt() >= 78 -> btnChild.background.setColorFilter(
                                ContextCompat.getColor(
                                    this,
                                    R.color.g78
                                ), PorterDuff.Mode.MULTIPLY
                            )
                            i.getString("Grade").toInt() >= 76 -> btnChild.background.setColorFilter(
                                ContextCompat.getColor(
                                    this,
                                    R.color.g76
                                ), PorterDuff.Mode.MULTIPLY
                            )
                            i.getString("Grade").toInt() >= 74 -> btnChild.background.setColorFilter(
                                ContextCompat.getColor(
                                    this,
                                    R.color.g74
                                ), PorterDuff.Mode.MULTIPLY
                            )
                            i.getString("Grade").toInt() >= 72 -> btnChild.background.setColorFilter(
                                ContextCompat.getColor(
                                    this,
                                    R.color.g72
                                ), PorterDuff.Mode.MULTIPLY
                            )
                            i.getString("Grade").toInt() >= 70 -> btnChild.background.setColorFilter(
                                ContextCompat.getColor(
                                    this,
                                    R.color.g70
                                ), PorterDuff.Mode.MULTIPLY
                            )
                            else -> btnChild.background.setColorFilter(
                                ContextCompat.getColor(
                                    this,
                                    R.color.gbelow
                                ), PorterDuff.Mode.MULTIPLY
                            )
                        }
                    }
                    btnChild.textSize = 30.0f
                    btnChild.setOnClickListener {
                        startActivity(
                            Intent(
                                this,
                                AssignmentsActivity::class.java
                            ).putExtra("CourseRefer", i.getString("Course"))
                        )
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
            else -> mLayout.setBackgroundColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.blandBackground,
                    null
                )
            )
        }
    }
}
