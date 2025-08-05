package com.sun.java.swing.plaf.windows;

import com.sun.java.swing.plaf.windows.TMSchema;
import com.sun.java.swing.plaf.windows.XPStyle;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSliderUI;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsSliderUI.class */
public class WindowsSliderUI extends BasicSliderUI {
    private boolean rollover;
    private boolean pressed;

    public WindowsSliderUI(JSlider jSlider) {
        super(jSlider);
        this.rollover = false;
        this.pressed = false;
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new WindowsSliderUI((JSlider) jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    protected BasicSliderUI.TrackListener createTrackListener(JSlider jSlider) {
        return new WindowsTrackListener();
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsSliderUI$WindowsTrackListener.class */
    private class WindowsTrackListener extends BasicSliderUI.TrackListener {
        private WindowsTrackListener() {
            super();
        }

        @Override // javax.swing.plaf.basic.BasicSliderUI.TrackListener, java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
            updateRollover(WindowsSliderUI.this.thumbRect.contains(mouseEvent.getX(), mouseEvent.getY()));
            super.mouseMoved(mouseEvent);
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            updateRollover(WindowsSliderUI.this.thumbRect.contains(mouseEvent.getX(), mouseEvent.getY()));
            super.mouseEntered(mouseEvent);
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            updateRollover(false);
            super.mouseExited(mouseEvent);
        }

        @Override // javax.swing.plaf.basic.BasicSliderUI.TrackListener, java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            updatePressed(WindowsSliderUI.this.thumbRect.contains(mouseEvent.getX(), mouseEvent.getY()));
            super.mousePressed(mouseEvent);
        }

        @Override // javax.swing.plaf.basic.BasicSliderUI.TrackListener, java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            updatePressed(false);
            super.mouseReleased(mouseEvent);
        }

        public void updatePressed(boolean z2) {
            if (WindowsSliderUI.this.slider.isEnabled() && WindowsSliderUI.this.pressed != z2) {
                WindowsSliderUI.this.pressed = z2;
                WindowsSliderUI.this.slider.repaint(WindowsSliderUI.this.thumbRect);
            }
        }

        public void updateRollover(boolean z2) {
            if (WindowsSliderUI.this.slider.isEnabled() && WindowsSliderUI.this.rollover != z2) {
                WindowsSliderUI.this.rollover = z2;
                WindowsSliderUI.this.slider.repaint(WindowsSliderUI.this.thumbRect);
            }
        }
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    public void paintTrack(Graphics graphics) {
        XPStyle xp = XPStyle.getXP();
        if (xp != null) {
            boolean z2 = this.slider.getOrientation() == 1;
            XPStyle.Skin skin = xp.getSkin(this.slider, z2 ? TMSchema.Part.TKP_TRACKVERT : TMSchema.Part.TKP_TRACK);
            if (z2) {
                skin.paintSkin(graphics, this.trackRect.f12372x + ((this.trackRect.width - skin.getWidth()) / 2), this.trackRect.f12373y, skin.getWidth(), this.trackRect.height, null);
                return;
            } else {
                skin.paintSkin(graphics, this.trackRect.f12372x, this.trackRect.f12373y + ((this.trackRect.height - skin.getHeight()) / 2), this.trackRect.width, skin.getHeight(), null);
                return;
            }
        }
        super.paintTrack(graphics);
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    protected void paintMinorTickForHorizSlider(Graphics graphics, Rectangle rectangle, int i2) {
        XPStyle xp = XPStyle.getXP();
        if (xp != null) {
            graphics.setColor(xp.getColor(this.slider, TMSchema.Part.TKP_TICS, null, TMSchema.Prop.COLOR, Color.black));
        }
        super.paintMinorTickForHorizSlider(graphics, rectangle, i2);
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    protected void paintMajorTickForHorizSlider(Graphics graphics, Rectangle rectangle, int i2) {
        XPStyle xp = XPStyle.getXP();
        if (xp != null) {
            graphics.setColor(xp.getColor(this.slider, TMSchema.Part.TKP_TICS, null, TMSchema.Prop.COLOR, Color.black));
        }
        super.paintMajorTickForHorizSlider(graphics, rectangle, i2);
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    protected void paintMinorTickForVertSlider(Graphics graphics, Rectangle rectangle, int i2) {
        XPStyle xp = XPStyle.getXP();
        if (xp != null) {
            graphics.setColor(xp.getColor(this.slider, TMSchema.Part.TKP_TICSVERT, null, TMSchema.Prop.COLOR, Color.black));
        }
        super.paintMinorTickForVertSlider(graphics, rectangle, i2);
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    protected void paintMajorTickForVertSlider(Graphics graphics, Rectangle rectangle, int i2) {
        XPStyle xp = XPStyle.getXP();
        if (xp != null) {
            graphics.setColor(xp.getColor(this.slider, TMSchema.Part.TKP_TICSVERT, null, TMSchema.Prop.COLOR, Color.black));
        }
        super.paintMajorTickForVertSlider(graphics, rectangle, i2);
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    public void paintThumb(Graphics graphics) {
        XPStyle xp = XPStyle.getXP();
        if (xp != null) {
            TMSchema.Part xPThumbPart = getXPThumbPart();
            TMSchema.State state = TMSchema.State.NORMAL;
            if (this.slider.hasFocus()) {
                state = TMSchema.State.FOCUSED;
            }
            if (this.rollover) {
                state = TMSchema.State.HOT;
            }
            if (this.pressed) {
                state = TMSchema.State.PRESSED;
            }
            if (!this.slider.isEnabled()) {
                state = TMSchema.State.DISABLED;
            }
            xp.getSkin(this.slider, xPThumbPart).paintSkin(graphics, this.thumbRect.f12372x, this.thumbRect.f12373y, state);
            return;
        }
        super.paintThumb(graphics);
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    protected Dimension getThumbSize() {
        XPStyle xp = XPStyle.getXP();
        if (xp != null) {
            Dimension dimension = new Dimension();
            XPStyle.Skin skin = xp.getSkin(this.slider, getXPThumbPart());
            dimension.width = skin.getWidth();
            dimension.height = skin.getHeight();
            return dimension;
        }
        return super.getThumbSize();
    }

    private TMSchema.Part getXPThumbPart() {
        TMSchema.Part part;
        boolean z2 = this.slider.getOrientation() == 1;
        boolean zIsLeftToRight = this.slider.getComponentOrientation().isLeftToRight();
        Boolean bool = (Boolean) this.slider.getClientProperty("Slider.paintThumbArrowShape");
        if ((!this.slider.getPaintTicks() && bool == null) || bool == Boolean.FALSE) {
            part = z2 ? TMSchema.Part.TKP_THUMBVERT : TMSchema.Part.TKP_THUMB;
        } else {
            part = z2 ? zIsLeftToRight ? TMSchema.Part.TKP_THUMBRIGHT : TMSchema.Part.TKP_THUMBLEFT : TMSchema.Part.TKP_THUMBBOTTOM;
        }
        return part;
    }
}
