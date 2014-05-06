package ua.zs.signalcorps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ua.zs.elements.Transport;

import java.util.List;

public class TransportArrayAdapter extends ArrayAdapter<Transport> {

    private List<Transport> list;
    private Context context;
    private boolean showOwner;

    public TransportArrayAdapter(Context context, List<Transport> list, boolean showOwner) {
        super(context, R.layout.list_transport, list);
        this.list = list;
        this.context = context;
        this.showOwner = showOwner;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_transport, parent, false);
        TextView model = (TextView) rowView.findViewById(R.id.modelView);
        TextView id = (TextView) rowView.findViewById(R.id.numberView);
        TextView owner = (TextView) rowView.findViewById(R.id.equipageView);
        model.setText(list.get(position).getModel() + " ");
        id.setText(context.getString(R.string.number_sym) + String.valueOf(list.get(position).getId()));
        owner.setText(context.getResources().getString(R.string.equipage_car) +
                String.valueOf(list.get(position).getOwner().getId()));
        owner.setVisibility(showOwner ? View.VISIBLE : View.GONE);
        return rowView;
    }

}
