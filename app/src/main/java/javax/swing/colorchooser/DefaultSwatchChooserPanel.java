package javax.swing.colorchooser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import javax.accessibility.AccessibleContext;
import javax.swing.Icon;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

/* loaded from: rt.jar:javax/swing/colorchooser/DefaultSwatchChooserPanel.class */
class DefaultSwatchChooserPanel extends AbstractColorChooserPanel {
    SwatchPanel swatchPanel;
    RecentSwatchPanel recentSwatchPanel;
    MouseListener mainSwatchListener;
    MouseListener recentSwatchListener;
    private KeyListener mainSwatchKeyListener;
    private KeyListener recentSwatchKeyListener;

    public DefaultSwatchChooserPanel() {
        setInheritsPopupMenu(true);
    }

    @Override // javax.swing.colorchooser.AbstractColorChooserPanel
    public String getDisplayName() {
        return UIManager.getString("ColorChooser.swatchesNameText", getLocale());
    }

    @Override // javax.swing.colorchooser.AbstractColorChooserPanel
    public int getMnemonic() {
        return getInt("ColorChooser.swatchesMnemonic", -1);
    }

    @Override // javax.swing.colorchooser.AbstractColorChooserPanel
    public int getDisplayedMnemonicIndex() {
        return getInt("ColorChooser.swatchesDisplayedMnemonicIndex", -1);
    }

    @Override // javax.swing.colorchooser.AbstractColorChooserPanel
    public Icon getSmallDisplayIcon() {
        return null;
    }

    @Override // javax.swing.colorchooser.AbstractColorChooserPanel
    public Icon getLargeDisplayIcon() {
        return null;
    }

    @Override // javax.swing.colorchooser.AbstractColorChooserPanel
    public void installChooserPanel(JColorChooser jColorChooser) {
        super.installChooserPanel(jColorChooser);
    }

