package com.bigdreamscoders.cihua

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import kotlinx.android.synthetic.main.activity_main.*
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.location.Location
import android.util.DisplayMetrics
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.annotation.NonNull
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.view.*
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    var previousL: Location? = null
    lateinit var widthVW: HorizontalScrollView
    lateinit var heightVW: ScrollView
    lateinit var gpsI:ImageView
    val imageMapWidth = 1871
    val imageMapHeigh = 1503
    var sector = 0
    @SuppressLint("MissingPermission", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val outLocation = IntArray(2)
        heightVW = scrollView
        widthVW = scrollViewVertical
        widthVW.scrollTo(0,imageMapWidth)
        heightVW.scrollTo(imageMapHeigh,0)
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Define a listener that responds to location updates
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {

                if(previousL==null){
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
            mapa.getLocationOnScreen(outLocation)
            outLocation[1] = scrollView.scrollY
            outLocation[0] = scrollViewVertical.scrollX
            //juego de la pelota y =  0..382
            if (outLocation.first() in 2806..3126 && outLocation[1]  in 0..558) sector = 4
            //Piramide
            if (outLocation.first() in  2632..2976 && outLocation[1]  in 1196..1698) sector = 3
            //Choza
            if (outLocation.first() in 1827..1993 && outLocation[1]  in 1135..1384) sector = 2
            //Templo
            if (outLocation.first() in 1153..1284 && outLocation[1]  in 400..554) sector = 1

            Toast.makeText(this.baseContext, " DX "+outLocation.first().toString()+" DY "+outLocation[1], Toast.LENGTH_LONG).show()

            val intent = Intent(this.baseContext, AnimationActivity::class.java)
            intent.putExtra("sector", sector)
            startActivity(intent)
        }
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

    private fun getScreenCenter(): android.graphics.Point {
        val vw = findViewById<View>(android.R.id.content)
        return Point(vw.width / 2, vw.height / 2)
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
