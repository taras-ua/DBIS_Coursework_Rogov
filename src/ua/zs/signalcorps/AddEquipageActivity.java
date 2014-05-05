package ua.zs.signalcorps;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import ua.zs.elements.Equipage;

public class AddEquipageActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_equipage);
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
                String secretName = String.valueOf(((EditText) findViewById(R.id.ownerText)).getText());
                String idText = String.valueOf(((EditText) findViewById(R.id.idText)).getText());
                int id = Integer.valueOf( !idText.equals("") ? idText : "0" );
                SignalCorpsDB db = new SignalCorpsDB(AddEquipageActivity.this);
                if( !idText.equals("") && !secretName.equals("")) {
                    if(id >= 0) {
                        if(db.getPersonBySecretName(secretName) != null) {
                            if(db.getPersonBySecretName(secretName).getRank() <= HomeActivity.user.getRank()) {
                                if (!db.addEquipage(new Equipage(id, db.getPersonBySecretName(secretName)))) {
                                    Toast.makeText(AddEquipageActivity.this, R.string.id_exists_error, Toast.LENGTH_LONG)
                                            .show();
                                } else {
                                    AddEquipageActivity.this.finish();
                                }
                            } else {
                                Toast.makeText(AddEquipageActivity.this, R.string.no_permission, Toast.LENGTH_LONG)
                                        .show();
                            }
                        } else {
                            Toast.makeText(AddEquipageActivity.this, R.string.owner_not_found, Toast.LENGTH_LONG)
                                    .show();
                        }
                    } else {
                        Toast.makeText(AddEquipageActivity.this, R.string.id_error, Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(AddEquipageActivity.this, R.string.needed_error, Toast.LENGTH_LONG)
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