    @Override // javax.swing.colorchooser.AbstractColorChooserPanel
    protected void buildChooser() {
        String string = UIManager.getString("ColorChooser.swatchesRecentText", getLocale());
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        JPanel jPanel = new JPanel(gridBagLayout);
        this.swatchPanel = new MainSwatchPanel();
        this.swatchPanel.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, getDisplayName());
        this.swatchPanel.setInheritsPopupMenu(true);
        this.recentSwatchPanel = new RecentSwatchPanel();
        this.recentSwatchPanel.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, string);
        this.mainSwatchKeyListener = new MainSwatchKeyListener();
        this.mainSwatchListener = new MainSwatchListener();
        this.swatchPanel.addMouseListener(this.mainSwatchListener);
        this.swatchPanel.addKeyListener(this.mainSwatchKeyListener);
        this.recentSwatchListener = new RecentSwatchListener();
        this.recentSwatchKeyListener = new RecentSwatchKeyListener();
        this.recentSwatchPanel.addMouseListener(this.recentSwatchListener);
        this.recentSwatchPanel.addKeyListener(this.recentSwatchKeyListener);
        JPanel jPanel2 = new JPanel(new BorderLayout());
        CompoundBorder compoundBorder = new CompoundBorder(new LineBorder(Color.black), new LineBorder(Color.white));
        jPanel2.setBorder(compoundBorder);
        jPanel2.add(this.swatchPanel, BorderLayout.CENTER);
        gridBagConstraints.anchor = 25;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 2;
        Insets insets = gridBagConstraints.insets;
        gridBagConstraints.insets = new Insets(0, 0, 0, 10);
        jPanel.add(jPanel2, gridBagConstraints);
        gridBagConstraints.insets = insets;
        this.recentSwatchPanel.setInheritsPopupMenu(true);
        JPanel jPanel3 = new JPanel(new BorderLayout());
        jPanel3.setBorder(compoundBorder);
        jPanel3.setInheritsPopupMenu(true);
        jPanel3.add(this.recentSwatchPanel, BorderLayout.CENTER);
        JLabel jLabel = new JLabel(string);
        jLabel.setLabelFor(this.recentSwatchPanel);
        gridBagConstraints.gridwidth = 0;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.weighty = 1.0d;
        jPanel.add(jLabel, gridBagConstraints);
        gridBagConstraints.weighty = 0.0d;
        gridBagConstraints.gridheight = 0;
        gridBagConstraints.insets = new Insets(0, 0, 0, 2);
        jPanel.add(jPanel3, gridBagConstraints);
        jPanel.setInheritsPopupMenu(true);
        add(jPanel);
    }

    @Override // javax.swing.colorchooser.AbstractColorChooserPanel
    public void uninstallChooserPanel(JColorChooser jColorChooser) {
        super.uninstallChooserPanel(jColorChooser);
        this.swatchPanel.removeMouseListener(this.mainSwatchListener);
        this.swatchPanel.removeKeyListener(this.mainSwatchKeyListener);
        this.recentSwatchPanel.removeMouseListener(this.recentSwatchListener);
        this.recentSwatchPanel.removeKeyListener(this.recentSwatchKeyListener);
        this.swatchPanel = null;
        this.recentSwatchPanel = null;
        this.mainSwatchListener = null;
        this.mainSwatchKeyListener = null;
        this.recentSwatchListener = null;
        this.recentSwatchKeyListener = null;
        removeAll();
    }

    @Override // javax.swing.colorchooser.AbstractColorChooserPanel
    public void updateChooser() {
    }

    /* loaded from: rt.jar:javax/swing/colorchooser/DefaultSwatchChooserPanel$RecentSwatchKeyListener.class */
    private class RecentSwatchKeyListener extends KeyAdapter {
        private RecentSwatchKeyListener() {
        }

        @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
        public void keyPressed(KeyEvent keyEvent) {
            if (32 == keyEvent.getKeyCode()) {
                DefaultSwatchChooserPanel.this.setSelectedColor(DefaultSwatchChooserPanel.this.recentSwatchPanel.getSelectedColor());
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/colorchooser/DefaultSwatchChooserPanel$MainSwatchKeyListener.class */
    private class MainSwatchKeyListener extends KeyAdapter {
        private MainSwatchKeyListener() {
        }

        @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
        public void keyPressed(KeyEvent keyEvent) {
            if (32 == keyEvent.getKeyCode()) {
                Color selectedColor = DefaultSwatchChooserPanel.this.swatchPanel.getSelectedColor();
                DefaultSwatchChooserPanel.this.setSelectedColor(selectedColor);
                DefaultSwatchChooserPanel.this.recentSwatchPanel.setMostRecentColor(selectedColor);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/colorchooser/DefaultSwatchChooserPanel$RecentSwatchListener.class */
    class RecentSwatchListener extends MouseAdapter implements Serializable {
        RecentSwatchListener() {
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            if (DefaultSwatchChooserPanel.this.isEnabled()) {
                Color colorForLocation = DefaultSwatchChooserPanel.this.recentSwatchPanel.getColorForLocation(mouseEvent.getX(), mouseEvent.getY());
                DefaultSwatchChooserPanel.this.recentSwatchPanel.setSelectedColorFromLocation(mouseEvent.getX(), mouseEvent.getY());
                DefaultSwatchChooserPanel.this.setSelectedColor(colorForLocation);
                DefaultSwatchChooserPanel.this.recentSwatchPanel.requestFocusInWindow();
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/colorchooser/DefaultSwatchChooserPanel$MainSwatchListener.class */
    class MainSwatchListener extends MouseAdapter implements Serializable {
        MainSwatchListener() {
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            if (DefaultSwatchChooserPanel.this.isEnabled()) {
                Color colorForLocation = DefaultSwatchChooserPanel.this.swatchPanel.getColorForLocation(mouseEvent.getX(), mouseEvent.getY());
                DefaultSwatchChooserPanel.this.setSelectedColor(colorForLocation);
                DefaultSwatchChooserPanel.this.swatchPanel.setSelectedColorFromLocation(mouseEvent.getX(), mouseEvent.getY());
                DefaultSwatchChooserPanel.this.recentSwatchPanel.setMostRecentColor(colorForLocation);
                DefaultSwatchChooserPanel.this.swatchPanel.requestFocusInWindow();
            }
        }
    }
}
