package sun.swing;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.View;

/* loaded from: rt.jar:sun/swing/MenuItemLayoutHelper.class */
public class MenuItemLayoutHelper {
    public static final StringUIClientPropertyKey MAX_ARROW_WIDTH;
    public static final StringUIClientPropertyKey MAX_CHECK_WIDTH;
    public static final StringUIClientPropertyKey MAX_ICON_WIDTH;
    public static final StringUIClientPropertyKey MAX_TEXT_WIDTH;
    public static final StringUIClientPropertyKey MAX_ACC_WIDTH;
    public static final StringUIClientPropertyKey MAX_LABEL_WIDTH;
    private JMenuItem mi;
    private JComponent miParent;
    private Font font;
    private Font accFont;
    private FontMetrics fm;
    private FontMetrics accFm;
    private Icon icon;
    private Icon checkIcon;
    private Icon arrowIcon;
    private String text;
    private String accText;
    private boolean isColumnLayout;
    private boolean useCheckAndArrow;
    private boolean isLeftToRight;
    private boolean isTopLevelMenu;
    private View htmlView;
    private int verticalAlignment;
    private int horizontalAlignment;
    private int verticalTextPosition;
    private int horizontalTextPosition;
    private int gap;
    private int leadingGap;
    private int afterCheckIconGap;
    private int minTextOffset;
    private int leftTextExtraWidth;
    private Rectangle viewRect;
    private RectSize iconSize;
    private RectSize textSize;
    private RectSize accSize;
    private RectSize checkSize;
    private RectSize arrowSize;
    private RectSize labelSize;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !MenuItemLayoutHelper.class.desiredAssertionStatus();
        MAX_ARROW_WIDTH = new StringUIClientPropertyKey("maxArrowWidth");
        MAX_CHECK_WIDTH = new StringUIClientPropertyKey("maxCheckWidth");
        MAX_ICON_WIDTH = new StringUIClientPropertyKey("maxIconWidth");
        MAX_TEXT_WIDTH = new StringUIClientPropertyKey("maxTextWidth");
        MAX_ACC_WIDTH = new StringUIClientPropertyKey("maxAccWidth");
        MAX_LABEL_WIDTH = new StringUIClientPropertyKey("maxLabelWidth");
    }

    protected MenuItemLayoutHelper() {
    }

    public MenuItemLayoutHelper(JMenuItem jMenuItem, Icon icon, Icon icon2, Rectangle rectangle, int i2, String str, boolean z2, Font font, Font font2, boolean z3, String str2) {
        reset(jMenuItem, icon, icon2, rectangle, i2, str, z2, font, font2, z3, str2);
    }

    protected void reset(JMenuItem jMenuItem, Icon icon, Icon icon2, Rectangle rectangle, int i2, String str, boolean z2, Font font, Font font2, boolean z3, String str2) {
        this.mi = jMenuItem;
        this.miParent = getMenuItemParent(jMenuItem);
        this.accText = getAccText(str);
        this.verticalAlignment = jMenuItem.getVerticalAlignment();
        this.horizontalAlignment = jMenuItem.getHorizontalAlignment();
        this.verticalTextPosition = jMenuItem.getVerticalTextPosition();
        this.horizontalTextPosition = jMenuItem.getHorizontalTextPosition();
        this.useCheckAndArrow = z3;
        this.font = font;
        this.accFont = font2;
        this.fm = jMenuItem.getFontMetrics(font);
        this.accFm = jMenuItem.getFontMetrics(font2);
        this.isLeftToRight = z2;
        this.isColumnLayout = isColumnLayout(z2, this.horizontalAlignment, this.horizontalTextPosition, this.verticalTextPosition);
        this.isTopLevelMenu = this.miParent == null;
        this.checkIcon = icon;
        this.icon = getIcon(str2);
        this.arrowIcon = icon2;
        this.text = jMenuItem.getText();
        this.gap = i2;
        this.afterCheckIconGap = getAfterCheckIconGap(str2);
        this.minTextOffset = getMinTextOffset(str2);
        this.htmlView = (View) jMenuItem.getClientProperty("html");
        this.viewRect = rectangle;
        this.iconSize = new RectSize();
        this.textSize = new RectSize();
        this.accSize = new RectSize();
        this.checkSize = new RectSize();
        this.arrowSize = new RectSize();
        this.labelSize = new RectSize();
        calcExtraWidths();
        calcWidthsAndHeights();
        setOriginalWidths();
        calcMaxWidths();
        this.leadingGap = getLeadingGap(str2);
        calcMaxTextOffset(rectangle);
    }

    private void calcExtraWidths() {
        this.leftTextExtraWidth = getLeftExtraWidth(this.text);
    }

    private int getLeftExtraWidth(String str) {
        int leftSideBearing = SwingUtilities2.getLeftSideBearing(this.mi, this.fm, str);
        if (leftSideBearing < 0) {
            return -leftSideBearing;
        }
        return 0;
    }

    private void setOriginalWidths() {
        this.iconSize.origWidth = this.iconSize.width;
        this.textSize.origWidth = this.textSize.width;
        this.accSize.origWidth = this.accSize.width;
        this.checkSize.origWidth = this.checkSize.width;
        this.arrowSize.origWidth = this.arrowSize.width;
    }

    private String getAccText(String str) {
        String str2 = "";
        KeyStroke accelerator = this.mi.getAccelerator();
        if (accelerator != null) {
            int modifiers = accelerator.getModifiers();
            if (modifiers > 0) {
                str2 = KeyEvent.getKeyModifiersText(modifiers) + str;
            }
            int keyCode = accelerator.getKeyCode();
            if (keyCode != 0) {
                str2 = str2 + KeyEvent.getKeyText(keyCode);
            } else {
                str2 = str2 + accelerator.getKeyChar();
            }
        }
        return str2;
    }

    private Icon getIcon(String str) {
        Icon icon = null;
        MenuItemCheckIconFactory menuItemCheckIconFactory = (MenuItemCheckIconFactory) UIManager.get(str + ".checkIconFactory");
        if (!this.isColumnLayout || !this.useCheckAndArrow || menuItemCheckIconFactory == null || !menuItemCheckIconFactory.isCompatible(this.checkIcon, str)) {
            icon = this.mi.getIcon();
        }
        return icon;
    }

    private int getMinTextOffset(String str) {
        int iIntValue = 0;
        Object obj = UIManager.get(str + ".minimumTextOffset");
        if (obj instanceof Integer) {
            iIntValue = ((Integer) obj).intValue();
        }
        return iIntValue;
    }

    private int getAfterCheckIconGap(String str) {
        int iIntValue = this.gap;
        Object obj = UIManager.get(str + ".afterCheckIconGap");
        if (obj instanceof Integer) {
            iIntValue = ((Integer) obj).intValue();
        }
        return iIntValue;
    }

    private int getLeadingGap(String str) {
        if (this.checkSize.getMaxWidth() > 0) {
            return getCheckOffset(str);
        }
        return this.gap;
    }

    private int getCheckOffset(String str) {
        int iIntValue = this.gap;
        Object obj = UIManager.get(str + ".checkIconOffset");
        if (obj instanceof Integer) {
            iIntValue = ((Integer) obj).intValue();
        }
        return iIntValue;
    }

    protected void calcWidthsAndHeights() {
        if (this.icon != null) {
            this.iconSize.width = this.icon.getIconWidth();
            this.iconSize.height = this.icon.getIconHeight();
        }
        if (!this.accText.equals("")) {
            this.accSize.width = SwingUtilities2.stringWidth(this.mi, this.accFm, this.accText);
            this.accSize.height = this.accFm.getHeight();
        }
        if (this.text == null) {
            this.text = "";
        } else if (!this.text.equals("")) {
            if (this.htmlView != null) {
                this.textSize.width = (int) this.htmlView.getPreferredSpan(0);
                this.textSize.height = (int) this.htmlView.getPreferredSpan(1);
            } else {
                this.textSize.width = SwingUtilities2.stringWidth(this.mi, this.fm, this.text);
                this.textSize.height = this.fm.getHeight();
            }
        }
        if (this.useCheckAndArrow) {
            if (this.checkIcon != null) {
                this.checkSize.width = this.checkIcon.getIconWidth();
                this.checkSize.height = this.checkIcon.getIconHeight();
            }
            if (this.arrowIcon != null) {
                this.arrowSize.width = this.arrowIcon.getIconWidth();
                this.arrowSize.height = this.arrowIcon.getIconHeight();
            }
        }
        if (!this.isColumnLayout) {
            Rectangle rectangle = new Rectangle();
            Rectangle rectangle2 = new Rectangle();
            SwingUtilities.layoutCompoundLabel(this.mi, this.fm, this.text, this.icon, this.verticalAlignment, this.horizontalAlignment, this.verticalTextPosition, this.horizontalTextPosition, this.viewRect, rectangle2, rectangle, this.gap);
            rectangle.width += this.leftTextExtraWidth;
            Rectangle rectangleUnion = rectangle2.union(rectangle);
            this.labelSize.height = rectangleUnion.height;
            this.labelSize.width = rectangleUnion.width;
            return;
        }
        this.labelSize.width = this.iconSize.width + this.textSize.width + this.gap;
        this.labelSize.height = max(this.checkSize.height, this.iconSize.height, this.textSize.height, this.accSize.height, this.arrowSize.height);
    }

    protected void calcMaxWidths() {
        calcMaxWidth(this.checkSize, MAX_CHECK_WIDTH);
        calcMaxWidth(this.arrowSize, MAX_ARROW_WIDTH);
        calcMaxWidth(this.accSize, MAX_ACC_WIDTH);
        if (this.isColumnLayout) {
            calcMaxWidth(this.iconSize, MAX_ICON_WIDTH);
            calcMaxWidth(this.textSize, MAX_TEXT_WIDTH);
            int i2 = this.gap;
            if (this.iconSize.getMaxWidth() == 0 || this.textSize.getMaxWidth() == 0) {
                i2 = 0;
            }
            this.labelSize.maxWidth = calcMaxValue(MAX_LABEL_WIDTH, this.iconSize.maxWidth + this.textSize.maxWidth + i2);
            return;
        }
        this.iconSize.maxWidth = getParentIntProperty(MAX_ICON_WIDTH);
        calcMaxWidth(this.labelSize, MAX_LABEL_WIDTH);
        int i3 = this.labelSize.maxWidth - this.iconSize.maxWidth;
        if (this.iconSize.maxWidth > 0) {
            i3 -= this.gap;
        }
        this.textSize.maxWidth = calcMaxValue(MAX_TEXT_WIDTH, i3);
    }

    protected void calcMaxWidth(RectSize rectSize, Object obj) {
        rectSize.maxWidth = calcMaxValue(obj, rectSize.width);
    }

    protected int calcMaxValue(Object obj, int i2) {
        int parentIntProperty = getParentIntProperty(obj);
        if (i2 > parentIntProperty) {
            if (this.miParent != null) {
                this.miParent.putClientProperty(obj, Integer.valueOf(i2));
            }
            return i2;
        }
        return parentIntProperty;
    }

    protected int getParentIntProperty(Object obj) {
        Object clientProperty = null;
        if (this.miParent != null) {
            clientProperty = this.miParent.getClientProperty(obj);
        }
        if (clientProperty == null || !(clientProperty instanceof Integer)) {
            clientProperty = 0;
        }
        return ((Integer) clientProperty).intValue();
    }

    public static boolean isColumnLayout(boolean z2, JMenuItem jMenuItem) {
        if ($assertionsDisabled || jMenuItem != null) {
            return isColumnLayout(z2, jMenuItem.getHorizontalAlignment(), jMenuItem.getHorizontalTextPosition(), jMenuItem.getVerticalTextPosition());
        }
        throw new AssertionError();
    }

    public static boolean isColumnLayout(boolean z2, int i2, int i3, int i4) {
        if (i4 != 0) {
            return false;
        }
        if (z2) {
            if (i2 != 10 && i2 != 2) {
                return false;
            }
            if (i3 != 11 && i3 != 4) {
                return false;
            }
            return true;
        }
        if (i2 != 10 && i2 != 4) {
            return false;
        }
        if (i3 != 11 && i3 != 2) {
            return false;
        }
        return true;
    }

    private void calcMaxTextOffset(Rectangle rectangle) {
        if (!this.isColumnLayout || !this.isLeftToRight) {
            return;
        }
        int i2 = rectangle.f12372x + this.leadingGap + this.checkSize.maxWidth + this.afterCheckIconGap + this.iconSize.maxWidth + this.gap;
        if (this.checkSize.maxWidth == 0) {
            i2 -= this.afterCheckIconGap;
        }
        if (this.iconSize.maxWidth == 0) {
            i2 -= this.gap;
        }
        if (i2 < this.minTextOffset) {
            i2 = this.minTextOffset;
        }
        calcMaxValue(SwingUtilities2.BASICMENUITEMUI_MAX_TEXT_OFFSET, i2);
    }

    public LayoutResult layoutMenuItem() {
        LayoutResult layoutResultCreateLayoutResult = createLayoutResult();
        prepareForLayout(layoutResultCreateLayoutResult);
        if (isColumnLayout()) {
            if (isLeftToRight()) {
                doLTRColumnLayout(layoutResultCreateLayoutResult, getLTRColumnAlignment());
            } else {
                doRTLColumnLayout(layoutResultCreateLayoutResult, getRTLColumnAlignment());
            }
        } else if (isLeftToRight()) {
            doLTRComplexLayout(layoutResultCreateLayoutResult, getLTRColumnAlignment());
        } else {
            doRTLComplexLayout(layoutResultCreateLayoutResult, getRTLColumnAlignment());
        }
        alignAccCheckAndArrowVertically(layoutResultCreateLayoutResult);
        return layoutResultCreateLayoutResult;
    }

    private LayoutResult createLayoutResult() {
        return new LayoutResult(new Rectangle(this.iconSize.width, this.iconSize.height), new Rectangle(this.textSize.width, this.textSize.height), new Rectangle(this.accSize.width, this.accSize.height), new Rectangle(this.checkSize.width, this.checkSize.height), new Rectangle(this.arrowSize.width, this.arrowSize.height), new Rectangle(this.labelSize.width, this.labelSize.height));
    }

    public ColumnAlignment getLTRColumnAlignment() {
        return ColumnAlignment.LEFT_ALIGNMENT;
    }

    public ColumnAlignment getRTLColumnAlignment() {
        return ColumnAlignment.RIGHT_ALIGNMENT;
    }

    protected void prepareForLayout(LayoutResult layoutResult) {
        layoutResult.checkRect.width = this.checkSize.maxWidth;
        layoutResult.accRect.width = this.accSize.maxWidth;
        layoutResult.arrowRect.width = this.arrowSize.maxWidth;
    }

    private void alignAccCheckAndArrowVertically(LayoutResult layoutResult) {
        layoutResult.accRect.f12373y = (int) ((layoutResult.labelRect.f12373y + (layoutResult.labelRect.height / 2.0f)) - (layoutResult.accRect.height / 2.0f));
        fixVerticalAlignment(layoutResult, layoutResult.accRect);
        if (!this.useCheckAndArrow) {
            return;
        }
        layoutResult.arrowRect.f12373y = (int) ((layoutResult.labelRect.f12373y + (layoutResult.labelRect.height / 2.0f)) - (layoutResult.arrowRect.height / 2.0f));
        layoutResult.checkRect.f12373y = (int) ((layoutResult.labelRect.f12373y + (layoutResult.labelRect.height / 2.0f)) - (layoutResult.checkRect.height / 2.0f));
        fixVerticalAlignment(layoutResult, layoutResult.arrowRect);
        fixVerticalAlignment(layoutResult, layoutResult.checkRect);
    }

    private void fixVerticalAlignment(LayoutResult layoutResult, Rectangle rectangle) {
        int i2 = 0;
        if (rectangle.f12373y < this.viewRect.f12373y) {
            i2 = this.viewRect.f12373y - rectangle.f12373y;
        } else if (rectangle.f12373y + rectangle.height > this.viewRect.f12373y + this.viewRect.height) {
            i2 = ((this.viewRect.f12373y + this.viewRect.height) - rectangle.f12373y) - rectangle.height;
        }
        if (i2 == 0) {
            return;
        }
        layoutResult.checkRect.f12373y += i2;
        layoutResult.iconRect.f12373y += i2;
        layoutResult.textRect.f12373y += i2;
        layoutResult.accRect.f12373y += i2;
        layoutResult.arrowRect.f12373y += i2;
        layoutResult.labelRect.f12373y += i2;
    }

    private void doLTRColumnLayout(LayoutResult layoutResult, ColumnAlignment columnAlignment) {
        layoutResult.iconRect.width = this.iconSize.maxWidth;
        layoutResult.textRect.width = this.textSize.maxWidth;
        calcXPositionsLTR(this.viewRect.f12372x, this.leadingGap, this.gap, layoutResult.checkRect, layoutResult.iconRect, layoutResult.textRect);
        if (layoutResult.checkRect.width > 0) {
            layoutResult.iconRect.f12372x += this.afterCheckIconGap - this.gap;
            layoutResult.textRect.f12372x += this.afterCheckIconGap - this.gap;
        }
        calcXPositionsRTL(this.viewRect.f12372x + this.viewRect.width, this.leadingGap, this.gap, layoutResult.arrowRect, layoutResult.accRect);
        int i2 = layoutResult.textRect.f12372x - this.viewRect.f12372x;
        if (!this.isTopLevelMenu && i2 < this.minTextOffset) {
            layoutResult.textRect.f12372x += this.minTextOffset - i2;
        }
        alignRects(layoutResult, columnAlignment);
        calcTextAndIconYPositions(layoutResult);
        layoutResult.setLabelRect(layoutResult.textRect.union(layoutResult.iconRect));
    }

    private void doLTRComplexLayout(LayoutResult layoutResult, ColumnAlignment columnAlignment) {
        layoutResult.labelRect.width = this.labelSize.maxWidth;
        calcXPositionsLTR(this.viewRect.f12372x, this.leadingGap, this.gap, layoutResult.checkRect, layoutResult.labelRect);
        if (layoutResult.checkRect.width > 0) {
            layoutResult.labelRect.f12372x += this.afterCheckIconGap - this.gap;
        }
        calcXPositionsRTL(this.viewRect.f12372x + this.viewRect.width, this.leadingGap, this.gap, layoutResult.arrowRect, layoutResult.accRect);
        int i2 = layoutResult.labelRect.f12372x - this.viewRect.f12372x;
        if (!this.isTopLevelMenu && i2 < this.minTextOffset) {
            layoutResult.labelRect.f12372x += this.minTextOffset - i2;
        }
        alignRects(layoutResult, columnAlignment);
        calcLabelYPosition(layoutResult);
        layoutIconAndTextInLabelRect(layoutResult);
    }

    private void doRTLColumnLayout(LayoutResult layoutResult, ColumnAlignment columnAlignment) {
        layoutResult.iconRect.width = this.iconSize.maxWidth;
        layoutResult.textRect.width = this.textSize.maxWidth;
        calcXPositionsRTL(this.viewRect.f12372x + this.viewRect.width, this.leadingGap, this.gap, layoutResult.checkRect, layoutResult.iconRect, layoutResult.textRect);
        if (layoutResult.checkRect.width > 0) {
            layoutResult.iconRect.f12372x -= this.afterCheckIconGap - this.gap;
            layoutResult.textRect.f12372x -= this.afterCheckIconGap - this.gap;
        }
        calcXPositionsLTR(this.viewRect.f12372x, this.leadingGap, this.gap, layoutResult.arrowRect, layoutResult.accRect);
        int i2 = (this.viewRect.f12372x + this.viewRect.width) - (layoutResult.textRect.f12372x + layoutResult.textRect.width);
        if (!this.isTopLevelMenu && i2 < this.minTextOffset) {
            layoutResult.textRect.f12372x -= this.minTextOffset - i2;
        }
        alignRects(layoutResult, columnAlignment);
        calcTextAndIconYPositions(layoutResult);
        layoutResult.setLabelRect(layoutResult.textRect.union(layoutResult.iconRect));
    }

    private void doRTLComplexLayout(LayoutResult layoutResult, ColumnAlignment columnAlignment) {
        layoutResult.labelRect.width = this.labelSize.maxWidth;
        calcXPositionsRTL(this.viewRect.f12372x + this.viewRect.width, this.leadingGap, this.gap, layoutResult.checkRect, layoutResult.labelRect);
        if (layoutResult.checkRect.width > 0) {
            layoutResult.labelRect.f12372x -= this.afterCheckIconGap - this.gap;
        }
        calcXPositionsLTR(this.viewRect.f12372x, this.leadingGap, this.gap, layoutResult.arrowRect, layoutResult.accRect);
        int i2 = (this.viewRect.f12372x + this.viewRect.width) - (layoutResult.labelRect.f12372x + layoutResult.labelRect.width);
        if (!this.isTopLevelMenu && i2 < this.minTextOffset) {
            layoutResult.labelRect.f12372x -= this.minTextOffset - i2;
        }
        alignRects(layoutResult, columnAlignment);
        calcLabelYPosition(layoutResult);
        layoutIconAndTextInLabelRect(layoutResult);
    }

    private void alignRects(LayoutResult layoutResult, ColumnAlignment columnAlignment) {
        alignRect(layoutResult.checkRect, columnAlignment.getCheckAlignment(), this.checkSize.getOrigWidth());
        alignRect(layoutResult.iconRect, columnAlignment.getIconAlignment(), this.iconSize.getOrigWidth());
        alignRect(layoutResult.textRect, columnAlignment.getTextAlignment(), this.textSize.getOrigWidth());
        alignRect(layoutResult.accRect, columnAlignment.getAccAlignment(), this.accSize.getOrigWidth());
        alignRect(layoutResult.arrowRect, columnAlignment.getArrowAlignment(), this.arrowSize.getOrigWidth());
    }

    private void alignRect(Rectangle rectangle, int i2, int i3) {
        if (i2 == 4) {
            rectangle.f12372x = (rectangle.f12372x + rectangle.width) - i3;
        }
        rectangle.width = i3;
    }

    protected void layoutIconAndTextInLabelRect(LayoutResult layoutResult) {
        layoutResult.setTextRect(new Rectangle());
        layoutResult.setIconRect(new Rectangle());
        SwingUtilities.layoutCompoundLabel(this.mi, this.fm, this.text, this.icon, this.verticalAlignment, this.horizontalAlignment, this.verticalTextPosition, this.horizontalTextPosition, layoutResult.labelRect, layoutResult.iconRect, layoutResult.textRect, this.gap);
    }

    private void calcXPositionsLTR(int i2, int i3, int i4, Rectangle... rectangleArr) {
        int i5 = i2 + i3;
        for (Rectangle rectangle : rectangleArr) {
            rectangle.f12372x = i5;
            if (rectangle.width > 0) {
                i5 += rectangle.width + i4;
            }
        }
    }

    private void calcXPositionsRTL(int i2, int i3, int i4, Rectangle... rectangleArr) {
        int i5 = i2 - i3;
        for (Rectangle rectangle : rectangleArr) {
            rectangle.f12372x = i5 - rectangle.width;
            if (rectangle.width > 0) {
                i5 -= rectangle.width + i4;
            }
        }
    }

    private void calcTextAndIconYPositions(LayoutResult layoutResult) {
        if (this.verticalAlignment != 1) {
            if (this.verticalAlignment != 0) {
                if (this.verticalAlignment != 3) {
                    return;
                }
                layoutResult.textRect.f12373y = (int) (((this.viewRect.f12373y + this.viewRect.height) - (layoutResult.labelRect.height / 2.0f)) - (layoutResult.textRect.height / 2.0f));
                layoutResult.iconRect.f12373y = (int) (((this.viewRect.f12373y + this.viewRect.height) - (layoutResult.labelRect.height / 2.0f)) - (layoutResult.iconRect.height / 2.0f));
                return;
            }
            layoutResult.textRect.f12373y = (int) ((this.viewRect.f12373y + (this.viewRect.height / 2.0f)) - (layoutResult.textRect.height / 2.0f));
            layoutResult.iconRect.f12373y = (int) ((this.viewRect.f12373y + (this.viewRect.height / 2.0f)) - (layoutResult.iconRect.height / 2.0f));
            return;
        }
        layoutResult.textRect.f12373y = (int) ((this.viewRect.f12373y + (layoutResult.labelRect.height / 2.0f)) - (layoutResult.textRect.height / 2.0f));
        layoutResult.iconRect.f12373y = (int) ((this.viewRect.f12373y + (layoutResult.labelRect.height / 2.0f)) - (layoutResult.iconRect.height / 2.0f));
    }

    private void calcLabelYPosition(LayoutResult layoutResult) {
        if (this.verticalAlignment != 1) {
            if (this.verticalAlignment != 0) {
                if (this.verticalAlignment != 3) {
                    return;
                }
                layoutResult.labelRect.f12373y = (this.viewRect.f12373y + this.viewRect.height) - layoutResult.labelRect.height;
                return;
            }
            layoutResult.labelRect.f12373y = (int) ((this.viewRect.f12373y + (this.viewRect.height / 2.0f)) - (layoutResult.labelRect.height / 2.0f));
            return;
        }
        layoutResult.labelRect.f12373y = this.viewRect.f12373y;
    }

    public static JComponent getMenuItemParent(JMenuItem jMenuItem) {
        Container parent = jMenuItem.getParent();
        if (parent instanceof JComponent) {
            if (!(jMenuItem instanceof JMenu) || !((JMenu) jMenuItem).isTopLevelMenu()) {
                return (JComponent) parent;
            }
            return null;
        }
        return null;
    }

    public static void clearUsedParentClientProperties(JMenuItem jMenuItem) {
        clearUsedClientProperties(getMenuItemParent(jMenuItem));
    }

    public static void clearUsedClientProperties(JComponent jComponent) {
        if (jComponent != null) {
            jComponent.putClientProperty(MAX_ARROW_WIDTH, null);
            jComponent.putClientProperty(MAX_CHECK_WIDTH, null);
            jComponent.putClientProperty(MAX_ACC_WIDTH, null);
            jComponent.putClientProperty(MAX_TEXT_WIDTH, null);
            jComponent.putClientProperty(MAX_ICON_WIDTH, null);
            jComponent.putClientProperty(MAX_LABEL_WIDTH, null);
            jComponent.putClientProperty(SwingUtilities2.BASICMENUITEMUI_MAX_TEXT_OFFSET, null);
        }
    }

    public static int max(int... iArr) {
        int i2 = Integer.MIN_VALUE;
        for (int i3 : iArr) {
            if (i3 > i2) {
                i2 = i3;
            }
        }
        return i2;
    }

    public static Rectangle createMaxRect() {
        return new Rectangle(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public static void addMaxWidth(RectSize rectSize, int i2, Dimension dimension) {
        if (rectSize.maxWidth > 0) {
            dimension.width += rectSize.maxWidth + i2;
        }
    }

    public static void addWidth(int i2, int i3, Dimension dimension) {
        if (i2 > 0) {
            dimension.width += i2 + i3;
        }
    }

    public JMenuItem getMenuItem() {
        return this.mi;
    }

    public JComponent getMenuItemParent() {
        return this.miParent;
    }

    public Font getFont() {
        return this.font;
    }

    public Font getAccFont() {
        return this.accFont;
    }

    public FontMetrics getFontMetrics() {
        return this.fm;
    }

    public FontMetrics getAccFontMetrics() {
        return this.accFm;
    }

    public Icon getIcon() {
        return this.icon;
    }

    public Icon getCheckIcon() {
        return this.checkIcon;
    }

    public Icon getArrowIcon() {
        return this.arrowIcon;
    }

    public String getText() {
        return this.text;
    }

    public String getAccText() {
        return this.accText;
    }

    public boolean isColumnLayout() {
        return this.isColumnLayout;
    }

    public boolean useCheckAndArrow() {
        return this.useCheckAndArrow;
    }

    public boolean isLeftToRight() {
        return this.isLeftToRight;
    }

    public boolean isTopLevelMenu() {
        return this.isTopLevelMenu;
    }

    public View getHtmlView() {
        return this.htmlView;
    }

    public int getVerticalAlignment() {
        return this.verticalAlignment;
    }

    public int getHorizontalAlignment() {
        return this.horizontalAlignment;
    }

    public int getVerticalTextPosition() {
        return this.verticalTextPosition;
    }

    public int getHorizontalTextPosition() {
        return this.horizontalTextPosition;
    }

    public int getGap() {
        return this.gap;
    }

    public int getLeadingGap() {
        return this.leadingGap;
    }

    public int getAfterCheckIconGap() {
        return this.afterCheckIconGap;
    }

    public int getMinTextOffset() {
        return this.minTextOffset;
    }

    public Rectangle getViewRect() {
        return this.viewRect;
    }

    public RectSize getIconSize() {
        return this.iconSize;
    }

    public RectSize getTextSize() {
        return this.textSize;
    }

    public RectSize getAccSize() {
        return this.accSize;
    }

    public RectSize getCheckSize() {
        return this.checkSize;
    }

    public RectSize getArrowSize() {
        return this.arrowSize;
    }

    public RectSize getLabelSize() {
        return this.labelSize;
    }

    protected void setMenuItem(JMenuItem jMenuItem) {
        this.mi = jMenuItem;
    }

    protected void setMenuItemParent(JComponent jComponent) {
        this.miParent = jComponent;
    }

    protected void setFont(Font font) {
        this.font = font;
    }

    protected void setAccFont(Font font) {
        this.accFont = font;
    }

    protected void setFontMetrics(FontMetrics fontMetrics) {
        this.fm = fontMetrics;
    }

    protected void setAccFontMetrics(FontMetrics fontMetrics) {
        this.accFm = fontMetrics;
    }

    protected void setIcon(Icon icon) {
        this.icon = icon;
    }

    protected void setCheckIcon(Icon icon) {
        this.checkIcon = icon;
    }

    protected void setArrowIcon(Icon icon) {
        this.arrowIcon = icon;
    }

    protected void setText(String str) {
        this.text = str;
    }

    protected void setAccText(String str) {
        this.accText = str;
    }

    protected void setColumnLayout(boolean z2) {
        this.isColumnLayout = z2;
    }

    protected void setUseCheckAndArrow(boolean z2) {
        this.useCheckAndArrow = z2;
    }

    protected void setLeftToRight(boolean z2) {
        this.isLeftToRight = z2;
    }

    protected void setTopLevelMenu(boolean z2) {
        this.isTopLevelMenu = z2;
    }

    protected void setHtmlView(View view) {
        this.htmlView = view;
    }

    protected void setVerticalAlignment(int i2) {
        this.verticalAlignment = i2;
    }

    protected void setHorizontalAlignment(int i2) {
        this.horizontalAlignment = i2;
    }

    protected void setVerticalTextPosition(int i2) {
        this.verticalTextPosition = i2;
    }

    protected void setHorizontalTextPosition(int i2) {
        this.horizontalTextPosition = i2;
    }

    protected void setGap(int i2) {
        this.gap = i2;
    }

    protected void setLeadingGap(int i2) {
        this.leadingGap = i2;
    }

    protected void setAfterCheckIconGap(int i2) {
        this.afterCheckIconGap = i2;
    }

    protected void setMinTextOffset(int i2) {
        this.minTextOffset = i2;
    }

    protected void setViewRect(Rectangle rectangle) {
        this.viewRect = rectangle;
    }

    protected void setIconSize(RectSize rectSize) {
        this.iconSize = rectSize;
    }

    protected void setTextSize(RectSize rectSize) {
        this.textSize = rectSize;
    }

    protected void setAccSize(RectSize rectSize) {
        this.accSize = rectSize;
    }

    protected void setCheckSize(RectSize rectSize) {
        this.checkSize = rectSize;
    }

    protected void setArrowSize(RectSize rectSize) {
        this.arrowSize = rectSize;
    }

    protected void setLabelSize(RectSize rectSize) {
        this.labelSize = rectSize;
    }

    public int getLeftTextExtraWidth() {
        return this.leftTextExtraWidth;
    }

    public static boolean useCheckAndArrow(JMenuItem jMenuItem) {
        boolean z2 = true;
        if ((jMenuItem instanceof JMenu) && ((JMenu) jMenuItem).isTopLevelMenu()) {
            z2 = false;
        }
        return z2;
    }

    /* loaded from: rt.jar:sun/swing/MenuItemLayoutHelper$LayoutResult.class */
    public static class LayoutResult {
        private Rectangle iconRect;
        private Rectangle textRect;
        private Rectangle accRect;
        private Rectangle checkRect;
        private Rectangle arrowRect;
        private Rectangle labelRect;

        public LayoutResult() {
            this.iconRect = new Rectangle();
            this.textRect = new Rectangle();
            this.accRect = new Rectangle();
            this.checkRect = new Rectangle();
            this.arrowRect = new Rectangle();
            this.labelRect = new Rectangle();
        }

        public LayoutResult(Rectangle rectangle, Rectangle rectangle2, Rectangle rectangle3, Rectangle rectangle4, Rectangle rectangle5, Rectangle rectangle6) {
            this.iconRect = rectangle;
            this.textRect = rectangle2;
            this.accRect = rectangle3;
            this.checkRect = rectangle4;
            this.arrowRect = rectangle5;
            this.labelRect = rectangle6;
        }

        public Rectangle getIconRect() {
            return this.iconRect;
        }

        public void setIconRect(Rectangle rectangle) {
            this.iconRect = rectangle;
        }

        public Rectangle getTextRect() {
            return this.textRect;
        }

        public void setTextRect(Rectangle rectangle) {
            this.textRect = rectangle;
        }

        public Rectangle getAccRect() {
            return this.accRect;
        }

        public void setAccRect(Rectangle rectangle) {
            this.accRect = rectangle;
        }

        public Rectangle getCheckRect() {
            return this.checkRect;
        }

        public void setCheckRect(Rectangle rectangle) {
            this.checkRect = rectangle;
        }

        public Rectangle getArrowRect() {
            return this.arrowRect;
        }

        public void setArrowRect(Rectangle rectangle) {
            this.arrowRect = rectangle;
        }

        public Rectangle getLabelRect() {
            return this.labelRect;
        }

        public void setLabelRect(Rectangle rectangle) {
            this.labelRect = rectangle;
        }

        public Map<String, Rectangle> getAllRects() {
            HashMap map = new HashMap();
            map.put("checkRect", this.checkRect);
            map.put("iconRect", this.iconRect);
            map.put("textRect", this.textRect);
            map.put("accRect", this.accRect);
            map.put("arrowRect", this.arrowRect);
            map.put("labelRect", this.labelRect);
            return map;
        }
    }

    /* loaded from: rt.jar:sun/swing/MenuItemLayoutHelper$ColumnAlignment.class */
    public static class ColumnAlignment {
        private int checkAlignment;
        private int iconAlignment;
        private int textAlignment;
        private int accAlignment;
        private int arrowAlignment;
        public static final ColumnAlignment LEFT_ALIGNMENT = new ColumnAlignment(2, 2, 2, 2, 2);
        public static final ColumnAlignment RIGHT_ALIGNMENT = new ColumnAlignment(4, 4, 4, 4, 4);

        public ColumnAlignment(int i2, int i3, int i4, int i5, int i6) {
            this.checkAlignment = i2;
            this.iconAlignment = i3;
            this.textAlignment = i4;
            this.accAlignment = i5;
            this.arrowAlignment = i6;
        }

        public int getCheckAlignment() {
            return this.checkAlignment;
        }

        public int getIconAlignment() {
            return this.iconAlignment;
        }

        public int getTextAlignment() {
            return this.textAlignment;
        }

        public int getAccAlignment() {
            return this.accAlignment;
        }

        public int getArrowAlignment() {
            return this.arrowAlignment;
        }
    }

    /* loaded from: rt.jar:sun/swing/MenuItemLayoutHelper$RectSize.class */
    public static class RectSize {
        private int width;
        private int height;
        private int origWidth;
        private int maxWidth;

        public RectSize() {
        }

        public RectSize(int i2, int i3, int i4, int i5) {
            this.width = i2;
            this.height = i3;
            this.origWidth = i4;
            this.maxWidth = i5;
        }

        public int getWidth() {
            return this.width;
        }

        public int getHeight() {
            return this.height;
        }

        public int getOrigWidth() {
            return this.origWidth;
        }

        public int getMaxWidth() {
            return this.maxWidth;
        }

        public void setWidth(int i2) {
            this.width = i2;
        }

        public void setHeight(int i2) {
            this.height = i2;
        }

        public void setOrigWidth(int i2) {
            this.origWidth = i2;
        }

        public void setMaxWidth(int i2) {
            this.maxWidth = i2;
        }

        public String toString() {
            return "[w=" + this.width + ",h=" + this.height + ",ow=" + this.origWidth + ",mw=" + this.maxWidth + "]";
        }
    }
}
