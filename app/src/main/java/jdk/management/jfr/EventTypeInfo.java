package jdk.management.jfr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;
import javax.management.openmbean.CompositeData;
import jdk.jfr.EventType;
import jdk.jfr.SettingDescriptor;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: jfr.jar:jdk/management/jfr/EventTypeInfo.class */
public final class EventTypeInfo {
    private final List<SettingDescriptorInfo> settings;
    private final long id;
    private final String name;
    private final String description;
    private final String label;
    private final List<String> categoryNames;

    EventTypeInfo(EventType eventType) {
        this.settings = creatingSettingDescriptorInfos(eventType);
        this.id = eventType.getId();
        this.name = eventType.getName();
        this.label = eventType.getLabel();
        this.description = eventType.getDescription();
        this.categoryNames = eventType.getCategoryNames();
    }

    private EventTypeInfo(CompositeData compositeData) {
        this.settings = createSettings(compositeData.get("settings"));
        this.id = ((Long) compositeData.get("id")).longValue();
        this.name = (String) compositeData.get("name");
        this.label = (String) compositeData.get("label");
        this.description = (String) compositeData.get("description");
        this.categoryNames = createCategoryNames((Object[]) compositeData.get("category"));
    }

    private static List<String> createCategoryNames(Object[] objArr) {
        ArrayList arrayList = new ArrayList(objArr.length);
        for (Object obj : objArr) {
            arrayList.add((String) obj);
        }
        return Collections.unmodifiableList(arrayList);
    }

    private static List<SettingDescriptorInfo> creatingSettingDescriptorInfos(EventType eventType) {
        List<SettingDescriptor> settingDescriptors = eventType.getSettingDescriptors();
        ArrayList arrayList = new ArrayList(settingDescriptors.size());
        Iterator<SettingDescriptor> it = settingDescriptors.iterator();
        while (it.hasNext()) {
            arrayList.add(new SettingDescriptorInfo(it.next()));
        }
        return Collections.unmodifiableList(arrayList);
    }

    private static List<SettingDescriptorInfo> createSettings(Object obj) {
        if (obj != null && obj.getClass().isArray()) {
            Object[] objArr = (Object[]) obj;
            ArrayList arrayList = new ArrayList(objArr.length);
            for (Object obj2 : objArr) {
                if (obj2 instanceof CompositeData) {
                    arrayList.add(SettingDescriptorInfo.from((CompositeData) obj2));
                }
            }
            return Collections.unmodifiableList(arrayList);
        }
        return Collections.emptyList();
    }

    public String getLabel() {
        return this.label;
    }

    public List<String> getCategoryNames() {
        return this.categoryNames;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public List<SettingDescriptorInfo> getSettingDescriptors() {
        return this.settings;
    }

    public String toString() {
        Stringifier stringifier = new Stringifier();
        stringifier.add("id", Long.valueOf(this.id));
        stringifier.add("name", this.name);
        stringifier.add("label", this.label);
        stringifier.add("description", this.description);
        StringJoiner stringJoiner = new StringJoiner(", ", VectorFormat.DEFAULT_PREFIX, "}");
        Iterator<String> it = this.categoryNames.iterator();
        while (it.hasNext()) {
            stringJoiner.add(it.next());
        }
        stringifier.add("category", stringJoiner.toString());
        return stringifier.toString();
    }

    public static EventTypeInfo from(CompositeData compositeData) {
        if (compositeData == null) {
            return null;
        }
        return new EventTypeInfo(compositeData);
    }
}
