package com.bigdreamscoders.cihua

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.ar.core.Anchor
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_animation.*

class AnimationActivity : AppCompatActivity() {

    private var timer: ObjectAnimator? = null
    private var degreesPerSecond = 90.0f
    private var lastSpeedMultiplier = 1.0f
    private val animationDuration: Long
        get() = (1000 * 360 / (degreesPerSecond * speedMultiplier)).toLong()
    private val speedMultiplier: Float
        get() = 1.0f

    lateinit var fragment: ArFragment
    val anchors: MutableList<Anchor> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)

        fragment = supportFragmentManager.findFragmentById(R.id.sceneformFragment) as ArFragment

        fab.setOnClickListener {
            if(anchors.size==0){
                addObject()
            }
            else{
                removeNodes()
                addObject()
            }
        }
        callstop.setOnClickListener {
            removeNodes()
        }
    }

    private fun removeNodes(){
        for(ancho in anchors){
            ancho.detach()
        }
        anchors.removeAll { true }

    }

    private fun addObject() {
        val frame = fragment.arSceneView.arFrame
        val point = getScreenCenter()
        var elements =  listOf("personaB0.sfb", "model.sfb")
        if (frame != null ) {
            val hits = frame.hitTest(point.x.toFloat(), point.y.toFloat())
            for (hit in hits) {
                val trackable = hit.trackable
                if (trackable is Plane && trackable.isPoseInPolygon(hit.hitPose)) {
                    placeObject(fragment, hit.createAnchor(), Uri.parse(elements.get(0)))
                    placeFloor(fragment, hit.createAnchor(), Uri.parse(elements.get(1)))
                    break
                }
            }



        }
    }

    private fun placeObject(fragment: ArFragment, createAnchor: Anchor, model: Uri) {
        ModelRenderable.builder()
            .setSource(fragment.context, model)
            .build()
            .thenAccept {
                addRotatingNodeToScene(fragment, createAnchor, it)
                anchors.add(createAnchor)
            }
            .exceptionally {
                val builder = AlertDialog.Builder(this)
                builder.setMessage(it.message)
                    .setTitle("error!")
                val dialog = builder.create()
                dialog.show()
                return@exceptionally null
            }
    }

    private fun placeFloor(fragment: ArFragment, createAnchor: Anchor, model: Uri){
        ModelRenderable.builder()
            .setSource(fragment.context, model)
            .build()
            .thenAccept {
                addStaticNodeToScene(fragment, createAnchor, it)
                anchors.add(createAnchor)
            }
            .exceptionally {
                val builder = AlertDialog.Builder(this)
                builder.setMessage(it.message)
                    .setTitle("error!")
                val dialog = builder.create()
                dialog.show()
                return@exceptionally null
            }
    }


    private fun addStaticNodeToScene(fragment: ArFragment, createAnchor: Anchor, renderable: ModelRenderable){
        val anchorNode = AnchorNode(createAnchor)
        val transformableNode = TransformableNode(fragment.transformationSystem)
        val staticNode = StaticNode()

        staticNode.renderable = renderable
        staticNode.addChild(transformableNode)
        staticNode.setParent(anchorNode)

        fragment.arSceneView.scene.addChild(anchorNode)
        transformableNode.select()
    }

    private fun addRotatingNodeToScene(fragment: ArFragment, createAnchor: Anchor, renderable: ModelRenderable) {
        val anchorNode = AnchorNode(createAnchor)
        val transformableNode = TransformableNode(fragment.transformationSystem)
        val rotatingNode = RotatingNode()

        rotatingNode.renderable = renderable
        rotatingNode.addChild(transformableNode)
        rotatingNode.setParent(anchorNode)

        fragment.arSceneView.scene.addChild(anchorNode)
        transformableNode.select()
    }

    private fun getScreenCenter(): android.graphics.Point {
        val vw = findViewById<View>(android.R.id.content)
        return Point(vw.width / 2, vw.height / 2)
    }

}