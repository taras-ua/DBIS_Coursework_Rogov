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
import android.widget.*;
import ua.zs.elements.Equipage;

import java.util.ArrayList;

public class EquipageActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipage);
        initiateActionBarIconButton();
        initiateDrawerButtons();
        handleIntent(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleIntent(getIntent());
    }

    private void initiateListView() {
        ListView list = (ListView) findViewById(R.id.equipageView);
        SignalCorpsDB dataBase = new SignalCorpsDB(this);
        EquipageArrayAdapter adapter = new EquipageArrayAdapter(this, dataBase.getAllEquipages());
        list.setAdapter(adapter);
    }

    private void initiateActionBarIconButton() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.equipageDrawer);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.equipage_menu, menu);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.equipage_menu_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        ((ImageView) searchView.findViewById(R.id.search_button))
                .setImageResource(R.drawable.ic_action_search);
        return super.onCreateOptionsMenu(menu);
    }

    private void searchEquipage(String query) {
        String search = query.toUpperCase();
        ListView list = (ListView) findViewById(R.id.equipageView);
        SignalCorpsDB dataBase = new SignalCorpsDB(this);
        ArrayList<Equipage> equipageList = dataBase.getAllEquipages();
        if(!query.equals("")) {
            for (int i = 0; i < equipageList.size(); i++) {
                if(!( equipageList.get(i).getCommander().getFirstName().toUpperCase().startsWith(search) ||           // Фільтр по імені
                        equipageList.get(i).getCommander().getSecondName().toUpperCase().startsWith(search) ||          // Фільтр по прізвищу
                        equipageList.get(i).getCommander().getFathersName().toUpperCase().startsWith(search) ||         // Фільтр по по-батькові
                        equipageList.get(i).getCommander().getSecretName().toUpperCase().startsWith(search) ||          // Фільтр по позивному
                        (equipageList.get(i).getId() != 0 ?
                                String.valueOf(equipageList.get(i).getId()) :
                                "").equals(search) )) {                                              // Пошук по екіпажу
                    equipageList.remove(i);
                    i--;
                }
            }
        }
        EquipageArrayAdapter adapter = new EquipageArrayAdapter(this, equipageList);
        list.setAdapter(adapter);
    }

    private void handleIntent(Intent intent) {
        ListView list = (ListView) findViewById(R.id.equipageView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int chosenEquipage = Integer.parseInt( ((TextView) view.findViewById(R.id.idEquipageText))
                        .getText()
                        .toString()
                        .substring(1));
                Intent watch = new Intent(EquipageActivity.this, WatchEquipageActivity.class);
                watch.putExtra("equipage", chosenEquipage);
                startActivity(watch);
            }
        });
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            setTitle(getResources().getString(R.string.equipages) +
                    getResources().getString(R.string.search_query) + query);
            searchEquipage(query);
        } else {
            initiateListView();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    private void initiateEquipageAdding() {
        Intent intent = new Intent(EquipageActivity.this, AddEquipageActivity.class);
        startActivity(intent);
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
            case R.id.logout:
                userLogout();
                return true;
            case R.id.equipage_menu_add:
                initiateEquipageAdding();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initiateDrawerButtons() {
        Button navigateHome = (Button) findViewById(R.id.homeButton);
        navigateHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(EquipageActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        Button navigatePeople = (Button) findViewById(R.id.peopleButton);
        navigatePeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(EquipageActivity.this, PeopleActivity.class);
                startActivity(intent);
            }
        });
        Button navigateEquipage = (Button) findViewById(R.id.equipagesButton);
        navigateEquipage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                //Intent intent = new Intent(EquipageActivity.this, EquipageActivity.class);
                //startActivity(intent);
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
        Button navigateContacts = (Button) findViewById(R.id.contactsButton);
        navigateContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(EquipageActivity.this, ContactsActivity.class);
                startActivity(intent);
            }
        });
        Button navigateTransport = (Button) findViewById(R.id.transportButton);
        navigateTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(EquipageActivity.this, TransportActivity.class);
                startActivity(intent);
            }
        });
        Button navigateWeapon = (Button) findViewById(R.id.weaponButton);
        navigateWeapon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(EquipageActivity.this, WeaponActivity.class);
                startActivity(intent);
            }
        });
    }

    private void userLogout() {
        finish();
        Intent intent = new Intent(EquipageActivity.this, IntroActivity.class);
        startActivity(intent);
    }

}