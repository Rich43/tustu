package javax.sound.sampled;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* loaded from: rt.jar:javax/sound/sampled/AudioFileFormat.class */
public class AudioFileFormat {
    private Type type;
    private int byteLength;
    private AudioFormat format;
    private int frameLength;
    private HashMap<String, Object> properties;

    protected AudioFileFormat(Type type, int i2, AudioFormat audioFormat, int i3) {
        this.type = type;
        this.byteLength = i2;
        this.format = audioFormat;
        this.frameLength = i3;
        this.properties = null;
    }

    public AudioFileFormat(Type type, AudioFormat audioFormat, int i2) {
        this(type, -1, audioFormat, i2);
    }

    public AudioFileFormat(Type type, AudioFormat audioFormat, int i2, Map<String, Object> map) {
        this(type, -1, audioFormat, i2);
        this.properties = new HashMap<>(map);
    }

    public Type getType() {
        return this.type;
    }

    public int getByteLength() {
        return this.byteLength;
    }

    public AudioFormat getFormat() {
        return this.format;
    }

    public int getFrameLength() {
        return this.frameLength;
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

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        if (this.type != null) {
            stringBuffer.append(this.type.toString() + " (." + this.type.getExtension() + ") file");
        } else {
            stringBuffer.append("unknown file format");
        }
        if (this.byteLength != -1) {
            stringBuffer.append(", byte length: " + this.byteLength);
        }
        stringBuffer.append(", data format: " + ((Object) this.format));
        if (this.frameLength != -1) {
            stringBuffer.append(", frame length: " + this.frameLength);
        }
        return new String(stringBuffer);
    }

    /* loaded from: rt.jar:javax/sound/sampled/AudioFileFormat$Type.class */
    public static class Type {
        public static final Type WAVE = new Type("WAVE", "wav");
        public static final Type AU = new Type("AU", "au");
        public static final Type AIFF = new Type("AIFF", "aif");
        public static final Type AIFC = new Type("AIFF-C", "aifc");
        public static final Type SND = new Type("SND", "snd");
        private final String name;
        private final String extension;

        public Type(String str, String str2) {
            this.name = str;
            this.extension = str2;
        }

        public final boolean equals(Object obj) {
            if (toString() == null) {
                return obj != null && obj.toString() == null;
            }
            if (obj instanceof Type) {
                return toString().equals(obj.toString());
            }
            return false;
        }

        public final int hashCode() {
            if (toString() == null) {
                return 0;
            }
            return toString().hashCode();
        }

        public final String toString() {
            return this.name;
        }

        public String getExtension() {
            return this.extension;
        }
    }
}
