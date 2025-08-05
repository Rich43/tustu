package jdk.management.jfr;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;
import jdk.jfr.Configuration;

/* loaded from: jfr.jar:jdk/management/jfr/ConfigurationInfo.class */
public final class ConfigurationInfo {
    private final Map<String, String> settings;
    private final String name;
    private final String label;
    private final String description;
    private final String provider;
    private final String contents;

    ConfigurationInfo(Configuration configuration) {
        this.settings = configuration.getSettings();
        this.name = configuration.getName();
        this.label = configuration.getLabel();
        this.description = configuration.getDescription();
        this.provider = configuration.getProvider();
        this.contents = configuration.getContents();
    }

    private ConfigurationInfo(CompositeData compositeData) {
        this.settings = createMap(compositeData.get("settings"));
        this.name = (String) compositeData.get("name");
        this.label = (String) compositeData.get("label");
        this.description = (String) compositeData.get("description");
        this.provider = (String) compositeData.get("provider");
        this.contents = (String) compositeData.get(Constants.ELEMNAME_CONTENTS_STRING);
    }

    private static Map<String, String> createMap(Object obj) {
        if (obj instanceof TabularData) {
            TabularData tabularData = (TabularData) obj;
            HashMap map = new HashMap(tabularData.values().size());
            for (Object obj2 : tabularData.values()) {
                if (obj2 instanceof CompositeData) {
                    CompositeData compositeData = (CompositeData) obj2;
                    Object obj3 = compositeData.get("key");
                    Object obj4 = compositeData.get("value");
                    if ((obj3 instanceof String) && (obj4 instanceof String)) {
                        map.put((String) obj3, (String) obj4);
                    }
                }
            }
            return Collections.unmodifiableMap(map);
        }
        return Collections.emptyMap();
    }

    public String getProvider() {
        return this.provider;
    }

    public String getContents() {
        return this.contents;
    }

    public Map<String, String> getSettings() {
        return this.settings;
    }

    public String getLabel() {
        return this.label;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public static ConfigurationInfo from(CompositeData compositeData) {
        if (compositeData == null) {
            return null;
        }
        return new ConfigurationInfo(compositeData);
    }

    public String toString() {
        Stringifier stringifier = new Stringifier();
        stringifier.add("name", this.name);
        stringifier.add("label", this.label);
        stringifier.add("description", this.description);
        stringifier.add("provider", this.provider);
        return stringifier.toString();
    }
}
