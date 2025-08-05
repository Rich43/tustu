package com.sun.webkit.perf;

import com.sun.prism.paint.Color;
import com.sun.webkit.graphics.Ref;
import com.sun.webkit.graphics.RenderTheme;
import com.sun.webkit.graphics.ScrollBarTheme;
import com.sun.webkit.graphics.WCFont;
import com.sun.webkit.graphics.WCGradient;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCIcon;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCPath;
import com.sun.webkit.graphics.WCPoint;
import com.sun.webkit.graphics.WCRectangle;
import com.sun.webkit.graphics.WCTransform;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

/* loaded from: jfxrt.jar:com/sun/webkit/perf/WCGraphicsPerfLogger.class */
public final class WCGraphicsPerfLogger extends WCGraphicsContext {
    private static final Logger log = Logger.getLogger(WCGraphicsPerfLogger.class.getName());
    private static final PerfLogger logger = PerfLogger.getLogger(log);
    private final WCGraphicsContext gc;

    public WCGraphicsPerfLogger(WCGraphicsContext gc) {
        this.gc = gc;
    }

    public static synchronized boolean isEnabled() {
        return logger.isEnabled();
    }

    public static void log() {
        logger.log();
    }

    public static void reset() {
        logger.reset();
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public Object getPlatformGraphics() {
        return this.gc.getPlatformGraphics();
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public boolean isValid() {
        return this.gc.isValid();
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void drawString(WCFont f2, int[] glyphs, float[] advanceDXY, float x2, float y2) {
        logger.resumeCount("DRAWSTRING_GV");
        this.gc.drawString(f2, glyphs, advanceDXY, x2, y2);
        logger.suspendCount("DRAWSTRING_GV");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void strokeRect(float x2, float y2, float w2, float h2, float lengthWidth) {
        logger.resumeCount("STROKERECT_FFFFF");
        this.gc.strokeRect(x2, y2, w2, h2, lengthWidth);
        logger.suspendCount("STROKERECT_FFFFF");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void fillRect(float x2, float y2, float w2, float h2, Color color) {
        logger.resumeCount("FILLRECT_FFFFI");
        this.gc.fillRect(x2, y2, w2, h2, color);
        logger.suspendCount("FILLRECT_FFFFI");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void fillRoundedRect(float x2, float y2, float w2, float h2, float topLeftW, float topLeftH, float topRightW, float topRightH, float bottomLeftW, float bottomLeftH, float bottomRightW, float bottomRightH, Color color) {
        logger.resumeCount("FILL_ROUNDED_RECT");
        this.gc.fillRoundedRect(x2, y2, w2, h2, topLeftW, topLeftH, topRightW, topRightH, bottomLeftW, bottomLeftH, bottomRightW, bottomRightH, color);
        logger.suspendCount("FILL_ROUNDED_RECT");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void clearRect(float x2, float y2, float w2, float h2) {
        logger.resumeCount("CLEARRECT");
        this.gc.clearRect(x2, y2, w2, h2);
        logger.suspendCount("CLEARRECT");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setFillColor(Color color) {
        logger.resumeCount("SETFILLCOLOR");
        this.gc.setFillColor(color);
        logger.suspendCount("SETFILLCOLOR");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setFillGradient(WCGradient gradient) {
        logger.resumeCount("SET_FILL_GRADIENT");
        this.gc.setFillGradient(gradient);
        logger.suspendCount("SET_FILL_GRADIENT");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setTextMode(boolean fill, boolean stroke, boolean clip) {
        logger.resumeCount("SET_TEXT_MODE");
        this.gc.setTextMode(fill, stroke, clip);
        logger.suspendCount("SET_TEXT_MODE");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setFontSmoothingType(int fontSmoothingType) {
        logger.resumeCount("SET_FONT_SMOOTHING_TYPE");
        this.gc.setFontSmoothingType(fontSmoothingType);
        logger.suspendCount("SET_FONT_SMOOTHING_TYPE");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public int getFontSmoothingType() {
        logger.resumeCount("GET_FONT_SMOOTHING_TYPE");
        int n2 = this.gc.getFontSmoothingType();
        logger.suspendCount("GET_FONT_SMOOTHING_TYPE");
        return n2;
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setStrokeStyle(int style) {
        logger.resumeCount("SETSTROKESTYLE");
        this.gc.setStrokeStyle(style);
        logger.suspendCount("SETSTROKESTYLE");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setStrokeColor(Color color) {
        logger.resumeCount("SETSTROKECOLOR");
        this.gc.setStrokeColor(color);
        logger.suspendCount("SETSTROKECOLOR");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setStrokeWidth(float width) {
        logger.resumeCount("SETSTROKEWIDTH");
        this.gc.setStrokeWidth(width);
        logger.suspendCount("SETSTROKEWIDTH");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setStrokeGradient(WCGradient gradient) {
        logger.resumeCount("SET_STROKE_GRADIENT");
        this.gc.setStrokeGradient(gradient);
        logger.suspendCount("SET_STROKE_GRADIENT");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setLineDash(float offset, float... sizes) {
        logger.resumeCount("SET_LINE_DASH");
        this.gc.setLineDash(offset, sizes);
        logger.suspendCount("SET_LINE_DASH");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setLineCap(int lineCap) {
        logger.resumeCount("SET_LINE_CAP");
        this.gc.setLineCap(lineCap);
        logger.suspendCount("SET_LINE_CAP");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setLineJoin(int lineJoin) {
        logger.resumeCount("SET_LINE_JOIN");
        this.gc.setLineJoin(lineJoin);
        logger.suspendCount("SET_LINE_JOIN");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setMiterLimit(float miterLimit) {
        logger.resumeCount("SET_MITER_LIMIT");
        this.gc.setMiterLimit(miterLimit);
        logger.suspendCount("SET_MITER_LIMIT");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setShadow(float dx, float dy, float blur, Color color) {
        logger.resumeCount("SETSHADOW");
        this.gc.setShadow(dx, dy, blur, color);
        logger.suspendCount("SETSHADOW");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void drawPolygon(WCPath path, boolean shouldAntialias) {
        logger.resumeCount("DRAWPOLYGON");
        this.gc.drawPolygon(path, shouldAntialias);
        logger.suspendCount("DRAWPOLYGON");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void drawLine(int x0, int y0, int x1, int y1) {
        logger.resumeCount("DRAWLINE");
        this.gc.drawLine(x0, y0, x1, y1);
        logger.suspendCount("DRAWLINE");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void drawImage(WCImage img, float dstx, float dsty, float dstw, float dsth, float srcx, float srcy, float srcw, float srch) {
        logger.resumeCount("DRAWIMAGE");
        this.gc.drawImage(img, dstx, dsty, dstw, dsth, srcx, srcy, srcw, srch);
        logger.suspendCount("DRAWIMAGE");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void drawIcon(WCIcon icon, int x2, int y2) {
        logger.resumeCount("DRAWICON");
        this.gc.drawIcon(icon, x2, y2);
        logger.suspendCount("DRAWICON");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void drawPattern(WCImage texture, WCRectangle srcRect, WCTransform patternTransform, WCPoint phase, WCRectangle destRect) {
        logger.resumeCount("DRAWPATTERN");
        this.gc.drawPattern(texture, srcRect, patternTransform, phase, destRect);
        logger.suspendCount("DRAWPATTERN");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void translate(float x2, float y2) {
        logger.resumeCount("TRANSLATE");
        this.gc.translate(x2, y2);
        logger.suspendCount("TRANSLATE");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void scale(float scaleX, float scaleY) {
        logger.resumeCount("SCALE");
        this.gc.scale(scaleX, scaleY);
        logger.suspendCount("SCALE");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void rotate(float radians) {
        logger.resumeCount("ROTATE");
        this.gc.rotate(radians);
        logger.suspendCount("ROTATE");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void saveState() {
        logger.resumeCount("SAVESTATE");
        this.gc.saveState();
        logger.suspendCount("SAVESTATE");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void restoreState() {
        logger.resumeCount("RESTORESTATE");
        this.gc.restoreState();
        logger.suspendCount("RESTORESTATE");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setClip(WCPath path, boolean isOut) {
        logger.resumeCount("CLIP_PATH");
        this.gc.setClip(path, isOut);
        logger.suspendCount("CLIP_PATH");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setClip(WCRectangle clip) {
        logger.resumeCount("SETCLIP_R");
        this.gc.setClip(clip);
        logger.suspendCount("SETCLIP_R");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setClip(int cx, int cy, int cw, int ch) {
        logger.resumeCount("SETCLIP_IIII");
        this.gc.setClip(cx, cy, cw, ch);
        logger.suspendCount("SETCLIP_IIII");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public WCRectangle getClip() {
        logger.resumeCount("SETCLIP_IIII");
        WCRectangle r2 = this.gc.getClip();
        logger.suspendCount("SETCLIP_IIII");
        return r2;
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void drawRect(int x2, int y2, int w2, int h2) {
        logger.resumeCount("DRAWRECT");
        this.gc.drawRect(x2, y2, w2, h2);
        logger.suspendCount("DRAWRECT");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setComposite(int composite) {
        logger.resumeCount("SETCOMPOSITE");
        this.gc.setComposite(composite);
        logger.suspendCount("SETCOMPOSITE");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void strokeArc(int x2, int y2, int w2, int h2, int startAngle, int angleSpan) {
        logger.resumeCount("STROKEARC");
        this.gc.strokeArc(x2, y2, w2, h2, startAngle, angleSpan);
        logger.suspendCount("STROKEARC");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void drawEllipse(int x2, int y2, int w2, int h2) {
        logger.resumeCount("DRAWELLIPSE");
        this.gc.drawEllipse(x2, y2, w2, h2);
        logger.suspendCount("DRAWELLIPSE");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void drawFocusRing(int x2, int y2, int w2, int h2, Color color) {
        logger.resumeCount("DRAWFOCUSRING");
        this.gc.drawFocusRing(x2, y2, w2, h2, color);
        logger.suspendCount("DRAWFOCUSRING");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setAlpha(float alpha) {
        logger.resumeCount("SETALPHA");
        this.gc.setAlpha(alpha);
        logger.suspendCount("SETALPHA");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public float getAlpha() {
        logger.resumeCount("GETALPHA");
        float a2 = this.gc.getAlpha();
        logger.suspendCount("GETALPHA");
        return a2;
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void beginTransparencyLayer(float opacity) {
        logger.resumeCount("BEGINTRANSPARENCYLAYER");
        this.gc.beginTransparencyLayer(opacity);
        logger.suspendCount("BEGINTRANSPARENCYLAYER");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void endTransparencyLayer() {
        logger.resumeCount("ENDTRANSPARENCYLAYER");
        this.gc.endTransparencyLayer();
        logger.suspendCount("ENDTRANSPARENCYLAYER");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void drawString(WCFont f2, String str, boolean rtl, int from, int to, float x2, float y2) {
        logger.resumeCount("DRAWSTRING");
        this.gc.drawString(f2, str, rtl, from, to, x2, y2);
        logger.suspendCount("DRAWSTRING");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void strokePath(WCPath path) {
        logger.resumeCount("STROKE_PATH");
        this.gc.strokePath(path);
        logger.suspendCount("STROKE_PATH");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void fillPath(WCPath path) {
        logger.resumeCount("FILL_PATH");
        this.gc.fillPath(path);
        logger.suspendCount("FILL_PATH");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public WCImage getImage() {
        logger.resumeCount("GETIMAGE");
        WCImage res = this.gc.getImage();
        logger.suspendCount("GETIMAGE");
        return res;
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void drawWidget(RenderTheme theme, Ref widget, int x2, int y2) {
        logger.resumeCount("DRAWWIDGET");
        this.gc.drawWidget(theme, widget, x2, y2);
        logger.suspendCount("DRAWWIDGET");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void drawScrollbar(ScrollBarTheme theme, Ref widget, int x2, int y2, int pressedPart, int hoveredPart) {
        logger.resumeCount("DRAWSCROLLBAR");
        this.gc.drawScrollbar(theme, widget, x2, y2, pressedPart, hoveredPart);
        logger.suspendCount("DRAWSCROLLBAR");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void dispose() {
        logger.resumeCount("DISPOSE");
        this.gc.dispose();
        logger.suspendCount("DISPOSE");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void flush() {
        logger.resumeCount("FLUSH");
        this.gc.flush();
        logger.suspendCount("FLUSH");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setPerspectiveTransform(WCTransform t2) {
        logger.resumeCount("SETPERSPECTIVETRANSFORM");
        this.gc.setPerspectiveTransform(t2);
        logger.suspendCount("SETPERSPECTIVETRANSFORM");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void setTransform(WCTransform t2) {
        logger.resumeCount("SETTRANSFORM");
        this.gc.setTransform(t2);
        logger.suspendCount("SETTRANSFORM");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public WCTransform getTransform() {
        logger.resumeCount("GETTRANSFORM");
        WCTransform t2 = this.gc.getTransform();
        logger.suspendCount("GETTRANSFORM");
        return t2;
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void concatTransform(WCTransform t2) {
        logger.resumeCount("CONCATTRANSFORM");
        this.gc.concatTransform(t2);
        logger.suspendCount("CONCATTRANSFORM");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public void drawBitmapImage(ByteBuffer image, int x2, int y2, int w2, int h2) {
        logger.resumeCount("DRAWBITMAPIMAGE");
        this.gc.drawBitmapImage(image, x2, y2, w2, h2);
        logger.suspendCount("DRAWBITMAPIMAGE");
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public WCGradient createLinearGradient(WCPoint p1, WCPoint p2) {
        logger.resumeCount("CREATE_LINEAR_GRADIENT");
        WCGradient gradient = this.gc.createLinearGradient(p1, p2);
        logger.suspendCount("CREATE_LINEAR_GRADIENT");
        return gradient;
    }

    @Override // com.sun.webkit.graphics.WCGraphicsContext
    public WCGradient createRadialGradient(WCPoint p1, float r1, WCPoint p2, float r2) {
        logger.resumeCount("CREATE_RADIAL_GRADIENT");
        WCGradient gradient = this.gc.createRadialGradient(p1, r1, p2, r2);
        logger.suspendCount("CREATE_RADIAL_GRADIENT");
        return gradient;
    }
}
