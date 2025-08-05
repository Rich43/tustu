package com.sun.javafx.tk.quantum;

import com.sun.glass.events.TouchEvent;
import com.sun.glass.events.ViewEvent;
import com.sun.glass.ui.Accessible;
import com.sun.glass.ui.ClipboardAssistance;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.logging.PulseLogger;
import com.sun.javafx.scene.input.KeyCodeMap;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.InputMethodHighlight;
import javafx.scene.input.InputMethodTextRun;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.RotateEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.input.TouchPoint;
import javafx.scene.input.TransferMode;
import javafx.scene.input.ZoomEvent;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.nntp.NNTPReply;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/GlassViewEventHandler.class */
class GlassViewEventHandler extends View.EventHandler {
    static boolean zoomGestureEnabled;
    static boolean rotateGestureEnabled;
    static boolean scrollGestureEnabled;
    private ViewScene scene;
    private final GlassSceneDnDEventHandler dndHandler;
    private ClipboardAssistance dropSourceAssistant;
    private final PaintCollector collector = PaintCollector.getInstance();
    private final KeyEventNotification keyNotification = new KeyEventNotification();
    private int mouseButtonPressedMask = 0;
    private final MouseEventNotification mouseNotification = new MouseEventNotification();
    private final ViewEventNotification viewNotification = new ViewEventNotification();
    private final GestureRecognizers gestures = new GestureRecognizers();

    static {
        AccessController.doPrivileged(() -> {
            zoomGestureEnabled = Boolean.valueOf(System.getProperty("com.sun.javafx.gestures.zoom", "false")).booleanValue();
            rotateGestureEnabled = Boolean.valueOf(System.getProperty("com.sun.javafx.gestures.rotate", "false")).booleanValue();
            scrollGestureEnabled = Boolean.valueOf(System.getProperty("com.sun.javafx.gestures.scroll", "false")).booleanValue();
            return null;
        });
    }

    public GlassViewEventHandler(ViewScene scene) {
        this.scene = scene;
        this.dndHandler = new GlassSceneDnDEventHandler(scene);
        if (PlatformUtil.isWindows() || PlatformUtil.isIOS() || PlatformUtil.isEmbedded()) {
            this.gestures.add(new SwipeGestureRecognizer(scene));
        }
        if (zoomGestureEnabled) {
            this.gestures.add(new ZoomGestureRecognizer(scene));
        }
        if (rotateGestureEnabled) {
            this.gestures.add(new RotateGestureRecognizer(scene));
        }
        if (scrollGestureEnabled) {
            this.gestures.add(new ScrollGestureRecognizer(scene));
        }
    }

