package com.efiAnalytics.plugin.ecu.servers;

import com.efiAnalytics.plugin.ecu.ControllerException;

/* loaded from: TunerStudioPluginAPI.jar:com/efiAnalytics/plugin/ecu/servers/BurnExecutor.class */
public interface BurnExecutor {
    void burnData(String str) throws ControllerException;
}
