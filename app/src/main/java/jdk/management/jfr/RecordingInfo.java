package jdk.management.jfr;

import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;
import jdk.jfr.Recording;
import jdk.jfr.internal.management.ManagementSupport;

/* loaded from: jfr.jar:jdk/management/jfr/RecordingInfo.class */
public final class RecordingInfo {
    private final long id;
    private final String name;
    private final String state;
    private final boolean dumpOnExit;
    private final long size;
    private final boolean disk;
    private final long maxAge;
    private final long maxSize;
    private final long startTime;
    private final long stopTime;
    private final String destination;
    private final long durationInSeconds;
    private final Map<String, String> settings;

    RecordingInfo(Recording recording) {
        this.id = recording.getId();
        this.name = recording.getName();
        this.state = recording.getState().toString();
        this.dumpOnExit = recording.getDumpOnExit();
        this.size = recording.getSize();
        this.disk = recording.isToDisk();
        Duration maxAge = recording.getMaxAge();
        if (maxAge == null) {
            this.maxAge = 0L;
        } else {
            this.maxAge = maxAge.getSeconds();
        }
        this.maxSize = recording.getMaxSize();
        Instant startTime = recording.getStartTime();
        this.startTime = startTime == null ? 0L : startTime.toEpochMilli();
        Instant stopTime = recording.getStopTime();
        this.stopTime = stopTime == null ? 0L : stopTime.toEpochMilli();
        this.destination = ManagementSupport.getDestinationOriginalText(recording);
        Duration duration = recording.getDuration();
        this.durationInSeconds = duration == null ? 0L : duration.getSeconds();
        this.settings = recording.getSettings();
    }

    private RecordingInfo(CompositeData compositeData) {
        this.id = ((Integer) compositeData.get("id")).intValue();
        this.name = (String) compositeData.get("name");
        this.state = (String) compositeData.get("state");
        this.dumpOnExit = ((Boolean) compositeData.get("dumpOnExit")).booleanValue();
        this.size = ((Long) compositeData.get("size")).longValue();
        this.disk = ((Boolean) compositeData.get("disk")).booleanValue();
        this.maxAge = ((Long) compositeData.get("maxAge")).longValue();
        this.maxSize = ((Long) compositeData.get("maxSize")).longValue();
        this.startTime = ((Long) compositeData.get("startTime")).longValue();
        this.stopTime = ((Long) compositeData.get("stopTime")).longValue();
        this.destination = (String) compositeData.get("destination");
        this.durationInSeconds = ((Long) compositeData.get("duration")).longValue();
        this.settings = new LinkedHashMap();
        Object obj = compositeData.get("settings");
        if (obj instanceof TabularData) {
            TabularData tabularData = (TabularData) obj;
            List<String> indexNames = tabularData.getTabularType().getIndexNames();
            int size = indexNames.size();
            Iterator<?> it = tabularData.keySet().iterator();
            while (it.hasNext()) {
                Object[] array = ((List) it.next()).toArray();
                for (int i2 = 0; i2 < size; i2++) {
                    String str = indexNames.get(i2);
                    Object obj2 = array[i2];
                    if (obj2 instanceof String) {
                        this.settings.put(str, (String) obj2);
                    }
                }
            }
        }
    }

    public String getName() {
        return this.name;
    }

    public long getId() {
        return this.id;
    }

    public boolean getDumpOnExit() {
        return this.dumpOnExit;
    }

    public long getMaxAge() {
        return this.maxAge;
    }

    public long getMaxSize() {
        return this.maxSize;
    }

    public String getState() {
        return this.state;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public long getStopTime() {
        return this.stopTime;
    }

    public Map<String, String> getSettings() {
        return this.settings;
    }

    public String getDestination() {
        return this.destination;
    }

    public String toString() {
        Stringifier stringifier = new Stringifier();
        stringifier.add("name", this.name);
        stringifier.add("id", Long.valueOf(this.id));
        stringifier.add("maxAge", Long.valueOf(this.maxAge));
        stringifier.add("maxSize", Long.valueOf(this.maxSize));
        return stringifier.toString();
    }

    public long getSize() {
        return this.size;
    }

    public boolean isToDisk() {
        return this.disk;
    }

    public long getDuration() {
        return this.durationInSeconds;
    }

    public static RecordingInfo from(CompositeData compositeData) {
        if (compositeData == null) {
            return null;
        }
        return new RecordingInfo(compositeData);
    }
}
