package com.efiAnalytics.plugin.ecu;

/* loaded from: TunerStudioPluginAPI.jar:com/efiAnalytics/plugin/ecu/UiCurve.class */
public interface UiCurve {
    String getName();

    String getXAxisOutputChannelName();

    String getYAxisOutputChannelName();

    int getYAxisParameterCount();

    String getYAxisParameterName(int i2);

    int getXAxisParameterCount();

    String getXAxisParameterName(int i2);
}
