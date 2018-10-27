package com.bigdreamscoders.cihua

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import com.google.ar.core.Anchor
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

import kotlinx.android.synthetic.main.activity_main.*
import android.R.attr.width
import android.util.DisplayMetrics


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val windowWidth = displayMetrics.widthPixels
        val windowHeight = displayMetrics.heightPixels
        var dx: Float = 0F
        var dy: Float = 0F
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        callanimation.setOnClickListener {
            startActivity(Intent(this.baseContext, AnimationActivity::class.java))
        }

        val listener = View.OnTouchListener(function = { view, motionEvent->
            val width = view.layoutParams.width.toFloat()
            val height = view.layoutParams.height.toFloat()

            when(motionEvent.action){
                    MotionEvent.ACTION_DOWN->{
                        dy =  motionEvent.rawY -view.height/2
                        dx =  motionEvent.rawX -view.width/2
                    }
                    MotionEvent.ACTION_MOVE->{
                        view.animate()
                            .x(motionEvent.rawX)
                            .y(motionEvent.rawY)
                            .setDuration(1000)
                            .start()
                        if (motionEvent.rawX +  width/2 > windowWidth) {
                            view.animate()
                                .x(windowWidth+width/2)
                                .setDuration(1000)
                                .start();
                        }
                        if (motionEvent.rawX + windowWidth/2 < 0) {
                            view.animate()
                                .x(-width/2)
                                .setDuration(1000)
                                .start();
                        }
                        if (motionEvent.rawY + height/2 > windowHeight) {
                            view.animate()
                                .y(windowHeight + height/2)
                                .setDuration(1000)
                                .start();
                        }
                        if (motionEvent.rawY + dy < 0) {
                            view.animate()
                                .y(0F)
                                .setDuration(1000)
                                .start();
                        }
                    }
                    else ->{}

            }
            true
        })
        dragbutton.setOnTouchListener(listener)
    }

}
