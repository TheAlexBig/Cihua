package com.bigdreamscoders.cihua

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import kotlinx.android.synthetic.main.activity_main.*
import android.annotation.SuppressLint
import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.util.DisplayMetrics
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.annotation.NonNull
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.ScrollView
import kotlinx.android.synthetic.main.activity_main.view.*
import java.lang.Exception
import android.view.MotionEvent
import android.view.View


class MainActivity : AppCompatActivity() {

    val locationManager: LocationManager? = null
    val listener: LocationListener? = null
    var previousL: Location? = null
    //lateinit var gpsI:ImageView
    val imageMapWidth = 751
    val imageMapHeigh = 935
    // val imageGps = gps
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        //gpsI = gps
        //gpsI.animate().translationX(imageMapWidth.toFloat())
        //gpsI.animate().translationY(imageMapHeigh.toFloat())
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Define a listener that responds to location updates
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {

                if(previousL==null){
                    //imageGps.animate().translationX(imageMapWidth.toFloat())
                    //imageGps.animate().translationY(imageMapHeigh.toFloat())
                    previousL=location
                }
                else{
                    if(previousL?.latitude!=location.latitude){

                    }
                    if(previousL?.longitude!=location.longitude){

                    }
                    previousL = location
                }
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            }

            override fun onProviderEnabled(provider: String) {
            }

            override fun onProviderDisabled(provider: String) {
            }
        }
        if(requestGps()){
            try {
                locationManager.requestLocationUpdates(android.location.LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)
            }
            catch (e: Exception) {
                // Must be safe
            }
        }
        callanimation.setOnClickListener {
            startActivity(Intent(this.baseContext, AnimationActivity::class.java))
        }

        val windowwidth = windowManager.defaultDisplay.width
        val windowheight = windowManager.defaultDisplay.height
        val balls = findViewById<View>(R.id.mapa)
        


        
        balls.setOnTouchListener(object : View.OnTouchListener {
            
            override 
            fun onTouch(v: View, event: MotionEvent): Boolean {
                val layoutParams = balls.layoutParams as ActionBar.LayoutParams
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                    }
                    MotionEvent.ACTION_MOVE -> {
                        var x_cord = event.rawX.toInt()
                        var y_cord = event.rawY.toInt()

                        if (x_cord > windowwidth) {
                            x_cord = windowwidth
                        }
                        if (y_cord > windowheight) {
                            y_cord = windowheight
                        }

                        layoutParams.leftMargin = x_cord - 25
                        layoutParams.topMargin = y_cord - 75

                        balls.layoutParams = layoutParams
                    }
                    else -> {
                    }
                }
                return true
            }
        })
    }

    fun requestGps(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET
                    ), 10
                )
                return false
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 10)
                return false
            }
        }
        return true
    }
    /*
    private fun double getCurrentPixelY(Location upperLeft, Location lowerRight, Location current) {
        double hypotenuse = upperLeft.distanceTo(current);
        double bearing = upperLeft.bearingTo(current);
        double currentDistanceY = Math.cos(bearing * Math.PI / OneEightyDeg) * hypotenuse;
        //                           "percentage to mark the position"
        double totalHypotenuse = upperLeft.distanceTo(lowerRight);
        double totalDistanceY = totalHypotenuse * Math.cos(upperLeft.bearingTo(lowerRight) * Math.PI / OneEightyDeg);
        double currentPixelY = currentDistanceY / totalDistanceY * ImageSizeH;

        return currentPixelY;
    }

    public double getCurrentPixelX(Location upperLeft, Location lowerRight, Location current) {
        double hypotenuse = upperLeft.distanceTo(current);
        double bearing = upperLeft.bearingTo(current);
        double currentDistanceX = Math.sin(bearing * Math.PI / OneEightyDeg) * hypotenuse;
        //                           "percentage to mark the position"
        double totalHypotenuse = upperLeft.distanceTo(lowerRight);
        double totalDistanceX = totalHypotenuse * Math.sin(upperLeft.bearingTo(lowerRight) * Math.PI / OneEightyDeg);
        double currentPixelX = currentDistanceX / totalDistanceX * ImageSizeW;

        return currentPixelX;
    }*/
}
