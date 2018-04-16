package mobile.instachat.android2practicalexam

import adapter.GpsAdapter
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_fragment_list_view.*
import kotlinx.android.synthetic.main.fragment_fragment_list_view.view.*


class FragmentListView : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    var gpsAdapter : GpsAdapter? = null

    var list : ArrayList<Geo>? = null

    fun setTheList(l : ArrayList<Geo>?){
        list = l
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_fragment_list_view, container, false)

        gpsAdapter = GpsAdapter(context, list)
        view.listViewGPS.adapter = gpsAdapter
        view.listViewGPS.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(context, "$position clicked!", Toast.LENGTH_SHORT).show()
            drawGoogleMap(list?.get(position)!!.lat.toDouble(), list?.get(position)?.lon!!.toDouble(), position+1)
        }

        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }


    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                FragmentListView().apply {
                }
    }

    fun dataChange(){
        gpsAdapter?.notifyDataSetChanged()
    }

    fun drawGoogleMap(latitude : Double, longitude : Double, index : Int)
    {
        /*val myGeoCode = "geo:"+latitude +"," + longitude +"?z=15";
        val intentViewMap = Intent(Intent.ACTION_VIEW, Uri.parse(myGeoCode))
        startActivity(intentViewMap)*/

        val intent = Intent(context, MapsActivity::class.java)

        intent.putExtra("lat", latitude)
        intent.putExtra("lon", longitude)
        intent.putExtra("index", index)

        context?.startActivity(intent)
    }
}
