package javax.swing.plaf.synth;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.text.View;
import sun.swing.MenuItemLayoutHelper;
import sun.swing.SwingUtilities2;
import sun.swing.plaf.synth.SynthIcon;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthGraphicsUtils.class */
public class SynthGraphicsUtils {
    private Rectangle paintIconR = new Rectangle();
    private Rectangle paintTextR = new Rectangle();
    private Rectangle paintViewR = new Rectangle();
    private Insets paintInsets = new Insets(0, 0, 0, 0);
    private Rectangle iconR = new Rectangle();
    private Rectangle textR = new Rectangle();
    private Rectangle viewR = new Rectangle();
    private Insets viewSizingInsets = new Insets(0, 0, 0, 0);

    public void drawLine(SynthContext synthContext, Object obj, Graphics graphics, int i2, int i3, int i4, int i5) {
        graphics.drawLine(i2, i3, i4, i5);
    }

    public void drawLine(SynthContext synthContext, Object obj, Graphics graphics, int i2, int i3, int i4, int i5, Object obj2) {
        if ("dashed".equals(obj2)) {
            if (i2 == i4) {
                for (int i6 = i3 + (i3 % 2); i6 <= i5; i6 += 2) {
                    graphics.drawLine(i2, i6, i4, i6);
                }
                return;
            }
            if (i3 == i5) {
                for (int i7 = i2 + (i2 % 2); i7 <= i4; i7 += 2) {
                    graphics.drawLine(i7, i3, i7, i5);
                }
                return;
            }
            return;
        }
        drawLine(synthContext, obj, graphics, i2, i3, i4, i5);
    }

    public String layoutText(SynthContext synthContext, FontMetrics fontMetrics, String str, Icon icon, int i2, int i3, int i4, int i5, Rectangle rectangle, Rectangle rectangle2, Rectangle rectangle3, int i6) {
        if (icon instanceof SynthIcon) {
            SynthIconWrapper synthIconWrapper = SynthIconWrapper.get((SynthIcon) icon, synthContext);
            String strLayoutCompoundLabel = SwingUtilities.layoutCompoundLabel(synthContext.getComponent(), fontMetrics, str, synthIconWrapper, i3, i2, i5, i4, rectangle, rectangle2, rectangle3, i6);
            SynthIconWrapper.release(synthIconWrapper);
            return strLayoutCompoundLabel;
        }
        return SwingUtilities.layoutCompoundLabel(synthContext.getComponent(), fontMetrics, str, icon, i3, i2, i5, i4, rectangle, rectangle2, rectangle3, i6);
    }

    public int computeStringWidth(SynthContext synthContext, Font font, FontMetrics fontMetrics, String str) {
        return SwingUtilities2.stringWidth(synthContext.getComponent(), fontMetrics, str);
    }

    public Dimension getMinimumSize(SynthContext synthContext, Font font, String str, Icon icon, int i2, int i3, int i4, int i5, int i6, int i7) {
        JComponent component = synthContext.getComponent();
        Dimension preferredSize = getPreferredSize(synthContext, font, str, icon, i2, i3, i4, i5, i6, i7);
        View view = (View) component.getClientProperty("html");
        if (view != null) {
            preferredSize.width = (int) (preferredSize.width - (view.getPreferredSpan(0) - view.getMinimumSpan(0)));
        }
        return preferredSize;
    }

    public Dimension getMaximumSize(SynthContext synthContext, Font font, String str, Icon icon, int i2, int i3, int i4, int i5, int i6, int i7) {
        JComponent component = synthContext.getComponent();
        Dimension preferredSize = getPreferredSize(synthContext, font, str, icon, i2, i3, i4, i5, i6, i7);
        View view = (View) component.getClientProperty("html");
        if (view != null) {
            preferredSize.width = (int) (preferredSize.width + (view.getMaximumSpan(0) - view.getPreferredSpan(0)));
        }
        return preferredSize;
    }

