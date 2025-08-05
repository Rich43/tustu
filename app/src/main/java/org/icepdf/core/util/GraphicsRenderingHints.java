package org.icepdf.core.util;

import java.awt.Color;
import java.awt.RenderingHints;

/* loaded from: icepdf-core.jar:org/icepdf/core/util/GraphicsRenderingHints.class */
public class GraphicsRenderingHints {
    public static final int SCREEN = 1;
    public static final int PRINT = 2;
    private static GraphicsRenderingHints singleton;
    Object printAlphaInterpolocation = RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY;
    Object printAntiAliasing = RenderingHints.VALUE_ANTIALIAS_ON;
    Object printTextAntiAliasing = RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
    Object printColorRendering = RenderingHints.VALUE_COLOR_RENDER_QUALITY;
    Object printDithering = RenderingHints.VALUE_DITHER_ENABLE;
    Object printFractionalMetrics = RenderingHints.VALUE_FRACTIONALMETRICS_ON;
    Object printInterPolation = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
    Object printRendering = RenderingHints.VALUE_RENDER_QUALITY;
    Object printStrokeControl = RenderingHints.VALUE_STROKE_NORMALIZE;
    Color printBackground = Color.white;
    Object screenAlphaInterpolocation = RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED;
    Object screenAntiAliasing = RenderingHints.VALUE_ANTIALIAS_ON;
    Object screenTextAntiAliasing = RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
    Object screenColorRendering = RenderingHints.VALUE_COLOR_RENDER_SPEED;
    Object screenDithering = RenderingHints.VALUE_DITHER_DEFAULT;
    Object screenFractionalMetrics = RenderingHints.VALUE_FRACTIONALMETRICS_ON;
    Object screenInterPolation = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
    Object screenRendering = RenderingHints.VALUE_RENDER_SPEED;
    Object screenStrokeControl = RenderingHints.VALUE_STROKE_PURE;
    Color screenBackground = Color.white;
    private RenderingHints screenHints;
    private RenderingHints printHints;

    public static synchronized GraphicsRenderingHints getDefault() {
        if (singleton == null) {
            singleton = new GraphicsRenderingHints();
        }
        return singleton;
    }

    private GraphicsRenderingHints() {
        setFromProperties();
    }

    public RenderingHints getRenderingHints(int hintType) {
        if (hintType == 1) {
            return (RenderingHints) this.screenHints.clone();
        }
        return (RenderingHints) this.printHints.clone();
    }

    public Color getPageBackgroundColor(int hintType) {
        if (hintType == 1) {
            return this.screenBackground;
        }
        return this.printBackground;
    }

    public synchronized void reset() {
        setFromProperties();
    }

