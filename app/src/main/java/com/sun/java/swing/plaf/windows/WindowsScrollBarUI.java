package com.sun.java.swing.plaf.windows;

import com.sun.java.swing.plaf.windows.TMSchema;
import com.sun.java.swing.plaf.windows.XPStyle;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicScrollBarUI;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsScrollBarUI.class */
public class WindowsScrollBarUI extends BasicScrollBarUI {
    private Grid thumbGrid;
    private Grid highlightGrid;
    private Dimension horizontalThumbSize;
    private Dimension verticalThumbSize;

    public static ComponentUI createUI(JComponent jComponent) {
        return new WindowsScrollBarUI();
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected void installDefaults() {
        super.installDefaults();
        XPStyle xp = XPStyle.getXP();
        if (xp != null) {
            this.scrollbar.setBorder(null);
            this.horizontalThumbSize = getSize(this.scrollbar, xp, TMSchema.Part.SBP_THUMBBTNHORZ);
            this.verticalThumbSize = getSize(this.scrollbar, xp, TMSchema.Part.SBP_THUMBBTNVERT);
        } else {
            this.horizontalThumbSize = null;
            this.verticalThumbSize = null;
        }
    }

    private static Dimension getSize(Component component, XPStyle xPStyle, TMSchema.Part part) {
        XPStyle.Skin skin = xPStyle.getSkin(component, part);
        return new Dimension(skin.getWidth(), skin.getHeight());
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected Dimension getMinimumThumbSize() {
        if (this.horizontalThumbSize == null || this.verticalThumbSize == null) {
            return super.getMinimumThumbSize();
        }
        return 0 == this.scrollbar.getOrientation() ? this.horizontalThumbSize : this.verticalThumbSize;
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI, javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
        this.highlightGrid = null;
        this.thumbGrid = null;
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected void configureScrollBarColors() {
        super.configureScrollBarColors();
        Color color = UIManager.getColor("ScrollBar.trackForeground");
        if (color != null && this.trackColor != null) {
            this.thumbGrid = Grid.getGrid(color, this.trackColor);
        }
        Color color2 = UIManager.getColor("ScrollBar.trackHighlightForeground");
        if (color2 != null && this.trackHighlightColor != null) {
            this.highlightGrid = Grid.getGrid(color2, this.trackHighlightColor);
        }
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected JButton createDecreaseButton(int i2) {
        return new WindowsArrowButton(i2, UIManager.getColor("ScrollBar.thumb"), UIManager.getColor("ScrollBar.thumbShadow"), UIManager.getColor("ScrollBar.thumbDarkShadow"), UIManager.getColor("ScrollBar.thumbHighlight"));
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected JButton createIncreaseButton(int i2) {
        return new WindowsArrowButton(i2, UIManager.getColor("ScrollBar.thumb"), UIManager.getColor("ScrollBar.thumbShadow"), UIManager.getColor("ScrollBar.thumbDarkShadow"), UIManager.getColor("ScrollBar.thumbHighlight"));
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected BasicScrollBarUI.ArrowButtonListener createArrowButtonListener() {
        if (XPStyle.isVista()) {
            return new BasicScrollBarUI.ArrowButtonListener() { // from class: com.sun.java.swing.plaf.windows.WindowsScrollBarUI.1
                @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
                public void mouseEntered(MouseEvent mouseEvent) {
                    repaint();
                    super.mouseEntered(mouseEvent);
                }

                @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
                public void mouseExited(MouseEvent mouseEvent) {
                    repaint();
                    super.mouseExited(mouseEvent);
                }

                private void repaint() {
                    WindowsScrollBarUI.this.scrollbar.repaint();
                }
            };
        }
        return super.createArrowButtonListener();
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected void paintTrack(Graphics graphics, JComponent jComponent, Rectangle rectangle) {
        boolean z2 = this.scrollbar.getOrientation() == 1;
        XPStyle xp = XPStyle.getXP();
        if (xp != null) {
            JScrollBar jScrollBar = (JScrollBar) jComponent;
            TMSchema.State state = TMSchema.State.NORMAL;
            if (!jScrollBar.isEnabled()) {
                state = TMSchema.State.DISABLED;
            }
            xp.getSkin(jScrollBar, z2 ? TMSchema.Part.SBP_LOWERTRACKVERT : TMSchema.Part.SBP_LOWERTRACKHORZ).paintSkin(graphics, rectangle, state);
            return;
        }
        if (this.thumbGrid == null) {
            super.paintTrack(graphics, jComponent, rectangle);
            return;
        }
        this.thumbGrid.paint(graphics, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
        if (this.trackHighlight == 1) {
            paintDecreaseHighlight(graphics);
        } else if (this.trackHighlight == 2) {
            paintIncreaseHighlight(graphics);
        }
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected void paintThumb(Graphics graphics, JComponent jComponent, Rectangle rectangle) {
        boolean z2 = this.scrollbar.getOrientation() == 1;
        XPStyle xp = XPStyle.getXP();
        if (xp != null) {
            JScrollBar jScrollBar = (JScrollBar) jComponent;
            TMSchema.State state = TMSchema.State.NORMAL;
            if (!jScrollBar.isEnabled()) {
                state = TMSchema.State.DISABLED;
            } else if (this.isDragging) {
                state = TMSchema.State.PRESSED;
            } else if (isThumbRollover()) {
                state = TMSchema.State.HOT;
            } else if (XPStyle.isVista() && ((this.incrButton != null && this.incrButton.getModel().isRollover()) || (this.decrButton != null && this.decrButton.getModel().isRollover()))) {
                state = TMSchema.State.HOVER;
            }
            TMSchema.Part part = z2 ? TMSchema.Part.SBP_THUMBBTNVERT : TMSchema.Part.SBP_THUMBBTNHORZ;
            xp.getSkin(jScrollBar, part).paintSkin(graphics, rectangle, state);
            XPStyle.Skin skin = xp.getSkin(jScrollBar, z2 ? TMSchema.Part.SBP_GRIPPERVERT : TMSchema.Part.SBP_GRIPPERHORZ);
            Insets margin = xp.getMargin(jComponent, part, null, TMSchema.Prop.CONTENTMARGINS);
            if (margin == null || ((z2 && (rectangle.height - margin.top) - margin.bottom >= skin.getHeight()) || (!z2 && (rectangle.width - margin.left) - margin.right >= skin.getWidth()))) {
                skin.paintSkin(graphics, rectangle.f12372x + ((rectangle.width - skin.getWidth()) / 2), rectangle.f12373y + ((rectangle.height - skin.getHeight()) / 2), skin.getWidth(), skin.getHeight(), state);
                return;
            }
            return;
        }
        super.paintThumb(graphics, jComponent, rectangle);
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected void paintDecreaseHighlight(Graphics graphics) {
        int x2;
        int y2;
        int width;
        int height;
        if (this.highlightGrid == null) {
            super.paintDecreaseHighlight(graphics);
            return;
        }
        Insets insets = this.scrollbar.getInsets();
        Rectangle thumbBounds = getThumbBounds();
        if (this.scrollbar.getOrientation() == 1) {
            x2 = insets.left;
            y2 = this.decrButton.getY() + this.decrButton.getHeight();
            width = this.scrollbar.getWidth() - (insets.left + insets.right);
            height = thumbBounds.f12373y - y2;
        } else {
            x2 = this.decrButton.getX() + this.decrButton.getHeight();
            y2 = insets.top;
            width = thumbBounds.f12372x - x2;
            height = this.scrollbar.getHeight() - (insets.top + insets.bottom);
        }
        this.highlightGrid.paint(graphics, x2, y2, width, height);
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected void paintIncreaseHighlight(Graphics graphics) {
        int i2;
        int i3;
        int x2;
        int height;
        if (this.highlightGrid == null) {
            super.paintDecreaseHighlight(graphics);
            return;
        }
        Insets insets = this.scrollbar.getInsets();
        Rectangle thumbBounds = getThumbBounds();
        if (this.scrollbar.getOrientation() == 1) {
            i2 = insets.left;
            i3 = thumbBounds.f12373y + thumbBounds.height;
            x2 = this.scrollbar.getWidth() - (insets.left + insets.right);
            height = this.incrButton.getY() - i3;
        } else {
            i2 = thumbBounds.f12372x + thumbBounds.width;
            i3 = insets.top;
            x2 = this.incrButton.getX() - i2;
            height = this.scrollbar.getHeight() - (insets.top + insets.bottom);
        }
        this.highlightGrid.paint(graphics, i2, i3, x2, height);
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected void setThumbRollover(boolean z2) {
        boolean zIsThumbRollover = isThumbRollover();
        super.setThumbRollover(z2);
        if (XPStyle.isVista() && z2 != zIsThumbRollover) {
            this.scrollbar.repaint();
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsScrollBarUI$WindowsArrowButton.class */
    private class WindowsArrowButton extends BasicArrowButton {
        public WindowsArrowButton(int i2, Color color, Color color2, Color color3, Color color4) {
            super(i2, color, color2, color3, color4);
        }

        public WindowsArrowButton(int i2) {
            super(i2);
        }

        @Override // javax.swing.plaf.basic.BasicArrowButton, javax.swing.JComponent, java.awt.Container, java.awt.Component
        public void paint(Graphics graphics) {
            XPStyle xp = XPStyle.getXP();
            if (xp != null) {
                ButtonModel model = getModel();
                XPStyle.Skin skin = xp.getSkin(this, TMSchema.Part.SBP_ARROWBTN);
                TMSchema.State state = null;
                boolean z2 = XPStyle.isVista() && (WindowsScrollBarUI.this.isThumbRollover() || ((this == WindowsScrollBarUI.this.incrButton && WindowsScrollBarUI.this.decrButton.getModel().isRollover()) || (this == WindowsScrollBarUI.this.decrButton && WindowsScrollBarUI.this.incrButton.getModel().isRollover())));
                if (model.isArmed() && model.isPressed()) {
                    switch (this.direction) {
                        case 1:
                            state = TMSchema.State.UPPRESSED;
                            break;
                        case 3:
                            state = TMSchema.State.RIGHTPRESSED;
                            break;
                        case 5:
                            state = TMSchema.State.DOWNPRESSED;
                            break;
                        case 7:
                            state = TMSchema.State.LEFTPRESSED;
                            break;
                    }
                } else if (!model.isEnabled()) {
                    switch (this.direction) {
                        case 1:
                            state = TMSchema.State.UPDISABLED;
                            break;
                        case 3:
                            state = TMSchema.State.RIGHTDISABLED;
                            break;
                        case 5:
                            state = TMSchema.State.DOWNDISABLED;
                            break;
                        case 7:
                            state = TMSchema.State.LEFTDISABLED;
                            break;
                    }
                } else if (model.isRollover() || model.isPressed()) {
                    switch (this.direction) {
                        case 1:
                            state = TMSchema.State.UPHOT;
                            break;
                        case 3:
                            state = TMSchema.State.RIGHTHOT;
                            break;
                        case 5:
                            state = TMSchema.State.DOWNHOT;
                            break;
                        case 7:
                            state = TMSchema.State.LEFTHOT;
                            break;
                    }
                } else if (z2) {
                    switch (this.direction) {
                        case 1:
                            state = TMSchema.State.UPHOVER;
                            break;
                        case 3:
                            state = TMSchema.State.RIGHTHOVER;
                            break;
                        case 5:
                            state = TMSchema.State.DOWNHOVER;
                            break;
                        case 7:
                            state = TMSchema.State.LEFTHOVER;
                            break;
                    }
                } else {
                    switch (this.direction) {
                        case 1:
                            state = TMSchema.State.UPNORMAL;
                            break;
                        case 3:
                            state = TMSchema.State.RIGHTNORMAL;
                            break;
                        case 5:
                            state = TMSchema.State.DOWNNORMAL;
                            break;
                        case 7:
                            state = TMSchema.State.LEFTNORMAL;
                            break;
                    }
                }
                skin.paintSkin(graphics, 0, 0, getWidth(), getHeight(), state);
                return;
            }
            super.paint(graphics);
        }

        @Override // javax.swing.plaf.basic.BasicArrowButton, javax.swing.JComponent, java.awt.Container, java.awt.Component
        public Dimension getPreferredSize() {
            int iMax = 16;
            if (WindowsScrollBarUI.this.scrollbar != null) {
                switch (WindowsScrollBarUI.this.scrollbar.getOrientation()) {
                    case 0:
                        iMax = WindowsScrollBarUI.this.scrollbar.getHeight();
                        break;
                    case 1:
                        iMax = WindowsScrollBarUI.this.scrollbar.getWidth();
                        break;
                }
                iMax = Math.max(iMax, 5);
            }
            return new Dimension(iMax, iMax);
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsScrollBarUI$Grid.class */
    private static class Grid {
        private static final int BUFFER_SIZE = 64;
        private static HashMap<String, WeakReference<Grid>> map = new HashMap<>();
        private BufferedImage image;

        public static Grid getGrid(Color color, Color color2) {
            String str = color.getRGB() + " " + color2.getRGB();
            WeakReference<Grid> weakReference = map.get(str);
            Grid grid = weakReference == null ? null : weakReference.get();
            if (grid == null) {
                grid = new Grid(color, color2);
                map.put(str, new WeakReference<>(grid));
            }
            return grid;
        }

        public Grid(Color color, Color color2) {
            this.image = new BufferedImage(64, 64, 13, new IndexColorModel(8, 2, new int[]{color.getRGB(), color2.getRGB()}, 0, false, -1, 0));
            Graphics graphics = this.image.getGraphics();
            try {
                graphics.setClip(0, 0, 64, 64);
                paintGrid(graphics, color, color2);
                graphics.dispose();
            } catch (Throwable th) {
                graphics.dispose();
                throw th;
            }
        }

        public void paint(Graphics graphics, int i2, int i3, int i4, int i5) {
            Rectangle clipBounds = graphics.getClipBounds();
            int iMax = Math.max(i2, clipBounds.f12372x);
            int iMax2 = Math.max(i3, clipBounds.f12373y);
            int iMin = Math.min(clipBounds.f12372x + clipBounds.width, i2 + i4);
            int iMin2 = Math.min(clipBounds.f12373y + clipBounds.height, i3 + i5);
            if (iMin <= iMax || iMin2 <= iMax2) {
                return;
            }
            int i6 = (iMax - i2) % 2;
            int i7 = iMax;
            while (i7 < iMin) {
                int i8 = (iMax2 - i3) % 2;
                int iMin3 = Math.min(64 - i6, iMin - i7);
                int i9 = iMax2;
                while (i9 < iMin2) {
                    int iMin4 = Math.min(64 - i8, iMin2 - i9);
                    graphics.drawImage(this.image, i7, i9, i7 + iMin3, i9 + iMin4, i6, i8, i6 + iMin3, i8 + iMin4, null);
                    if (i8 != 0) {
                        i9 -= i8;
                        i8 = 0;
                    }
                    i9 += 64;
                }
                if (i6 != 0) {
                    i7 -= i6;
                    i6 = 0;
                }
                i7 += 64;
            }
        }

        private void paintGrid(Graphics graphics, Color color, Color color2) {
            Rectangle clipBounds = graphics.getClipBounds();
            graphics.setColor(color2);
            graphics.fillRect(clipBounds.f12372x, clipBounds.f12373y, clipBounds.width, clipBounds.height);
            graphics.setColor(color);
            graphics.translate(clipBounds.f12372x, clipBounds.f12373y);
            int i2 = clipBounds.width;
            int i3 = clipBounds.height;
            int i4 = clipBounds.f12372x % 2;
            int i5 = i2 - i3;
            while (i4 < i5) {
                graphics.drawLine(i4, 0, i4 + i3, i3);
                i4 += 2;
            }
            while (i4 < i2) {
                graphics.drawLine(i4, 0, i2, i2 - i4);
                i4 += 2;
            }
            int i6 = clipBounds.f12372x % 2 == 0 ? 2 : 1;
            int i7 = i3 - i2;
            while (i6 < i7) {
                graphics.drawLine(0, i6, i2, i6 + i2);
                i6 += 2;
            }
            while (i6 < i3) {
                graphics.drawLine(0, i6, i3 - i6, i3);
                i6 += 2;
            }
            graphics.translate(-clipBounds.f12372x, -clipBounds.f12373y);
        }
    }
}
