package com.sun.javafx.text;

import com.sun.javafx.scene.text.TextLayout;
import com.sun.javafx.scene.text.TextLayoutFactory;

/* loaded from: jfxrt.jar:com/sun/javafx/text/PrismTextLayoutFactory.class */
public class PrismTextLayoutFactory implements TextLayoutFactory {
    private static boolean inUse;
    private static final PrismTextLayout reusableTL = new PrismTextLayout();
    private static final PrismTextLayoutFactory factory = new PrismTextLayoutFactory();

    private PrismTextLayoutFactory() {
    }

    @Override // com.sun.javafx.scene.text.TextLayoutFactory
    public TextLayout createLayout() {
        return new PrismTextLayout();
    }

    @Override // com.sun.javafx.scene.text.TextLayoutFactory
    public TextLayout getLayout() {
        if (inUse) {
            return new PrismTextLayout();
        }
        synchronized (PrismTextLayoutFactory.class) {
            if (inUse) {
                return new PrismTextLayout();
            }
            inUse = true;
            reusableTL.setAlignment(0);
            reusableTL.setWrapWidth(0.0f);
            reusableTL.setDirection(0);
            reusableTL.setContent(null);
            return reusableTL;
        }
    }

    @Override // com.sun.javafx.scene.text.TextLayoutFactory
    public void disposeLayout(TextLayout layout) {
        if (layout == reusableTL) {
            inUse = false;
        }
    }

    public static PrismTextLayoutFactory getFactory() {
        return factory;
    }
}
