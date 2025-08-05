package com.sun.java.swing.plaf.motif;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;
import sun.awt.AppContext;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifLabelUI.class */
public class MotifLabelUI extends BasicLabelUI {
    private static final Object MOTIF_LABEL_UI_KEY = new Object();

    public static ComponentUI createUI(JComponent jComponent) {
        AppContext appContext = AppContext.getAppContext();
        MotifLabelUI motifLabelUI = (MotifLabelUI) appContext.get(MOTIF_LABEL_UI_KEY);
        if (motifLabelUI == null) {
            motifLabelUI = new MotifLabelUI();
            appContext.put(MOTIF_LABEL_UI_KEY, motifLabelUI);
        }
        return motifLabelUI;
    }
}