    public int getMaximumCharHeight(SynthContext synthContext) {
        FontMetrics fontMetrics = synthContext.getComponent().getFontMetrics(synthContext.getStyle().getFont(synthContext));
        return fontMetrics.getAscent() + fontMetrics.getDescent();
    }

    public Dimension getPreferredSize(SynthContext synthContext, Font font, String str, Icon icon, int i2, int i3, int i4, int i5, int i6, int i7) {
        JComponent component = synthContext.getComponent();
        Insets insets = component.getInsets(this.viewSizingInsets);
        int i8 = insets.left + insets.right;
        int i9 = insets.top + insets.bottom;
        if (icon == null && (str == null || font == null)) {
            return new Dimension(i8, i9);
        }
        if (str == null || (icon != null && font == null)) {
            return new Dimension(SynthIcon.getIconWidth(icon, synthContext) + i8, SynthIcon.getIconHeight(icon, synthContext) + i9);
        }
        FontMetrics fontMetrics = component.getFontMetrics(font);
        Rectangle rectangle = this.iconR;
        Rectangle rectangle2 = this.iconR;
        Rectangle rectangle3 = this.iconR;
        this.iconR.height = 0;
        rectangle3.width = 0;
        rectangle2.f12373y = 0;
        rectangle.f12372x = 0;
        Rectangle rectangle4 = this.textR;
        Rectangle rectangle5 = this.textR;
        Rectangle rectangle6 = this.textR;
        this.textR.height = 0;
        rectangle6.width = 0;
        rectangle5.f12373y = 0;
        rectangle4.f12372x = 0;
        this.viewR.f12372x = i8;
        this.viewR.f12373y = i9;
        Rectangle rectangle7 = this.viewR;
        this.viewR.height = Short.MAX_VALUE;
        rectangle7.width = Short.MAX_VALUE;
        layoutText(synthContext, fontMetrics, str, icon, i2, i3, i4, i5, this.viewR, this.iconR, this.textR, i6);
        int iMin = Math.min(this.iconR.f12372x, this.textR.f12372x);
        Dimension dimension = new Dimension(Math.max(this.iconR.f12372x + this.iconR.width, this.textR.f12372x + this.textR.width) - iMin, Math.max(this.iconR.f12373y + this.iconR.height, this.textR.f12373y + this.textR.height) - Math.min(this.iconR.f12373y, this.textR.f12373y));
        dimension.width += i8;
        dimension.height += i9;
        return dimension;
    }

    public void paintText(SynthContext synthContext, Graphics graphics, String str, Rectangle rectangle, int i2) {
        paintText(synthContext, graphics, str, rectangle.f12372x, rectangle.f12373y, i2);
    }

    public void paintText(SynthContext synthContext, Graphics graphics, String str, int i2, int i3, int i4) {
        if (str != null) {
            JComponent component = synthContext.getComponent();
            SwingUtilities2.drawStringUnderlineCharAt(component, graphics, str, i4, i2, i3 + SwingUtilities2.getFontMetrics(component, graphics).getAscent());
        }
    }

