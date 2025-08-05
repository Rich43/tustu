package javax.swing.plaf.synth;

import java.awt.Rectangle;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import sun.swing.MenuItemLayoutHelper;
import sun.swing.StringUIClientPropertyKey;
import sun.swing.plaf.synth.SynthIcon;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthMenuItemLayoutHelper.class */
class SynthMenuItemLayoutHelper extends MenuItemLayoutHelper {
    public static final StringUIClientPropertyKey MAX_ACC_OR_ARROW_WIDTH = new StringUIClientPropertyKey("maxAccOrArrowWidth");
    public static final MenuItemLayoutHelper.ColumnAlignment LTR_ALIGNMENT_1 = new MenuItemLayoutHelper.ColumnAlignment(2, 2, 2, 4, 4);
    public static final MenuItemLayoutHelper.ColumnAlignment LTR_ALIGNMENT_2 = new MenuItemLayoutHelper.ColumnAlignment(2, 2, 2, 2, 4);
    public static final MenuItemLayoutHelper.ColumnAlignment RTL_ALIGNMENT_1 = new MenuItemLayoutHelper.ColumnAlignment(4, 4, 4, 2, 2);
    public static final MenuItemLayoutHelper.ColumnAlignment RTL_ALIGNMENT_2 = new MenuItemLayoutHelper.ColumnAlignment(4, 4, 4, 4, 2);
    private SynthContext context;
    private SynthContext accContext;
    private SynthStyle style;
    private SynthStyle accStyle;
    private SynthGraphicsUtils gu;
    private SynthGraphicsUtils accGu;
    private boolean alignAcceleratorText;
    private int maxAccOrArrowWidth;

    public SynthMenuItemLayoutHelper(SynthContext synthContext, SynthContext synthContext2, JMenuItem jMenuItem, Icon icon, Icon icon2, Rectangle rectangle, int i2, String str, boolean z2, boolean z3, String str2) {
        this.context = synthContext;
        this.accContext = synthContext2;
        this.style = synthContext.getStyle();
        this.accStyle = synthContext2.getStyle();
        this.gu = this.style.getGraphicsUtils(synthContext);
        this.accGu = this.accStyle.getGraphicsUtils(synthContext2);
        this.alignAcceleratorText = getAlignAcceleratorText(str2);
        reset(jMenuItem, icon, icon2, rectangle, i2, str, z2, this.style.getFont(synthContext), this.accStyle.getFont(synthContext2), z3, str2);
        setLeadingGap(0);
    }

    private boolean getAlignAcceleratorText(String str) {
        return this.style.getBoolean(this.context, str + ".alignAcceleratorText", true);
    }

    @Override // sun.swing.MenuItemLayoutHelper
    protected void calcWidthsAndHeights() {
        if (getIcon() != null) {
            getIconSize().setWidth(SynthIcon.getIconWidth(getIcon(), this.context));
            getIconSize().setHeight(SynthIcon.getIconHeight(getIcon(), this.context));
        }
        if (!getAccText().equals("")) {
            getAccSize().setWidth(this.accGu.computeStringWidth(getAccContext(), getAccFontMetrics().getFont(), getAccFontMetrics(), getAccText()));
            getAccSize().setHeight(getAccFontMetrics().getHeight());
        }
        if (getText() == null) {
            setText("");
        } else if (!getText().equals("")) {
            if (getHtmlView() != null) {
                getTextSize().setWidth((int) getHtmlView().getPreferredSpan(0));
                getTextSize().setHeight((int) getHtmlView().getPreferredSpan(1));
            } else {
                getTextSize().setWidth(this.gu.computeStringWidth(this.context, getFontMetrics().getFont(), getFontMetrics(), getText()));
                getTextSize().setHeight(getFontMetrics().getHeight());
            }
        }
        if (useCheckAndArrow()) {
            if (getCheckIcon() != null) {
                getCheckSize().setWidth(SynthIcon.getIconWidth(getCheckIcon(), this.context));
                getCheckSize().setHeight(SynthIcon.getIconHeight(getCheckIcon(), this.context));
            }
            if (getArrowIcon() != null) {
                getArrowSize().setWidth(SynthIcon.getIconWidth(getArrowIcon(), this.context));
                getArrowSize().setHeight(SynthIcon.getIconHeight(getArrowIcon(), this.context));
            }
        }
        if (isColumnLayout()) {
            getLabelSize().setWidth(getIconSize().getWidth() + getTextSize().getWidth() + getGap());
            getLabelSize().setHeight(MenuItemLayoutHelper.max(getCheckSize().getHeight(), getIconSize().getHeight(), getTextSize().getHeight(), getAccSize().getHeight(), getArrowSize().getHeight()));
            return;
        }
        Rectangle rectangle = new Rectangle();
        Rectangle rectangle2 = new Rectangle();
        this.gu.layoutText(this.context, getFontMetrics(), getText(), getIcon(), getHorizontalAlignment(), getVerticalAlignment(), getHorizontalTextPosition(), getVerticalTextPosition(), getViewRect(), rectangle2, rectangle, getGap());
        rectangle.width += getLeftTextExtraWidth();
        Rectangle rectangleUnion = rectangle2.union(rectangle);
        getLabelSize().setHeight(rectangleUnion.height);
        getLabelSize().setWidth(rectangleUnion.width);
    }

