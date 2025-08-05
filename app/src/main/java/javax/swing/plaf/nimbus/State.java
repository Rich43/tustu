package javax.swing.plaf.nimbus;

import java.util.HashMap;
import java.util.Map;
import javax.swing.Action;
import javax.swing.JComponent;
import org.slf4j.Marker;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/State.class */
public abstract class State<T extends JComponent> {
    static final Map<String, StandardState> standardStates = new HashMap(7);
    static final State Enabled = new StandardState(1);
    static final State MouseOver = new StandardState(2);
    static final State Pressed = new StandardState(4);
    static final State Disabled = new StandardState(8);
    static final State Focused = new StandardState(256);
    static final State Selected = new StandardState(512);
    static final State Default = new StandardState(1024);
    private String name;

    protected abstract boolean isInState(T t2);

    protected State(String str) {
        this.name = str;
    }

    public String toString() {
        return this.name;
    }

    boolean isInState(T t2, int i2) {
        return isInState(t2);
    }

    String getName() {
        return this.name;
    }

    static boolean isStandardStateName(String str) {
        return standardStates.containsKey(str);
    }

    static StandardState getStandardState(String str) {
        return standardStates.get(str);
    }

    /* loaded from: rt.jar:javax/swing/plaf/nimbus/State$StandardState.class */
    static final class StandardState extends State<JComponent> {
        private int state;

        private StandardState(int i2) {
            super(toString(i2));
            this.state = i2;
            standardStates.put(getName(), this);
        }

        public int getState() {
            return this.state;
        }

        @Override // javax.swing.plaf.nimbus.State
        boolean isInState(JComponent jComponent, int i2) {
            return (i2 & this.state) == this.state;
        }

        @Override // javax.swing.plaf.nimbus.State
        protected boolean isInState(JComponent jComponent) {
            throw new AssertionError((Object) "This method should never be called");
        }

        private static String toString(int i2) {
            StringBuffer stringBuffer = new StringBuffer();
            if ((i2 & 1024) == 1024) {
                stringBuffer.append(Action.DEFAULT);
            }
            if ((i2 & 8) == 8) {
                if (stringBuffer.length() > 0) {
                    stringBuffer.append(Marker.ANY_NON_NULL_MARKER);
                }
                stringBuffer.append("Disabled");
            }
            if ((i2 & 1) == 1) {
                if (stringBuffer.length() > 0) {
                    stringBuffer.append(Marker.ANY_NON_NULL_MARKER);
                }
                stringBuffer.append("Enabled");
            }
            if ((i2 & 256) == 256) {
                if (stringBuffer.length() > 0) {
                    stringBuffer.append(Marker.ANY_NON_NULL_MARKER);
                }
                stringBuffer.append("Focused");
            }
            if ((i2 & 2) == 2) {
                if (stringBuffer.length() > 0) {
                    stringBuffer.append(Marker.ANY_NON_NULL_MARKER);
                }
                stringBuffer.append("MouseOver");
            }
            if ((i2 & 4) == 4) {
                if (stringBuffer.length() > 0) {
                    stringBuffer.append(Marker.ANY_NON_NULL_MARKER);
                }
                stringBuffer.append("Pressed");
            }
            if ((i2 & 512) == 512) {
                if (stringBuffer.length() > 0) {
                    stringBuffer.append(Marker.ANY_NON_NULL_MARKER);
                }
                stringBuffer.append("Selected");
            }
            return stringBuffer.toString();
        }
    }
}
