package ua.zs.elements;

import ua.zs.signalcorps.R;

public class Rank {

    public final static int CIVILIAN = 0;
    public final static int SOLDIER = 1;
    public final static int SENIOR_SOLDIER = 2;
    public final static int JUNIOR_SERGEANT = 3;
    public final static int SERGEANT = 4;
    public final static int SENIOR_SERGEANT = 5;
    public final static int STARSHYNA = 6;
    public final static int PRAPORSHCHYK = 7;
    public final static int SENIOR_PRAPORSHCHYK = 8;
    public final static int JUNIOR_LIEUTENANT = 9;
    public final static int LIEUTENANT = 10;
    public final static int SENIOR_LIEUTENANT = 11;
    public final static int CAPTAIN = 12;
    public final static int MAJOR = 13;
    public final static int PIDPOLKOVNYK = 14;
    public final static int POLKOVNYK = 15;
    public final static int GENERAL_MAJOR = 16;
    public final static int GENERAL_LIEUTENANT = 17;
    public final static int GENERAL_POLKOVNYK = 18;
    public final static int GENERAL_OF_ARMY = 19;

    public static int toImage(int code) {
        switch(code) {
            case CIVILIAN: return R.drawable.sign_civil;
            case SOLDIER: return R.drawable.sign_sold;
            case SENIOR_SOLDIER: return R.drawable.sign_sensold;
            case JUNIOR_SERGEANT: return R.drawable.sign_junserg;
            case SERGEANT: return R.drawable.sign_serg;
            case SENIOR_SERGEANT: return R.drawable.sign_senserg;
            case STARSHYNA: return R.drawable.sign_starsh;
            case PRAPORSHCHYK: return R.drawable.sign_prap;
            case SENIOR_PRAPORSHCHYK: return R.drawable.sign_senprap;
            case JUNIOR_LIEUTENANT: return R.drawable.sign_junlieut;
            case LIEUTENANT: return R.drawable.sign_lieut;
            case SENIOR_LIEUTENANT: return R.drawable.sign_senlieut;
            case CAPTAIN: return R.drawable.sign_cap;
            case MAJOR: return R.drawable.sign_major;
            case PIDPOLKOVNYK: return R.drawable.sign_pidpolkovnyk;
            case POLKOVNYK: return R.drawable.sign_polkovnyk;
            case GENERAL_MAJOR: return R.drawable.sign_genmajor;
            case GENERAL_LIEUTENANT: return R.drawable.sign_genlieut;
            case GENERAL_POLKOVNYK: return R.drawable.sign_genpolk;
            case GENERAL_OF_ARMY: return R.drawable.sign_armygen;
        }
        return 0;
    }

    public static String toString(int code) {
        switch(code) {
            case CIVILIAN: return "Цивільний";
            case SOLDIER: return "Солдат";
            case SENIOR_SOLDIER: return "Старший солдат";
            case JUNIOR_SERGEANT: return "Молодший сержант";
            case SERGEANT: return "Сержант";
            case SENIOR_SERGEANT: return "Старший сержант";
            case STARSHYNA: return "Старшина";
            case PRAPORSHCHYK: return "Прапорщик";
            case SENIOR_PRAPORSHCHYK: return "Старший прапорщик";
            case JUNIOR_LIEUTENANT: return "Молодший лейтенант";
            case LIEUTENANT: return "Лейтенант";
            case SENIOR_LIEUTENANT: return "Старший лейтенант";
            case CAPTAIN: return "Капітан";
            case MAJOR: return "Майор";
            case PIDPOLKOVNYK: return "Підполковник";
            case POLKOVNYK: return "Полковник";
            case GENERAL_MAJOR: return "Генерал-майор";
            case GENERAL_LIEUTENANT: return "Генерал-лейтенант";
            case GENERAL_POLKOVNYK: return "Генерал-полковник";
            case GENERAL_OF_ARMY: return "Генерал Армії України";
        }
        return null;
    }

}