    private static boolean allowableFullScreenKeys(int key) {
        switch (key) {
            case 9:
            case 10:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
                return true;
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            default:
                return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkFullScreenKeyEvent(int type, int key, char[] chars, int modifiers) {
        return this.scene.getWindowStage().isTrustedFullScreen() || allowableFullScreenKeys(key);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static EventType<KeyEvent> keyEventType(int glassType) {
        switch (glassType) {
            case 111:
                return KeyEvent.KEY_PRESSED;
            case 112:
                return KeyEvent.KEY_RELEASED;
            case 113:
                return KeyEvent.KEY_TYPED;
            default:
                if (QuantumToolkit.verbose) {
                    System.err.println("Unknown Glass key event type: " + glassType);
                    return null;
                }
                return null;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/GlassViewEventHandler$KeyEventNotification.class */
    private class KeyEventNotification implements PrivilegedAction<Void> {
        View view;
        long time;
        int type;
        int key;
        char[] chars;
        int modifiers;
        private KeyCode lastKeyCode;

        private KeyEventNotification() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public Void run2() {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(GlassViewEventHandler.keyEventType(this.type).toString());
            }
            WindowStage stage = GlassViewEventHandler.this.scene.getWindowStage();
            try {
                boolean shiftDown = (this.modifiers & 1) != 0;
                boolean controlDown = (this.modifiers & 4) != 0;
                boolean altDown = (this.modifiers & 8) != 0;
                boolean metaDown = (this.modifiers & 16) != 0;
                String str = new String(this.chars);
                KeyEvent keyEvent = new KeyEvent(GlassViewEventHandler.keyEventType(this.type), str, str, KeyCodeMap.valueOf(this.key), shiftDown, controlDown, altDown, metaDown);
                KeyCode keyCode = KeyCodeMap.valueOf(this.key);
                switch (this.type) {
                    case 111:
                    case 112:
                        this.lastKeyCode = keyCode;
                        break;
                    case 113:
                        keyCode = this.lastKeyCode;
                        break;
                }
                if (stage != null) {
                    if (keyCode == KeyCode.ESCAPE) {
                        stage.setInAllowedEventHandler(false);
                    } else {
                        stage.setInAllowedEventHandler(true);
                    }
                }
                switch (this.type) {
                    case 111:
                        if (this.view.isInFullscreen() && stage != null && stage.getSavedFullScreenExitKey() != null && stage.getSavedFullScreenExitKey().match(keyEvent)) {
                            stage.exitFullScreen();
                        }
                        break;
                    case 112:
                    case 113:
                        if ((!this.view.isInFullscreen() || GlassViewEventHandler.this.checkFullScreenKeyEvent(this.type, this.key, this.chars, this.modifiers)) && GlassViewEventHandler.this.scene.sceneListener != null) {
                            GlassViewEventHandler.this.scene.sceneListener.keyEvent(keyEvent);
                            break;
                        }
                        break;
                    default:
                        if (QuantumToolkit.verbose) {
                            System.out.println("handleKeyEvent: unhandled type: " + this.type);
                            break;
                        }
                        break;
                }
            } finally {
                if (stage != null) {
                    stage.setInAllowedEventHandler(false);
                }
                if (PulseLogger.PULSE_LOGGING_ENABLED) {
                    PulseLogger.newInput(null);
                }
            }
        }
    }

    @Override // com.sun.glass.ui.View.EventHandler
    public void handleKeyEvent(View view, long time, int type, int key, char[] chars, int modifiers) {
        this.keyNotification.view = view;
        this.keyNotification.time = time;
        this.keyNotification.type = type;
        this.keyNotification.key = key;
        this.keyNotification.chars = chars;
        this.keyNotification.modifiers = modifiers;
        QuantumToolkit.runWithoutRenderLock(() -> {
            return (Void) AccessController.doPrivileged(this.keyNotification, this.scene.getAccessControlContext());
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static EventType<MouseEvent> mouseEventType(int glassType) {
        switch (glassType) {
            case 221:
                return MouseEvent.MOUSE_PRESSED;
            case 222:
                return MouseEvent.MOUSE_RELEASED;
            case 223:
                return MouseEvent.MOUSE_DRAGGED;
            case 224:
                return MouseEvent.MOUSE_MOVED;
            case 225:
                return MouseEvent.MOUSE_ENTERED;
            case 226:
                return MouseEvent.MOUSE_EXITED;
            case 227:
            default:
                if (QuantumToolkit.verbose) {
                    System.err.println("Unknown Glass mouse event type: " + glassType);
                    return null;
                }
                return null;
            case 228:
                throw new IllegalArgumentException("WHEEL event cannot be translated to MouseEvent, must be translated to ScrollEvent");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static MouseButton mouseEventButton(int glassButton) {
        switch (glassButton) {
            case 212:
                return MouseButton.PRIMARY;
            case 213:
                return MouseButton.SECONDARY;
            case 214:
                return MouseButton.MIDDLE;
            default:
                return MouseButton.NONE;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/GlassViewEventHandler$MouseEventNotification.class */
    private class MouseEventNotification implements PrivilegedAction<Void> {
        View view;
        long time;
        int type;
        int button;

        /* renamed from: x, reason: collision with root package name */
        int f11966x;

        /* renamed from: y, reason: collision with root package name */
        int f11967y;
        int xAbs;
        int yAbs;
        int modifiers;
        boolean isPopupTrigger;
        boolean isSynthesized;

        private MouseEventNotification() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public Void run2() {
            int buttonMask;
            double pScale;
            double sy;
            double sx;
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(GlassViewEventHandler.mouseEventType(this.type).toString());
            }
            switch (this.button) {
                case 212:
                    buttonMask = 32;
                    break;
                case 213:
                    buttonMask = 64;
                    break;
                case 214:
                    buttonMask = 128;
                    break;
                default:
                    buttonMask = 0;
                    break;
            }
            switch (this.type) {
                case 221:
                    GlassViewEventHandler.this.mouseButtonPressedMask |= buttonMask;
                    break;
                case 222:
                    if ((GlassViewEventHandler.this.mouseButtonPressedMask & buttonMask) != 0) {
                        GlassViewEventHandler.this.mouseButtonPressedMask &= buttonMask ^ (-1);
                        break;
                    } else {
                        return null;
                    }
                case 223:
                default:
                    if (QuantumToolkit.verbose) {
                        System.out.println("handleMouseEvent: unhandled type: " + this.type);
                        break;
                    }
                    break;
                case 224:
                    if (this.button != 211) {
                        return null;
                    }
                    break;
                case 225:
                case 226:
                    break;
                case 227:
                    return null;
            }
            WindowStage stage = GlassViewEventHandler.this.scene.getWindowStage();
            if (stage != null) {
                try {
                    switch (this.type) {
                        case 221:
                        case 222:
                            stage.setInAllowedEventHandler(true);
                            break;
                        default:
                            stage.setInAllowedEventHandler(false);
                            break;
                    }
                } finally {
                    if (stage != null) {
                        stage.setInAllowedEventHandler(false);
                    }
                    if (PulseLogger.PULSE_LOGGING_ENABLED) {
                        PulseLogger.newInput(null);
                    }
                }
            }
            if (GlassViewEventHandler.this.scene.sceneListener != null) {
                boolean shiftDown = (this.modifiers & 1) != 0;
                boolean controlDown = (this.modifiers & 4) != 0;
                boolean altDown = (this.modifiers & 8) != 0;
                boolean metaDown = (this.modifiers & 16) != 0;
                boolean primaryButtonDown = (this.modifiers & 32) != 0;
                boolean middleButtonDown = (this.modifiers & 128) != 0;
                boolean secondaryButtonDown = (this.modifiers & 64) != 0;
                Window w2 = this.view.getWindow();
                if (w2 != null) {
                    pScale = w2.getPlatformScale();
                    Screen scr = w2.getScreen();
                    if (scr != null) {
                        sx = scr.getX();
                        sy = scr.getY();
                    } else {
                        sy = 0.0d;
                        sx = 0.0d;
                    }
                } else {
                    pScale = 1.0d;
                    sy = 0.0d;
                    sx = 0.0d;
                }
                GlassViewEventHandler.this.scene.sceneListener.mouseEvent(GlassViewEventHandler.mouseEventType(this.type), this.f11966x / pScale, this.f11967y / pScale, sx + ((this.xAbs - sx) / pScale), sy + ((this.yAbs - sy) / pScale), GlassViewEventHandler.mouseEventButton(this.button), this.isPopupTrigger, this.isSynthesized, shiftDown, controlDown, altDown, metaDown, primaryButtonDown, middleButtonDown, secondaryButtonDown);
            }
        }
    }

    @Override // com.sun.glass.ui.View.EventHandler
    public void handleMouseEvent(View view, long time, int type, int button, int x2, int y2, int xAbs, int yAbs, int modifiers, boolean isPopupTrigger, boolean isSynthesized) {
        this.mouseNotification.view = view;
        this.mouseNotification.time = time;
        this.mouseNotification.type = type;
        this.mouseNotification.button = button;
        this.mouseNotification.f11966x = x2;
        this.mouseNotification.f11967y = y2;
        this.mouseNotification.xAbs = xAbs;
        this.mouseNotification.yAbs = yAbs;
        this.mouseNotification.modifiers = modifiers;
        this.mouseNotification.isPopupTrigger = isPopupTrigger;
        this.mouseNotification.isSynthesized = isSynthesized;
        QuantumToolkit.runWithoutRenderLock(() -> {
            return (Void) AccessController.doPrivileged(this.mouseNotification, this.scene.getAccessControlContext());
        });
    }

    @Override // com.sun.glass.ui.View.EventHandler
    public void handleMenuEvent(View view, int x2, int y2, int xAbs, int yAbs, boolean isKeyboardTrigger) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("MENU_EVENT");
        }
        WindowStage stage = this.scene.getWindowStage();
        if (stage != null) {
            try {
                stage.setInAllowedEventHandler(true);
            } catch (Throwable th) {
                if (stage != null) {
                    stage.setInAllowedEventHandler(false);
                }
                if (PulseLogger.PULSE_LOGGING_ENABLED) {
                    PulseLogger.newInput(null);
                }
                throw th;
            }
        }
        QuantumToolkit.runWithoutRenderLock(() -> {
            return (Void) AccessController.doPrivileged(() -> {
                double pScale;
                double sy;
                double sx;
                if (this.scene.sceneListener != null) {
                    Window w2 = view.getWindow();
                    if (w2 != null) {
                        pScale = w2.getPlatformScale();
                        Screen scr = w2.getScreen();
                        if (scr != null) {
                            sx = scr.getX();
                            sy = scr.getY();
                        } else {
                            sy = 0.0d;
                            sx = 0.0d;
                        }
                    } else {
                        pScale = 1.0d;
                        sy = 0.0d;
                        sx = 0.0d;
                    }
                    this.scene.sceneListener.menuEvent(x2 / pScale, y2 / pScale, sx + ((xAbs - sx) / pScale), sy + ((yAbs - sy) / pScale), isKeyboardTrigger);
                    return null;
                }
                return null;
            }, this.scene.getAccessControlContext());
        });
        if (stage != null) {
            stage.setInAllowedEventHandler(false);
        }
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput(null);
        }
    }

    @Override // com.sun.glass.ui.View.EventHandler
    public void handleScrollEvent(View view, long time, int x2, int y2, int xAbs, int yAbs, double deltaX, double deltaY, int modifiers, int lines, int chars, int defaultLines, int defaultChars, double xMultiplier, double yMultiplier) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("SCROLL_EVENT");
        }
        WindowStage stage = this.scene.getWindowStage();
        if (stage != null) {
            try {
                stage.setInAllowedEventHandler(false);
            } catch (Throwable th) {
                if (stage != null) {
                    stage.setInAllowedEventHandler(false);
                }
                if (PulseLogger.PULSE_LOGGING_ENABLED) {
                    PulseLogger.newInput(null);
                }
                throw th;
            }
        }
        QuantumToolkit.runWithoutRenderLock(() -> {
            return (Void) AccessController.doPrivileged(() -> {
                double pScale;
                double sy;
                double sx;
                if (this.scene.sceneListener != null) {
                    Window w2 = view.getWindow();
                    if (w2 != null) {
                        pScale = w2.getPlatformScale();
                        Screen scr = w2.getScreen();
                        if (scr != null) {
                            sx = scr.getX();
                            sy = scr.getY();
                        } else {
                            sy = 0.0d;
                            sx = 0.0d;
                        }
                    } else {
                        pScale = 1.0d;
                        sy = 0.0d;
                        sx = 0.0d;
                    }
                    this.scene.sceneListener.scrollEvent(ScrollEvent.SCROLL, deltaX / pScale, deltaY / pScale, 0.0d, 0.0d, xMultiplier, yMultiplier, 0, chars, lines, defaultChars, defaultLines, x2 / pScale, y2 / pScale, sx + ((xAbs - sx) / pScale), sy + ((yAbs - sy) / pScale), (modifiers & 1) != 0, (modifiers & 4) != 0, (modifiers & 8) != 0, (modifiers & 16) != 0, false, false);
                    return null;
                }
                return null;
            }, this.scene.getAccessControlContext());
        });
        if (stage != null) {
            stage.setInAllowedEventHandler(false);
        }
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput(null);
        }
    }

    private static byte inputMethodEventAttrValue(int pos, int[] attrBoundary, byte[] attrValue) {
        if (attrBoundary != null) {
            for (int current = 0; current < attrBoundary.length - 1; current++) {
                if (pos >= attrBoundary[current] && pos < attrBoundary[current + 1]) {
                    return attrValue[current];
                }
            }
            return (byte) 4;
        }
        return (byte) 4;
    }

    private static ObservableList<InputMethodTextRun> inputMethodEventComposed(String text, int commitCount, int[] clauseBoundary, int[] attrBoundary, byte[] attrValue) {
        InputMethodHighlight highlight;
        ObservableList<InputMethodTextRun> composed = new TrackableObservableList<InputMethodTextRun>() { // from class: com.sun.javafx.tk.quantum.GlassViewEventHandler.1
            @Override // com.sun.javafx.collections.TrackableObservableList
            protected void onChanged(ListChangeListener.Change<InputMethodTextRun> c2) {
            }
        };
        if (commitCount < text.length()) {
            if (clauseBoundary == null) {
                composed.add(new InputMethodTextRun(text.substring(commitCount), InputMethodHighlight.UNSELECTED_RAW));
            } else {
                for (int current = 0; current < clauseBoundary.length - 1; current++) {
                    if (clauseBoundary[current] >= commitCount) {
                        switch (inputMethodEventAttrValue(clauseBoundary[current], attrBoundary, attrValue)) {
                            case 0:
                            case 4:
                            default:
                                highlight = InputMethodHighlight.UNSELECTED_RAW;
                                break;
                            case 1:
                                highlight = InputMethodHighlight.SELECTED_CONVERTED;
                                break;
                            case 2:
                                highlight = InputMethodHighlight.UNSELECTED_CONVERTED;
                                break;
                            case 3:
                                highlight = InputMethodHighlight.SELECTED_RAW;
                                break;
                        }
                        composed.add(new InputMethodTextRun(text.substring(clauseBoundary[current], clauseBoundary[current + 1]), highlight));
                    }
                }
            }
        }
        return composed;
    }

    @Override // com.sun.glass.ui.View.EventHandler
    public void handleInputMethodEvent(long time, String text, int[] clauseBoundary, int[] attrBoundary, byte[] attrValue, int commitCount, int cursorPos) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("INPUT_METHOD_EVENT");
        }
        WindowStage stage = this.scene.getWindowStage();
        if (stage != null) {
            try {
                stage.setInAllowedEventHandler(true);
            } catch (Throwable th) {
                if (stage != null) {
                    stage.setInAllowedEventHandler(false);
                }
                if (PulseLogger.PULSE_LOGGING_ENABLED) {
                    PulseLogger.newInput(null);
                }
                throw th;
            }
        }
        QuantumToolkit.runWithoutRenderLock(() -> {
            return (Void) AccessController.doPrivileged(() -> {
                if (this.scene.sceneListener != null) {
                    String t2 = text != null ? text : "";
                    EventType<InputMethodEvent> eventType = InputMethodEvent.INPUT_METHOD_TEXT_CHANGED;
                    ObservableList<InputMethodTextRun> composed = inputMethodEventComposed(t2, commitCount, clauseBoundary, attrBoundary, attrValue);
                    String committed = t2.substring(0, commitCount);
                    this.scene.sceneListener.inputMethodEvent(eventType, composed, committed, cursorPos);
                    return null;
                }
                return null;
            }, this.scene.getAccessControlContext());
        });
        if (stage != null) {
            stage.setInAllowedEventHandler(false);
        }
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput(null);
        }
    }

    @Override // com.sun.glass.ui.View.EventHandler
    public double[] getInputMethodCandidatePos(int offset) {
        Point2D p2d = this.scene.inputMethodRequests.getTextLocation(offset);
        double[] ret = {p2d.getX(), p2d.getY()};
        return ret;
    }

    private static TransferMode actionToTransferMode(int dropActions) {
        if (dropActions == 0) {
            return null;
        }
        if (dropActions == 1 || dropActions == 1073741825) {
            return TransferMode.COPY;
        }
        if (dropActions == 2 || dropActions == 1073741826) {
            return TransferMode.MOVE;
        }
        if (dropActions == 1073741824) {
            return TransferMode.LINK;
        }
        if (dropActions == 3) {
            if (QuantumToolkit.verbose) {
                System.err.println("Ambiguous drop action: " + Integer.toHexString(dropActions));
                return null;
            }
            return null;
        }
        if (QuantumToolkit.verbose) {
            System.err.println("Unknown drop action: " + Integer.toHexString(dropActions));
            return null;
        }
        return null;
    }

    private static int transferModeToAction(TransferMode tm) {
        if (tm == null) {
            return 0;
        }
        switch (tm) {
            case COPY:
                return 1;
            case MOVE:
                return 2;
            case LINK:
                return 1073741824;
            default:
                return 0;
        }
    }

    @Override // com.sun.glass.ui.View.EventHandler
    public int handleDragEnter(View view, int x2, int y2, int xAbs, int yAbs, int recommendedDropAction, ClipboardAssistance dropTargetAssistant) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("DRAG_ENTER");
        }
        try {
            TransferMode action = (TransferMode) QuantumToolkit.runWithoutRenderLock(() -> {
                return this.dndHandler.handleDragEnter(x2, y2, xAbs, yAbs, actionToTransferMode(recommendedDropAction), dropTargetAssistant);
            });
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
            return transferModeToAction(action);
        } catch (Throwable th) {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
            throw th;
        }
    }

    @Override // com.sun.glass.ui.View.EventHandler
    public void handleDragLeave(View view, ClipboardAssistance dropTargetAssistant) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("DRAG_LEAVE");
        }
        try {
            QuantumToolkit.runWithoutRenderLock(() -> {
                this.dndHandler.handleDragLeave(dropTargetAssistant);
                return null;
            });
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        } catch (Throwable th) {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
            throw th;
        }
    }

    @Override // com.sun.glass.ui.View.EventHandler
    public int handleDragDrop(View view, int x2, int y2, int xAbs, int yAbs, int recommendedDropAction, ClipboardAssistance dropTargetAssistant) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("DRAG_DROP");
        }
        try {
            TransferMode action = (TransferMode) QuantumToolkit.runWithoutRenderLock(() -> {
                return this.dndHandler.handleDragDrop(x2, y2, xAbs, yAbs, actionToTransferMode(recommendedDropAction), dropTargetAssistant);
            });
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
            return transferModeToAction(action);
        } catch (Throwable th) {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
            throw th;
        }
    }

    @Override // com.sun.glass.ui.View.EventHandler
    public int handleDragOver(View view, int x2, int y2, int xAbs, int yAbs, int recommendedDropAction, ClipboardAssistance dropTargetAssistant) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("DRAG_OVER");
        }
        try {
            TransferMode action = (TransferMode) QuantumToolkit.runWithoutRenderLock(() -> {
                return this.dndHandler.handleDragOver(x2, y2, xAbs, yAbs, actionToTransferMode(recommendedDropAction), dropTargetAssistant);
            });
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
            return transferModeToAction(action);
        } catch (Throwable th) {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
            throw th;
        }
    }

    @Override // com.sun.glass.ui.View.EventHandler
    public void handleDragStart(View view, int button, int x2, int y2, int xAbs, int yAbs, ClipboardAssistance assistant) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("DRAG_START");
        }
        this.dropSourceAssistant = assistant;
        try {
            QuantumToolkit.runWithoutRenderLock(() -> {
                this.dndHandler.handleDragStart(button, x2, y2, xAbs, yAbs, assistant);
                return null;
            });
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        } catch (Throwable th) {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
            throw th;
        }
    }

    @Override // com.sun.glass.ui.View.EventHandler
    public void handleDragEnd(View view, int performedAction) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("DRAG_END");
        }
        try {
            QuantumToolkit.runWithoutRenderLock(() -> {
                this.dndHandler.handleDragEnd(actionToTransferMode(performedAction), this.dropSourceAssistant);
                return null;
            });
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        } catch (Throwable th) {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
            throw th;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/GlassViewEventHandler$ViewEventNotification.class */
    private class ViewEventNotification implements PrivilegedAction<Void> {
        View view;
        long time;
        int type;

        private ViewEventNotification() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public Void run2() {
            WindowStage stage;
            WindowStage stage2;
            if (GlassViewEventHandler.this.scene.sceneListener == null) {
                return null;
            }
            switch (this.type) {
                case 411:
                case 412:
                    return null;
                case 413:
                case 414:
                case 415:
                case 416:
                case 417:
                case 418:
                case 419:
                case NNTPReply.NO_CURRENT_ARTICLE_SELECTED /* 420 */:
                case 424:
                case FTPReply.CANNOT_OPEN_DATA_CONNECTION /* 425 */:
                case FTPReply.TRANSFER_ABORTED /* 426 */:
                case 427:
                case 428:
                case 429:
                case NNTPReply.NO_SUCH_ARTICLE_FOUND /* 430 */:
                default:
                    throw new RuntimeException("handleViewEvent: unhandled type: " + this.type);
                case 421:
                    Window w2 = this.view.getWindow();
                    if (w2 == null || w2.getMinimumWidth() != this.view.getWidth() || w2.isVisible()) {
                        if (QuantumToolkit.drawInPaint && w2 != null && w2.isVisible() && (stage2 = GlassViewEventHandler.this.scene.getWindowStage()) != null && !stage2.isApplet()) {
                            GlassViewEventHandler.this.collector.liveRepaintRenderJob(GlassViewEventHandler.this.scene);
                        }
                        GlassViewEventHandler.this.scene.entireSceneNeedsRepaint();
                        return null;
                    }
                    return null;
                case 422:
                    Window w3 = this.view.getWindow();
                    float pScale = w3 == null ? 1.0f : w3.getPlatformScale();
                    GlassViewEventHandler.this.scene.sceneListener.changedSize(this.view.getWidth() / pScale, this.view.getHeight() / pScale);
                    GlassViewEventHandler.this.scene.entireSceneNeedsRepaint();
                    QuantumToolkit.runWithRenderLock(() -> {
                        GlassViewEventHandler.this.scene.updateSceneState();
                        return null;
                    });
                    if (QuantumToolkit.liveResize && w3 != null && w3.isVisible() && (stage = GlassViewEventHandler.this.scene.getWindowStage()) != null && !stage.isApplet()) {
                        GlassViewEventHandler.this.collector.liveRepaintRenderJob(GlassViewEventHandler.this.scene);
                        return null;
                    }
                    return null;
                case 423:
                    Window w4 = this.view.getWindow();
                    float pScale2 = w4 == null ? 1.0f : w4.getPlatformScale();
                    GlassViewEventHandler.this.scene.sceneListener.changedLocation(this.view.getX() / pScale2, this.view.getY() / pScale2);
                    return null;
                case 431:
                case ViewEvent.FULLSCREEN_EXIT /* 432 */:
                    if (GlassViewEventHandler.this.scene.getWindowStage() != null) {
                        GlassViewEventHandler.this.scene.getWindowStage().fullscreenChanged(this.type == 431);
                        return null;
                    }
                    return null;
            }
        }
    }

    @Override // com.sun.glass.ui.View.EventHandler
    public void handleViewEvent(View view, long time, int type) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("VIEW_EVENT: " + ViewEvent.getTypeString(type));
        }
        this.viewNotification.view = view;
        this.viewNotification.time = time;
        this.viewNotification.type = type;
        try {
            QuantumToolkit.runWithoutRenderLock(() -> {
                return (Void) AccessController.doPrivileged(this.viewNotification, this.scene.getAccessControlContext());
            });
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        } catch (Throwable th) {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
            throw th;
        }
    }

    @Override // com.sun.glass.ui.View.EventHandler
    public void handleScrollGestureEvent(View view, long time, int type, int modifiers, boolean isDirect, boolean isInertia, int touchCount, int x2, int y2, int xAbs, int yAbs, double dx, double dy, double totaldx, double totaldy, double multiplierX, double multiplierY) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("SCROLL_GESTURE_EVENT");
        }
        WindowStage stage = this.scene.getWindowStage();
        if (stage != null) {
            try {
                stage.setInAllowedEventHandler(false);
            } catch (Throwable th) {
                if (stage != null) {
                    stage.setInAllowedEventHandler(false);
                }
                if (PulseLogger.PULSE_LOGGING_ENABLED) {
                    PulseLogger.newInput(null);
                }
                throw th;
            }
        }
        QuantumToolkit.runWithoutRenderLock(() -> {
            return (Void) AccessController.doPrivileged(() -> {
                EventType<ScrollEvent> eventType;
                double pScale;
                double sy;
                double sx;
                if (this.scene.sceneListener != null) {
                    switch (type) {
                        case 1:
                            eventType = ScrollEvent.SCROLL_STARTED;
                            break;
                        case 2:
                            eventType = ScrollEvent.SCROLL;
                            break;
                        case 3:
                            eventType = ScrollEvent.SCROLL_FINISHED;
                            break;
                        default:
                            throw new RuntimeException("Unknown scroll event type: " + type);
                    }
                    Window w2 = view.getWindow();
                    if (w2 != null) {
                        pScale = w2.getPlatformScale();
                        Screen scr = w2.getScreen();
                        if (scr != null) {
                            sx = scr.getX();
                            sy = scr.getY();
                        } else {
                            sy = 0.0d;
                            sx = 0.0d;
                        }
                    } else {
                        pScale = 1.0d;
                        sy = 0.0d;
                        sx = 0.0d;
                    }
                    this.scene.sceneListener.scrollEvent(eventType, dx / pScale, dy / pScale, totaldx / pScale, totaldy / pScale, multiplierX, multiplierY, touchCount, 0, 0, 0, 0, x2 == Integer.MAX_VALUE ? Double.NaN : x2 / pScale, y2 == Integer.MAX_VALUE ? Double.NaN : y2 / pScale, xAbs == Integer.MAX_VALUE ? Double.NaN : sx + ((xAbs - sx) / pScale), yAbs == Integer.MAX_VALUE ? Double.NaN : sy + ((yAbs - sy) / pScale), (modifiers & 1) != 0, (modifiers & 4) != 0, (modifiers & 8) != 0, (modifiers & 16) != 0, isDirect, isInertia);
                    return null;
                }
                return null;
            }, this.scene.getAccessControlContext());
        });
        if (stage != null) {
            stage.setInAllowedEventHandler(false);
        }
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput(null);
        }
    }

    @Override // com.sun.glass.ui.View.EventHandler
    public void handleZoomGestureEvent(View view, long time, int type, int modifiers, boolean isDirect, boolean isInertia, int originx, int originy, int originxAbs, int originyAbs, double scale, double expansion, double totalscale, double totalexpansion) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("ZOOM_GESTURE_EVENT");
        }
        WindowStage stage = this.scene.getWindowStage();
        if (stage != null) {
            try {
                stage.setInAllowedEventHandler(false);
            } catch (Throwable th) {
                if (stage != null) {
                    stage.setInAllowedEventHandler(false);
                }
                if (PulseLogger.PULSE_LOGGING_ENABLED) {
                    PulseLogger.newInput(null);
                }
                throw th;
            }
        }
        QuantumToolkit.runWithoutRenderLock(() -> {
            return (Void) AccessController.doPrivileged(() -> {
                EventType<ZoomEvent> eventType;
                double pScale;
                double sy;
                double sx;
                if (this.scene.sceneListener != null) {
                    switch (type) {
                        case 1:
                            eventType = ZoomEvent.ZOOM_STARTED;
                            break;
                        case 2:
                            eventType = ZoomEvent.ZOOM;
                            break;
                        case 3:
                            eventType = ZoomEvent.ZOOM_FINISHED;
                            break;
                        default:
                            throw new RuntimeException("Unknown scroll event type: " + type);
                    }
                    Window w2 = view.getWindow();
                    if (w2 != null) {
                        pScale = w2.getPlatformScale();
                        Screen scr = w2.getScreen();
                        if (scr != null) {
                            sx = scr.getX();
                            sy = scr.getY();
                        } else {
                            sy = 0.0d;
                            sx = 0.0d;
                        }
                    } else {
                        pScale = 1.0d;
                        sy = 0.0d;
                        sx = 0.0d;
                    }
                    this.scene.sceneListener.zoomEvent(eventType, scale, totalscale, originx == Integer.MAX_VALUE ? Double.NaN : originx / pScale, originy == Integer.MAX_VALUE ? Double.NaN : originy / pScale, originxAbs == Integer.MAX_VALUE ? Double.NaN : sx + ((originxAbs - sx) / pScale), originyAbs == Integer.MAX_VALUE ? Double.NaN : sy + ((originyAbs - sy) / pScale), (modifiers & 1) != 0, (modifiers & 4) != 0, (modifiers & 8) != 0, (modifiers & 16) != 0, isDirect, isInertia);
                    return null;
                }
                return null;
            }, this.scene.getAccessControlContext());
        });
        if (stage != null) {
            stage.setInAllowedEventHandler(false);
        }
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput(null);
        }
    }

    @Override // com.sun.glass.ui.View.EventHandler
    public void handleRotateGestureEvent(View view, long time, int type, int modifiers, boolean isDirect, boolean isInertia, int originx, int originy, int originxAbs, int originyAbs, double dangle, double totalangle) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("ROTATE_GESTURE_EVENT");
        }
        WindowStage stage = this.scene.getWindowStage();
        if (stage != null) {
            try {
                stage.setInAllowedEventHandler(false);
            } catch (Throwable th) {
                if (stage != null) {
                    stage.setInAllowedEventHandler(false);
                }
                if (PulseLogger.PULSE_LOGGING_ENABLED) {
                    PulseLogger.newInput(null);
                }
                throw th;
            }
        }
        QuantumToolkit.runWithoutRenderLock(() -> {
            return (Void) AccessController.doPrivileged(() -> {
                EventType<RotateEvent> eventType;
                double pScale;
                double sy;
                double sx;
                if (this.scene.sceneListener != null) {
                    switch (type) {
                        case 1:
                            eventType = RotateEvent.ROTATION_STARTED;
                            break;
                        case 2:
                            eventType = RotateEvent.ROTATE;
                            break;
                        case 3:
                            eventType = RotateEvent.ROTATION_FINISHED;
                            break;
                        default:
                            throw new RuntimeException("Unknown scroll event type: " + type);
                    }
                    Window w2 = view.getWindow();
                    if (w2 != null) {
                        pScale = w2.getPlatformScale();
                        Screen scr = w2.getScreen();
                        if (scr != null) {
                            sx = scr.getX();
                            sy = scr.getY();
                        } else {
                            sy = 0.0d;
                            sx = 0.0d;
                        }
                    } else {
                        pScale = 1.0d;
                        sy = 0.0d;
                        sx = 0.0d;
                    }
                    this.scene.sceneListener.rotateEvent(eventType, dangle, totalangle, originx == Integer.MAX_VALUE ? Double.NaN : originx / pScale, originy == Integer.MAX_VALUE ? Double.NaN : originy / pScale, originxAbs == Integer.MAX_VALUE ? Double.NaN : sx + ((originxAbs - sx) / pScale), originyAbs == Integer.MAX_VALUE ? Double.NaN : sy + ((originyAbs - sy) / pScale), (modifiers & 1) != 0, (modifiers & 4) != 0, (modifiers & 8) != 0, (modifiers & 16) != 0, isDirect, isInertia);
                    return null;
                }
                return null;
            }, this.scene.getAccessControlContext());
        });
        if (stage != null) {
            stage.setInAllowedEventHandler(false);
        }
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput(null);
        }
    }

    @Override // com.sun.glass.ui.View.EventHandler
    public void handleSwipeGestureEvent(View view, long time, int type, int modifiers, boolean isDirect, boolean isInertia, int touchCount, int dir, int x2, int y2, int xAbs, int yAbs) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("SWIPE_EVENT");
        }
        WindowStage stage = this.scene.getWindowStage();
        if (stage != null) {
            try {
                stage.setInAllowedEventHandler(false);
            } catch (Throwable th) {
                if (stage != null) {
                    stage.setInAllowedEventHandler(false);
                }
                if (PulseLogger.PULSE_LOGGING_ENABLED) {
                    PulseLogger.newInput(null);
                }
                throw th;
            }
        }
        QuantumToolkit.runWithoutRenderLock(() -> {
            return (Void) AccessController.doPrivileged(() -> {
                EventType<SwipeEvent> eventType;
                double pScale;
                double sy;
                double sx;
                if (this.scene.sceneListener != null) {
                    switch (dir) {
                        case 1:
                            eventType = SwipeEvent.SWIPE_UP;
                            break;
                        case 2:
                            eventType = SwipeEvent.SWIPE_DOWN;
                            break;
                        case 3:
                            eventType = SwipeEvent.SWIPE_LEFT;
                            break;
                        case 4:
                            eventType = SwipeEvent.SWIPE_RIGHT;
                            break;
                        default:
                            throw new RuntimeException("Unknown swipe event direction: " + dir);
                    }
                    Window w2 = view.getWindow();
                    if (w2 != null) {
                        pScale = w2.getPlatformScale();
                        Screen scr = w2.getScreen();
                        if (scr != null) {
                            sx = scr.getX();
                            sy = scr.getY();
                        } else {
                            sy = 0.0d;
                            sx = 0.0d;
                        }
                    } else {
                        pScale = 1.0d;
                        sy = 0.0d;
                        sx = 0.0d;
                    }
                    this.scene.sceneListener.swipeEvent(eventType, touchCount, x2 == Integer.MAX_VALUE ? Double.NaN : x2 / pScale, y2 == Integer.MAX_VALUE ? Double.NaN : y2 / pScale, xAbs == Integer.MAX_VALUE ? Double.NaN : sx + ((xAbs - sx) / pScale), yAbs == Integer.MAX_VALUE ? Double.NaN : sy + ((yAbs - sy) / pScale), (modifiers & 1) != 0, (modifiers & 4) != 0, (modifiers & 8) != 0, (modifiers & 16) != 0, isDirect);
                    return null;
                }
                return null;
            }, this.scene.getAccessControlContext());
        });
        if (stage != null) {
            stage.setInAllowedEventHandler(false);
        }
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput(null);
        }
    }

    @Override // com.sun.glass.ui.View.EventHandler
    public void handleBeginTouchEvent(View view, long time, int modifiers, boolean isDirect, int touchEventCount) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("BEGIN_TOUCH_EVENT");
        }
        WindowStage stage = this.scene.getWindowStage();
        if (stage != null) {
            try {
                stage.setInAllowedEventHandler(true);
            } catch (Throwable th) {
                if (stage != null) {
                    stage.setInAllowedEventHandler(false);
                }
                if (PulseLogger.PULSE_LOGGING_ENABLED) {
                    PulseLogger.newInput(null);
                }
                throw th;
            }
        }
        QuantumToolkit.runWithoutRenderLock(() -> {
            return (Void) AccessController.doPrivileged(() -> {
                if (this.scene.sceneListener != null) {
                    this.scene.sceneListener.touchEventBegin(time, touchEventCount, isDirect, (modifiers & 1) != 0, (modifiers & 4) != 0, (modifiers & 8) != 0, (modifiers & 16) != 0);
                    return null;
                }
                return null;
            }, this.scene.getAccessControlContext());
        });
        if (stage != null) {
            stage.setInAllowedEventHandler(false);
        }
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput(null);
        }
        this.gestures.notifyBeginTouchEvent(time, modifiers, isDirect, touchEventCount);
    }

    @Override // com.sun.glass.ui.View.EventHandler
    public void handleNextTouchEvent(View view, long time, int type, long touchId, int x2, int y2, int xAbs, int yAbs) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("NEXT_TOUCH_EVENT");
        }
        WindowStage stage = this.scene.getWindowStage();
        if (stage != null) {
            try {
                stage.setInAllowedEventHandler(true);
            } catch (Throwable th) {
                if (stage != null) {
                    stage.setInAllowedEventHandler(false);
                }
                if (PulseLogger.PULSE_LOGGING_ENABLED) {
                    PulseLogger.newInput(null);
                }
                throw th;
            }
        }
        QuantumToolkit.runWithoutRenderLock(() -> {
            return (Void) AccessController.doPrivileged(() -> {
                TouchPoint.State state;
                double pScale;
                double sy;
                double sx;
                if (this.scene.sceneListener != null) {
                    switch (type) {
                        case TouchEvent.TOUCH_PRESSED /* 811 */:
                            state = TouchPoint.State.PRESSED;
                            break;
                        case TouchEvent.TOUCH_MOVED /* 812 */:
                            state = TouchPoint.State.MOVED;
                            break;
                        case TouchEvent.TOUCH_RELEASED /* 813 */:
                            state = TouchPoint.State.RELEASED;
                            break;
                        case TouchEvent.TOUCH_STILL /* 814 */:
                            state = TouchPoint.State.STATIONARY;
                            break;
                        default:
                            throw new RuntimeException("Unknown touch state: " + type);
                    }
                    Window w2 = view.getWindow();
                    if (w2 != null) {
                        pScale = w2.getPlatformScale();
                        Screen scr = w2.getScreen();
                        if (scr != null) {
                            sx = scr.getX();
                            sy = scr.getY();
                        } else {
                            sy = 0.0d;
                            sx = 0.0d;
                        }
                    } else {
                        pScale = 1.0d;
                        sy = 0.0d;
                        sx = 0.0d;
                    }
                    this.scene.sceneListener.touchEventNext(state, touchId, x2 / pScale, y2 / pScale, sx + ((xAbs - sx) / pScale), sy + ((yAbs - sy) / pScale));
                    return null;
                }
                return null;
            }, this.scene.getAccessControlContext());
        });
        if (stage != null) {
            stage.setInAllowedEventHandler(false);
        }
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput(null);
        }
        this.gestures.notifyNextTouchEvent(time, type, touchId, x2, y2, xAbs, yAbs);
    }

    @Override // com.sun.glass.ui.View.EventHandler
    public void handleEndTouchEvent(View view, long time) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("END_TOUCH_EVENT");
        }
        WindowStage stage = this.scene.getWindowStage();
        if (stage != null) {
            try {
                stage.setInAllowedEventHandler(true);
            } catch (Throwable th) {
                if (stage != null) {
                    stage.setInAllowedEventHandler(false);
                }
                if (PulseLogger.PULSE_LOGGING_ENABLED) {
                    PulseLogger.newInput(null);
                }
                throw th;
            }
        }
        QuantumToolkit.runWithoutRenderLock(() -> {
            return (Void) AccessController.doPrivileged(() -> {
                if (this.scene.sceneListener != null) {
                    this.scene.sceneListener.touchEventEnd();
                    return null;
                }
                return null;
            }, this.scene.getAccessControlContext());
        });
        if (stage != null) {
            stage.setInAllowedEventHandler(false);
        }
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput(null);
        }
        this.gestures.notifyEndTouchEvent(time);
    }

    @Override // com.sun.glass.ui.View.EventHandler
    public Accessible getSceneAccessible() {
        if (this.scene != null && this.scene.sceneListener != null) {
            return this.scene.sceneListener.getSceneAccessible();
        }
        return null;
    }
}
