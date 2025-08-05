package javax.swing.plaf.metal;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;
import javax.swing.plaf.metal.MetalBorders;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalScrollPaneUI.class */
public class MetalScrollPaneUI extends BasicScrollPaneUI {
    private PropertyChangeListener scrollBarSwapListener;

    public static ComponentUI createUI(JComponent jComponent) {
        return new MetalScrollPaneUI();
    }

    @Override // javax.swing.plaf.basic.BasicScrollPaneUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
        updateScrollbarsFreeStanding();
    }

    @Override // javax.swing.plaf.basic.BasicScrollPaneUI, javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
        JScrollPane jScrollPane = (JScrollPane) jComponent;
        JScrollBar horizontalScrollBar = jScrollPane.getHorizontalScrollBar();
        JScrollBar verticalScrollBar = jScrollPane.getVerticalScrollBar();
        if (horizontalScrollBar != null) {
            horizontalScrollBar.putClientProperty(MetalScrollBarUI.FREE_STANDING_PROP, null);
        }
        if (verticalScrollBar != null) {
            verticalScrollBar.putClientProperty(MetalScrollBarUI.FREE_STANDING_PROP, null);
        }
    }

    @Override // javax.swing.plaf.basic.BasicScrollPaneUI
    public void installListeners(JScrollPane jScrollPane) {
        super.installListeners(jScrollPane);
        this.scrollBarSwapListener = createScrollBarSwapListener();
        jScrollPane.addPropertyChangeListener(this.scrollBarSwapListener);
    }

    @Override // javax.swing.plaf.basic.BasicScrollPaneUI
    protected void uninstallListeners(JComponent jComponent) {
        super.uninstallListeners(jComponent);
        jComponent.removePropertyChangeListener(this.scrollBarSwapListener);
    }

    @Deprecated
    public void uninstallListeners(JScrollPane jScrollPane) {
        super.uninstallListeners((JComponent) jScrollPane);
        jScrollPane.removePropertyChangeListener(this.scrollBarSwapListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateScrollbarsFreeStanding() {
        Boolean bool;
        if (this.scrollpane == null) {
            return;
        }
        if (this.scrollpane.getBorder() instanceof MetalBorders.ScrollPaneBorder) {
            bool = Boolean.FALSE;
        } else {
            bool = Boolean.TRUE;
        }
        JScrollBar horizontalScrollBar = this.scrollpane.getHorizontalScrollBar();
        if (horizontalScrollBar != null) {
            horizontalScrollBar.putClientProperty(MetalScrollBarUI.FREE_STANDING_PROP, bool);
        }
        JScrollBar verticalScrollBar = this.scrollpane.getVerticalScrollBar();
        if (verticalScrollBar != null) {
            verticalScrollBar.putClientProperty(MetalScrollBarUI.FREE_STANDING_PROP, bool);
        }
    }

    protected PropertyChangeListener createScrollBarSwapListener() {
        return new PropertyChangeListener() { // from class: javax.swing.plaf.metal.MetalScrollPaneUI.1
            @Override // java.beans.PropertyChangeListener
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                String propertyName = propertyChangeEvent.getPropertyName();
                if (propertyName.equals("verticalScrollBar") || propertyName.equals("horizontalScrollBar")) {
                    JScrollBar jScrollBar = (JScrollBar) propertyChangeEvent.getOldValue();
                    if (jScrollBar != null) {
                        jScrollBar.putClientProperty(MetalScrollBarUI.FREE_STANDING_PROP, null);
                    }
                    JScrollBar jScrollBar2 = (JScrollBar) propertyChangeEvent.getNewValue();
                    if (jScrollBar2 != null) {
                        jScrollBar2.putClientProperty(MetalScrollBarUI.FREE_STANDING_PROP, Boolean.FALSE);
                        return;
                    }
                    return;
                }
                if ("border".equals(propertyName)) {
                    MetalScrollPaneUI.this.updateScrollbarsFreeStanding();
                }
            }
        };
    }
}
