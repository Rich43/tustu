package javax.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageObserver;
import java.io.PrintStream;
import java.text.AttributedCharacterIterator;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:javax/swing/DebugGraphics.class */
public class DebugGraphics extends Graphics {
    Graphics graphics;
    Image buffer;
    int debugOptions;
    int graphicsID;
    int xOffset;
    int yOffset;
    private static int graphicsCount = 0;
    private static ImageIcon imageLoadingIcon = new ImageIcon();
    public static final int LOG_OPTION = 1;
    public static final int FLASH_OPTION = 2;
    public static final int BUFFERED_OPTION = 4;
    public static final int NONE_OPTION = -1;
    private static final Class debugGraphicsInfoKey;

    static {
        JComponent.DEBUG_GRAPHICS_LOADED = true;
        debugGraphicsInfoKey = DebugGraphicsInfo.class;
    }

    public DebugGraphics() {
        int i2 = graphicsCount;
        graphicsCount = i2 + 1;
        this.graphicsID = i2;
        this.buffer = null;
        this.yOffset = 0;
        this.xOffset = 0;
    }

    public DebugGraphics(Graphics graphics, JComponent jComponent) {
        this(graphics);
        setDebugOptions(jComponent.shouldDebugGraphics());
    }

    public DebugGraphics(Graphics graphics) {
        this();
        this.graphics = graphics;
    }

    @Override // java.awt.Graphics
    public Graphics create() {
        DebugGraphics debugGraphics = new DebugGraphics();
        debugGraphics.graphics = this.graphics.create();
        debugGraphics.debugOptions = this.debugOptions;
        debugGraphics.buffer = this.buffer;
        return debugGraphics;
    }

    @Override // java.awt.Graphics
    public Graphics create(int i2, int i3, int i4, int i5) {
        DebugGraphics debugGraphics = new DebugGraphics();
        debugGraphics.graphics = this.graphics.create(i2, i3, i4, i5);
        debugGraphics.debugOptions = this.debugOptions;
        debugGraphics.buffer = this.buffer;
        debugGraphics.xOffset = this.xOffset + i2;
        debugGraphics.yOffset = this.yOffset + i3;
        return debugGraphics;
    }

    public static void setFlashColor(Color color) {
        info().flashColor = color;
    }

    public static Color flashColor() {
        return info().flashColor;
    }

    public static void setFlashTime(int i2) {
        info().flashTime = i2;
    }

    public static int flashTime() {
        return info().flashTime;
    }

    public static void setFlashCount(int i2) {
        info().flashCount = i2;
    }

    public static int flashCount() {
        return info().flashCount;
    }

    public static void setLogStream(PrintStream printStream) {
        info().stream = printStream;
    }

    public static PrintStream logStream() {
        return info().stream;
    }

    @Override // java.awt.Graphics
    public void setFont(Font font) {
        if (debugLog()) {
            info().log(toShortString() + " Setting font: " + ((Object) font));
        }
        this.graphics.setFont(font);
    }

    @Override // java.awt.Graphics
    public Font getFont() {
        return this.graphics.getFont();
    }

    @Override // java.awt.Graphics
    public void setColor(Color color) {
        if (debugLog()) {
            info().log(toShortString() + " Setting color: " + ((Object) color));
        }
        this.graphics.setColor(color);
    }

    @Override // java.awt.Graphics
    public Color getColor() {
        return this.graphics.getColor();
    }

    @Override // java.awt.Graphics
    public FontMetrics getFontMetrics() {
        return this.graphics.getFontMetrics();
    }

    @Override // java.awt.Graphics
    public FontMetrics getFontMetrics(Font font) {
        return this.graphics.getFontMetrics(font);
    }

    @Override // java.awt.Graphics
    public void translate(int i2, int i3) {
        if (debugLog()) {
            info().log(toShortString() + " Translating by: " + ((Object) new Point(i2, i3)));
        }
        this.xOffset += i2;
        this.yOffset += i3;
        this.graphics.translate(i2, i3);
    }

    @Override // java.awt.Graphics
    public void setPaintMode() {
        if (debugLog()) {
            info().log(toShortString() + " Setting paint mode");
        }
        this.graphics.setPaintMode();
    }

    @Override // java.awt.Graphics
    public void setXORMode(Color color) {
        if (debugLog()) {
            info().log(toShortString() + " Setting XOR mode: " + ((Object) color));
        }
        this.graphics.setXORMode(color);
    }

    @Override // java.awt.Graphics
    public Rectangle getClipBounds() {
        return this.graphics.getClipBounds();
    }

    @Override // java.awt.Graphics
    public void clipRect(int i2, int i3, int i4, int i5) {
        this.graphics.clipRect(i2, i3, i4, i5);
        if (debugLog()) {
            info().log(toShortString() + " Setting clipRect: " + ((Object) new Rectangle(i2, i3, i4, i5)) + " New clipRect: " + ((Object) this.graphics.getClip()));
        }
    }

    @Override // java.awt.Graphics
    public void setClip(int i2, int i3, int i4, int i5) {
        this.graphics.setClip(i2, i3, i4, i5);
        if (debugLog()) {
            info().log(toShortString() + " Setting new clipRect: " + ((Object) this.graphics.getClip()));
        }
    }

