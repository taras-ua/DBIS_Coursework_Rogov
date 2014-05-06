package ua.zs.signalcorps;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import ua.zs.elements.Transport;

import java.util.Date;

public class AddTransportActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transport);
        initiateActionBarIconButton();
        initiateButtons();
    }

    private void initiateActionBarIconButton() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    private void initiateButtons() {
        Button addButton = (Button) findViewById(R.id.buttonAdd);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String equipage = String.valueOf(((EditText) findViewById(R.id.ownerText)).getText());
                String idText = String.valueOf(((EditText) findViewById(R.id.idText)).getText());
                int id = Integer.valueOf( !idText.equals("") ? idText : "0" );
                int idOwner = Integer.valueOf( !equipage.equals("") ? equipage : "0" );
                String model = String.valueOf(((EditText) findViewById(R.id.modelText)).getText());
                Date techDate = new Date(((DatePicker) findViewById(R.id.techworkText)).getCalendarView().getDate());
                SignalCorpsDB db = new SignalCorpsDB(AddTransportActivity.this);
                if( !idText.equals("") && !model.equals("") && !equipage.equals("")) {
                    if(id > 0) {
                        if(db.getEquipageById(idOwner) != null) {
                            if(db.getEquipageById(idOwner).getCommander().getRank() <= HomeActivity.user.getRank()) {
                                if(techDate.getTime() <= new Date().getTime()) {
                                    if (!db.addTransport(new Transport(id, model, techDate, db.getEquipageById(idOwner)))) {
                                        Toast.makeText(AddTransportActivity.this, R.string.id_exists_error, Toast.LENGTH_LONG)
                                                .show();
                                    } else {
                                        AddTransportActivity.this.finish();
                                    }
                                } else {
                                    Toast.makeText(AddTransportActivity.this, R.string.date_error, Toast.LENGTH_LONG)
                                            .show();
                                }
                            } else {
                                Toast.makeText(AddTransportActivity.this, R.string.no_permission, Toast.LENGTH_LONG)
                                        .show();
                            }
                        } else {
                            Toast.makeText(AddTransportActivity.this, R.string.equipage_not_found, Toast.LENGTH_LONG)
                                    .show();
                        }
                    } else {
                        Toast.makeText(AddTransportActivity.this, R.string.id_error, Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(AddTransportActivity.this, R.string.needed_error, Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
        Button cancelButton = (Button) findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}