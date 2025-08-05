package javax.sound.sampled;

import java.util.EventObject;
import javafx.fxml.FXMLLoader;
import sun.security.tools.policytool.ToolWindow;

/* loaded from: rt.jar:javax/sound/sampled/LineEvent.class */
public class LineEvent extends EventObject {
    private final Type type;
    private final long position;

    public LineEvent(Line line, Type type, long j2) {
        super(line);
        this.type = type;
        this.position = j2;
    }

    public final Line getLine() {
        return (Line) getSource();
    }

    public final Type getType() {
        return this.type;
    }

    public final long getFramePosition() {
        return this.position;
    }

    @Override // java.util.EventObject
    public String toString() {
        String string;
        String str = this.type != null ? this.type.toString() + " " : "";
        if (getLine() == null) {
            string = FXMLLoader.NULL_KEYWORD;
        } else {
            string = getLine().toString();
        }
        return new String(str + "event from line " + string);
    }

    /* loaded from: rt.jar:javax/sound/sampled/LineEvent$Type.class */
    public static class Type {
        private String name;
        public static final Type OPEN = new Type(ToolWindow.OPEN_POLICY_FILE);
        public static final Type CLOSE = new Type("Close");
        public static final Type START = new Type("Start");
        public static final Type STOP = new Type("Stop");

        protected Type(String str) {
            this.name = str;
        }

        public final boolean equals(Object obj) {
            return super.equals(obj);
        }

        public final int hashCode() {
            return super.hashCode();
        }

        public String toString() {
            return this.name;
        }
    }
}
