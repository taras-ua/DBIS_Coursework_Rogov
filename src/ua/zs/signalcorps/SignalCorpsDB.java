package ua.zs.signalcorps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.*;
import ua.zs.elements.*;

public class SignalCorpsDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "SignalCorpsDB";

    public static final String TABLE_PERSON = "person";
    public static final String TABLE_CONTACT = "contact";
    public static final String TABLE_CONTACT_COURIER = "contact_courier";
    public static final String TABLE_EQUIPAGE = "equipage";
    public static final String TABLE_PACKAGE = "package";
    public static final String TABLE_CONTACT_RADIO = "contact_radio";
    public static final String TABLE_CONTACT_RADIORELATED = "contact_radiorelated";
    public static final String TABLE_CONTACT_SATELLITE = "contact_satellite";
    public static final String TABLE_TRANSPORT = "transport";
    public static final String TABLE_WEAPON = "weapon";
    public static final String TABLE_CONTACT_WIRED = "contact_wired";

    public static final String PK_PERSON = "id_person";
    public static final String PK_CONTACT = "id_contact";
    public static final String PK_EQUIPAGE = "id_equipage";
    public static final String PK_PACKAGE = "id_package";
    public static final String PK_TRANSPORT = "id_transport";
    public static final String PK_WEAPON = "id_weapon";

    public static final String KEY_NAME = "name";
    public static final String KEY_FATHER_NAME = "father_name";
    public static final String KEY_FAMILY_NAME = "family_name";
    public static final String KEY_RANK = "rank";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_CLASSIFIED = "classified";
    public static final String KEY_STARTED = "started";
    public static final String KEY_FINISHED = "finished";
    public static final String KEY_DELIVERTO = "deliver_to";
    public static final String KEY_FREQUENCY = "frequency";
    public static final String KEY_AZIMUTH = "azimuth";
    public static final String KEY_SATELLITE = "satellite";
    public static final String KEY_MODEL = "model";
    public static final String KEY_LASTTECHWORK = "last_techwork";
    public static final String KEY_NODE = "node";

    public static final String FK_EQUIPAGE = "fk_equipage";
    public static final String FK_COMMANDER = "fk_commander";
    public static final String FK_CONATCT = "fk_contact";
    public static final String FK_PERSON = "fk_person";

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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT_COURIER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EQUIPAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PACKAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT_RADIO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT_RADIORELATED);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT_SATELLITE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSPORT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEAPON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT_WIRED);
        createTables(db);
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
        createPersonTable(db);
        createContactTable(db);
        createCourierContactTable(db);
        createEquipageTable(db);
        createPackageTable(db);
        createRadioContactTable(db);
        createRadioRelatedContactTable(db);
        createSatelliteContactTable(db);
        createTransportTable(db);
        createWeaponTable(db);
        createWiredContactTable(db);
        Log.i(DATABASE_NAME, ".createTables > tables creating finished.");
    }

    private void createWiredContactTable(SQLiteDatabase db) {
        if(db != null) {
            String CREATE_WIREDCONTACT_TABLE = "CREATE TABLE contact_wired (" +
                    "id_contact INTEGER PRIMARY KEY, " +
                    "node INTEGER)";
            try {
                db.compileStatement(CREATE_WIREDCONTACT_TABLE).execute();
            } catch (Exception e) {
                Log.e(DATABASE_NAME, ".createWiredContactTable threw <" +e.toString()+">. " +
                        "Is statement <"+CREATE_WIREDCONTACT_TABLE+"> valid?");
            }
        } else {
            throw new NullPointerException("Can't reach SQLDateBase in createWiredContactTable.");
        }
    }

    private void createWeaponTable(SQLiteDatabase db) {
        if(db != null) {
            String CREATE_WEAPON_TABLE = "CREATE TABLE weapon (" +
                    "id_weapon INTEGER PRIMARY KEY, " +
                    "model VARCHAR(15), " +
                    "fk_person VARCHAR(15))";
            try {
                db.compileStatement(CREATE_WEAPON_TABLE).execute();
            } catch (Exception e) {
                Log.e(DATABASE_NAME, ".createWeaponTable threw <" +e.toString()+">. " +
                        "Is statement <"+CREATE_WEAPON_TABLE+"> valid?");
            }
        } else {
            throw new NullPointerException("Can't reach SQLDateBase in createWeaponTable.");
        }
    }

    private void createTransportTable(SQLiteDatabase db) {
        if(db != null) {
            String CREATE_TRANSPORT_TABLE = "CREATE TABLE transport (" +
                    "id_transport INTEGER PRIMARY KEY, " +
                    "model VARCHAR(15), " +
                    "last_techwork DATE, " +
                    "fk_equipage INTEGER)";
            try {
                db.compileStatement(CREATE_TRANSPORT_TABLE).execute();
            } catch (Exception e) {
                Log.e(DATABASE_NAME, ".createTransportTable threw <" +e.toString()+">. " +
                        "Is statement <"+CREATE_TRANSPORT_TABLE+"> valid?");
            }
        } else {
            throw new NullPointerException("Can't reach SQLDateBase in createTransportTable.");
        }
    }

    private void createSatelliteContactTable(SQLiteDatabase db) {
        if(db != null) {
            String CREATE_SATELLITECONTACT_TABLE = "CREATE TABLE contact_satellite (" +
                    "id_contact INTEGER PRIMARY KEY, " +
                    "satellite VARCHAR(15))";
            try {
                db.compileStatement(CREATE_SATELLITECONTACT_TABLE).execute();
            } catch (Exception e) {
                Log.e(DATABASE_NAME, ".createSatelliteContactTable threw <" +e.toString()+">. " +
                        "Is statement <"+CREATE_SATELLITECONTACT_TABLE+"> valid?");
            }
        } else {
            throw new NullPointerException("Can't reach SQLDateBase in createSatelliteContactTable.");
        }
    }

    private void createRadioRelatedContactTable(SQLiteDatabase db) {
        if(db != null) {
            String CREATE_RADIORELATEDCONTACT_TABLE = "CREATE TABLE contact_radiorelated (" +
                    "id_contact INTEGER PRIMARY KEY, " +
                    "azimuth AZIMUTH)";
            try {
                db.compileStatement(CREATE_RADIORELATEDCONTACT_TABLE).execute();
            } catch (Exception e) {
                Log.e(DATABASE_NAME, ".createRadioRelatedContactTable threw <" +e.toString()+">. " +
                        "Is statement <"+CREATE_RADIORELATEDCONTACT_TABLE+"> valid?");
            }
        } else {
            throw new NullPointerException("Can't reach SQLDateBase in createRadioRelatedContactTable.");
        }
    }

    private void createRadioContactTable(SQLiteDatabase db) {
        if(db != null) {
            String CREATE_RADIOCONTACT_TABLE = "CREATE TABLE contact_radio (" +
                    "id_contact INTEGER PRIMARY KEY, " +
                    "frequency INTEGER)";
            try {
                db.compileStatement(CREATE_RADIOCONTACT_TABLE).execute();
            } catch (Exception e) {
                Log.e(DATABASE_NAME, ".createRadioContactTable threw <" +e.toString()+">. " +
                        "Is statement <"+CREATE_RADIOCONTACT_TABLE+"> valid?");
            }
        } else {
            throw new NullPointerException("Can't reach SQLDateBase in createRadioContactTable.");
        }
    }

    private void createPackageTable(SQLiteDatabase db) {
        if(db != null) {
            String CREATE_PACKAGE_TABLE = "CREATE TABLE package (" +
                    "id_package INTEGER PRIMARY KEY, " +
                    "fk_contact INTEGER, " +
                    "classified CLASSIFIED)";
            try {
                db.compileStatement(CREATE_PACKAGE_TABLE).execute();
            } catch (Exception e) {
                Log.e(DATABASE_NAME, ".createPackageTable threw <" +e.toString()+">. " +
                        "Is statement <"+CREATE_PACKAGE_TABLE+"> valid?");
            }
        } else {
            throw new NullPointerException("Can't reach SQLDateBase in createPackageTable.");
        }
    }

    private void createEquipageTable(SQLiteDatabase db) {
        if(db != null) {
            String CREATE_EQUIPAGE_TABLE = "CREATE TABLE equipage (" +
                    "id_equipage INTEGER PRIMARY KEY, " +
                    "fk_commander VARCHAR(15))";
            try {
                db.compileStatement(CREATE_EQUIPAGE_TABLE).execute();
            } catch (Exception e) {
                Log.e(DATABASE_NAME, ".createEquipageTable threw <" +e.toString()+">. " +
                        "Is statement <"+CREATE_EQUIPAGE_TABLE+"> valid?");
            }
        } else {
            throw new NullPointerException("Can't reach SQLDateBase in createEquipageTable.");
        }
    }

    private void createCourierContactTable(SQLiteDatabase db) {
        if(db != null) {
            String CREATE_COURIERCONTACT_TABLE = "CREATE TABLE contact_courier (" +
                    "id_contact INTEGER PRIMARY KEY, " +
                    "deliver_to VARCHAR(50))";
            try {
                db.compileStatement(CREATE_COURIERCONTACT_TABLE).execute();
            } catch (Exception e) {
                Log.e(DATABASE_NAME, ".createCourierContactTable threw <" +e.toString()+">. " +
                        "Is statement <"+CREATE_COURIERCONTACT_TABLE+"> valid?");
            }
        } else {
            throw new NullPointerException("Can't reach SQLDateBase in createCourierContactTable.");
        }
    }

    private void createContactTable(SQLiteDatabase db) {
        if(db != null) {
            String CREATE_CONTACT_TABLE = "CREATE TABLE contact (" +
                    "id_contact INTEGER PRIMARY KEY, " +
                    "fk_equipage INTEGER, " +
                    "started DATE, " +
                    "finished DATE)";
            try {
                db.compileStatement(CREATE_CONTACT_TABLE).execute();
            } catch (Exception e) {
                Log.e(DATABASE_NAME, ".createContactTable threw <" +e.toString()+">. " +
                        "Is statement <"+CREATE_CONTACT_TABLE+"> valid?");
            }
        } else {
            throw new NullPointerException("Can't reach SQLDateBase in createContactTable.");
        }
    }

    private void createPersonTable(SQLiteDatabase db) {
        if(db != null) {
            String CREATE_PERSON_TABLE = "CREATE TABLE person (" +
                    "id_person VARCHAR(15) PRIMARY KEY, " +
                    "name VARCHAR(30), " +
                    "father_name VARCHAR(30), " +
                    "family_name VARCHAR(30), " +
                    "rank MILITARY_RANK, " +
                    "fk_equipage INTEGER, " +
                    "password VARCHAR(30), " +
                    "classified CLASSIFIED)";
            try {
                db.compileStatement(CREATE_PERSON_TABLE).execute();
            } catch (Exception e) {
                Log.e(DATABASE_NAME, ".createPersonTable threw <" +e.toString()+">. " +
                        "Is statement <"+CREATE_PERSON_TABLE+"> valid?");
            }
        } else {
            throw new NullPointerException("Can't reach SQLDateBase in createPersonTable.");
        }
    }

    public boolean addPerson(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(db != null) {
            if(this.getPersonBySecretName(person.getSecretName()) == null) {
                ContentValues values = new ContentValues();
                values.put(PK_PERSON, person.getSecretName());
                values.put(KEY_NAME, person.getFirstName());
                values.put(KEY_FATHER_NAME, person.getFathersName());
                values.put(KEY_FAMILY_NAME, person.getSecondName());
                values.put(KEY_RANK, person.getRank());
                values.put(FK_EQUIPAGE, person.getEquipage());
                values.put(KEY_PASSWORD, person.getPersonalPassword());
                values.put(KEY_CLASSIFIED, person.getClassified());
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
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        if(db != null) {
            cursor = db.query(TABLE_PERSON, new String[] { "*" }, null, null, null, null, KEY_RANK);
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
                                       Integer.parseInt(cursor.getString(5)), cursor.getString(6),
                                       Integer.parseInt(cursor.getString(7))));
            cursor.moveToNext();
        } while(!cursor.isAfterLast());
        return personsList;
    }

    public boolean isLegalAuthInformation(String login, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        if(db != null) {
            cursor = db.query(TABLE_PERSON, new String[] {PK_PERSON, KEY_PASSWORD }, null, null, null, null, null);
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
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        if(db != null) {
            cursor = db.query(TABLE_PERSON, new String[] { "*" },
                    PK_PERSON + " = ?", // where
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
                    Integer.parseInt(cursor.getString(5)), cursor.getString(6),
                    Integer.parseInt(cursor.getString(7)));
        }
        return null;
    }

    public ArrayList<Person> getPersonBySearchQuery(String search) {
        String searchString = search.toUpperCase();
        ArrayList<Person> personsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        String queryForEquipage = " OR " + FK_EQUIPAGE + "=" + searchString;
        try {
            Integer equipage = Integer.parseInt(searchString);
            if(equipage.equals(Integer.valueOf(0))) {
                queryForEquipage = "";
            }
        } catch(NumberFormatException e) {
            queryForEquipage = "";       // This will cancel searching integer fields if search string is NaN
        }
        if(db != null) {
            cursor = db.query(TABLE_PERSON, new String[] { "*" },
                    "upper(" + KEY_NAME + ") LIKE '" + searchString + "%' OR " +
                    "upper(" + PK_PERSON + ") LIKE '" + searchString + "%' OR " +
                    "upper(" + KEY_FAMILY_NAME + ") LIKE '" + searchString + "%' OR " +
                    "upper(" + KEY_FATHER_NAME + ") LIKE '" + searchString + "%'" +
                    queryForEquipage, // where
                    null, // params
                    null, // groupBy
                    null, // having
                    KEY_RANK); // orderBy
        } else {
            throw new NullPointerException("Can't reach SQLDateBase in getAllPersons.");
        }
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if(cursor.getCount() > 0) do {
            personsList.add(new Person(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), Integer.parseInt(cursor.getString(4)),
                    Integer.parseInt(cursor.getString(5)), cursor.getString(6),
                    Integer.parseInt(cursor.getString(7))));
            cursor.moveToNext();
        } while(!cursor.isAfterLast());
        return personsList;
    }

}
