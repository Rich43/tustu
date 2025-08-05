package javafx.embed.swt;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Clipboard;
import com.sun.glass.ui.Pixels;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.cursor.CursorType;
import com.sun.javafx.embed.EmbeddedSceneDSInterface;
import com.sun.javafx.embed.EmbeddedSceneDTInterface;
import com.sun.javafx.embed.EmbeddedSceneInterface;
import com.sun.javafx.embed.EmbeddedStageInterface;
import com.sun.javafx.embed.HostInterface;
import com.sun.javafx.stage.EmbeddedWindow;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.scene.Scene;
import javafx.scene.input.TransferMode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.HTMLTransfer;
import org.eclipse.swt.dnd.ImageTransfer;
import org.eclipse.swt.dnd.RTFTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.dnd.URLTransfer;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: jfxswt.jar:javafx/embed/swt/FXCanvas.class */
public class FXCanvas extends Canvas {
    private HostContainer hostContainer;
    private volatile EmbeddedWindow stage;
    private volatile Scene scene;
    private EmbeddedStageInterface stagePeer;
    private EmbeddedSceneInterface scenePeer;
    private int pWidth;
    private int pHeight;
    private volatile int pPreferredWidth;
    private volatile int pPreferredHeight;
    private IntBuffer pixelsBuf;
    Listener moveFilter;
    private DropTarget dropTarget;
    static Transfer[] StandardTransfers = {TextTransfer.getInstance(), RTFTransfer.getInstance(), HTMLTransfer.getInstance(), URLTransfer.getInstance(), ImageTransfer.getInstance(), FileTransfer.getInstance()};
    static Transfer[] CustomTransfers = new Transfer[0];
    private static Field windowField;
    private static Method windowMethod;
    private static Method screenMethod;
    private static Method backingScaleFactorMethod;
    static ArrayList<DropTarget> targets;
    double lastScaleFactor;
    int lastWidth;
    int lastHeight;
    IntBuffer lastPixelsBuf;

    /* JADX INFO: Access modifiers changed from: private */
    public double getScaleFactor() {
        if (!SWT.getPlatform().equals("cocoa") || windowField == null || screenMethod == null || backingScaleFactorMethod == null) {
            return 1.0d;
        }
        try {
            Object nsWindow = windowField.get(getShell());
            Object nsScreen = screenMethod.invoke(nsWindow, new Object[0]);
            Object bsFactor = backingScaleFactorMethod.invoke(nsScreen, new Object[0]);
            return ((Double) bsFactor).doubleValue();
        } catch (Exception e2) {
            return 1.0d;
        }
    }

    static {
        if (SWT.getPlatform().equals("cocoa")) {
            try {
                windowField = Shell.class.getDeclaredField("window");
                windowField.setAccessible(true);
                Class nsViewClass = Class.forName("org.eclipse.swt.internal.cocoa.NSView");
                windowMethod = nsViewClass.getDeclaredMethod("window", new Class[0]);
                windowMethod.setAccessible(true);
                Class nsWindowClass = Class.forName("org.eclipse.swt.internal.cocoa.NSWindow");
                screenMethod = nsWindowClass.getDeclaredMethod("screen", new Class[0]);
                screenMethod.setAccessible(true);
                Class nsScreenClass = Class.forName("org.eclipse.swt.internal.cocoa.NSScreen");
                backingScaleFactorMethod = nsScreenClass.getDeclaredMethod("backingScaleFactor", new Class[0]);
                backingScaleFactorMethod.setAccessible(true);
            } catch (Exception e2) {
            }
        }
        targets = new ArrayList<>();
    }

    static Transfer[] getAllTransfers() {
        Transfer[] transfers = new Transfer[StandardTransfers.length + CustomTransfers.length];
        System.arraycopy(StandardTransfers, 0, transfers, 0, StandardTransfers.length);
        System.arraycopy(CustomTransfers, 0, transfers, StandardTransfers.length, CustomTransfers.length);
        return transfers;
    }

    static Transfer getCustomTransfer(String mime) {
        for (int i2 = 0; i2 < CustomTransfers.length; i2++) {
            if (CustomTransfers[i2].getMime().equals(mime)) {
                return CustomTransfers[i2];
            }
        }
        Transfer transfer = new CustomTransfer(mime, mime);
        Transfer[] newCustom = new Transfer[CustomTransfers.length + 1];
        System.arraycopy(CustomTransfers, 0, newCustom, 0, CustomTransfers.length);
        newCustom[CustomTransfers.length] = transfer;
        CustomTransfers = newCustom;
        return transfer;
    }