    private void setFromProperties() {
        String property = Defs.sysProperty("org.icepdf.core.screen.alphaInterpolation");
        if (property != null) {
            if (property.equalsIgnoreCase("VALUE_ALPHA_INTERPOLATION_QUALITY")) {
                this.screenAlphaInterpolocation = RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY;
            } else if (property.equalsIgnoreCase("VALUE_ALPHA_INTERPOLATION_DEFAULT")) {
                this.screenAlphaInterpolocation = RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT;
            } else if (property.equalsIgnoreCase("VALUE_ALPHA_INTERPOLATION_SPEED")) {
                this.screenAlphaInterpolocation = RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED;
            }
        }
        String property2 = Defs.sysProperty("org.icepdf.core.screen.antiAliasing");
        if (property2 != null) {
            if (property2.equalsIgnoreCase("VALUE_ANTIALIAS_DEFAULT")) {
                this.screenAntiAliasing = RenderingHints.VALUE_ANTIALIAS_DEFAULT;
            } else if (property2.equalsIgnoreCase("VALUE_ANTIALIAS_ON")) {
                this.screenAntiAliasing = RenderingHints.VALUE_ANTIALIAS_ON;
            } else if (property2.equalsIgnoreCase("VALUE_ANTIALIAS_OFF")) {
                this.screenAntiAliasing = RenderingHints.VALUE_ANTIALIAS_OFF;
            }
        }
        String property3 = Defs.sysProperty("org.icepdf.core.screen.textAntiAliasing");
        if (property3 != null) {
            if (property3.equalsIgnoreCase("VALUE_TEXT_ANTIALIAS_DEFAULT")) {
                this.screenTextAntiAliasing = RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT;
            } else if (property3.equalsIgnoreCase("VALUE_TEXT_ANTIALIAS_ON")) {
                this.screenTextAntiAliasing = RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
            } else if (property3.equalsIgnoreCase("VALUE_TEXT_ANTIALIAS_OFF")) {
                this.screenTextAntiAliasing = RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
            }
        }
        String property4 = Defs.sysProperty("org.icepdf.core.screen.colorRender");
        if (property4 != null) {
            if (property4.equalsIgnoreCase("VALUE_COLOR_RENDER_DEFAULT")) {
                this.screenColorRendering = RenderingHints.VALUE_COLOR_RENDER_DEFAULT;
            } else if (property4.equalsIgnoreCase("VALUE_COLOR_RENDER_QUALITY")) {
                this.screenColorRendering = RenderingHints.VALUE_COLOR_RENDER_QUALITY;
            } else if (property4.equalsIgnoreCase("VALUE_COLOR_RENDER_SPEED")) {
                this.screenColorRendering = RenderingHints.VALUE_COLOR_RENDER_SPEED;
            }
        }
        String property5 = Defs.sysProperty("org.icepdf.core.screen.dither");
        if (property5 != null) {
            if (property5.equalsIgnoreCase("VALUE_DITHER_DEFAULT")) {
                this.screenDithering = RenderingHints.VALUE_DITHER_DEFAULT;
            } else if (property5.equalsIgnoreCase("VALUE_DITHER_DISABLE")) {
                this.screenDithering = RenderingHints.VALUE_DITHER_DISABLE;
            } else if (property5.equalsIgnoreCase("VALUE_DITHER_ENABLE")) {
                this.screenDithering = RenderingHints.VALUE_DITHER_ENABLE;
            }
        }
        String property6 = Defs.sysProperty("org.icepdf.core.screen.fractionalmetrics");
        if (property6 != null) {
            if (property6.equalsIgnoreCase("VALUE_FRACTIONALMETRICS_DEFAULT")) {
                this.screenFractionalMetrics = RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT;
            } else if (property6.equalsIgnoreCase("VALUE_FRACTIONALMETRICS_ON")) {
                this.screenFractionalMetrics = RenderingHints.VALUE_FRACTIONALMETRICS_ON;
            } else if (property6.equalsIgnoreCase("VALUE_FRACTIONALMETRICS_OFF")) {
                this.screenFractionalMetrics = RenderingHints.VALUE_FRACTIONALMETRICS_OFF;
            }
        }
        String property7 = Defs.sysProperty("org.icepdf.core.screen.interpolation");
        if (property7 != null) {
            if (property7.equalsIgnoreCase("VALUE_INTERPOLATION_BICUBIC")) {
                this.screenInterPolation = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
            } else if (property7.equalsIgnoreCase("VALUE_INTERPOLATION_BILINEAR")) {
                this.screenInterPolation = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
            } else if (property7.equalsIgnoreCase("VALUE_INTERPOLATION_NEAREST_NEIGHBOR")) {
                this.screenInterPolation = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
            }
        }
        String property8 = Defs.sysProperty("org.icepdf.core.screen.render");
        if (property8 != null) {
            if (property8.equalsIgnoreCase("VALUE_RENDER_DEFAULT")) {
                this.screenRendering = RenderingHints.VALUE_RENDER_DEFAULT;
            } else if (property8.equalsIgnoreCase("VALUE_RENDER_QUALITY")) {
                this.screenRendering = RenderingHints.VALUE_RENDER_QUALITY;
            } else if (property8.equalsIgnoreCase("VALUE_RENDER_SPEED")) {
                this.screenRendering = RenderingHints.VALUE_RENDER_SPEED;
            }
        }
        String property9 = Defs.sysProperty("org.icepdf.core.screen.stroke");
        if (property9 != null) {
            if (property9.equalsIgnoreCase("VALUE_STROKE_DEFAULT")) {
                this.screenStrokeControl = RenderingHints.VALUE_STROKE_DEFAULT;
            } else if (property9.equalsIgnoreCase("VALUE_STROKE_NORMALIZE")) {
                this.screenStrokeControl = RenderingHints.VALUE_STROKE_NORMALIZE;
            } else if (property9.equalsIgnoreCase("VALUE_STROKE_PURE")) {
                this.screenStrokeControl = RenderingHints.VALUE_STROKE_PURE;
            }
        }
        String property10 = Defs.sysProperty("org.icepdf.core.screen.background");
        if (property10 != null) {
            if (property10.equalsIgnoreCase("VALUE_DRAW_WHITE_BACKGROUND")) {
                this.screenBackground = Color.white;
            } else if (property10.equalsIgnoreCase("VALUE_DRAW_NO_BACKGROUND")) {
                this.screenBackground = null;
            }
        }
        this.screenHints = new RenderingHints(RenderingHints.KEY_ALPHA_INTERPOLATION, this.screenAlphaInterpolocation);
        this.screenHints.put(RenderingHints.KEY_ANTIALIASING, this.screenAntiAliasing);
        this.screenHints.put(RenderingHints.KEY_TEXT_ANTIALIASING, this.screenTextAntiAliasing);
        this.screenHints.put(RenderingHints.KEY_COLOR_RENDERING, this.screenColorRendering);
        this.screenHints.put(RenderingHints.KEY_DITHERING, this.screenDithering);
        this.screenHints.put(RenderingHints.KEY_FRACTIONALMETRICS, this.screenFractionalMetrics);
        this.screenHints.put(RenderingHints.KEY_INTERPOLATION, this.screenInterPolation);
        this.screenHints.put(RenderingHints.KEY_RENDERING, this.screenRendering);
        this.screenHints.put(RenderingHints.KEY_STROKE_CONTROL, this.screenStrokeControl);
        String property11 = Defs.sysProperty("org.icepdf.core.print.alphaInterpolation");
        if (property11 != null) {
            if (property11.equalsIgnoreCase("VALUE_ALPHA_INTERPOLATION_QUALITY")) {
                this.printAlphaInterpolocation = RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY;
            } else if (property11.equalsIgnoreCase("VALUE_ALPHA_INTERPOLATION_DEFAULT")) {
                this.printAlphaInterpolocation = RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT;
            } else if (property11.equalsIgnoreCase("VALUE_ALPHA_INTERPOLATION_SPEED")) {
                this.printAlphaInterpolocation = RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED;
            }
        }
        String property12 = Defs.sysProperty("org.icepdf.core.print.antiAliasing");
        if (property12 != null) {
            if (property12.equalsIgnoreCase("VALUE_ANTIALIAS_DEFAULT")) {
                this.printAntiAliasing = RenderingHints.VALUE_ANTIALIAS_DEFAULT;
            } else if (property12.equalsIgnoreCase("VALUE_ANTIALIAS_ON")) {
                this.printAntiAliasing = RenderingHints.VALUE_ANTIALIAS_ON;
            } else if (property12.equalsIgnoreCase("VALUE_ANTIALIAS_OFF")) {
                this.printAntiAliasing = RenderingHints.VALUE_ANTIALIAS_OFF;
            }
        }
        String property13 = Defs.sysProperty("org.icepdf.core.print.textAntiAliasing");
        if (property13 != null) {
            if (property13.equalsIgnoreCase("VALUE_TEXT_ANTIALIAS_DEFAULT")) {
                this.printTextAntiAliasing = RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT;
            } else if (property13.equalsIgnoreCase("VALUE_TEXT_ANTIALIAS_ON")) {
                this.printTextAntiAliasing = RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
            } else if (property13.equalsIgnoreCase("VALUE_TEXT_ANTIALIAS_OFF")) {
                this.printTextAntiAliasing = RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
            }
        }
        String property14 = Defs.sysProperty("org.icepdf.core.print.colorRender");
        if (property14 != null) {
            if (property14.equalsIgnoreCase("VALUE_COLOR_RENDER_DEFAULT")) {
                this.printColorRendering = RenderingHints.VALUE_COLOR_RENDER_DEFAULT;
            } else if (property14.equalsIgnoreCase("VALUE_COLOR_RENDER_QUALITY")) {
                this.printColorRendering = RenderingHints.VALUE_COLOR_RENDER_QUALITY;
            } else if (property14.equalsIgnoreCase("VALUE_COLOR_RENDER_SPEED")) {
                this.printColorRendering = RenderingHints.VALUE_COLOR_RENDER_SPEED;
            }
        }
        String property15 = Defs.sysProperty("org.icepdf.core.print.dither");
        if (property15 != null) {
            if (property15.equalsIgnoreCase("VALUE_DITHER_DEFAULT")) {
                this.printDithering = RenderingHints.VALUE_DITHER_DEFAULT;
            } else if (property15.equalsIgnoreCase("VALUE_DITHER_DISABLE")) {
                this.printDithering = RenderingHints.VALUE_DITHER_DISABLE;
            } else if (property15.equalsIgnoreCase("VALUE_DITHER_ENABLE")) {
                this.printDithering = RenderingHints.VALUE_DITHER_ENABLE;
            }
        }
        String property16 = Defs.sysProperty("org.icepdf.core.print.fractionalmetrics");
        if (property16 != null) {
            if (property16.equalsIgnoreCase("VALUE_FRACTIONALMETRICS_DEFAULT")) {
                this.printFractionalMetrics = RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT;
            } else if (property16.equalsIgnoreCase("VALUE_FRACTIONALMETRICS_ON")) {
                this.printFractionalMetrics = RenderingHints.VALUE_FRACTIONALMETRICS_ON;
            } else if (property16.equalsIgnoreCase("VALUE_FRACTIONALMETRICS_OFF")) {
                this.printFractionalMetrics = RenderingHints.VALUE_FRACTIONALMETRICS_OFF;
            }
        }
        String property17 = Defs.sysProperty("org.icepdf.core.print.interpolation");
        if (property17 != null) {
            if (property17.equalsIgnoreCase("VALUE_INTERPOLATION_BICUBIC")) {
                this.printInterPolation = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
            } else if (property17.equalsIgnoreCase("VALUE_INTERPOLATION_BILINEAR")) {
                this.printInterPolation = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
            } else if (property17.equalsIgnoreCase("VALUE_INTERPOLATION_NEAREST_NEIGHBOR")) {
                this.printInterPolation = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
            }
        }
        String property18 = Defs.sysProperty("org.icepdf.core.print.render");
        if (property18 != null) {
            if (property18.equalsIgnoreCase("VALUE_RENDER_DEFAULT")) {
                this.printRendering = RenderingHints.VALUE_RENDER_DEFAULT;
            } else if (property18.equalsIgnoreCase("VALUE_RENDER_QUALITY")) {
                this.printRendering = RenderingHints.VALUE_RENDER_QUALITY;
            } else if (property18.equalsIgnoreCase("VALUE_RENDER_SPEED")) {
                this.printRendering = RenderingHints.VALUE_RENDER_SPEED;
            }
        }
        String property19 = Defs.sysProperty("org.icepdf.core.print.stroke");
        if (property19 != null) {
            if (property19.equalsIgnoreCase("VALUE_STROKE_DEFAULT")) {
                this.printStrokeControl = RenderingHints.VALUE_STROKE_DEFAULT;
            } else if (property19.equalsIgnoreCase("VALUE_STROKE_NORMALIZE")) {
                this.printStrokeControl = RenderingHints.VALUE_STROKE_NORMALIZE;
            } else if (property19.equalsIgnoreCase("VALUE_STROKE_PURE")) {
                this.printStrokeControl = RenderingHints.VALUE_STROKE_PURE;
            }
        }
        String property20 = Defs.sysProperty("org.icepdf.core.print.background");
        if (property20 != null) {
            if (property20.equalsIgnoreCase("VALUE_DRAW_WHITE_BACKGROUND")) {
                this.printBackground = Color.white;
            } else if (property20.equalsIgnoreCase("VALUE_DRAW_NO_BACKGROUND")) {
                this.printBackground = null;
            }
        }
        this.printHints = new RenderingHints(RenderingHints.KEY_ALPHA_INTERPOLATION, this.printAlphaInterpolocation);
        this.printHints.put(RenderingHints.KEY_ANTIALIASING, this.printAntiAliasing);
        this.printHints.put(RenderingHints.KEY_TEXT_ANTIALIASING, this.printTextAntiAliasing);
        this.printHints.put(RenderingHints.KEY_COLOR_RENDERING, this.printColorRendering);
        this.printHints.put(RenderingHints.KEY_DITHERING, this.printDithering);
        this.printHints.put(RenderingHints.KEY_FRACTIONALMETRICS, this.printFractionalMetrics);
        this.printHints.put(RenderingHints.KEY_INTERPOLATION, this.printInterPolation);
        this.printHints.put(RenderingHints.KEY_RENDERING, this.printRendering);
        this.printHints.put(RenderingHints.KEY_STROKE_CONTROL, this.printStrokeControl);
    }
}
