package sun.awt;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.util.StringTokenizer;

/* loaded from: rt.jar:sun/awt/TracedEventQueue.class */
public class TracedEventQueue extends EventQueue {
    static boolean trace = false;
    static int[] suppressedIDs;

    static {
        suppressedIDs = null;
        String property = Toolkit.getProperty("AWT.IgnoreEventIDs", "");
        if (property.length() > 0) {
            StringTokenizer stringTokenizer = new StringTokenizer(property, ",");
            int iCountTokens = stringTokenizer.countTokens();
            suppressedIDs = new int[iCountTokens];
            for (int i2 = 0; i2 < iCountTokens; i2++) {
                String strNextToken = stringTokenizer.nextToken();
                try {
                    suppressedIDs[i2] = Integer.parseInt(strNextToken);
                } catch (NumberFormatException e2) {
                    System.err.println("Bad ID listed in AWT.IgnoreEventIDs in awt.properties: \"" + strNextToken + "\" -- skipped");
                    suppressedIDs[i2] = 0;
                }
            }
            return;
        }
        suppressedIDs = new int[0];
    }

    @Override // java.awt.EventQueue
    public void postEvent(AWTEvent aWTEvent) {
        boolean z2 = true;
        int id = aWTEvent.getID();
        int i2 = 0;
        while (true) {
            if (i2 >= suppressedIDs.length) {
                break;
            }
            if (id != suppressedIDs[i2]) {
                i2++;
            } else {
                z2 = false;
                break;
            }
        }
        if (z2) {
            System.out.println(Thread.currentThread().getName() + ": " + ((Object) aWTEvent));
        }
        super.postEvent(aWTEvent);
    }
}