    @Override // java.awt.Graphics
    public Shape getClip() {
        return this.graphics.getClip();
    }

    @Override // java.awt.Graphics
    public void setClip(Shape shape) {
        this.graphics.setClip(shape);
        if (debugLog()) {
            info().log(toShortString() + " Setting new clipRect: " + ((Object) this.graphics.getClip()));
        }
    }

    @Override // java.awt.Graphics
    public void drawRect(int i2, int i3, int i4, int i5) {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        if (debugLog()) {
            info().log(toShortString() + " Drawing rect: " + ((Object) new Rectangle(i2, i3, i4, i5)));
        }
        if (isDrawingBuffer()) {
            if (debugBuffered()) {
                Graphics graphicsDebugGraphics = debugGraphics();
                graphicsDebugGraphics.drawRect(i2, i3, i4, i5);
                graphicsDebugGraphics.dispose();
            }
        } else if (debugFlash()) {
            Color color = getColor();
            int i6 = (debugGraphicsInfoInfo.flashCount * 2) - 1;
            for (int i7 = 0; i7 < i6; i7++) {
                this.graphics.setColor(i7 % 2 == 0 ? debugGraphicsInfoInfo.flashColor : color);
                this.graphics.drawRect(i2, i3, i4, i5);
                Toolkit.getDefaultToolkit().sync();
                sleep(debugGraphicsInfoInfo.flashTime);
            }
            this.graphics.setColor(color);
        }
        this.graphics.drawRect(i2, i3, i4, i5);
    }

    @Override // java.awt.Graphics
    public void fillRect(int i2, int i3, int i4, int i5) {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        if (debugLog()) {
            info().log(toShortString() + " Filling rect: " + ((Object) new Rectangle(i2, i3, i4, i5)));
        }
        if (isDrawingBuffer()) {
            if (debugBuffered()) {
                Graphics graphicsDebugGraphics = debugGraphics();
                graphicsDebugGraphics.fillRect(i2, i3, i4, i5);
                graphicsDebugGraphics.dispose();
            }
        } else if (debugFlash()) {
            Color color = getColor();
            int i6 = (debugGraphicsInfoInfo.flashCount * 2) - 1;
            for (int i7 = 0; i7 < i6; i7++) {
                this.graphics.setColor(i7 % 2 == 0 ? debugGraphicsInfoInfo.flashColor : color);
                this.graphics.fillRect(i2, i3, i4, i5);
                Toolkit.getDefaultToolkit().sync();
                sleep(debugGraphicsInfoInfo.flashTime);
            }
            this.graphics.setColor(color);
        }
        this.graphics.fillRect(i2, i3, i4, i5);
    }

    @Override // java.awt.Graphics
    public void clearRect(int i2, int i3, int i4, int i5) {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        if (debugLog()) {
            info().log(toShortString() + " Clearing rect: " + ((Object) new Rectangle(i2, i3, i4, i5)));
        }
        if (isDrawingBuffer()) {
            if (debugBuffered()) {
                Graphics graphicsDebugGraphics = debugGraphics();
                graphicsDebugGraphics.clearRect(i2, i3, i4, i5);
                graphicsDebugGraphics.dispose();
            }
        } else if (debugFlash()) {
            Color color = getColor();
            int i6 = (debugGraphicsInfoInfo.flashCount * 2) - 1;
            for (int i7 = 0; i7 < i6; i7++) {
                this.graphics.setColor(i7 % 2 == 0 ? debugGraphicsInfoInfo.flashColor : color);
                this.graphics.clearRect(i2, i3, i4, i5);
                Toolkit.getDefaultToolkit().sync();
                sleep(debugGraphicsInfoInfo.flashTime);
            }
            this.graphics.setColor(color);
        }
        this.graphics.clearRect(i2, i3, i4, i5);
    }

    @Override // java.awt.Graphics
    public void drawRoundRect(int i2, int i3, int i4, int i5, int i6, int i7) {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        if (debugLog()) {
            info().log(toShortString() + " Drawing round rect: " + ((Object) new Rectangle(i2, i3, i4, i5)) + " arcWidth: " + i6 + " archHeight: " + i7);
        }
        if (isDrawingBuffer()) {
            if (debugBuffered()) {
                Graphics graphicsDebugGraphics = debugGraphics();
                graphicsDebugGraphics.drawRoundRect(i2, i3, i4, i5, i6, i7);
                graphicsDebugGraphics.dispose();
            }
        } else if (debugFlash()) {
            Color color = getColor();
            int i8 = (debugGraphicsInfoInfo.flashCount * 2) - 1;
            for (int i9 = 0; i9 < i8; i9++) {
                this.graphics.setColor(i9 % 2 == 0 ? debugGraphicsInfoInfo.flashColor : color);
                this.graphics.drawRoundRect(i2, i3, i4, i5, i6, i7);
                Toolkit.getDefaultToolkit().sync();
                sleep(debugGraphicsInfoInfo.flashTime);
            }
            this.graphics.setColor(color);
        }
        this.graphics.drawRoundRect(i2, i3, i4, i5, i6, i7);
    }

