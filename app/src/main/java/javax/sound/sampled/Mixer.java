package javax.sound.sampled;

import javax.sound.sampled.Line;

/* loaded from: rt.jar:javax/sound/sampled/Mixer.class */
public interface Mixer extends Line {
    Info getMixerInfo();

    Line.Info[] getSourceLineInfo();

    Line.Info[] getTargetLineInfo();

    Line.Info[] getSourceLineInfo(Line.Info info);

    Line.Info[] getTargetLineInfo(Line.Info info);

    boolean isLineSupported(Line.Info info);

    Line getLine(Line.Info info) throws LineUnavailableException;

    int getMaxLines(Line.Info info);

    Line[] getSourceLines();

    Line[] getTargetLines();

    void synchronize(Line[] lineArr, boolean z2);

    void unsynchronize(Line[] lineArr);

    boolean isSynchronizationSupported(Line[] lineArr, boolean z2);

    /* loaded from: rt.jar:javax/sound/sampled/Mixer$Info.class */
    public static class Info {
        private final String name;
        private final String vendor;
        private final String description;
        private final String version;

        protected Info(String str, String str2, String str3, String str4) {
            this.name = str;
            this.vendor = str2;
            this.description = str3;
            this.version = str4;
        }

        public final boolean equals(Object obj) {
            return super.equals(obj);
        }

        public final int hashCode() {
            return super.hashCode();
        }

        public final String getName() {
            return this.name;
        }

        public final String getVendor() {
            return this.vendor;
        }

        public final String getDescription() {
            return this.description;
        }

        public final String getVersion() {
            return this.version;
        }

        public final String toString() {
            return this.name + ", version " + this.version;
        }
    }
}
