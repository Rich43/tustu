package javax.swing;

import java.awt.Container;
import sun.awt.AppContext;

/* loaded from: rt.jar:javax/swing/LayoutStyle.class */
public abstract class LayoutStyle {

    /* loaded from: rt.jar:javax/swing/LayoutStyle$ComponentPlacement.class */
    public enum ComponentPlacement {
        RELATED,
        UNRELATED,
        INDENT
    }

    public abstract int getPreferredGap(JComponent jComponent, JComponent jComponent2, ComponentPlacement componentPlacement, int i2, Container container);

    public abstract int getContainerGap(JComponent jComponent, int i2, Container container);

    public static void setInstance(LayoutStyle layoutStyle) {
        synchronized (LayoutStyle.class) {
            if (layoutStyle == null) {
                AppContext.getAppContext().remove(LayoutStyle.class);
            } else {
                AppContext.getAppContext().put(LayoutStyle.class, layoutStyle);
            }
        }
    }

    public static LayoutStyle getInstance() {
        LayoutStyle layoutStyle;
        synchronized (LayoutStyle.class) {
            layoutStyle = (LayoutStyle) AppContext.getAppContext().get(LayoutStyle.class);
        }
        if (layoutStyle == null) {
            return UIManager.getLookAndFeel().getLayoutStyle();
        }
        return layoutStyle;
    }
}
