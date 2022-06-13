package com.example.parstagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("PWf9T3ADAewS14G9iUaP9D60VngyTycp88mYjZYm")
                .clientKey("ZWRDzaM7gw96ZY97rTYzdRqfafrvMA5at4gr4dIH")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
