package com.stevenrios.unitconverter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.LinkedHashMap;
import java.util.Map;

/** Pure-Java conversion logic, so it can be unit-tested on the JVM without Android. */
public final class UnitConverter {

    public enum Category { LENGTH, WEIGHT, TEMPERATURE, VOLUME, SPEED }

    // factor = value of 1 unit expressed in the category's base unit (metre, gram, litre, m/s)
    private static final Map<Category, LinkedHashMap<String, Double>> FACTORS = new LinkedHashMap<>();
    static {
        LinkedHashMap<String, Double> len = new LinkedHashMap<>();
        len.put("Millimetre (mm)", 0.001); len.put("Centimetre (cm)", 0.01); len.put("Metre (m)", 1.0);
        len.put("Kilometre (km)", 1000.0); len.put("Inch (in)", 0.0254); len.put("Foot (ft)", 0.3048);
        len.put("Yard (yd)", 0.9144); len.put("Mile (mi)", 1609.344);
        FACTORS.put(Category.LENGTH, len);
        LinkedHashMap<String, Double> w = new LinkedHashMap<>();
        w.put("Milligram (mg)", 0.001); w.put("Gram (g)", 1.0); w.put("Kilogram (kg)", 1000.0);
        w.put("Tonne (t)", 1_000_000.0); w.put("Ounce (oz)", 28.349523125); w.put("Pound (lb)", 453.59237);
        FACTORS.put(Category.WEIGHT, w);
        LinkedHashMap<String, Double> v = new LinkedHashMap<>();
        v.put("Millilitre (mL)", 0.001); v.put("Litre (L)", 1.0); v.put("Cubic metre (m³)", 1000.0);
        v.put("US cup", 0.2365882365); v.put("US gallon (gal)", 3.785411784); v.put("Imperial gallon", 4.54609);
        FACTORS.put(Category.VOLUME, v);
        LinkedHashMap<String, Double> s = new LinkedHashMap<>();
        s.put("Metre/second (m/s)", 1.0); s.put("Kilometre/hour (km/h)", 1000.0 / 3600.0);
        s.put("Mile/hour (mph)", 1609.344 / 3600.0); s.put("Knot (kn)", 1852.0 / 3600.0);
        FACTORS.put(Category.SPEED, s);
        LinkedHashMap<String, Double> t = new LinkedHashMap<>();
        t.put("Celsius (°C)", 0.0); t.put("Fahrenheit (°F)", 0.0); t.put("Kelvin (K)", 0.0);
        FACTORS.put(Category.TEMPERATURE, t);
    }

    private UnitConverter() {}

    public static String[] units(Category c) { return FACTORS.get(c).keySet().toArray(new String[0]); }

    public static double convert(Category c, String from, String to, double value) {
        if (c == Category.TEMPERATURE) return fromKelvin(to, toKelvin(from, value));
        Map<String, Double> f = FACTORS.get(c);
        if (!f.containsKey(from) || !f.containsKey(to)) throw new IllegalArgumentException("Unknown unit");
        return value * f.get(from) / f.get(to);
    }

    private static double toKelvin(String u, double v) {
        if (u.startsWith("Celsius")) return v + 273.15;
        if (u.startsWith("Fahrenheit")) return (v - 32) * 5.0 / 9.0 + 273.15;
        if (u.startsWith("Kelvin")) return v;
        throw new IllegalArgumentException("Unknown unit");
    }

    private static double fromKelvin(String u, double k) {
        if (u.startsWith("Celsius")) return k - 273.15;
        if (u.startsWith("Fahrenheit")) return (k - 273.15) * 9.0 / 5.0 + 32;
        if (u.startsWith("Kelvin")) return k;
        throw new IllegalArgumentException("Unknown unit");
    }

    /** "12.5 cm" style label: up to 6 significant digits, no trailing zeros. */
    public static String format(double v, String unit) {
        String abbr = unit.contains("(") ? unit.substring(unit.indexOf('(') + 1, unit.indexOf(')')) : unit;
        String num = new BigDecimal(v).round(new MathContext(6)).stripTrailingZeros().toPlainString();
        if (num.equals("-0")) num = "0";
        return num + " " + abbr;
    }

    /** Returns null when the text is a valid number, else an error key: "empty" or "nan". */
    public static String validate(String text) {
        if (text == null || text.trim().isEmpty()) return "empty";
        try { double d = Double.parseDouble(text.trim()); return Double.isFinite(d) ? null : "nan"; }
        catch (NumberFormatException e) { return "nan"; }
    }
}
