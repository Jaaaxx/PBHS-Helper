package com.browardschools.pbhshelper

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log.d
import android.view.Gravity
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_assignments.*
import kotlinx.android.synthetic.main.content_assignments.*
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*


class AssignmentsActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assignments)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        @SuppressLint("SimpleDateFormat")
        val formatter = SimpleDateFormat("MM/dd")
        val date: String = formatter.format(Date())
        val density = this.resources.displayMetrics.density
        val dp = { p: Int -> (p * density).toInt() }
        val mLayout = findViewById<CoordinatorLayout>(R.id.assignment_layout)

        var sessionId = "FALSE"
        try {
            sessionId = intent.getStringExtra("CourseRefer")!!
        } catch (e: NullPointerException) {
        }
        if (sessionId != "FALSE") {
            val gradesText = getSharedPreferences("GradesSettings", 0).getString("rawGrades", "")
            if (gradesText != "" && gradesText != null) {
                runOnUiThread { vertASParent.removeAllViews() }
                val gradesJSON = JSONArray(gradesText)
                var assignmentsJSON: JSONArray = JSONArray()
                for (i in 0 until gradesJSON.length()) {
                    if (gradesJSON.getJSONObject(i).getString("Course") == sessionId) {
                        assignmentsJSON = gradesJSON.getJSONObject(i).getJSONArray("Assignments")
                    }
                }
                if (assignmentsJSON.length() != 0) {
                    for (i in 0 until assignmentsJSON.length()) {
                        val assignment = assignmentsJSON.getJSONObject(i)
                        d("assignments name", assignment.getString("Name"))
                        val horParent = LinearLayout(this)
                        val lp = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            2f
                        )
                        lp.setMargins(dp(10), dp(10), dp(10), dp(0))
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
                        txtViewChild.text = assignment.getString("Name")
                        txtViewChild.textSize = 20.0f

                        val valTV = TextView(this)
                        valTV.layoutParams = LinearLayout.LayoutParams(
                            dp(120),
                            LinearLayout.LayoutParams.MATCH_PARENT
                        )
                        try {
                            valTV.text =
                                "${(assignment.getString("Points").toFloat() / assignment.getString(
                                    "Max"
                                ).toFloat() * 100).toInt()}%"
                        } catch (e: NumberFormatException) {
                            valTV.text = getString(R.string.null_val)
                        }
                        valTV.gravity = Gravity.CENTER
                        valTV.background =
                            resources.getDrawable(android.R.drawable.editbox_dropdown_light_frame)
                        valTV.textSize = 30.0f
                        try {
                            when {
                                (assignment.getString("Points").toFloat() / assignment.getString("Max").toFloat() * 100).toInt() >= 90 || assignment.getString(
                                    "Max"
                                ).toInt() == 0 -> valTV.background.setColorFilter(
                                    ContextCompat.getColor(
                                        this,
                                        R.color.americangreen
                                    ), PorterDuff.Mode.MULTIPLY
                                )
                                (assignment.getString("Points").toFloat() / assignment.getString("Max").toFloat() * 100).toInt() >= 85 -> valTV.background.setColorFilter(
                                    ContextCompat.getColor(
                                        this,
                                        R.color.arylideyellow
                                    ), PorterDuff.Mode.MULTIPLY
                                )
                                (assignment.getString("Points").toFloat() / assignment.getString("Max").toFloat() * 100).toInt() >= 80 -> valTV.background.setColorFilter(
                                    ContextCompat.getColor(
                                        this,
                                        R.color.amurcorktree
                                    ), PorterDuff.Mode.MULTIPLY
                                )
                                else -> valTV.background.setColorFilter(
                                    ContextCompat.getColor(
                                        this,
                                        R.color.auburn
                                    ), PorterDuff.Mode.MULTIPLY
                                )
                            }
                        } catch (e: NumberFormatException) {
                        }
                        horParent.addView(txtViewChild)
                        horParent.addView(valTV)
                        runOnUiThread { vertASParent.addView(horParent) }
                    }
                } else {
                    val horParent = LinearLayout(this)
                    val lp = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        2f
                    )
                    lp.setMargins(dp(10), dp(10), dp(10), dp(0))
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
                    txtViewChild.text = getString(R.string.no_grades_assigned)
                    txtViewChild.textSize = 20.0f

                    horParent.addView(txtViewChild)
                    runOnUiThread { vertASParent.addView(horParent) }
                }
            }
        } else {
            Snackbar.make(
                findViewById(android.R.id.content),
                "Please try again.",
                Snackbar.LENGTH_SHORT
            ).show()
        }


        when (datesDays[date]) {
            1 -> mLayout.setBackgroundColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.straw,
                    null
                )
            )
            0 -> mLayout.setBackgroundColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.water,
                    null
                )
            )
            else -> mLayout.setBackgroundColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.shiracha,
                    null
                )
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(
                    this@AssignmentsActivity,
                    GradesActivity::class.java
                ).putExtra("Referrer", "AssignmentsActivity")
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(
            this@AssignmentsActivity,
            GradesActivity::class.java
        ).putExtra("Referrer", "AssignmentsActivity")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}
