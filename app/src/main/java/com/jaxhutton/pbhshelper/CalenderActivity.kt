package com.jaxhutton.pbhshelper

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat

import kotlinx.android.synthetic.main.activity_calender.*
import java.text.SimpleDateFormat
import java.util.*

class CalenderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calender)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        @SuppressLint("SimpleDateFormat")
        val formatter = SimpleDateFormat("MM/dd")
        val date: String = formatter.format(Date())
        val mLayout = findViewById<ConstraintLayout>(R.id.calenderLayout)
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
                    R.color.blandBackground,
                    null
                )
            )
        }
    }

}
