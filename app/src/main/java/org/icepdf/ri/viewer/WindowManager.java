package org.icepdf.ri.viewer;

import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.util.Defs;
import org.icepdf.ri.common.MyAnnotationCallback;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.icepdf.ri.common.ViewModel;
import org.icepdf.ri.common.WindowManagementCallback;
import org.icepdf.ri.util.PropertiesManager;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/viewer/WindowManager.class */
public class WindowManager implements WindowManagementCallback {
    private static WindowManager windowManager;
    private PropertiesManager properties;
    private ArrayList<SwingController> controllers;
    private long newWindowInvocationCounter = 0;
    private ResourceBundle messageBundle = null;

    private WindowManager() {
    }

    public static WindowManager getInstance() {
        return windowManager;
    }

    public static WindowManager createInstance(PropertiesManager properties, ResourceBundle messageBundle) {
        windowManager = new WindowManager();
        windowManager.properties = properties;
        windowManager.controllers = new ArrayList<>();
        if (messageBundle != null) {
            windowManager.messageBundle = messageBundle;
        } else {
            windowManager.messageBundle = ResourceBundle.getBundle(PropertiesManager.DEFAULT_MESSAGE_BUNDLE);
        }
        if (Defs.booleanProperty("org.icepdf.core.verbose", true)) {
            System.out.println("\nICEsoft ICEpdf Viewer " + Document.getLibraryVersion());
            System.out.println("Copyright ICEsoft Technologies, Inc.\n");
        }
        return windowManager;
    }

    @Override // org.icepdf.ri.common.WindowManagementCallback
    public PropertiesManager getProperties() {
        return this.properties;
    }

    public long getNumberOfWindows() {
        return this.newWindowInvocationCounter;
    }

    @Override // org.icepdf.ri.common.WindowManagementCallback
    public void newWindow(String location) throws HeadlessException, IllegalArgumentException {
        SwingController controller = commonWindowCreation();
        controller.openDocument(location);
    }

    public void newWindow(Document document, String fileName) throws HeadlessException, IllegalArgumentException {
        SwingController controller = commonWindowCreation();
        controller.openDocument(document, fileName);
    }

    @Override // org.icepdf.ri.common.WindowManagementCallback
    public void newWindow(URL location) throws HeadlessException, IllegalArgumentException {
        SwingController controller = commonWindowCreation();
        controller.openDocument(location);
    }

    protected SwingController commonWindowCreation() throws HeadlessException, IllegalArgumentException {
        SwingController controller = new SwingController(this.messageBundle);
        controller.setWindowManagementCallback(this);
        controller.setPropertiesManager(this.properties);
        controller.getDocumentViewController().setAnnotationCallback(new MyAnnotationCallback(controller.getDocumentViewController()));
        this.controllers.add(controller);
        int viewType = 1;
        int pageFit = 4;
        try {
            viewType = getProperties().getInt("document.viewtype", 1);
            pageFit = getProperties().getInt(PropertiesManager.PROPERTY_DEFAULT_PAGEFIT, 4);
        } catch (NumberFormatException e2) {
        }
        SwingViewBuilder factory = new SwingViewBuilder(controller, viewType, pageFit);
        JFrame frame = factory.buildViewerFrame();
        if (frame != null) {
            int width = getProperties().getInt("application.width", 800);
            int height = getProperties().getInt("application.height", 600);
            frame.setSize(width, height);
            int x2 = getProperties().getInt("application.x", 1);
            int y2 = getProperties().getInt("application.y", 1);
            frame.setLocation((int) (x2 + (this.newWindowInvocationCounter * 10)), (int) (y2 + (this.newWindowInvocationCounter * 10)));
            this.newWindowInvocationCounter++;
            frame.setVisible(true);
        }
        return controller;
    }

