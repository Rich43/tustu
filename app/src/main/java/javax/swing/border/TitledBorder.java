package javax.swing.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.beans.ConstructorProperties;
import javax.swing.JLabel;
import javax.swing.UIManager;

/* loaded from: rt.jar:javax/swing/border/TitledBorder.class */
public class TitledBorder extends AbstractBorder {
    protected String title;
    protected Border border;
    protected int titlePosition;
    protected int titleJustification;
    protected Font titleFont;
    protected Color titleColor;
    private final JLabel label;
    public static final int DEFAULT_POSITION = 0;
    public static final int ABOVE_TOP = 1;
    public static final int TOP = 2;
    public static final int BELOW_TOP = 3;
    public static final int ABOVE_BOTTOM = 4;
    public static final int BOTTOM = 5;
    public static final int BELOW_BOTTOM = 6;
    public static final int DEFAULT_JUSTIFICATION = 0;
    public static final int LEFT = 1;
    public static final int CENTER = 2;
    public static final int RIGHT = 3;
    public static final int LEADING = 4;
    public static final int TRAILING = 5;
    protected static final int EDGE_SPACING = 2;
    protected static final int TEXT_SPACING = 2;
    protected static final int TEXT_INSET_H = 5;

    public TitledBorder(String str) {
        this(null, str, 4, 0, null, null);
    }

    public TitledBorder(Border border) {
        this(border, "", 4, 0, null, null);
    }

    public TitledBorder(Border border, String str) {
        this(border, str, 4, 0, null, null);
    }

    public TitledBorder(Border border, String str, int i2, int i3) {
        this(border, str, i2, i3, null, null);
    }

    public TitledBorder(Border border, String str, int i2, int i3, Font font) {
        this(border, str, i2, i3, font, null);
    }

    @ConstructorProperties({"border", "title", "titleJustification", "titlePosition", "titleFont", "titleColor"})
    public TitledBorder(Border border, String str, int i2, int i3, Font font, Color color) {
        this.title = str;
        this.border = border;
        this.titleFont = font;
        this.titleColor = color;
        setTitleJustification(i2);
        setTitlePosition(i3);
        this.label = new JLabel();
        this.label.setOpaque(false);
        this.label.putClientProperty("html", null);
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) throws IllegalArgumentException {
        Border border = getBorder();
        String title = getTitle();
        if (title != null && !title.isEmpty()) {
            int i6 = border instanceof TitledBorder ? 0 : 2;
            JLabel label = getLabel(component);
            Dimension preferredSize = label.getPreferredSize();
            Insets borderInsets = getBorderInsets(border, component, new Insets(0, 0, 0, 0));
            int i7 = i2 + i6;
            int i8 = i3 + i6;
            int i9 = (i4 - i6) - i6;
            int i10 = (i5 - i6) - i6;
            int i11 = i3;
            int i12 = preferredSize.height;
            int position = getPosition();
            switch (position) {
                case 1:
                    borderInsets.left = 0;
                    borderInsets.right = 0;
                    i8 += i12 - i6;
                    i10 -= i12 - i6;
                    break;
                case 2:
                    borderInsets.top = (i6 + (borderInsets.top / 2)) - (i12 / 2);
                    if (borderInsets.top < i6) {
                        i8 -= borderInsets.top;
                        i10 += borderInsets.top;
                        break;
                    } else {
                        i11 += borderInsets.top;
                        break;
                    }
                case 3:
                    i11 += borderInsets.top + i6;
                    break;
                case 4:
                    i11 += ((i5 - i12) - borderInsets.bottom) - i6;
                    break;
                case 5:
                    i11 += i5 - i12;
                    borderInsets.bottom = i6 + ((borderInsets.bottom - i12) / 2);
                    if (borderInsets.bottom < i6) {
                        i10 += borderInsets.bottom;
                        break;
                    } else {
                        i11 -= borderInsets.bottom;
                        break;
                    }
                case 6:
                    borderInsets.left = 0;
                    borderInsets.right = 0;
                    i11 += i5 - i12;
                    i10 -= i12 - i6;
                    break;
            }
            borderInsets.left += i6 + 5;
            borderInsets.right += i6 + 5;
            int i13 = i2;
            int i14 = (i4 - borderInsets.left) - borderInsets.right;
            if (i14 > preferredSize.width) {
                i14 = preferredSize.width;
            }
            switch (getJustification(component)) {
                case 1:
                    i13 += borderInsets.left;
                    break;
                case 2:
                    i13 += (i4 - i14) / 2;
                    break;
                case 3:
                    i13 += (i4 - borderInsets.right) - i14;
                    break;
            }
            if (border != null) {
                if (position != 2 && position != 5) {
                    border.paintBorder(component, graphics, i7, i8, i9, i10);
                } else {
                    Graphics graphicsCreate = graphics.create();
                    if (graphicsCreate instanceof Graphics2D) {
                        Graphics2D graphics2D = (Graphics2D) graphicsCreate;
                        Path2D.Float r0 = new Path2D.Float();
                        r0.append((Shape) new Rectangle(i7, i8, i9, i11 - i8), false);
                        r0.append((Shape) new Rectangle(i7, i11, (i13 - i7) - 2, i12), false);
                        r0.append((Shape) new Rectangle(i13 + i14 + 2, i11, (((i7 - i13) + i9) - i14) - 2, i12), false);
                        r0.append((Shape) new Rectangle(i7, i11 + i12, i9, ((i8 - i11) + i10) - i12), false);
                        graphics2D.clip(r0);
                    }
                    border.paintBorder(component, graphicsCreate, i7, i8, i9, i10);
                    graphicsCreate.dispose();
                }
            }
            graphics.translate(i13, i11);
            label.setSize(i14, i12);
            label.paint(graphics);
            graphics.translate(-i13, -i11);
            return;
        }
        if (border != null) {
            border.paintBorder(component, graphics, i2, i3, i4, i5);
        }
    }

