package com.efiAnalytics.plugin.ecu;

import java.awt.Dimension;
import java.util.ArrayList;

/* loaded from: TunerStudioPluginAPI.jar:com/efiAnalytics/plugin/ecu/ControllerParameter.class */
public class ControllerParameter {
    public static final String PARAM_CLASS_BITS = "bits";
    public static final String PARAM_CLASS_SCALAR = "scalar";
    public static final String PARAM_CLASS_ARRAY = "array";
    private String paramClass = null;
    private int decimalPlaces = 0;
    private Dimension shape = null;
    private String units = "";
    private double min = Double.MIN_VALUE;
    private double max = Double.MAX_VALUE;
    private ArrayList optionDescriptions = null;
    private double[][] arrayValues = (double[][]) null;
    private String stringValue = null;

    public String getParamClass() {
        return this.paramClass;
    }

    public void setParamClass(String paramClass) {
        this.paramClass = paramClass;
    }

    public int getDecimalPlaces() {
        return this.decimalPlaces;
    }

    public void setDecimalPlaces(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public Dimension getShape() {
        return this.shape;
    }

    public void setShape(Dimension shape) {
        this.shape = shape;
    }

    public String getUnits() {
        return this.units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public double getMin() {
        return this.min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return this.max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public ArrayList getOptionDescriptions() {
        return this.optionDescriptions;
    }

    public void setOptionDescriptions(ArrayList optionDescriptions) {
        this.optionDescriptions = optionDescriptions;
    }

    public double[][] getArrayValues() {
        return this.arrayValues;
    }

    public void setScalarValue(double scalar) {
        this.arrayValues = new double[1][1];
        this.arrayValues[0][0] = scalar;
    }

    public double getScalarValue() {
        return this.arrayValues[0][0];
    }

    public void setArrayValues(double[][] arrayValues) {
        this.arrayValues = arrayValues;
    }

    public String getStringValue() {
        return this.stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}