    public void paintText(SynthContext synthContext, Graphics graphics, String str, Icon icon, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (icon == null && str == null) {
            return;
        }
        JComponent component = synthContext.getComponent();
        FontMetrics fontMetrics = SwingUtilities2.getFontMetrics(component, graphics);
        Insets paintingInsets = SynthLookAndFeel.getPaintingInsets(synthContext, this.paintInsets);
        this.paintViewR.f12372x = paintingInsets.left;
        this.paintViewR.f12373y = paintingInsets.top;
        this.paintViewR.width = component.getWidth() - (paintingInsets.left + paintingInsets.right);
        this.paintViewR.height = component.getHeight() - (paintingInsets.top + paintingInsets.bottom);
        Rectangle rectangle = this.paintIconR;
        Rectangle rectangle2 = this.paintIconR;
        Rectangle rectangle3 = this.paintIconR;
        this.paintIconR.height = 0;
        rectangle3.width = 0;
        rectangle2.f12373y = 0;
        rectangle.f12372x = 0;
        Rectangle rectangle4 = this.paintTextR;
        Rectangle rectangle5 = this.paintTextR;
        Rectangle rectangle6 = this.paintTextR;
        this.paintTextR.height = 0;
        rectangle6.width = 0;
        rectangle5.f12373y = 0;
        rectangle4.f12372x = 0;
        String strLayoutText = layoutText(synthContext, fontMetrics, str, icon, i2, i3, i4, i5, this.paintViewR, this.paintIconR, this.paintTextR, i6);
        if (icon != null) {
            Color color = graphics.getColor();
            if (synthContext.getStyle().getBoolean(synthContext, "TableHeader.alignSorterArrow", false) && "TableHeader.renderer".equals(component.getName())) {
                this.paintIconR.f12372x = this.paintViewR.width - this.paintIconR.width;
            } else {
                this.paintIconR.f12372x += i8;
            }
            this.paintIconR.f12373y += i8;
            SynthIcon.paintIcon(icon, synthContext, graphics, this.paintIconR.f12372x, this.paintIconR.f12373y, this.paintIconR.width, this.paintIconR.height);
            graphics.setColor(color);
        }
        if (str != null) {
            View view = (View) component.getClientProperty("html");
            if (view != null) {
                view.paint(graphics, this.paintTextR);
                return;
            }
            this.paintTextR.f12372x += i8;
            this.paintTextR.f12373y += i8;
            paintText(synthContext, graphics, strLayoutText, this.paintTextR, i7);
        }
    }

    static Dimension getPreferredMenuItemSize(SynthContext synthContext, SynthContext synthContext2, JComponent jComponent, Icon icon, Icon icon2, int i2, String str, boolean z2, String str2) {
        JMenuItem jMenuItem = (JMenuItem) jComponent;
        SynthMenuItemLayoutHelper synthMenuItemLayoutHelper = new SynthMenuItemLayoutHelper(synthContext, synthContext2, jMenuItem, icon, icon2, MenuItemLayoutHelper.createMaxRect(), i2, str, SynthLookAndFeel.isLeftToRight(jMenuItem), z2, str2);
        Dimension dimension = new Dimension();
        int gap = synthMenuItemLayoutHelper.getGap();
        dimension.width = 0;
        MenuItemLayoutHelper.addMaxWidth(synthMenuItemLayoutHelper.getCheckSize(), gap, dimension);
        MenuItemLayoutHelper.addMaxWidth(synthMenuItemLayoutHelper.getLabelSize(), gap, dimension);
        MenuItemLayoutHelper.addWidth(synthMenuItemLayoutHelper.getMaxAccOrArrowWidth(), 5 * gap, dimension);
        dimension.width -= gap;
        dimension.height = MenuItemLayoutHelper.max(synthMenuItemLayoutHelper.getCheckSize().getHeight(), synthMenuItemLayoutHelper.getLabelSize().getHeight(), synthMenuItemLayoutHelper.getAccSize().getHeight(), synthMenuItemLayoutHelper.getArrowSize().getHeight());
        Insets insets = synthMenuItemLayoutHelper.getMenuItem().getInsets();
        if (insets != null) {
            dimension.width += insets.left + insets.right;
            dimension.height += insets.top + insets.bottom;
        }
        if (dimension.width % 2 == 0) {
            dimension.width++;
        }
        if (dimension.height % 2 == 0) {
            dimension.height++;
        }
        return dimension;
    }

    static void applyInsets(Rectangle rectangle, Insets insets, boolean z2) {
        if (insets != null) {
            rectangle.f12372x += z2 ? insets.left : insets.right;
            rectangle.f12373y += insets.top;
            rectangle.width -= (z2 ? insets.right : insets.left) + rectangle.f12372x;
            rectangle.height -= insets.bottom + rectangle.f12373y;
        }
    }

