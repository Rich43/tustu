package javax.swing.tree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import sun.swing.DefaultLookup;

/* loaded from: rt.jar:javax/swing/tree/DefaultTreeCellRenderer.class */
public class DefaultTreeCellRenderer extends JLabel implements TreeCellRenderer {
    private JTree tree;
    protected boolean selected;
    protected boolean hasFocus;
    private boolean drawsFocusBorderAroundIcon;
    private boolean drawDashedFocusIndicator;
    private Color treeBGColor;
    private Color focusBGColor;
    protected transient Icon closedIcon;
    protected transient Icon leafIcon;
    protected transient Icon openIcon;
    protected Color textSelectionColor;
    protected Color textNonSelectionColor;
    protected Color backgroundSelectionColor;
    protected Color backgroundNonSelectionColor;
    protected Color borderSelectionColor;
    private boolean isDropCell;
    private boolean fillBackground;
    private boolean inited = true;

    @Override // javax.swing.JLabel, javax.swing.JComponent
    public void updateUI() {
        super.updateUI();
        if (!this.inited || (getLeafIcon() instanceof UIResource)) {
            setLeafIcon(DefaultLookup.getIcon(this, this.ui, "Tree.leafIcon"));
        }
        if (!this.inited || (getClosedIcon() instanceof UIResource)) {
            setClosedIcon(DefaultLookup.getIcon(this, this.ui, "Tree.closedIcon"));
        }
        if (!this.inited || (getOpenIcon() instanceof UIManager)) {
            setOpenIcon(DefaultLookup.getIcon(this, this.ui, "Tree.openIcon"));
        }
        if (!this.inited || (getTextSelectionColor() instanceof UIResource)) {
            setTextSelectionColor(DefaultLookup.getColor(this, this.ui, "Tree.selectionForeground"));
        }
        if (!this.inited || (getTextNonSelectionColor() instanceof UIResource)) {
            setTextNonSelectionColor(DefaultLookup.getColor(this, this.ui, "Tree.textForeground"));
        }
        if (!this.inited || (getBackgroundSelectionColor() instanceof UIResource)) {
            setBackgroundSelectionColor(DefaultLookup.getColor(this, this.ui, "Tree.selectionBackground"));
        }
        if (!this.inited || (getBackgroundNonSelectionColor() instanceof UIResource)) {
            setBackgroundNonSelectionColor(DefaultLookup.getColor(this, this.ui, "Tree.textBackground"));
        }
        if (!this.inited || (getBorderSelectionColor() instanceof UIResource)) {
            setBorderSelectionColor(DefaultLookup.getColor(this, this.ui, "Tree.selectionBorderColor"));
        }
        this.drawsFocusBorderAroundIcon = DefaultLookup.getBoolean(this, this.ui, "Tree.drawsFocusBorderAroundIcon", false);
        this.drawDashedFocusIndicator = DefaultLookup.getBoolean(this, this.ui, "Tree.drawDashedFocusIndicator", false);
        this.fillBackground = DefaultLookup.getBoolean(this, this.ui, "Tree.rendererFillBackground", true);
        Insets insets = DefaultLookup.getInsets(this, this.ui, "Tree.rendererMargins");
        if (insets != null) {
            setBorder(new EmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
        }
        setName("Tree.cellRenderer");
    }

    public Icon getDefaultOpenIcon() {
        return DefaultLookup.getIcon(this, this.ui, "Tree.openIcon");
    }

    public Icon getDefaultClosedIcon() {
        return DefaultLookup.getIcon(this, this.ui, "Tree.closedIcon");
    }

    public Icon getDefaultLeafIcon() {
        return DefaultLookup.getIcon(this, this.ui, "Tree.leafIcon");
    }

    public void setOpenIcon(Icon icon) {
        this.openIcon = icon;
    }

    public Icon getOpenIcon() {
        return this.openIcon;
    }

    public void setClosedIcon(Icon icon) {
        this.closedIcon = icon;
    }

    public Icon getClosedIcon() {
        return this.closedIcon;
    }

    public void setLeafIcon(Icon icon) {
        this.leafIcon = icon;
    }

    public Icon getLeafIcon() {
        return this.leafIcon;
    }

    public void setTextSelectionColor(Color color) {
        this.textSelectionColor = color;
    }

    public Color getTextSelectionColor() {
        return this.textSelectionColor;
    }

    public void setTextNonSelectionColor(Color color) {
        this.textNonSelectionColor = color;
    }

    public Color getTextNonSelectionColor() {
        return this.textNonSelectionColor;
    }

    public void setBackgroundSelectionColor(Color color) {
        this.backgroundSelectionColor = color;
    }

    public Color getBackgroundSelectionColor() {
        return this.backgroundSelectionColor;
    }

    public void setBackgroundNonSelectionColor(Color color) {
        this.backgroundNonSelectionColor = color;
    }

    public Color getBackgroundNonSelectionColor() {
        return this.backgroundNonSelectionColor;
    }

    public void setBorderSelectionColor(Color color) {
        this.borderSelectionColor = color;
    }

    public Color getBorderSelectionColor() {
        return this.borderSelectionColor;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void setFont(Font font) {
        if (font instanceof FontUIResource) {
            font = null;
        }
        super.setFont(font);
    }

    @Override // java.awt.Component, java.awt.MenuContainer
    public Font getFont() {
        Font font = super.getFont();
        if (font == null && this.tree != null) {
            font = this.tree.getFont();
        }
        return font;
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setBackground(Color color) {
        if (color instanceof ColorUIResource) {
            color = null;
        }
        super.setBackground(color);
    }

    public Component getTreeCellRendererComponent(JTree jTree, Object obj, boolean z2, boolean z3, boolean z4, int i2, boolean z5) {
        Color textNonSelectionColor;
        Icon closedIcon;
        String strConvertValueToText = jTree.convertValueToText(obj, z2, z3, z4, i2, z5);
        this.tree = jTree;
        this.hasFocus = z5;
        setText(strConvertValueToText);
        this.isDropCell = false;
        JTree.DropLocation dropLocation = jTree.getDropLocation();
        if (dropLocation != null && dropLocation.getChildIndex() == -1 && jTree.getRowForPath(dropLocation.getPath()) == i2) {
            Color color = DefaultLookup.getColor(this, this.ui, "Tree.dropCellForeground");
            if (color != null) {
                textNonSelectionColor = color;
            } else {
                textNonSelectionColor = getTextSelectionColor();
            }
            this.isDropCell = true;
        } else if (z2) {
            textNonSelectionColor = getTextSelectionColor();
        } else {
            textNonSelectionColor = getTextNonSelectionColor();
        }
        setForeground(textNonSelectionColor);
        if (z4) {
            closedIcon = getLeafIcon();
        } else if (z3) {
            closedIcon = getOpenIcon();
        } else {
            closedIcon = getClosedIcon();
        }
        if (!jTree.isEnabled()) {
            setEnabled(false);
            Icon disabledIcon = UIManager.getLookAndFeel().getDisabledIcon(jTree, closedIcon);
            if (disabledIcon != null) {
                closedIcon = disabledIcon;
            }
            setDisabledIcon(closedIcon);
        } else {
            setEnabled(true);
            setIcon(closedIcon);
        }
        setComponentOrientation(jTree.getComponentOrientation());
        this.selected = z2;
        return this;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        Color backgroundNonSelectionColor;
        if (this.isDropCell) {
            backgroundNonSelectionColor = DefaultLookup.getColor(this, this.ui, "Tree.dropCellBackground");
            if (backgroundNonSelectionColor == null) {
                backgroundNonSelectionColor = getBackgroundSelectionColor();
            }
        } else if (this.selected) {
            backgroundNonSelectionColor = getBackgroundSelectionColor();
        } else {
            backgroundNonSelectionColor = getBackgroundNonSelectionColor();
            if (backgroundNonSelectionColor == null) {
                backgroundNonSelectionColor = getBackground();
            }
        }
        int labelStart = -1;
        if (backgroundNonSelectionColor != null && this.fillBackground) {
            labelStart = getLabelStart();
            graphics.setColor(backgroundNonSelectionColor);
            if (getComponentOrientation().isLeftToRight()) {
                graphics.fillRect(labelStart, 0, getWidth() - labelStart, getHeight());
            } else {
                graphics.fillRect(0, 0, getWidth() - labelStart, getHeight());
            }
        }
        if (this.hasFocus) {
            if (this.drawsFocusBorderAroundIcon) {
                labelStart = 0;
            } else if (labelStart == -1) {
                labelStart = getLabelStart();
            }
            if (getComponentOrientation().isLeftToRight()) {
                paintFocus(graphics, labelStart, 0, getWidth() - labelStart, getHeight(), backgroundNonSelectionColor);
            } else {
                paintFocus(graphics, 0, 0, getWidth() - labelStart, getHeight(), backgroundNonSelectionColor);
            }
        }
        super.paint(graphics);
    }

    private void paintFocus(Graphics graphics, int i2, int i3, int i4, int i5, Color color) {
        Color borderSelectionColor = getBorderSelectionColor();
        if (borderSelectionColor != null && (this.selected || !this.drawDashedFocusIndicator)) {
            graphics.setColor(borderSelectionColor);
            graphics.drawRect(i2, i3, i4 - 1, i5 - 1);
        }
        if (this.drawDashedFocusIndicator && color != null) {
            if (this.treeBGColor != color) {
                this.treeBGColor = color;
                this.focusBGColor = new Color(color.getRGB() ^ (-1));
            }
            graphics.setColor(this.focusBGColor);
            BasicGraphicsUtils.drawDashedRect(graphics, i2, i3, i4, i5);
        }
    }

    private int getLabelStart() {
        Icon icon = getIcon();
        if (icon != null && getText() != null) {
            return icon.getIconWidth() + Math.max(0, getIconTextGap() - 1);
        }
        return 0;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        if (preferredSize != null) {
            preferredSize = new Dimension(preferredSize.width + 3, preferredSize.height);
        }
        return preferredSize;
    }

    @Override // java.awt.Container, java.awt.Component
    public void validate() {
    }

    @Override // java.awt.Container, java.awt.Component
    public void invalidate() {
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void revalidate() {
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void repaint(long j2, int i2, int i3, int i4, int i5) {
    }

    @Override // javax.swing.JComponent
    public void repaint(Rectangle rectangle) {
    }

    @Override // java.awt.Component
    public void repaint() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // java.awt.Component
    public void firePropertyChange(String str, Object obj, Object obj2) {
        if (str == "text" || ((str == "font" || str == "foreground") && obj != obj2 && getClientProperty("html") != null)) {
            super.firePropertyChange(str, obj, obj2);
        }
    }

    @Override // java.awt.Component
    public void firePropertyChange(String str, byte b2, byte b3) {
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void firePropertyChange(String str, char c2, char c3) {
    }

    @Override // java.awt.Component
    public void firePropertyChange(String str, short s2, short s3) {
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void firePropertyChange(String str, int i2, int i3) {
    }

    @Override // java.awt.Component
    public void firePropertyChange(String str, long j2, long j3) {
    }

    @Override // java.awt.Component
    public void firePropertyChange(String str, float f2, float f3) {
    }

    @Override // java.awt.Component
    public void firePropertyChange(String str, double d2, double d3) {
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void firePropertyChange(String str, boolean z2, boolean z3) {
    }
}