    public FXCanvas(@NamedArg("parent") Composite parent, @NamedArg(Constants.ATTRNAME_STYLE) int style) {
        super(parent, style | 262144);
        this.pWidth = 0;
        this.pHeight = 0;
        this.pPreferredWidth = -1;
        this.pPreferredHeight = -1;
        this.pixelsBuf = null;
        this.moveFilter = event -> {
            FXCanvas parent2 = this;
            while (true) {
                FXCanvas fXCanvas = parent2;
                if (fXCanvas != null) {
                    if (fXCanvas == event.widget) {
                        sendMoveEventToFX();
                        return;
                    }
                    parent2 = fXCanvas.getParent();
                } else {
                    return;
                }
            }
        };
        this.lastScaleFactor = 1.0d;
        this.lastPixelsBuf = null;
        initFx();
        this.hostContainer = new HostContainer();
        registerEventListeners();
        Display display = parent.getDisplay();
        display.addFilter(10, this.moveFilter);
    }

    private static void initFx() {
        AccessController.doPrivileged(() -> {
            System.setProperty("javafx.embed.isEventThread", "true");
            System.setProperty("glass.win.uiScale", "100%");
            System.setProperty("glass.win.renderScale", "100%");
            return null;
        });
        Map map = Application.getDeviceDetails();
        if (map == null) {
            HashMap map2 = new HashMap();
            map = map2;
            Application.setDeviceDetails(map2);
        }
        if (map.get("javafx.embed.eventProc") == null) {
            long eventProc = 0;
            try {
                Field field = Display.class.getDeclaredField("eventProc");
                field.setAccessible(true);
                if (field.getType() == Integer.TYPE || field.getType() == Long.TYPE) {
                    eventProc = field.getLong(Display.getDefault());
                }
            } catch (Throwable th) {
            }
            map.put("javafx.embed.eventProc", Long.valueOf(eventProc));
        }
        PlatformImpl.startup(() -> {
            Application.GetApplication().setName(Display.getAppName());
        });
    }

    DropTarget getDropTarget() {
        return this.dropTarget;
    }

    void setDropTarget(DropTarget newTarget) {
        if (this.dropTarget != null) {
            targets.remove(this.dropTarget);
            this.dropTarget.dispose();
        }
        this.dropTarget = newTarget;
        if (this.dropTarget != null) {
            targets.add(this.dropTarget);
        }
    }

    static void updateDropTarget() {
        Iterator<DropTarget> it = targets.iterator();
        while (it.hasNext()) {
            DropTarget target = it.next();
            target.setTransfer(getAllTransfers());
        }
    }

    public Point computeSize(int wHint, int hHint, boolean changed) {
        checkWidget();
        if (wHint == -1 && hHint == -1 && this.pPreferredWidth != -1 && this.pPreferredHeight != -1) {
            return new Point(this.pPreferredWidth, this.pPreferredHeight);
        }
        return super.computeSize(wHint, hHint, changed);
    }

    public Scene getScene() {
        checkWidget();
        return this.scene;
    }

    public void setScene(Scene newScene) {
        checkWidget();
        if (this.stage == null && newScene != null) {
            this.stage = new EmbeddedWindow(this.hostContainer);
            this.stage.show();
        }
        this.scene = newScene;
        if (this.stage != null) {
            this.stage.setScene(newScene);
        }
        if (this.stage != null && newScene == null) {
            this.stage.hide();
            this.stage = null;
        }
    }

