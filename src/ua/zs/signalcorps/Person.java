package ua.zs.signalcorps;


public class Person {

    private String firstName;
    private String secondName;
    private String fathersName;
    private String secretName;
    private int militaryRank;
    private int equipageCode;
    private String personalPassword;

    public Person(String secretName, String firstName, String fathersName, String secondName, int militaryRank,
                  int equipageCode, String personalPassword) {
        if(secretName != null && secretName.length() > 0 ) {
            this.secretName = secretName;
        } else {
            throw new IllegalArgumentException("Secret name must not be null or zero-length.");
        }
        if(secondName != null && secondName.length() > 0 ) {
            this.secondName = secondName;
        } else {
            throw new IllegalArgumentException("Second name must not be null or zero-length.");
        }
        if(firstName != null && firstName.length() > 0 ) {
            this.firstName = firstName;
        } else {
            throw new IllegalArgumentException("First name must not be null or zero-length.");
        }
        if(fathersName != null) {
            this.fathersName = fathersName;
        } else {
            this.fathersName = "";
        }
        if(militaryRank >=0 && militaryRank < 20) {
            this.militaryRank = militaryRank;
        } else {
            throw new IllegalArgumentException("Rank code must be from 0 to 19. " +
                    "Use <Rank> class for legal constants.");
        }
        this.equipageCode = equipageCode;
        if(personalPassword != null && personalPassword.length() > 0 ) {
            this.personalPassword = personalPassword;
        } else {
            throw new IllegalArgumentException("Personal password must not be null or zero-length.");
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getFathersName() {
        return fathersName;
    }

    public String getSecretName() {
        return secretName;
    }

    public int getRank() {
        return militaryRank;
    }

    public int getEquipage() {
        return equipageCode;
    }

    public String getPersonalPassword() {
        return personalPassword;
    }

    public void giveSecretName(String name) {
        if(name != null && name.length() > 0) {
            this.secretName = name.toUpperCase();
        } else {
            throw new IllegalArgumentException("Secret name must not be null or zero-length.");
        }
    }

    public void assignToEquipage(int equipageCode) {
        this.equipageCode = equipageCode;
    }

    public void setMilitaryRank(int rank) {
        if(rank >=0 && rank < 20) {
            this.militaryRank++;
        } else {
            throw new IllegalArgumentException("Rank code must be from 0 to 19. " +
                    "Use <Rank> class for legal constants.");
        }
    }

    public void changePersonalPassword(String password) {
        if(password != null && password.length() > 0) {
            this.personalPassword = password;
        } else {
            throw new IllegalArgumentException("Personal password must not be null or zero-length.");
        }
    }

    public String toString() {
        return Rank.toString(militaryRank) + " " +
                secondName + " " +
                firstName + " " +
                fathersName +
                " (позивний: " + secretName +
                ( equipageCode!=0 ? ", екіпаж №"+String.valueOf(equipageCode) : "" ) +
                ")"; // ex. "Капітан Коваленко Остап Іванович (позивний: ТЕРНОПІЛЬ-10, екіпаж №2715)"
    }

}
