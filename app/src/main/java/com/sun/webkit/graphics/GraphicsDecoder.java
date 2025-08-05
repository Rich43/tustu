package com.sun.webkit.graphics;

import com.sun.prism.paint.Color;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;

/* loaded from: jfxrt.jar:com/sun/webkit/graphics/GraphicsDecoder.class */
public final class GraphicsDecoder {
    public static final int FILLRECT_FFFFI = 0;
    public static final int SETFILLCOLOR = 1;
    public static final int SETSTROKESTYLE = 2;
    public static final int SETSTROKECOLOR = 3;
    public static final int SETSTROKEWIDTH = 4;
    public static final int DRAWPOLYGON = 6;
    public static final int DRAWLINE = 7;
    public static final int DRAWIMAGE = 8;
    public static final int DRAWICON = 9;
    public static final int DRAWPATTERN = 10;
    public static final int TRANSLATE = 11;
    public static final int SAVESTATE = 12;
    public static final int RESTORESTATE = 13;
    public static final int CLIP_PATH = 14;
    public static final int SETCLIP_IIII = 15;
    public static final int DRAWRECT = 16;
    public static final int SETCOMPOSITE = 17;
    public static final int STROKEARC = 18;
    public static final int DRAWELLIPSE = 19;
    public static final int DRAWFOCUSRING = 20;
    public static final int SETALPHA = 21;
    public static final int BEGINTRANSPARENCYLAYER = 22;
    public static final int ENDTRANSPARENCYLAYER = 23;
    public static final int STROKE_PATH = 24;
    public static final int FILL_PATH = 25;
    public static final int GETIMAGE = 26;
    public static final int SCALE = 27;
    public static final int SETSHADOW = 28;
    public static final int DRAWSTRING = 29;
    public static final int DRAWSTRING_FAST = 31;
    public static final int DRAWWIDGET = 33;
    public static final int DRAWSCROLLBAR = 34;
    public static final int CLEARRECT_FFFF = 36;
    public static final int STROKERECT_FFFFF = 37;
    public static final int RENDERMEDIAPLAYER = 38;
    public static final int CONCATTRANSFORM_FFFFFF = 39;
    public static final int COPYREGION = 40;
    public static final int DECODERQ = 41;
    public static final int SET_TRANSFORM = 42;
    public static final int ROTATE = 43;
    public static final int RENDERMEDIACONTROL = 44;
    public static final int RENDERMEDIA_TIMETRACK = 45;
    public static final int RENDERMEDIA_VOLUMETRACK = 46;
    public static final int FILLRECT_FFFF = 47;
    public static final int FILL_ROUNDED_RECT = 48;
    public static final int SET_FILL_GRADIENT = 49;
    public static final int SET_STROKE_GRADIENT = 50;
    public static final int SET_LINE_DASH = 51;
    public static final int SET_LINE_CAP = 52;
    public static final int SET_LINE_JOIN = 53;
    public static final int SET_MITER_LIMIT = 54;
    public static final int SET_TEXT_MODE = 55;
    public static final int SET_PERSPECTIVE_TRANSFORM = 56;
    private static final Logger log = Logger.getLogger(GraphicsDecoder.class.getName());

