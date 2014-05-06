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
import ua.zs.elements.Transport;

import java.text.SimpleDateFormat;

public class WatchTransportActivity extends Activity {

    private Transport transport;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_transport);
        initiateActionBarIconButton();
        Intent intent = getIntent();
        SignalCorpsDB db = new SignalCorpsDB(WatchTransportActivity.this);
        transport = db.getTransportById(intent.getIntExtra("transport", 0));
        initiateWeaponPageElements();
    }

    private void initiateWeaponPageElements() {
        ImageView transportImage = (ImageView) findViewById(R.id.transportImage);
        TextView id = (TextView) findViewById(R.id.idTextView);
        TextView model = (TextView) findViewById(R.id.modelTextView);
        TextView owner = (TextView) findViewById(R.id.ownerTextView);
        TextView techView = (TextView) findViewById(R.id.techTextView);
        id.setText(" №" + String.valueOf(transport.getId()));
        model.setText(" " + transport.getModel());
        owner.setText(String.valueOf(transport.getOwner().getId()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        techView.setText(" " + simpleDateFormat.format(transport.getLastTechwork()));
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
        if(transport.getOwner().getCommander().getRank() <= HomeActivity.user.getRank()) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.confirmation)
                    .setMessage(R.string.confirmation_message)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SignalCorpsDB dataBase = new SignalCorpsDB(WatchTransportActivity.this);
                            dataBase.deleteTransport(transport.getId());
                            WatchTransportActivity.this.finish();
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        } else {
            Toast.makeText(WatchTransportActivity.this, R.string.no_permission, Toast.LENGTH_LONG)
                    .show();
        }
    }

}