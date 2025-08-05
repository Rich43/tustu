package com.efiAnalytics.plugin;

import com.efiAnalytics.plugin.ecu.ControllerAccess;
import javax.swing.JComponent;

/* loaded from: TunerStudioPluginAPI.jar:com/efiAnalytics/plugin/ApplicationPlugin.class */
public interface ApplicationPlugin {
    public static final int DIALOG_WIDGET = 1;
    public static final int PERSISTENT_DIALOG_PANEL = 2;
    public static final int TAB_PANEL = 4;
    public static final double PLUGIN_API_VERSION = 1.0d;

    String getIdName();

    int getPluginType();

    String getDisplayName();

    String getDescription();

    void initialize(ControllerAccess controllerAccess);

    boolean displayPlugin(String str);

    boolean isMenuEnabled();

    String getAuthor();

    JComponent getPluginPanel();

    void close();

    String getHelpUrl();

    String getVersion();

    double getRequiredPluginSpec();
}
