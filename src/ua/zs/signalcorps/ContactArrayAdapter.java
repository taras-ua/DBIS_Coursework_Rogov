package ua.zs.signalcorps;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ua.zs.elements.Contact;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ContactArrayAdapter extends ArrayAdapter<Contact> {

    private List<Contact> list;
    private Context context;
    private boolean showEquipage;

    public ContactArrayAdapter(Context context, List<Contact> list, boolean showEquipage) {
        super(context, R.layout.list_contact, list);
        this.list = list;
        this.context = context;
        this.showEquipage = showEquipage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_contact, parent, false);
        TextView number = (TextView) rowView.findViewById(R.id.numberView);
        TextView time = (TextView) rowView.findViewById(R.id.timeView);
        ImageView type = (ImageView) rowView.findViewById(R.id.typeView);
        TextView equipage = (TextView) rowView.findViewById(R.id.equipageView);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String timeString = simpleDateFormat.format(list.get(position).getStartTime());
        try {
            Date finishDate = list.get(position).getEndTime();
            timeString += " => " + simpleDateFormat.format(finishDate);
        } catch (Exception e) {
            Log.i("ContactArrayAdapter", "Adding not finished contact to ListView.");
        }
        number.setText(list.get(position).toString());
        time.setText(timeString);
        equipage.setText(context.getString(R.string.equipage_id) +
                        String.valueOf(list.get(position).getEquipage().getId()));
        equipage.setVisibility(showEquipage ? View.VISIBLE : View.GONE);
        type.setImageResource(R.drawable.ic_action_contact);
        if(list.get(position).getNode() > -1) {
            type.setImageResource(R.drawable.ic_wired);
        }
        if(!list.get(position).getSatellite().equals("")) {
            type.setImageResource(R.drawable.ic_satellite);
        }
        if(list.get(position).getAzimuth() > -1.0) {
            type.setImageResource(R.drawable.ic_radiorelated);
        }
        if(list.get(position).getFrequency() > -1) {
            type.setImageResource(R.drawable.ic_radio);
        }
        if(!list.get(position).getReceiver().equals("")) {
            type.setImageResource(R.drawable.ic_courier);
        }
        return rowView;
    }

}
