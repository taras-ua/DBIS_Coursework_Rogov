package ua.zs.signalcorps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ua.zs.elements.Contact;

import java.text.SimpleDateFormat;
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
        if(list.get(position).getEndTime() != null) {
            timeString += " => " + simpleDateFormat.format(list.get(position).getEndTime());
        }
        number.setText(list.get(position).toString());
        time.setText(timeString);
        equipage.setText(context.getString(R.string.equipage_id) +
                        String.valueOf(list.get(position).getEquipage().getId()));
        equipage.setVisibility(showEquipage ? View.VISIBLE : View.GONE);
        type.setImageResource(R.drawable.ic_action_contact);
        if(list.get(position).getNode() > -1) {
            //type.setImageResource(R.drawable.wired_contact);
        }
        if(!list.get(position).getSatellite().equals("")) {
            //type.setImageResource(R.drawable.satellite_contact);
        }
        if(list.get(position).getAzimuth() > -1.0) {
            //type.setImageResource(R.drawable.radiorelated_contact);
        }
        if(list.get(position).getFrequency() > -1) {
            //type.setImageResource(R.drawable.radio_contact);
        }
        if(!list.get(position).getReceiver().equals("")) {
            //type.setImageResource(R.drawable.courier_contact);
        }
        return rowView;
    }

}
