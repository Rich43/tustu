package com.sun.java.swing.plaf.motif;

import javax.swing.AbstractButton;
import javax.swing.plaf.basic.BasicButtonListener;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifButtonListener.class */
public class MotifButtonListener extends BasicButtonListener {
    public MotifButtonListener(AbstractButton abstractButton) {
        super(abstractButton);
    }

    @Override // javax.swing.plaf.basic.BasicButtonListener
    protected void checkOpacity(AbstractButton abstractButton) {
        abstractButton.setOpaque(false);
    }
}
