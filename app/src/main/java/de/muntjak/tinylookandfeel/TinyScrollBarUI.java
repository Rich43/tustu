package de.muntjak.tinylookandfeel;

import de.muntjak.tinylookandfeel.util.ColorRoutines;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyScrollBarUI.class */
public class TinyScrollBarUI extends BasicScrollBarUI {
    private static final HashMap cache = new HashMap();
    static final int ALPHA = 92;
    private int scrollBarWidth;
    protected boolean isRollover = false;
    protected boolean wasRollover = false;
    private boolean freeStanding = false;
    private boolean buttonsEnabled = true;

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyScrollBarUI$MyTrackListener.class */
    protected class MyTrackListener extends BasicScrollBarUI.TrackListener {
        private final TinyScrollBarUI this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        protected MyTrackListener(TinyScrollBarUI tinyScrollBarUI) {
            super();
            this.this$0 = tinyScrollBarUI;
        }

        @Override // javax.swing.plaf.basic.BasicScrollBarUI.TrackListener, java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            super.mouseReleased(mouseEvent);
            this.this$0.scrollbar.repaint();
        }

        @Override // javax.swing.plaf.basic.BasicScrollBarUI.TrackListener, java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            super.mousePressed(mouseEvent);
            this.this$0.scrollbar.repaint();
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            this.this$0.isRollover = false;
            this.this$0.wasRollover = false;
            if (this.this$0.getThumbBounds().contains(mouseEvent.getPoint())) {
                this.this$0.isRollover = true;
                this.this$0.wasRollover = this.this$0.isRollover;
                this.this$0.scrollbar.repaint();
            }
        }

        @Override // javax.swing.plaf.basic.BasicScrollBarUI.TrackListener, java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            this.this$0.isRollover = false;
            if (this.this$0.isRollover != this.this$0.wasRollover) {
                this.this$0.wasRollover = this.this$0.isRollover;
                this.this$0.scrollbar.repaint();
            }
        }

        @Override // javax.swing.plaf.basic.BasicScrollBarUI.TrackListener, java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            if (this.this$0.getThumbBounds().contains(mouseEvent.getPoint())) {
                this.this$0.isDragging = true;
            }
            super.mouseDragged(mouseEvent);
        }

        @Override // javax.swing.plaf.basic.BasicScrollBarUI.TrackListener, java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
            if (this.this$0.getThumbBounds().contains(mouseEvent.getPoint())) {
                this.this$0.isRollover = true;
                if (this.this$0.isRollover != this.this$0.wasRollover) {
                    this.this$0.scrollbar.repaint();
                    this.this$0.wasRollover = this.this$0.isRollover;
                    return;
                }
                return;
            }
            this.this$0.isRollover = false;
            if (this.this$0.isRollover != this.this$0.wasRollover) {
                this.this$0.scrollbar.repaint();
                this.this$0.wasRollover = this.this$0.isRollover;
            }
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyScrollBarUI$OrientationChangeListener.class */
    protected class OrientationChangeListener implements PropertyChangeListener {
        private final TinyScrollBarUI this$0;

        protected OrientationChangeListener(TinyScrollBarUI tinyScrollBarUI) {
            this.this$0 = tinyScrollBarUI;
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            Integer num = (Integer) propertyChangeEvent.getNewValue();
            if (this.this$0.scrollbar.getComponentOrientation().isLeftToRight()) {
                if (this.this$0.incrButton instanceof TinyScrollButton) {
                    ((TinyScrollButton) this.this$0.incrButton).setDirection(num.intValue() == 0 ? 3 : 5);
                }
                if (this.this$0.decrButton instanceof TinyScrollButton) {
                    ((TinyScrollButton) this.this$0.decrButton).setDirection(num.intValue() == 0 ? 7 : 1);
                    return;
                }
                return;
            }
            if (this.this$0.incrButton instanceof TinyScrollButton) {
                ((TinyScrollButton) this.this$0.incrButton).setDirection(num.intValue() == 0 ? 7 : 5);
            }
            if (this.this$0.decrButton instanceof TinyScrollButton) {
                ((TinyScrollButton) this.this$0.decrButton).setDirection(num.intValue() == 0 ? 3 : 1);
            }
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyScrollBarUI$ThumbKey.class */
    private static class ThumbKey {

        /* renamed from: c, reason: collision with root package name */
        private Color f12134c;
        private int spread1;
        private int spread2;
        private Dimension size;
        private boolean vertical;

        ThumbKey(Color color, int i2, int i3, boolean z2, Dimension dimension) {
            this.f12134c = color;
            this.spread1 = i2;
            this.spread2 = i3;
            this.vertical = z2;
            this.size = dimension;
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof ThumbKey)) {
                return false;
            }
            ThumbKey thumbKey = (ThumbKey) obj;
            return this.spread1 == thumbKey.spread1 && this.spread2 == thumbKey.spread2 && this.vertical == thumbKey.vertical && this.size.equals(thumbKey.size) && this.f12134c.equals(thumbKey.f12134c);
        }

        public int hashCode() {
            return this.f12134c.hashCode() * this.size.hashCode() * (this.vertical ? 1 : 2) * this.spread1 * this.spread2;
        }
    }

    public static void clearCache() {
        cache.clear();
    }

    private void calculateButtonsEnabled() {
        this.buttonsEnabled = ((float) (this.scrollbar.getMaximum() - this.scrollbar.getMinimum())) > ((float) this.scrollbar.getVisibleAmount());
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected void layoutVScrollbar(JScrollBar jScrollBar) {
        super.layoutVScrollbar(jScrollBar);
        calculateButtonsEnabled();
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected void layoutHScrollbar(JScrollBar jScrollBar) {
        super.layoutHScrollbar(jScrollBar);
        calculateButtonsEnabled();
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected void installDefaults() {
        this.scrollBarWidth = Theme.scrollSize.getValue();
        super.installDefaults();
        this.scrollbar.setBorder(null);
        this.minimumThumbSize = new Dimension(17, 17);
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new TinyScrollBarUI();
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected JButton createDecreaseButton(int i2) {
        return new TinyScrollButton(i2, this);
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected JButton createIncreaseButton(int i2) {
        return new TinyScrollButton(i2, this);
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        return this.scrollbar.getOrientation() == 1 ? new Dimension(this.scrollBarWidth, (this.scrollBarWidth * 3) + 10) : new Dimension((this.scrollBarWidth * 3) + 10, this.scrollBarWidth);
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    public void paintTrack(Graphics graphics, JComponent jComponent, Rectangle rectangle) {
        drawXpTrack(graphics, rectangle);
    }

    private void drawXpTrack(Graphics graphics, Rectangle rectangle) {
        if (isThumbVisible()) {
            graphics.setColor(Theme.scrollTrackColor.getColor());
            graphics.fillRect(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
            graphics.setColor(Theme.scrollTrackBorderColor.getColor());
        } else {
            graphics.setColor(Theme.scrollTrackDisabledColor.getColor());
            graphics.fillRect(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
            graphics.setColor(Theme.scrollTrackBorderDisabledColor.getColor());
        }
        if (this.scrollbar.getOrientation() == 1) {
            graphics.drawLine(rectangle.f12372x, rectangle.f12373y, rectangle.f12372x, (rectangle.f12373y + rectangle.height) - 1);
            graphics.drawLine((rectangle.f12372x + rectangle.width) - 1, rectangle.f12373y, (rectangle.f12372x + rectangle.width) - 1, (rectangle.f12373y + rectangle.height) - 1);
        } else {
            graphics.drawLine(rectangle.f12372x, rectangle.f12373y, (rectangle.f12372x + rectangle.width) - 1, rectangle.f12373y);
            graphics.drawLine(rectangle.f12372x, (rectangle.f12373y + rectangle.height) - 1, (rectangle.f12372x + rectangle.width) - 1, (rectangle.f12373y + rectangle.height) - 1);
        }
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    public void paintThumb(Graphics graphics, JComponent jComponent, Rectangle rectangle) {
        if (rectangle.isEmpty() || !this.scrollbar.isEnabled()) {
            return;
        }
        if (TinyLookAndFeel.controlPanelInstantiated) {
            drawXpThumbNoCache(graphics, jComponent, rectangle);
        } else {
            drawXpThumb(graphics, jComponent, rectangle);
        }
    }

    private void drawXpThumb(Graphics graphics, JComponent jComponent, Rectangle rectangle) {
        int value = Theme.scrollSpreadLight.getValue();
        int value2 = Theme.scrollSpreadDark.getValue();
        ColorUIResource color = (this.isDragging && this.isRollover) ? Theme.scrollThumbPressedColor.getColor() : (this.isRollover && Theme.scrollRollover.getValue()) ? Theme.scrollThumbRolloverColor.getColor() : Theme.scrollThumbColor.getColor();
        ThumbKey thumbKey = new ThumbKey(color, value, value2, this.scrollbar.getOrientation() == 1, rectangle.getSize());
        Object obj = cache.get(thumbKey);
        if (obj != null) {
            graphics.drawImage((Image) obj, rectangle.f12372x, rectangle.f12373y, jComponent);
            return;
        }
        BufferedImage bufferedImage = new BufferedImage(rectangle.width, rectangle.height, 2);
        Graphics graphics2 = bufferedImage.getGraphics();
        graphics2.setColor(color);
        int i2 = rectangle.width - 1;
        int i3 = rectangle.height - 1;
        float f2 = (10.0f * value) / 10.0f;
        float f3 = (10.0f * value2) / 10.0f;
        switch (this.scrollbar.getOrientation()) {
            case 0:
                int i4 = rectangle.height - 2;
                int i5 = (rectangle.height * 3) / 8;
                for (int i6 = 1; i6 < i4; i6++) {
                    if (i6 < i5) {
                        graphics2.setColor(ColorRoutines.lighten(color, (int) ((i5 - i6) * f2)));
                    } else if (i6 == i5) {
                        graphics2.setColor(color);
                    } else {
                        graphics2.setColor(ColorRoutines.darken(color, (int) ((i6 - i5) * f3)));
                    }
                    graphics2.drawLine(0 + 1, 0 + i6, i2 - 2, 0 + i6);
                }
                graphics2.setColor(Theme.scrollBorderLightColor.getColor());
                graphics2.drawLine(0 + 2, (0 + rectangle.height) - 2, i2 - 2, (0 + rectangle.height) - 2);
                graphics2.drawLine(i2 - 1, 0 + 3, i2 - 1, 0 + 14);
                graphics2.setColor(Theme.scrollBorderColor.getColor());
                graphics2.drawRect(0, 0 + 1, i2 - 0, (0 + rectangle.height) - 2);
                graphics2.setColor(Theme.scrollTrackBorderColor.getColor());
                graphics2.drawLine(0, 0, i2, 0);
                ColorUIResource color2 = Theme.scrollBorderColor.getColor();
                graphics2.setColor(new Color(color2.getRed(), color2.getGreen(), color2.getBlue(), 92));
                graphics2.drawLine(0 + 1, 0 + 2, 0 + 1, 0 + 2);
                graphics2.drawLine(0 + 1, (0 + rectangle.height) - 2, 0 + 1, (0 + rectangle.height) - 2);
                graphics2.drawLine(i2 - 1, 0 + 2, i2 - 1, 0 + 2);
                graphics2.drawLine(i2 - 1, (0 + rectangle.height) - 2, i2 - 1, (0 + rectangle.height) - 2);
                ColorUIResource color3 = Theme.scrollBorderLightColor.getColor();
                graphics2.setColor(new Color(color3.getRed(), color3.getGreen(), color3.getBlue(), 92));
                graphics2.drawLine(0, 0 + 1, 0, 0 + 1);
                graphics2.drawLine(0, (0 + rectangle.height) - 1, 0, (0 + rectangle.height) - 1);
                graphics2.drawLine(i2, 0 + 1, i2, 0 + 1);
                graphics2.drawLine(i2, (0 + rectangle.height) - 1, i2, (0 + rectangle.height) - 1);
                break;
            case 1:
                int i7 = rectangle.width - 2;
                int i8 = (rectangle.width * 3) / 8;
                for (int i9 = 1; i9 < i7; i9++) {
                    if (i9 < i8) {
                        graphics2.setColor(ColorRoutines.lighten(color, (int) ((i8 - i9) * f2)));
                    } else if (i9 == i8) {
                        graphics2.setColor(color);
                    } else {
                        graphics2.setColor(ColorRoutines.darken(color, (int) ((i9 - i8) * f3)));
                    }
                    graphics2.drawLine(0 + i9, 0 + 2, 0 + i9, i3 - 1);
                }
                graphics2.setColor(Theme.scrollBorderLightColor.getColor());
                graphics2.drawLine(0 + 3, 0 + 1, (0 + rectangle.width) - 3, 0 + 1);
                graphics2.drawLine((0 + rectangle.width) - 2, 0 + 2, (0 + rectangle.width) - 2, i3 - 2);
                graphics2.setColor(Theme.scrollBorderColor.getColor());
                graphics2.drawRect(0 + 1, 0, (0 + rectangle.width) - 2, i3 - 0);
                ColorUIResource color4 = Theme.scrollBorderColor.getColor();
                graphics2.setColor(new Color(color4.getRed(), color4.getGreen(), color4.getBlue(), 92));
                graphics2.drawLine(0 + 2, 0 + 1, 0 + 2, 0 + 1);
                graphics2.drawLine((0 + rectangle.width) - 2, 0 + 1, (0 + rectangle.width) - 2, 0 + 1);
                graphics2.drawLine(0 + 2, i3 - 1, 0 + 2, i3 - 1);
                graphics2.drawLine((0 + rectangle.width) - 2, i3 - 1, (0 + rectangle.width) - 2, i3 - 1);
                ColorUIResource color5 = Theme.scrollBorderLightColor.getColor();
                graphics2.setColor(new Color(color5.getRed(), color5.getGreen(), color5.getBlue(), 92));
                graphics2.drawLine(0 + 1, 0, 0 + 1, 0);
                graphics2.drawLine((0 + rectangle.width) - 1, 0, (0 + rectangle.width) - 1, 0);
                graphics2.drawLine(0 + 1, i3, 0 + 1, i3);
                graphics2.drawLine((0 + rectangle.width) - 1, i3, (0 + rectangle.width) - 1, i3);
                break;
        }
        if (rectangle.height < 13) {
            return;
        }
        if (this.scrollbar.getOrientation() == 1) {
            int i10 = (rectangle.height / 2) - 4;
            int iMin = Math.min(i10 + 8, rectangle.height - 3);
            int i11 = (rectangle.width / 4) + 1;
            int i12 = (rectangle.width - (rectangle.width / 4)) - 2;
            graphics2.setColor(ColorRoutines.getAdjustedColor(color, Theme.scrollGripLightColor.getSaturation(), Theme.scrollGripLightColor.getBrightness()));
            for (int i13 = i10 + 1; i13 < iMin; i13 += 2) {
                graphics2.drawLine(i11, i13, i12, i13);
            }
            int i14 = (rectangle.width / 4) + 2;
            int i15 = (rectangle.width - (rectangle.width / 4)) - 1;
            graphics2.setColor(ColorRoutines.getAdjustedColor(color, Theme.scrollGripDarkColor.getSaturation(), Theme.scrollGripDarkColor.getBrightness()));
            for (int i16 = i10; i16 < iMin; i16 += 2) {
                graphics2.drawLine(i14, i16, i15, i16);
            }
        } else {
            int i17 = (rectangle.width / 2) - 4;
            int iMin2 = Math.min(i17 + 8, rectangle.width - 3);
            int i18 = (rectangle.height / 4) + 1;
            int i19 = (rectangle.height - (rectangle.height / 4)) - 2;
            graphics2.setColor(ColorRoutines.getAdjustedColor(color, Theme.scrollGripLightColor.getSaturation(), Theme.scrollGripLightColor.getBrightness()));
            for (int i20 = i17 + 1; i20 < iMin2; i20 += 2) {
                graphics2.drawLine(i20, i18, i20, i19);
            }
            int i21 = (rectangle.height / 4) + 2;
            int i22 = (rectangle.height - (rectangle.height / 4)) - 1;
            graphics2.setColor(ColorRoutines.getAdjustedColor(color, Theme.scrollGripDarkColor.getSaturation(), Theme.scrollGripDarkColor.getBrightness()));
            for (int i23 = i17; i23 < iMin2; i23 += 2) {
                graphics2.drawLine(i23, i21, i23, i22);
            }
        }
        graphics2.dispose();
        graphics.drawImage(bufferedImage, rectangle.f12372x, rectangle.f12373y, jComponent);
        cache.put(thumbKey, bufferedImage);
    }

    private void drawXpThumbNoCache(Graphics graphics, JComponent jComponent, Rectangle rectangle) {
        ColorUIResource color = (this.isDragging && this.isRollover) ? Theme.scrollThumbPressedColor.getColor() : (this.isRollover && Theme.scrollRollover.getValue()) ? Theme.scrollThumbRolloverColor.getColor() : Theme.scrollThumbColor.getColor();
        graphics.setColor(color);
        int i2 = (rectangle.f12372x + rectangle.width) - 1;
        int i3 = (rectangle.f12373y + rectangle.height) - 1;
        float value = (10.0f * Theme.scrollSpreadLight.getValue()) / 10.0f;
        float value2 = (10.0f * Theme.scrollSpreadDark.getValue()) / 10.0f;
        switch (this.scrollbar.getOrientation()) {
            case 0:
                int i4 = rectangle.height - 2;
                int i5 = (rectangle.height * 3) / 8;
                for (int i6 = 1; i6 < i4; i6++) {
                    if (i6 < i5) {
                        graphics.setColor(ColorRoutines.lighten(color, (int) ((i5 - i6) * value)));
                    } else if (i6 == i5) {
                        graphics.setColor(color);
                    } else {
                        graphics.setColor(ColorRoutines.darken(color, (int) ((i6 - i5) * value2)));
                    }
                    graphics.drawLine(rectangle.f12372x + 1, rectangle.f12373y + i6, i2 - 2, rectangle.f12373y + i6);
                }
                graphics.setColor(Theme.scrollBorderLightColor.getColor());
                graphics.drawLine(rectangle.f12372x + 2, (rectangle.f12373y + rectangle.height) - 2, i2 - 2, (rectangle.f12373y + rectangle.height) - 2);
                graphics.drawLine(i2 - 1, rectangle.f12373y + 3, i2 - 1, rectangle.f12373y + 14);
                graphics.setColor(Theme.scrollBorderColor.getColor());
                graphics.drawRect(rectangle.f12372x, rectangle.f12373y + 1, i2 - rectangle.f12372x, (rectangle.f12373y + rectangle.height) - 2);
                graphics.setColor(Theme.scrollTrackBorderColor.getColor());
                graphics.drawLine(rectangle.f12372x, rectangle.f12373y, i2, rectangle.f12373y);
                ColorUIResource color2 = Theme.scrollBorderColor.getColor();
                graphics.setColor(new Color(color2.getRed(), color2.getGreen(), color2.getBlue(), 92));
                graphics.drawLine(rectangle.f12372x + 1, rectangle.f12373y + 2, rectangle.f12372x + 1, rectangle.f12373y + 2);
                graphics.drawLine(rectangle.f12372x + 1, (rectangle.f12373y + rectangle.height) - 2, rectangle.f12372x + 1, (rectangle.f12373y + rectangle.height) - 2);
                graphics.drawLine(i2 - 1, rectangle.f12373y + 2, i2 - 1, rectangle.f12373y + 2);
                graphics.drawLine(i2 - 1, (rectangle.f12373y + rectangle.height) - 2, i2 - 1, (rectangle.f12373y + rectangle.height) - 2);
                ColorUIResource color3 = Theme.scrollBorderLightColor.getColor();
                graphics.setColor(new Color(color3.getRed(), color3.getGreen(), color3.getBlue(), 92));
                graphics.drawLine(rectangle.f12372x, rectangle.f12373y + 1, rectangle.f12372x, rectangle.f12373y + 1);
                graphics.drawLine(rectangle.f12372x, (rectangle.f12373y + rectangle.height) - 1, rectangle.f12372x, (rectangle.f12373y + rectangle.height) - 1);
                graphics.drawLine(i2, rectangle.f12373y + 1, i2, rectangle.f12373y + 1);
                graphics.drawLine(i2, (rectangle.f12373y + rectangle.height) - 1, i2, (rectangle.f12373y + rectangle.height) - 1);
                break;
            case 1:
                int i7 = rectangle.width - 2;
                int i8 = (rectangle.width * 3) / 8;
                for (int i9 = 1; i9 < i7; i9++) {
                    if (i9 < i8) {
                        graphics.setColor(ColorRoutines.lighten(color, (int) ((i8 - i9) * value)));
                    } else if (i9 == i8) {
                        graphics.setColor(color);
                    } else {
                        graphics.setColor(ColorRoutines.darken(color, (int) ((i9 - i8) * value2)));
                    }
                    graphics.drawLine(rectangle.f12372x + i9, rectangle.f12373y + 2, rectangle.f12372x + i9, i3 - 1);
                }
                graphics.setColor(Theme.scrollBorderLightColor.getColor());
                graphics.drawLine(rectangle.f12372x + 3, rectangle.f12373y + 1, (rectangle.f12372x + rectangle.width) - 3, rectangle.f12373y + 1);
                graphics.drawLine((rectangle.f12372x + rectangle.width) - 2, rectangle.f12373y + 2, (rectangle.f12372x + rectangle.width) - 2, i3 - 2);
                graphics.setColor(Theme.scrollBorderColor.getColor());
                graphics.drawRect(rectangle.f12372x + 1, rectangle.f12373y, (rectangle.f12372x + rectangle.width) - 2, i3 - rectangle.f12373y);
                ColorUIResource color4 = Theme.scrollBorderColor.getColor();
                graphics.setColor(new Color(color4.getRed(), color4.getGreen(), color4.getBlue(), 92));
                graphics.drawLine(rectangle.f12372x + 2, rectangle.f12373y + 1, rectangle.f12372x + 2, rectangle.f12373y + 1);
                graphics.drawLine((rectangle.f12372x + rectangle.width) - 2, rectangle.f12373y + 1, (rectangle.f12372x + rectangle.width) - 2, rectangle.f12373y + 1);
                graphics.drawLine(rectangle.f12372x + 2, i3 - 1, rectangle.f12372x + 2, i3 - 1);
                graphics.drawLine((rectangle.f12372x + rectangle.width) - 2, i3 - 1, (rectangle.f12372x + rectangle.width) - 2, i3 - 1);
                ColorUIResource color5 = Theme.scrollBorderLightColor.getColor();
                graphics.setColor(new Color(color5.getRed(), color5.getGreen(), color5.getBlue(), 92));
                graphics.drawLine(rectangle.f12372x + 1, rectangle.f12373y, rectangle.f12372x + 1, rectangle.f12373y);
                graphics.drawLine((rectangle.f12372x + rectangle.width) - 1, rectangle.f12373y, (rectangle.f12372x + rectangle.width) - 1, rectangle.f12373y);
                graphics.drawLine(rectangle.f12372x + 1, i3, rectangle.f12372x + 1, i3);
                graphics.drawLine((rectangle.f12372x + rectangle.width) - 1, i3, (rectangle.f12372x + rectangle.width) - 1, i3);
                break;
        }
        if (rectangle.height < 13) {
            return;
        }
        if (this.scrollbar.getOrientation() == 1) {
            int i10 = (rectangle.f12373y + (rectangle.height / 2)) - 4;
            int iMin = Math.min(i10 + 8, (rectangle.f12373y + rectangle.height) - 3);
            int i11 = rectangle.f12372x + (rectangle.width / 4) + 1;
            int i12 = ((rectangle.f12372x + rectangle.width) - (rectangle.width / 4)) - 2;
            graphics.setColor(ColorRoutines.getAdjustedColor(color, Theme.scrollGripLightColor.getSaturation(), Theme.scrollGripLightColor.getBrightness()));
            for (int i13 = i10 + 1; i13 < iMin; i13 += 2) {
                graphics.drawLine(i11, i13, i12, i13);
            }
            int i14 = rectangle.f12372x + (rectangle.width / 4) + 2;
            int i15 = ((rectangle.f12372x + rectangle.width) - (rectangle.width / 4)) - 1;
            graphics.setColor(ColorRoutines.getAdjustedColor(color, Theme.scrollGripDarkColor.getSaturation(), Theme.scrollGripDarkColor.getBrightness()));
            for (int i16 = i10; i16 < iMin; i16 += 2) {
                graphics.drawLine(i14, i16, i15, i16);
            }
            return;
        }
        int i17 = (rectangle.f12372x + (rectangle.width / 2)) - 4;
        int iMin2 = Math.min(i17 + 8, (rectangle.f12372x + rectangle.width) - 3);
        int i18 = rectangle.f12373y + (rectangle.height / 4) + 1;
        int i19 = ((rectangle.f12373y + rectangle.height) - (rectangle.height / 4)) - 2;
        graphics.setColor(ColorRoutines.getAdjustedColor(color, Theme.scrollGripLightColor.getSaturation(), Theme.scrollGripLightColor.getBrightness()));
        for (int i20 = i17 + 1; i20 < iMin2; i20 += 2) {
            graphics.drawLine(i20, i18, i20, i19);
        }
        int i21 = rectangle.f12373y + (rectangle.height / 4) + 2;
        int i22 = ((rectangle.f12373y + rectangle.height) - (rectangle.height / 4)) - 1;
        graphics.setColor(ColorRoutines.getAdjustedColor(color, Theme.scrollGripDarkColor.getSaturation(), Theme.scrollGripDarkColor.getBrightness()));
        for (int i23 = i17; i23 < iMin2; i23 += 2) {
            graphics.drawLine(i23, i21, i23, i22);
        }
    }

    public boolean isThumbVisible() {
        return this.buttonsEnabled;
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected BasicScrollBarUI.TrackListener createTrackListener() {
        return new MyTrackListener(this);
    }
}
