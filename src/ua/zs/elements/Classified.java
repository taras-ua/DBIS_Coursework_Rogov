package ua.zs.elements;

import android.content.Context;
import ua.zs.signalcorps.R;

public class Classified {

    public final static int UNCLASSIFIED = 0;
    public final static int CONFIDENTIAL = 1;
    public final static int SECRET = 2;
    public final static int TOP_SECRET = 3;

    public static String levelOfPerson(Context context, int code) {
        switch(code) {
            case UNCLASSIFIED: return context.getResources().getString(R.string.UNCLASSIFIED);
            case CONFIDENTIAL: return context.getResources().getString(R.string.CONFIDENTIAL);
            case SECRET: return context.getResources().getString(R.string.SECRET);
            case TOP_SECRET: return context.getResources().getString(R.string.TOP_SECRET);
        }
        return null;
    }

    public static String packageType(Context context, int code) {
        switch(code) {
            case UNCLASSIFIED: return context.getResources().getString(R.string.UNCLASSIFIED_PACKAGE);
            case CONFIDENTIAL: return context.getResources().getString(R.string.CONFIDENTIAL_PACKAGE);
            case SECRET: return context.getResources().getString(R.string.SECRET_PACKAGE);
            case TOP_SECRET: return context.getResources().getString(R.string.TOP_SECRET_PACKAGE);
        }
        return null;
    }

}
