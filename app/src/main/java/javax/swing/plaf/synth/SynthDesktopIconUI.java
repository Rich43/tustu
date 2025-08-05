package javax.swing.plaf.synth;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicDesktopIconUI;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthDesktopIconUI.class */
public class SynthDesktopIconUI extends BasicDesktopIconUI implements SynthUI, PropertyChangeListener {
    private SynthStyle style;
    private Handler handler = new Handler();

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthDesktopIconUI();
    }

    @Override // javax.swing.plaf.basic.BasicDesktopIconUI
    protected void installComponents() {
        if (UIManager.getBoolean("InternalFrame.useTaskBar")) {
            this.iconPane = new JToggleButton(this.frame.getTitle(), this.frame.getFrameIcon()) { // from class: javax.swing.plaf.synth.SynthDesktopIconUI.1
                @Override // javax.swing.JComponent
                public String getToolTipText() {
                    return getText();
                }

                @Override // javax.swing.JComponent
                public JPopupMenu getComponentPopupMenu() {
                    return SynthDesktopIconUI.this.frame.getComponentPopupMenu();
                }
            };
            ToolTipManager.sharedInstance().registerComponent(this.iconPane);
            this.iconPane.setFont(this.desktopIcon.getFont());
            this.iconPane.setBackground(this.desktopIcon.getBackground());
            this.iconPane.setForeground(this.desktopIcon.getForeground());
        } else {
            this.iconPane = new SynthInternalFrameTitlePane(this.frame);
            this.iconPane.setName("InternalFrame.northPane");
        }
        this.desktopIcon.setLayout(new BorderLayout());
        this.desktopIcon.add(this.iconPane, BorderLayout.CENTER);
    }

    @Override // javax.swing.plaf.basic.BasicDesktopIconUI
    protected void installListeners() {
        super.installListeners();
        this.desktopIcon.addPropertyChangeListener(this);
        if (this.iconPane instanceof JToggleButton) {
            this.frame.addPropertyChangeListener(this);
            ((JToggleButton) this.iconPane).addActionListener(this.handler);
        }
    }

    @Override // javax.swing.plaf.basic.BasicDesktopIconUI
    protected void uninstallListeners() {
        if (this.iconPane instanceof JToggleButton) {
            ((JToggleButton) this.iconPane).removeActionListener(this.handler);
            this.frame.removePropertyChangeListener(this);
        }
        this.desktopIcon.removePropertyChangeListener(this);
        super.uninstallListeners();
    }

    @Override // javax.swing.plaf.basic.BasicDesktopIconUI
    protected void installDefaults() {
        updateStyle(this.desktopIcon);
    }

    private void updateStyle(JComponent jComponent) {
        SynthContext context = getContext(jComponent, 1);
        this.style = SynthLookAndFeel.updateStyle(context, this);
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicDesktopIconUI
    protected void uninstallDefaults() {
        SynthContext context = getContext(this.desktopIcon, 1);
        this.style.uninstallDefaults(context);
        context.dispose();
        this.style = null;
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
        context.getPainter().paintDesktopIconBackground(context, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
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
        synthContext.getPainter().paintDesktopIconBorder(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (propertyChangeEvent.getSource() instanceof JInternalFrame.JDesktopIcon) {
            if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
                updateStyle((JInternalFrame.JDesktopIcon) propertyChangeEvent.getSource());
                return;
            }
            return;
        }
        if (propertyChangeEvent.getSource() instanceof JInternalFrame) {
            JInternalFrame jInternalFrame = (JInternalFrame) propertyChangeEvent.getSource();
            if (this.iconPane instanceof JToggleButton) {
                JToggleButton jToggleButton = (JToggleButton) this.iconPane;
                String propertyName = propertyChangeEvent.getPropertyName();
                if (propertyName == "title") {
                    jToggleButton.setText((String) propertyChangeEvent.getNewValue());
                    return;
                }
                if (propertyName == JInternalFrame.FRAME_ICON_PROPERTY) {
                    jToggleButton.setIcon((Icon) propertyChangeEvent.getNewValue());
                } else if (propertyName == "icon" || propertyName == JInternalFrame.IS_SELECTED_PROPERTY) {
                    jToggleButton.setSelected(!jInternalFrame.isIcon() && jInternalFrame.isSelected());
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthDesktopIconUI$Handler.class */
    private final class Handler implements ActionListener {
        private Handler() {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (actionEvent.getSource() instanceof JToggleButton) {
                JToggleButton jToggleButton = (JToggleButton) actionEvent.getSource();
                try {
                    boolean zIsSelected = jToggleButton.isSelected();
                    if (zIsSelected || SynthDesktopIconUI.this.frame.isIconifiable()) {
                        SynthDesktopIconUI.this.frame.setIcon(!zIsSelected);
                        if (zIsSelected) {
                            SynthDesktopIconUI.this.frame.setSelected(true);
                        }
                    } else {
                        jToggleButton.setSelected(true);
                    }
                } catch (PropertyVetoException e2) {
                }
            }
        }
    }
}
