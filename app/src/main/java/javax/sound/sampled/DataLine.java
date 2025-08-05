package javax.sound.sampled;

import java.util.Arrays;
import javax.sound.sampled.Line;

/* loaded from: rt.jar:javax/sound/sampled/DataLine.class */
public interface DataLine extends Line {
    void drain();

    void flush();

    void start();

    void stop();

    boolean isRunning();

    boolean isActive();

    AudioFormat getFormat();

    int getBufferSize();

    int available();

    int getFramePosition();

    long getLongFramePosition();

    long getMicrosecondPosition();

    float getLevel();

    /* loaded from: rt.jar:javax/sound/sampled/DataLine$Info.class */
    public static class Info extends Line.Info {
        private final AudioFormat[] formats;
        private final int minBufferSize;
        private final int maxBufferSize;

        public Info(Class<?> cls, AudioFormat[] audioFormatArr, int i2, int i3) {
            super(cls);
            if (audioFormatArr == null) {
                this.formats = new AudioFormat[0];
            } else {
                this.formats = (AudioFormat[]) Arrays.copyOf(audioFormatArr, audioFormatArr.length);
            }
            this.minBufferSize = i2;
            this.maxBufferSize = i3;
        }

        public Info(Class<?> cls, AudioFormat audioFormat, int i2) {
            super(cls);
            if (audioFormat == null) {
                this.formats = new AudioFormat[0];
            } else {
                this.formats = new AudioFormat[]{audioFormat};
            }
            this.minBufferSize = i2;
            this.maxBufferSize = i2;
        }

        public Info(Class<?> cls, AudioFormat audioFormat) {
            this(cls, audioFormat, -1);
        }

        public AudioFormat[] getFormats() {
            return (AudioFormat[]) Arrays.copyOf(this.formats, this.formats.length);
        }

        public boolean isFormatSupported(AudioFormat audioFormat) {
            for (int i2 = 0; i2 < this.formats.length; i2++) {
                if (audioFormat.matches(this.formats[i2])) {
                    return true;
                }
            }
            return false;
        }

        public int getMinBufferSize() {
            return this.minBufferSize;
        }

        public int getMaxBufferSize() {
            return this.maxBufferSize;
        }

        @Override // javax.sound.sampled.Line.Info
        public boolean matches(Line.Info info) {
            if (!super.matches(info)) {
                return false;
            }
            Info info2 = (Info) info;
            if (getMaxBufferSize() >= 0 && info2.getMaxBufferSize() >= 0 && getMaxBufferSize() > info2.getMaxBufferSize()) {
                return false;
            }
            if (getMinBufferSize() >= 0 && info2.getMinBufferSize() >= 0 && getMinBufferSize() < info2.getMinBufferSize()) {
                return false;
            }
            AudioFormat[] formats = getFormats();
            if (formats != null) {
                for (int i2 = 0; i2 < formats.length; i2++) {
                    if (formats[i2] != null && !info2.isFormatSupported(formats[i2])) {
                        return false;
                    }
                }
                return true;
            }
            return true;
        }

        @Override // javax.sound.sampled.Line.Info
        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            if (this.formats.length == 1 && this.formats[0] != null) {
                stringBuffer.append(" supporting format " + ((Object) this.formats[0]));
            } else if (getFormats().length > 1) {
                stringBuffer.append(" supporting " + getFormats().length + " audio formats");
            }
            if (this.minBufferSize != -1 && this.maxBufferSize != -1) {
                stringBuffer.append(", and buffers of " + this.minBufferSize + " to " + this.maxBufferSize + " bytes");
            } else if (this.minBufferSize != -1 && this.minBufferSize > 0) {
                stringBuffer.append(", and buffers of at least " + this.minBufferSize + " bytes");
            } else if (this.maxBufferSize != -1) {
                stringBuffer.append(", and buffers of up to " + this.minBufferSize + " bytes");
            }
            return new String(super.toString() + ((Object) stringBuffer));
        }
    }
}
