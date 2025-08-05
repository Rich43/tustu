package com.efiAnalytics.plugin.ecu.servers;

import com.efiAnalytics.plugin.ecu.UiCurve;
import com.efiAnalytics.plugin.ecu.UiTable;
import java.awt.Component;
import java.util.List;
import javax.swing.JComponent;

/* loaded from: TunerStudioPluginAPI.jar:com/efiAnalytics/plugin/ecu/servers/UiSettingServer.class */
public interface UiSettingServer {
    JComponent getUiComponent(String str);

    void disposeUiComponent(Component component);

    List<String> getUiPanelNames();

    List<UiCurve> getUiCurves();

    List<UiTable> getUiTable();
}
