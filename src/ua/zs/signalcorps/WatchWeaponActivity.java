package ua.zs.signalcorps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import ua.zs.elements.Rank;
import ua.zs.elements.Weapon;

public class WatchWeaponActivity extends Activity {

    private Weapon weapon;
    private final static int[] availableImages = { R.drawable.ak74, R.drawable.ak74m, R.drawable.akmsu,
                                                   R.drawable.aks74, R.drawable.aks74u, R.drawable.igla,
                                                   R.drawable.nrs, R.drawable.pm, R.drawable.rpg7,
                                                   R.drawable.rpk74, R.drawable.sgd };
    private final static String[] availableWeapon = { "АК-74", "АК-74М", "АКМСУ",
                                                      "АКС-74", "АКС-74У", "ПЗРК \"ІГЛА\"",
                                                      "НРС", "9-мм ПМ", "РПГ-7",
                                                      "РПК-74", "СГД" };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_weapon);
        initiateActionBarIconButton();
        Intent intent = getIntent();
        SignalCorpsDB db = new SignalCorpsDB(WatchWeaponActivity.this);
        weapon = db.getWeaponById(intent.getIntExtra("weapon", 0));
        initiateWeaponPageElements();
    }

    private void initiateWeaponPageElements() {
        ImageView weaponImage = (ImageView) findViewById(R.id.weaponImage);
        TextView id = (TextView) findViewById(R.id.idTextView);
        TextView model = (TextView) findViewById(R.id.modelTextView);
        TextView owner = (TextView) findViewById(R.id.ownerTextView);
        id.setText(" №" + String.valueOf(weapon.getId()));
        model.setText(" " + weapon.getModel());
        owner.setText(" " + Rank.toString(this, weapon.getOwner().getRank()).toLowerCase() +
                      " " + weapon.getOwner().toString());
        for (int i = 0; i < availableWeapon.length; i++) {
            if(weapon.getModel().startsWith(availableWeapon[i])) {
                weaponImage.setImageResource(availableImages[i]);
                break;
            }
        }
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
        if(weapon.getOwner().getRank() <= HomeActivity.user.getRank()) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.confirmation)
                    .setMessage(R.string.confirmation_message)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SignalCorpsDB dataBase = new SignalCorpsDB(WatchWeaponActivity.this);
                            dataBase.deleteWeapon(weapon.getId());
                            WatchWeaponActivity.this.finish();
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        } else {
            Toast.makeText(WatchWeaponActivity.this, R.string.no_permission, Toast.LENGTH_LONG)
                    .show();
        }
    }

}