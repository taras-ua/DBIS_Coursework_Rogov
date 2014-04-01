package ua.zs.signalcorps;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class IntroActivity extends Activity {

    private SignalCorpsDB dataBase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
        initiateDB();
        initiateUIElements();
    }

    private void initiateDB() {
        dataBase = new SignalCorpsDB(this);
        dataBase.addPerson(new Person("КИЇВ-1", "Михайло", "Володимирович", "Коваль",
                Rank.GENERAL_POLKOVNYK, 0, "1234")); // Example user 1
        dataBase.addPerson(new Person("ЛЬВІВ-5", "Тарас", "Дмитрович", "Рогов",
                Rank.JUNIOR_LIEUTENANT, 23, "1234")); // Example user 2
    }

    private void initiateUIElements() {
        final EditText loginText = (EditText) findViewById(R.id.loginView);
        final EditText passwordText = (EditText) findViewById(R.id.passwordView);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!dataBase.isLegalAuthInformation( String.valueOf(loginText.getText()).toUpperCase(),
                                                    String.valueOf(passwordText.getText()) )) {
                    Toast.makeText(IntroActivity.this, getResources().getString(R.string.auth_error),
                            Toast.LENGTH_LONG).show(); // Доступ не надано
                } else {
                    String authRank = Rank.toString(dataBase
                                          .getPersonBySecretName(String.valueOf(loginText.getText()).toUpperCase())
                                          .getRank()).toLowerCase();
                    Toast.makeText(IntroActivity.this, getResources().getString(R.string.auth_ok) +
                                    " " + authRank + "!", Toast.LENGTH_LONG).show();
                    /*
                     Доступ отримано
                     ...
                    */
                }
            }
        });
    }

}
