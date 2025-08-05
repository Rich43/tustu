package com.sun.scenario;

import com.sun.javafx.tk.Toolkit;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import javafx.util.Callback;

/* loaded from: jfxrt.jar:com/sun/scenario/Settings.class */
public class Settings {
    private final Map<String, String> settings = new HashMap(5);
    private final CopyOnWriteArrayList<Callback<String, Void>> listeners = new CopyOnWriteArrayList<>();
    private static final Object SETTINGS_KEY = new StringBuilder("SettingsKey");

    private static synchronized Settings getInstance() {
        Map<Object, Object> contextMap = Toolkit.getToolkit().getContextMap();
        Settings instance = (Settings) contextMap.get(SETTINGS_KEY);
        if (instance == null) {
            instance = new Settings();
            contextMap.put(SETTINGS_KEY, instance);
        }
        return instance;
    }

    public static void set(String key, String value) {
        getInstance().setImpl(key, value);
    }

    private void setImpl(String key, String value) {
        checkKeyArg(key);
        this.settings.put(key, value);
        Iterator<Callback<String, Void>> it = this.listeners.iterator();
        while (it.hasNext()) {
            Callback<String, Void> l2 = it.next();
            l2.call(key);
        }
    }

    public static String get(String key) {
        return getInstance().getImpl(key);
    }

    private String getImpl(String key) {
        checkKeyArg(key);
        String retVal = this.settings.get(key);
        if (retVal == null) {
            try {
                retVal = System.getProperty(key);
            } catch (SecurityException e2) {
            }
        }
        return retVal;
    }

    public static boolean getBoolean(String key) {
        return getInstance().getBooleanImpl(key);
    }

    private boolean getBooleanImpl(String key) {
        String value = getImpl(key);
        return "true".equals(value);
    }

    public static boolean getBoolean(String key, boolean defaultVal) {
        return getInstance().getBooleanImpl(key, defaultVal);
    }

    private boolean getBooleanImpl(String key, boolean defaultVal) {
        String value = getImpl(key);
        boolean retVal = defaultVal;
        if (value != null) {
            if ("false".equals(value)) {
                retVal = false;
            } else if ("true".equals(value)) {
                retVal = true;
            }
        }
        return retVal;
    }

    public static int getInt(String key, int defaultVal) {
        return getInstance().getIntImpl(key, defaultVal);
    }

    private int getIntImpl(String key, int defaultVal) {
        String value = getImpl(key);
        int retVal = defaultVal;
        try {
            retVal = Integer.parseInt(value);
        } catch (NumberFormatException e2) {
        }
        return retVal;
    }

    public static void addPropertyChangeListener(Callback<String, Void> pcl) {
        getInstance().addPropertyChangeListenerImpl(pcl);
    }

    private void addPropertyChangeListenerImpl(Callback<String, Void> pcl) {
        this.listeners.add(pcl);
    }

    public static void removePropertyChangeListener(Callback<String, Void> pcl) {
        getInstance().removePropertyChangeListenerImpl(pcl);
    }

    private void removePropertyChangeListenerImpl(Callback<String, Void> pcl) {
        this.listeners.remove(pcl);
    }

    private void checkKeyArg(String key) {
        if (null == key || "".equals(key)) {
            throw new IllegalArgumentException("null key not allowed");
        }
    }

    private Settings() {
    }
}