    @Override // java.awt.Graphics
    public void fillRoundRect(int i2, int i3, int i4, int i5, int i6, int i7) {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        if (debugLog()) {
            info().log(toShortString() + " Filling round rect: " + ((Object) new Rectangle(i2, i3, i4, i5)) + " arcWidth: " + i6 + " archHeight: " + i7);
        }
        if (isDrawingBuffer()) {
            if (debugBuffered()) {
                Graphics graphicsDebugGraphics = debugGraphics();
                graphicsDebugGraphics.fillRoundRect(i2, i3, i4, i5, i6, i7);
                graphicsDebugGraphics.dispose();
            }
        } else if (debugFlash()) {
            Color color = getColor();
            int i8 = (debugGraphicsInfoInfo.flashCount * 2) - 1;
            for (int i9 = 0; i9 < i8; i9++) {
                this.graphics.setColor(i9 % 2 == 0 ? debugGraphicsInfoInfo.flashColor : color);
                this.graphics.fillRoundRect(i2, i3, i4, i5, i6, i7);
                Toolkit.getDefaultToolkit().sync();
                sleep(debugGraphicsInfoInfo.flashTime);
            }
            this.graphics.setColor(color);
        }
        this.graphics.fillRoundRect(i2, i3, i4, i5, i6, i7);
    }

    @Override // java.awt.Graphics
    public void drawLine(int i2, int i3, int i4, int i5) {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        if (debugLog()) {
            info().log(toShortString() + " Drawing line: from " + pointToString(i2, i3) + " to " + pointToString(i4, i5));
        }
        if (isDrawingBuffer()) {
            if (debugBuffered()) {
                Graphics graphicsDebugGraphics = debugGraphics();
                graphicsDebugGraphics.drawLine(i2, i3, i4, i5);
                graphicsDebugGraphics.dispose();
            }
        } else if (debugFlash()) {
            Color color = getColor();
            int i6 = (debugGraphicsInfoInfo.flashCount * 2) - 1;
            for (int i7 = 0; i7 < i6; i7++) {
                this.graphics.setColor(i7 % 2 == 0 ? debugGraphicsInfoInfo.flashColor : color);
                this.graphics.drawLine(i2, i3, i4, i5);
                Toolkit.getDefaultToolkit().sync();
                sleep(debugGraphicsInfoInfo.flashTime);
            }
            this.graphics.setColor(color);
        }
        this.graphics.drawLine(i2, i3, i4, i5);
    }

    @Override // java.awt.Graphics
    public void draw3DRect(int i2, int i3, int i4, int i5, boolean z2) {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        if (debugLog()) {
            info().log(toShortString() + " Drawing 3D rect: " + ((Object) new Rectangle(i2, i3, i4, i5)) + " Raised bezel: " + z2);
        }
        if (isDrawingBuffer()) {
            if (debugBuffered()) {
                Graphics graphicsDebugGraphics = debugGraphics();
                graphicsDebugGraphics.draw3DRect(i2, i3, i4, i5, z2);
                graphicsDebugGraphics.dispose();
            }
        } else if (debugFlash()) {
            Color color = getColor();
            int i6 = (debugGraphicsInfoInfo.flashCount * 2) - 1;
            for (int i7 = 0; i7 < i6; i7++) {
                this.graphics.setColor(i7 % 2 == 0 ? debugGraphicsInfoInfo.flashColor : color);
                this.graphics.draw3DRect(i2, i3, i4, i5, z2);
                Toolkit.getDefaultToolkit().sync();
                sleep(debugGraphicsInfoInfo.flashTime);
            }
            this.graphics.setColor(color);
        }
        this.graphics.draw3DRect(i2, i3, i4, i5, z2);
    }

    @Override // java.awt.Graphics
    public void fill3DRect(int i2, int i3, int i4, int i5, boolean z2) {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        if (debugLog()) {
            info().log(toShortString() + " Filling 3D rect: " + ((Object) new Rectangle(i2, i3, i4, i5)) + " Raised bezel: " + z2);
        }
        if (isDrawingBuffer()) {
            if (debugBuffered()) {
                Graphics graphicsDebugGraphics = debugGraphics();
                graphicsDebugGraphics.fill3DRect(i2, i3, i4, i5, z2);
                graphicsDebugGraphics.dispose();
            }
        } else if (debugFlash()) {
            Color color = getColor();
            int i6 = (debugGraphicsInfoInfo.flashCount * 2) - 1;
            for (int i7 = 0; i7 < i6; i7++) {
                this.graphics.setColor(i7 % 2 == 0 ? debugGraphicsInfoInfo.flashColor : color);
                this.graphics.fill3DRect(i2, i3, i4, i5, z2);
                Toolkit.getDefaultToolkit().sync();
                sleep(debugGraphicsInfoInfo.flashTime);
            }
            this.graphics.setColor(color);
        }
        this.graphics.fill3DRect(i2, i3, i4, i5, z2);
    }

