package javax.swing.plaf.nimbus;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.image.ImageObserver;
import java.awt.image.VolatileImage;
import java.awt.print.PrinterGraphics;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.Painter;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.nimbus.ImageScalingHelper;
import sun.reflect.misc.MethodUtil;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/AbstractRegionPainter.class */
public abstract class AbstractRegionPainter implements Painter<JComponent> {
    private PaintContext ctx;

    /* renamed from: f, reason: collision with root package name */
    private float f12821f;
    private float leftWidth;
    private float topHeight;
    private float centerWidth;
    private float centerHeight;
    private float rightWidth;
    private float bottomHeight;
    private float leftScale;
    private float topScale;
    private float centerHScale;
    private float centerVScale;
    private float rightScale;
    private float bottomScale;

    protected abstract PaintContext getPaintContext();

    protected abstract void doPaint(Graphics2D graphics2D, JComponent jComponent, int i2, int i3, Object[] objArr);

    protected AbstractRegionPainter() {
    }

    @Override // javax.swing.Painter
    public final void paint(Graphics2D graphics2D, JComponent jComponent, int i2, int i3) {
        if (i2 <= 0 || i3 <= 0) {
            return;
        }
        Object[] extendedCacheKeys = getExtendedCacheKeys(jComponent);
        this.ctx = getPaintContext();
        PaintContext.CacheMode cacheMode = this.ctx == null ? PaintContext.CacheMode.NO_CACHING : this.ctx.cacheMode;
        if (cacheMode == PaintContext.CacheMode.NO_CACHING || !ImageCache.getInstance().isImageCachable(i2, i3) || (graphics2D instanceof PrinterGraphics)) {
            paint0(graphics2D, jComponent, i2, i3, extendedCacheKeys);
        } else if (cacheMode == PaintContext.CacheMode.FIXED_SIZES) {
            paintWithFixedSizeCaching(graphics2D, jComponent, i2, i3, extendedCacheKeys);
        } else {
            paintWith9SquareCaching(graphics2D, this.ctx, jComponent, i2, i3, extendedCacheKeys);
        }
    }

    protected Object[] getExtendedCacheKeys(JComponent jComponent) {
        return null;
    }

