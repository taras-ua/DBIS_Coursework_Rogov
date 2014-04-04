package ua.zs.signalcorps;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class AddPersonActivity extends ActionBarActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_person);
        initiateActionBarIconButton();
    }

    private void initiateActionBarIconButton() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

}