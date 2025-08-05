package javax.swing.plaf.synth;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.nimbus.NimbusStyle;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthScrollBarUI.class */
public class SynthScrollBarUI extends BasicScrollBarUI implements PropertyChangeListener, SynthUI {
    private SynthStyle style;
    private SynthStyle thumbStyle;
    private SynthStyle trackStyle;
    private boolean validMinimumThumbSize;

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthScrollBarUI();
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected void installDefaults() {
        super.installDefaults();
        this.trackHighlight = 0;
        if (this.scrollbar.getLayout() == null || (this.scrollbar.getLayout() instanceof UIResource)) {
            this.scrollbar.setLayout(this);
        }
        configureScrollBarColors();
        updateStyle(this.scrollbar);
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected void configureScrollBarColors() {
    }

    private void updateStyle(JScrollBar jScrollBar) {
        SynthStyle synthStyle = this.style;
        SynthContext context = getContext(jScrollBar, 1);
        this.style = SynthLookAndFeel.updateStyle(context, this);
        if (this.style != synthStyle) {
            this.scrollBarWidth = this.style.getInt(context, "ScrollBar.thumbHeight", 14);
            this.minimumThumbSize = (Dimension) this.style.get(context, "ScrollBar.minimumThumbSize");
            if (this.minimumThumbSize == null) {
                this.minimumThumbSize = new Dimension();
                this.validMinimumThumbSize = false;
            } else {
                this.validMinimumThumbSize = true;
            }
            this.maximumThumbSize = (Dimension) this.style.get(context, "ScrollBar.maximumThumbSize");
            if (this.maximumThumbSize == null) {
                this.maximumThumbSize = new Dimension(4096, 4097);
            }
            this.incrGap = this.style.getInt(context, "ScrollBar.incrementButtonGap", 0);
            this.decrGap = this.style.getInt(context, "ScrollBar.decrementButtonGap", 0);
            String str = (String) this.scrollbar.getClientProperty("JComponent.sizeVariant");
            if (str != null) {
                if (NimbusStyle.LARGE_KEY.equals(str)) {
                    this.scrollBarWidth = (int) (this.scrollBarWidth * 1.15d);
                    this.incrGap = (int) (this.incrGap * 1.15d);
                    this.decrGap = (int) (this.decrGap * 1.15d);
                } else if (NimbusStyle.SMALL_KEY.equals(str)) {
                    this.scrollBarWidth = (int) (this.scrollBarWidth * 0.857d);
                    this.incrGap = (int) (this.incrGap * 0.857d);
                    this.decrGap = (int) (this.decrGap * 0.857d);
                } else if (NimbusStyle.MINI_KEY.equals(str)) {
                    this.scrollBarWidth = (int) (this.scrollBarWidth * 0.714d);
                    this.incrGap = (int) (this.incrGap * 0.714d);
                    this.decrGap = (int) (this.decrGap * 0.714d);
                }
            }
            if (synthStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }
        context.dispose();
        SynthContext context2 = getContext(jScrollBar, Region.SCROLL_BAR_TRACK, 1);
        this.trackStyle = SynthLookAndFeel.updateStyle(context2, this);
        context2.dispose();
        SynthContext context3 = getContext(jScrollBar, Region.SCROLL_BAR_THUMB, 1);
        this.thumbStyle = SynthLookAndFeel.updateStyle(context3, this);
        context3.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected void installListeners() {
        super.installListeners();
        this.scrollbar.addPropertyChangeListener(this);
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected void uninstallListeners() {
        super.uninstallListeners();
        this.scrollbar.removePropertyChangeListener(this);
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected void uninstallDefaults() {
        SynthContext context = getContext(this.scrollbar, 1);
        this.style.uninstallDefaults(context);
        context.dispose();
        this.style = null;
        SynthContext context2 = getContext(this.scrollbar, Region.SCROLL_BAR_TRACK, 1);
        this.trackStyle.uninstallDefaults(context2);
        context2.dispose();
        this.trackStyle = null;
        SynthContext context3 = getContext(this.scrollbar, Region.SCROLL_BAR_THUMB, 1);
        this.thumbStyle.uninstallDefaults(context3);
        context3.dispose();
        this.thumbStyle = null;
        super.uninstallDefaults();
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public SynthContext getContext(JComponent jComponent) {
        return getContext(jComponent, SynthLookAndFeel.getComponentState(jComponent));
    }

    private SynthContext getContext(JComponent jComponent, int i2) {
        return SynthContext.getContext(jComponent, this.style, i2);
    }

    private SynthContext getContext(JComponent jComponent, Region region) {
        return getContext(jComponent, region, getComponentState(jComponent, region));
    }

    private SynthContext getContext(JComponent jComponent, Region region, int i2) {
        SynthStyle synthStyle = this.trackStyle;
        if (region == Region.SCROLL_BAR_THUMB) {
            synthStyle = this.thumbStyle;
        }
        return SynthContext.getContext(jComponent, region, synthStyle, i2);
    }

    private int getComponentState(JComponent jComponent, Region region) {
        if (region == Region.SCROLL_BAR_THUMB && isThumbRollover() && jComponent.isEnabled()) {
            return 2;
        }
        return SynthLookAndFeel.getComponentState(jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    public boolean getSupportsAbsolutePositioning() {
        SynthContext context = getContext(this.scrollbar);
        boolean z2 = this.style.getBoolean(context, "ScrollBar.allowsAbsolutePositioning", false);
        context.dispose();
        return z2;
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        SynthLookAndFeel.update(context, graphics);
        context.getPainter().paintScrollBarBackground(context, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight(), this.scrollbar.getOrientation());
        paint(context, graphics);
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        paint(context, graphics);
        context.dispose();
    }

    protected void paint(SynthContext synthContext, Graphics graphics) {
        SynthContext context = getContext(this.scrollbar, Region.SCROLL_BAR_TRACK);
        paintTrack(context, graphics, getTrackBounds());
        context.dispose();
        SynthContext context2 = getContext(this.scrollbar, Region.SCROLL_BAR_THUMB);
        paintThumb(context2, graphics, getThumbBounds());
        context2.dispose();
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintScrollBarBorder(synthContext, graphics, i2, i3, i4, i5, this.scrollbar.getOrientation());
    }

    protected void paintTrack(SynthContext synthContext, Graphics graphics, Rectangle rectangle) {
        SynthLookAndFeel.updateSubregion(synthContext, graphics, rectangle);
        synthContext.getPainter().paintScrollBarTrackBackground(synthContext, graphics, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height, this.scrollbar.getOrientation());
        synthContext.getPainter().paintScrollBarTrackBorder(synthContext, graphics, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height, this.scrollbar.getOrientation());
    }

    protected void paintThumb(SynthContext synthContext, Graphics graphics, Rectangle rectangle) {
        SynthLookAndFeel.updateSubregion(synthContext, graphics, rectangle);
        int orientation = this.scrollbar.getOrientation();
        synthContext.getPainter().paintScrollBarThumbBackground(synthContext, graphics, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height, orientation);
        synthContext.getPainter().paintScrollBarThumbBorder(synthContext, graphics, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height, orientation);
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        Insets insets = jComponent.getInsets();
        return this.scrollbar.getOrientation() == 1 ? new Dimension(this.scrollBarWidth + insets.left + insets.right, 48) : new Dimension(48, this.scrollBarWidth + insets.top + insets.bottom);
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected Dimension getMinimumThumbSize() {
        if (!this.validMinimumThumbSize) {
            if (this.scrollbar.getOrientation() == 1) {
                this.minimumThumbSize.width = this.scrollBarWidth;
                this.minimumThumbSize.height = 7;
            } else {
                this.minimumThumbSize.width = 7;
                this.minimumThumbSize.height = this.scrollBarWidth;
            }
        }
        return this.minimumThumbSize;
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected JButton createDecreaseButton(int i2) {
        SynthArrowButton synthArrowButton = new SynthArrowButton(i2) { // from class: javax.swing.plaf.synth.SynthScrollBarUI.1
            @Override // javax.swing.JComponent, java.awt.Component
            public boolean contains(int i3, int i4) {
                if (SynthScrollBarUI.this.decrGap < 0) {
                    int width = getWidth();
                    int height = getHeight();
                    if (SynthScrollBarUI.this.scrollbar.getOrientation() == 1) {
                        height += SynthScrollBarUI.this.decrGap;
                    } else {
                        width += SynthScrollBarUI.this.decrGap;
                    }
                    return i3 >= 0 && i3 < width && i4 >= 0 && i4 < height;
                }
                return super.contains(i3, i4);
            }
        };
        synthArrowButton.setName("ScrollBar.button");
        return synthArrowButton;
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected JButton createIncreaseButton(int i2) {
        SynthArrowButton synthArrowButton = new SynthArrowButton(i2) { // from class: javax.swing.plaf.synth.SynthScrollBarUI.2
            @Override // javax.swing.JComponent, java.awt.Component
            public boolean contains(int i3, int i4) {
                if (SynthScrollBarUI.this.incrGap < 0) {
                    int width = getWidth();
                    int height = getHeight();
                    if (SynthScrollBarUI.this.scrollbar.getOrientation() == 1) {
                        height += SynthScrollBarUI.this.incrGap;
                        i4 += SynthScrollBarUI.this.incrGap;
                    } else {
                        width += SynthScrollBarUI.this.incrGap;
                        i3 += SynthScrollBarUI.this.incrGap;
                    }
                    return i3 >= 0 && i3 < width && i4 >= 0 && i4 < height;
                }
                return super.contains(i3, i4);
            }
        };
        synthArrowButton.setName("ScrollBar.button");
        return synthArrowButton;
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected void setThumbRollover(boolean z2) {
        if (isThumbRollover() != z2) {
            this.scrollbar.repaint(getThumbBounds());
            super.setThumbRollover(z2);
        }
    }

    private void updateButtonDirections() {
        int orientation = this.scrollbar.getOrientation();
        if (this.scrollbar.getComponentOrientation().isLeftToRight()) {
            ((SynthArrowButton) this.incrButton).setDirection(orientation == 0 ? 3 : 5);
            ((SynthArrowButton) this.decrButton).setDirection(orientation == 0 ? 7 : 1);
        } else {
            ((SynthArrowButton) this.incrButton).setDirection(orientation == 0 ? 7 : 5);
            ((SynthArrowButton) this.decrButton).setDirection(orientation == 0 ? 3 : 1);
        }
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        String propertyName = propertyChangeEvent.getPropertyName();
        if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
            updateStyle((JScrollBar) propertyChangeEvent.getSource());
        }
        if ("orientation" == propertyName) {
            updateButtonDirections();
        } else if ("componentOrientation" == propertyName) {
            updateButtonDirections();
        }
    }
}
