package com.efiAnalytics.plugin.ecu.servers;

import com.efiAnalytics.plugin.ecu.ControllerException;

/* loaded from: TunerStudioPluginAPI.jar:com/efiAnalytics/plugin/ecu/servers/UiSettingServerProvider.class */
public interface UiSettingServerProvider {
    UiSettingServer getUiComponentServer(String str) throws ControllerException;
}