    static void decode(WCGraphicsManager gm, WCGraphicsContext gc, BufferData bdata) {
        if (gc == null || !gc.isValid()) {
            log.fine("GraphicsDecoder::decode : GC is " + (gc == null ? FXMLLoader.NULL_KEYWORD : " invalid"));
            return;
        }
        ByteBuffer buf = bdata.getBuffer();
        buf.order(ByteOrder.nativeOrder());
        while (buf.remaining() > 0) {
            int op = buf.getInt();
            switch (op) {
                case 0:
                    gc.fillRect(buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), getColor(buf));
                    break;
                case 1:
                    gc.setFillColor(getColor(buf));
                    break;
                case 2:
                    gc.setStrokeStyle(buf.getInt());
                    break;
                case 3:
                    gc.setStrokeColor(getColor(buf));
                    break;
                case 4:
                    gc.setStrokeWidth(buf.getFloat());
                    break;
                case 5:
                case 26:
                case 30:
                case 32:
                case 35:
                default:
                    log.fine("ERROR. Unknown primitive found");
                    break;
                case 6:
                    gc.drawPolygon(getPath(gm, buf), buf.getInt() == -1);
                    break;
                case 7:
                    gc.drawLine(buf.getInt(), buf.getInt(), buf.getInt(), buf.getInt());
                    break;
                case 8:
                    drawImage(gc, gm.getRef(buf.getInt()), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat());
                    break;
                case 9:
                    gc.drawIcon((WCIcon) gm.getRef(buf.getInt()), buf.getInt(), buf.getInt());
                    break;
                case 10:
                    drawPattern(gc, gm.getRef(buf.getInt()), getRectangle(buf), (WCTransform) gm.getRef(buf.getInt()), getPoint(buf), getRectangle(buf));
                    break;
                case 11:
                    gc.translate(buf.getFloat(), buf.getFloat());
                    break;
                case 12:
                    gc.saveState();
                    break;
                case 13:
                    gc.restoreState();
                    break;
                case 14:
                    gc.setClip(getPath(gm, buf), buf.getInt() > 0);
                    break;
                case 15:
                    gc.setClip(buf.getInt(), buf.getInt(), buf.getInt(), buf.getInt());
                    break;
                case 16:
                    gc.drawRect(buf.getInt(), buf.getInt(), buf.getInt(), buf.getInt());
                    break;
                case 17:
                    gc.setComposite(buf.getInt());
                    break;
                case 18:
                    gc.strokeArc(buf.getInt(), buf.getInt(), buf.getInt(), buf.getInt(), buf.getInt(), buf.getInt());
                    break;
                case 19:
                    gc.drawEllipse(buf.getInt(), buf.getInt(), buf.getInt(), buf.getInt());
                    break;
                case 20:
                    gc.drawFocusRing(buf.getInt(), buf.getInt(), buf.getInt(), buf.getInt(), getColor(buf));
                    break;
                case 21:
                    gc.setAlpha(buf.getFloat());
                    break;
                case 22:
                    gc.beginTransparencyLayer(buf.getFloat());
                    break;
                case 23:
                    gc.endTransparencyLayer();
                    break;
                case 24:
                    gc.strokePath(getPath(gm, buf));
                    break;
                case 25:
                    gc.fillPath(getPath(gm, buf));
                    break;
                case 27:
                    gc.scale(buf.getFloat(), buf.getFloat());
                    break;
                case 28:
                    gc.setShadow(buf.getFloat(), buf.getFloat(), buf.getFloat(), getColor(buf));
                    break;
                case 29:
                    gc.drawString((WCFont) gm.getRef(buf.getInt()), bdata.getString(buf.getInt()), buf.getInt() == -1, buf.getInt(), buf.getInt(), buf.getFloat(), buf.getFloat());
                    break;
                case 31:
                    gc.drawString((WCFont) gm.getRef(buf.getInt()), bdata.getIntArray(buf.getInt()), bdata.getFloatArray(buf.getInt()), buf.getFloat(), buf.getFloat());
                    break;
                case 33:
                    gc.drawWidget((RenderTheme) gm.getRef(buf.getInt()), gm.getRef(buf.getInt()), buf.getInt(), buf.getInt());
                    break;
                case 34:
                    gc.drawScrollbar((ScrollBarTheme) gm.getRef(buf.getInt()), gm.getRef(buf.getInt()), buf.getInt(), buf.getInt(), buf.getInt(), buf.getInt());
                    break;
                case 36:
                    gc.clearRect(buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat());
                    break;
                case 37:
                    gc.strokeRect(buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat());
                    break;
                case 38:
                    WCMediaPlayer mp = (WCMediaPlayer) gm.getRef(buf.getInt());
                    mp.render(gc, buf.getInt(), buf.getInt(), buf.getInt(), buf.getInt());
                    break;
                case 39:
                    gc.concatTransform(new WCTransform(buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat()));
                    break;
                case 40:
                    WCPageBackBuffer buffer = (WCPageBackBuffer) gm.getRef(buf.getInt());
                    buffer.copyArea(buf.getInt(), buf.getInt(), buf.getInt(), buf.getInt(), buf.getInt(), buf.getInt());
                    break;
                case 41:
                    WCRenderQueue _rq = (WCRenderQueue) gm.getRef(buf.getInt());
                    _rq.decode(gc.getFontSmoothingType());
                    break;
                case 42:
                    gc.setTransform(new WCTransform(buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat()));
                    break;
                case 43:
                    gc.rotate(buf.getFloat());
                    break;
                case 44:
                    RenderMediaControls.paintControl(gc, buf.getInt(), buf.getInt(), buf.getInt(), buf.getInt(), buf.getInt());
                    break;
                case 45:
                    int n2 = buf.getInt();
                    float[] buffered = new float[n2 * 2];
                    buf.asFloatBuffer().get(buffered);
                    buf.position(buf.position() + (n2 * 4 * 2));
                    RenderMediaControls.paintTimeSliderTrack(gc, buf.getFloat(), buf.getFloat(), buffered, buf.getInt(), buf.getInt(), buf.getInt(), buf.getInt());
                    break;
                case 46:
                    RenderMediaControls.paintVolumeTrack(gc, buf.getFloat(), buf.getInt() != 0, buf.getInt(), buf.getInt(), buf.getInt(), buf.getInt());
                    break;
                case 47:
                    gc.fillRect(buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), null);
                    break;
                case 48:
                    gc.fillRoundedRect(buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), getColor(buf));
                    break;
                case 49:
                    gc.setFillGradient(getGradient(gc, buf));
                    break;
                case 50:
                    gc.setStrokeGradient(getGradient(gc, buf));
                    break;
                case 51:
                    gc.setLineDash(buf.getFloat(), getFloatArray(buf));
                    break;
                case 52:
                    gc.setLineCap(buf.getInt());
                    break;
                case 53:
                    gc.setLineJoin(buf.getInt());
                    break;
                case 54:
                    gc.setMiterLimit(buf.getFloat());
                    break;
                case 55:
                    gc.setTextMode(getBoolean(buf), getBoolean(buf), getBoolean(buf));
                    break;
                case 56:
                    gc.setPerspectiveTransform(new WCTransform(buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat()));
                    break;
            }
        }
    }

    private static void drawPattern(WCGraphicsContext gc, Object imgFrame, WCRectangle srcRect, WCTransform patternTransform, WCPoint phase, WCRectangle destRect) {
        WCImage img = WCImage.getImage(imgFrame);
        if (img != null) {
            try {
                gc.drawPattern(img, srcRect, patternTransform, phase, destRect);
            } catch (OutOfMemoryError error) {
                error.printStackTrace();
            }
        }
    }

    private static void drawImage(WCGraphicsContext gc, Object imgFrame, float dstx, float dsty, float dstw, float dsth, float srcx, float srcy, float srcw, float srch) {
        WCImage img = WCImage.getImage(imgFrame);
        if (img != null) {
            try {
                gc.drawImage(img, dstx, dsty, dstw, dsth, srcx, srcy, srcw, srch);
            } catch (OutOfMemoryError error) {
                error.printStackTrace();
            }
        }
    }

    private static boolean getBoolean(ByteBuffer buf) {
        return 0 != buf.getInt();
    }

    private static float[] getFloatArray(ByteBuffer buf) {
        float[] array = new float[buf.getInt()];
        for (int i2 = 0; i2 < array.length; i2++) {
            array[i2] = buf.getFloat();
        }
        return array;
    }

    private static WCPath getPath(WCGraphicsManager gm, ByteBuffer buf) {
        WCPath path = (WCPath) gm.getRef(buf.getInt());
        path.setWindingRule(buf.getInt());
        return path;
    }

    private static WCPoint getPoint(ByteBuffer buf) {
        return new WCPoint(buf.getFloat(), buf.getFloat());
    }

    private static WCRectangle getRectangle(ByteBuffer buf) {
        return new WCRectangle(buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat());
    }

    private static Color getColor(ByteBuffer buf) {
        return new Color(buf.getFloat(), buf.getFloat(), buf.getFloat(), buf.getFloat());
    }

    private static WCGradient getGradient(WCGraphicsContext gc, ByteBuffer buf) {
        WCGradient wCGradientCreateLinearGradient;
        WCPoint p1 = getPoint(buf);
        WCPoint p2 = getPoint(buf);
        if (getBoolean(buf)) {
            wCGradientCreateLinearGradient = gc.createRadialGradient(p1, buf.getFloat(), p2, buf.getFloat());
        } else {
            wCGradientCreateLinearGradient = gc.createLinearGradient(p1, p2);
        }
        WCGradient gradient = wCGradientCreateLinearGradient;
        boolean proportional = getBoolean(buf);
        int spreadMethod = buf.getInt();
        if (gradient != null) {
            gradient.setProportional(proportional);
            gradient.setSpreadMethod(spreadMethod);
        }
        int count = buf.getInt();
        for (int i2 = 0; i2 < count; i2++) {
            Color color = getColor(buf);
            float offset = buf.getFloat();
            if (gradient != null) {
                gradient.addStop(color, offset);
            }
        }
        return gradient;
    }
}
