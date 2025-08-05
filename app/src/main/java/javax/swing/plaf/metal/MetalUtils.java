package javax.swing.plaf.metal;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import sun.swing.CachedPainter;
import sun.swing.ImageIconUIResource;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalUtils.class */
class MetalUtils {
    MetalUtils() {
    }

    static void drawFlush3DBorder(Graphics graphics, Rectangle rectangle) {
        drawFlush3DBorder(graphics, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
    }

    static void drawFlush3DBorder(Graphics graphics, int i2, int i3, int i4, int i5) {
        graphics.translate(i2, i3);
        graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
        graphics.drawRect(0, 0, i4 - 2, i5 - 2);
        graphics.setColor(MetalLookAndFeel.getControlHighlight());
        graphics.drawRect(1, 1, i4 - 2, i5 - 2);
        graphics.setColor(MetalLookAndFeel.getControl());
        graphics.drawLine(0, i5 - 1, 1, i5 - 2);
        graphics.drawLine(i4 - 1, 0, i4 - 2, 1);
        graphics.translate(-i2, -i3);
    }

    static void drawPressed3DBorder(Graphics graphics, Rectangle rectangle) {
        drawPressed3DBorder(graphics, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
    }

    static void drawDisabledBorder(Graphics graphics, int i2, int i3, int i4, int i5) {
        graphics.translate(i2, i3);
        graphics.setColor(MetalLookAndFeel.getControlShadow());
        graphics.drawRect(0, 0, i4 - 1, i5 - 1);
        graphics.translate(-i2, -i3);
    }

    static void drawPressed3DBorder(Graphics graphics, int i2, int i3, int i4, int i5) {
        graphics.translate(i2, i3);
        drawFlush3DBorder(graphics, 0, 0, i4, i5);
        graphics.setColor(MetalLookAndFeel.getControlShadow());
        graphics.drawLine(1, 1, 1, i5 - 2);
        graphics.drawLine(1, 1, i4 - 2, 1);
        graphics.translate(-i2, -i3);
    }

    static void drawDark3DBorder(Graphics graphics, Rectangle rectangle) {
        drawDark3DBorder(graphics, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
    }

    static void drawDark3DBorder(Graphics graphics, int i2, int i3, int i4, int i5) {
        graphics.translate(i2, i3);
        drawFlush3DBorder(graphics, 0, 0, i4, i5);
        graphics.setColor(MetalLookAndFeel.getControl());
        graphics.drawLine(1, 1, 1, i5 - 2);
        graphics.drawLine(1, 1, i4 - 2, 1);
        graphics.setColor(MetalLookAndFeel.getControlShadow());
        graphics.drawLine(1, i5 - 2, 1, i5 - 2);
        graphics.drawLine(i4 - 2, 1, i4 - 2, 1);
        graphics.translate(-i2, -i3);
    }

    static void drawButtonBorder(Graphics graphics, int i2, int i3, int i4, int i5, boolean z2) {
        if (z2) {
            drawActiveButtonBorder(graphics, i2, i3, i4, i5);
        } else {
            drawFlush3DBorder(graphics, i2, i3, i4, i5);
        }
    }

    static void drawActiveButtonBorder(Graphics graphics, int i2, int i3, int i4, int i5) {
        drawFlush3DBorder(graphics, i2, i3, i4, i5);
        graphics.setColor(MetalLookAndFeel.getPrimaryControl());
        graphics.drawLine(i2 + 1, i3 + 1, i2 + 1, i5 - 3);
        graphics.drawLine(i2 + 1, i3 + 1, i4 - 3, i2 + 1);
        graphics.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
        graphics.drawLine(i2 + 2, i5 - 2, i4 - 2, i5 - 2);
        graphics.drawLine(i4 - 2, i3 + 2, i4 - 2, i5 - 2);
    }

    static void drawDefaultButtonBorder(Graphics graphics, int i2, int i3, int i4, int i5, boolean z2) {
        drawButtonBorder(graphics, i2 + 1, i3 + 1, i4 - 1, i5 - 1, z2);
        graphics.translate(i2, i3);
        graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
        graphics.drawRect(0, 0, i4 - 3, i5 - 3);
        graphics.drawLine(i4 - 2, 0, i4 - 2, 0);
        graphics.drawLine(0, i5 - 2, 0, i5 - 2);
        graphics.translate(-i2, -i3);
    }

    static void drawDefaultButtonPressedBorder(Graphics graphics, int i2, int i3, int i4, int i5) {
        drawPressed3DBorder(graphics, i2 + 1, i3 + 1, i4 - 1, i5 - 1);
        graphics.translate(i2, i3);
        graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
        graphics.drawRect(0, 0, i4 - 3, i5 - 3);
        graphics.drawLine(i4 - 2, 0, i4 - 2, 0);
        graphics.drawLine(0, i5 - 2, 0, i5 - 2);
        graphics.setColor(MetalLookAndFeel.getControl());
        graphics.drawLine(i4 - 1, 0, i4 - 1, 0);
        graphics.drawLine(0, i5 - 1, 0, i5 - 1);
        graphics.translate(-i2, -i3);
    }

    static boolean isLeftToRight(Component component) {
        return component.getComponentOrientation().isLeftToRight();
    }

    static int getInt(Object obj, int i2) {
        Object obj2 = UIManager.get(obj);
        if (obj2 instanceof Integer) {
            return ((Integer) obj2).intValue();
        }
        if (obj2 instanceof String) {
            try {
                return Integer.parseInt((String) obj2);
            } catch (NumberFormatException e2) {
            }
        }
        return i2;
    }

    static boolean drawGradient(Component component, Graphics graphics, String str, int i2, int i3, int i4, int i5, boolean z2) {
        List list = (List) UIManager.get(str);
        if (list == null || !(graphics instanceof Graphics2D)) {
            return false;
        }
        if (i4 <= 0 || i5 <= 0) {
            return true;
        }
        GradientPainter.INSTANCE.paint(component, (Graphics2D) graphics, list, i2, i3, i4, i5, z2);
        return true;
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalUtils$GradientPainter.class */
    private static class GradientPainter extends CachedPainter {
        public static final GradientPainter INSTANCE = new GradientPainter(8);
        private static final int IMAGE_SIZE = 64;

        /* renamed from: w, reason: collision with root package name */
        private int f12819w;

        /* renamed from: h, reason: collision with root package name */
        private int f12820h;

        GradientPainter(int i2) {
            super(i2);
        }

        public void paint(Component component, Graphics2D graphics2D, List list, int i2, int i3, int i4, int i5, boolean z2) {
            int i6;
            int i7;
            if (z2) {
                i6 = 64;
                i7 = i5;
            } else {
                i6 = i4;
                i7 = 64;
            }
            synchronized (component.getTreeLock()) {
                this.f12819w = i4;
                this.f12820h = i5;
                paint(component, graphics2D, i2, i3, i6, i7, list, Boolean.valueOf(z2));
            }
        }

        @Override // sun.swing.CachedPainter
        protected void paintToImage(Component component, Image image, Graphics graphics, int i2, int i3, Object[] objArr) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            List list = (List) objArr[0];
            if (((Boolean) objArr[1]).booleanValue()) {
                drawVerticalGradient(graphics2D, ((Number) list.get(0)).floatValue(), ((Number) list.get(1)).floatValue(), (Color) list.get(2), (Color) list.get(3), (Color) list.get(4), i2, i3);
            } else {
                drawHorizontalGradient(graphics2D, ((Number) list.get(0)).floatValue(), ((Number) list.get(1)).floatValue(), (Color) list.get(2), (Color) list.get(3), (Color) list.get(4), i2, i3);
            }
        }

        @Override // sun.swing.CachedPainter
        protected void paintImage(Component component, Graphics graphics, int i2, int i3, int i4, int i5, Image image, Object[] objArr) {
            boolean zBooleanValue = ((Boolean) objArr[1]).booleanValue();
            graphics.translate(i2, i3);
            if (zBooleanValue) {
                for (int i6 = 0; i6 < this.f12819w; i6 += 64) {
                    int iMin = Math.min(64, this.f12819w - i6);
                    graphics.drawImage(image, i6, 0, i6 + iMin, this.f12820h, 0, 0, iMin, this.f12820h, null);
                }
            } else {
                for (int i7 = 0; i7 < this.f12820h; i7 += 64) {
                    int iMin2 = Math.min(64, this.f12820h - i7);
                    graphics.drawImage(image, 0, i7, this.f12819w, i7 + iMin2, 0, 0, this.f12819w, iMin2, null);
                }
            }
            graphics.translate(-i2, -i3);
        }

        private void drawVerticalGradient(Graphics2D graphics2D, float f2, float f3, Color color, Color color2, Color color3, int i2, int i3) {
            int i4 = (int) (f2 * i3);
            int i5 = (int) (f3 * i3);
            if (i4 > 0) {
                graphics2D.setPaint(getGradient(0.0f, 0.0f, color, 0.0f, i4, color2));
                graphics2D.fillRect(0, 0, i2, i4);
            }
            if (i5 > 0) {
                graphics2D.setColor(color2);
                graphics2D.fillRect(0, i4, i2, i5);
            }
            if (i4 > 0) {
                graphics2D.setPaint(getGradient(0.0f, i4 + i5, color2, 0.0f, (i4 * 2.0f) + i5, color));
                graphics2D.fillRect(0, i4 + i5, i2, i4);
            }
            if ((i3 - (i4 * 2)) - i5 > 0) {
                graphics2D.setPaint(getGradient(0.0f, (i4 * 2.0f) + i5, color, 0.0f, i3, color3));
                graphics2D.fillRect(0, (i4 * 2) + i5, i2, (i3 - (i4 * 2)) - i5);
            }
        }

        private void drawHorizontalGradient(Graphics2D graphics2D, float f2, float f3, Color color, Color color2, Color color3, int i2, int i3) {
            int i4 = (int) (f2 * i2);
            int i5 = (int) (f3 * i2);
            if (i4 > 0) {
                graphics2D.setPaint(getGradient(0.0f, 0.0f, color, i4, 0.0f, color2));
                graphics2D.fillRect(0, 0, i4, i3);
            }
            if (i5 > 0) {
                graphics2D.setColor(color2);
                graphics2D.fillRect(i4, 0, i5, i3);
            }
            if (i4 > 0) {
                graphics2D.setPaint(getGradient(i4 + i5, 0.0f, color2, (i4 * 2.0f) + i5, 0.0f, color));
                graphics2D.fillRect(i4 + i5, 0, i4, i3);
            }
            if ((i2 - (i4 * 2)) - i5 > 0) {
                graphics2D.setPaint(getGradient((i4 * 2.0f) + i5, 0.0f, color, i2, 0.0f, color3));
                graphics2D.fillRect((i4 * 2) + i5, 0, (i2 - (i4 * 2)) - i5, i3);
            }
        }

        private GradientPaint getGradient(float f2, float f3, Color color, float f4, float f5, Color color2) {
            return new GradientPaint(f2, f3, color, f4, f5, color2, true);
        }
    }

    static boolean isToolBarButton(JComponent jComponent) {
        return jComponent.getParent() instanceof JToolBar;
    }

    static Icon getOceanToolBarIcon(Image image) {
        return new ImageIconUIResource(Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), new OceanToolBarImageFilter())));
    }

