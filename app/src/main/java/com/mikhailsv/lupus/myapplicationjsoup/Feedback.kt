package com.mikhailsv.lupus.myapplicationjsoup

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_feedback.*

class Feedback : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        supportActionBar!!.title = "App Feedback"
    }

    fun btnSendClick(view: View) {
        val mDatabase = FirebaseDatabase.getInstance().getReference()
        mDatabase.child("feedback").child("feedback").push().setValue(feedbackText.text.toString())
    }
}
