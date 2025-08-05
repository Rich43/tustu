package de.muntjak.tinylookandfeel;

import de.muntjak.tinylookandfeel.borders.TinyToolButtonBorder;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalFileChooserUI;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyFileChooserUI.class */
public class TinyFileChooserUI extends MetalFileChooserUI {
    public static final String IS_FILE_CHOOSER_BUTTON_KEY = "isFileChooserButton";
    private static final Dimension hstrut1 = new Dimension(1, 1);

    public static ComponentUI createUI(JComponent jComponent) {
        return new TinyFileChooserUI((JFileChooser) jComponent);
    }

    private TinyFileChooserUI(JFileChooser jFileChooser) {
        super(jFileChooser);
    }

    @Override // javax.swing.plaf.metal.MetalFileChooserUI, javax.swing.plaf.basic.BasicFileChooserUI
    public void installComponents(JFileChooser jFileChooser) throws IllegalArgumentException {
        super.installComponents(jFileChooser);
        Component[] components = jFileChooser.getComponents();
        for (int i2 = 0; i2 < components.length; i2++) {
            if (components[i2] instanceof JPanel) {
                Component[] components2 = ((Container) components[i2]).getComponents();
                for (int i3 = 0; i3 < components2.length; i3++) {
                    if (components2[i3] instanceof JPanel) {
                        Component[] components3 = ((Container) components2[i3]).getComponents();
                        Vector vector = new Vector();
                        for (int i4 = 0; i4 < components3.length; i4++) {
                            if ((components3[i4] instanceof AbstractButton) && isFileChooserIcon(((AbstractButton) components3[i4]).getIcon())) {
                                vector.add(components3[i4]);
                            }
                        }
                        if (vector.size() >= 4) {
                            JPanel jPanel = (JPanel) components2[i3];
                            TinyToolButtonBorder tinyToolButtonBorder = new TinyToolButtonBorder();
                            jPanel.removeAll();
                            Iterator it = vector.iterator();
                            while (it.hasNext()) {
                                AbstractButton abstractButton = (AbstractButton) it.next();
                                abstractButton.putClientProperty(IS_FILE_CHOOSER_BUTTON_KEY, Boolean.TRUE);
                                abstractButton.setOpaque(false);
                                abstractButton.setBorder(tinyToolButtonBorder);
                                if (abstractButton instanceof JToggleButton) {
                                    jPanel.add(Box.createRigidArea(hstrut1));
                                    abstractButton.setMargin(new Insets(4, 2, 5, 2));
                                } else {
                                    abstractButton.setMargin(new Insets(2, 2, 2, 2));
                                }
                                jPanel.add(abstractButton);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isFileChooserIcon(Icon icon) {
        if (icon == null) {
            return false;
        }
        return icon.equals(this.upFolderIcon) || icon.equals(this.homeFolderIcon) || icon.equals(this.newFolderIcon) || icon.equals(this.listViewIcon) || icon.equals(this.detailsViewIcon);
    }
}