    @Override // org.icepdf.ri.common.WindowManagementCallback
    public void disposeWindow(SwingController controller, JFrame viewer, Properties properties) throws HeadlessException, IllegalArgumentException {
        if (this.controllers.size() <= 1) {
            quit(controller, viewer, properties);
            return;
        }
        int index = this.controllers.indexOf(controller);
        if (index >= 0) {
            this.controllers.remove(index);
            this.newWindowInvocationCounter--;
            if (viewer != null) {
                viewer.setVisible(false);
                viewer.dispose();
            }
        }
    }

    @Override // org.icepdf.ri.common.WindowManagementCallback
    public void quit(SwingController controller, JFrame viewer, Properties properties) throws HeadlessException, IllegalArgumentException {
        if (controller != null && viewer != null) {
            Rectangle sz = viewer.getBounds();
            getProperties().setInt("application.x", sz.f12372x);
            getProperties().setInt("application.y", sz.f12373y);
            getProperties().setInt("application.height", sz.height);
            getProperties().setInt("application.width", sz.width);
            if (properties != null) {
                getProperties().set(PropertiesManager.PROPERTY_DEFAULT_PAGEFIT, properties.getProperty(PropertiesManager.PROPERTY_DEFAULT_PAGEFIT));
                int viewType = Integer.parseInt(properties.getProperty("document.viewtype"));
                if (viewType != 7) {
                    getProperties().set("document.viewtype", properties.getProperty("document.viewtype"));
                }
            }
            getProperties().setDefaultFilePath(ViewModel.getDefaultFilePath());
            getProperties().setDefaultURL(ViewModel.getDefaultURL());
        }
        getProperties().saveAndEnd();
        Iterator i$ = this.controllers.iterator();
        while (i$.hasNext()) {
            SwingController c2 = i$.next();
            if (c2 != null) {
                c2.dispose();
            }
        }
        System.exit(0);
    }

    @Override // org.icepdf.ri.common.WindowManagementCallback
    public void minimiseAllWindows() {
        Iterator i$ = this.controllers.iterator();
        while (i$.hasNext()) {
            SwingController controller = i$.next();
            JFrame frame = controller.getViewerFrame();
            if (frame != null) {
                frame.setState(1);
            }
        }
    }

    @Override // org.icepdf.ri.common.WindowManagementCallback
    public void bringAllWindowsToFront(SwingController frontMost) {
        JFrame frontMostFrame = null;
        Iterator i$ = this.controllers.iterator();
        while (i$.hasNext()) {
            SwingController controller = i$.next();
            JFrame frame = controller.getViewerFrame();
            if (frame != null) {
                if (frontMost == controller) {
                    frontMostFrame = frame;
                } else {
                    frame.setState(0);
                    frame.toFront();
                }
            }
        }
        if (frontMostFrame != null) {
            frontMostFrame.setState(0);
            frontMostFrame.toFront();
        }
    }

    @Override // org.icepdf.ri.common.WindowManagementCallback
    public void bringWindowToFront(int index) {
        if (index >= 0 && index < this.controllers.size()) {
            SwingController controller = this.controllers.get(index);
            JFrame frame = controller.getViewerFrame();
            if (frame != null) {
                frame.setState(0);
                frame.toFront();
            }
        }
    }

    @Override // org.icepdf.ri.common.WindowManagementCallback
    public List getWindowDocumentOriginList(SwingController giveIndex) {
        Integer foundIndex = null;
        int count = this.controllers.size();
        List<Object> list = new ArrayList<>(count + 1);
        for (int i2 = 0; i2 < count; i2++) {
            Object toAdd = null;
            SwingController controller = this.controllers.get(i2);
            if (giveIndex == controller) {
                foundIndex = Integer.valueOf(i2);
            }
            Document document = controller.getDocument();
            if (document != null) {
                toAdd = document.getDocumentOrigin();
            }
            list.add(toAdd);
        }
        if (foundIndex != null) {
            list.add(foundIndex);
        }
        return list;
    }

    void updateUI() {
        Iterator i$ = this.controllers.iterator();
        while (i$.hasNext()) {
            SwingController controller = i$.next();
            JFrame frame = controller.getViewerFrame();
            if (frame != null) {
                SwingUtilities.updateComponentTreeUI(frame);
            }
        }
    }
}
