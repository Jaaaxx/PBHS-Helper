package com.browardschools.pbhshelper

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
    }

}
