package ua.zs.signalcorps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ua.zs.elements.Equipage;
import ua.zs.elements.Rank;

import java.util.List;

public class EquipageArrayAdapter extends ArrayAdapter<Equipage> {

    private List<Equipage> list;
    private Context context;

    public EquipageArrayAdapter(Context context, List<Equipage> list) {
        super(context, R.layout.list_equipage, list);
        this.list = list;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_equipage, parent, false);
        TextView id = (TextView) rowView.findViewById(R.id.idEquipageText);
        TextView commander = (TextView) rowView.findViewById(R.id.commanderView);
        id.setText(context.getString(R.string.number_sym) + String.valueOf(list.get(position).getId()));
        commander.setText(" " + Rank.toString(context, list.get(position).getCommander().getRank()).toLowerCase() +
                " " + list.get(position).getCommander().toString());
        return rowView;
    }

}
