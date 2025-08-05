package jdk.jfr.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import javafx.fxml.FXMLLoader;
import jdk.jfr.Enabled;
import jdk.jfr.Event;
import jdk.jfr.internal.handlers.EventHandler;
import org.apache.commons.math3.geometry.VectorFormat;
import org.icepdf.core.util.PdfOps;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: jfr.jar:jdk/jfr/internal/SettingsManager.class */
final class SettingsManager {
    private Map<String, InternalSetting> availableSettings = new LinkedHashMap();

    SettingsManager() {
    }

    /* loaded from: jfr.jar:jdk/jfr/internal/SettingsManager$InternalSetting.class */
    private static class InternalSetting {
        private final String identifier;
        private Map<String, Set<String>> enabledMap = new LinkedHashMap(5);
        private Map<String, Set<String>> allMap = new LinkedHashMap(5);
        private boolean enabled;

        public InternalSetting(String str) {
            this.identifier = str;
        }

        public Set<String> getValues(String str) {
            if (this.enabled) {
                return this.enabledMap.get(str);
            }
            return this.allMap.get(str);
        }

        public void add(String str, String str2) {
            if (Enabled.NAME.equals(str) && "true".equals(str2)) {
                this.enabled = true;
                this.allMap = null;
            }
            addToMap(this.enabledMap, str, str2);
            if (this.allMap != null) {
                addToMap(this.allMap, str, str2);
            }
        }

        private void addToMap(Map<String, Set<String>> map, String str, String str2) {
            Set<String> hashSet = map.get(str);
            if (hashSet == null) {
                hashSet = new HashSet(5);
                map.put(str, hashSet);
            }
            hashSet.add(str2);
        }

        public String getSettingsId() {
            return this.identifier;
        }

        public void add(InternalSetting internalSetting) {
            for (Map.Entry<String, Set<String>> entry : internalSetting.enabledMap.entrySet()) {
                Iterator<String> it = entry.getValue().iterator();
                while (it.hasNext()) {
                    add(entry.getKey(), it.next());
                }
            }
        }

        public boolean isEnabled() {
            return this.enabled;
        }

        public String toString() {
            return this.identifier + ": " + this.enabledMap.toString();
        }

        public void finish() {
            if (!this.enabled) {
                HashMap map = new HashMap(2);
                HashSet hashSet = new HashSet(2);
                hashSet.add("false");
                map.put(Enabled.NAME, hashSet);
                this.enabledMap = map;
            }
        }
    }

    void setSettings(List<Map<String, String>> list) {
        this.availableSettings = createSettingsMap(list);
        List<EventControl> eventControls = MetadataRepository.getInstance().getEventControls();
        if (!JVM.getJVM().isRecording()) {
            Iterator<EventControl> it = eventControls.iterator();
            while (it.hasNext()) {
                it.next().disable();
            }
        } else {
            if (Logger.shouldLog(LogTag.JFR_SETTING, LogLevel.INFO)) {
                Collections.sort(eventControls, (eventControl, eventControl2) -> {
                    return eventControl.getEventType().getName().compareTo(eventControl2.getEventType().getName());
                });
            }
            Iterator<EventControl> it2 = eventControls.iterator();
            while (it2.hasNext()) {
                setEventControl(it2.next());
            }
        }
        if (JVM.getJVM().getAllowedToDoEventRetransforms()) {
            updateRetransform(JVM.getJVM().getAllEventClasses());
        }
    }

