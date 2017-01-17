package utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gabrielnicolae.weatherapp.R;

public class CustomList extends ArrayAdapter<String> {
    private String[] ids;
    private String[] times;
    private String[] temps;
    private String[] hums;
    private String[] winds;
    private String[] press;
    private Activity context;

    public CustomList(Activity context, String[] ids, String[] times, String[] temps, String[] hums, String[] winds, String[] press) {
        super(context, R.layout.list_view_layout, ids);
        this.context = context;
        this.ids = ids;
        this.times = times;
        this.temps = temps;
        this.hums = hums;
        this.winds = winds;
        this.press = press;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_view_layout, null, true);
        TextView textViewId = (TextView) listViewItem.findViewById(R.id.textViewId);
        TextView textViewTime = (TextView) listViewItem.findViewById(R.id.textViewTime);
        TextView textViewTemp = (TextView) listViewItem.findViewById(R.id.textViewTemp);
        TextView textViewHum = (TextView) listViewItem.findViewById(R.id.textViewHum);
        TextView textViewWind = (TextView) listViewItem.findViewById(R.id.textViewWind);
        TextView textViewPress = (TextView) listViewItem.findViewById(R.id.textViewPress);

        textViewId.setText(ids[position]);
        textViewTime.setText(times[position]);
        textViewTemp.setText(temps[position]);
        textViewHum.setText(hums[position]);
        textViewWind.setText(winds[position]);
        textViewPress.setText(press[position]);

        return listViewItem;
    }
}