package javax.swing.plaf.synth;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.plaf.synth.SynthDesktopPaneUI;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthInternalFrameUI.class */
public class SynthInternalFrameUI extends BasicInternalFrameUI implements SynthUI, PropertyChangeListener {
    private SynthStyle style;

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthInternalFrameUI((JInternalFrame) jComponent);
    }

    protected SynthInternalFrameUI(JInternalFrame jInternalFrame) {
        super(jInternalFrame);
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    public void installDefaults() {
        JInternalFrame jInternalFrame = this.frame;
        LayoutManager layoutManagerCreateLayoutManager = createLayoutManager();
        this.internalFrameLayout = layoutManagerCreateLayoutManager;
        jInternalFrame.setLayout(layoutManagerCreateLayoutManager);
        updateStyle(this.frame);
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    protected void installListeners() {
        super.installListeners();
        this.frame.addPropertyChangeListener(this);
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    protected void uninstallComponents() {
        if (this.frame.getComponentPopupMenu() instanceof UIResource) {
            this.frame.setComponentPopupMenu(null);
        }
        super.uninstallComponents();
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    protected void uninstallListeners() {
        this.frame.removePropertyChangeListener(this);
        super.uninstallListeners();
    }

    private void updateStyle(JComponent jComponent) {
        SynthContext context = getContext(jComponent, 1);
        SynthStyle synthStyle = this.style;
        this.style = SynthLookAndFeel.updateStyle(context, this);
        if (this.style != synthStyle) {
            Icon frameIcon = this.frame.getFrameIcon();
            if (frameIcon == null || (frameIcon instanceof UIResource)) {
                this.frame.setFrameIcon(context.getStyle().getIcon(context, "InternalFrame.icon"));
            }
            if (synthStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    protected void uninstallDefaults() {
        SynthContext context = getContext(this.frame, 1);
        this.style.uninstallDefaults(context);
        context.dispose();
        this.style = null;
        if (this.frame.getLayout() == this.internalFrameLayout) {
            this.frame.setLayout(null);
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

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    protected JComponent createNorthPane(JInternalFrame jInternalFrame) {
        this.titlePane = new SynthInternalFrameTitlePane(jInternalFrame);
        this.titlePane.setName("InternalFrame.northPane");
        return this.titlePane;
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameUI
    protected ComponentListener createComponentListener() {
        if (UIManager.getBoolean("InternalFrame.useTaskBar")) {
            return new BasicInternalFrameUI.ComponentHandler() { // from class: javax.swing.plaf.synth.SynthInternalFrameUI.1
                @Override // javax.swing.plaf.basic.BasicInternalFrameUI.ComponentHandler, java.awt.event.ComponentListener
                public void componentResized(ComponentEvent componentEvent) {
                    if (SynthInternalFrameUI.this.frame != null && SynthInternalFrameUI.this.frame.isMaximum()) {
                        JDesktopPane jDesktopPane = (JDesktopPane) componentEvent.getSource();
                        Component[] components = jDesktopPane.getComponents();
                        int length = components.length;
                        int i2 = 0;
                        while (true) {
                            if (i2 >= length) {
                                break;
                            }
                            Component component = components[i2];
                            if (!(component instanceof SynthDesktopPaneUI.TaskBar)) {
                                i2++;
                            } else {
                                SynthInternalFrameUI.this.frame.setBounds(0, 0, jDesktopPane.getWidth(), jDesktopPane.getHeight() - component.getHeight());
                                SynthInternalFrameUI.this.frame.revalidate();
                                break;
                            }
                        }
                    }
                    JInternalFrame jInternalFrame = SynthInternalFrameUI.this.frame;
                    SynthInternalFrameUI.this.frame = null;
                    super.componentResized(componentEvent);
                    SynthInternalFrameUI.this.frame = jInternalFrame;
                }
            };
        }
        return super.createComponentListener();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        SynthLookAndFeel.update(context, graphics);
        context.getPainter().paintInternalFrameBackground(context, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
        paint(context, graphics);
        context.dispose();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        paint(context, graphics);
        context.dispose();
    }

    protected void paint(SynthContext synthContext, Graphics graphics) {
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintInternalFrameBorder(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        SynthStyle synthStyle = this.style;
        JInternalFrame jInternalFrame = (JInternalFrame) propertyChangeEvent.getSource();
        String propertyName = propertyChangeEvent.getPropertyName();
        if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
            updateStyle(jInternalFrame);
        }
        if (this.style == synthStyle) {
            if (propertyName == JInternalFrame.IS_MAXIMUM_PROPERTY || propertyName == JInternalFrame.IS_SELECTED_PROPERTY) {
                SynthContext context = getContext(jInternalFrame, 1);
                this.style.uninstallDefaults(context);
                this.style.installDefaults(context, this);
            }
        }
    }
}
