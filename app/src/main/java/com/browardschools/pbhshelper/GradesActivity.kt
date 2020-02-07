package com.browardschools.pbhshelper

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_grades.*
import kotlinx.android.synthetic.main.content_grades.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class GradesActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        var refreshing = true
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grades)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        @SuppressLint("SimpleDateFormat")
        val formatter = SimpleDateFormat("MM/dd")
        val date: String = formatter.format(Date())
        lastUpdatedGrades.text =
            getSharedPreferences("GradesSettings", 0).getString("lastUpdated", "")
        refresh_grades.background.setColorFilter(
            resources.getColor(R.color.snow),
            PorterDuff.Mode.SRC_ATOP
        )
        buttonEffect(refresh_grades)
        refresh_grades.setOnClickListener {
            if (!refreshing) {
                getSharedPreferences("GradesSettings", 0).edit().clear().apply()
                finish()
                overridePendingTransition(0, 0)
                startActivity(Intent(this, GradesActivity::class.java))
                overridePendingTransition(0, 0)
            }
        }
        findViewById<RelativeLayout>(R.id.loadingPanel).visibility = View.VISIBLE

        val mLayout = findViewById<CoordinatorLayout>(R.id.grades_layout)
        val settings = getSharedPreferences("Login", 0)
        val username = settings.getString("user", "")
        val password = settings.getString("pass", "")
        val density = this.resources.displayMetrics.density
        val dp = { p: Int -> (p * density).toInt() }
        var sessionId = "FALSE"
        try {
            sessionId = intent.getStringExtra("Referrer")!!
        } catch (e: NullPointerException) {
        }
        AsyncTask.execute {
            val gradesPreText = getSharedPreferences("GradesSettings", 0).getString("rawGrades", "")
            if (gradesPreText == "") {
                runOnUiThread {
                    findViewById<RelativeLayout>(R.id.loadingPanel).visibility = View.VISIBLE
                }
                try {
                    val jsonText =
                        URL("https://pinnacle-scraper.herokuapp.com/api?un=$username&pw=$password").readText()
                    if (jsonText == "Username or Password was Incorrect") {
                        getSharedPreferences("Login", 0).edit().clear().apply()
                        startActivity(Intent(this, LoginActivity::class.java))
                    } else {
                        getSharedPreferences("GradesSettings", 0).edit()
                            .putString("rawGrades", jsonText).apply()
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
                    val formatterGA = SimpleDateFormat("MM/dd hh:mm aa")
                    val dateGA: String = formatterGA.format(Date())
                    getSharedPreferences("GradesSettings", 0).edit()
                        .putString("lastUpdated", dateGA).apply()
                    lastUpdatedGrades.text = dateGA
                    findViewById<RelativeLayout>(R.id.loadingPanel).visibility = View.GONE
                }
            }
            val gradesText = getSharedPreferences("GradesSettings", 0).getString("rawGrades", "")
            runOnUiThread { vertParent.removeAllViews() }
            var gradesJSON = JSONArray("[]")
            try {
                gradesJSON = JSONArray(gradesText)
            } catch (e: Exception) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Server error. Try again later!",
                    Snackbar.LENGTH_LONG
                ).show()
                getSharedPreferences("GradesSettings", 0).edit().clear().apply()
            }
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
                runOnUiThread {
                    findViewById<RelativeLayout>(R.id.loadingPanel).visibility = View.GONE
                }
                runOnUiThread { vertParent.addView(horParent) }
                refreshing = false
            }
        }
    }

    fun buttonEffect(button: View) {
        button.setOnTouchListener { v, event ->
            val rotate = RotateAnimation(
                0f,
                180f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            rotate.duration = 500
            rotate.repeatCount = Animation.INFINITE
            rotate.interpolator = LinearInterpolator()
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    button.startAnimation(rotate)
                    v.background.setColorFilter(
                        resources.getColor(R.color.behr),
                        PorterDuff.Mode.SRC_ATOP
                    )
                    v.invalidate()
                }
                MotionEvent.ACTION_CANCEL -> {
                    button.clearAnimation()
                    v.background.setColorFilter(
                        resources.getColor(R.color.snow),
                        PorterDuff.Mode.SRC_ATOP
                    )
                    v.invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    button.clearAnimation()
                    v.background.setColorFilter(
                        resources.getColor(R.color.snow),
                        PorterDuff.Mode.SRC_ATOP
                    )
                    v.invalidate()
                }
            }
            false
        }
    }
}
