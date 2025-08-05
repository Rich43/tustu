package javax.sound.sampled;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* loaded from: rt.jar:javax/sound/sampled/AudioFormat.class */
public class AudioFormat {
    protected Encoding encoding;
    protected float sampleRate;
    protected int sampleSizeInBits;
    protected int channels;
    protected int frameSize;
    protected float frameRate;
    protected boolean bigEndian;
    private HashMap<String, Object> properties;

    public AudioFormat(Encoding encoding, float f2, int i2, int i3, int i4, float f3, boolean z2) {
        this.encoding = encoding;
        this.sampleRate = f2;
        this.sampleSizeInBits = i2;
        this.channels = i3;
        this.frameSize = i4;
        this.frameRate = f3;
        this.bigEndian = z2;
        this.properties = null;
    }

    public AudioFormat(Encoding encoding, float f2, int i2, int i3, int i4, float f3, boolean z2, Map<String, Object> map) {
        this(encoding, f2, i2, i3, i4, f3, z2);
        this.properties = new HashMap<>(map);
    }

    public AudioFormat(float f2, int i2, int i3, boolean z2, boolean z3) {
        this(z2 ? Encoding.PCM_SIGNED : Encoding.PCM_UNSIGNED, f2, i2, i3, (i3 == -1 || i2 == -1) ? -1 : ((i2 + 7) / 8) * i3, f2, z3);
    }

    public Encoding getEncoding() {
        return this.encoding;
    }

    public float getSampleRate() {
        return this.sampleRate;
    }

    public int getSampleSizeInBits() {
        return this.sampleSizeInBits;
    }

    public int getChannels() {
        return this.channels;
    }

    public int getFrameSize() {
        return this.frameSize;
    }

    public float getFrameRate() {
        return this.frameRate;
    }

    public boolean isBigEndian() {
        return this.bigEndian;
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

    public boolean matches(AudioFormat audioFormat) {
        if (audioFormat.getEncoding().equals(getEncoding())) {
            if (audioFormat.getChannels() != -1 && audioFormat.getChannels() != getChannels()) {
                return false;
            }
            if (audioFormat.getSampleRate() == -1.0f || audioFormat.getSampleRate() == getSampleRate()) {
                if (audioFormat.getSampleSizeInBits() != -1 && audioFormat.getSampleSizeInBits() != getSampleSizeInBits()) {
                    return false;
                }
                if (audioFormat.getFrameRate() == -1.0f || audioFormat.getFrameRate() == getFrameRate()) {
                    if (audioFormat.getFrameSize() == -1 || audioFormat.getFrameSize() == getFrameSize()) {
                        if (getSampleSizeInBits() <= 8 || audioFormat.isBigEndian() == isBigEndian()) {
                            return true;
                        }
                        return false;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public String toString() {
        String str;
        String str2;
        String str3;
        String str4;
        String str5 = "";
        if (getEncoding() != null) {
            str5 = getEncoding().toString() + " ";
        }
        if (getSampleRate() == -1.0f) {
            str = "unknown sample rate, ";
        } else {
            str = "" + getSampleRate() + " Hz, ";
        }
        if (getSampleSizeInBits() == -1.0f) {
            str2 = "unknown bits per sample, ";
        } else {
            str2 = "" + getSampleSizeInBits() + " bit, ";
        }
        if (getChannels() == 1) {
            str3 = "mono, ";
        } else if (getChannels() == 2) {
            str3 = "stereo, ";
        } else if (getChannels() == -1) {
            str3 = " unknown number of channels, ";
        } else {
            str3 = "" + getChannels() + " channels, ";
        }
        if (getFrameSize() == -1.0f) {
            str4 = "unknown frame size, ";
        } else {
            str4 = "" + getFrameSize() + " bytes/frame, ";
        }
        String str6 = "";
        if (Math.abs(getSampleRate() - getFrameRate()) > 1.0E-5d) {
            if (getFrameRate() == -1.0f) {
                str6 = "unknown frame rate, ";
            } else {
                str6 = getFrameRate() + " frames/second, ";
            }
        }
        String str7 = "";
        if ((getEncoding().equals(Encoding.PCM_SIGNED) || getEncoding().equals(Encoding.PCM_UNSIGNED)) && (getSampleSizeInBits() > 8 || getSampleSizeInBits() == -1)) {
            str7 = isBigEndian() ? "big-endian" : "little-endian";
        }
        return str5 + str + str2 + str3 + str4 + str6 + str7;
    }

    /* loaded from: rt.jar:javax/sound/sampled/AudioFormat$Encoding.class */
    public static class Encoding {
        public static final Encoding PCM_SIGNED = new Encoding("PCM_SIGNED");
        public static final Encoding PCM_UNSIGNED = new Encoding("PCM_UNSIGNED");
        public static final Encoding PCM_FLOAT = new Encoding("PCM_FLOAT");
        public static final Encoding ULAW = new Encoding("ULAW");
        public static final Encoding ALAW = new Encoding("ALAW");
        private String name;

        public Encoding(String str) {
            this.name = str;
        }

        public final boolean equals(Object obj) {
            if (toString() == null) {
                return obj != null && obj.toString() == null;
            }
            if (obj instanceof Encoding) {
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
    }
}
