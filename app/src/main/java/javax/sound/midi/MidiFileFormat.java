package javax.sound.midi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* loaded from: rt.jar:javax/sound/midi/MidiFileFormat.class */
public class MidiFileFormat {
    public static final int UNKNOWN_LENGTH = -1;
    protected int type;
    protected float divisionType;
    protected int resolution;
    protected int byteLength;
    protected long microsecondLength;
    private HashMap<String, Object> properties;

    public MidiFileFormat(int i2, float f2, int i3, int i4, long j2) {
        this.type = i2;
        this.divisionType = f2;
        this.resolution = i3;
        this.byteLength = i4;
        this.microsecondLength = j2;
        this.properties = null;
    }

    public MidiFileFormat(int i2, float f2, int i3, int i4, long j2, Map<String, Object> map) {
        this(i2, f2, i3, i4, j2);
        this.properties = new HashMap<>(map);
    }

    public int getType() {
        return this.type;
    }

    public float getDivisionType() {
        return this.divisionType;
    }

    public int getResolution() {
        return this.resolution;
    }

    public int getByteLength() {
        return this.byteLength;
    }

    public long getMicrosecondLength() {
        return this.microsecondLength;
    }

    public Map<String, Object> properties() {
        Map map;
        if (this.properties == null) {
            map = new HashMap(0);
        } else {
            map = (Map) this.properties.clone();
        }
        return Collections.unmodifiableMap(map);
    }

    public Object getProperty(String str) {
        if (this.properties == null) {
            return null;
        }
        return this.properties.get(str);
    }
}
