package com.browardschools.pbhshelper

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.res.ResourcesCompat

import kotlinx.android.synthetic.main.activity_assignments.*
import java.text.SimpleDateFormat
import java.util.*

class AssignmentsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assignments)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        @SuppressLint("SimpleDateFormat")
        val formatter = SimpleDateFormat("MM/dd")
        val date: String = formatter.format(Date())
        val mLayout = findViewById<CoordinatorLayout>(R.id.assignment_layout)

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

}
