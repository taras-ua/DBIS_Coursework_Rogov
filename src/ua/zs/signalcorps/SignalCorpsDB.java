package ua.zs.signalcorps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.*;

public class SignalCorpsDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SignalCorpsDB";

    public static final String TABLE_PERSON = "person";

    public static final String KEY_ID_PERSON = "id_person";
    public static final String KEY_NAME = "name";
    public static final String KEY_FATHER_NAME = "father_name";
    public static final String KEY_FAMILY_NAME = "family_name";
    public static final String KEY_RANK = "rank";
    public static final String KEY_PERSONS_EQUIPAGE = "fk_equipage";
    public static final String KEY_PASSWORD = "password";

    public SignalCorpsDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(DATABASE_NAME, ".onCreate > DB creating started.");
        createDomains(db);
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(DATABASE_NAME, ".onUpgrade > DB upgrading started.");

    }

    private void createDomains(SQLiteDatabase db) {
        if(db != null) {
            String CREATE_MILITARY_RANK_DOMAIN = "CREATE DOMAIN MILITARY_RANK INTEGER " +
                    "CHECK (VALUE >= 0 AND VALUE < 20)";
            try {
                db.compileStatement(CREATE_MILITARY_RANK_DOMAIN).execute(); // Create 'Military Rank' domain
            } catch (Exception e) {
                Log.e(DATABASE_NAME, ".createDomains threw <" + e.toString() + ">. " +
                        "Is statement <" + CREATE_MILITARY_RANK_DOMAIN + "> valid?");
            }

            String CREATE_CLASSIFIED_DOMAIN = "CREATE DOMAIN CLASSIFIED INTEGER " +
                    "CHECK (VALUE >= 0 AND VALUE < 4)";
            try {
                db.compileStatement(CREATE_CLASSIFIED_DOMAIN).execute(); // Create 'Classified Level' domain
            } catch (Exception e) {
                Log.e(DATABASE_NAME, ".createDomains threw <" + e.toString() + ">. " +
                        "Is statement <" + CREATE_CLASSIFIED_DOMAIN + "> valid?");
            }

            String CREATE_AZIMUTH_DOMAIN = "CREATE DOMAIN AZIMUTH NUMBER " +
                    "CHECK (VALUE >= 0 AND VALUE < 360)";
            try {
                db.compileStatement(CREATE_AZIMUTH_DOMAIN).execute(); // Create 'Astronomic Azimuth' domain
            } catch (Exception e) {
                Log.e(DATABASE_NAME, ".createDomains threw <" + e.toString() + ">. " +
                        "Is statement <" + CREATE_AZIMUTH_DOMAIN + "> valid?");
            }
            Log.i(DATABASE_NAME, ".createDomains > domains creating finished.");
        } else {
            throw new NullPointerException("Can't reach SQLDateBase in createDomains.");
        }
    }

    private void createTables(SQLiteDatabase db) {
        if(db != null) {
            String CREATE_PERSON_TABLE = "CREATE TABLE person ( " +
                    "id_person VARCHAR(15) PRIMARY KEY, " +
                    "name VARCHAR(30), " +
                    "father_name VARCHAR(30), " +
                    "family_name VARCHAR(30), " +
                    "rank MILITARY_RANK, " +
                    "fk_equipage INTEGER, " +
                    "password VARCHAR(30))";
            try {
                db.compileStatement(CREATE_PERSON_TABLE).execute(); // Create 'Person' table
            } catch (Exception e) {
                Log.e(DATABASE_NAME, ".createTables threw <" +e.toString()+">. " +
                        "Is statement <"+CREATE_PERSON_TABLE+"> valid?");
            }
        } else {
            throw new NullPointerException("Can't reach SQLDateBase in createTables.");
        }
    }

    public boolean addPerson(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();

        if(db != null) {
            if(this.getPersonBySecretName(person.getSecretName()) == null) {
                ContentValues values = new ContentValues();
                values.put(KEY_ID_PERSON, person.getSecretName());
                values.put(KEY_NAME, person.getFirstName());
                values.put(KEY_FATHER_NAME, person.getFathersName());
                values.put(KEY_FAMILY_NAME, person.getSecondName());
                values.put(KEY_RANK, person.getRank());
                values.put(KEY_PERSONS_EQUIPAGE, person.getEquipage());
                values.put(KEY_PASSWORD, person.getPersonalPassword());

                try {
                    db.insert(TABLE_PERSON, null, values);
                } catch (Exception e) {
                    Log.e(DATABASE_NAME, ".addPerson.input threw <" + e.toString() + ">.");
                }
                db.close();
                return true; // Inserted successfully
            } else {
                db.close();
                return false; // This ID already exists
            }
        } else {
            throw new NullPointerException("Can't reach SQLDateBase in addPersons.");
        }
    }

    public ArrayList<Person> getAllPersons() {
        ArrayList<Person> personsList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor;
        if(db != null) {
            cursor = db.query(TABLE_PERSON, new String[] { "*" }, null, null, null, null, null);
        } else {
            throw new NullPointerException("Can't reach SQLDateBase in getAllPersons.");
        }

        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }

        do {
            personsList.add(new Person(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                                       cursor.getString(3), Integer.parseInt(cursor.getString(4)),
                                       Integer.parseInt(cursor.getString(5)), cursor.getString(6)));
            cursor.moveToNext();
        } while(!cursor.isAfterLast());

        return personsList;
    }

    public boolean isLegalAuthInformation(String login, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor;
        if(db != null) {
            cursor = db.query(TABLE_PERSON, new String[] { KEY_ID_PERSON, KEY_PASSWORD }, null, null, null, null, null);
        } else {
            throw new NullPointerException("Can't reach SQLDateBase in checkAuthInformation.");
        }

        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            throw new NullPointerException("Null-cursor of SQLDateBase.query in checkAuthInformation.");
        }

        do {
            if(login.equals(cursor.getString(0)) && password.equals(cursor.getString(1))) {
                return true;
            }
            cursor.moveToNext();
        } while(!cursor.isAfterLast());

        return false;
    }

    public Person getPersonBySecretName(String secretName) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor;
        if(db != null) {
            cursor = db.query(TABLE_PERSON, new String[] { "*" },
                    KEY_ID_PERSON + " = ?", // where
                    new String[] { secretName }, // value to replace "?"
                    null, // groupBy
                    null, // having
                    null); // orderBy
        } else {
            throw new NullPointerException("Can't reach SQLDateBase in getPersonBySecretName.");
        }

        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }

        if(cursor.getCount() > 0) {
            return new Person(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), Integer.parseInt(cursor.getString(4)),
                    Integer.parseInt(cursor.getString(5)), cursor.getString(6));
        }

        return null;
    }

}
