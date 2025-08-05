package javax.swing;

import java.awt.Color;
import java.io.PrintStream;
import java.util.Hashtable;

/* loaded from: rt.jar:javax/swing/DebugGraphicsInfo.class */
class DebugGraphicsInfo {
    Hashtable<JComponent, Integer> componentToDebug;
    Color flashColor = Color.red;
    int flashTime = 100;
    int flashCount = 2;
    JFrame debugFrame = null;
    PrintStream stream = System.out;

    DebugGraphicsInfo() {
    }

    void setDebugOptions(JComponent jComponent, int i2) {
        if (i2 == 0) {
            return;
        }
        if (this.componentToDebug == null) {
            this.componentToDebug = new Hashtable<>();
        }
        if (i2 > 0) {
            this.componentToDebug.put(jComponent, Integer.valueOf(i2));
        } else {
            this.componentToDebug.remove(jComponent);
        }
    }

    int getDebugOptions(JComponent jComponent) {
        Integer num;
        if (this.componentToDebug == null || (num = this.componentToDebug.get(jComponent)) == null) {
            return 0;
        }
        return num.intValue();
    }

    void log(String str) {
        this.stream.println(str);
    }
}
