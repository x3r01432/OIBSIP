package com.stevenrios.todo;

import android.content.Context;
import android.content.SharedPreferences;

/** Remembers the logged-in user id (only the id, never the password). */
public final class Session {
    private static final String PREFS = "session";
    private Session() {}
    public static void login(Context c, long id, String name) { prefs(c).edit().putLong("uid", id).putString("name", name).apply(); }
    public static long userId(Context c) { return prefs(c).getLong("uid", -1); }
    public static String name(Context c) { return prefs(c).getString("name", ""); }
    public static void logout(Context c) { prefs(c).edit().clear().apply(); }
    private static SharedPreferences prefs(Context c) { return c.getSharedPreferences(PREFS, Context.MODE_PRIVATE); }
}