    static void paint(SynthContext synthContext, SynthContext synthContext2, Graphics graphics, Icon icon, Icon icon2, String str, int i2, String str2) {
        JMenuItem jMenuItem = (JMenuItem) synthContext.getComponent();
        graphics.setFont(synthContext.getStyle().getFont(synthContext));
        Rectangle rectangle = new Rectangle(0, 0, jMenuItem.getWidth(), jMenuItem.getHeight());
        boolean zIsLeftToRight = SynthLookAndFeel.isLeftToRight(jMenuItem);
        applyInsets(rectangle, jMenuItem.getInsets(), zIsLeftToRight);
        SynthMenuItemLayoutHelper synthMenuItemLayoutHelper = new SynthMenuItemLayoutHelper(synthContext, synthContext2, jMenuItem, icon, icon2, rectangle, i2, str, zIsLeftToRight, MenuItemLayoutHelper.useCheckAndArrow(jMenuItem), str2);
        paintMenuItem(graphics, synthMenuItemLayoutHelper, synthMenuItemLayoutHelper.layoutMenuItem());
    }

    static void paintMenuItem(Graphics graphics, SynthMenuItemLayoutHelper synthMenuItemLayoutHelper, MenuItemLayoutHelper.LayoutResult layoutResult) {
        Font font = graphics.getFont();
        Color color = graphics.getColor();
        paintCheckIcon(graphics, synthMenuItemLayoutHelper, layoutResult);
        paintIcon(graphics, synthMenuItemLayoutHelper, layoutResult);
        paintText(graphics, synthMenuItemLayoutHelper, layoutResult);
        paintAccText(graphics, synthMenuItemLayoutHelper, layoutResult);
        paintArrowIcon(graphics, synthMenuItemLayoutHelper, layoutResult);
        graphics.setColor(color);
        graphics.setFont(font);
    }

    static void paintBackground(Graphics graphics, SynthMenuItemLayoutHelper synthMenuItemLayoutHelper) {
        paintBackground(synthMenuItemLayoutHelper.getContext(), graphics, synthMenuItemLayoutHelper.getMenuItem());
    }

