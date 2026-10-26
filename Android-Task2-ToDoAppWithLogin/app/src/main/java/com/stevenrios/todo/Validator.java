package com.stevenrios.todo;

/** Input rules for registration / tasks. Returns an error message, or null when valid. */
public final class Validator {
    private Validator() {}
    public static String username(String u) {
        if (u == null || u.trim().isEmpty()) return "Username is required";
        if (!u.trim().matches("[A-Za-z0-9_]{3,20}")) return "Username must be 3–20 letters, digits or _";
        return null;
    }
    public static String password(String p, String confirm) {
        if (p == null || p.isEmpty()) return "Password is required";
        if (p.length() < 6) return "Password must be at least 6 characters";
        if (confirm != null && !p.equals(confirm)) return "Passwords do not match";
        return null;
    }
    public static String task(String t) {
        if (t == null || t.trim().isEmpty()) return "Task cannot be empty";
        if (t.trim().length() > 200) return "Task is too long (max 200)";
        return null;
    }
}
