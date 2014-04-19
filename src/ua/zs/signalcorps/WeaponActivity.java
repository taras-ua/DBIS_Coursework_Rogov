package ua.zs.signalcorps;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.*;
import android.widget.Button;

public class WeaponActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weapon);
        initiateActionBarIconButton();
        initiateDrawerButtons();
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
        return super.onCreateOptionsMenu(menu);
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
            case R.id.logout:
                userLogout();
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
                finish();
                Intent intent = new Intent(WeaponActivity.this, WeaponActivity.class);
                startActivity(intent);
            }
        });
    }

    private void userLogout() {
        finish();
        Intent intent = new Intent(WeaponActivity.this, IntroActivity.class);
        startActivity(intent);
    }

}