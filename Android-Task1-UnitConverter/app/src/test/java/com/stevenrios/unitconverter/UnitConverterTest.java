package com.stevenrios.unitconverter;

import static org.junit.Assert.*;
import static com.stevenrios.unitconverter.UnitConverter.Category.*;
import org.junit.Test;

public class UnitConverterTest {
    private static final double E = 1e-9;
    @Test public void length() {
        assertEquals(1.0, UnitConverter.convert(LENGTH, "Inch (in)", "Centimetre (cm)", 1 / 2.54), E);
        assertEquals(1.609344, UnitConverter.convert(LENGTH, "Mile (mi)", "Kilometre (km)", 1), E);
        assertEquals(3.0, UnitConverter.convert(LENGTH, "Yard (yd)", "Foot (ft)", 1), E);
    }
    @Test public void weight() {
        assertEquals(2.20462262, UnitConverter.convert(WEIGHT, "Kilogram (kg)", "Pound (lb)", 1), 1e-6);
        assertEquals(16.0, UnitConverter.convert(WEIGHT, "Pound (lb)", "Ounce (oz)", 1), E);
    }
    @Test public void temperature() {
        assertEquals(212.0, UnitConverter.convert(TEMPERATURE, "Celsius (°C)", "Fahrenheit (°F)", 100), E);
        assertEquals(-40.0, UnitConverter.convert(TEMPERATURE, "Fahrenheit (°F)", "Celsius (°C)", -40), E);
        assertEquals(0.0, UnitConverter.convert(TEMPERATURE, "Celsius (°C)", "Kelvin (K)", -273.15), E);
    }
    @Test public void volumeAndSpeed() {
        assertEquals(3.785411784, UnitConverter.convert(VOLUME, "US gallon (gal)", "Litre (L)", 1), E);
        assertEquals(100.0, UnitConverter.convert(SPEED, "Metre/second (m/s)", "Kilometre/hour (km/h)", 100 / 3.6), 1e-9);
    }
    @Test public void sameUnitIsIdentity() {
        for (UnitConverter.Category c : UnitConverter.Category.values())
            for (String u : UnitConverter.units(c)) assertEquals(42.5, UnitConverter.convert(c, u, u, 42.5), 1e-9);
    }
    @Test public void validation() {
        assertEquals("empty", UnitConverter.validate(""));
        assertEquals("empty", UnitConverter.validate("   "));
        assertEquals("nan", UnitConverter.validate("abc"));
        assertEquals("nan", UnitConverter.validate("1e999"));
        assertNull(UnitConverter.validate("-3.5"));
    }
    @Test public void formatting() {
        assertEquals("2.54 cm", UnitConverter.format(2.54, "Centimetre (cm)"));
        assertEquals("0.333333 m", UnitConverter.format(1 / 3.0, "Metre (m)"));
        assertEquals("1000000 g", UnitConverter.format(1e6, "Gram (g)"));
    }
}
