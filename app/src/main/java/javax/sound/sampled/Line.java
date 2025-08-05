package javax.sound.sampled;

import javax.sound.sampled.Control;

/* loaded from: rt.jar:javax/sound/sampled/Line.class */
public interface Line extends AutoCloseable {
    Info getLineInfo();

    void open() throws LineUnavailableException;

    @Override // java.lang.AutoCloseable
    void close();

    boolean isOpen();

    Control[] getControls();

    boolean isControlSupported(Control.Type type);

    Control getControl(Control.Type type);

    void addLineListener(LineListener lineListener);

    void removeLineListener(LineListener lineListener);

    /* loaded from: rt.jar:javax/sound/sampled/Line$Info.class */
    public static class Info {
        private final Class lineClass;

        public Info(Class<?> cls) {
            if (cls == null) {
                this.lineClass = Line.class;
            } else {
                this.lineClass = cls;
            }
        }

        public Class<?> getLineClass() {
            return this.lineClass;
        }

        public boolean matches(Info info) {
            if (!getClass().isInstance(info) || !getLineClass().isAssignableFrom(info.getLineClass())) {
                return false;
            }
            return true;
        }

        public String toString() {
            String str;
            String str2 = new String(getLineClass().toString());
            int iIndexOf = str2.indexOf("javax.sound.sampled.");
            if (iIndexOf != -1) {
                str = str2.substring(0, iIndexOf) + str2.substring(iIndexOf + "javax.sound.sampled.".length(), str2.length());
            } else {
                str = str2;
            }
            return str;
        }
    }
}
