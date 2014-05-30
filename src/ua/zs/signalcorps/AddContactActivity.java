package ua.zs.signalcorps;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import ua.zs.elements.*;

import java.util.Date;

public class AddContactActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);
        initiateActionBarIconButton();
        initiateButtons();
        initiateSpinners();
        findViewById(R.id.infoText).setEnabled(false);
    }

    private void initiateActionBarIconButton() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    private void initiateSpinners() {
        Spinner typeSpinner = (Spinner) findViewById(R.id.typeText);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(R.id.textView)).setText("");
                    ((TextView) v.findViewById(R.id.textView)).setHint(getItem(getCount())); // Hint
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // Last item is used as hint
            }
        };
        typeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        typeAdapter.add(getResources().getString(R.string.contact_wired));
        typeAdapter.add(getResources().getString(R.string.contact_satellite));
        typeAdapter.add(getResources().getString(R.string.contact_radiorelated));
        typeAdapter.add(getResources().getString(R.string.contact_radio));
        typeAdapter.add(getResources().getString(R.string.contact_courier));
        typeAdapter.add(getResources().getString(R.string.contact_type)); // Spinner hint hack
        typeSpinner.setAdapter(typeAdapter);
        typeSpinner.setSelection(typeAdapter.getCount());
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                EditText param = (EditText) findViewById(R.id.infoText);
                switch(i) {
                    case 0:
                        param.setHint(R.string.wired_hint);
                        param.setInputType(InputType.TYPE_CLASS_NUMBER);
                        param.setText("");
                        param.setEnabled(true);
                        break;
                    case 1:
                        param.setHint(R.string.satellite_hint);
                        param.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                        param.setText("");
                        param.setEnabled(true);
                        break;
                    case 2:
                        param.setHint(R.string.radiorelated_hint);
                        param.setInputType(InputType.TYPE_CLASS_NUMBER);
                        param.setText("");
                        param.setEnabled(true);
                        break;
                    case 3:
                        param.setHint(R.string.radio_hint);
                        param.setInputType(InputType.TYPE_CLASS_NUMBER);
                        param.setText("");
                        param.setEnabled(true);
                        break;
                    case 4:
                        param.setHint(R.string.courier_hint);
                        param.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                        param.setText("");
                        param.setEnabled(true);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                EditText param = (EditText) findViewById(R.id.infoText);
                param.setHint(R.string.contact_param);
                param.setText("");
                param.setEnabled(false);
            }
        });
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
                String paramString = String.valueOf(((EditText) findViewById(R.id.infoText)).getText());
                Date startDate = new Date(((DatePicker) findViewById(R.id.timeText)).getCalendarView().getDate());
                Spinner type = (Spinner) findViewById(R.id.typeText);
                SignalCorpsDB db = new SignalCorpsDB(AddContactActivity.this);
                if( !idText.equals("") && !paramString.equals("") && !equipage.equals("")) {
                    if(id > 0) {
                        if(db.getEquipageById(idOwner) != null) {
                            if(db.getEquipageById(idOwner).getCommander().getRank() <= HomeActivity.user.getRank()) {
                                if(startDate.getTime() > new Date().getTime()) {
                                    switch(type.getSelectedItemPosition()) {
                                        case 0: if (!db.addContact(new WiredContact(id, db.getEquipageById(idOwner), startDate, Integer.parseInt(paramString)))) {
                                                    Toast.makeText(AddContactActivity.this, R.string.id_exists_error, Toast.LENGTH_LONG)
                                                            .show();
                                                } else {
                                                    AddContactActivity.this.finish();
                                                }
                                                break;
                                        case 1: if (!db.addContact(new SatelliteContact(id, db.getEquipageById(idOwner), startDate, paramString))) {
                                                    Toast.makeText(AddContactActivity.this, R.string.id_exists_error, Toast.LENGTH_LONG)
                                                            .show();
                                                } else {
                                                    AddContactActivity.this.finish();
                                                }
                                                break;
                                        case 2: if (!db.addContact(new RadioRelatedContact(id, db.getEquipageById(idOwner), startDate, Double.parseDouble(paramString)))) {
                                                    Toast.makeText(AddContactActivity.this, R.string.id_exists_error, Toast.LENGTH_LONG)
                                                            .show();
                                                } else {
                                                    AddContactActivity.this.finish();
                                                }
                                                break;
                                        case 3: if (!db.addContact(new RadioContact(id, db.getEquipageById(idOwner), startDate, Integer.parseInt(paramString)))) {
                                                    Toast.makeText(AddContactActivity.this, R.string.id_exists_error, Toast.LENGTH_LONG)
                                                            .show();
                                                } else {
                                                    AddContactActivity.this.finish();
                                                }
                                                break;
                                        case 4: if (!db.addContact(new CourierContact(id, db.getEquipageById(idOwner), startDate, paramString))) {
                                                    Toast.makeText(AddContactActivity.this, R.string.id_exists_error, Toast.LENGTH_LONG)
                                                            .show();
                                                } else {
                                                    AddContactActivity.this.finish();
                                                }
                                                break;
                                    }
                                } else {
                                    Toast.makeText(AddContactActivity.this, R.string.date_error_vv, Toast.LENGTH_LONG)
                                            .show();
                                }
                            } else {
                                Toast.makeText(AddContactActivity.this, R.string.no_permission, Toast.LENGTH_LONG)
                                        .show();
                            }
                        } else {
                            Toast.makeText(AddContactActivity.this, R.string.equipage_not_found, Toast.LENGTH_LONG)
                                    .show();
                        }
                    } else {
                        Toast.makeText(AddContactActivity.this, R.string.id_error, Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(AddContactActivity.this, R.string.needed_error, Toast.LENGTH_LONG)
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