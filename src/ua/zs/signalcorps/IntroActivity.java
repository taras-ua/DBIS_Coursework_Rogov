package ua.zs.signalcorps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import ua.zs.elements.Classified;
import ua.zs.elements.Person;
import ua.zs.elements.Rank;

public class IntroActivity extends Activity {

    private SignalCorpsDB dataBase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
        initiateDB();
        initiateUIElements();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((EditText) findViewById(R.id.loginView)).setText("");
        ((EditText) findViewById(R.id.passwordView)).setText("");
    }

    private void initiateDB() {
        dataBase = new SignalCorpsDB(this);
        dataBase.addPerson(new Person("КИЇВ-1", "Михайло", "Володимирович", "Коваль",
                Rank.GENERAL_OF_ARMY, 1, "1234", Classified.TOP_SECRET)); // Example user 1
        //new SignalCorpsDataBaseFiller(this).test(); // Fills DB with test rows. Needs file in ExtStorage
    }

    private void initiateUIElements() {
        final EditText loginText = (EditText) findViewById(R.id.loginView);
        final EditText passwordText = (EditText) findViewById(R.id.passwordView);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButtonClick(String.valueOf(loginText.getText()).toUpperCase(),
                                 String.valueOf(passwordText.getText()));
            }
        });
    }

    private void loginButtonClick(String login, String password) {
        if(!dataBase.isLegalAuthInformation(login, password)) {
            Toast.makeText(IntroActivity.this, getResources().getString(R.string.auth_error),
                    Toast.LENGTH_LONG).show(); // Доступ не надано
        } else {
            HomeActivity.user = dataBase.getPersonBySecretName(login);
            String authRank = Rank.toString(this, HomeActivity.user
                                                        .getRank())
                                                        .toLowerCase();
            Toast.makeText(IntroActivity.this, getResources().getString(R.string.auth_ok) +
                    " " + authRank + "!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(IntroActivity.this, HomeActivity.class);
            finish();
            startActivity(intent);
        }
    }

}
