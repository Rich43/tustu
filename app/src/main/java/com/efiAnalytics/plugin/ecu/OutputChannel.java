package com.efiAnalytics.plugin.ecu;

/* loaded from: TunerStudioPluginAPI.jar:com/efiAnalytics/plugin/ecu/OutputChannel.class */
public class OutputChannel {
    private String name = "";
    private String units = "";
    private String formula = "";
    private double minValue = Double.MIN_VALUE;
    private double maxValue = Double.MAX_VALUE;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnits() {
        return this.units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public double getMinValue() {
        return this.minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return this.maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public String getFormula() {
        return this.formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }
}
