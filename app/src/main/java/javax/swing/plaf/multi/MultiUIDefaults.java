package javax.swing.plaf.multi;

import javax.swing.UIDefaults;

/* compiled from: MultiLookAndFeel.java */
/* loaded from: rt.jar:javax/swing/plaf/multi/MultiUIDefaults.class */
class MultiUIDefaults extends UIDefaults {
    MultiUIDefaults(int i2, float f2) {
        super(i2, f2);
    }

    @Override // javax.swing.UIDefaults
    protected void getUIError(String str) {
        System.err.println("Multiplexing LAF:  " + str);
    }
}
