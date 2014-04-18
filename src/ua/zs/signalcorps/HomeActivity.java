package ua.zs.signalcorps;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import ua.zs.elements.Person;
import ua.zs.elements.Rank;

public class HomeActivity extends ActionBarActivity {

    protected static Person user;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        initiateActionBarIconButton();
        initiateDrawerButtons();
        initiateHomePageElements();
        initiateWeaponView();
    }

    private void initiateHomePageElements() {
        ImageView rankImage = (ImageView) findViewById(R.id.rankView);
        TextView rankText = (TextView) findViewById(R.id.rankTextView);
        TextView name = (TextView) findViewById(R.id.nameView);
        TextView secret = (TextView) findViewById(R.id.secretNameView);
        Button equipage = (Button) findViewById(R.id.equipageButton);
        rankImage.setImageResource(Rank.toImage(user.getRank()));
        rankText.setText(Rank.toString(user.getRank()));
        name.setText(user.getSecondName() + " " +
                user.getFirstName() + " " +
                user.getFathersName());
        secret.setText(user.getSecretName());
        equipage.setText(user.getEquipage() != 0 ?
                "Екіпаж №" + String.valueOf(user.getEquipage()) :
                "Не приписаний до екіпажу");
        equipage.setEnabled(user.getEquipage() != 0);
        equipage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
    }

    private void initiateWeaponView() {
        ListView list = (ListView) findViewById(R.id.myWeaponView);
        SignalCorpsDB dataBase = new SignalCorpsDB(this);
        WeaponArrayAdapter adapter = new WeaponArrayAdapter(this,
                                                dataBase.getWeaponOfPerson(user.getSecretName()), false);
        list.setAdapter(adapter);
        TextView emptyList = (TextView) findViewById(R.id.noWeaponText);
        emptyList.setVisibility(list.getCount() > 0 ? View.GONE : View.VISIBLE);
    }

    private void initiateActionBarIconButton() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.homeDrawer);
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
        inflater.inflate(R.menu.home_menu, menu);
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
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        Button navigatePeople = (Button) findViewById(R.id.peopleButton);
        navigatePeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(HomeActivity.this, PeopleActivity.class);
                startActivity(intent);
            }
        });
    }

    private void userLogout() {
        finish();
        Intent intent = new Intent(HomeActivity.this, IntroActivity.class);
        startActivity(intent);
    }

}