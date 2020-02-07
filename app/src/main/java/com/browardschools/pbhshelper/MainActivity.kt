package com.browardschools.pbhshelper

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.MotionEventCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var testButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        @SuppressLint("SimpleDateFormat")
        val formatter = SimpleDateFormat("MM/dd")
        val date: String = formatter.format(Date())
        val mLayout = findViewById<ConstraintLayout>(R.id.main_layout)
        val menu = findViewById<ImageView>(R.id.menu)
        menu.x = 1500f
        menu.visibility = View.VISIBLE
        menu.elevation = 4.5f
        testButton = Button(this)
        testButton.id = View.generateViewId()
        testButton.background = ContextCompat.getDrawable(this, R.drawable.menu_grades)
        testButton.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
        testButton.width = ConstraintLayout.LayoutParams.WRAP_CONTENT
        testButton.x = menu.x - 25f
        testButton.y = 1000f
        testButton.elevation = 5f
        mLayout.addView(testButton)

        val settings = getSharedPreferences("Login", 0)
        val username = settings.getString("user", "")
        val password = settings.getString("pass", "")
        /*
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
        */
        grades.text = getString(R.string.grades)
        grades.setOnClickListener { startActivity(Intent(this, GradesActivity::class.java)) }
        if (username != null) {
            if (username[0].toLowerCase() == "p".toCharArray()[0]) {
                grades.text = getString(R.string.teacher)
                grades.setOnClickListener {
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
    var x1 = 0f
    var x2 = 0f
    var locked = false
    override fun onTouchEvent(event: MotionEvent): Boolean {

        val action: Int = MotionEventCompat.getActionMasked(event)

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                x1 = event.x
            }
            MotionEvent.ACTION_MOVE -> {
                // || event.x > x1
                if ((menu.x > 950f && !locked)) {
                    x2 = event.x
                    menu.x -= x1 - x2
                    testButton.x = menu.x - 25f
                    x1 = x2
                }
            }
            MotionEvent.ACTION_UP -> {
                if (menu.x > 950f) {
                    locked = false
                    return false
                }
                if (locked && event.x < 925f) {
                    menu.x = 1500f
                    testButton.x = menu.x - 25f
                    locked = false
                }
                if (menu.x < 950f) {
                    menu.x = 950f
                    testButton.x = menu.x - 25f
                    locked = true
                }
                return false
            }
        }
        return true
    }
}
