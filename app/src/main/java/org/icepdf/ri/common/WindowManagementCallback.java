package org.icepdf.ri.common;

import java.net.URL;
import java.util.List;
import java.util.Properties;
import javax.swing.JFrame;
import org.icepdf.ri.util.PropertiesManager;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/WindowManagementCallback.class */
public interface WindowManagementCallback {
    void newWindow(String str);

    void newWindow(URL url);

    void disposeWindow(SwingController swingController, JFrame jFrame, Properties properties);

    void minimiseAllWindows();

    void bringAllWindowsToFront(SwingController swingController);

    void bringWindowToFront(int i2);

    List getWindowDocumentOriginList(SwingController swingController);

    void quit(SwingController swingController, JFrame jFrame, Properties properties);

    PropertiesManager getProperties();
}
