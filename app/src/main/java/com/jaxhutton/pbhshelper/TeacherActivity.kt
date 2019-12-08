package com.jaxhutton.pbhshelper

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.activity_teacher.*
import kotlinx.android.synthetic.main.content_teacher.*
import java.text.SimpleDateFormat
import java.util.*

class TeacherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)
        setSupportActionBar(toolbar)
        @SuppressLint("SimpleDateFormat")
        val formatter = SimpleDateFormat("MM/dd")
        val date: String = formatter.format(Date())
        val mLayout = teacher_layout
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
