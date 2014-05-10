package ua.zs.signalcorps;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import ua.zs.elements.Contact;
import ua.zs.elements.Equipage;
import ua.zs.elements.WiredContact;

import java.util.ArrayList;
import java.util.Date;

public class ContactsActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);
        initiateActionBarIconButton();
        initiateDrawerButtons();
        handleIntent(getIntent());
        SignalCorpsDB dataBase = new SignalCorpsDB(this);
        dataBase.addContact(new WiredContact(12, new Equipage(23, null), new Date(), 34));
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleIntent(getIntent());
    }

    private void initiateListView() {
        ListView list = (ListView) findViewById(R.id.contactsView);
        SignalCorpsDB dataBase = new SignalCorpsDB(this);
        ContactArrayAdapter adapter = new ContactArrayAdapter(this, dataBase.getAllContacts(), true);
        list.setAdapter(adapter);
    }

    private void initiateActionBarIconButton() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.contactsDrawer);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle(mTitle); // Set title while seeing parent layout
            }
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle(mDrawerTitle); // Set title while seeing drawer
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch(item.getItemId()) {
            case R.id.contact_menu_add:
                initiateContactAdding();
                return true;
            case R.id.logout:
                userLogout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initiateContactAdding() {
        /*Intent intent = new Intent(ContactsActivity.this, AddContactActivity.class);
        startActivity(intent);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contacts_menu, menu);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.contact_menu_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        ((ImageView) searchView.findViewById(R.id.search_button))
                .setImageResource(R.drawable.ic_action_search);
        return super.onCreateOptionsMenu(menu);
    }

    private void searchContact(String query) {
        String search = query.toUpperCase();
        ListView list = (ListView) findViewById(R.id.contactsView);
        SignalCorpsDB dataBase = new SignalCorpsDB(this);
        ArrayList<Contact> contactList = dataBase.getAllContacts();
        if(!query.equals("")) {
            for (int i = 0; i < contactList.size(); i++) {
                if (!(contactList.get(i).getSatellite().toUpperCase().startsWith(search) ||
                        contactList.get(i).getReceiver().toUpperCase().contains(search) ||
                        String.valueOf(contactList.get(i).getEquipage().getId()).equals(search) ||
                        (contactList.get(i).getNode() > 0 ?
                                String.valueOf(contactList.get(i).getNode()) :
                                "").equals(search) ||
                        String.valueOf(contactList.get(i).getId()).equals(search) ||
                        (contactList.get(i).getFrequency() > 0 ?
                                String.valueOf(contactList.get(i).getFrequency()) :
                                "").equals(search) ||
                        (contactList.get(i).getAzimuth() > -1.0 ?
                                String.valueOf(contactList.get(i).getAzimuth()) :
                                "").startsWith(search))) {
                    contactList.remove(i);
                    i--;
                }
            }
        }
        ContactArrayAdapter adapter = new ContactArrayAdapter(this, contactList, true);
        list.setAdapter(adapter);
    }

    private void handleIntent(Intent intent) {
        ListView list = (ListView) findViewById(R.id.contactsView);
        /*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int chosenContact = Integer.parseInt( ((TextView) view.findViewById(R.id.numberView))
                        .getText()
                        .toString()
                        .substring(1));
                Intent watch = new Intent(ContactsActivity.this, WatchContactActivity.class);
                watch.putExtra("contact", chosenContact);
                startActivity(watch);
            }
        });*/
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            setTitle(getResources().getString(R.string.contacts) +
                    getResources().getString(R.string.search_query) + query);
            searchContact(query);
        } else {
            initiateListView();
        }
    }

    private void initiateDrawerButtons() {
        Button navigateHome = (Button) findViewById(R.id.homeButton);
        navigateHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ContactsActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        Button navigatePeople = (Button) findViewById(R.id.peopleButton);
        navigatePeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ContactsActivity.this, PeopleActivity.class);
                startActivity(intent);
            }
        });
        Button navigateEquipage = (Button) findViewById(R.id.equipagesButton);
        navigateEquipage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ContactsActivity.this, EquipageActivity.class);
                startActivity(intent);
            }
        });
        Button navigateContacts = (Button) findViewById(R.id.contactsButton);
        navigateContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                //Intent intent = new Intent(ContactsActivity.this, ContactsActivity.class);
                //startActivity(intent);
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
        Button navigateTransport = (Button) findViewById(R.id.transportButton);
        navigateTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ContactsActivity.this, TransportActivity.class);
                startActivity(intent);
            }
        });
        Button navigateWeapon = (Button) findViewById(R.id.weaponButton);
        navigateWeapon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ContactsActivity.this, WeaponActivity.class);
                startActivity(intent);
            }
        });
    }

    private void userLogout() {
        finish();
        Intent intent = new Intent(ContactsActivity.this, IntroActivity.class);
        startActivity(intent);
    }

}