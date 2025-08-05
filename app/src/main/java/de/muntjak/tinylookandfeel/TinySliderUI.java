package de.muntjak.tinylookandfeel;

import de.muntjak.tinylookandfeel.util.ColorRoutines;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSliderUI;
import javax.swing.plaf.metal.MetalSliderUI;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinySliderUI.class */
public class TinySliderUI extends MetalSliderUI {
    private static final BasicStroke focusStroke = new BasicStroke(1.0f, 0, 2, 1.0f, new float[]{1.0f, 1.0f}, 0.0f);
    private static final Dimension sliderVertSize = new Dimension(22, 11);
    private static final Dimension sliderHorzSize = new Dimension(11, 22);
    protected boolean isRollover = false;
    protected boolean wasRollover = false;
    protected boolean isDragging = false;
    protected BasicSliderUI.TrackListener trackListener;

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinySliderUI$MyTrackListener.class */
    class MyTrackListener extends BasicSliderUI.TrackListener {
        private final TinySliderUI this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        MyTrackListener(TinySliderUI tinySliderUI) {
            super();
            this.this$0 = tinySliderUI;
        }

        @Override // javax.swing.plaf.basic.BasicSliderUI.TrackListener, java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            super.mouseReleased(mouseEvent);
            this.this$0.isDragging = false;
            this.this$0.slider.repaint();
        }