    private void registerEventListeners() {
        addDisposeListener(new DisposeListener() { // from class: javafx.embed.swt.FXCanvas.1
            public void widgetDisposed(DisposeEvent de2) {
                Display display = FXCanvas.this.getDisplay();
                display.removeFilter(10, FXCanvas.this.moveFilter);
                FXCanvas.this.widgetDisposed(de2);
            }
        });
        addPaintListener(pe -> {
            paintControl(pe);
        });
        addMouseListener(new MouseListener() { // from class: javafx.embed.swt.FXCanvas.2
            public void mouseDoubleClick(MouseEvent me) {
            }

            public void mouseDown(MouseEvent me) {
                if (me.button > 3) {
                    return;
                }
                FXCanvas.this.sendMouseEventToFX(me, 0);
            }

            public void mouseUp(MouseEvent me) {
                if (me.button > 3) {
                    return;
                }
                FXCanvas.this.sendMouseEventToFX(me, 1);
            }
        });
        addMouseMoveListener(me -> {
            if ((me.stateMask & SWT.BUTTON_MASK) != 0) {
                if ((me.stateMask & 3670016) != 0) {
                    sendMouseEventToFX(me, 6);
                    return;
                } else {
                    sendMouseEventToFX(me, 5);
                    return;
                }
            }
            sendMouseEventToFX(me, 5);
        });
        addMouseWheelListener(me2 -> {
            sendMouseEventToFX(me2, 7);
        });
        addMouseTrackListener(new MouseTrackListener() { // from class: javafx.embed.swt.FXCanvas.3
            public void mouseEnter(MouseEvent me3) {
                FXCanvas.this.sendMouseEventToFX(me3, 3);
            }

            public void mouseExit(MouseEvent me3) {
                FXCanvas.this.sendMouseEventToFX(me3, 4);
            }

            public void mouseHover(MouseEvent me3) {
            }
        });
        addControlListener(new ControlListener() { // from class: javafx.embed.swt.FXCanvas.4
            public void controlMoved(ControlEvent ce) {
                FXCanvas.this.sendMoveEventToFX();
            }

            public void controlResized(ControlEvent ce) {
                FXCanvas.this.sendResizeEventToFX();
            }
        });
        addFocusListener(new FocusListener() { // from class: javafx.embed.swt.FXCanvas.5
            public void focusGained(FocusEvent fe) {
                FXCanvas.this.sendFocusEventToFX(fe, true);
            }

            public void focusLost(FocusEvent fe) {
                FXCanvas.this.sendFocusEventToFX(fe, false);
            }
        });
        addKeyListener(new KeyListener() { // from class: javafx.embed.swt.FXCanvas.6
            public void keyPressed(KeyEvent e2) {
                FXCanvas.this.sendKeyEventToFX(e2, 1);
            }

            public void keyReleased(KeyEvent e2) {
                FXCanvas.this.sendKeyEventToFX(e2, 2);
            }
        });
        addMenuDetectListener(e2 -> {
            Runnable r2 = () -> {
                if (isDisposed()) {
                    return;
                }
                sendMenuEventToFX(e2);
            };
            if ("cocoa".equals(SWT.getPlatform())) {
                getDisplay().asyncExec(r2);
            } else {
                r2.run();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void widgetDisposed(DisposeEvent de2) {
        setDropTarget(null);
        if (this.stage != null) {
            this.stage.hide();
        }
    }

    private void paintControl(PaintEvent pe) {
        int width;
        int height;
        IntBuffer buffer;
        ImageData imageData;
        if (this.scenePeer == null || this.pixelsBuf == null) {
            return;
        }
        double scaleFactor = getScaleFactor();
        if (this.lastScaleFactor != scaleFactor) {
            resizePixelBuffer(scaleFactor);
            this.lastScaleFactor = scaleFactor;
            this.scenePeer.setPixelScaleFactor((float) scaleFactor);
        }
        IntBuffer intBuffer = this.pixelsBuf;
        int i2 = this.pWidth;
        int i3 = this.pHeight;
        if (this.scenePeer.getPixels(this.pixelsBuf, this.pWidth, this.pHeight)) {
            int i4 = this.pWidth;
            this.lastWidth = i4;
            width = i4;
            int i5 = this.pHeight;
            this.lastHeight = i5;
            height = i5;
            IntBuffer intBuffer2 = this.pixelsBuf;
            this.lastPixelsBuf = intBuffer2;
            buffer = intBuffer2;
        } else {
            if (this.lastPixelsBuf == null) {
                return;
            }
            width = this.lastWidth;
            height = this.lastHeight;
            buffer = this.lastPixelsBuf;
        }
        int width2 = (int) Math.round(width * scaleFactor);
        int height2 = (int) Math.round(height * scaleFactor);
        if ("win32".equals(SWT.getPlatform())) {
            PaletteData palette = new PaletteData(NormalizerImpl.CC_MASK, 16711680, -16777216);
            int scanline = width2 * 4;
            byte[] dstData = new byte[scanline * height2];
            int[] srcData = buffer.array();
            int dp = 0;
            int sp = 0;
            for (int y2 = 0; y2 < height2; y2++) {
                for (int x2 = 0; x2 < width2; x2++) {
                    int i6 = sp;
                    sp++;
                    int p2 = srcData[i6];
                    int i7 = dp;
                    int dp2 = dp + 1;
                    dstData[i7] = (byte) (p2 & 255);
                    int dp3 = dp2 + 1;
                    dstData[dp2] = (byte) ((p2 >> 8) & 255);
                    int dp4 = dp3 + 1;
                    dstData[dp3] = (byte) ((p2 >> 16) & 255);
                    dp = dp4 + 1;
                    dstData[dp4] = 0;
                }
            }
            imageData = new ImageData(width2, height2, 32, palette, 4, dstData);
        } else if (width2 * height2 > buffer.array().length) {
            System.err.println("FXCanvas.paintControl: scale mismatch!");
            return;
        } else {
            PaletteData palette2 = new PaletteData(16711680, NormalizerImpl.CC_MASK, 255);
            imageData = new ImageData(width2, height2, 32, palette2);
            imageData.setPixels(0, 0, width2 * height2, buffer.array(), 0);
        }
        Image image = new Image(Display.getDefault(), imageData);
        pe.gc.drawImage(image, 0, 0, width2, height2, 0, 0, this.pWidth, this.pHeight);
        image.dispose();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMoveEventToFX() {
        if (this.stagePeer == null) {
            return;
        }
        Rectangle rect = getClientArea();
        Point los = toDisplay(rect.x, rect.y);
        this.stagePeer.setLocation(los.x, los.y);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMouseEventToFX(MouseEvent me, int embedMouseType) {
        if (this.scenePeer == null) {
            return;
        }
        Point los = toDisplay(me.x, me.y);
        boolean primaryBtnDown = (me.stateMask & 524288) != 0;
        boolean middleBtnDown = (me.stateMask & 1048576) != 0;
        boolean secondaryBtnDown = (me.stateMask & 2097152) != 0;
        boolean shift = (me.stateMask & 131072) != 0;
        boolean control = (me.stateMask & 262144) != 0;
        boolean alt = (me.stateMask & 65536) != 0;
        boolean meta = (me.stateMask & 4194304) != 0;
        int button = me.button;
        switch (embedMouseType) {
            case 0:
                primaryBtnDown |= me.button == 1;
                middleBtnDown |= me.button == 2;
                secondaryBtnDown |= me.button == 3;
                break;
            case 1:
                primaryBtnDown &= me.button != 1;
                middleBtnDown &= me.button != 2;
                secondaryBtnDown &= me.button != 3;
                break;
            case 2:
                return;
            case 3:
            case 4:
            case 5:
            case 6:
                if (button == 0) {
                    if ((me.stateMask & 524288) != 0) {
                        button = 1;
                        break;
                    } else if ((me.stateMask & 1048576) != 0) {
                        button = 2;
                        break;
                    } else if ((me.stateMask & 2097152) != 0) {
                        button = 3;
                        break;
                    }
                }
                break;
        }
        this.scenePeer.mouseEvent(embedMouseType, SWTEvents.mouseButtonToEmbedMouseButton(button, me.stateMask), primaryBtnDown, middleBtnDown, secondaryBtnDown, me.x, me.y, los.x, los.y, shift, control, alt, meta, SWTEvents.getWheelRotation(me, embedMouseType), false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendKeyEventToFX(KeyEvent e2, int type) {
        if (this.scenePeer == null) {
            return;
        }
        int stateMask = e2.stateMask;
        if (type == 1) {
            if (e2.keyCode == 131072) {
                stateMask |= 131072;
            }
            if (e2.keyCode == 262144) {
                stateMask |= 262144;
            }
            if (e2.keyCode == 65536) {
                stateMask |= 65536;
            }
            if (e2.keyCode == 4194304) {
                stateMask |= 4194304;
            }
        } else {
            if (e2.keyCode == 131072) {
                stateMask &= -131073;
            }
            if (e2.keyCode == 262144) {
                stateMask &= -262145;
            }
            if (e2.keyCode == 65536) {
                stateMask &= -65537;
            }
            if (e2.keyCode == 4194304) {
                stateMask &= -4194305;
            }
        }
        int keyCode = SWTEvents.keyCodeToEmbedKeyCode(e2.keyCode);
        this.scenePeer.keyEvent(SWTEvents.keyIDToEmbedKeyType(type), keyCode, new char[0], SWTEvents.keyModifiersToEmbedKeyModifiers(stateMask));
        if (e2.character != 0 && type == 1) {
            char[] chars = {e2.character};
            this.scenePeer.keyEvent(2, e2.keyCode, chars, SWTEvents.keyModifiersToEmbedKeyModifiers(stateMask));
        }
    }

    private void sendMenuEventToFX(MenuDetectEvent me) {
        if (this.scenePeer == null) {
            return;
        }
        Point pt = toControl(me.x, me.y);
        this.scenePeer.menuEvent(pt.x, pt.y, me.x, me.y, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendResizeEventToFX() {
        redraw();
        update();
        this.pWidth = getClientArea().width;
        this.pHeight = getClientArea().height;
        resizePixelBuffer(this.lastScaleFactor);
        if (this.scenePeer == null) {
            return;
        }
        this.stagePeer.setSize(this.pWidth, this.pHeight);
        this.scenePeer.setSize(this.pWidth, this.pHeight);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resizePixelBuffer(double newScaleFactor) {
        this.lastPixelsBuf = null;
        if (this.pWidth <= 0 || this.pHeight <= 0) {
            this.pixelsBuf = null;
            return;
        }
        this.pixelsBuf = IntBuffer.allocate(((int) Math.round(this.pWidth * newScaleFactor)) * ((int) Math.round(this.pHeight * newScaleFactor)));
        RGB rgb = getBackground().getRGB();
        Arrays.fill(this.pixelsBuf.array(), (rgb.red << 16) | (rgb.green << 8) | rgb.blue);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendFocusEventToFX(FocusEvent fe, boolean focused) {
        if (this.stage == null || this.stagePeer == null) {
            return;
        }
        int focusCause = focused ? 0 : 3;
        this.stagePeer.setFocused(focused, focusCause);
    }

    /* loaded from: jfxswt.jar:javafx/embed/swt/FXCanvas$HostContainer.class */
    private class HostContainer implements HostInterface {
        Object lock;
        boolean queued;

        private HostContainer() {
            this.lock = new Object();
            this.queued = false;
        }

        @Override // com.sun.javafx.embed.HostInterface
        public void setEmbeddedStage(EmbeddedStageInterface embeddedStage) {
            FXCanvas.this.stagePeer = embeddedStage;
            if (FXCanvas.this.stagePeer != null) {
                if (FXCanvas.this.pWidth > 0 && FXCanvas.this.pHeight > 0) {
                    FXCanvas.this.stagePeer.setSize(FXCanvas.this.pWidth, FXCanvas.this.pHeight);
                }
                if (FXCanvas.this.isFocusControl()) {
                    FXCanvas.this.stagePeer.setFocused(true, 0);
                }
                FXCanvas.this.sendMoveEventToFX();
                FXCanvas.this.sendResizeEventToFX();
            }
        }

        TransferMode getTransferMode(int bits) {
            switch (bits) {
                case 1:
                    return TransferMode.COPY;
                case 2:
                case 8:
                    return TransferMode.MOVE;
                case 3:
                case 5:
                case 6:
                case 7:
                default:
                    return null;
                case 4:
                    return TransferMode.LINK;
            }
        }

        Set<TransferMode> getTransferModes(int bits) {
            Set<TransferMode> set = new HashSet<>();
            if ((bits & 1) != 0) {
                set.add(TransferMode.COPY);
            }
            if ((bits & 2) != 0) {
                set.add(TransferMode.MOVE);
            }
            if ((bits & 8) != 0) {
                set.add(TransferMode.MOVE);
            }
            if ((bits & 4) != 0) {
                set.add(TransferMode.LINK);
            }
            return set;
        }

        ImageData createImageData(Pixels pixels) {
            if (pixels == null) {
                return null;
            }
            int width = pixels.getWidth();
            int height = pixels.getHeight();
            int bpr = width * 4;
            int dataSize = bpr * height;
            byte[] buffer = new byte[dataSize];
            byte[] alphaData = new byte[width * height];
            if (pixels.getBytesPerComponent() == 1) {
                ByteBuffer pixbuf = (ByteBuffer) pixels.getPixels();
                int offset = 0;
                int alphaOffset = 0;
                for (int y2 = 0; y2 < height; y2++) {
                    int x2 = 0;
                    while (x2 < width) {
                        byte b2 = pixbuf.get();
                        byte g2 = pixbuf.get();
                        byte r2 = pixbuf.get();
                        byte a2 = pixbuf.get();
                        int i2 = alphaOffset;
                        alphaOffset++;
                        alphaData[i2] = a2;
                        buffer[offset] = b2;
                        buffer[offset + 1] = g2;
                        buffer[offset + 2] = r2;
                        buffer[offset + 3] = 0;
                        x2++;
                        offset += 4;
                    }
                }
            } else if (pixels.getBytesPerComponent() == 4) {
                IntBuffer pixbuf2 = (IntBuffer) pixels.getPixels();
                int offset2 = 0;
                int alphaOffset2 = 0;
                for (int y3 = 0; y3 < height; y3++) {
                    int x3 = 0;
                    while (x3 < width) {
                        int pixel = pixbuf2.get();
                        byte b3 = (byte) (pixel & 255);
                        byte g3 = (byte) ((pixel >> 8) & 255);
                        byte r3 = (byte) ((pixel >> 16) & 255);
                        byte a3 = (byte) ((pixel >> 24) & 255);
                        int i3 = alphaOffset2;
                        alphaOffset2++;
                        alphaData[i3] = a3;
                        buffer[offset2] = b3;
                        buffer[offset2 + 1] = g3;
                        buffer[offset2 + 2] = r3;
                        buffer[offset2 + 3] = 0;
                        x3++;
                        offset2 += 4;
                    }
                }
            } else {
                return null;
            }
            PaletteData palette = new PaletteData(NormalizerImpl.CC_MASK, 16711680, -16777216);
            ImageData imageData = new ImageData(width, height, 32, palette, 4, buffer);
            imageData.alphaData = alphaData;
            return imageData;
        }

        private DragSource createDragSource(final EmbeddedSceneDSInterface fxDragSource, TransferMode dragAction) {
            Transfer[] transfers = getTransferTypes(fxDragSource.getMimeTypes());
            if (transfers.length == 0) {
                return null;
            }
            int dragOperation = getDragActions(fxDragSource.getSupportedActions());
            final DragSource dragSource = new DragSource(FXCanvas.this, dragOperation);
            dragSource.setTransfer(transfers);
            dragSource.addDragListener(new DragSourceListener() { // from class: javafx.embed.swt.FXCanvas.HostContainer.1
                public void dragFinished(DragSourceEvent event) {
                    dragSource.dispose();
                    fxDragSource.dragDropEnd(HostContainer.this.getTransferMode(event.detail));
                }

                public void dragSetData(DragSourceEvent event) {
                    String mime;
                    Transfer[] transfers2 = dragSource.getTransfer();
                    for (int i2 = 0; i2 < transfers2.length; i2++) {
                        if (transfers2[i2].isSupportedType(event.dataType) && (mime = HostContainer.this.getMime(transfers2[i2])) != null) {
                            event.doit = true;
                            event.data = fxDragSource.getData(mime);
                            if (event.data instanceof Pixels) {
                                event.data = HostContainer.this.createImageData((Pixels) event.data);
                                return;
                            }
                            return;
                        }
                        event.doit = false;
                    }
                }

                public void dragStart(DragSourceEvent event) {
                }
            });
            return dragSource;
        }

        int getDragAction(TransferMode tm) {
            if (tm == null) {
                return 0;
            }
            switch (tm) {
                case COPY:
                    return 1;
                case MOVE:
                    return 2;
                case LINK:
                    return 4;
                default:
                    throw new IllegalArgumentException("Invalid transfer mode");
            }
        }

        int getDragActions(Set<TransferMode> set) {
            int result = 0;
            for (TransferMode mode : set) {
                result |= getDragAction(mode);
            }
            return result;
        }

        Transfer getTransferType(String mime) {
            if (mime.equals(Clipboard.TEXT_TYPE)) {
                return TextTransfer.getInstance();
            }
            if (mime.equals(Clipboard.RTF_TYPE)) {
                return RTFTransfer.getInstance();
            }
            if (mime.equals(Clipboard.HTML_TYPE)) {
                return HTMLTransfer.getInstance();
            }
            if (mime.equals(Clipboard.URI_TYPE)) {
                return URLTransfer.getInstance();
            }
            if (mime.equals(Clipboard.RAW_IMAGE_TYPE)) {
                return ImageTransfer.getInstance();
            }
            if (mime.equals(Clipboard.FILE_LIST_TYPE) || mime.equals("java.file-list")) {
                return FileTransfer.getInstance();
            }
            return FXCanvas.getCustomTransfer(mime);
        }

        Transfer[] getTransferTypes(String[] mimeTypes) {
            int count = 0;
            Transfer[] transfers = new Transfer[mimeTypes.length];
            for (String str : mimeTypes) {
                Transfer transfer = getTransferType(str);
                if (transfer != null) {
                    int i2 = count;
                    count++;
                    transfers[i2] = transfer;
                }
            }
            if (count != mimeTypes.length) {
                Transfer[] newTransfers = new Transfer[count];
                System.arraycopy(transfers, 0, newTransfers, 0, count);
                transfers = newTransfers;
            }
            return transfers;
        }

        String getMime(Transfer transfer) {
            if (transfer.equals(TextTransfer.getInstance())) {
                return Clipboard.TEXT_TYPE;
            }
            if (transfer.equals(RTFTransfer.getInstance())) {
                return Clipboard.RTF_TYPE;
            }
            if (transfer.equals(HTMLTransfer.getInstance())) {
                return Clipboard.HTML_TYPE;
            }
            if (transfer.equals(URLTransfer.getInstance())) {
                return Clipboard.URI_TYPE;
            }
            if (transfer.equals(ImageTransfer.getInstance())) {
                return Clipboard.RAW_IMAGE_TYPE;
            }
            if (transfer.equals(FileTransfer.getInstance())) {
                return Clipboard.FILE_LIST_TYPE;
            }
            if (transfer instanceof CustomTransfer) {
                return ((CustomTransfer) transfer).getMime();
            }
            return null;
        }

        String[] getMimes(Transfer[] transfers, TransferData data) {
            int count = 0;
            String[] result = new String[transfers.length];
            for (int i2 = 0; i2 < transfers.length; i2++) {
                if (transfers[i2].isSupportedType(data)) {
                    int i3 = count;
                    count++;
                    result[i3] = getMime(transfers[i2]);
                }
            }
            if (count != result.length) {
                String[] newResult = new String[count];
                System.arraycopy(result, 0, newResult, 0, count);
                result = newResult;
            }
            return result;
        }

        DropTarget createDropTarget(EmbeddedSceneInterface embeddedScene) {
            DropTarget dropTarget = new DropTarget(FXCanvas.this, 7);
            EmbeddedSceneDTInterface fxDropTarget = embeddedScene.createDropTarget();
            dropTarget.setTransfer(FXCanvas.getAllTransfers());
            dropTarget.addDropListener(new AnonymousClass2(dropTarget, fxDropTarget));
            return dropTarget;
        }

        /* renamed from: javafx.embed.swt.FXCanvas$HostContainer$2, reason: invalid class name */
        /* loaded from: jfxswt.jar:javafx/embed/swt/FXCanvas$HostContainer$2.class */
        class AnonymousClass2 implements DropTargetListener {
            Object data;
            TransferData currentTransferData;
            boolean ignoreLeave;
            int detail = 0;
            int operations = 0;
            EmbeddedSceneDSInterface fxDragSource = new EmbeddedSceneDSInterface() { // from class: javafx.embed.swt.FXCanvas.HostContainer.2.1
                @Override // com.sun.javafx.embed.EmbeddedSceneDSInterface
                public Set<TransferMode> getSupportedActions() {
                    return HostContainer.this.getTransferModes(AnonymousClass2.this.operations);
                }

                @Override // com.sun.javafx.embed.EmbeddedSceneDSInterface
                public Object getData(String mimeType) {
                    return AnonymousClass2.this.data;
                }

                @Override // com.sun.javafx.embed.EmbeddedSceneDSInterface
                public String[] getMimeTypes() {
                    return AnonymousClass2.this.currentTransferData == null ? new String[0] : HostContainer.this.getMimes(FXCanvas.getAllTransfers(), AnonymousClass2.this.currentTransferData);
                }

                @Override // com.sun.javafx.embed.EmbeddedSceneDSInterface
                public boolean isMimeTypeAvailable(String mimeType) {
                    String[] mimes = getMimeTypes();
                    for (String str : mimes) {
                        if (str.equals(mimeType)) {
                            return true;
                        }
                    }
                    return false;
                }

                @Override // com.sun.javafx.embed.EmbeddedSceneDSInterface
                public void dragDropEnd(TransferMode performedAction) {
                    AnonymousClass2.this.data = null;
                    AnonymousClass2.this.currentTransferData = null;
                }
            };
            final /* synthetic */ DropTarget val$dropTarget;
            final /* synthetic */ EmbeddedSceneDTInterface val$fxDropTarget;

            AnonymousClass2(DropTarget dropTarget, EmbeddedSceneDTInterface embeddedSceneDTInterface) {
                this.val$dropTarget = dropTarget;
                this.val$fxDropTarget = embeddedSceneDTInterface;
            }

            public void dragEnter(DropTargetEvent event) {
                this.ignoreLeave = false;
                this.val$dropTarget.setTransfer(FXCanvas.getAllTransfers());
                this.detail = event.detail;
                this.operations = event.operations;
                dragOver(event, true, this.detail);
            }

            public void dragLeave(DropTargetEvent event) {
                this.operations = 0;
                this.detail = 0;
                this.data = null;
                this.currentTransferData = null;
                Display display = FXCanvas.this.getDisplay();
                EmbeddedSceneDTInterface embeddedSceneDTInterface = this.val$fxDropTarget;
                display.asyncExec(() -> {
                    if (this.ignoreLeave) {
                        return;
                    }
                    embeddedSceneDTInterface.handleDragLeave();
                });
            }

            public void dragOperationChanged(DropTargetEvent event) {
                this.detail = event.detail;
                this.operations = event.operations;
                dragOver(event, false, this.detail);
            }

            public void dragOver(DropTargetEvent event) {
                this.operations = event.operations;
                dragOver(event, false, this.detail);
            }

            public void dragOver(DropTargetEvent event, boolean enter, int detail) {
                TransferMode acceptedMode;
                this.currentTransferData = event.currentDataType;
                Point pt = FXCanvas.this.toControl(event.x, event.y);
                if (detail == 0) {
                    detail = 1;
                }
                TransferMode recommendedMode = HostContainer.this.getTransferMode(detail);
                if (enter) {
                    acceptedMode = this.val$fxDropTarget.handleDragEnter(pt.x, pt.y, event.x, event.y, recommendedMode, this.fxDragSource);
                } else {
                    acceptedMode = this.val$fxDropTarget.handleDragOver(pt.x, pt.y, event.x, event.y, recommendedMode);
                }
                event.detail = HostContainer.this.getDragAction(acceptedMode);
            }

            public void drop(DropTargetEvent event) {
                this.detail = event.detail;
                this.operations = event.operations;
                this.data = event.data;
                this.currentTransferData = event.currentDataType;
                Point pt = FXCanvas.this.toControl(event.x, event.y);
                TransferMode recommendedDropAction = HostContainer.this.getTransferMode(event.detail);
                TransferMode acceptedMode = this.val$fxDropTarget.handleDragDrop(pt.x, pt.y, event.x, event.y, recommendedDropAction);
                event.detail = HostContainer.this.getDragAction(acceptedMode);
                this.data = null;
                this.currentTransferData = null;
            }

            public void dropAccept(DropTargetEvent event) {
                this.ignoreLeave = true;
            }
        }

        @Override // com.sun.javafx.embed.HostInterface
        public void setEmbeddedScene(EmbeddedSceneInterface embeddedScene) {
            FXCanvas.this.scenePeer = embeddedScene;
            if (FXCanvas.this.scenePeer != null) {
                if (FXCanvas.this.pWidth > 0 && FXCanvas.this.pHeight > 0) {
                    FXCanvas.this.scenePeer.setSize(FXCanvas.this.pWidth, FXCanvas.this.pHeight);
                }
                double scaleFactor = FXCanvas.this.getScaleFactor();
                FXCanvas.this.resizePixelBuffer(scaleFactor);
                FXCanvas.this.lastScaleFactor = scaleFactor;
                FXCanvas.this.scenePeer.setPixelScaleFactor((float) scaleFactor);
                FXCanvas.this.scenePeer.setDragStartListener((fxDragSource, dragAction) -> {
                    Platform.runLater(() -> {
                        DragSource dragSource = createDragSource(fxDragSource, dragAction);
                        if (dragSource == null) {
                            fxDragSource.dragDropEnd(null);
                        } else {
                            FXCanvas.updateDropTarget();
                            FXCanvas.this.notifyListeners(29, null);
                        }
                    });
                });
                FXCanvas.this.setDropTarget(null);
                FXCanvas.this.setDropTarget(createDropTarget(embeddedScene));
            }
        }

        @Override // com.sun.javafx.embed.HostInterface
        public boolean requestFocus() {
            Display.getDefault().asyncExec(() -> {
                if (FXCanvas.this.isDisposed()) {
                    return;
                }
                FXCanvas.this.forceFocus();
            });
            return true;
        }

        @Override // com.sun.javafx.embed.HostInterface
        public boolean traverseFocusOut(boolean bln) {
            return true;
        }

        @Override // com.sun.javafx.embed.HostInterface
        public void repaint() {
            synchronized (this.lock) {
                if (this.queued) {
                    return;
                }
                this.queued = true;
                Display.getDefault().asyncExec(() -> {
                    try {
                        if (!FXCanvas.this.isDisposed()) {
                            FXCanvas.this.redraw();
                            synchronized (this.lock) {
                                this.queued = false;
                            }
                            return;
                        }
                        synchronized (this.lock) {
                            this.queued = false;
                        }
                    } catch (Throwable th) {
                        synchronized (this.lock) {
                            this.queued = false;
                            throw th;
                        }
                    }
                });
            }
        }

        @Override // com.sun.javafx.embed.HostInterface
        public void setPreferredSize(int width, int height) {
            FXCanvas.this.pPreferredWidth = width;
            FXCanvas.this.pPreferredHeight = height;
        }

        @Override // com.sun.javafx.embed.HostInterface
        public void setEnabled(boolean bln) {
            FXCanvas.this.setEnabled(bln);
        }

        @Override // com.sun.javafx.embed.HostInterface
        public void setCursor(CursorFrame cursorFrame) {
            FXCanvas.this.setCursor(getPlatformCursor(cursorFrame));
        }

        private Cursor getPlatformCursor(CursorFrame cursorFrame) {
            if (cursorFrame.getCursorType() == CursorType.DEFAULT) {
                return null;
            }
            Cursor cachedPlatformCursor = (Cursor) cursorFrame.getPlatformCursor(Cursor.class);
            if (cachedPlatformCursor != null) {
                return cachedPlatformCursor;
            }
            Cursor platformCursor = SWTCursors.embedCursorToCursor(cursorFrame);
            cursorFrame.setPlatforCursor(Cursor.class, platformCursor);
            return platformCursor;
        }

        @Override // com.sun.javafx.embed.HostInterface
        public boolean grabFocus() {
            return true;
        }

        @Override // com.sun.javafx.embed.HostInterface
        public void ungrabFocus() {
        }
    }
}
