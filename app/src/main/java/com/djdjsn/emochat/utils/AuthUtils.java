package com.djdjsn.emochat.utils;

import java.util.Locale;

public class AuthUtils {

    public static String emailize(String id) {
        return String.format(Locale.getDefault(), "%s@emochat.com", id);
    }

}
