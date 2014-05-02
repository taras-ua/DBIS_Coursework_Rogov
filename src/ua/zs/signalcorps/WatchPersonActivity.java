package ua.zs.signalcorps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import ua.zs.elements.Person;
import ua.zs.elements.Rank;

public class WatchPersonActivity extends Activity {

    private Person user;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_person);
        initiateActionBarIconButton();
        Intent intent = getIntent();
        SignalCorpsDB db = new SignalCorpsDB(WatchPersonActivity.this);
        user = db.getPersonBySecretName(intent.getStringExtra("user"));
        initiatePersonPageElements();
        initiateWeaponView();
    }

    private void initiatePersonPageElements() {
        ImageView rankImage = (ImageView) findViewById(R.id.rankView);
        TextView rankText = (TextView) findViewById(R.id.rankTextView);
        TextView name = (TextView) findViewById(R.id.nameView);
        TextView secret = (TextView) findViewById(R.id.secretNameView);
        Button equipage = (Button) findViewById(R.id.equipageButton);
        rankImage.setImageResource(Rank.toImage(user.getRank()));
        rankText.setText(Rank.toString(this, user.getRank()));
        name.setText(user.getSecondName() + " " +
                user.getFirstName() + " " +
                user.getFathersName());
        secret.setText(user.getSecretName());
        equipage.setText(user.getEquipage() != 0 ?
                getResources().getString(R.string.equipage_id) +
                        String.valueOf(user.getEquipage()) :
                getResources().getString(R.string.no_equipage));
        equipage.setEnabled(user.getEquipage() != 0);
        equipage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
    }

    private void initiateWeaponView() {
        ListView list = (ListView) findViewById(R.id.myWeaponView);
        SignalCorpsDB dataBase = new SignalCorpsDB(this);
        WeaponArrayAdapter adapter = new WeaponArrayAdapter(this,
                dataBase.getWeaponOfPerson(user.getSecretName()), false);
        list.setAdapter(adapter);
        TextView emptyList = (TextView) findViewById(R.id.noWeaponText);
        emptyList.setVisibility(list.getCount() > 0 ? View.GONE : View.VISIBLE);
    }

    private void initiateActionBarIconButton() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.watch_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.delete:
                deleteFromDB();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteFromDB() {
        if(user.getRank() <= HomeActivity.user.getRank()) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.confirmation)
                    .setMessage(R.string.confirmation_message)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SignalCorpsDB dataBase = new SignalCorpsDB(WatchPersonActivity.this);
                            dataBase.deletePerson(user.getSecretName());
                            WatchPersonActivity.this.finish();
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        } else {
            Toast.makeText(WatchPersonActivity.this, R.string.no_permission, Toast.LENGTH_LONG)
                    .show();
        }
    }

}