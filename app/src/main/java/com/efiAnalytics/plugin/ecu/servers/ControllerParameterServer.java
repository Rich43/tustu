package com.efiAnalytics.plugin.ecu.servers;

import com.efiAnalytics.plugin.ecu.ControllerException;
import com.efiAnalytics.plugin.ecu.ControllerParameter;
import com.efiAnalytics.plugin.ecu.ControllerParameterChangeListener;

/* loaded from: TunerStudioPluginAPI.jar:com/efiAnalytics/plugin/ecu/servers/ControllerParameterServer.class */
public interface ControllerParameterServer {
    void subscribe(String str, String str2, ControllerParameterChangeListener controllerParameterChangeListener) throws ControllerException;

    void unsubscribe(ControllerParameterChangeListener controllerParameterChangeListener);

    String[] getParameterNames(String str);

    ControllerParameter getControllerParameter(String str, String str2) throws ControllerException;

    void updateParameter(String str, String str2, double d2) throws ControllerException;

    void updateParameter(String str, String str2, double[][] dArr) throws ControllerException;

    void updateParameter(String str, String str2, String str3) throws ControllerException;

    void burnData(String str) throws ControllerException;
}
