package ua.zs.elements;

public class Classified {

    public final static int UNCLASSIFIED = 0;
    public final static int CONFIDENTIAL = 1;
    public final static int SECRET = 2;
    public final static int TOP_SECRET = 3;

    public static String toString(int code) {
        switch(code) {
            case UNCLASSIFIED: return "відкритий";
            case CONFIDENTIAL: return "таємний";
            case SECRET: return "цілком таємний";
            case TOP_SECRET: return "особливої важливості";
        }
        return null;
    }

}