        @Override // javax.swing.plaf.basic.BasicSliderUI.TrackListener, java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            super.mousePressed(mouseEvent);
            if (this.this$0.thumbRect.contains(mouseEvent.getX(), mouseEvent.getY())) {
                this.this$0.isDragging = true;
            }
            this.this$0.slider.repaint();
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            this.this$0.isRollover = false;
            this.this$0.wasRollover = false;
            if (this.this$0.thumbRect.contains(mouseEvent.getX(), mouseEvent.getY())) {
                this.this$0.isRollover = true;
            }
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            this.this$0.isRollover = false;
            if (this.this$0.isRollover != this.this$0.wasRollover) {
                this.this$0.slider.repaint();
                this.this$0.wasRollover = this.this$0.isRollover;
            }
        }

        @Override // javax.swing.plaf.basic.BasicSliderUI.TrackListener, java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            if (this.this$0.thumbRect.contains(mouseEvent.getX(), mouseEvent.getY())) {
                this.this$0.isRollover = true;
            }
            super.mouseDragged(mouseEvent);
        }

        @Override // javax.swing.plaf.basic.BasicSliderUI.TrackListener, java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
            if (this.this$0.thumbRect.contains(mouseEvent.getX(), mouseEvent.getY())) {
                this.this$0.isRollover = true;
                if (this.this$0.isRollover != this.this$0.wasRollover) {
                    this.this$0.slider.repaint();
                    this.this$0.wasRollover = this.this$0.isRollover;
                    return;
                }
                return;
            }
            this.this$0.isRollover = false;
            if (this.this$0.isRollover != this.this$0.wasRollover) {
                this.this$0.slider.repaint();
                this.this$0.wasRollover = this.this$0.isRollover;
            }
        }
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    protected BasicSliderUI.TrackListener createTrackListener(JSlider jSlider) {
        return new MyTrackListener(this);
    }

    @Override // javax.swing.plaf.metal.MetalSliderUI, javax.swing.plaf.basic.BasicSliderUI
    protected Dimension getThumbSize() {
        return this.slider.getOrientation() == 1 ? sliderVertSize : sliderHorzSize;
    }

    @Override // javax.swing.plaf.metal.MetalSliderUI
    protected int getTrackWidth() {
        return 4;
    }

    @Override // javax.swing.plaf.metal.MetalSliderUI, javax.swing.plaf.basic.BasicSliderUI
    public void paintThumb(Graphics graphics) {
        if (!this.slider.isEnabled()) {
            graphics.setColor(Theme.sliderThumbDisabledColor.getColor());
            drawXpThumbDisabled(graphics);
            return;
        }
        if (this.isDragging) {
            graphics.setColor(Theme.sliderThumbPressedColor.getColor());
        } else if (!this.isRollover || Theme.sliderRolloverEnabled.getValue()) {
            graphics.setColor(Theme.sliderThumbColor.getColor());
        } else {
            graphics.setColor(Theme.sliderThumbColor.getColor());
        }
        drawXpThumb(graphics);
    }

    private void drawXpThumb(Graphics graphics) {
        int i2 = this.thumbRect.f12372x;
        int i3 = this.thumbRect.f12373y;
        int i4 = (i2 + this.thumbRect.width) - 1;
        int i5 = (i3 + this.thumbRect.height) - 1;
        Color color = graphics.getColor();
        if (!this.slider.getPaintTicks()) {
            if (this.slider.getOrientation() == 0) {
                graphics.fillRect(i2 + 1, i3 + 1, this.thumbRect.width - 4, this.thumbRect.height - 4);
                graphics.setColor(ColorRoutines.darken(color, 10));
                graphics.drawLine(i4 - 2, i3 + 3, i4 - 2, i5 - 3);
                graphics.setColor(ColorRoutines.darken(color, 20));
                graphics.drawLine(i4 - 1, i3 + 3, i4 - 1, i5 - 3);
                graphics.setColor(Theme.sliderBorderColor.getColor());
                graphics.drawLine(i2, i3 + 1, i2, i5 - 1);
                graphics.drawLine(i2 + 1, i3, i4 - 1, i3);
                graphics.setColor(Theme.sliderDarkColor.getColor());
                graphics.drawLine(i2 + 1, i5, i4 - 1, i5);
                graphics.drawLine(i4, i3 + 1, i4, i5 - 1);
                ColorUIResource color2 = (!this.isDragging && this.isRollover && Theme.sliderRolloverEnabled.getValue()) ? Theme.sliderThumbRolloverColor.getColor() : Theme.sliderLightColor.getColor();
                Color adjustedColor = ColorRoutines.getAdjustedColor(color2, 67, 39);
                graphics.setColor(adjustedColor);
                graphics.drawLine(i2 + 1, i3 + 1, i4 - 1, i3 + 1);
                graphics.setColor(ColorRoutines.getAverage(color2, adjustedColor));
                graphics.drawLine(i2 + 1, i3 + 2, i4 - 1, i3 + 2);
                graphics.drawLine(i2 + 1, i5 - 2, i4 - 1, i5 - 2);
                graphics.setColor(color2);
                graphics.drawLine(i2 + 1, i5 - 1, i4 - 1, i5 - 1);
                return;
            }
            graphics.fillRect(i2 + 1, i3 + 1, this.thumbRect.width - 4, this.thumbRect.height - 4);
            graphics.setColor(ColorRoutines.darken(color, 10));
            graphics.drawLine(i2 + 3, i5 - 2, i4 - 3, i5 - 2);
            graphics.setColor(ColorRoutines.darken(color, 20));
            graphics.drawLine(i2 + 3, i5 - 1, i4 - 3, i5 - 1);
            graphics.setColor(Theme.sliderBorderColor.getColor());
            graphics.drawLine(i2 + 1, i3, i4 - 1, i3);
            graphics.drawLine(i2, i3 + 1, i2, i5 - 1);
            graphics.setColor(Theme.sliderDarkColor.getColor());
            graphics.drawLine(i4, i3 + 1, i4, i5 - 1);
            graphics.drawLine(i2 + 1, i5, i4 - 1, i5);
            ColorUIResource color3 = (!this.isDragging && this.isRollover && Theme.sliderRolloverEnabled.getValue()) ? Theme.sliderThumbRolloverColor.getColor() : Theme.sliderLightColor.getColor();
            Color adjustedColor2 = ColorRoutines.getAdjustedColor(color3, 67, 39);
            graphics.setColor(adjustedColor2);
            graphics.drawLine(i2 + 1, i3 + 1, i2 + 1, i5 - 1);
            graphics.setColor(ColorRoutines.getAverage(color3, adjustedColor2));
            graphics.drawLine(i2 + 2, i3 + 1, i2 + 2, i5 - 1);
            graphics.drawLine(i4 - 2, i3 + 1, i4 - 2, i5 - 1);
            graphics.setColor(color3);
            graphics.drawLine(i4 - 1, i3 + 1, i4 - 1, i5 - 1);
            return;
        }
        if (this.slider.getOrientation() == 0) {
            graphics.fillRect(i2 + 1, i3 + 4, this.thumbRect.width - 4, this.thumbRect.height - 8);
            graphics.drawLine(i2 + 5, i5 - 3, i2 + 5, i5 - 3);
            graphics.setColor(ColorRoutines.darken(color, 10));
            graphics.drawLine(i4 - 2, i3 + 4, i4 - 2, i5 - 6);
            graphics.setColor(ColorRoutines.darken(color, 20));
            graphics.drawLine(i4 - 1, i3 + 4, i4 - 1, i5 - 7);
            ColorUIResource color4 = Theme.sliderBorderColor.getColor();
            graphics.setColor(color4);
            graphics.drawLine(i2 + 1, i3, i4 - 1, i3);
            graphics.drawLine(i2, i3 + 1, i2, i5 - 5);
            graphics.drawLine(i2 + 1, i5 - 4, i2 + 1, i5 - 4);
            graphics.drawLine(i2 + 2, i5 - 3, i2 + 2, i5 - 3);
            graphics.drawLine(i2 + 3, i5 - 2, i2 + 3, i5 - 2);
            graphics.drawLine(i2 + 4, i5 - 1, i2 + 4, i5 - 1);
            graphics.setColor(Theme.sliderDarkColor.getColor());
            graphics.drawLine(i4, i3 + 1, i4, i5 - 5);
            graphics.drawLine(i4 - 1, i5 - 4, i4 - 1, i5 - 4);
            graphics.drawLine(i4 - 2, i5 - 3, i4 - 2, i5 - 3);
            graphics.drawLine(i4 - 3, i5 - 2, i4 - 3, i5 - 2);
            graphics.drawLine(i4 - 4, i5 - 1, i4 - 4, i5 - 1);
            graphics.drawLine(i4 - 5, i5, i4 - 5, i5);
            ColorUIResource color5 = (!this.isDragging && this.isRollover && Theme.sliderRolloverEnabled.getValue()) ? Theme.sliderThumbRolloverColor.getColor() : Theme.sliderLightColor.getColor();
            Color adjustedColor3 = ColorRoutines.getAdjustedColor(color5, 67, 39);
            graphics.setColor(adjustedColor3);
            graphics.drawLine(i2 + 1, i3 + 1, i4 - 1, i3 + 1);
            graphics.drawLine(i2 + 1, i5 - 6, i2 + 1, i5 - 6);
            graphics.drawLine(i2 + 2, i5 - 5, i2 + 2, i5 - 5);
            graphics.drawLine(i2 + 3, i5 - 4, i2 + 3, i5 - 4);
            graphics.setColor(ColorRoutines.getAverage(color4, adjustedColor3));
            graphics.drawLine(i2 + 1, i5 - 5, i2 + 1, i5 - 5);
            graphics.drawLine(i2 + 2, i5 - 4, i2 + 2, i5 - 4);
            graphics.drawLine(i2 + 3, i5 - 3, i2 + 3, i5 - 3);
            Color average = ColorRoutines.getAverage(color5, adjustedColor3);
            graphics.setColor(average);
            graphics.drawLine(i2 + 1, i3 + 2, i4 - 1, i3 + 2);
            graphics.drawLine(i2 + 4, i5 - 3, i2 + 4, i5 - 3);
            graphics.drawLine(i2 + 5, i5 - 2, i2 + 5, i5 - 2);
            graphics.drawLine(i2 + 6, i5 - 3, i2 + 6, i5 - 3);
            graphics.setColor(ColorRoutines.getAverage(color4, average));
            graphics.drawLine(i2 + 4, i5 - 2, i2 + 4, i5 - 2);
            graphics.drawLine(i2 + 5, i5 - 1, i2 + 5, i5 - 1);
            graphics.drawLine(i2 + 6, i5 - 2, i2 + 6, i5 - 2);
            graphics.setColor(color5);
            graphics.drawLine(i2 + 1, i3 + 3, i4 - 1, i3 + 3);
            graphics.drawLine(i2 + 9, i5 - 6, i2 + 9, i5 - 6);
            graphics.drawLine(i2 + 8, i5 - 5, i2 + 8, i5 - 5);
            graphics.drawLine(i2 + 7, i5 - 4, i2 + 7, i5 - 4);
            graphics.setColor(ColorRoutines.getAverage(color4, color5));
            graphics.drawLine(i2 + 9, i5 - 5, i2 + 9, i5 - 5);
            graphics.drawLine(i2 + 8, i5 - 4, i2 + 8, i5 - 4);
            graphics.drawLine(i2 + 7, i5 - 3, i2 + 7, i5 - 3);
            return;
        }
        graphics.fillRect(i2 + 4, i3 + 1, this.thumbRect.width - 8, this.thumbRect.height - 4);
        graphics.drawLine(i4 - 3, i3 + 5, i4 - 3, i3 + 5);
        graphics.setColor(ColorRoutines.darken(color, 10));
        graphics.drawLine(i2 + 4, i5 - 2, i4 - 6, i5 - 2);
        graphics.setColor(ColorRoutines.darken(color, 20));
        graphics.drawLine(i2 + 4, i5 - 1, i4 - 7, i5 - 1);
        ColorUIResource color6 = Theme.sliderBorderColor.getColor();
        graphics.setColor(color6);
        graphics.drawLine(i2, i3 + 1, i2, i5 - 1);
        graphics.drawLine(i2 + 1, i3, i4 - 5, i3);
        graphics.drawLine(i4 - 4, i3 + 1, i4 - 4, i3 + 1);
        graphics.drawLine(i4 - 3, i3 + 2, i4 - 3, i3 + 2);
        graphics.drawLine(i4 - 2, i3 + 3, i4 - 2, i3 + 3);
        graphics.drawLine(i4 - 1, i3 + 4, i4 - 1, i3 + 4);
        graphics.setColor(Theme.sliderDarkColor.getColor());
        graphics.drawLine(i2 + 1, i5, i4 - 5, i5);
        graphics.drawLine(i4 - 4, i5 - 1, i4 - 4, i5 - 1);
        graphics.drawLine(i4 - 3, i5 - 2, i4 - 3, i5 - 2);
        graphics.drawLine(i4 - 2, i5 - 3, i4 - 2, i5 - 3);
        graphics.drawLine(i4 - 1, i5 - 4, i4 - 1, i5 - 4);
        graphics.drawLine(i4, i5 - 5, i4, i5 - 5);
        ColorUIResource color7 = (!this.isDragging && this.isRollover && Theme.sliderRolloverEnabled.getValue()) ? Theme.sliderThumbRolloverColor.getColor() : Theme.sliderLightColor.getColor();
        Color adjustedColor4 = ColorRoutines.getAdjustedColor(color7, 67, 39);
        graphics.setColor(adjustedColor4);
        graphics.drawLine(i2 + 1, i3 + 1, i2 + 1, i5 - 1);
        graphics.drawLine(i4 - 6, i3 + 1, i4 - 6, i3 + 1);
        graphics.drawLine(i4 - 5, i3 + 2, i4 - 5, i3 + 2);
        graphics.drawLine(i4 - 4, i3 + 3, i4 - 4, i3 + 3);
        graphics.setColor(ColorRoutines.getAverage(color6, adjustedColor4));
        graphics.drawLine(i4 - 5, i3 + 1, i4 - 5, i3 + 1);
        graphics.drawLine(i4 - 4, i3 + 2, i4 - 4, i3 + 2);
        graphics.drawLine(i4 - 3, i3 + 3, i4 - 3, i3 + 3);
        Color average2 = ColorRoutines.getAverage(color7, adjustedColor4);
        graphics.setColor(average2);
        graphics.drawLine(i2 + 2, i3 + 1, i2 + 2, i5 - 1);
        graphics.drawLine(i4 - 3, i3 + 4, i4 - 3, i3 + 4);
        graphics.drawLine(i4 - 2, i3 + 5, i4 - 2, i3 + 5);
        graphics.drawLine(i4 - 3, i3 + 6, i4 - 3, i3 + 6);
        graphics.setColor(ColorRoutines.getAverage(color6, average2));
        graphics.drawLine(i4 - 2, i3 + 4, i4 - 2, i3 + 4);
        graphics.drawLine(i4 - 1, i3 + 5, i4 - 1, i3 + 5);
        graphics.drawLine(i4 - 2, i3 + 6, i4 - 2, i3 + 6);
        graphics.setColor(color7);
        graphics.drawLine(i2 + 3, i3 + 1, i2 + 3, i5 - 1);
        graphics.drawLine(i4 - 6, i3 + 9, i4 - 6, i3 + 9);
        graphics.drawLine(i4 - 5, i3 + 8, i4 - 5, i3 + 8);
        graphics.drawLine(i4 - 4, i3 + 7, i4 - 4, i3 + 7);
        graphics.setColor(ColorRoutines.getAverage(color6, color7));
        graphics.drawLine(i4 - 5, i3 + 9, i4 - 5, i3 + 9);
        graphics.drawLine(i4 - 4, i3 + 8, i4 - 4, i3 + 8);
        graphics.drawLine(i4 - 3, i3 + 7, i4 - 3, i3 + 7);
    }

    private void drawXpThumbDisabled(Graphics graphics) {
        int i2 = this.thumbRect.f12372x;
        int i3 = this.thumbRect.f12373y;
        int i4 = (i2 + this.thumbRect.width) - 1;
        int i5 = (i3 + this.thumbRect.height) - 1;
        Color color = graphics.getColor();
        Color adjustedColor = ColorRoutines.getAdjustedColor(color, 0, -3);
        Color adjustedColor2 = ColorRoutines.getAdjustedColor(color, 0, -7);
        ColorUIResource color2 = Theme.sliderBorderDisabledColor.getColor();
        Color adjustedColor3 = ColorRoutines.getAdjustedColor(color2, -19, -1);
        Color adjustedColor4 = ColorRoutines.getAdjustedColor(color2, 0, -3);
        Color adjustedColor5 = ColorRoutines.getAdjustedColor(color2, 0, -10);
        Color adjustedColor6 = ColorRoutines.getAdjustedColor(color2, 3, -12);
        Color adjustedColor7 = ColorRoutines.getAdjustedColor(color2, 0, -13);
        if (!this.slider.getPaintTicks()) {
            if (this.slider.getOrientation() == 0) {
                graphics.fillRect(i2 + 1, i3 + 3, 7, 16);
                graphics.setColor(adjustedColor);
                graphics.drawLine(i4 - 2, i3 + 3, i4 - 2, i5 - 3);
                graphics.setColor(adjustedColor2);
                graphics.drawLine(i4 - 1, i3 + 3, i4 - 1, i5 - 3);
                graphics.setColor(color2);
                graphics.drawLine(i2, i3 + 1, i2, i5 - 3);
                graphics.drawLine(i2 + 1, i3, i4 - 1, i3);
                graphics.setColor(adjustedColor3);
                graphics.drawLine(i2, i3 + 1, i4, i3 + 1);
                graphics.setColor(adjustedColor4);
                graphics.drawLine(i2, i3 + 2, i4, i3 + 2);
                graphics.setColor(adjustedColor5);
                graphics.drawLine(i2, i5 - 2, i4, i5 - 2);
                graphics.setColor(adjustedColor7);
                graphics.drawLine(i2, i5 - 1, i4, i5 - 1);
                graphics.setColor(adjustedColor6);
                graphics.drawLine(i2 + 1, i5, i4 - 1, i5);
                graphics.drawLine(i4, i3 + 3, i4, i5 - 3);
                return;
            }
            graphics.fillRect(i2 + 3, i3 + 1, this.thumbRect.width - 6, this.thumbRect.height - 4);
            graphics.setColor(adjustedColor);
            graphics.drawLine(i2 + 3, i5 - 2, i4 - 3, i5 - 2);
            graphics.setColor(adjustedColor2);
            graphics.drawLine(i2 + 3, i5 - 1, i4 - 3, i5 - 1);
            graphics.setColor(color2);
            graphics.drawLine(i2, i3 + 1, i2, i5 - 1);
            graphics.drawLine(i2 + 1, i3, i4 - 1, i3);
            graphics.setColor(adjustedColor3);
            graphics.drawLine(i2 + 1, i3, i2 + 1, i5);
            graphics.setColor(adjustedColor4);
            graphics.drawLine(i2 + 2, i3, i2 + 2, i5);
            graphics.setColor(adjustedColor5);
            graphics.drawLine(i4 - 2, i3, i4 - 2, i5);
            graphics.setColor(adjustedColor7);
            graphics.drawLine(i4 - 1, i3, i4 - 1, i5);
            graphics.setColor(adjustedColor6);
            graphics.drawLine(i4, i3 + 1, i4, i5 - 1);
            graphics.drawLine(i2 + 3, i5, i4 - 3, i5);
            return;
        }
        if (this.slider.getOrientation() != 0) {
            graphics.fillRect(i2 + 3, i3 + 1, 13, 7);
            graphics.drawLine(i4 - 5, i3 + 2, i4 - 5, i3 + 6);
            graphics.drawLine(i4 - 4, i3 + 3, i4 - 4, i3 + 5);
            graphics.drawLine(i4 - 3, i3 + 4, i4 - 3, i3 + 4);
            graphics.setColor(adjustedColor);
            graphics.drawLine(i2 + 3, i5 - 2, i4 - 6, i5 - 2);
            graphics.drawLine(i4 - 5, i5 - 3, i4 - 5, i5 - 3);
            graphics.drawLine(i4 - 4, i5 - 4, i4 - 4, i5 - 4);
            graphics.drawLine(i4 - 3, i5 - 5, i4 - 3, i5 - 5);
            graphics.setColor(adjustedColor2);
            graphics.drawLine(i2 + 3, i5 - 1, i4 - 6, i5 - 1);
            graphics.drawLine(i4 - 5, i5 - 2, i4 - 5, i5 - 2);
            graphics.drawLine(i4 - 4, i5 - 3, i4 - 4, i5 - 3);
            graphics.drawLine(i4 - 3, i5 - 4, i4 - 3, i5 - 4);
            graphics.drawLine(i4 - 2, i5 - 5, i4 - 2, i5 - 5);
            graphics.setColor(color2);
            graphics.drawLine(i2, i3 + 1, i2, i5 - 1);
            graphics.drawLine(i2 + 1, i3, i4 - 5, i3);
            graphics.setColor(adjustedColor3);
            graphics.drawLine(i2 + 1, i3, i2 + 1, i5);
            graphics.setColor(adjustedColor4);
            graphics.drawLine(i2 + 2, i3, i2 + 2, i5);
            graphics.drawLine(i4 - 5, i3 + 1, i4 - 4, i3 + 1);
            graphics.drawLine(i4 - 4, i3 + 2, i4 - 3, i3 + 2);
            graphics.drawLine(i4 - 3, i3 + 3, i4 - 2, i3 + 3);
            graphics.drawLine(i4 - 2, i3 + 4, i4 - 1, i3 + 4);
            graphics.setColor(adjustedColor5);
            graphics.drawLine(i4 - 1, i3 + 5, i4, i3 + 5);
            graphics.setColor(adjustedColor6);
            graphics.drawLine(i2 + 3, i5, i4 - 5, i5);
            graphics.setColor(adjustedColor7);
            graphics.drawLine(i4 - 5, i5 - 1, i4 - 4, i5 - 1);
            graphics.drawLine(i4 - 4, i5 - 2, i4 - 3, i5 - 2);
            graphics.drawLine(i4 - 3, i5 - 3, i4 - 2, i5 - 3);
            graphics.drawLine(i4 - 2, i5 - 4, i4 - 1, i5 - 4);
            return;
        }
        graphics.fillRect(i2 + 1, i3 + 3, 7, 13);
        graphics.drawLine(i2 + 2, i5 - 5, i2 + 6, i5 - 5);
        graphics.drawLine(i2 + 3, i5 - 4, i2 + 5, i5 - 4);
        graphics.drawLine(i2 + 4, i5 - 3, i2 + 4, i5 - 3);
        graphics.setColor(adjustedColor);
        graphics.drawLine(i4 - 2, i3 + 3, i4 - 2, i5 - 3);
        graphics.drawLine(i4 - 3, i5 - 5, i4 - 3, i5 - 5);
        graphics.drawLine(i4 - 4, i5 - 4, i4 - 4, i5 - 4);
        graphics.drawLine(i4 - 5, i5 - 3, i4 - 5, i5 - 3);
        graphics.drawLine(i4 - 5, i5 - 2, i4 - 5, i5 - 2);
        graphics.setColor(adjustedColor2);
        graphics.drawLine(i4 - 1, i3 + 3, i4 - 1, i5 - 6);
        graphics.drawLine(i4 - 2, i5 - 5, i4 - 2, i5 - 5);
        graphics.drawLine(i4 - 3, i5 - 4, i4 - 3, i5 - 4);
        graphics.drawLine(i4 - 4, i5 - 3, i4 - 4, i5 - 3);
        graphics.setColor(color2);
        graphics.drawLine(i2 + 1, i3, i4 - 1, i3);
        graphics.drawLine(i2, i3 + 1, i2, i5 - 5);
        graphics.setColor(adjustedColor3);
        graphics.drawLine(i2 + 1, i3 + 1, i4, i3 + 1);
        graphics.setColor(adjustedColor4);
        graphics.drawLine(i2 + 1, i3 + 2, i4, i3 + 2);
        graphics.drawLine(i2 + 1, i5 - 4, i2 + 1, i5 - 4);
        graphics.drawLine(i2 + 2, i5 - 3, i2 + 2, i5 - 3);
        graphics.drawLine(i2 + 3, i5 - 2, i2 + 3, i5 - 2);
        graphics.drawLine(i2 + 4, i5 - 1, i2 + 4, i5 - 1);
        graphics.setColor(adjustedColor5);
        graphics.drawLine(i2 + 1, i5 - 5, i2 + 1, i5 - 5);
        graphics.drawLine(i2 + 2, i5 - 4, i2 + 2, i5 - 4);
        graphics.drawLine(i2 + 3, i5 - 3, i2 + 3, i5 - 3);
        graphics.drawLine(i2 + 4, i5 - 2, i2 + 4, i5 - 2);
        graphics.drawLine(i2 + 5, i5 - 1, i2 + 5, i5);
        graphics.setColor(adjustedColor6);
        graphics.drawLine(i4, i3 + 3, i4, i5 - 5);
        graphics.drawLine(i4 - 1, i5 - 4, i4 - 1, i5 - 4);
        graphics.drawLine(i4 - 2, i5 - 3, i4 - 2, i5 - 3);
        graphics.drawLine(i4 - 3, i5 - 2, i4 - 3, i5 - 2);
        graphics.drawLine(i4 - 4, i5 - 1, i4 - 4, i5 - 1);
        graphics.drawLine(i4 - 5, i5, i4 - 5, i5);
        graphics.setColor(adjustedColor7);
        graphics.drawLine(i4 - 1, i5 - 5, i4 - 1, i5 - 5);
        graphics.drawLine(i4 - 2, i5 - 4, i4 - 2, i5 - 4);
        graphics.drawLine(i4 - 3, i5 - 3, i4 - 3, i5 - 3);
        graphics.drawLine(i4 - 4, i5 - 2, i4 - 4, i5 - 2);
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new TinySliderUI();
    }

    @Override // javax.swing.plaf.metal.MetalSliderUI, javax.swing.plaf.basic.BasicSliderUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
        jComponent.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    }

    @Override // javax.swing.plaf.metal.MetalSliderUI
    protected int getThumbOverhang() {
        return this.slider.getOrientation() == 1 ? ((int) (getThumbSize().getWidth() - getTrackWidth())) / 2 : ((int) (getThumbSize().getHeight() - getTrackWidth())) / 2;
    }

    @Override // javax.swing.plaf.metal.MetalSliderUI, javax.swing.plaf.basic.BasicSliderUI
    public void paintTrack(Graphics graphics) {
        drawXpTrack(graphics);
    }

    @Override // javax.swing.plaf.metal.MetalSliderUI, javax.swing.plaf.basic.BasicSliderUI
    public void paintFocus(Graphics graphics) {
        if (Theme.sliderFocusEnabled.getValue() && (graphics instanceof Graphics2D)) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            Stroke stroke = graphics2D.getStroke();
            graphics2D.setStroke(focusStroke);
            graphics2D.setColor(Theme.sliderFocusColor.getColor());
            graphics2D.drawRect(0, 0, this.slider.getWidth() - 1, this.slider.getHeight() - 1);
            graphics2D.setStroke(stroke);
        }
    }

    private void drawXpTrack(Graphics graphics) {
        int i2 = this.trackRect.f12372x;
        int i3 = i2 + this.trackRect.width;
        int i4 = this.trackRect.f12373y;
        int i5 = i4 + this.trackRect.height;
        if (this.slider.getOrientation() == 0) {
            int i6 = i4 + ((this.trackRect.height - 4) / 2);
            graphics.setColor(Theme.sliderTrackColor.getColor());
            graphics.drawLine(i2 + 1, i6 + 2, i3 - 2, i6 + 2);
            graphics.setColor(Theme.sliderTrackDarkColor.getColor());
            graphics.drawLine(i2 + 1, i6 + 1, i3 - 2, i6 + 1);
            graphics.drawLine(i3 - 1, i6 + 1, i3 - 1, i6 + 2);
            graphics.setColor(ColorRoutines.darken(Theme.sliderTrackDarkColor.getColor(), 10));
            graphics.drawLine(i2, i6, i2, i6);
            graphics.drawLine(i2, i6 + 3, i2, i6 + 3);
            graphics.setColor(Theme.sliderTrackLightColor.getColor());
            graphics.drawLine(i2 + 1, i6 + 3, i3 - 1, i6 + 3);
            graphics.drawLine(i3, i6 + 1, i3, i6 + 3);
            graphics.setColor(Theme.sliderTrackBorderColor.getColor());
            graphics.drawLine(i2 + 1, i6, i3, i6);
            graphics.setColor(ColorRoutines.lighten(Theme.sliderTrackBorderColor.getColor(), 20));
            graphics.drawLine(i2, i6 + 1, i2, i6 + 2);
            return;
        }
        int i7 = i2 + ((this.trackRect.width - 4) / 2);
        graphics.setColor(Theme.sliderTrackBorderColor.getColor());
        graphics.drawLine(i7, i4 + 1, i7, i5 - 1);
        graphics.setColor(ColorRoutines.lighten(Theme.sliderTrackBorderColor.getColor(), 20));
        graphics.drawLine(i7 + 1, i4, i7 + 1, i4);
        graphics.drawLine(i7 + 1, i5, i7 + 1, i5);
        graphics.setColor(Theme.sliderTrackDarkColor.getColor());
        graphics.drawLine(i7 + 1, i4 + 1, i7 + 1, i5 - 1);
        graphics.setColor(ColorRoutines.darken(Theme.sliderTrackDarkColor.getColor(), 10));
        graphics.drawLine(i7, i4, i7, i4);
        graphics.drawLine(i7, i5, i7, i5);
        graphics.setColor(Theme.sliderTrackLightColor.getColor());
        graphics.drawLine(i7 + 3, i4, i7 + 3, i5);
        graphics.drawLine(i7 + 2, i4, i7 + 2, i4);
        graphics.drawLine(i7 + 2, i5, i7 + 2, i5);
        graphics.setColor(Theme.sliderTrackColor.getColor());
        graphics.drawLine(i7 + 2, i4 + 1, i7 + 2, i5 - 1);
    }

    @Override // javax.swing.plaf.metal.MetalSliderUI, javax.swing.plaf.basic.BasicSliderUI
    protected void paintMinorTickForHorizSlider(Graphics graphics, Rectangle rectangle, int i2) {
        graphics.setColor(this.slider.isEnabled() ? Theme.sliderTickColor.getColor() : Theme.sliderTickDisabledColor.getColor());
        graphics.drawLine(i2, 0, i2, (rectangle.height / 2) - 1);
    }

    @Override // javax.swing.plaf.metal.MetalSliderUI, javax.swing.plaf.basic.BasicSliderUI
    protected void paintMajorTickForHorizSlider(Graphics graphics, Rectangle rectangle, int i2) {
        graphics.setColor(this.slider.isEnabled() ? Theme.sliderTickColor.getColor() : Theme.sliderTickDisabledColor.getColor());
        graphics.drawLine(i2, 0, i2, rectangle.height - 2);
    }

    @Override // javax.swing.plaf.metal.MetalSliderUI, javax.swing.plaf.basic.BasicSliderUI
    protected void paintMinorTickForVertSlider(Graphics graphics, Rectangle rectangle, int i2) {
        graphics.setColor(this.slider.isEnabled() ? Theme.sliderTickColor.getColor() : Theme.sliderTickDisabledColor.getColor());
        graphics.drawLine(0, i2, (rectangle.width / 2) - 1, i2);
    }

    @Override // javax.swing.plaf.metal.MetalSliderUI, javax.swing.plaf.basic.BasicSliderUI
    protected void paintMajorTickForVertSlider(Graphics graphics, Rectangle rectangle, int i2) {
        graphics.setColor(this.slider.isEnabled() ? Theme.sliderTickColor.getColor() : Theme.sliderTickDisabledColor.getColor());
        graphics.drawLine(0, i2, rectangle.width - 2, i2);
    }
}
