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

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        callanimation.setOnClickListener {
            startActivity(Intent(this.baseContext, AnimationActivity::class.java))
        }

        var listener = View.OnTouchListener(function = {view, motionEvent->
            if(motionEvent.action == MotionEvent.ACTION_MOVE ){
                view.y = motionEvent.rawY -view.height/2
                view.x = motionEvent.rawX -view.height/2
            }
            true
        })
        dragbutton.setOnTouchListener(listener)
    }

}
