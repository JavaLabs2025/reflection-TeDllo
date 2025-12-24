package org.example.classes;

import org.example.generator.Generatable;

@Generatable
public class PrimitiveExample {
    // Primitive types
    private byte byteValue;
    private short shortValue;
    private int intValue;
    private long longValue;
    private float floatValue;
    private double doubleValue;
    private char charValue;
    private boolean booleanValue;

    // Primitive wrapper types
    private Byte byteWrapper;
    private Short shortWrapper;
    private Integer intWrapper;
    private Long longWrapper;
    private Float floatWrapper;
    private Double doubleWrapper;
    private Character charWrapper;
    private Boolean booleanWrapper;

    public PrimitiveExample(byte byteValue, short shortValue, int intValue, long longValue,
                           float floatValue, double doubleValue, char charValue, boolean booleanValue,
                           Byte byteWrapper, Short shortWrapper, Integer intWrapper, Long longWrapper,
                           Float floatWrapper, Double doubleWrapper, Character charWrapper, Boolean booleanWrapper) {
        this.byteValue = byteValue;
        this.shortValue = shortValue;
        this.intValue = intValue;
        this.longValue = longValue;
        this.floatValue = floatValue;
        this.doubleValue = doubleValue;
        this.charValue = charValue;
        this.booleanValue = booleanValue;
        this.byteWrapper = byteWrapper;
        this.shortWrapper = shortWrapper;
        this.intWrapper = intWrapper;
        this.longWrapper = longWrapper;
        this.floatWrapper = floatWrapper;
        this.doubleWrapper = doubleWrapper;
        this.charWrapper = charWrapper;
        this.booleanWrapper = booleanWrapper;
    }

    @Override
    public String toString() {
        return "PrimitiveExample{" +
                "byteValue=" + byteValue +
                ", shortValue=" + shortValue +
                ", intValue=" + intValue +
                ", longValue=" + longValue +
                ", floatValue=" + floatValue +
                ", doubleValue=" + doubleValue +
                ", charValue=" + charValue +
                ", booleanValue=" + booleanValue +
                ", byteWrapper=" + byteWrapper +
                ", shortWrapper=" + shortWrapper +
                ", intWrapper=" + intWrapper +
                ", longWrapper=" + longWrapper +
                ", floatWrapper=" + floatWrapper +
                ", doubleWrapper=" + doubleWrapper +
                ", charWrapper=" + charWrapper +
                ", booleanWrapper=" + booleanWrapper +
                '}';
    }
}

