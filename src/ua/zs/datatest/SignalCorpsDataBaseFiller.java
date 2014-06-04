package ua.zs.datatest;

import android.content.Context;
import android.os.Environment;
import ua.zs.elements.*;
import ua.zs.elements.Package;
import ua.zs.signalcorps.SignalCorpsDB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class SignalCorpsDataBaseFiller {

    Context context;

    public SignalCorpsDataBaseFiller(Context context) {
        this.context = context;
    }

    public void test() {
        fillPeople("people.test"); // File on external storage
        fillWeapons();
        fillTransport();
        fillWiredContacts();
        fillSatelliteContacts();
        fillRadiorelatedContacts();
        fillRadioContacts();
        fillCourierContacts();
        fillPackages();
    }

    private void fillPeople(String fileName) {
        String[] codes = { "КИЇВ", "ЛЬВІВ", "ХЕРСОН", "ХАРКІВ",
                "ДОНЕЦЬК", "ОДЕСА", "ЛУГАНСЬК", "ПОЛТАВА", "РІВНЕ" };
        Scanner scanner = null;
        SignalCorpsDB db = new SignalCorpsDB(context);
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(Environment
                    .getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator + fileName)));
            Random random = new Random();
            while(scanner.hasNext()) {
                String name;
                do {
                    name = codes[random.nextInt(9)] + "-" +
                            String.valueOf(random.nextInt(1000));
                } while(db.getPersonBySecretName(name) != null);
                int rank = random.nextInt(19);
                int classified = 3;
                if(rank < 12) classified = 2;
                if(rank < 9) classified = 1;
                if(rank < 1) classified = 0;
                String surname = scanner.next();
                String firstName = scanner.next();
                String fathersName = scanner.next();
                db.addPerson(new Person(name, // Secret Name
                                        firstName, // First Name
                                        fathersName, // Father's Name
                                        surname, // Second Name
                                        rank, // Rank
                                        random.nextInt(20)+1, // Equipage
                                        "1234", // Password
                                        classified)); // Classified rank
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private void fillWeapons() {
        SignalCorpsDB db = new SignalCorpsDB(context);
        String[] availableWeapon = { "АК-74", "АК-74М", "АКМСУ",
                                    "АКС-74", "АКС-74У", "ПЗРК \"ІГЛА\"",
                                    "НРС", "9-мм ПМ", "РПГ-7",
                                    "РПК-74", "СГД" };
        ArrayList<String> people = db.getAllPersonsSecretNames();
        Random random = new Random();
        int peopleCount = people.size();
        for (int i = 0; i < peopleCount * 2; i++) {
            db.addWeapon(new Weapon(1839+i, // id
                    availableWeapon[random.nextInt(11)], // model
                    db.getPersonBySecretName(people.get(random.nextInt(peopleCount))))); // owner
        }
    }

    private void fillTransport() {
        String[] availableTransport = { "БМП-1", "БМП-2", "БМП-3",
                                        "БМПВ-64", "БРДМ-2", "БТМП-84",
                                        "ГАЗ-66", "Мі-26", "Т-64",
                                        "УАЗ-469" };
        SignalCorpsDB db = new SignalCorpsDB(context);
        Random random = new Random();
        long now = Calendar.getInstance().getTime().getTime();
        for (int i = 0; i < 50; i++) {
            int equipage;
            do {
                equipage = random.nextInt(20)+1;
            } while(db.getEquipageById(equipage) == null);
            db.addTransport(new Transport(i+461, // id
                    availableTransport[random.nextInt(10)], // model
                    new Date(now - random.nextInt(86400000) - 6000), // last techwork
                    db.getEquipageById(equipage))); // owner
        }
    }

    private void fillWiredContacts() {
        SignalCorpsDB db = new SignalCorpsDB(context);
        Random random = new Random();
        long now = Calendar.getInstance().getTime().getTime();
        for (int i = 0; i < 25; i++) {
            int equipage;
            do {
                equipage = random.nextInt(20)+1;
            } while(db.getEquipageById(equipage) == null);
            db.addContact(new WiredContact(25781+i, // id
                    db.getEquipageById(equipage), // equipage
                    new Date(now + random.nextInt(86400000) + 6000), // start time
                    random.nextInt(50)+1)); // node
        }
    }

    private void fillSatelliteContacts() {
        String[] availableSatellites = { "SIRIUS-1", "SIRIUS-2", "SIRIUS-3", "SIRIUS-4",
                                         "HOT BIRD 1", "HOT BIRD 2", "HOT BIRD 3",
                                         "APSTAR-1", "APSTAR-2", "AMOS-1", "AMOS-2",
                                         "OPTUS-1", "OPTUS-2", "STAR ONE", "WILDBLUE" };
        SignalCorpsDB db = new SignalCorpsDB(context);
        Random random = new Random();
        long now = Calendar.getInstance().getTime().getTime();
        for (int i = 0; i < 30; i++) {
            int equipage;
            do {
                equipage = random.nextInt(20)+1;
            } while(db.getEquipageById(equipage) == null);
            db.addContact(new SatelliteContact(89413+i, // id
                    db.getEquipageById(equipage), // equipage
                    new Date(now + random.nextInt(86400000) + 6000), // start time
                    availableSatellites[random.nextInt(15)])); // satellite
        }
    }

    private void fillRadiorelatedContacts() {
        SignalCorpsDB db = new SignalCorpsDB(context);
        Random random = new Random();
        long now = Calendar.getInstance().getTime().getTime();
        for (int i = 0; i < 25; i++) {
            int equipage;
            do {
                equipage = random.nextInt(20)+1;
            } while(db.getEquipageById(equipage) == null);
            db.addContact(new RadioRelatedContact(36807+i, // id
                    db.getEquipageById(equipage), // equipage
                    new Date(now + random.nextInt(86400000) + 6000), // start time
                    random.nextInt(360))); // azimuth
        }
    }

    private void fillRadioContacts() {
        SignalCorpsDB db = new SignalCorpsDB(context);
        Random random = new Random();
        long now = Calendar.getInstance().getTime().getTime();
        for (int i = 0; i < 25; i++) {
            int equipage;
            do {
                equipage = random.nextInt(20)+1;
            } while(db.getEquipageById(equipage) == null);
            db.addContact(new RadioContact(18004+i, // id
                    db.getEquipageById(equipage), // equipage
                    new Date(now + random.nextInt(86400000) + 6000), // start time
                    random.nextInt(100)+1)); // frequency
        }
    }

    private void fillCourierContacts() {
        String[] codeLetters = { "А", "Б", "В", "Г", "Д" };
        SignalCorpsDB db = new SignalCorpsDB(context);
        Random random = new Random();
        long now = Calendar.getInstance().getTime().getTime();
        for (int i = 0; i < 25; i++) {
            int equipage;
            do {
                equipage = random.nextInt(20)+1;
            } while(db.getEquipageById(equipage) == null);
            db.addContact(new CourierContact(79443+i, // id
                    db.getEquipageById(equipage), // equipage
                    new Date(now + random.nextInt(86400000) + 6000), // start time
                    "в/ч " + codeLetters[random.nextInt(5)] + String.valueOf(1000 + random.nextInt(9000)))); // address
        }
    }

    private void fillPackages() {
        SignalCorpsDB db = new SignalCorpsDB(context);
        Random random = new Random();
        for (int i = 0; i < 25; i++) {
            int equipage;
            do {
                equipage = random.nextInt(20)+1;
            } while(db.getEquipageById(equipage) == null);
            db.addPackage(new Package(159+i, // id
                    (CourierContact) db.getContactById(79444 + random.nextInt(24)), // contact
                    random.nextInt(4))); // classified
        }
    }

}
