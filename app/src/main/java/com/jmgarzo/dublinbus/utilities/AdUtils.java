package com.jmgarzo.dublinbus.utilities;

import com.google.android.gms.ads.AdRequest;

/**
 * Created by jmgarzo on 29/08/17.
 */

public class AdUtils {

    public static AdRequest getAdRequest() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("7A96701D1162F3BAA9884E567FDE869A")
                .addTestDevice("1C1F83B043C4EB13E7A72D25906BBFA4")
                .build();
        return  adRequest;
    }

}
