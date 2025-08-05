package javax.swing.plaf.synth;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthButtonUI.class */
public class SynthButtonUI extends BasicButtonUI implements PropertyChangeListener, SynthUI {
    private SynthStyle style;

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthButtonUI();
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void installDefaults(AbstractButton abstractButton) {
        updateStyle(abstractButton);
        LookAndFeel.installProperty(abstractButton, AbstractButton.ROLLOVER_ENABLED_CHANGED_PROPERTY, Boolean.TRUE);
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void installListeners(AbstractButton abstractButton) {
        super.installListeners(abstractButton);
        abstractButton.addPropertyChangeListener(this);
    }

    void updateStyle(AbstractButton abstractButton) {
        SynthContext context = getContext(abstractButton, 1);
        SynthStyle synthStyle = this.style;
        this.style = SynthLookAndFeel.updateStyle(context, this);
        if (this.style != synthStyle) {
            if (abstractButton.getMargin() == null || (abstractButton.getMargin() instanceof UIResource)) {
                Insets insets = (Insets) this.style.get(context, getPropertyPrefix() + AbstractButton.MARGIN_CHANGED_PROPERTY);
                if (insets == null) {
                    insets = SynthLookAndFeel.EMPTY_UIRESOURCE_INSETS;
                }
                abstractButton.setMargin(insets);
            }
            Object obj = this.style.get(context, getPropertyPrefix() + "iconTextGap");
            if (obj != null) {
                LookAndFeel.installProperty(abstractButton, "iconTextGap", obj);
            }
            Object obj2 = this.style.get(context, getPropertyPrefix() + AbstractButton.CONTENT_AREA_FILLED_CHANGED_PROPERTY);
            LookAndFeel.installProperty(abstractButton, AbstractButton.CONTENT_AREA_FILLED_CHANGED_PROPERTY, obj2 != null ? obj2 : Boolean.TRUE);
            if (synthStyle != null) {
                uninstallKeyboardActions(abstractButton);
                installKeyboardActions(abstractButton);
            }
        }
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void uninstallListeners(AbstractButton abstractButton) {
        super.uninstallListeners(abstractButton);
        abstractButton.removePropertyChangeListener(this);
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void uninstallDefaults(AbstractButton abstractButton) {
        SynthContext context = getContext(abstractButton, 1);
        this.style.uninstallDefaults(context);
        context.dispose();
        this.style = null;
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public SynthContext getContext(JComponent jComponent) {
        return getContext(jComponent, getComponentState(jComponent));
    }

    SynthContext getContext(JComponent jComponent, int i2) {
        return SynthContext.getContext(jComponent, this.style, i2);
    }

    private int getComponentState(JComponent jComponent) {
        int i2 = 1;
        if (!jComponent.isEnabled()) {
            i2 = 8;
        }
        if (SynthLookAndFeel.getSelectedUI() == this) {
            return SynthLookAndFeel.getSelectedUIState() | 1;
        }
        AbstractButton abstractButton = (AbstractButton) jComponent;
        ButtonModel model = abstractButton.getModel();
        if (model.isPressed()) {
            if (model.isArmed()) {
                i2 = 4;
            } else {
                i2 = 2;
            }
        }
        if (model.isRollover()) {
            i2 |= 2;
        }
        if (model.isSelected()) {
            i2 |= 512;
        }
        if (jComponent.isFocusOwner() && abstractButton.isFocusPainted()) {
            i2 |= 256;
        }
        if ((jComponent instanceof JButton) && ((JButton) jComponent).isDefaultButton()) {
            i2 |= 1024;
        }
        return i2;
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI, javax.swing.plaf.ComponentUI
    public int getBaseline(JComponent jComponent, int i2, int i3) {
        int ascent;
        if (jComponent == null) {
            throw new NullPointerException("Component must be non-null");
        }
        if (i2 < 0 || i3 < 0) {
            throw new IllegalArgumentException("Width and height must be >= 0");
        }
        AbstractButton abstractButton = (AbstractButton) jComponent;
        String text = abstractButton.getText();
        if (text == null || "".equals(text)) {
            return -1;
        }
        Insets insets = abstractButton.getInsets();
        Rectangle rectangle = new Rectangle();
        Rectangle rectangle2 = new Rectangle();
        Rectangle rectangle3 = new Rectangle();
        rectangle.f12372x = insets.left;
        rectangle.f12373y = insets.top;
        rectangle.width = i2 - (insets.right + rectangle.f12372x);
        rectangle.height = i3 - (insets.bottom + rectangle.f12373y);
        SynthContext context = getContext(abstractButton);
        FontMetrics fontMetrics = context.getComponent().getFontMetrics(context.getStyle().getFont(context));
        context.getStyle().getGraphicsUtils(context).layoutText(context, fontMetrics, abstractButton.getText(), abstractButton.getIcon(), abstractButton.getHorizontalAlignment(), abstractButton.getVerticalAlignment(), abstractButton.getHorizontalTextPosition(), abstractButton.getVerticalTextPosition(), rectangle, rectangle3, rectangle2, abstractButton.getIconTextGap());
        View view = (View) abstractButton.getClientProperty("html");
        if (view != null) {
            ascent = BasicHTML.getHTMLBaseline(view, rectangle2.width, rectangle2.height);
            if (ascent >= 0) {
                ascent += rectangle2.f12373y;
            }
        } else {
            ascent = rectangle2.f12373y + fontMetrics.getAscent();
        }
        context.dispose();
        return ascent;
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        SynthLookAndFeel.update(context, graphics);
        paintBackground(context, graphics, jComponent);
        paint(context, graphics);
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        paint(context, graphics);
        context.dispose();
    }

    protected void paint(SynthContext synthContext, Graphics graphics) {
        AbstractButton abstractButton = (AbstractButton) synthContext.getComponent();
        graphics.setColor(synthContext.getStyle().getColor(synthContext, ColorType.TEXT_FOREGROUND));
        graphics.setFont(this.style.getFont(synthContext));
        synthContext.getStyle().getGraphicsUtils(synthContext).paintText(synthContext, graphics, abstractButton.getText(), getIcon(abstractButton), abstractButton.getHorizontalAlignment(), abstractButton.getVerticalAlignment(), abstractButton.getHorizontalTextPosition(), abstractButton.getVerticalTextPosition(), abstractButton.getIconTextGap(), abstractButton.getDisplayedMnemonicIndex(), getTextShiftOffset(synthContext));
    }

    void paintBackground(SynthContext synthContext, Graphics graphics, JComponent jComponent) {
        if (((AbstractButton) jComponent).isContentAreaFilled()) {
            synthContext.getPainter().paintButtonBackground(synthContext, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
        }
    }

    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintButtonBorder(synthContext, graphics, i2, i3, i4, i5);
    }

    protected Icon getDefaultIcon(AbstractButton abstractButton) {
        SynthContext context = getContext(abstractButton);
        Icon icon = context.getStyle().getIcon(context, getPropertyPrefix() + "icon");
        context.dispose();
        return icon;
    }

    protected Icon getIcon(AbstractButton abstractButton) {
        Icon enabledIcon;
        Icon icon = abstractButton.getIcon();
        ButtonModel model = abstractButton.getModel();
        if (!model.isEnabled()) {
            enabledIcon = getSynthDisabledIcon(abstractButton, icon);
        } else if (model.isPressed() && model.isArmed()) {
            enabledIcon = getPressedIcon(abstractButton, getSelectedIcon(abstractButton, icon));
        } else if (abstractButton.isRolloverEnabled() && model.isRollover()) {
            enabledIcon = getRolloverIcon(abstractButton, getSelectedIcon(abstractButton, icon));
        } else if (model.isSelected()) {
            enabledIcon = getSelectedIcon(abstractButton, icon);
        } else {
            enabledIcon = getEnabledIcon(abstractButton, icon);
        }
        if (enabledIcon == null) {
            return getDefaultIcon(abstractButton);
        }
        return enabledIcon;
    }

    private Icon getIcon(AbstractButton abstractButton, Icon icon, Icon icon2, int i2) {
        Icon synthIcon = icon;
        if (synthIcon == null) {
            if (icon2 instanceof UIResource) {
                synthIcon = getSynthIcon(abstractButton, i2);
                if (synthIcon == null) {
                    synthIcon = icon2;
                }
            } else {
                synthIcon = icon2;
            }
        }
        return synthIcon;
    }

    private Icon getSynthIcon(AbstractButton abstractButton, int i2) {
        return this.style.getIcon(getContext(abstractButton, i2), getPropertyPrefix() + "icon");
    }

    private Icon getEnabledIcon(AbstractButton abstractButton, Icon icon) {
        if (icon == null) {
            icon = getSynthIcon(abstractButton, 1);
        }
        return icon;
    }

    private Icon getSelectedIcon(AbstractButton abstractButton, Icon icon) {
        return getIcon(abstractButton, abstractButton.getSelectedIcon(), icon, 512);
    }

    private Icon getRolloverIcon(AbstractButton abstractButton, Icon icon) {
        Icon icon2;
        if (abstractButton.getModel().isSelected()) {
            icon2 = getIcon(abstractButton, abstractButton.getRolloverSelectedIcon(), icon, 514);
        } else {
            icon2 = getIcon(abstractButton, abstractButton.getRolloverIcon(), icon, 2);
        }
        return icon2;
    }

    private Icon getPressedIcon(AbstractButton abstractButton, Icon icon) {
        return getIcon(abstractButton, abstractButton.getPressedIcon(), icon, 4);
    }

    private Icon getSynthDisabledIcon(AbstractButton abstractButton, Icon icon) {
        Icon icon2;
        if (abstractButton.getModel().isSelected()) {
            icon2 = getIcon(abstractButton, abstractButton.getDisabledSelectedIcon(), icon, 520);
        } else {
            icon2 = getIcon(abstractButton, abstractButton.getDisabledIcon(), icon, 8);
        }
        return icon2;
    }

    private int getTextShiftOffset(SynthContext synthContext) {
        AbstractButton abstractButton = (AbstractButton) synthContext.getComponent();
        ButtonModel model = abstractButton.getModel();
        if (model.isArmed() && model.isPressed() && abstractButton.getPressedIcon() == null) {
            return synthContext.getStyle().getInt(synthContext, getPropertyPrefix() + "textShiftOffset", 0);
        }
        return 0;
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI, javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        if (jComponent.getComponentCount() > 0 && jComponent.getLayout() != null) {
            return null;
        }
        AbstractButton abstractButton = (AbstractButton) jComponent;
        SynthContext context = getContext(jComponent);
        Dimension minimumSize = context.getStyle().getGraphicsUtils(context).getMinimumSize(context, context.getStyle().getFont(context), abstractButton.getText(), getSizingIcon(abstractButton), abstractButton.getHorizontalAlignment(), abstractButton.getVerticalAlignment(), abstractButton.getHorizontalTextPosition(), abstractButton.getVerticalTextPosition(), abstractButton.getIconTextGap(), abstractButton.getDisplayedMnemonicIndex());
        context.dispose();
        return minimumSize;
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        if (jComponent.getComponentCount() > 0 && jComponent.getLayout() != null) {
            return null;
        }
        AbstractButton abstractButton = (AbstractButton) jComponent;
        SynthContext context = getContext(jComponent);
        Dimension preferredSize = context.getStyle().getGraphicsUtils(context).getPreferredSize(context, context.getStyle().getFont(context), abstractButton.getText(), getSizingIcon(abstractButton), abstractButton.getHorizontalAlignment(), abstractButton.getVerticalAlignment(), abstractButton.getHorizontalTextPosition(), abstractButton.getVerticalTextPosition(), abstractButton.getIconTextGap(), abstractButton.getDisplayedMnemonicIndex());
        context.dispose();
        return preferredSize;
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI, javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        if (jComponent.getComponentCount() > 0 && jComponent.getLayout() != null) {
            return null;
        }
        AbstractButton abstractButton = (AbstractButton) jComponent;
        SynthContext context = getContext(jComponent);
        Dimension maximumSize = context.getStyle().getGraphicsUtils(context).getMaximumSize(context, context.getStyle().getFont(context), abstractButton.getText(), getSizingIcon(abstractButton), abstractButton.getHorizontalAlignment(), abstractButton.getVerticalAlignment(), abstractButton.getHorizontalTextPosition(), abstractButton.getVerticalTextPosition(), abstractButton.getIconTextGap(), abstractButton.getDisplayedMnemonicIndex());
        context.dispose();
        return maximumSize;
    }

    protected Icon getSizingIcon(AbstractButton abstractButton) {
        Icon enabledIcon = getEnabledIcon(abstractButton, abstractButton.getIcon());
        if (enabledIcon == null) {
            enabledIcon = getDefaultIcon(abstractButton);
        }
        return enabledIcon;
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
            updateStyle((AbstractButton) propertyChangeEvent.getSource());
        }
    }
}