    static void paintBackground(SynthContext synthContext, Graphics graphics, JComponent jComponent) {
        synthContext.getPainter().paintMenuItemBackground(synthContext, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
    }

    static void paintIcon(Graphics graphics, SynthMenuItemLayoutHelper synthMenuItemLayoutHelper, MenuItemLayoutHelper.LayoutResult layoutResult) {
        Icon icon;
        if (synthMenuItemLayoutHelper.getIcon() != null) {
            JMenuItem menuItem = synthMenuItemLayoutHelper.getMenuItem();
            ButtonModel model = menuItem.getModel();
            if (!model.isEnabled()) {
                icon = menuItem.getDisabledIcon();
            } else if (model.isPressed() && model.isArmed()) {
                icon = menuItem.getPressedIcon();
                if (icon == null) {
                    icon = menuItem.getIcon();
                }
            } else {
                icon = menuItem.getIcon();
            }
            if (icon != null) {
                Rectangle iconRect = layoutResult.getIconRect();
                SynthIcon.paintIcon(icon, synthMenuItemLayoutHelper.getContext(), graphics, iconRect.f12372x, iconRect.f12373y, iconRect.width, iconRect.height);
            }
        }
    }

    static void paintCheckIcon(Graphics graphics, SynthMenuItemLayoutHelper synthMenuItemLayoutHelper, MenuItemLayoutHelper.LayoutResult layoutResult) {
        if (synthMenuItemLayoutHelper.getCheckIcon() != null) {
            Rectangle checkRect = layoutResult.getCheckRect();
            SynthIcon.paintIcon(synthMenuItemLayoutHelper.getCheckIcon(), synthMenuItemLayoutHelper.getContext(), graphics, checkRect.f12372x, checkRect.f12373y, checkRect.width, checkRect.height);
        }
    }

    static void paintAccText(Graphics graphics, SynthMenuItemLayoutHelper synthMenuItemLayoutHelper, MenuItemLayoutHelper.LayoutResult layoutResult) {
        String accText = synthMenuItemLayoutHelper.getAccText();
        if (accText != null && !accText.equals("")) {
            graphics.setColor(synthMenuItemLayoutHelper.getAccStyle().getColor(synthMenuItemLayoutHelper.getAccContext(), ColorType.TEXT_FOREGROUND));
            graphics.setFont(synthMenuItemLayoutHelper.getAccStyle().getFont(synthMenuItemLayoutHelper.getAccContext()));
            synthMenuItemLayoutHelper.getAccGraphicsUtils().paintText(synthMenuItemLayoutHelper.getAccContext(), graphics, accText, layoutResult.getAccRect().f12372x, layoutResult.getAccRect().f12373y, -1);
        }
    }

    static void paintText(Graphics graphics, SynthMenuItemLayoutHelper synthMenuItemLayoutHelper, MenuItemLayoutHelper.LayoutResult layoutResult) {
        if (!synthMenuItemLayoutHelper.getText().equals("")) {
            if (synthMenuItemLayoutHelper.getHtmlView() != null) {
                synthMenuItemLayoutHelper.getHtmlView().paint(graphics, layoutResult.getTextRect());
                return;
            }
            graphics.setColor(synthMenuItemLayoutHelper.getStyle().getColor(synthMenuItemLayoutHelper.getContext(), ColorType.TEXT_FOREGROUND));
            graphics.setFont(synthMenuItemLayoutHelper.getStyle().getFont(synthMenuItemLayoutHelper.getContext()));
            synthMenuItemLayoutHelper.getGraphicsUtils().paintText(synthMenuItemLayoutHelper.getContext(), graphics, synthMenuItemLayoutHelper.getText(), layoutResult.getTextRect().f12372x, layoutResult.getTextRect().f12373y, synthMenuItemLayoutHelper.getMenuItem().getDisplayedMnemonicIndex());
        }
    }

    static void paintArrowIcon(Graphics graphics, SynthMenuItemLayoutHelper synthMenuItemLayoutHelper, MenuItemLayoutHelper.LayoutResult layoutResult) {
        if (synthMenuItemLayoutHelper.getArrowIcon() != null) {
            Rectangle arrowRect = layoutResult.getArrowRect();
            SynthIcon.paintIcon(synthMenuItemLayoutHelper.getArrowIcon(), synthMenuItemLayoutHelper.getContext(), graphics, arrowRect.f12372x, arrowRect.f12373y, arrowRect.width, arrowRect.height);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthGraphicsUtils$SynthIconWrapper.class */
    private static class SynthIconWrapper implements Icon {
        private static final List<SynthIconWrapper> CACHE = new ArrayList(1);
        private SynthIcon synthIcon;
        private SynthContext context;

        static SynthIconWrapper get(SynthIcon synthIcon, SynthContext synthContext) {
            synchronized (CACHE) {
                int size = CACHE.size();
                if (size > 0) {
                    SynthIconWrapper synthIconWrapperRemove = CACHE.remove(size - 1);
                    synthIconWrapperRemove.reset(synthIcon, synthContext);
                    return synthIconWrapperRemove;
                }
                return new SynthIconWrapper(synthIcon, synthContext);
            }
        }

        static void release(SynthIconWrapper synthIconWrapper) {
            synthIconWrapper.reset(null, null);
            synchronized (CACHE) {
                CACHE.add(synthIconWrapper);
            }
        }

        SynthIconWrapper(SynthIcon synthIcon, SynthContext synthContext) {
            reset(synthIcon, synthContext);
        }

        void reset(SynthIcon synthIcon, SynthContext synthContext) {
            this.synthIcon = synthIcon;
            this.context = synthContext;
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return this.synthIcon.getIconWidth(this.context);
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return this.synthIcon.getIconHeight(this.context);
        }
    }
}
