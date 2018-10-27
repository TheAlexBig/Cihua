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
        val windowWidth = displayMetrics.widthPixels
        val windowHeight = displayMetrics.heightPixels
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        callanimation.setOnClickListener {
            startActivity(Intent(this.baseContext, AnimationActivity::class.java))
        }

        val listener = View.OnTouchListener(function = { view, motionEvent->
            val width = view.layoutParams.width.toFloat()
            val height = view.layoutParams.height.toFloat()
            var dx: Float = 0F
            var dy: Float = 0F
            when(motionEvent.action){
                    MotionEvent.ACTION_DOWN->{
                        dx =  motionEvent.rawY -view.height/2
                        dy =  motionEvent.rawX -view.height/2
                        true
                    }
                    MotionEvent.ACTION_MOVE->{
                        view.animate()
                            .x(motionEvent.rawX+dx)
                            .y(motionEvent.rawY+dy)
                            .setDuration(0)
                            .start()
                        if (motionEvent.rawX + dx + width > windowWidth) {
                            view.animate()
                                .x(windowWidth - width)
                                .setDuration(0)
                                .start();
                        }
                        if (motionEvent.rawX + dx < 0) {
                            view.animate()
                                .x(0F)
                                .setDuration(0)
                                .start();
                        }
                        if (motionEvent.rawY + dy + height > windowHeight) {
                            view.animate()
                                .y(windowHeight - height)
                                .setDuration(0)
                                .start();
                        }
                        if (motionEvent.rawY + dy < 0) {
                            view.animate()
                                .y(0F)
                                .setDuration(0)
                                .start();
                        }
                        true
                    }
                    else ->
                        true
            }

        })
        dragbutton.setOnTouchListener(listener)
    }

}
