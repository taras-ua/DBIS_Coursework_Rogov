package ua.zs.signalcorps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import ua.zs.elements.Contact;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WatchContactActivity extends Activity {

    private Contact contact;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_contact);
        initiateActionBarIconButton();
        Intent intent = getIntent();
        SignalCorpsDB db = new SignalCorpsDB(WatchContactActivity.this);
        contact = db.getContactById(intent.getIntExtra("contact", 0));
        initiateContactPageElements();
        if(!contact.getReceiver().equals("")) {
            initiatePackagesView();
        }
    }

    private void initiateContactPageElements() {
        ImageView typeImage = (ImageView) findViewById(R.id.typeView);
        TextView typeText = (TextView) findViewById(R.id.typeTextView);
        TextView id = (TextView) findViewById(R.id.idView);
        TextView startText = (TextView) findViewById(R.id.startText);
        TextView finishText = (TextView) findViewById(R.id.finishText);
        TextView infoText = (TextView) findViewById(R.id.infoText);
        Button equipage = (Button) findViewById(R.id.equipageButton);
        Button endButton = (Button) findViewById(R.id.buttonEnd);
        if(contact.getNode() > -1) {
            typeImage.setImageResource(R.drawable.ic_wired);
            typeText.setText(R.string.contact_wired);
            infoText.setText(getResources().getString(R.string.wired_hint) + ": "
                    + String.valueOf(contact.getNode()));
        }
        if(!contact.getSatellite().equals("")) {
            typeImage.setImageResource(R.drawable.ic_satellite);
            typeText.setText(R.string.contact_satellite);
            infoText.setText(getResources().getString(R.string.satellite_hint) + ": "
                    + contact.getSatellite());
        }
        if(contact.getAzimuth() > -1.0) {
            typeImage.setImageResource(R.drawable.ic_radiorelated);
            typeText.setText(R.string.contact_radiorelated);
            infoText.setText(getResources().getString(R.string.radiorelated_hint) + ": "
                    + String.valueOf(contact.getAzimuth()));
        }
        if(contact.getFrequency() > -1) {
            typeImage.setImageResource(R.drawable.ic_radio);
            typeText.setText(R.string.contact_radio);
            infoText.setText(getResources().getString(R.string.radio_hint) + ": "
                    + String.valueOf(contact.getFrequency()));
        }
        if(!contact.getReceiver().equals("")) {
            typeImage.setImageResource(R.drawable.ic_courier);
            typeText.setText(R.string.contact_courier);
            infoText.setText(getResources().getString(R.string.courier_hint) + ": "
                    + contact.getReceiver());
        }
        id.setText(contact.toString());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String timeStart = simpleDateFormat.format(contact.getStartTime());
        startText.setText(getResources().getString(R.string.start_time) + " " + timeStart);
        try {
            String timeFinish = simpleDateFormat.format(contact.getEndTime());
            finishText.setText(getResources().getString(R.string.finish_time) + " " + timeFinish);
            finishText.setVisibility(View.VISIBLE);
            endButton.setVisibility(View.GONE);
        } catch(Exception e) {
            Log.i("ContactView", "Not finished contact added.");
        }
        equipage.setText(getResources().getString(R.string.equipage_id) +
                String.valueOf(contact.getEquipage().getId()));
        equipage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent watch = new Intent(WatchContactActivity.this, WatchEquipageActivity.class);
                watch.putExtra("equipage", contact.getEquipage().getId());
                startActivity(watch);
            }
        });
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contact.getEquipage().getId() == HomeActivity.user.getEquipage()) {
                    if(contact.getStartTime().getTime() < Calendar.getInstance().getTime().getTime()) {
                        new AlertDialog.Builder(WatchContactActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle(R.string.confirmation)
                                .setMessage(R.string.confirmation_end)
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        contact.finish();
                                        SignalCorpsDB db = new SignalCorpsDB(WatchContactActivity.this);
                                        db.finishContact(contact.getId(), contact.getEndTime());
                                        initiateContactPageElements();
                                    }
                                })
                                .setNegativeButton(R.string.no, null)
                                .show();
                    } else {
                        Toast.makeText(WatchContactActivity.this, R.string.end_contact_error, Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(WatchContactActivity.this, R.string.not_from_equipage, Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    private void initiatePackagesView() {
        findViewById(R.id.packageHeadlineText).setVisibility(View.VISIBLE);
        ListView list = (ListView) findViewById(R.id.packageView);
        list.setVisibility(View.VISIBLE);
        SignalCorpsDB dataBase = new SignalCorpsDB(this);
        PackageArrayAdapter adapter = new PackageArrayAdapter(this,
                dataBase.getPackaegOfContact(contact.getId()), contact);
        list.setAdapter(adapter);
        Button addButton = (Button) findViewById(R.id.buttonAdd);
        addButton.setVisibility(View.VISIBLE);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contact.getEquipage().getCommander().getRank() <= HomeActivity.user.getRank()) {
                    Intent intent = new Intent(WatchContactActivity.this, AddPackageActivity.class);
                    intent.putExtra("contact", contact.getId());
                    startActivityForResult(intent, 1);
                } else {
                    Toast.makeText(WatchContactActivity.this, R.string.no_permission, Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK) {
            initiatePackagesView();
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
        if(contact.getEquipage().getCommander().getRank() <= HomeActivity.user.getRank()) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.confirmation)
                    .setMessage(R.string.confirmation_message)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SignalCorpsDB dataBase = new SignalCorpsDB(WatchContactActivity.this);
                            dataBase.cancelContact(contact.getId());
                            WatchContactActivity.this.finish();
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        } else {
            Toast.makeText(WatchContactActivity.this, R.string.no_permission, Toast.LENGTH_LONG)
                    .show();
        }
    }

}