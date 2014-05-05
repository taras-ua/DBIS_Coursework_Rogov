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
import ua.zs.elements.Weapon;

import java.util.ArrayList;

public class WeaponActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weapon);
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
        ListView list = (ListView) findViewById(R.id.weaponView);
        SignalCorpsDB dataBase = new SignalCorpsDB(this);
        WeaponArrayAdapter adapter = new WeaponArrayAdapter(this, dataBase.getAllWeapon(), true);
        list.setAdapter(adapter);
    }

    private void initiateActionBarIconButton() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.weaponDrawer);
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
        inflater.inflate(R.menu.weapon_menu, menu);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.weapon_menu_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        ((ImageView) searchView.findViewById(R.id.search_button))
                .setImageResource(R.drawable.ic_action_search);
        return super.onCreateOptionsMenu(menu);
    }

    private void searchWeapon(String query) {
        String search = query.toUpperCase();
        ListView list = (ListView) findViewById(R.id.weaponView);
        SignalCorpsDB dataBase = new SignalCorpsDB(this);
        ArrayList<Weapon> weaponList = dataBase.getAllWeapon(); //dataBase.getPersonBySearchQuery(query);
        if(!query.equals("")) {
            for (int i = 0; i < weaponList.size(); i++) {
                if(!( weaponList.get(i).getOwner().getFirstName().toUpperCase().startsWith(search) ||           // Фільтр по імені
                        weaponList.get(i).getOwner().getSecondName().toUpperCase().startsWith(search) ||          // Фільтр по прізвищу
                        weaponList.get(i).getOwner().getFathersName().toUpperCase().startsWith(search) ||         // Фільтр по по-батькові
                        weaponList.get(i).getOwner().getSecretName().toUpperCase().startsWith(search) ||          // Фільтр по позивному
                        weaponList.get(i).getModel().toUpperCase().startsWith(search) ||                        // Фільтр по моделі
                        (weaponList.get(i).getId() != 0 ?
                                String.valueOf(weaponList.get(i).getId()) :
                                "").equals(search) )) {                                              // Пошук по екіпажу
                    weaponList.remove(i);
                    i--;
                }
            }
        }
        WeaponArrayAdapter adapter = new WeaponArrayAdapter(this, weaponList, true);
        list.setAdapter(adapter);
    }

    private void handleIntent(Intent intent) {
        ListView list = (ListView) findViewById(R.id.weaponView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int chosenWeapon = Integer.parseInt( ((TextView) view.findViewById(R.id.numberView))
                                                       .getText()
                                                       .toString()
                                                       .substring(1));
                Intent watch = new Intent(WeaponActivity.this, WatchWeaponActivity.class);
                watch.putExtra("weapon", chosenWeapon);
                startActivity(watch);
            }
        });
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            setTitle(getResources().getString(R.string.weapon) +
                    getResources().getString(R.string.search_query) + query);
            searchWeapon(query);
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

    private void initiateWeaponAdding() {
        Intent intent = new Intent(WeaponActivity.this, AddWeaponActivity.class);
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
            case R.id.weapon_menu_add:
                initiateWeaponAdding();
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
                Intent intent = new Intent(WeaponActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        Button navigatePeople = (Button) findViewById(R.id.peopleButton);
        navigatePeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(WeaponActivity.this, PeopleActivity.class);
                startActivity(intent);
            }
        });
        Button navigateEquipage = (Button) findViewById(R.id.equipagesButton);
        navigateEquipage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(WeaponActivity.this, EquipageActivity.class);
                startActivity(intent);
            }
        });
        Button navigateContacts = (Button) findViewById(R.id.contactsButton);
        navigateContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(WeaponActivity.this, ContactsActivity.class);
                startActivity(intent);
            }
        });
        Button navigateTransport = (Button) findViewById(R.id.transportButton);
        navigateTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(WeaponActivity.this, TransportActivity.class);
                startActivity(intent);
            }
        });
        Button navigateWeapon = (Button) findViewById(R.id.weaponButton);
        navigateWeapon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                //Intent intent = new Intent(WeaponActivity.this, WeaponActivity.class);
                //startActivity(intent);
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                setTitle(getResources().getString(R.string.weapon));
                searchWeapon("");
            }
        });
    }

    private void userLogout() {
        finish();
        Intent intent = new Intent(WeaponActivity.this, IntroActivity.class);
        startActivity(intent);
    }

}