    public void updateRetransform(List<Class<? extends Event>> list) {
        ArrayList arrayList = new ArrayList();
        for (Class<? extends Event> cls : list) {
            EventHandler handler = Utils.getHandler(cls);
            if (handler != null) {
                PlatformEventType platformEventType = handler.getPlatformEventType();
                if (platformEventType.isMarkedForInstrumentation()) {
                    arrayList.add(cls);
                    platformEventType.markForInstrumentation(false);
                    platformEventType.setInstrumented();
                }
            }
        }
        if (!arrayList.isEmpty()) {
            JVM.getJVM().retransformClasses((Class[]) arrayList.toArray(new Class[0]));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private Map<String, InternalSetting> createSettingsMap(List<Map<String, String>> list) {
        LinkedHashMap linkedHashMap = new LinkedHashMap(list.size());
        Iterator<Map<String, String>> it = list.iterator();
        while (it.hasNext()) {
            for (InternalSetting internalSetting : makeInternalSettings(it.next())) {
                InternalSetting internalSetting2 = (InternalSetting) linkedHashMap.get(internalSetting.getSettingsId());
                if (internalSetting2 == null) {
                    linkedHashMap.put(internalSetting.getSettingsId(), internalSetting);
                } else {
                    internalSetting2.add(internalSetting);
                }
            }
        }
        return linkedHashMap;
    }

    private Collection<InternalSetting> makeInternalSettings(Map<String, String> map) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            int iIndexOf = key.indexOf(FXMLLoader.CONTROLLER_METHOD_PREFIX);
            if (iIndexOf > 1 && iIndexOf < key.length() - 2) {
                String strUpgradeLegacyJDKEvent = Utils.upgradeLegacyJDKEvent(key.substring(0, iIndexOf));
                InternalSetting internalSetting = (InternalSetting) linkedHashMap.get(strUpgradeLegacyJDKEvent);
                String strTrim = key.substring(iIndexOf + 1).trim();
                if (internalSetting == null) {
                    internalSetting = new InternalSetting(strUpgradeLegacyJDKEvent);
                    linkedHashMap.put(strUpgradeLegacyJDKEvent, internalSetting);
                }
                internalSetting.add(strTrim, value);
            }
        }
        Iterator it = linkedHashMap.values().iterator();
        while (it.hasNext()) {
            ((InternalSetting) it.next()).finish();
        }
        return linkedHashMap.values();
    }

    void setEventControl(EventControl eventControl) {
        InternalSetting internalSetting = getInternalSetting(eventControl);
        Logger.log(LogTag.JFR_SETTING, LogLevel.INFO, "Applied settings for " + eventControl.getEventType().getLogName() + " {");
        for (Map.Entry<String, Control> entry : eventControl.getEntries()) {
            Set<String> values = null;
            String key = entry.getKey();
            if (internalSetting != null) {
                values = internalSetting.getValues(key);
            }
            Control value = entry.getValue();
            if (values != null) {
                value.apply(values);
                String lastValue = value.getLastValue();
                if (Logger.shouldLog(LogTag.JFR_SETTING, LogLevel.INFO) && Utils.isSettingVisible(value, eventControl.getEventType().hasEventHook())) {
                    if (values.size() > 1) {
                        StringJoiner stringJoiner = new StringJoiner(", ", VectorFormat.DEFAULT_PREFIX, "}");
                        Iterator<String> it = values.iterator();
                        while (it.hasNext()) {
                            stringJoiner.add(PdfOps.DOUBLE_QUOTE__TOKEN + it.next() + PdfOps.DOUBLE_QUOTE__TOKEN);
                        }
                        Logger.log(LogTag.JFR_SETTING, LogLevel.INFO, Constants.INDENT + key + "= " + stringJoiner.toString() + " => \"" + lastValue + PdfOps.DOUBLE_QUOTE__TOKEN);
                    } else {
                        Logger.log(LogTag.JFR_SETTING, LogLevel.INFO, Constants.INDENT + key + "=\"" + value.getLastValue() + PdfOps.DOUBLE_QUOTE__TOKEN);
                    }
                }
            } else {
                value.setDefault();
                if (Logger.shouldLog(LogTag.JFR_SETTING, LogLevel.INFO)) {
                    Logger.log(LogTag.JFR_SETTING, LogLevel.INFO, Constants.INDENT + key + "=\"" + value.getLastValue() + PdfOps.DOUBLE_QUOTE__TOKEN);
                }
            }
        }
        eventControl.writeActiveSettingEvent();
        Logger.log(LogTag.JFR_SETTING, LogLevel.INFO, "}");
    }

    private InternalSetting getInternalSetting(EventControl eventControl) {
        InternalSetting internalSetting = this.availableSettings.get(eventControl.getEventType().getName());
        InternalSetting internalSetting2 = this.availableSettings.get(eventControl.getSettingsId());
        if (internalSetting == null && internalSetting2 == null) {
            return null;
        }
        if (internalSetting2 == null) {
            return internalSetting;
        }
        if (internalSetting == null) {
            return internalSetting2;
        }
        InternalSetting internalSetting3 = new InternalSetting(internalSetting.getSettingsId());
        internalSetting3.add(internalSetting);
        internalSetting3.add(internalSetting2);
        return internalSetting3;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<InternalSetting> it = this.availableSettings.values().iterator();
        while (it.hasNext()) {
            sb.append(it.next().toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    boolean isEnabled(String str) {
        InternalSetting internalSetting = this.availableSettings.get(str);
        if (internalSetting == null) {
            return false;
        }
        return internalSetting.isEnabled();
    }
}
