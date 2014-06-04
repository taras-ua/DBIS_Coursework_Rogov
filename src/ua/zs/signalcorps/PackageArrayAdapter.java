package ua.zs.signalcorps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import ua.zs.elements.Classified;
import ua.zs.elements.Contact;
import ua.zs.elements.Package;

import java.util.List;

public class PackageArrayAdapter extends ArrayAdapter<Package> {

    private List<Package> list;
    private Context context;
    private Contact contact;

    public PackageArrayAdapter(Context context, List<Package> list, Contact contact) {
        super(context, R.layout.list_package, list);
        this.list = list;
        this.context = context;
        this.contact = contact;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_package, parent, false);
        final TextView id = (TextView) rowView.findViewById(R.id.idView);
        final TextView classified = (TextView) rowView.findViewById(R.id.classifiedView);
        final ImageButton deleteButton = (ImageButton) rowView.findViewById(R.id.deleteButton);
        final int pos = position;
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list.get(pos).getDelivery().getEquipage().getCommander().getRank() <=
                        HomeActivity.user.getRank()) {
                    new AlertDialog.Builder(context)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(R.string.confirmation)
                            .setMessage(R.string.confirmation_message)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(contact.getEquipage().getCommander().getRank() <= HomeActivity.user.getRank()) {
                                        if(list.get(pos).getClassified() <= HomeActivity.user.getClassified()) {
                                            SignalCorpsDB dataBase = new SignalCorpsDB(context);
                                            dataBase.deletePackage(list.get(pos).getId());
                                            classified.setText("-");
                                            id.setText("");
                                            deleteButton.setEnabled(false);
                                        } else {
                                            Toast.makeText(context, R.string.classified_error, Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    } else {
                                        Toast.makeText(context, R.string.no_permission, Toast.LENGTH_LONG)
                                                .show();
                                    }
                                }
                            })
                            .setNegativeButton(R.string.no, null)
                            .show();
                } else {
                    Toast.makeText(context, R.string.no_permission, Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
        id.setText(list.get(position).toString());
        classified.setText(Classified.packageType(context, list.get(position).getClassified()) + " ");
        return rowView;
    }

}
