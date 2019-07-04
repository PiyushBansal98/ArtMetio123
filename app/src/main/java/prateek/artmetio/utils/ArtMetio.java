package prateek.artmetio.utils;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

public class ArtMetio extends MultiDexApplication {

    private static ArtMetio mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(getApplicationContext());
        mInstance=this;
    }

    public static synchronized ArtMetio getInstance() {
        return mInstance;
    }
}

