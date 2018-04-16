package mobile.instachat.android2practicalexam

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.AdapterView



class MainActivity : AppCompatActivity() {

    var service: ComponentName? = null
    var intentMyService: Intent? = null
    var receiver: BroadcastReceiver? = null
    var GPS_FILTER = "yev_GPS"
    var latitude: Double = 0.toDouble()
    var longitude: Double = 0.toDouble()
    var provider : String = ""
    var list = ArrayList<Geo>()
    var fragmentListView : FragmentListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //UI Elements
        //TODO:

        checkLocationPermission()
        getMyLocationServiceStarted()
        // register & define filter for local listener
        val myLocationFilter = IntentFilter(GPS_FILTER)
        receiver = MyMainLocalReceiver()
        registerReceiver(receiver, myLocationFilter)

        fragmentListView = FragmentListView.newInstance()
        fragmentListView?.setTheList(list)

        //list.add(Geo("TEST", "TEST"))

        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.fragmentMain, fragmentListView)
                .commit()


    }

    fun getMyLocationServiceStarted() {
        // get background service started
        intentMyService = Intent(this, service.MyGpsService::class.java)
        service = startService(intentMyService)
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            stopService(intentMyService)
            unregisterReceiver(receiver)
        } catch (e: Exception) {
            Log.e("MAIN-DESTROY>>>", e.message)
        }

        Log.e("MAIN-DESTROY>>>", "Adios")
    }//

    // local RECEIVER
    private inner class MyMainLocalReceiver : BroadcastReceiver() {
        override fun onReceive(localContext: Context, intentFilteredResponse: Intent) {
            latitude = intentFilteredResponse.getDoubleExtra("latitude", -1.0)
            longitude = intentFilteredResponse.getDoubleExtra("longitude", -1.0)
            provider = intentFilteredResponse.getStringExtra("provider")
            Log.e("MAIN>>>", java.lang.Double.toString(latitude))
            Log.e("MAIN>>>", java.lang.Double.toString(longitude))
            val msg = " lat: " + java.lang.Double.toString(latitude) + " " + " lon: " + java.lang.Double.toString(longitude)
            println("\n" + msg)
            list.add(Geo(latitude.toString(),longitude.toString()))
            fragmentListView?.dataChange()
            for (g in list){
                println("LATLON : ${g.lat} ${g.lon}")
            }
        }
    }

    fun drawGoogleMap(latitude : Double, longitude : Double)
    {
        val myGeoCode = "geo:"+latitude +"," + longitude +"?z=15";
        val intentViewMap = Intent(Intent.ACTION_VIEW, Uri.parse(myGeoCode))
        startActivity(intentViewMap)
    }

    fun checkLocationPermission() {
        //FIRST RUN OF THE APP -- USER NEEDS TO GIVE PERMISSIONS TO ALLOW THE DEVICE TO OBTAIN LOCATION
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
            Log.i("lat,lon", "NO PERMISSION")
            return
        }
    }

}
