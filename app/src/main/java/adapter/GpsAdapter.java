package adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import mobile.instachat.android2practicalexam.Geo;
import mobile.instachat.android2practicalexam.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GpsAdapter extends ArrayAdapter<Geo> {

    List<Geo> list;

    public GpsAdapter(Context context, List<Geo> objects) {
        super(context, R.layout.listview_gps, objects);
        list = objects;
    }

    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View gpsView = inflater.inflate(R.layout.listview_gps, viewGroup, false);

        TextView geo = gpsView.findViewById(R.id.textViewGeo);
        TextView date = gpsView.findViewById(R.id.textViewDate);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();

        String strDate = dateFormat.format(cal.getTime());
        date.setText(strDate);
        geo.setText("Lat:" + list.get(i).getLat() + "/ Lon:" + list.get(i).getLat());

        return gpsView;
    }

}
