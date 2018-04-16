package mobile.instachat.android2practicalexam

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var lat : Double? = null
    var lon : Double? = null
    var index : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val data = intent.extras
        lat = data.getDouble("lat")
        lon = data.getDouble("lon")
        index = data.getInt("index")

        println("MAP IS CREATED")

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        println("MAP IS READY")

        // Add a marker in Sydney and move the camera
        val position = LatLng(lat as Double, lon as Double)
        mMap.addMarker(MarkerOptions().position(position).title("Position #$index")).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 18.0f))
    }
}
