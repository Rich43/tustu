package javax.swing.plaf.synth;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import javax.swing.DefaultDesktopManager;
import javax.swing.DesktopManager;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicDesktopPaneUI;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthDesktopPaneUI.class */
public class SynthDesktopPaneUI extends BasicDesktopPaneUI implements PropertyChangeListener, SynthUI {
    private SynthStyle style;
    private TaskBar taskBar;
    private DesktopManager oldDesktopManager;

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthDesktopPaneUI();
    }

    @Override // javax.swing.plaf.basic.BasicDesktopPaneUI
    protected void installListeners() {
        super.installListeners();
        this.desktop.addPropertyChangeListener(this);
        if (this.taskBar != null) {
            this.desktop.addComponentListener(this.taskBar);
            this.desktop.addContainerListener(this.taskBar);
        }
    }

    @Override // javax.swing.plaf.basic.BasicDesktopPaneUI
    protected void installDefaults() {
        JInternalFrame.JDesktopIcon desktopIcon;
        updateStyle(this.desktop);
        if (UIManager.getBoolean("InternalFrame.useTaskBar")) {
            this.taskBar = new TaskBar();
            for (Component component : this.desktop.getComponents()) {
                if (component instanceof JInternalFrame.JDesktopIcon) {
                    desktopIcon = (JInternalFrame.JDesktopIcon) component;
                } else if (component instanceof JInternalFrame) {
                    desktopIcon = ((JInternalFrame) component).getDesktopIcon();
                }
                if (desktopIcon.getParent() == this.desktop) {
                    this.desktop.remove(desktopIcon);
                }
                if (desktopIcon.getParent() != this.taskBar) {
                    this.taskBar.add(desktopIcon);
                    desktopIcon.getInternalFrame().addComponentListener(this.taskBar);
                }
            }
            this.taskBar.setBackground(this.desktop.getBackground());
            this.desktop.add(this.taskBar, Integer.valueOf(JLayeredPane.PALETTE_LAYER.intValue() + 1));
            if (this.desktop.isShowing()) {
                this.taskBar.adjustSize();
            }
        }
    }

    private void updateStyle(JDesktopPane jDesktopPane) {
        SynthStyle synthStyle = this.style;
        SynthContext context = getContext(jDesktopPane, 1);
        this.style = SynthLookAndFeel.updateStyle(context, this);
        if (synthStyle != null) {
            uninstallKeyboardActions();
            installKeyboardActions();
        }
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicDesktopPaneUI
    protected void uninstallListeners() {
        if (this.taskBar != null) {
            this.desktop.removeComponentListener(this.taskBar);
            this.desktop.removeContainerListener(this.taskBar);
        }
        this.desktop.removePropertyChangeListener(this);
        super.uninstallListeners();
    }

    @Override // javax.swing.plaf.basic.BasicDesktopPaneUI
    protected void uninstallDefaults() {
        SynthContext context = getContext(this.desktop, 1);
        this.style.uninstallDefaults(context);
        context.dispose();
        this.style = null;
        if (this.taskBar != null) {
            for (Component component : this.taskBar.getComponents()) {
                JInternalFrame.JDesktopIcon jDesktopIcon = (JInternalFrame.JDesktopIcon) component;
                this.taskBar.remove(jDesktopIcon);
                jDesktopIcon.setPreferredSize(null);
                JInternalFrame internalFrame = jDesktopIcon.getInternalFrame();
                if (internalFrame.isIcon()) {
                    this.desktop.add(jDesktopIcon);
                }
                internalFrame.removeComponentListener(this.taskBar);
            }
            this.desktop.remove(this.taskBar);
            this.taskBar = null;
        }
    }

    @Override // javax.swing.plaf.basic.BasicDesktopPaneUI
    protected void installDesktopManager() {
        if (UIManager.getBoolean("InternalFrame.useTaskBar")) {
            DesktopManager desktopManager = this.desktop.getDesktopManager();
            this.oldDesktopManager = desktopManager;
            this.desktopManager = desktopManager;
            if (!(this.desktopManager instanceof SynthDesktopManager)) {
                this.desktopManager = new SynthDesktopManager();
                this.desktop.setDesktopManager(this.desktopManager);
                return;
            }
            return;
        }
        super.installDesktopManager();
    }

    @Override // javax.swing.plaf.basic.BasicDesktopPaneUI
    protected void uninstallDesktopManager() {
        if (this.oldDesktopManager != null && !(this.oldDesktopManager instanceof UIResource)) {
            this.desktopManager = this.desktop.getDesktopManager();
            if (this.desktopManager == null || (this.desktopManager instanceof UIResource)) {
                this.desktop.setDesktopManager(this.oldDesktopManager);
            }
        }
        this.oldDesktopManager = null;
        super.uninstallDesktopManager();
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthDesktopPaneUI$TaskBar.class */
    static class TaskBar extends JPanel implements ComponentListener, ContainerListener {
        TaskBar() {
            setOpaque(true);
            setLayout(new FlowLayout(0, 0, 0) { // from class: javax.swing.plaf.synth.SynthDesktopPaneUI.TaskBar.1
                @Override // java.awt.FlowLayout, java.awt.LayoutManager
                public void layoutContainer(Container container) {
                    Component[] components = container.getComponents();
                    int length = components.length;
                    if (length > 0) {
                        int i2 = 0;
                        for (Component component : components) {
                            component.setPreferredSize(null);
                            Dimension preferredSize = component.getPreferredSize();
                            if (preferredSize.width > i2) {
                                i2 = preferredSize.width;
                            }
                        }
                        Insets insets = container.getInsets();
                        int iMin = Math.min(i2, Math.max(10, ((container.getWidth() - insets.left) - insets.right) / length));
                        for (Component component2 : components) {
                            component2.setPreferredSize(new Dimension(iMin, component2.getPreferredSize().height));
                        }
                    }
                    super.layoutContainer(container);
                }
            });
            setBorder(new BevelBorder(0) { // from class: javax.swing.plaf.synth.SynthDesktopPaneUI.TaskBar.2
                @Override // javax.swing.border.BevelBorder
                protected void paintRaisedBevel(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
                    Color color = graphics.getColor();
                    graphics.translate(i2, i3);
                    graphics.setColor(getHighlightOuterColor(component));
                    graphics.drawLine(0, 0, 0, i5 - 2);
                    graphics.drawLine(1, 0, i4 - 2, 0);
                    graphics.setColor(getShadowOuterColor(component));
                    graphics.drawLine(0, i5 - 1, i4 - 1, i5 - 1);
                    graphics.drawLine(i4 - 1, 0, i4 - 1, i5 - 2);
                    graphics.translate(-i2, -i3);
                    graphics.setColor(color);
                }
            });
        }

        void adjustSize() {
            JDesktopPane jDesktopPane = (JDesktopPane) getParent();
            if (jDesktopPane != null) {
                int height = getPreferredSize().height;
                Insets insets = getInsets();
                if (height == insets.top + insets.bottom) {
                    if (getHeight() <= height) {
                        height += 21;
                    } else {
                        height = getHeight();
                    }
                }
                setBounds(0, jDesktopPane.getHeight() - height, jDesktopPane.getWidth(), height);
                revalidate();
                repaint();
            }
        }

        @Override // java.awt.event.ComponentListener
        public void componentResized(ComponentEvent componentEvent) {
            if (componentEvent.getSource() instanceof JDesktopPane) {
                adjustSize();
            }
        }

        @Override // java.awt.event.ComponentListener
        public void componentMoved(ComponentEvent componentEvent) {
        }

        @Override // java.awt.event.ComponentListener
        public void componentShown(ComponentEvent componentEvent) {
            if (componentEvent.getSource() instanceof JInternalFrame) {
                adjustSize();
            }
        }

        @Override // java.awt.event.ComponentListener
        public void componentHidden(ComponentEvent componentEvent) {
            if (componentEvent.getSource() instanceof JInternalFrame) {
                ((JInternalFrame) componentEvent.getSource()).getDesktopIcon().setVisible(false);
                revalidate();
            }
        }

        @Override // java.awt.event.ContainerListener
        public void componentAdded(ContainerEvent containerEvent) {
            if (containerEvent.getChild() instanceof JInternalFrame) {
                JInternalFrame jInternalFrame = (JInternalFrame) containerEvent.getChild();
                JInternalFrame.JDesktopIcon desktopIcon = jInternalFrame.getDesktopIcon();
                for (Component component : getComponents()) {
                    if (component == desktopIcon) {
                        return;
                    }
                }
                add(desktopIcon);
                jInternalFrame.addComponentListener(this);
                if (getComponentCount() == 1) {
                    adjustSize();
                }
            }
        }

        @Override // java.awt.event.ContainerListener
        public void componentRemoved(ContainerEvent containerEvent) {
            if (containerEvent.getChild() instanceof JInternalFrame) {
                JInternalFrame jInternalFrame = (JInternalFrame) containerEvent.getChild();
                if (!jInternalFrame.isIcon()) {
                    remove(jInternalFrame.getDesktopIcon());
                    jInternalFrame.removeComponentListener(this);
                    revalidate();
                    repaint();
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthDesktopPaneUI$SynthDesktopManager.class */
    class SynthDesktopManager extends DefaultDesktopManager implements UIResource {
        SynthDesktopManager() {
        }

        @Override // javax.swing.DefaultDesktopManager, javax.swing.DesktopManager
        public void maximizeFrame(JInternalFrame jInternalFrame) {
            if (jInternalFrame.isIcon()) {
                try {
                    jInternalFrame.setIcon(false);
                } catch (PropertyVetoException e2) {
                }
            } else {
                jInternalFrame.setNormalBounds(jInternalFrame.getBounds());
                Container parent = jInternalFrame.getParent();
                setBoundsForFrame(jInternalFrame, 0, 0, parent.getWidth(), parent.getHeight() - SynthDesktopPaneUI.this.taskBar.getHeight());
            }
            try {
                jInternalFrame.setSelected(true);
            } catch (PropertyVetoException e3) {
            }
        }

        @Override // javax.swing.DefaultDesktopManager, javax.swing.DesktopManager
        public void iconifyFrame(JInternalFrame jInternalFrame) {
            Container parent = jInternalFrame.getParent();
            jInternalFrame.getDesktopPane();
            boolean zIsSelected = jInternalFrame.isSelected();
            if (parent == null) {
                return;
            }
            jInternalFrame.getDesktopIcon();
            if (!jInternalFrame.isMaximum()) {
                jInternalFrame.setNormalBounds(jInternalFrame.getBounds());
            }
            parent.remove(jInternalFrame);
            parent.repaint(jInternalFrame.getX(), jInternalFrame.getY(), jInternalFrame.getWidth(), jInternalFrame.getHeight());
            try {
                jInternalFrame.setSelected(false);
            } catch (PropertyVetoException e2) {
            }
            if (zIsSelected) {
                for (Component component : parent.getComponents()) {
                    if (component instanceof JInternalFrame) {
                        try {
                            ((JInternalFrame) component).setSelected(true);
                        } catch (PropertyVetoException e3) {
                        }
                        ((JInternalFrame) component).moveToFront();
                        return;
                    }
                }
            }
        }

        @Override // javax.swing.DefaultDesktopManager, javax.swing.DesktopManager
        public void deiconifyFrame(JInternalFrame jInternalFrame) {
            Container parent;
            Container parent2 = jInternalFrame.getDesktopIcon().getParent();
            if (parent2 != null && (parent = parent2.getParent()) != null) {
                parent.add(jInternalFrame);
                if (jInternalFrame.isMaximum()) {
                    int width = parent.getWidth();
                    int height = parent.getHeight() - SynthDesktopPaneUI.this.taskBar.getHeight();
                    if (jInternalFrame.getWidth() != width || jInternalFrame.getHeight() != height) {
                        setBoundsForFrame(jInternalFrame, 0, 0, width, height);
                    }
                }
                if (jInternalFrame.isSelected()) {
                    jInternalFrame.moveToFront();
                } else {
                    try {
                        jInternalFrame.setSelected(true);
                    } catch (PropertyVetoException e2) {
                    }
                }
            }
        }

        @Override // javax.swing.DefaultDesktopManager
        protected void removeIconFor(JInternalFrame jInternalFrame) {
            super.removeIconFor(jInternalFrame);
            SynthDesktopPaneUI.this.taskBar.validate();
        }

        @Override // javax.swing.DefaultDesktopManager, javax.swing.DesktopManager
        public void setBoundsForFrame(JComponent jComponent, int i2, int i3, int i4, int i5) {
            super.setBoundsForFrame(jComponent, i2, i3, i4, i5);
            if (SynthDesktopPaneUI.this.taskBar != null && i3 >= SynthDesktopPaneUI.this.taskBar.getY()) {
                jComponent.setLocation(jComponent.getX(), SynthDesktopPaneUI.this.taskBar.getY() - jComponent.getInsets().top);
            }
        }
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public SynthContext getContext(JComponent jComponent) {
        return getContext(jComponent, getComponentState(jComponent));
    }

    private SynthContext getContext(JComponent jComponent, int i2) {
        return SynthContext.getContext(jComponent, this.style, i2);
    }

    private int getComponentState(JComponent jComponent) {
        return SynthLookAndFeel.getComponentState(jComponent);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        SynthLookAndFeel.update(context, graphics);
        context.getPainter().paintDesktopPaneBackground(context, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
        paint(context, graphics);
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicDesktopPaneUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        paint(context, graphics);
        context.dispose();
    }

    protected void paint(SynthContext synthContext, Graphics graphics) {
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintDesktopPaneBorder(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
            updateStyle((JDesktopPane) propertyChangeEvent.getSource());
        }
        if (propertyChangeEvent.getPropertyName() == "ancestor" && this.taskBar != null) {
            this.taskBar.adjustSize();
        }
    }
}
