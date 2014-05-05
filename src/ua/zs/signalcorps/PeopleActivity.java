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
import ua.zs.elements.Person;

import java.util.ArrayList;

public class PeopleActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people);
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
        ListView list = (ListView) findViewById(R.id.peopleView);
        SignalCorpsDB dataBase = new SignalCorpsDB(this);
        PersonArrayAdapter adapter = new PersonArrayAdapter(this, dataBase.getAllPersons(), true);
        list.setAdapter(adapter);
    }

    private void initiateActionBarIconButton() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.peopleDrawer);
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
            case R.id.people_menu_add:
                initiatePersonAdding();
                return true;
            case R.id.logout:
                userLogout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initiatePersonAdding() {
        Intent intent = new Intent(PeopleActivity.this, AddPersonActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.people_menu, menu);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.people_menu_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        ((ImageView) searchView.findViewById(R.id.search_button))
                .setImageResource(R.drawable.ic_action_search);
        return super.onCreateOptionsMenu(menu);
    }

    private void searchPerson(String query) {
        String search = query.toUpperCase();
        ListView list = (ListView) findViewById(R.id.peopleView);
        SignalCorpsDB dataBase = new SignalCorpsDB(this);
        ArrayList<Person> peopleList = dataBase.getAllPersons(); //dataBase.getPersonBySearchQuery(query);
        if(!query.equals("")) {
            for (int i = 0; i < peopleList.size(); i++) {
                if (!(peopleList.get(i).getFirstName().toUpperCase().startsWith(search) ||           // Фільтр по імені
                        peopleList.get(i).getSecondName().toUpperCase().startsWith(search) ||          // Фільтр по прізвищу
                        peopleList.get(i).getFathersName().toUpperCase().startsWith(search) ||         // Фільтр по по батькові
                        peopleList.get(i).getSecretName().toUpperCase().startsWith(search) ||          // Фільтр по позивному
                        (peopleList.get(i).getEquipage() != 0 ?
                                String.valueOf(peopleList.get(i).getEquipage()) :
                                "").equals(search))) {                                              // Пошук по екіпажу
                    peopleList.remove(i);
                    i--;
                }
            }
        }
        PersonArrayAdapter adapter = new PersonArrayAdapter(this, peopleList, true);
        list.setAdapter(adapter);
    }

    private void handleIntent(Intent intent) {
        ListView list = (ListView) findViewById(R.id.peopleView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String chosenUser = ((TextView) view.findViewById(R.id.secretNameView)).getText().toString();
                Intent watch;
                if(chosenUser.equals(HomeActivity.user.getSecretName())) {
                    watch = new Intent(PeopleActivity.this, HomeActivity.class);
                } else {
                    watch = new Intent(PeopleActivity.this, WatchPersonActivity.class);
                    watch.putExtra("user", ((TextView) view.findViewById(R.id.secretNameView)).getText().toString());
                }
                startActivity(watch);
            }
        });
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            setTitle(getResources().getString(R.string.people) +
                    getResources().getString(R.string.search_query) + query);
            searchPerson(query);
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
                Intent intent = new Intent(PeopleActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        Button navigatePeople = (Button) findViewById(R.id.peopleButton);
        navigatePeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                //Intent intent = new Intent(PeopleActivity.this, PeopleActivity.class);
                //startActivity(intent);
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                setTitle(getResources().getString(R.string.people));
                searchPerson("");
            }
        });
        Button navigateEquipage = (Button) findViewById(R.id.equipagesButton);
        navigateEquipage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(PeopleActivity.this, EquipageActivity.class);
                startActivity(intent);
            }
        });
        Button navigateContacts = (Button) findViewById(R.id.contactsButton);
        navigateContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(PeopleActivity.this, ContactsActivity.class);
                startActivity(intent);
            }
        });
        Button navigateTransport = (Button) findViewById(R.id.transportButton);
        navigateTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(PeopleActivity.this, TransportActivity.class);
                startActivity(intent);
            }
        });
        Button navigateWeapon = (Button) findViewById(R.id.weaponButton);
        navigateWeapon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(PeopleActivity.this, WeaponActivity.class);
                startActivity(intent);
            }
        });
    }

    private void userLogout() {
        finish();
        Intent intent = new Intent(PeopleActivity.this, IntroActivity.class);
        startActivity(intent);
    }

}