    @Override // java.awt.Graphics
    public void drawOval(int i2, int i3, int i4, int i5) {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        if (debugLog()) {
            info().log(toShortString() + " Drawing oval: " + ((Object) new Rectangle(i2, i3, i4, i5)));
        }
        if (isDrawingBuffer()) {
            if (debugBuffered()) {
                Graphics graphicsDebugGraphics = debugGraphics();
                graphicsDebugGraphics.drawOval(i2, i3, i4, i5);
                graphicsDebugGraphics.dispose();
            }
        } else if (debugFlash()) {
            Color color = getColor();
            int i6 = (debugGraphicsInfoInfo.flashCount * 2) - 1;
            for (int i7 = 0; i7 < i6; i7++) {
                this.graphics.setColor(i7 % 2 == 0 ? debugGraphicsInfoInfo.flashColor : color);
                this.graphics.drawOval(i2, i3, i4, i5);
                Toolkit.getDefaultToolkit().sync();
                sleep(debugGraphicsInfoInfo.flashTime);
            }
            this.graphics.setColor(color);
        }
        this.graphics.drawOval(i2, i3, i4, i5);
    }

    @Override // java.awt.Graphics
    public void fillOval(int i2, int i3, int i4, int i5) {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        if (debugLog()) {
            info().log(toShortString() + " Filling oval: " + ((Object) new Rectangle(i2, i3, i4, i5)));
        }
        if (isDrawingBuffer()) {
            if (debugBuffered()) {
                Graphics graphicsDebugGraphics = debugGraphics();
                graphicsDebugGraphics.fillOval(i2, i3, i4, i5);
                graphicsDebugGraphics.dispose();
            }
        } else if (debugFlash()) {
            Color color = getColor();
            int i6 = (debugGraphicsInfoInfo.flashCount * 2) - 1;
            for (int i7 = 0; i7 < i6; i7++) {
                this.graphics.setColor(i7 % 2 == 0 ? debugGraphicsInfoInfo.flashColor : color);
                this.graphics.fillOval(i2, i3, i4, i5);
                Toolkit.getDefaultToolkit().sync();
                sleep(debugGraphicsInfoInfo.flashTime);
            }
            this.graphics.setColor(color);
        }
        this.graphics.fillOval(i2, i3, i4, i5);
    }

    @Override // java.awt.Graphics
    public void drawArc(int i2, int i3, int i4, int i5, int i6, int i7) {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        if (debugLog()) {
            info().log(toShortString() + " Drawing arc: " + ((Object) new Rectangle(i2, i3, i4, i5)) + " startAngle: " + i6 + " arcAngle: " + i7);
        }
        if (isDrawingBuffer()) {
            if (debugBuffered()) {
                Graphics graphicsDebugGraphics = debugGraphics();
                graphicsDebugGraphics.drawArc(i2, i3, i4, i5, i6, i7);
                graphicsDebugGraphics.dispose();
            }
        } else if (debugFlash()) {
            Color color = getColor();
            int i8 = (debugGraphicsInfoInfo.flashCount * 2) - 1;
            for (int i9 = 0; i9 < i8; i9++) {
                this.graphics.setColor(i9 % 2 == 0 ? debugGraphicsInfoInfo.flashColor : color);
                this.graphics.drawArc(i2, i3, i4, i5, i6, i7);
                Toolkit.getDefaultToolkit().sync();
                sleep(debugGraphicsInfoInfo.flashTime);
            }
            this.graphics.setColor(color);
        }
        this.graphics.drawArc(i2, i3, i4, i5, i6, i7);
    }

    @Override // java.awt.Graphics
    public void fillArc(int i2, int i3, int i4, int i5, int i6, int i7) {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        if (debugLog()) {
            info().log(toShortString() + " Filling arc: " + ((Object) new Rectangle(i2, i3, i4, i5)) + " startAngle: " + i6 + " arcAngle: " + i7);
        }
        if (isDrawingBuffer()) {
            if (debugBuffered()) {
                Graphics graphicsDebugGraphics = debugGraphics();
                graphicsDebugGraphics.fillArc(i2, i3, i4, i5, i6, i7);
                graphicsDebugGraphics.dispose();
            }
        } else if (debugFlash()) {
            Color color = getColor();
            int i8 = (debugGraphicsInfoInfo.flashCount * 2) - 1;
            for (int i9 = 0; i9 < i8; i9++) {
                this.graphics.setColor(i9 % 2 == 0 ? debugGraphicsInfoInfo.flashColor : color);
                this.graphics.fillArc(i2, i3, i4, i5, i6, i7);
                Toolkit.getDefaultToolkit().sync();
                sleep(debugGraphicsInfoInfo.flashTime);
            }
            this.graphics.setColor(color);
        }
        this.graphics.fillArc(i2, i3, i4, i5, i6, i7);
    }