    static Icon getOceanDisabledButtonIcon(Image image) {
        Object[] objArr = (Object[]) UIManager.get("Button.disabledGrayRange");
        int iIntValue = 180;
        int iIntValue2 = 215;
        if (objArr != null) {
            iIntValue = ((Integer) objArr[0]).intValue();
            iIntValue2 = ((Integer) objArr[1]).intValue();
        }
        return new ImageIconUIResource(Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), new OceanDisabledButtonImageFilter(iIntValue, iIntValue2))));
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalUtils$OceanDisabledButtonImageFilter.class */
    private static class OceanDisabledButtonImageFilter extends RGBImageFilter {
        private float min;
        private float factor;

        OceanDisabledButtonImageFilter(int i2, int i3) {
            this.canFilterIndexColorModel = true;
            this.min = i2;
            this.factor = (i3 - i2) / 255.0f;
        }

        @Override // java.awt.image.RGBImageFilter
        public int filterRGB(int i2, int i3, int i4) {
            int iMin = Math.min(255, (int) ((((0.2125f * ((i4 >> 16) & 255)) + (0.7154f * ((i4 >> 8) & 255)) + (0.0721f * (i4 & 255)) + 0.5f) * this.factor) + this.min));
            return (i4 & (-16777216)) | (iMin << 16) | (iMin << 8) | (iMin << 0);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalUtils$OceanToolBarImageFilter.class */
    private static class OceanToolBarImageFilter extends RGBImageFilter {
        OceanToolBarImageFilter() {
            this.canFilterIndexColorModel = true;
        }

        @Override // java.awt.image.RGBImageFilter
        public int filterRGB(int i2, int i3, int i4) {
            int iMax = Math.max(Math.max((i4 >> 16) & 255, (i4 >> 8) & 255), i4 & 255);
            return (i4 & (-16777216)) | (iMax << 16) | (iMax << 8) | (iMax << 0);
        }
    }
}
