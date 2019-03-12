package com.example.pr06_version2.utils;

import android.content.Intent;
import android.net.Uri;

public class IntentUtils {

    private IntentUtils() {
    }

    public static Intent intentEmail(String email){
        return new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
    }

    public static Intent intentPhone(String phone){
        return new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
    }

    public static Intent intentMapByAddress(String address){
        return new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + address));
    }

    public static Intent intentSearchWeb(String web){
        return new Intent(Intent.ACTION_VIEW, Uri.parse(web));
    }
}
