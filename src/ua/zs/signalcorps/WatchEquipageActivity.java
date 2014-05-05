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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ua.zs.elements.Equipage;
import ua.zs.elements.Rank;

public class WatchEquipageActivity extends Activity {

    private Equipage equipage;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_equipage);
        initiateActionBarIconButton();
        Intent intent = getIntent();
        SignalCorpsDB db = new SignalCorpsDB(WatchEquipageActivity.this);
        equipage = db.getEquipageById(intent.getIntExtra("equipage", 0));
        initiateEquipagePageElements();
        initiatePeopleView();
    }

    private void initiateEquipagePageElements() {
        TextView id = (TextView) findViewById(R.id.idTextView);
        TextView commander = (TextView) findViewById(R.id.commanderTextView);
        id.setText(" №" + String.valueOf(equipage.getId()));
        commander.setText(" " + Rank.toString(this, equipage.getCommander().getRank()).toLowerCase() +
                " " + equipage.getCommander().toString());
    }

    private void initiatePeopleView() {
        ListView list = (ListView) findViewById(R.id.peopleListView);
        SignalCorpsDB dataBase = new SignalCorpsDB(this);
        PersonArrayAdapter adapter = new PersonArrayAdapter(this,
                dataBase.getPersonsFromEquipage(equipage.getId()), false);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String chosenUser = ((TextView) view.findViewById(R.id.secretNameView)).getText().toString();
                Intent watch;
                if(chosenUser.equals(HomeActivity.user.getSecretName())) {
                    watch = new Intent(WatchEquipageActivity.this, HomeActivity.class);
                } else {
                    watch = new Intent(WatchEquipageActivity.this, WatchPersonActivity.class);
                    watch.putExtra("user", ((TextView) view.findViewById(R.id.secretNameView)).getText().toString());
                }
                startActivity(watch);
            }
        });
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
        if(equipage.getCommander().getRank() <= HomeActivity.user.getRank()) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.confirmation)
                    .setMessage(R.string.confirmation_message)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SignalCorpsDB dataBase = new SignalCorpsDB(WatchEquipageActivity.this);
                            dataBase.deleteEquipage(equipage.getId());
                            WatchEquipageActivity.this.finish();
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        } else {
            Toast.makeText(WatchEquipageActivity.this, R.string.no_permission, Toast.LENGTH_LONG)
                    .show();
        }
    }

}
