package javax.swing.plaf;

import java.awt.Graphics;
import javax.swing.JSplitPane;

/* loaded from: rt.jar:javax/swing/plaf/SplitPaneUI.class */
public abstract class SplitPaneUI extends ComponentUI {
    public abstract void resetToPreferredSizes(JSplitPane jSplitPane);

    public abstract void setDividerLocation(JSplitPane jSplitPane, int i2);

    public abstract int getDividerLocation(JSplitPane jSplitPane);

    public abstract int getMinimumDividerLocation(JSplitPane jSplitPane);

    public abstract int getMaximumDividerLocation(JSplitPane jSplitPane);

    public abstract void finishedPaintingChildren(JSplitPane jSplitPane, Graphics graphics);
}
