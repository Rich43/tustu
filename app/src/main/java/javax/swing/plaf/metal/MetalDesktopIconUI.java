package javax.swing.plaf.metal;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicDesktopIconUI;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalDesktopIconUI.class */
public class MetalDesktopIconUI extends BasicDesktopIconUI {
    JButton button;
    JLabel label;
    TitleListener titleListener;
    private int width;

    public static ComponentUI createUI(JComponent jComponent) {
        return new MetalDesktopIconUI();
    }

    @Override // javax.swing.plaf.basic.BasicDesktopIconUI
    protected void installDefaults() {
        super.installDefaults();
        LookAndFeel.installColorsAndFont(this.desktopIcon, "DesktopIcon.background", "DesktopIcon.foreground", "DesktopIcon.font");
        this.width = UIManager.getInt("DesktopIcon.width");
    }

    @Override // javax.swing.plaf.basic.BasicDesktopIconUI
    protected void installComponents() {
        this.frame = this.desktopIcon.getInternalFrame();
        this.button = new JButton(this.frame.getTitle(), this.frame.getFrameIcon());
        this.button.addActionListener(new ActionListener() { // from class: javax.swing.plaf.metal.MetalDesktopIconUI.1
            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent actionEvent) {
                MetalDesktopIconUI.this.deiconize();
            }
        });
        this.button.setFont(this.desktopIcon.getFont());
        this.button.setBackground(this.desktopIcon.getBackground());
        this.button.setForeground(this.desktopIcon.getForeground());
        int i2 = this.button.getPreferredSize().height;
        this.label = new JLabel(new MetalBumps(i2 / 3, i2, MetalLookAndFeel.getControlHighlight(), MetalLookAndFeel.getControlDarkShadow(), MetalLookAndFeel.getControl()));
        this.label.setBorder(new MatteBorder(0, 2, 0, 1, this.desktopIcon.getBackground()));
        this.desktopIcon.setLayout(new BorderLayout(2, 0));
        this.desktopIcon.add(this.button, BorderLayout.CENTER);
        this.desktopIcon.add(this.label, "West");
    }

    @Override // javax.swing.plaf.basic.BasicDesktopIconUI
    protected void uninstallComponents() {
        this.desktopIcon.setLayout(null);
        this.desktopIcon.remove(this.label);
        this.desktopIcon.remove(this.button);
        this.button = null;
        this.frame = null;
    }

    @Override // javax.swing.plaf.basic.BasicDesktopIconUI
    protected void installListeners() {
        super.installListeners();
        JInternalFrame internalFrame = this.desktopIcon.getInternalFrame();
        TitleListener titleListener = new TitleListener();
        this.titleListener = titleListener;
        internalFrame.addPropertyChangeListener(titleListener);
    }

    @Override // javax.swing.plaf.basic.BasicDesktopIconUI
    protected void uninstallListeners() {
        this.desktopIcon.getInternalFrame().removePropertyChangeListener(this.titleListener);
        this.titleListener = null;
        super.uninstallListeners();
    }

    @Override // javax.swing.plaf.basic.BasicDesktopIconUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        return getMinimumSize(jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicDesktopIconUI, javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        return new Dimension(this.width, this.desktopIcon.getLayout().minimumLayoutSize(this.desktopIcon).height);
    }

    @Override // javax.swing.plaf.basic.BasicDesktopIconUI, javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        return getMinimumSize(jComponent);
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalDesktopIconUI$TitleListener.class */
    class TitleListener implements PropertyChangeListener {
        TitleListener() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (propertyChangeEvent.getPropertyName().equals("title")) {
                MetalDesktopIconUI.this.button.setText((String) propertyChangeEvent.getNewValue());
            }
            if (propertyChangeEvent.getPropertyName().equals(JInternalFrame.FRAME_ICON_PROPERTY)) {
                MetalDesktopIconUI.this.button.setIcon((Icon) propertyChangeEvent.getNewValue());
            }
        }
    }
}