    @Override // java.awt.Graphics
    public void drawPolyline(int[] iArr, int[] iArr2, int i2) {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        if (debugLog()) {
            info().log(toShortString() + " Drawing polyline:  nPoints: " + i2 + " X's: " + ((Object) iArr) + " Y's: " + ((Object) iArr2));
        }
        if (isDrawingBuffer()) {
            if (debugBuffered()) {
                Graphics graphicsDebugGraphics = debugGraphics();
                graphicsDebugGraphics.drawPolyline(iArr, iArr2, i2);
                graphicsDebugGraphics.dispose();
            }
        } else if (debugFlash()) {
            Color color = getColor();
            int i3 = (debugGraphicsInfoInfo.flashCount * 2) - 1;
            for (int i4 = 0; i4 < i3; i4++) {
                this.graphics.setColor(i4 % 2 == 0 ? debugGraphicsInfoInfo.flashColor : color);
                this.graphics.drawPolyline(iArr, iArr2, i2);
                Toolkit.getDefaultToolkit().sync();
                sleep(debugGraphicsInfoInfo.flashTime);
            }
            this.graphics.setColor(color);
        }
        this.graphics.drawPolyline(iArr, iArr2, i2);
    }

    @Override // java.awt.Graphics
    public void drawPolygon(int[] iArr, int[] iArr2, int i2) {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        if (debugLog()) {
            info().log(toShortString() + " Drawing polygon:  nPoints: " + i2 + " X's: " + ((Object) iArr) + " Y's: " + ((Object) iArr2));
        }
        if (isDrawingBuffer()) {
            if (debugBuffered()) {
                Graphics graphicsDebugGraphics = debugGraphics();
                graphicsDebugGraphics.drawPolygon(iArr, iArr2, i2);
                graphicsDebugGraphics.dispose();
            }
        } else if (debugFlash()) {
            Color color = getColor();
            int i3 = (debugGraphicsInfoInfo.flashCount * 2) - 1;
            for (int i4 = 0; i4 < i3; i4++) {
                this.graphics.setColor(i4 % 2 == 0 ? debugGraphicsInfoInfo.flashColor : color);
                this.graphics.drawPolygon(iArr, iArr2, i2);
                Toolkit.getDefaultToolkit().sync();
                sleep(debugGraphicsInfoInfo.flashTime);
            }
            this.graphics.setColor(color);
        }
        this.graphics.drawPolygon(iArr, iArr2, i2);
    }

    @Override // java.awt.Graphics
    public void fillPolygon(int[] iArr, int[] iArr2, int i2) {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        if (debugLog()) {
            info().log(toShortString() + " Filling polygon:  nPoints: " + i2 + " X's: " + ((Object) iArr) + " Y's: " + ((Object) iArr2));
        }
        if (isDrawingBuffer()) {
            if (debugBuffered()) {
                Graphics graphicsDebugGraphics = debugGraphics();
                graphicsDebugGraphics.fillPolygon(iArr, iArr2, i2);
                graphicsDebugGraphics.dispose();
            }
        } else if (debugFlash()) {
            Color color = getColor();
            int i3 = (debugGraphicsInfoInfo.flashCount * 2) - 1;
            for (int i4 = 0; i4 < i3; i4++) {
                this.graphics.setColor(i4 % 2 == 0 ? debugGraphicsInfoInfo.flashColor : color);
                this.graphics.fillPolygon(iArr, iArr2, i2);
                Toolkit.getDefaultToolkit().sync();
                sleep(debugGraphicsInfoInfo.flashTime);
            }
            this.graphics.setColor(color);
        }
        this.graphics.fillPolygon(iArr, iArr2, i2);
    }

    @Override // java.awt.Graphics
    public void drawString(String str, int i2, int i3) {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        if (debugLog()) {
            info().log(toShortString() + " Drawing string: \"" + str + "\" at: " + ((Object) new Point(i2, i3)));
        }
        if (isDrawingBuffer()) {
            if (debugBuffered()) {
                Graphics graphicsDebugGraphics = debugGraphics();
                graphicsDebugGraphics.drawString(str, i2, i3);
                graphicsDebugGraphics.dispose();
            }
        } else if (debugFlash()) {
            Color color = getColor();
            int i4 = (debugGraphicsInfoInfo.flashCount * 2) - 1;
            for (int i5 = 0; i5 < i4; i5++) {
                this.graphics.setColor(i5 % 2 == 0 ? debugGraphicsInfoInfo.flashColor : color);
                this.graphics.drawString(str, i2, i3);
                Toolkit.getDefaultToolkit().sync();
                sleep(debugGraphicsInfoInfo.flashTime);
            }
            this.graphics.setColor(color);
        }
        this.graphics.drawString(str, i2, i3);
    }

    @Override // java.awt.Graphics
    public void drawString(AttributedCharacterIterator attributedCharacterIterator, int i2, int i3) {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        if (debugLog()) {
            info().log(toShortString() + " Drawing text: \"" + ((Object) attributedCharacterIterator) + "\" at: " + ((Object) new Point(i2, i3)));
        }
        if (isDrawingBuffer()) {
            if (debugBuffered()) {
                Graphics graphicsDebugGraphics = debugGraphics();
                graphicsDebugGraphics.drawString(attributedCharacterIterator, i2, i3);
                graphicsDebugGraphics.dispose();
            }
        } else if (debugFlash()) {
            Color color = getColor();
            int i4 = (debugGraphicsInfoInfo.flashCount * 2) - 1;
            for (int i5 = 0; i5 < i4; i5++) {
                this.graphics.setColor(i5 % 2 == 0 ? debugGraphicsInfoInfo.flashColor : color);
                this.graphics.drawString(attributedCharacterIterator, i2, i3);
                Toolkit.getDefaultToolkit().sync();
                sleep(debugGraphicsInfoInfo.flashTime);
            }
            this.graphics.setColor(color);
        }
        this.graphics.drawString(attributedCharacterIterator, i2, i3);
    }

