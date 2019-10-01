package com.browardschools.pbhshelper

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        @SuppressLint("SimpleDateFormat")
        val formatter = SimpleDateFormat("MM/dd")
        val date: String = formatter.format(Date())
        val mLayout = findViewById<ConstraintLayout>(R.id.main_layout)

        when (datesDays[date]) {
            1 -> mLayout.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.straw, null))
            0 -> mLayout.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.water, null))
            else -> mLayout.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.shiracha, null))
        }
        grades.setOnClickListener {
            startActivity(Intent(this, GradesActivity::class.java))
        }
        calender.setOnClickListener {
            startActivity(Intent(this, CalenderActivity::class.java))
        }
        logout.setOnClickListener {
            getSharedPreferences("Login", 0).edit().clear().apply()
            startActivity(Intent(this, LoginActivity::class.java))
        }
        counselor.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://bcps.browardschools.com/VirtualCounselor/"))
            startActivity(browserIntent)
        }
    }
}
