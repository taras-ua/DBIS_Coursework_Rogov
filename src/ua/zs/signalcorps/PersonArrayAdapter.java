package ua.zs.signalcorps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ua.zs.elements.Person;
import ua.zs.elements.Rank;

import java.util.List;

public class PersonArrayAdapter extends ArrayAdapter<Person> {

    private List<Person> list;
    private Context context;
    private boolean showEquipage;

    public PersonArrayAdapter(Context context, List<Person> list, boolean showEquipage) {
        super(context, R.layout.list_person, list);
        this.list = list;
        this.context = context;
        this.showEquipage = showEquipage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_person, parent, false);
        TextView name = (TextView) rowView.findViewById(R.id.nameView);
        ImageView rank = (ImageView) rowView.findViewById(R.id.rankView);
        TextView secret = (TextView) rowView.findViewById(R.id.secretNameView);
        TextView equipage = (TextView) rowView.findViewById(R.id.equipageView);
        name.setText(list.get(position).getSecondName() + " " +
                     list.get(position).getFirstName() + " " +
                     list.get(position).getFathersName());
        rank.setImageResource(Rank.toImage(list.get(position).getRank()));
        secret.setText(list.get(position).getSecretName());
        equipage.setText(list.get(position).getEquipage() != 0 ?
                            context.getString(R.string.equipage_id) +
                            String.valueOf(list.get(position).getEquipage()) :
                            "");
        equipage.setVisibility(showEquipage ? View.VISIBLE : View.GONE);
        return rowView;
    }

}
