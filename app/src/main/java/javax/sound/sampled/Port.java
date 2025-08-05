package javax.sound.sampled;

import javax.sound.sampled.Line;

/* loaded from: rt.jar:javax/sound/sampled/Port.class */
public interface Port extends Line {

    /* loaded from: rt.jar:javax/sound/sampled/Port$Info.class */
    public static class Info extends Line.Info {
        public static final Info MICROPHONE = new Info(Port.class, "MICROPHONE", true);
        public static final Info LINE_IN = new Info(Port.class, "LINE_IN", true);
        public static final Info COMPACT_DISC = new Info(Port.class, "COMPACT_DISC", true);
        public static final Info SPEAKER = new Info(Port.class, "SPEAKER", false);
        public static final Info HEADPHONE = new Info(Port.class, "HEADPHONE", false);
        public static final Info LINE_OUT = new Info(Port.class, "LINE_OUT", false);
        private String name;
        private boolean isSource;

        public Info(Class<?> cls, String str, boolean z2) {
            super(cls);
            this.name = str;
            this.isSource = z2;
        }

        public String getName() {
            return this.name;
        }

        public boolean isSource() {
            return this.isSource;
        }

        @Override // javax.sound.sampled.Line.Info
        public boolean matches(Line.Info info) {
            if (!super.matches(info) || !this.name.equals(((Info) info).getName()) || this.isSource != ((Info) info).isSource()) {
                return false;
            }
            return true;
        }

        public final boolean equals(Object obj) {
            return super.equals(obj);
        }

        public final int hashCode() {
            return super.hashCode();
        }

        @Override // javax.sound.sampled.Line.Info
        public final String toString() {
            return this.name + (this.isSource ? " source" : " target") + " port";
        }
    }
}