    protected void configureGraphics(Graphics2D graphics2D) {
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    protected final float decodeX(float f2) {
        if (f2 >= 0.0f && f2 <= 1.0f) {
            return f2 * this.leftWidth;
        }
        if (f2 > 1.0f && f2 < 2.0f) {
            return ((f2 - 1.0f) * this.centerWidth) + this.leftWidth;
        }
        if (f2 >= 2.0f && f2 <= 3.0f) {
            return ((f2 - 2.0f) * this.rightWidth) + this.leftWidth + this.centerWidth;
        }
        throw new IllegalArgumentException("Invalid x");
    }

    protected final float decodeY(float f2) {
        if (f2 >= 0.0f && f2 <= 1.0f) {
            return f2 * this.topHeight;
        }
        if (f2 > 1.0f && f2 < 2.0f) {
            return ((f2 - 1.0f) * this.centerHeight) + this.topHeight;
        }
        if (f2 >= 2.0f && f2 <= 3.0f) {
            return ((f2 - 2.0f) * this.bottomHeight) + this.topHeight + this.centerHeight;
        }
        throw new IllegalArgumentException("Invalid y");
    }

    protected final float decodeAnchorX(float f2, float f3) {
        if (f2 >= 0.0f && f2 <= 1.0f) {
            return decodeX(f2) + (f3 * this.leftScale);
        }
        if (f2 > 1.0f && f2 < 2.0f) {
            return decodeX(f2) + (f3 * this.centerHScale);
        }
        if (f2 >= 2.0f && f2 <= 3.0f) {
            return decodeX(f2) + (f3 * this.rightScale);
        }
        throw new IllegalArgumentException("Invalid x");
    }

    protected final float decodeAnchorY(float f2, float f3) {
        if (f2 >= 0.0f && f2 <= 1.0f) {
            return decodeY(f2) + (f3 * this.topScale);
        }
        if (f2 > 1.0f && f2 < 2.0f) {
            return decodeY(f2) + (f3 * this.centerVScale);
        }
        if (f2 >= 2.0f && f2 <= 3.0f) {
            return decodeY(f2) + (f3 * this.bottomScale);
        }
        throw new IllegalArgumentException("Invalid y");
    }

    protected final Color decodeColor(String str, float f2, float f3, float f4, int i2) {
        if (UIManager.getLookAndFeel() instanceof NimbusLookAndFeel) {
            return ((NimbusLookAndFeel) UIManager.getLookAndFeel()).getDerivedColor(str, f2, f3, f4, i2, true);
        }
        return Color.getHSBColor(f2, f3, f4);
    }

    protected final Color decodeColor(Color color, Color color2, float f2) {
        return new Color(NimbusLookAndFeel.deriveARGB(color, color2, f2));
    }

    protected final LinearGradientPaint decodeGradient(float f2, float f3, float f4, float f5, float[] fArr, Color[] colorArr) {
        if (f2 == f4 && f3 == f5) {
            f5 += 1.0E-5f;
        }
        return new LinearGradientPaint(f2, f3, f4, f5, fArr, colorArr);
    }

    protected final RadialGradientPaint decodeRadialGradient(float f2, float f3, float f4, float[] fArr, Color[] colorArr) {
        if (f4 == 0.0f) {
            f4 = 1.0E-5f;
        }
        return new RadialGradientPaint(f2, f3, f4, fArr, colorArr);
    }

    protected final Color getComponentColor(JComponent jComponent, String str, Color color, float f2, float f3, int i2) {
        Color selectionBackground = null;
        if (jComponent != null) {
            if ("background".equals(str)) {
                selectionBackground = jComponent.getBackground();
            } else if ("foreground".equals(str)) {
                selectionBackground = jComponent.getForeground();
            } else if ((jComponent instanceof JList) && "selectionForeground".equals(str)) {
                selectionBackground = ((JList) jComponent).getSelectionForeground();
            } else if ((jComponent instanceof JList) && "selectionBackground".equals(str)) {
                selectionBackground = ((JList) jComponent).getSelectionBackground();
            } else if ((jComponent instanceof JTable) && "selectionForeground".equals(str)) {
                selectionBackground = ((JTable) jComponent).getSelectionForeground();
            } else if ((jComponent instanceof JTable) && "selectionBackground".equals(str)) {
                selectionBackground = ((JTable) jComponent).getSelectionBackground();
            } else {
                try {
                    selectionBackground = (Color) MethodUtil.invoke(MethodUtil.getMethod(jComponent.getClass(), "get" + Character.toUpperCase(str.charAt(0)) + str.substring(1), null), jComponent, null);
                } catch (Exception e2) {
                }
                if (selectionBackground == null) {
                    Object clientProperty = jComponent.getClientProperty(str);
                    if (clientProperty instanceof Color) {
                        selectionBackground = (Color) clientProperty;
                    }
                }
            }
        }
        if (selectionBackground == null || (selectionBackground instanceof UIResource)) {
            return color;
        }
        if (f2 != 0.0f || f3 != 0.0f || i2 != 0) {
            float[] fArrRGBtoHSB = Color.RGBtoHSB(selectionBackground.getRed(), selectionBackground.getGreen(), selectionBackground.getBlue(), null);
            fArrRGBtoHSB[1] = clamp(fArrRGBtoHSB[1] + f2);
            fArrRGBtoHSB[2] = clamp(fArrRGBtoHSB[2] + f3);
            return new Color((Color.HSBtoRGB(fArrRGBtoHSB[0], fArrRGBtoHSB[1], fArrRGBtoHSB[2]) & 16777215) | (clamp(selectionBackground.getAlpha() + i2) << 24));
        }
        return selectionBackground;
    }

    /* loaded from: rt.jar:javax/swing/plaf/nimbus/AbstractRegionPainter$PaintContext.class */
    protected static class PaintContext {
        private static Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
        private Insets stretchingInsets;
        private Dimension canvasSize;
        private boolean inverted;
        private CacheMode cacheMode;
        private double maxHorizontalScaleFactor;
        private double maxVerticalScaleFactor;

        /* renamed from: a, reason: collision with root package name */
        private float f12822a;

        /* renamed from: b, reason: collision with root package name */
        private float f12823b;

        /* renamed from: c, reason: collision with root package name */
        private float f12824c;

        /* renamed from: d, reason: collision with root package name */
        private float f12825d;
        private float aPercent;
        private float bPercent;
        private float cPercent;
        private float dPercent;

        /* loaded from: rt.jar:javax/swing/plaf/nimbus/AbstractRegionPainter$PaintContext$CacheMode.class */
        protected enum CacheMode {
            NO_CACHING,
            FIXED_SIZES,
            NINE_SQUARE_SCALE
        }

        public PaintContext(Insets insets, Dimension dimension, boolean z2) {
            this(insets, dimension, z2, null, 1.0d, 1.0d);
        }

        public PaintContext(Insets insets, Dimension dimension, boolean z2, CacheMode cacheMode, double d2, double d3) {
            if (d2 < 1.0d || d2 < 1.0d) {
                throw new IllegalArgumentException("Both maxH and maxV must be >= 1");
            }
            this.stretchingInsets = insets == null ? EMPTY_INSETS : insets;
            this.canvasSize = dimension;
            this.inverted = z2;
            this.cacheMode = cacheMode == null ? CacheMode.NO_CACHING : cacheMode;
            this.maxHorizontalScaleFactor = d2;
            this.maxVerticalScaleFactor = d3;
            if (dimension != null) {
                this.f12822a = this.stretchingInsets.left;
                this.f12823b = dimension.width - this.stretchingInsets.right;
                this.f12824c = this.stretchingInsets.top;
                this.f12825d = dimension.height - this.stretchingInsets.bottom;
                this.canvasSize = dimension;
                this.inverted = z2;
                if (z2) {
                    float f2 = dimension.width - (this.f12823b - this.f12822a);
                    this.aPercent = f2 > 0.0f ? this.f12822a / f2 : 0.0f;
                    this.bPercent = f2 > 0.0f ? this.f12823b / f2 : 0.0f;
                    float f3 = dimension.height - (this.f12825d - this.f12824c);
                    this.cPercent = f3 > 0.0f ? this.f12824c / f3 : 0.0f;
                    this.dPercent = f3 > 0.0f ? this.f12825d / f3 : 0.0f;
                }
            }
        }
    }

    private void prepare(float f2, float f3) {
        if (this.ctx == null || this.ctx.canvasSize == null) {
            this.f12821f = 1.0f;
            this.rightWidth = 0.0f;
            this.centerWidth = 0.0f;
            this.leftWidth = 0.0f;
            this.bottomHeight = 0.0f;
            this.centerHeight = 0.0f;
            this.topHeight = 0.0f;
            this.rightScale = 0.0f;
            this.centerHScale = 0.0f;
            this.leftScale = 0.0f;
            this.bottomScale = 0.0f;
            this.centerVScale = 0.0f;
            this.topScale = 0.0f;
            return;
        }
        Number number = (Number) UIManager.get("scale");
        this.f12821f = number == null ? 1.0f : number.floatValue();
        if (this.ctx.inverted) {
            this.centerWidth = (this.ctx.f12823b - this.ctx.f12822a) * this.f12821f;
            float f4 = f2 - this.centerWidth;
            this.leftWidth = f4 * this.ctx.aPercent;
            this.rightWidth = f4 * this.ctx.bPercent;
            this.centerHeight = (this.ctx.f12825d - this.ctx.f12824c) * this.f12821f;
            float f5 = f3 - this.centerHeight;
            this.topHeight = f5 * this.ctx.cPercent;
            this.bottomHeight = f5 * this.ctx.dPercent;
        } else {
            this.leftWidth = this.ctx.f12822a * this.f12821f;
            this.rightWidth = ((float) (this.ctx.canvasSize.getWidth() - this.ctx.f12823b)) * this.f12821f;
            this.centerWidth = (f2 - this.leftWidth) - this.rightWidth;
            this.topHeight = this.ctx.f12824c * this.f12821f;
            this.bottomHeight = ((float) (this.ctx.canvasSize.getHeight() - this.ctx.f12825d)) * this.f12821f;
            this.centerHeight = (f3 - this.topHeight) - this.bottomHeight;
        }
        this.leftScale = this.ctx.f12822a == 0.0f ? 0.0f : this.leftWidth / this.ctx.f12822a;
        this.centerHScale = this.ctx.f12823b - this.ctx.f12822a == 0.0f ? 0.0f : this.centerWidth / (this.ctx.f12823b - this.ctx.f12822a);
        this.rightScale = ((float) this.ctx.canvasSize.width) - this.ctx.f12823b == 0.0f ? 0.0f : this.rightWidth / (this.ctx.canvasSize.width - this.ctx.f12823b);
        this.topScale = this.ctx.f12824c == 0.0f ? 0.0f : this.topHeight / this.ctx.f12824c;
        this.centerVScale = this.ctx.f12825d - this.ctx.f12824c == 0.0f ? 0.0f : this.centerHeight / (this.ctx.f12825d - this.ctx.f12824c);
        this.bottomScale = ((float) this.ctx.canvasSize.height) - this.ctx.f12825d == 0.0f ? 0.0f : this.bottomHeight / (this.ctx.canvasSize.height - this.ctx.f12825d);
    }

    private void paintWith9SquareCaching(Graphics2D graphics2D, PaintContext paintContext, JComponent jComponent, int i2, int i3, Object[] objArr) {
        Insets insets;
        Dimension dimension = paintContext.canvasSize;
        Insets insets2 = paintContext.stretchingInsets;
        if (i2 <= dimension.width * paintContext.maxHorizontalScaleFactor && i3 <= dimension.height * paintContext.maxVerticalScaleFactor) {
            VolatileImage image = getImage(graphics2D.getDeviceConfiguration(), jComponent, dimension.width, dimension.height, objArr);
            if (image != null) {
                if (paintContext.inverted) {
                    int i4 = (i2 - (dimension.width - (insets2.left + insets2.right))) / 2;
                    int i5 = (i3 - (dimension.height - (insets2.top + insets2.bottom))) / 2;
                    insets = new Insets(i5, i4, i5, i4);
                } else {
                    insets = insets2;
                }
                Object renderingHint = graphics2D.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
                graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                ImageScalingHelper.paint(graphics2D, 0, 0, i2, i3, image, insets2, insets, ImageScalingHelper.PaintType.PAINT9_STRETCH, 512);
                graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, renderingHint != null ? renderingHint : RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                return;
            }
            paint0(graphics2D, jComponent, i2, i3, objArr);
            return;
        }
        paint0(graphics2D, jComponent, i2, i3, objArr);
    }

    private void paintWithFixedSizeCaching(Graphics2D graphics2D, JComponent jComponent, int i2, int i3, Object[] objArr) {
        VolatileImage image = getImage(graphics2D.getDeviceConfiguration(), jComponent, i2, i3, objArr);
        if (image != null) {
            graphics2D.drawImage(image, 0, 0, (ImageObserver) null);
        } else {
            paint0(graphics2D, jComponent, i2, i3, objArr);
        }
    }

    private VolatileImage getImage(GraphicsConfiguration graphicsConfiguration, JComponent jComponent, int i2, int i3, Object[] objArr) {
        int i4;
        ImageCache imageCache = ImageCache.getInstance();
        VolatileImage volatileImageCreateCompatibleVolatileImage = (VolatileImage) imageCache.getImage(graphicsConfiguration, i2, i3, this, objArr);
        int i5 = 0;
        do {
            int iValidate = 2;
            if (volatileImageCreateCompatibleVolatileImage != null) {
                iValidate = volatileImageCreateCompatibleVolatileImage.validate(graphicsConfiguration);
            }
            if (iValidate == 2 || iValidate == 1) {
                if (volatileImageCreateCompatibleVolatileImage == null || volatileImageCreateCompatibleVolatileImage.getWidth() != i2 || volatileImageCreateCompatibleVolatileImage.getHeight() != i3 || iValidate == 2) {
                    if (volatileImageCreateCompatibleVolatileImage != null) {
                        volatileImageCreateCompatibleVolatileImage.flush();
                    }
                    volatileImageCreateCompatibleVolatileImage = graphicsConfiguration.createCompatibleVolatileImage(i2, i3, 3);
                    imageCache.setImage(volatileImageCreateCompatibleVolatileImage, graphicsConfiguration, i2, i3, this, objArr);
                }
                Graphics2D graphics2DCreateGraphics = volatileImageCreateCompatibleVolatileImage.createGraphics();
                graphics2DCreateGraphics.setComposite(AlphaComposite.Clear);
                graphics2DCreateGraphics.fillRect(0, 0, i2, i3);
                graphics2DCreateGraphics.setComposite(AlphaComposite.SrcOver);
                configureGraphics(graphics2DCreateGraphics);
                paint0(graphics2DCreateGraphics, jComponent, i2, i3, objArr);
                graphics2DCreateGraphics.dispose();
            }
            if (!volatileImageCreateCompatibleVolatileImage.contentsLost()) {
                break;
            }
            i4 = i5;
            i5++;
        } while (i4 < 3);
        if (i5 == 3) {
            return null;
        }
        return volatileImageCreateCompatibleVolatileImage;
    }

    private void paint0(Graphics2D graphics2D, JComponent jComponent, int i2, int i3, Object[] objArr) {
        prepare(i2, i3);
        Graphics2D graphics2D2 = (Graphics2D) graphics2D.create();
        configureGraphics(graphics2D2);
        doPaint(graphics2D2, jComponent, i2, i3, objArr);
        graphics2D2.dispose();
    }

    private float clamp(float f2) {
        if (f2 < 0.0f) {
            f2 = 0.0f;
        } else if (f2 > 1.0f) {
            f2 = 1.0f;
        }
        return f2;
    }

    private int clamp(int i2) {
        if (i2 < 0) {
            i2 = 0;
        } else if (i2 > 255) {
            i2 = 255;
        }
        return i2;
    }
}
