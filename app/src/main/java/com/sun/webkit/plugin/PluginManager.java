package com.sun.webkit.plugin;

import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.TreeMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: jfxrt.jar:com/sun/webkit/plugin/PluginManager.class */
public final class PluginManager {
    private static PluginHandler[] hndArray;
    private static final Logger log = Logger.getLogger("com.sun.browser.plugin.PluginManager");
    private static final ServiceLoader<PluginHandler> pHandlers = ServiceLoader.load(PluginHandler.class);
    private static final TreeMap<String, PluginHandler> hndMap = new TreeMap<>();
    private static final HashSet<String> disabledPluginHandlers = new HashSet<>();

    static {
        if ("false".equalsIgnoreCase(System.getProperty("com.sun.browser.plugin"))) {
            for (PluginHandler hnd : getAvailablePlugins()) {
                disabledPluginHandlers.add(hnd.getClass().getCanonicalName());
            }
        }
        updatePluginHandlers();
    }

    private static void updatePluginHandlers() {
        log.fine("Update plugin handlers");
        hndMap.clear();
        Iterator<PluginHandler> iter = pHandlers.iterator();
        while (iter.hasNext()) {
            PluginHandler hnd = iter.next();
            if (hnd.isSupportedPlatform() && !isDisabledPlugin(hnd)) {
                String[] types = hnd.supportedMIMETypes();
                for (String type : types) {
                    hndMap.put(type, hnd);
                    log.fine(type);
                }
            }
        }
        Collection<PluginHandler> vals = hndMap.values();
        hndArray = (PluginHandler[]) vals.toArray(new PluginHandler[vals.size()]);
    }

    public static Plugin createPlugin(URL url, String type, String[] pNames, String[] pValues) {
        try {
            PluginHandler hnd = hndMap.get(type);
            if (hnd == null) {
                return new DefaultPlugin(url, type, pNames, pValues);
            }
            Plugin p2 = hnd.createPlugin(url, type, pNames, pValues);
            if (p2 == null) {
                return new DefaultPlugin(url, type, pNames, pValues);
            }
            return p2;
        } catch (Throwable ex) {
            log.log(Level.FINE, "Cannot create plugin", ex);
            return new DefaultPlugin(url, type, pNames, pValues);
        }
    }

    private static List<PluginHandler> getAvailablePlugins() {
        Vector<PluginHandler> res = new Vector<>();
        Iterator<PluginHandler> iter = pHandlers.iterator();
        while (iter.hasNext()) {
            PluginHandler hnd = iter.next();
            if (hnd.isSupportedPlatform()) {
                res.add(hnd);
            }
        }
        return res;
    }

    private static PluginHandler getEnabledPlugin(int i2) {
        if (i2 < 0 || i2 >= hndArray.length) {
            return null;
        }
        return hndArray[i2];
    }

    private static int getEnabledPluginCount() {
        return hndArray.length;
    }

    private static void disablePlugin(PluginHandler hnd) {
        disabledPluginHandlers.add(hnd.getClass().getCanonicalName());
        updatePluginHandlers();
    }

    private static void enablePlugin(PluginHandler hnd) {
        disabledPluginHandlers.remove(hnd.getClass().getCanonicalName());
        updatePluginHandlers();
    }

    private static boolean isDisabledPlugin(PluginHandler hnd) {
        return disabledPluginHandlers.contains(hnd.getClass().getCanonicalName());
    }

    private static boolean supportsMIMEType(String mimeType) {
        return hndMap.containsKey(mimeType);
    }

    private static String getPluginNameForMIMEType(String mimeType) {
        PluginHandler hnd = hndMap.get(mimeType);
        return hnd != null ? hnd.getName() : "";
    }
}
