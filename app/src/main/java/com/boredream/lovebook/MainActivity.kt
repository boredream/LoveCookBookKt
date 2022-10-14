package com.boredream.lovebook

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil.setContentView

class MainActivity : AppCompatActivity() {

    companion object{
        fun start(context: Context, target: Class<out AppCompatActivity>){
            val intent = Intent(context, target)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}