package ua.zs.signalcorps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ua.zs.elements.*;
import java.util.List;

public class WeaponArrayAdapter extends ArrayAdapter<Weapon> {

    private List<Weapon> list;
    private Context context;
    private boolean showOwner;

    public WeaponArrayAdapter(Context context, List<Weapon> list, boolean showOwner) {
        super(context, R.layout.list_weapon, list);
        this.list = list;
        this.context = context;
        this.showOwner = showOwner;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_weapon, parent, false);
        TextView model = (TextView) rowView.findViewById(R.id.modelView);
        TextView id = (TextView) rowView.findViewById(R.id.numberView);
        TextView owner = (TextView) rowView.findViewById(R.id.ownerView);
        model.setText(list.get(position).getModel());
        id.setText(context.getString(R.string.number_sym) + String.valueOf(list.get(position).getId()));
        owner.setText(showOwner ? ownerText(list.get(position).getOwner()) : "");
        return rowView;
    }
    
    private String ownerText(Person person) {
        return context.getString(R.string.owner) +
                             person.getSecondName() + " " +
                             person.getFirstName() + " " +
                             person.getFathersName();
    }

}
