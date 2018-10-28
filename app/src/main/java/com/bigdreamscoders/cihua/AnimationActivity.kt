package com.bigdreamscoders.cihua

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
import com.google.ar.core.Pose
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_animation.*

class AnimationActivity : AppCompatActivity() {
    lateinit var fragment: ArFragment
    val anchors: MutableList<Anchor> = arrayListOf()
    val nodes: MutableList<Node> = arrayListOf()
    var sector = 0
    var counter = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)

        sector = this.intent.getIntExtra("sector",0)

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

        val elements: List<String>
        if (frame != null) {
            when (sector) {
                0 -> {
                    var point = getScreenCenter()
                    elements = listOf("personaB0.sfb", "model.sfb")
                    val hits = frame.hitTest(point.x.toFloat(), point.y.toFloat())
                    for (hit in hits) {
                        val trackable = hit.trackable
                        if (trackable is Plane && trackable.isPoseInPolygon(hit.hitPose)) {
                            placeFloor(fragment, hit.createAnchor(), Uri.parse(elements.get(0)))
                            placeFloor(fragment, hit.createAnchor(), Uri.parse(elements.get(1)))
                            break
                        }
                    }
                }
                1 -> elements = listOf("Craneo.sfb", "model.sfb")
                2 -> {elements = listOf("Mascara_Low.sfb", "model.sfb", "Punta_de_flecha.sfb", "model.sfb"
                    , "TablillaMazapan.sfb", "model.sfb")
                    var point = getScreenCenter()
                    val hits = frame.hitTest((point.x).toFloat(), (point.y).toFloat())
                    for (hit in hits) {
                        val trackable = hit.trackable
                        if (trackable is Plane && trackable.isPoseInPolygon(hit.hitPose)) {
                            placeFloor(fragment, hit.createAnchor(), Uri.parse(elements[0]))
                            placeFloor(fragment, hit.createAnchor(), Uri.parse(elements[1]))
                            placeFloor(fragment, hit.createAnchor(), Uri.parse(elements[2]))
                            placeFloor(fragment, hit.createAnchor(), Uri.parse(elements[3]))
                            placeFloor(fragment, hit.createAnchor(), Uri.parse(elements[4]))
                            placeFloor(fragment, hit.createAnchor(), Uri.parse(elements[5]))
                            break
                        }

                        var pose = Pose.makeTranslation(1.0f,0.0f,0.0f)
                        nodes[0].localPosition = Vector3(pose.tx(),pose.ty(),pose.tz())
                        nodes[0].localRotation = Quaternion(pose.qx(),pose.qy(),pose.qz(),pose.qw())

                        nodes[1].localPosition = Vector3(pose.tx(),pose.ty(),pose.tz())
                        nodes[1].localRotation = Quaternion(pose.qx(),pose.qy(),pose.qz(),pose.qw())

                        pose = Pose.makeTranslation(0.0f,2.0f,0.0f)
                        nodes[2].localPosition = Vector3(pose.tx(),pose.ty(),pose.tz())
                        nodes[2].localRotation = Quaternion(pose.qx(),pose.qy(),pose.qz(),pose.qw())

                        nodes[3].localPosition = Vector3(pose.tx(),pose.ty(),pose.tz())
                        nodes[3].localRotation = Quaternion(pose.qx(),pose.qy(),pose.qz(),pose.qw())

                        pose = Pose.makeTranslation(3.0f,0.0f,0.0f)
                        nodes[4].localPosition = Vector3(pose.tx(),pose.ty(),pose.tz())
                        nodes[4].localRotation = Quaternion(pose.qx(),pose.qy(),pose.qz(),pose.qw())

                        nodes[5].localPosition = Vector3(pose.tx(),pose.ty(),pose.tz())
                        nodes[5].localRotation = Quaternion(pose.qx(),pose.qy(),pose.qz(),pose.qw())
                    }

                }
                3 -> elements = listOf("wave5.sfb", "model.sfb")
                4 -> elements = listOf("personaB0.sfb", "model.sfb")
                else -> {
                    var point = getScreenCenter()
                    elements = listOf("personaB0.sfb", "model.sfb")
                    val hits = frame.hitTest(point.x.toFloat(), point.y.toFloat())
                    for (hit in hits) {
                        val trackable = hit.trackable
                        if (trackable is Plane && trackable.isPoseInPolygon(hit.hitPose)) {
                            placeFloor(fragment, hit.createAnchor(), Uri.parse(elements.get(0)))
                            placeFloor(fragment, hit.createAnchor(), Uri.parse(elements.get(1)))
                            break
                        }
                    }
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


        nodes.add(staticNode)
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


        //transformableNode.renderable = renderable
        //transformableNode.setParent(anchorNode)
        fragment.arSceneView.scene.addChild(anchorNode)
        transformableNode.select()
    }

    private fun getScreenCenter(): android.graphics.Point {
        val vw = findViewById<View>(android.R.id.content)
        return Point(vw.width / 2, vw.height / 2)
    }
    private fun setPosition(): android.graphics.Point{
        val vw = findViewById<View>(android.R.id.content)
        when (counter) {
            0 -> {
                return Point(-vw.width / 2, vw.height / 2)
            }
            1 -> {
                return Point(-vw.width / 2, -vw.height / 2)
            }
            2 -> {
                return Point(vw.width / 2, -vw.height / 2)
            }
            else -> {
                return Point(-vw.width / 2, vw.height / 2)
            }
        }
    }
}