    @Override // javax.swing.border.AbstractBorder
    public Insets getBorderInsets(Component component, Insets insets) {
        Border border = getBorder();
        Insets borderInsets = getBorderInsets(border, component, insets);
        String title = getTitle();
        if (title != null && !title.isEmpty()) {
            int i2 = border instanceof TitledBorder ? 0 : 2;
            Dimension preferredSize = getLabel(component).getPreferredSize();
            switch (getPosition()) {
                case 1:
                    borderInsets.top += preferredSize.height - i2;
                    break;
                case 2:
                    if (borderInsets.top < preferredSize.height) {
                        borderInsets.top = preferredSize.height - i2;
                        break;
                    }
                    break;
                case 3:
                    borderInsets.top += preferredSize.height;
                    break;
                case 4:
                    borderInsets.bottom += preferredSize.height;
                    break;
                case 5:
                    if (borderInsets.bottom < preferredSize.height) {
                        borderInsets.bottom = preferredSize.height - i2;
                        break;
                    }
                    break;
                case 6:
                    borderInsets.bottom += preferredSize.height - i2;
                    break;
            }
            borderInsets.top += i2 + 2;
            borderInsets.left += i2 + 2;
            borderInsets.right += i2 + 2;
            borderInsets.bottom += i2 + 2;
        }
        return borderInsets;
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public boolean isBorderOpaque() {
        return false;
    }

    public String getTitle() {
        return this.title;
    }

    public Border getBorder() {
        return this.border != null ? this.border : UIManager.getBorder("TitledBorder.border");
    }

    public int getTitlePosition() {
        return this.titlePosition;
    }

    public int getTitleJustification() {
        return this.titleJustification;
    }

    public Font getTitleFont() {
        return this.titleFont == null ? UIManager.getFont("TitledBorder.font") : this.titleFont;
    }

    public Color getTitleColor() {
        return this.titleColor == null ? UIManager.getColor("TitledBorder.titleColor") : this.titleColor;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public void setBorder(Border border) {
        this.border = border;
    }

    public void setTitlePosition(int i2) {
        switch (i2) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                this.titlePosition = i2;
                return;
            default:
                throw new IllegalArgumentException(i2 + " is not a valid title position.");
        }
    }

    public void setTitleJustification(int i2) {
        switch (i2) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                this.titleJustification = i2;
                return;
            default:
                throw new IllegalArgumentException(i2 + " is not a valid title justification.");
        }
    }

    public void setTitleFont(Font font) {
        this.titleFont = font;
    }

    public void setTitleColor(Color color) {
        this.titleColor = color;
    }

    public Dimension getMinimumSize(Component component) {
        Insets borderInsets = getBorderInsets(component);
        Dimension dimension = new Dimension(borderInsets.right + borderInsets.left, borderInsets.top + borderInsets.bottom);
        String title = getTitle();
        if (title != null && !title.isEmpty()) {
            Dimension preferredSize = getLabel(component).getPreferredSize();
            int position = getPosition();
            if ((position != 1 && position != 6) || dimension.width < preferredSize.width) {
                dimension.width += preferredSize.width;
            }
        }
        return dimension;
    }

