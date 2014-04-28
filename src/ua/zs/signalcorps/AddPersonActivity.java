package ua.zs.signalcorps;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import ua.zs.elements.Person;

public class AddPersonActivity extends ActionBarActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_person);
        initiateActionBarIconButton();
        initiateSpinners();
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
                String secretName = String.valueOf(((EditText) findViewById(R.id.loginnameText)).getText());
                String name = String.valueOf(((EditText) findViewById(R.id.nameText)).getText());
                String surname = String.valueOf(((EditText) findViewById(R.id.surnameText)).getText());
                String fatherName = String.valueOf(((EditText) findViewById(R.id.fathernameText)).getText());
                String equipageText = String.valueOf(((EditText) findViewById(R.id.equipageText)).getText());
                int equipage = Integer.valueOf( !equipageText.equals("") ? equipageText : "0" );
                String password = String.valueOf(((EditText) findViewById(R.id.passwordText)).getText());
                String repeatedPassword = String.valueOf(((EditText) findViewById(R.id.repeatPasswordText)).getText());
                int rank = ((Spinner) findViewById(R.id.rankText)).getSelectedItemPosition();
                int classified = ((Spinner) findViewById(R.id.classifiedText)).getSelectedItemPosition();
                SignalCorpsDB db = new SignalCorpsDB(AddPersonActivity.this);
                if( !secretName.equals("") && db.getPersonBySecretName(secretName) == null &&
                        !name.equals("") && !surname.equals("") &
                        rank < 20 && classified < 4 &&
                        !password.equals("") && !repeatedPassword.equals("") ) {
                    if(equipage >= 0) {
                        if(password.equals(repeatedPassword)) {
                            if(!db.addPerson(new Person(secretName, name, fatherName, surname,
                                                    rank, equipage, password, classified))) {
                                Toast.makeText(AddPersonActivity.this, R.string.exists_error, Toast.LENGTH_LONG)
                                        .show();
                            } else {
                                AddPersonActivity.this.finish();
                            }
                        } else {
                            Toast.makeText(AddPersonActivity.this, R.string.repassord_error, Toast.LENGTH_LONG)
                                    .show();
                        }
                    } else {
                        Toast.makeText(AddPersonActivity.this, R.string.equipage_error, Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(AddPersonActivity.this, R.string.needed_error, Toast.LENGTH_LONG)
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

    private void initiateSpinners() {
        Spinner rankSpinner = (Spinner) findViewById(R.id.rankText);        
        ArrayAdapter<String> rankAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item) {
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
                return super.getCount()-1; // Last item is used as hint
            }
        };
        rankAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        rankAdapter.add(getResources().getString(R.string.CIVILIAN));
        rankAdapter.add(getResources().getString(R.string.SOLDIER));
        rankAdapter.add(getResources().getString(R.string.SENIOR_SOLDIER));
        rankAdapter.add(getResources().getString(R.string.JUNIOR_SERGEANT));
        rankAdapter.add(getResources().getString(R.string.SERGEANT));
        rankAdapter.add(getResources().getString(R.string.SENIOR_SERGEANT));
        rankAdapter.add(getResources().getString(R.string.STARSHYNA));
        rankAdapter.add(getResources().getString(R.string.PRAPORSHCHYK));
        rankAdapter.add(getResources().getString(R.string.SENIOR_PRAPORSHCHYK));
        rankAdapter.add(getResources().getString(R.string.JUNIOR_LIEUTENANT));
        rankAdapter.add(getResources().getString(R.string.LIEUTENANT));
        rankAdapter.add(getResources().getString(R.string.SENIOR_LIEUTENANT));
        rankAdapter.add(getResources().getString(R.string.CAPTAIN));
        rankAdapter.add(getResources().getString(R.string.MAJOR));
        rankAdapter.add(getResources().getString(R.string.PIDPOLKOVNYK));
        rankAdapter.add(getResources().getString(R.string.POLKOVNYK));
        rankAdapter.add(getResources().getString(R.string.GENERAL_MAJOR));
        rankAdapter.add(getResources().getString(R.string.GENERAL_LIEUTENANT));
        rankAdapter.add(getResources().getString(R.string.GENERAL_POLKOVNYK));
        rankAdapter.add(getResources().getString(R.string.GENERAL_OF_ARMY));
        rankAdapter.add(getResources().getString(R.string.rank)); // Spinner hint hack
        rankSpinner.setAdapter(rankAdapter);
        rankSpinner.setSelection(rankAdapter.getCount());
        Spinner classSpinner = (Spinner) findViewById(R.id.classifiedText);
        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(R.id.textView)).setText("");
                    ((TextView)v.findViewById(R.id.textView)).setHint(getItem(getCount())); // Hint
                }
                return v;
            }
            @Override
            public int getCount() {
                return super.getCount()-1; // Last item is used as hint
            }
        };
        classAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        classAdapter.add(getResources().getString(R.string.UNCLASSIFIED));
        classAdapter.add(getResources().getString(R.string.CONFIDENTIAL));
        classAdapter.add(getResources().getString(R.string.SECRET));
        classAdapter.add(getResources().getString(R.string.TOP_SECRET));
        classAdapter.add(getResources().getString(R.string.classified)); // Spinner hint hack
        classSpinner.setAdapter(classAdapter);
        classSpinner.setSelection(classAdapter.getCount());
    }

}