    @Override // sun.swing.MenuItemLayoutHelper
    protected void calcMaxWidths() {
        calcMaxWidth(getCheckSize(), MAX_CHECK_WIDTH);
        this.maxAccOrArrowWidth = calcMaxValue(MAX_ACC_OR_ARROW_WIDTH, getArrowSize().getWidth());
        this.maxAccOrArrowWidth = calcMaxValue(MAX_ACC_OR_ARROW_WIDTH, getAccSize().getWidth());
        if (isColumnLayout()) {
            calcMaxWidth(getIconSize(), MAX_ICON_WIDTH);
            calcMaxWidth(getTextSize(), MAX_TEXT_WIDTH);
            int gap = getGap();
            if (getIconSize().getMaxWidth() == 0 || getTextSize().getMaxWidth() == 0) {
                gap = 0;
            }
            getLabelSize().setMaxWidth(calcMaxValue(MAX_LABEL_WIDTH, getIconSize().getMaxWidth() + getTextSize().getMaxWidth() + gap));
            return;
        }
        getIconSize().setMaxWidth(getParentIntProperty(MAX_ICON_WIDTH));
        calcMaxWidth(getLabelSize(), MAX_LABEL_WIDTH);
        int maxWidth = getLabelSize().getMaxWidth() - getIconSize().getMaxWidth();
        if (getIconSize().getMaxWidth() > 0) {
            maxWidth -= getGap();
        }
        getTextSize().setMaxWidth(calcMaxValue(MAX_TEXT_WIDTH, maxWidth));
    }

    public SynthContext getContext() {
        return this.context;
    }

    public SynthContext getAccContext() {
        return this.accContext;
    }

    public SynthStyle getStyle() {
        return this.style;
    }

    public SynthStyle getAccStyle() {
        return this.accStyle;
    }

    public SynthGraphicsUtils getGraphicsUtils() {
        return this.gu;
    }

    public SynthGraphicsUtils getAccGraphicsUtils() {
        return this.accGu;
    }

    public boolean alignAcceleratorText() {
        return this.alignAcceleratorText;
    }

    public int getMaxAccOrArrowWidth() {
        return this.maxAccOrArrowWidth;
    }

    @Override // sun.swing.MenuItemLayoutHelper
    protected void prepareForLayout(MenuItemLayoutHelper.LayoutResult layoutResult) {
        layoutResult.getCheckRect().width = getCheckSize().getMaxWidth();
        if (useCheckAndArrow() && !"".equals(getAccText())) {
            layoutResult.getAccRect().width = this.maxAccOrArrowWidth;
        } else {
            layoutResult.getArrowRect().width = this.maxAccOrArrowWidth;
        }
    }

    @Override // sun.swing.MenuItemLayoutHelper
    public MenuItemLayoutHelper.ColumnAlignment getLTRColumnAlignment() {
        if (alignAcceleratorText()) {
            return LTR_ALIGNMENT_2;
        }
        return LTR_ALIGNMENT_1;
    }

    @Override // sun.swing.MenuItemLayoutHelper
    public MenuItemLayoutHelper.ColumnAlignment getRTLColumnAlignment() {
        if (alignAcceleratorText()) {
            return RTL_ALIGNMENT_2;
        }
        return RTL_ALIGNMENT_1;
    }

    @Override // sun.swing.MenuItemLayoutHelper
    protected void layoutIconAndTextInLabelRect(MenuItemLayoutHelper.LayoutResult layoutResult) {
        layoutResult.setTextRect(new Rectangle());
        layoutResult.setIconRect(new Rectangle());
        this.gu.layoutText(this.context, getFontMetrics(), getText(), getIcon(), getHorizontalAlignment(), getVerticalAlignment(), getHorizontalTextPosition(), getVerticalTextPosition(), layoutResult.getLabelRect(), layoutResult.getIconRect(), layoutResult.getTextRect(), getGap());
    }
}