    @Override // javax.swing.border.AbstractBorder
    public int getBaseline(Component component, int i2, int i3) throws IllegalArgumentException {
        if (component == null) {
            throw new NullPointerException("Must supply non-null component");
        }
        if (i2 < 0) {
            throw new IllegalArgumentException("Width must be >= 0");
        }
        if (i3 < 0) {
            throw new IllegalArgumentException("Height must be >= 0");
        }
        Border border = getBorder();
        String title = getTitle();
        if (title != null && !title.isEmpty()) {
            int i4 = border instanceof TitledBorder ? 0 : 2;
            JLabel label = getLabel(component);
            Dimension preferredSize = label.getPreferredSize();
            Insets borderInsets = getBorderInsets(border, component, new Insets(0, 0, 0, 0));
            int baseline = label.getBaseline(preferredSize.width, preferredSize.height);
            switch (getPosition()) {
                case 1:
                    return baseline;
                case 2:
                    borderInsets.top = i4 + ((borderInsets.top - preferredSize.height) / 2);
                    return borderInsets.top < i4 ? baseline : baseline + borderInsets.top;
                case 3:
                    return baseline + borderInsets.top + i4;
                case 4:
                    return (((baseline + i3) - preferredSize.height) - borderInsets.bottom) - i4;
                case 5:
                    borderInsets.bottom = i4 + ((borderInsets.bottom - preferredSize.height) / 2);
                    return borderInsets.bottom < i4 ? (baseline + i3) - preferredSize.height : ((baseline + i3) - preferredSize.height) + borderInsets.bottom;
                case 6:
                    return (baseline + i3) - preferredSize.height;
                default:
                    return -1;
            }
        }
        return -1;
    }

    @Override // javax.swing.border.AbstractBorder
    public Component.BaselineResizeBehavior getBaselineResizeBehavior(Component component) {
        super.getBaselineResizeBehavior(component);
        switch (getPosition()) {
            case 1:
            case 2:
            case 3:
                return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
            case 4:
            case 5:
            case 6:
                return Component.BaselineResizeBehavior.CONSTANT_DESCENT;
            default:
                return Component.BaselineResizeBehavior.OTHER;
        }
    }

    private int getPosition() {
        int titlePosition = getTitlePosition();
        if (titlePosition != 0) {
            return titlePosition;
        }
        Object obj = UIManager.get("TitledBorder.position");
        if (obj instanceof Integer) {
            int iIntValue = ((Integer) obj).intValue();
            if (0 < iIntValue && iIntValue <= 6) {
                return iIntValue;
            }
            return 2;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.equalsIgnoreCase("ABOVE_TOP")) {
                return 1;
            }
            if (str.equalsIgnoreCase("TOP")) {
                return 2;
            }
            if (str.equalsIgnoreCase("BELOW_TOP")) {
                return 3;
            }
            if (str.equalsIgnoreCase("ABOVE_BOTTOM")) {
                return 4;
            }
            if (str.equalsIgnoreCase("BOTTOM")) {
                return 5;
            }
            if (str.equalsIgnoreCase("BELOW_BOTTOM")) {
                return 6;
            }
            return 2;
        }
        return 2;
    }

    private int getJustification(Component component) {
        int titleJustification = getTitleJustification();
        if (titleJustification == 4 || titleJustification == 0) {
            return component.getComponentOrientation().isLeftToRight() ? 1 : 3;
        }
        if (titleJustification == 5) {
            return component.getComponentOrientation().isLeftToRight() ? 3 : 1;
        }
        return titleJustification;
    }

    protected Font getFont(Component component) {
        Font font;
        Font titleFont = getTitleFont();
        if (titleFont != null) {
            return titleFont;
        }
        if (component != null && (font = component.getFont()) != null) {
            return font;
        }
        return new Font(Font.DIALOG, 0, 12);
    }

    private Color getColor(Component component) {
        Color titleColor = getTitleColor();
        if (titleColor != null) {
            return titleColor;
        }
        if (component != null) {
            return component.getForeground();
        }
        return null;
    }

    private JLabel getLabel(Component component) throws IllegalArgumentException {
        this.label.setText(getTitle());
        this.label.setFont(getFont(component));
        this.label.setForeground(getColor(component));
        this.label.setComponentOrientation(component.getComponentOrientation());
        this.label.setEnabled(component.isEnabled());
        return this.label;
    }

    private static Insets getBorderInsets(Border border, Component component, Insets insets) {
        if (border == null) {
            insets.set(0, 0, 0, 0);
        } else if (border instanceof AbstractBorder) {
            insets = ((AbstractBorder) border).getBorderInsets(component, insets);
        } else {
            Insets borderInsets = border.getBorderInsets(component);
            insets.set(borderInsets.top, borderInsets.left, borderInsets.bottom, borderInsets.right);
        }
        return insets;
    }
}
