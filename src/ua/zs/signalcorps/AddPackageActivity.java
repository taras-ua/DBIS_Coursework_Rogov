package ua.zs.signalcorps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import ua.zs.elements.CourierContact;
import ua.zs.elements.Package;

public class AddPackageActivity extends Activity {

    private static int contact;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_package);
        initiateActionBarIconButton();
        initiateSpinner();
        initiateButtons();
        Intent intent = getIntent();
        contact = intent.getIntExtra("contact", 0);
        setTitle(getResources().getString(R.string.add_package) + String.valueOf(contact));
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
                String idString = String.valueOf(((EditText) findViewById(R.id.idText)).getText());
                int id = Integer.parseInt(idString);
                int classified = ((Spinner) findViewById(R.id.classifiedText)).getSelectedItemPosition();
                SignalCorpsDB db = new SignalCorpsDB(AddPackageActivity.this);
                if( !idString.equals("") && classified != 5) {
                    if(classified <= HomeActivity.user.getClassified()) {
                        if (id > 0) {
                            if (!db.addPackage(new Package(id, (CourierContact) db.getContactById(contact),
                                    classified))) {
                                Toast.makeText(AddPackageActivity.this, R.string.id_exists_error, Toast.LENGTH_LONG)
                                        .show();
                            } else {
                                setResult(RESULT_OK);
                                AddPackageActivity.this.finish();
                            }
                        } else {
                            Toast.makeText(AddPackageActivity.this, R.string.id_error, Toast.LENGTH_LONG)
                                    .show();
                        }
                    } else {
                        Toast.makeText(AddPackageActivity.this, R.string.classified_error, Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(AddPackageActivity.this, R.string.needed_error, Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
        Button cancelButton = (Button) findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void initiateSpinner() {
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
        classAdapter.add(getResources().getString(R.string.UNCLASSIFIED_PACKAGE));
        classAdapter.add(getResources().getString(R.string.CONFIDENTIAL_PACKAGE));
        classAdapter.add(getResources().getString(R.string.SECRET_PACKAGE));
        classAdapter.add(getResources().getString(R.string.TOP_SECRET_PACKAGE));
        classAdapter.add(getResources().getString(R.string.classified)); // Spinner hint hack
        classSpinner.setAdapter(classAdapter);
        classSpinner.setSelection(classAdapter.getCount());
    }

}