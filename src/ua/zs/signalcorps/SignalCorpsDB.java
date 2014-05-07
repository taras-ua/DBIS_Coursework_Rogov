package ua.zs.signalcorps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import ua.zs.elements.Equipage;
import ua.zs.elements.Person;
import ua.zs.elements.Transport;
import ua.zs.elements.Weapon;

import java.util.ArrayList;
import java.util.Date;

public class SignalCorpsDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 14;
    private static final String DATABASE_NAME = "SignalCorpsDB";

    public static final String TABLE_PERSON = "person";
    public static final String TABLE_PERSON_ARCHIVE = "person_archive";
    public static final String TABLE_CONTACT = "contact";
    public static final String TABLE_CONTACT_COURIER = "contact_courier";
    public static final String TABLE_EQUIPAGE = "equipage";
    public static final String TABLE_EQUIPAGE_ARCHIVE = "equipage_archive";
    public static final String TABLE_PACKAGE = "package";
    public static final String TABLE_PACKAGE_ARCHIVE = "package_archive";
    public static final String TABLE_CONTACT_RADIO = "contact_radio";
    public static final String TABLE_CONTACT_RADIORELATED = "contact_radiorelated";
    public static final String TABLE_CONTACT_SATELLITE = "contact_satellite";
    public static final String TABLE_TRANSPORT = "transport";
    public static final String TABLE_TRANSPORT_ARCHIVE = "transport_archive";
    public static final String TABLE_WEAPON = "weapon";
    public static final String TABLE_WEAPON_ARCHIVE = "weapon_archive";
    public static final String TABLE_CONTACT_WIRED = "contact_wired";

    public static final String PK_PERSON = "id_person";
    public static final String PK_CONTACT = "id_contact";
    public static final String PK_EQUIPAGE = "id_equipage";
    public static final String PK_PACKAGE = "id_package";
    public static final String PK_TRANSPORT = "id_transport";
    public static final String PK_WEAPON = "id_weapon";
    public static final String PK_ARCHIVE = "id_archive_row";

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
    public static final String KEY_ARCHIVED = "archived";

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
        //createTriggers(db);
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON_ARCHIVE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EQUIPAGE_ARCHIVE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PACKAGE_ARCHIVE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSPORT_ARCHIVE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEAPON_ARCHIVE);
        createTables(db);
        //createTriggers(db);
    }

    // ###################################### DOMAINS CREATION ######################################

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
            throw new NullPointerException("Can't reach database in createDomains.");
        }
    }

    // ###################################### TRIGGERS CREATION ######################################

    /*private void createTriggers(SQLiteDatabase db) {
        if(db != null) {
            String CREATE_ON_NEW_PERSON_TRIGGER = "CREATE TRIGGER IF NOT EXISTS insert_person AFTER INSERT ON " +
                    TABLE_PERSON + " WHEN new." + FK_EQUIPAGE + " > 0 BEGIN CASE WHEN (SELECT COUNT(*) FROM " +
                    TABLE_EQUIPAGE + " WHERE " + PK_EQUIPAGE + " = new." + FK_EQUIPAGE + ") = 0 THEN INSERT INTO " +
                    TABLE_EQUIPAGE + " VALUES (new." + FK_EQUIPAGE + ", new." + PK_PERSON + "); END;";
            try {
                db.compileStatement(CREATE_ON_NEW_PERSON_TRIGGER).execute();
            } catch (Exception e) {
                Log.e(DATABASE_NAME, ".createTriggers threw <" +e.toString()+">. " +
                        "Is statement <"+CREATE_ON_NEW_PERSON_TRIGGER+"> valid?");
            }
        } else {
            throw new NullPointerException("Can't reach database in createTriggers.");
        }
    }*/

    // ###################################### TABLES CREATION ######################################

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
            throw new NullPointerException("Can't reach database in createWiredContactTable.");
        }
    }

    private void createWeaponTable(SQLiteDatabase db) {
        if(db != null) {
            String CREATE_WEAPON_TABLE = "CREATE TABLE weapon (" +
                    "id_weapon INTEGER PRIMARY KEY, " +
                    "model VARCHAR(15), " +
                    "fk_person VARCHAR(15))";
            String CREATE_WEAPON_ARCHIVE_TABLE = "CREATE TABLE weapon_archive (" +
                    "id_archive_row INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "id_weapon INTEGER, " +
                    "model VARCHAR(15), " +
                    "fk_person VARCHAR(15), " +
                    "archived INTEGER)";
            try {
                db.compileStatement(CREATE_WEAPON_TABLE).execute();
                db.compileStatement(CREATE_WEAPON_ARCHIVE_TABLE).execute();
            } catch (Exception e) {
                Log.e(DATABASE_NAME, ".createWeaponTable threw <" +e.toString()+">. " +
                        "Is statement <"+CREATE_WEAPON_TABLE+"> valid? " +
                        "Is statement <"+CREATE_WEAPON_ARCHIVE_TABLE+"> valid?");
            }
        } else {
            throw new NullPointerException("Can't reach database in createWeaponTable.");
        }
    }

    private void createTransportTable(SQLiteDatabase db) {
        if(db != null) {
            String CREATE_TRANSPORT_TABLE = "CREATE TABLE transport (" +
                    "id_transport INTEGER PRIMARY KEY, " +
                    "model VARCHAR(15), " +
                    "last_techwork INTEGER, " +
                    "fk_equipage INTEGER)";
            String CREATE_TRANSPORT_ARCHIVE_TABLE = "CREATE TABLE transport_archive (" +
                    "id_archive_row INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "id_transport INTEGER, " +
                    "model VARCHAR(15), " +
                    "last_techwork INTEGER, " +
                    "fk_equipage INTEGER, " +
                    "archived INTEGER)";
            try {
                db.compileStatement(CREATE_TRANSPORT_TABLE).execute();
                db.compileStatement(CREATE_TRANSPORT_ARCHIVE_TABLE).execute();
            } catch (Exception e) {
                Log.e(DATABASE_NAME, ".createTransportTable threw <" +e.toString()+">. " +
                        "Is statement <"+CREATE_TRANSPORT_TABLE+"> valid? " +
                        "Is statement <"+CREATE_TRANSPORT_ARCHIVE_TABLE+"> valid?");
            }
        } else {
            throw new NullPointerException("Can't reach database in createTransportTable.");
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
            throw new NullPointerException("Can't reach database in createSatelliteContactTable.");
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
            throw new NullPointerException("Can't reach database in createRadioRelatedContactTable.");
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
            throw new NullPointerException("Can't reach database in createRadioContactTable.");
        }
    }

    private void createPackageTable(SQLiteDatabase db) {
        if(db != null) {
            String CREATE_PACKAGE_TABLE = "CREATE TABLE package (" +
                    "id_package INTEGER PRIMARY KEY, " +
                    "fk_contact INTEGER, " +
                    "classified CLASSIFIED)";
            String CREATE_PACKAGE_ARCHIVE_TABLE = "CREATE TABLE package_archive (" +
                    "id_archive_row INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "id_package INTEGER, " +
                    "fk_contact INTEGER, " +
                    "classified CLASSIFIED, " +
                    "archived INTEGER)";
            try {
                db.compileStatement(CREATE_PACKAGE_TABLE).execute();
                db.compileStatement(CREATE_PACKAGE_ARCHIVE_TABLE).execute();
            } catch (Exception e) {
                Log.e(DATABASE_NAME, ".createPackageTable threw <" +e.toString()+">. " +
                        "Is statement <"+CREATE_PACKAGE_TABLE+"> valid? " +
                        "Is statement <"+CREATE_PACKAGE_ARCHIVE_TABLE+"> valid?");
            }
        } else {
            throw new NullPointerException("Can't reach database in createPackageTable.");
        }
    }

    private void createEquipageTable(SQLiteDatabase db) {
        if(db != null) {
            String CREATE_EQUIPAGE_TABLE = "CREATE TABLE equipage (" +
                    "id_equipage INTEGER PRIMARY KEY, " +
                    "fk_commander VARCHAR(15))";
            String CREATE_EQUIPAGE_ARCHIVE_TABLE = "CREATE TABLE equipage_archive (" +
                    "id_archive_row INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "id_equipage INTEGER, " +
                    "fk_commander VARCHAR(15), " +
                    "archived INTEGER)";
            try {
                db.compileStatement(CREATE_EQUIPAGE_TABLE).execute();
                db.compileStatement(CREATE_EQUIPAGE_ARCHIVE_TABLE).execute();
            } catch (Exception e) {
                Log.e(DATABASE_NAME, ".createEquipageTable threw <" +e.toString()+">. " +
                        "Is statement <"+CREATE_EQUIPAGE_TABLE+"> valid? " +
                        "Is statement <"+CREATE_EQUIPAGE_ARCHIVE_TABLE+"> valid?");
            }
        } else {
            throw new NullPointerException("Can't reach database in createEquipageTable.");
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
            throw new NullPointerException("Can't reach database in createCourierContactTable.");
        }
    }

    private void createContactTable(SQLiteDatabase db) {
        if(db != null) {
            String CREATE_CONTACT_TABLE = "CREATE TABLE contact (" +
                    "id_contact INTEGER PRIMARY KEY, " +
                    "fk_equipage INTEGER, " +
                    "started INTEGER, " +
                    "finished INTEGER)";
            try {
                db.compileStatement(CREATE_CONTACT_TABLE).execute();
            } catch (Exception e) {
                Log.e(DATABASE_NAME, ".createContactTable threw <" +e.toString()+">. " +
                        "Is statement <"+CREATE_CONTACT_TABLE+"> valid?");
            }
        } else {
            throw new NullPointerException("Can't reach database in createContactTable.");
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
            String CREATE_PERSON_ARCHIVE_TABLE = "CREATE TABLE person_archive (" +
                    "id_archive_row INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "id_person VARCHAR(15), " +
                    "name VARCHAR(30), " +
                    "father_name VARCHAR(30), " +
                    "family_name VARCHAR(30), " +
                    "rank MILITARY_RANK, " +
                    "fk_equipage INTEGER, " +
                    "password VARCHAR(30), " +
                    "classified CLASSIFIED, " +
                    "archived INTEGER)";
            try {
                db.compileStatement(CREATE_PERSON_TABLE).execute();
                db.compileStatement(CREATE_PERSON_ARCHIVE_TABLE).execute();
            } catch (Exception e) {
                Log.e(DATABASE_NAME, ".createPersonTable threw <" +e.toString()+">. " +
                        "Is statement <"+CREATE_PERSON_TABLE+"> valid? " +
                        "Is statement <"+CREATE_PERSON_ARCHIVE_TABLE+"> valid?");
            }
        } else {
            throw new NullPointerException("Can't reach database in createPersonTable.");
        }
    }

    // ###################################### WORK WITH PERSON TABLE ######################################

    public boolean addPerson(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(db != null) {
            ContentValues values = new ContentValues();
            values.put(PK_PERSON, person.getSecretName());
            values.put(KEY_NAME, person.getFirstName());
            values.put(KEY_FATHER_NAME, person.getFathersName());
            values.put(KEY_FAMILY_NAME, person.getSecondName());
            values.put(KEY_RANK, person.getRank());
            values.put(FK_EQUIPAGE, person.getEquipage());
            values.put(KEY_PASSWORD, person.getPersonalPassword());
            values.put(KEY_CLASSIFIED, person.getClassified());
            ArrayList<Person> archive = this.getPersonFromArchive(person.getSecretName());
            if(this.getPersonBySecretName(person.getSecretName()) == null &&
                    archive.size() == 0) {
                try {
                    db.insert(TABLE_PERSON, null, values);
                    if(person.getEquipage() > 0) {
                        if(this.getEquipageById(person.getEquipage()) != null) {
                            if(this.getEquipageById(person.getEquipage()).getCommander().getRank() < person.getRank()) {
                                this.deleteEquipageForOneMoment(person.getEquipage());
                                this.addEquipageWithNewPerson(new Equipage(person.getEquipage(), person));
                            }
                        } else {
                            this.addEquipageWithNewPerson(new Equipage(person.getEquipage(), person));
                        }
                    } // New equipage commander in case of having the highest rank
                } catch (Exception e) {
                    Log.e(DATABASE_NAME, ".addPerson.insert threw <" + e.toString() + ">.");
                }
                db.close();
                return true; // Inserted successfully
            } else {
                if(archive.size() > 0) {
                    if(archive.get(0).getFirstName().equals(person.getFirstName()) &&
                            archive.get(0).getSecondName().equals(person.getSecondName()) &&
                            archive.get(0).getFathersName().equals(person.getFathersName())) {
                        try {
                            db.insert(TABLE_PERSON, null, values); // Return person from archive
                            if(person.getEquipage() > 0) {
                                if(this.getEquipageById(person.getEquipage()) != null) {
                                    if(this.getEquipageById(person.getEquipage()).getCommander().getRank() < person.getRank()) {
                                        this.deleteEquipageForOneMoment(person.getEquipage());
                                        this.addEquipageWithNewPerson(new Equipage(person.getEquipage(), person));
                                    }
                                } else {
                                    this.addEquipageWithNewPerson(new Equipage(person.getEquipage(), person));
                                }
                            } // New equipage commander in case of having the highest rank
                        } catch (Exception e) {
                            Log.e(DATABASE_NAME, ".addPerson.insert threw <" + e.toString() + ">.");
                        }
                        db.close();
                        return true;
                    }
                }
                db.close();
                return false; // This ID already exists
            }
        } else {
            throw new NullPointerException("Can't reach database in addPersons.");
        }
    }

    public boolean addEquipageCommander(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(db != null) {
            ContentValues values = new ContentValues();
            values.put(PK_PERSON, person.getSecretName());
            values.put(KEY_NAME, person.getFirstName());
            values.put(KEY_FATHER_NAME, person.getFathersName());
            values.put(KEY_FAMILY_NAME, person.getSecondName());
            values.put(KEY_RANK, person.getRank());
            values.put(FK_EQUIPAGE, person.getEquipage());
            values.put(KEY_PASSWORD, person.getPersonalPassword());
            values.put(KEY_CLASSIFIED, person.getClassified());
            if(this.getPersonBySecretName(person.getSecretName()) == null) {
                try {
                    db.insert(TABLE_PERSON, null, values);
                } catch (Exception e) {
                    Log.e(DATABASE_NAME, ".addPerson.insert threw <" + e.toString() + ">.");
                }
                db.close();
                return true; // Inserted successfully
            } else {
                db.close();
                return false;
            }
        } else {
            throw new NullPointerException("Can't reach database in addPersons.");
        }
    }

    public int deletePerson(String secreteName) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result;
        if(db != null) {
            Person row = this.getPersonBySecretName(secreteName);
            if(row != null) {
                ContentValues values = new ContentValues();
                values.put(KEY_ARCHIVED, new Date().getTime());
                values.put(PK_PERSON, row.getSecretName());
                values.put(KEY_NAME, row.getFirstName());
                values.put(KEY_FATHER_NAME, row.getFathersName());
                values.put(KEY_FAMILY_NAME, row.getSecondName());
                values.put(KEY_RANK, row.getRank());
                values.put(FK_EQUIPAGE, row.getEquipage());
                values.put(KEY_PASSWORD, row.getPersonalPassword());
                values.put(KEY_CLASSIFIED, row.getClassified());
                try {
                    if(row.getEquipage() > 0 &&
                            this.getEquipageById(row.getEquipage()).getCommander().getSecretName()
                                    .equals(row.getSecretName())) {
                        ArrayList<Person> equipagePeople = this.getPersonsFromEquipage(row.getEquipage());
                        this.deleteEquipageForOneMoment(row.getEquipage());
                        if(equipagePeople.size() > 1) {
                            Person newCommander = equipagePeople.get(1).getSecretName().equals(row.getSecretName()) ?
                                    equipagePeople.get(0) : equipagePeople.get(1);
                            this.addEquipage(new Equipage(row.getEquipage(), newCommander));
                        }
                    } // New equipage commander
                    db.insert(TABLE_PERSON_ARCHIVE, null, values);
                } catch (Exception e) {
                    Log.e(DATABASE_NAME, ".deletePerson.insert threw <" + e.toString() + ">.");
                }
                db = this.getWritableDatabase();
                result = db.delete(TABLE_PERSON, PK_PERSON + "='" + secreteName+"'", null);
            } else {
                throw new NullPointerException("Can't delete person <" + secreteName + ">. " +
                        "It's not in table now.");
            }
        } else {
            throw new NullPointerException("Can't reach database in deletePerson.");
        }
        return result;
    }

    public ArrayList<Person> getAllPersons() {
        ArrayList<Person> personsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        if(db != null) {
            cursor = db.query(TABLE_PERSON, new String[] { "*" }, null, null, null, null, KEY_RANK + " DESC");
        } else {
            throw new NullPointerException("Can't reach database in getAllPersons.");
        }
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if(cursor.getCount() > 0) do {
            personsList.add(new Person(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                                       cursor.getString(3), cursor.getInt(4),
                                       cursor.getInt(5), cursor.getString(6),
                                       cursor.getInt(7)));
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
            throw new NullPointerException("Can't reach database in checkAuthInformation.");
        }
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            throw new NullPointerException("Null-cursor of database.query in checkAuthInformation.");
        }
        do {
            if(login.equals(cursor.getString(0)) && password.equals(cursor.getString(1))) {
                return true;
            }
            cursor.moveToNext();
        } while(!cursor.isAfterLast());
        return false;
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
                    KEY_RANK + " DESC"); // orderBy
        } else {
            throw new NullPointerException("Can't reach database in getAllPersons.");
        }
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if(cursor.getCount() > 0) do {
            personsList.add(new Person(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getInt(4),
                    cursor.getInt(5), cursor.getString(6),
                    cursor.getInt(7)));
            cursor.moveToNext();
        } while(!cursor.isAfterLast());
        return personsList;
    }

    public ArrayList<Person> getPersonsFromEquipage(int id) {
        String searchString = String.valueOf(id);
        ArrayList<Person> personsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        if(db != null) {
            cursor = db.query(TABLE_PERSON, new String[] { "*" },
                    FK_EQUIPAGE + "=" + searchString, // where
                    null, // params
                    null, // groupBy
                    null, // having
                    KEY_RANK + " DESC"); // orderBy
        } else {
            throw new NullPointerException("Can't reach database in getPersonsFromEquipage.");
        }
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if(cursor.getCount() > 0) do {
            personsList.add(new Person(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getInt(4),
                    cursor.getInt(5), cursor.getString(6),
                    cursor.getInt(7)));
            cursor.moveToNext();
        } while(!cursor.isAfterLast());
        return personsList;
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
            throw new NullPointerException("Can't reach database in getPersonBySecretName.");
        }
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if(cursor.getCount() > 0) {
            return new Person(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getInt(4),
                    cursor.getInt(5), cursor.getString(6),
                    cursor.getInt(7));
        }
        return null;
    }

    public ArrayList<Person> getPersonFromArchive(String secretName) {
        ArrayList<Person> personsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        if(db != null) {
            cursor = db.query(TABLE_PERSON_ARCHIVE, new String[] { "*" },
                    PK_PERSON + " = ?", // where
                    new String[] { secretName }, // value to replace "?"
                    null, // groupBy
                    null, // having
                    KEY_ARCHIVED + " DESC"); // orderBy
        } else {
            throw new NullPointerException("Can't reach database in getPersonFromArchive.");
        }
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if(cursor.getCount() > 0)  do {
            personsList.add(new Person(cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getInt(5),
                    cursor.getInt(6), cursor.getString(7),
                    cursor.getInt(8)));
            cursor.moveToNext();
        } while(!cursor.isAfterLast());
        return personsList;
    }

    // ###################################### WORK WITH EQUIPAGE TABLE ######################################

    public boolean addEquipage(Equipage equipage) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(db != null) {
            if(this.getEquipageById(equipage.getId()) == null) {
                ContentValues values = new ContentValues();
                values.put(PK_EQUIPAGE, equipage.getId());
                values.put(FK_COMMANDER, equipage.getCommander().getSecretName());
                try {
                    db.insert(TABLE_EQUIPAGE, null, values);
                    Person person = getPersonBySecretName(equipage.getCommander().getSecretName());
                    deletePerson(person.getSecretName());
                    person.assignToEquipage(equipage.getId());
                    addEquipageCommander(person);
                } catch (Exception e) {
                    Log.e(DATABASE_NAME, ".addEquipage.input threw <" + e.toString() + ">.");
                }
                db.close();
                return true; // Inserted successfully
            } else {
                db.close();
                return false; // This ID already exists
            }
        } else {
            throw new NullPointerException("Can't reach database in addEquipage.");
        }
    }

    public boolean addEquipageWithNewPerson(Equipage equipage) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(db != null) {
            if(this.getEquipageById(equipage.getId()) == null) {
                ContentValues values = new ContentValues();
                values.put(PK_EQUIPAGE, equipage.getId());
                values.put(FK_COMMANDER, equipage.getCommander().getSecretName());
                try {
                    db.insert(TABLE_EQUIPAGE, null, values);
                } catch (Exception e) {
                    Log.e(DATABASE_NAME, ".addEquipage.input threw <" + e.toString() + ">.");
                }
                db.close();
                return true; // Inserted successfully
            } else {
                db.close();
                return false; // This ID already exists
            }
        } else {
            throw new NullPointerException("Can't reach database in addEquipage.");
        }
    }

    public int deleteEquipage(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result;
        if(db != null) {
            Equipage row = this.getEquipageById(id);
            if(row != null) {
                ContentValues values = new ContentValues();
                values.put(KEY_ARCHIVED, new Date().getTime());
                values.put(PK_EQUIPAGE, row.getId());
                values.put(FK_COMMANDER, row.getCommander().getSecretName());
                try {
                    ArrayList<Person> people = getPersonsFromEquipage(row.getId());
                    for (Person temp : people) {
                        deletePerson(temp.getSecretName());
                        temp.assignToEquipage(0);
                        addPerson(temp);
                    }
                    db.insert(TABLE_EQUIPAGE_ARCHIVE, null, values);
                } catch (Exception e) {
                    Log.e(DATABASE_NAME, ".deleteEquipage.insert threw <" + e.toString() + ">.");
                }
                db = this.getWritableDatabase();
                result = db.delete(TABLE_EQUIPAGE, PK_EQUIPAGE + "=" + String.valueOf(id), null);
            } else {
                throw new NullPointerException("Can't delete equipage <" + String.valueOf(id) + ">. " +
                        "It's not in table now.");
            }
        } else {
            throw new NullPointerException("Can't reach database in deletePerson.");
        }
        return result;
    }

    public int deleteEquipageForOneMoment(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result;
        if(db != null) {
            Equipage row = this.getEquipageById(id);
            if(row != null) {
                ContentValues values = new ContentValues();
                values.put(KEY_ARCHIVED, new Date().getTime());
                values.put(PK_EQUIPAGE, row.getId());
                values.put(FK_COMMANDER, row.getCommander().getSecretName());
                try {
                    db.insert(TABLE_EQUIPAGE_ARCHIVE, null, values);
                } catch (Exception e) {
                    Log.e(DATABASE_NAME, ".deleteEquipage.insert threw <" + e.toString() + ">.");
                }
                result = db.delete(TABLE_EQUIPAGE, PK_EQUIPAGE + "=" + String.valueOf(id), null);
            } else {
                throw new NullPointerException("Can't delete equipage <" + String.valueOf(id) + ">. " +
                        "It's not in table now.");
            }
        } else {
            throw new NullPointerException("Can't reach database in deletePerson.");
        }
        return result;
    }

    public ArrayList<Equipage> getAllEquipages() {
        ArrayList<Equipage> equipageList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        if(db != null) {
            cursor = db.query(TABLE_EQUIPAGE, new String[] { "*" }, null, null, null, null, PK_EQUIPAGE);
        } else {
            throw new NullPointerException("Can't reach database in getAllEquipages.");
        }
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if(cursor.getCount() > 0) do {
            equipageList.add(new Equipage(cursor.getInt(0), getPersonBySecretName(cursor.getString(1))));
            cursor.moveToNext();
        } while(!cursor.isAfterLast());
        return equipageList;
    }

    public Equipage getEquipageById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        if(db != null) {
            cursor = db.query(TABLE_EQUIPAGE, new String[] { "*" },
                    PK_EQUIPAGE + " = ?", // where
                    new String[] { String.valueOf(id) }, // value to replace "?"
                    null, // groupBy
                    null, // having
                    null); // orderBy
        } else {
            throw new NullPointerException("Can't reach database in getEquipageById.");
        }
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if(cursor.getCount() > 0) {
            return new Equipage(cursor.getInt(0), getPersonBySecretName(cursor.getString(1)));
        }
        return null;
    }

    public Equipage getEquipageFromArchive(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        if(db != null) {
            cursor = db.query(TABLE_EQUIPAGE_ARCHIVE, new String[] { "*" },
                    PK_EQUIPAGE + " = ?", // where
                    new String[] { String.valueOf(id) }, // value to replace "?"
                    null, // groupBy
                    null, // having
                    KEY_ARCHIVED + " DESC"); // orderBy
        } else {
            throw new NullPointerException("Can't reach database in getEquipageFromArchive.");
        }
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if(cursor.getCount() > 0) {
            Person commander = getPersonBySecretName(cursor.getString(1));
            if(commander == null) {
                commander = getPersonFromArchive(cursor.getString(1)).get(0);
            }
            return new Equipage(cursor.getInt(0), commander);
        }
        return null;
    }

    // ###################################### WORK WITH TRANSPORT TABLE ######################################

    public boolean addTransport(Transport transport) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(db != null) {
            if(this.getTransportById(transport.getId()) == null) {
                ContentValues values = new ContentValues();
                values.put(PK_TRANSPORT, transport.getId());
                values.put(KEY_MODEL, transport.getModel());
                values.put(FK_EQUIPAGE, transport.getOwner().getId());
                values.put(KEY_LASTTECHWORK, transport.getLastTechwork().getTime());
                try {
                    db.insert(TABLE_TRANSPORT, null, values);
                } catch (Exception e) {
                    Log.e(DATABASE_NAME, ".addTransport.insert threw <" + e.toString() + ">.");
                }
                db.close();
                return true; // Inserted successfully
            } else {
                db.close();
                return false; // This ID already exists
            }
        } else {
            throw new NullPointerException("Can't reach database in addTransport.");
        }
    }

    public int deleteTransport(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result;
        if(db != null) {
            Transport row = this.getTransportById(id);
            if(row != null) {
                ContentValues values = new ContentValues();
                values.put(KEY_ARCHIVED, new Date().getTime());
                values.put(PK_TRANSPORT, row.getId());
                values.put(KEY_MODEL, row.getModel());
                values.put(FK_EQUIPAGE, row.getOwner().getId());
                values.put(KEY_LASTTECHWORK, row.getLastTechwork().getTime());
                try {
                    db.insert(TABLE_TRANSPORT_ARCHIVE, null, values);
                } catch (Exception e) {
                    Log.e(DATABASE_NAME, ".deleteTransport.insert threw <" + e.toString() + ">.");
                }
                result = db.delete(TABLE_TRANSPORT, PK_TRANSPORT + "=" + String.valueOf(id), null);
            } else {
                throw new NullPointerException("Can't delete transport <" + String.valueOf(id) + ">. " +
                        "It's not in table now.");
            }
        } else {
            throw new NullPointerException("Can't reach database in deleteTransport.");
        }
        return result;
    }

    public ArrayList<Transport> getAllTransport() {
        ArrayList<Transport> transportList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        if(db != null) {
            cursor = db.query(TABLE_TRANSPORT, new String[] { "*" }, null, null, null, null, KEY_MODEL);
        } else {
            throw new NullPointerException("Can't reach database in getAllTransport.");
        }
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if(cursor.getCount() > 0) do {
            Equipage owner = getEquipageById(cursor.getInt(3));
            if(owner == null) {
                owner = getEquipageFromArchive(cursor.getInt(3));
            }
            transportList.add(new Transport(cursor.getInt(0), cursor.getString(1), new Date(cursor.getLong(2)),
                    owner));
            cursor.moveToNext();
        } while(!cursor.isAfterLast());
        return transportList;
    }

    public Transport getTransportById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        if(db != null) {
            cursor = db.query(TABLE_TRANSPORT, new String[] { "*" },
                    PK_TRANSPORT + " = ?", // where
                    new String[] { String.valueOf(id) }, // value to replace "?"
                    null, // groupBy
                    null, // having
                    null); // orderBy
        } else {
            throw new NullPointerException("Can't reach database in getTransportById.");
        }
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if(cursor.getCount() > 0) {
            Equipage owner = getEquipageById(cursor.getInt(3));
            if(owner == null) {
                owner = getEquipageFromArchive(cursor.getInt(3));
            }
            return new Transport(cursor.getInt(0), cursor.getString(1), new Date(cursor.getLong(2)),
                    owner);
        }
        return null;
    }

    public ArrayList<Transport> getTransportOfEquipage(int id) {
        ArrayList<Transport> transportList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        if(db != null) {
            cursor = db.query(TABLE_TRANSPORT, new String[] { "*" },
                    FK_EQUIPAGE + " = ?", // where
                    new String[] { String.valueOf(id) }, // value to replace "?"
                    null, // groupBy
                    null, // having
                    null); // orderBy
        } else {
            throw new NullPointerException("Can't reach database in getAllTransport.");
        }
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if(cursor.getCount() > 0) do {
            Equipage owner = getEquipageById(cursor.getInt(3));
            if(owner == null) {
                owner = getEquipageFromArchive(cursor.getInt(3));
            }
            transportList.add(new Transport(cursor.getInt(0), cursor.getString(1), new Date(cursor.getLong(2)),
                    owner));
            cursor.moveToNext();
        } while(!cursor.isAfterLast());
        return transportList;
    }

    // ###################################### WORK WITH WEAPON TABLE ######################################

    public boolean addWeapon(Weapon weapon) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(db != null) {
            if(this.getWeaponById(weapon.getId()) == null) {
                ContentValues values = new ContentValues();
                values.put(PK_WEAPON, weapon.getId());
                values.put(KEY_MODEL, weapon.getModel());
                values.put(FK_PERSON, weapon.getOwner().getSecretName());
                try {
                    db.insert(TABLE_WEAPON, null, values);
                } catch (Exception e) {
                    Log.e(DATABASE_NAME, ".addWeapon.input threw <" + e.toString() + ">.");
                }
                db.close();
                return true; // Inserted successfully
            } else {
                db.close();
                return false; // This ID already exists
            }
        } else {
            throw new NullPointerException("Can't reach database in addWeapon.");
        }
    }

    public int deleteWeapon(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result;
        if(db != null) {
            Weapon row = this.getWeaponById(id);
            if(row != null) {
                ContentValues values = new ContentValues();
                values.put(KEY_ARCHIVED, new Date().getTime());
                values.put(PK_WEAPON, row.getId());
                values.put(KEY_MODEL, row.getModel());
                values.put(FK_PERSON, row.getOwner().getSecretName());
                try {
                    db.insert(TABLE_WEAPON_ARCHIVE, null, values);
                } catch (Exception e) {
                    Log.e(DATABASE_NAME, ".deleteWeapon.insert threw <" + e.toString() + ">.");
                }
                result = db.delete(TABLE_WEAPON, PK_WEAPON + "=" + String.valueOf(id), null);
            } else {
                throw new NullPointerException("Can't delete weapon <" + String.valueOf(id) + ">. " +
                        "It's not in table now.");
            }
        } else {
            throw new NullPointerException("Can't reach database in deletePerson.");
        }
        return result;
    }

    public ArrayList<Weapon> getAllWeapon() {
        ArrayList<Weapon> weaponList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        if(db != null) {
            cursor = db.query(TABLE_WEAPON, new String[] { "*" }, null, null, null, null, KEY_MODEL);
        } else {
            throw new NullPointerException("Can't reach database in getAllWeapon.");
        }
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if(cursor.getCount() > 0) do {
            Person owner = getPersonBySecretName(cursor.getString(2));
            if(owner == null) {
                owner = getPersonFromArchive(cursor.getString(2)).get(0);
            }
            weaponList.add(new Weapon(cursor.getInt(0), cursor.getString(1), owner));
            cursor.moveToNext();
        } while(!cursor.isAfterLast());
        return weaponList;
    }

    public ArrayList<Weapon> getWeaponOfPerson(String secretName) {
        ArrayList<Weapon> weaponList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        if(db != null) {
            cursor = db.query(TABLE_WEAPON, new String[] { "*" },
                    FK_PERSON + " = ?", // where
                    new String[] { secretName }, // value to replace "?"
                    null, // groupBy
                    null, // having
                    KEY_MODEL); // orderBy
        } else {
            throw new NullPointerException("Can't reach database in getWeaponOfPerson.");
        }
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if(cursor.getCount() > 0) do {
            Person owner = getPersonBySecretName(cursor.getString(2));
            if(owner == null) {
                owner = getPersonFromArchive(cursor.getString(2)).get(0);
            }
            weaponList.add(new Weapon(cursor.getInt(0), cursor.getString(1), owner));
            cursor.moveToNext();
        } while(!cursor.isAfterLast());
        return weaponList;
    }

    public Weapon getWeaponById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        if(db != null) {
            cursor = db.query(TABLE_WEAPON, new String[] { "*" },
                    PK_WEAPON + " = ?", // where
                    new String[] { String.valueOf(id) }, // value to replace "?"
                    null, // groupBy
                    null, // having
                    null); // orderBy
        } else {
            throw new NullPointerException("Can't reach database in getWeaponById.");
        }
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if(cursor.getCount() > 0) {
            Person owner = getPersonBySecretName(cursor.getString(2));
            if(owner == null) {
                owner = getPersonFromArchive(cursor.getString(2)).get(0);
            }
            return new Weapon(cursor.getInt(0), cursor.getString(1), owner);
        }
        return null;
    }

}