    @Override // java.awt.Graphics
    public void drawBytes(byte[] bArr, int i2, int i3, int i4, int i5) {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        this.graphics.getFont();
        if (debugLog()) {
            info().log(toShortString() + " Drawing bytes at: " + ((Object) new Point(i4, i5)));
        }
        if (isDrawingBuffer()) {
            if (debugBuffered()) {
                Graphics graphicsDebugGraphics = debugGraphics();
                graphicsDebugGraphics.drawBytes(bArr, i2, i3, i4, i5);
                graphicsDebugGraphics.dispose();
            }
        } else if (debugFlash()) {
            Color color = getColor();
            int i6 = (debugGraphicsInfoInfo.flashCount * 2) - 1;
            for (int i7 = 0; i7 < i6; i7++) {
                this.graphics.setColor(i7 % 2 == 0 ? debugGraphicsInfoInfo.flashColor : color);
                this.graphics.drawBytes(bArr, i2, i3, i4, i5);
                Toolkit.getDefaultToolkit().sync();
                sleep(debugGraphicsInfoInfo.flashTime);
            }
            this.graphics.setColor(color);
        }
        this.graphics.drawBytes(bArr, i2, i3, i4, i5);
    }

    @Override // java.awt.Graphics
    public void drawChars(char[] cArr, int i2, int i3, int i4, int i5) {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        this.graphics.getFont();
        if (debugLog()) {
            info().log(toShortString() + " Drawing chars at " + ((Object) new Point(i4, i5)));
        }
        if (isDrawingBuffer()) {
            if (debugBuffered()) {
                Graphics graphicsDebugGraphics = debugGraphics();
                graphicsDebugGraphics.drawChars(cArr, i2, i3, i4, i5);
                graphicsDebugGraphics.dispose();
            }
        } else if (debugFlash()) {
            Color color = getColor();
            int i6 = (debugGraphicsInfoInfo.flashCount * 2) - 1;
            for (int i7 = 0; i7 < i6; i7++) {
                this.graphics.setColor(i7 % 2 == 0 ? debugGraphicsInfoInfo.flashColor : color);
                this.graphics.drawChars(cArr, i2, i3, i4, i5);
                Toolkit.getDefaultToolkit().sync();
                sleep(debugGraphicsInfoInfo.flashTime);
            }
            this.graphics.setColor(color);
        }
        this.graphics.drawChars(cArr, i2, i3, i4, i5);
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, ImageObserver imageObserver) {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        if (debugLog()) {
            debugGraphicsInfoInfo.log(toShortString() + " Drawing image: " + ((Object) image) + " at: " + ((Object) new Point(i2, i3)));
        }
        if (isDrawingBuffer()) {
            if (debugBuffered()) {
                Graphics graphicsDebugGraphics = debugGraphics();
                graphicsDebugGraphics.drawImage(image, i2, i3, imageObserver);
                graphicsDebugGraphics.dispose();
            }
        } else if (debugFlash()) {
            int i4 = (debugGraphicsInfoInfo.flashCount * 2) - 1;
            Image imageCreateImage = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), new DebugGraphicsFilter(debugGraphicsInfoInfo.flashColor)));
            DebugGraphicsObserver debugGraphicsObserver = new DebugGraphicsObserver();
            for (int i5 = 0; i5 < i4; i5++) {
                Image image2 = i5 % 2 == 0 ? imageCreateImage : image;
                loadImage(image2);
                this.graphics.drawImage(image2, i2, i3, debugGraphicsObserver);
                Toolkit.getDefaultToolkit().sync();
                sleep(debugGraphicsInfoInfo.flashTime);
            }
        }
        return this.graphics.drawImage(image, i2, i3, imageObserver);
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, int i4, int i5, ImageObserver imageObserver) {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        if (debugLog()) {
            debugGraphicsInfoInfo.log(toShortString() + " Drawing image: " + ((Object) image) + " at: " + ((Object) new Rectangle(i2, i3, i4, i5)));
        }
        if (isDrawingBuffer()) {
            if (debugBuffered()) {
                Graphics graphicsDebugGraphics = debugGraphics();
                graphicsDebugGraphics.drawImage(image, i2, i3, i4, i5, imageObserver);
                graphicsDebugGraphics.dispose();
            }
        } else if (debugFlash()) {
            int i6 = (debugGraphicsInfoInfo.flashCount * 2) - 1;
            Image imageCreateImage = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), new DebugGraphicsFilter(debugGraphicsInfoInfo.flashColor)));
            DebugGraphicsObserver debugGraphicsObserver = new DebugGraphicsObserver();
            for (int i7 = 0; i7 < i6; i7++) {
                Image image2 = i7 % 2 == 0 ? imageCreateImage : image;
                loadImage(image2);
                this.graphics.drawImage(image2, i2, i3, i4, i5, debugGraphicsObserver);
                Toolkit.getDefaultToolkit().sync();
                sleep(debugGraphicsInfoInfo.flashTime);
            }
        }
        return this.graphics.drawImage(image, i2, i3, i4, i5, imageObserver);
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, Color color, ImageObserver imageObserver) {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        if (debugLog()) {
            debugGraphicsInfoInfo.log(toShortString() + " Drawing image: " + ((Object) image) + " at: " + ((Object) new Point(i2, i3)) + ", bgcolor: " + ((Object) color));
        }
        if (isDrawingBuffer()) {
            if (debugBuffered()) {
                Graphics graphicsDebugGraphics = debugGraphics();
                graphicsDebugGraphics.drawImage(image, i2, i3, color, imageObserver);
                graphicsDebugGraphics.dispose();
            }
        } else if (debugFlash()) {
            int i4 = (debugGraphicsInfoInfo.flashCount * 2) - 1;
            Image imageCreateImage = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), new DebugGraphicsFilter(debugGraphicsInfoInfo.flashColor)));
            DebugGraphicsObserver debugGraphicsObserver = new DebugGraphicsObserver();
            for (int i5 = 0; i5 < i4; i5++) {
                Image image2 = i5 % 2 == 0 ? imageCreateImage : image;
                loadImage(image2);
                this.graphics.drawImage(image2, i2, i3, color, debugGraphicsObserver);
                Toolkit.getDefaultToolkit().sync();
                sleep(debugGraphicsInfoInfo.flashTime);
            }
        }
        return this.graphics.drawImage(image, i2, i3, color, imageObserver);
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, int i4, int i5, Color color, ImageObserver imageObserver) {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        if (debugLog()) {
            debugGraphicsInfoInfo.log(toShortString() + " Drawing image: " + ((Object) image) + " at: " + ((Object) new Rectangle(i2, i3, i4, i5)) + ", bgcolor: " + ((Object) color));
        }
        if (isDrawingBuffer()) {
            if (debugBuffered()) {
                Graphics graphicsDebugGraphics = debugGraphics();
                graphicsDebugGraphics.drawImage(image, i2, i3, i4, i5, color, imageObserver);
                graphicsDebugGraphics.dispose();
            }
        } else if (debugFlash()) {
            int i6 = (debugGraphicsInfoInfo.flashCount * 2) - 1;
            Image imageCreateImage = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), new DebugGraphicsFilter(debugGraphicsInfoInfo.flashColor)));
            DebugGraphicsObserver debugGraphicsObserver = new DebugGraphicsObserver();
            for (int i7 = 0; i7 < i6; i7++) {
                Image image2 = i7 % 2 == 0 ? imageCreateImage : image;
                loadImage(image2);
                this.graphics.drawImage(image2, i2, i3, i4, i5, color, debugGraphicsObserver);
                Toolkit.getDefaultToolkit().sync();
                sleep(debugGraphicsInfoInfo.flashTime);
            }
        }
        return this.graphics.drawImage(image, i2, i3, i4, i5, color, imageObserver);
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, ImageObserver imageObserver) {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        if (debugLog()) {
            debugGraphicsInfoInfo.log(toShortString() + " Drawing image: " + ((Object) image) + " destination: " + ((Object) new Rectangle(i2, i3, i4, i5)) + " source: " + ((Object) new Rectangle(i6, i7, i8, i9)));
        }
        if (isDrawingBuffer()) {
            if (debugBuffered()) {
                Graphics graphicsDebugGraphics = debugGraphics();
                graphicsDebugGraphics.drawImage(image, i2, i3, i4, i5, i6, i7, i8, i9, imageObserver);
                graphicsDebugGraphics.dispose();
            }
        } else if (debugFlash()) {
            int i10 = (debugGraphicsInfoInfo.flashCount * 2) - 1;
            Image imageCreateImage = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), new DebugGraphicsFilter(debugGraphicsInfoInfo.flashColor)));
            DebugGraphicsObserver debugGraphicsObserver = new DebugGraphicsObserver();
            for (int i11 = 0; i11 < i10; i11++) {
                Image image2 = i11 % 2 == 0 ? imageCreateImage : image;
                loadImage(image2);
                this.graphics.drawImage(image2, i2, i3, i4, i5, i6, i7, i8, i9, debugGraphicsObserver);
                Toolkit.getDefaultToolkit().sync();
                sleep(debugGraphicsInfoInfo.flashTime);
            }
        }
        return this.graphics.drawImage(image, i2, i3, i4, i5, i6, i7, i8, i9, imageObserver);
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, Color color, ImageObserver imageObserver) {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        if (debugLog()) {
            debugGraphicsInfoInfo.log(toShortString() + " Drawing image: " + ((Object) image) + " destination: " + ((Object) new Rectangle(i2, i3, i4, i5)) + " source: " + ((Object) new Rectangle(i6, i7, i8, i9)) + ", bgcolor: " + ((Object) color));
        }
        if (isDrawingBuffer()) {
            if (debugBuffered()) {
                Graphics graphicsDebugGraphics = debugGraphics();
                graphicsDebugGraphics.drawImage(image, i2, i3, i4, i5, i6, i7, i8, i9, color, imageObserver);
                graphicsDebugGraphics.dispose();
            }
        } else if (debugFlash()) {
            int i10 = (debugGraphicsInfoInfo.flashCount * 2) - 1;
            Image imageCreateImage = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), new DebugGraphicsFilter(debugGraphicsInfoInfo.flashColor)));
            DebugGraphicsObserver debugGraphicsObserver = new DebugGraphicsObserver();
            for (int i11 = 0; i11 < i10; i11++) {
                Image image2 = i11 % 2 == 0 ? imageCreateImage : image;
                loadImage(image2);
                this.graphics.drawImage(image2, i2, i3, i4, i5, i6, i7, i8, i9, color, debugGraphicsObserver);
                Toolkit.getDefaultToolkit().sync();
                sleep(debugGraphicsInfoInfo.flashTime);
            }
        }
        return this.graphics.drawImage(image, i2, i3, i4, i5, i6, i7, i8, i9, color, imageObserver);
    }

    static void loadImage(Image image) {
        imageLoadingIcon.loadImage(image);
    }

    @Override // java.awt.Graphics
    public void copyArea(int i2, int i3, int i4, int i5, int i6, int i7) {
        if (debugLog()) {
            info().log(toShortString() + " Copying area from: " + ((Object) new Rectangle(i2, i3, i4, i5)) + " to: " + ((Object) new Point(i6, i7)));
        }
        this.graphics.copyArea(i2, i3, i4, i5, i6, i7);
    }

    final void sleep(int i2) {
        try {
            Thread.sleep(i2);
        } catch (Exception e2) {
        }
    }

    @Override // java.awt.Graphics
    public void dispose() {
        this.graphics.dispose();
        this.graphics = null;
    }

    public boolean isDrawingBuffer() {
        return this.buffer != null;
    }

    String toShortString() {
        return "Graphics" + (isDrawingBuffer() ? "<B>" : "") + "(" + this.graphicsID + LanguageTag.SEP + this.debugOptions + ")";
    }

    String pointToString(int i2, int i3) {
        return "(" + i2 + ", " + i3 + ")";
    }

    public void setDebugOptions(int i2) {
        if (i2 != 0) {
            if (i2 == -1) {
                if (this.debugOptions != 0) {
                    System.err.println(toShortString() + " Disabling debug");
                    this.debugOptions = 0;
                    return;
                }
                return;
            }
            if (this.debugOptions != i2) {
                this.debugOptions |= i2;
                if (debugLog()) {
                    System.err.println(toShortString() + " Enabling debug");
                }
            }
        }
    }

    public int getDebugOptions() {
        return this.debugOptions;
    }

    static void setDebugOptions(JComponent jComponent, int i2) {
        info().setDebugOptions(jComponent, i2);
    }

    static int getDebugOptions(JComponent jComponent) {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        if (debugGraphicsInfoInfo == null) {
            return 0;
        }
        return debugGraphicsInfoInfo.getDebugOptions(jComponent);
    }

    static int shouldComponentDebug(JComponent jComponent) {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        if (debugGraphicsInfoInfo == null) {
            return 0;
        }
        int debugOptions = 0;
        for (JComponent parent = jComponent; parent != null && (parent instanceof JComponent); parent = parent.getParent()) {
            debugOptions |= debugGraphicsInfoInfo.getDebugOptions(parent);
        }
        return debugOptions;
    }

    static int debugComponentCount() {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        if (debugGraphicsInfoInfo != null && debugGraphicsInfoInfo.componentToDebug != null) {
            return debugGraphicsInfoInfo.componentToDebug.size();
        }
        return 0;
    }

    boolean debugLog() {
        return (this.debugOptions & 1) == 1;
    }

    boolean debugFlash() {
        return (this.debugOptions & 2) == 2;
    }

    boolean debugBuffered() {
        return (this.debugOptions & 4) == 4;
    }

    private Graphics debugGraphics() {
        DebugGraphicsInfo debugGraphicsInfoInfo = info();
        if (debugGraphicsInfoInfo.debugFrame == null) {
            debugGraphicsInfoInfo.debugFrame = new JFrame();
            debugGraphicsInfoInfo.debugFrame.setSize(500, 500);
        }
        JFrame jFrame = debugGraphicsInfoInfo.debugFrame;
        jFrame.show();
        DebugGraphics debugGraphics = new DebugGraphics(jFrame.getGraphics());
        debugGraphics.setFont(getFont());
        debugGraphics.setColor(getColor());
        debugGraphics.translate(this.xOffset, this.yOffset);
        debugGraphics.setClip(getClipBounds());
        if (debugFlash()) {
            debugGraphics.setDebugOptions(2);
        }
        return debugGraphics;
    }

    static DebugGraphicsInfo info() {
        DebugGraphicsInfo debugGraphicsInfo = (DebugGraphicsInfo) SwingUtilities.appContextGet(debugGraphicsInfoKey);
        if (debugGraphicsInfo == null) {
            debugGraphicsInfo = new DebugGraphicsInfo();
            SwingUtilities.appContextPut(debugGraphicsInfoKey, debugGraphicsInfo);
        }
        return debugGraphicsInfo;
    }
}
