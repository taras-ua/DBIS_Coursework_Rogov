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
        initiateTransportView();
        initiateContactsView();
    }

    private void initiateEquipagePageElements() {
        TextView id = (TextView) findViewById(R.id.idTextView);
        TextView commander = (TextView) findViewById(R.id.commanderTextView);
        id.setText(" №" + String.valueOf(equipage.getId()));
        commander.setText(" " + Rank.toString(this, equipage.getCommander().getRank()).toLowerCase() +
                " " + equipage.getCommander().toString());
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        final TabWidget tabWidget = tabHost.getTabWidget();
        final FrameLayout tabContent = tabHost.getTabContentView();
        // Get the original tab textviews and remove them from the viewgroup.
        TextView[] originalTextViews = new TextView[tabWidget.getTabCount()];
        for (int index = 0; index < tabWidget.getTabCount(); index++) {
            originalTextViews[index] = (TextView) tabWidget.getChildTabViewAt(index);
        }
        tabWidget.removeAllViews();
        // Ensure that all tab content childs are not visible at startup.
        for (int index = 0; index < tabContent.getChildCount(); index++) {
            tabContent.getChildAt(index).setVisibility(View.GONE);
        }
        // Create the tabspec based on the textview childs in the xml file.
        // Or create simple tabspec instances in any other way...
        SignalCorpsDB db = new SignalCorpsDB(this);
        for (int index = 0; index < originalTextViews.length; index++) {
            final TextView tabWidgetTextView = originalTextViews[index];
            final View tabContentView = tabContent.getChildAt(index);
            TabHost.TabSpec tabSpec = tabHost.newTabSpec((String) tabWidgetTextView.getTag());
            tabSpec.setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String tag) {
                    return tabContentView;
                }
            });
            if (tabWidgetTextView.getBackground() == null) {
                if(String.valueOf(tabWidgetTextView.getText()).equals(getResources().getString(R.string.people))) {
                    tabSpec.setIndicator(tabWidgetTextView.getText() + " : " +
                            String.valueOf(db.getPersonInEquipageCount(equipage.getId())));
                }
                if(String.valueOf(tabWidgetTextView.getText()).equals(getResources().getString(R.string.transport))) {
                    tabSpec.setIndicator(tabWidgetTextView.getText() + " : " +
                            String.valueOf(db.getTransportInEquipageCount(equipage.getId())));
                }
                if(String.valueOf(tabWidgetTextView.getText()).equals(getResources().getString(R.string.contacts))) {
                    tabSpec.setIndicator(tabWidgetTextView.getText() + " : " +
                            String.valueOf(db.getInProgressContactsInEquipageCount(equipage.getId())) + "/" +
                            String.valueOf(db.getContactsInEquipageCount(equipage.getId())));
                }
            } else {
                if(String.valueOf(tabWidgetTextView.getText()).equals(getResources().getString(R.string.people))) {
                    tabSpec.setIndicator(tabWidgetTextView.getText() + " : " +
                            String.valueOf(db.getPersonInEquipageCount(equipage.getId())),
                            tabWidgetTextView.getBackground());
                }
                if(String.valueOf(tabWidgetTextView.getText()).equals(getResources().getString(R.string.transport))) {
                    tabSpec.setIndicator(tabWidgetTextView.getText() + " : " +
                            String.valueOf(db.getTransportInEquipageCount(equipage.getId())),
                            tabWidgetTextView.getBackground());
                }
                if(String.valueOf(tabWidgetTextView.getText()).equals(getResources().getString(R.string.contacts))) {
                    tabSpec.setIndicator(tabWidgetTextView.getText() + " : " +
                            String.valueOf(db.getInProgressContactsInEquipageCount(equipage.getId())) + "/" +
                            String.valueOf(db.getContactsInEquipageCount(equipage.getId())));
                            tabWidgetTextView.getBackground();
                }
            }
            tabHost.addTab(tabSpec);
        }
		//tabHost.setCurrentTab(0);
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
                if (chosenUser.equals(HomeActivity.user.getSecretName())) {
                    watch = new Intent(WatchEquipageActivity.this, HomeActivity.class);
                } else {
                    watch = new Intent(WatchEquipageActivity.this, WatchPersonActivity.class);
                    watch.putExtra("user", ((TextView) view.findViewById(R.id.secretNameView)).getText().toString());
                }
                startActivity(watch);
            }
        });
    }

    private void initiateContactsView() {
        ListView list = (ListView) findViewById(R.id.contactsListView);
        SignalCorpsDB dataBase = new SignalCorpsDB(this);
        ContactArrayAdapter adapter = new ContactArrayAdapter(this,
                dataBase.getContactsOfEquipage(equipage.getId()), false);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int chosenContact = Integer.parseInt( ((TextView) view.findViewById(R.id.numberView))
                        .getText()
                        .toString()
                        .substring(1));
                Intent watch = new Intent(WatchEquipageActivity.this, WatchContactActivity.class);
                watch.putExtra("contact", chosenContact);
                startActivity(watch);
            }
        });
    }

    private void initiateTransportView() {
        ListView list = (ListView) findViewById(R.id.transportListView);
        SignalCorpsDB dataBase = new SignalCorpsDB(this);
        TransportArrayAdapter adapter = new TransportArrayAdapter(this,
                dataBase.getTransportOfEquipage(equipage.getId()), false);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int chosenTransport = Integer.parseInt(((TextView) view.findViewById(R.id.numberView))
                        .getText()
                        .toString()
                        .substring(1));
                Intent watch = new Intent(WatchEquipageActivity.this, WatchTransportActivity.class);
                watch.putExtra("transport", chosenTransport);
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
