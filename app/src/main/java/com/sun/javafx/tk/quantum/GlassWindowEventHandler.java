package com.sun.javafx.tk.quantum;

import com.sun.glass.events.WindowEvent;
import com.sun.glass.ui.Application;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.Window;
import com.sun.javafx.tk.FocusCause;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.apache.commons.net.ftp.FTPReply;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/GlassWindowEventHandler.class */
class GlassWindowEventHandler extends Window.EventHandler implements PrivilegedAction<Void> {
    private final WindowStage stage;
    private Window window;
    private int type;

    public GlassWindowEventHandler(WindowStage stage) {
        this.stage = stage;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.security.PrivilegedAction
    /* renamed from: run */
    public Void run2() {
        if (this.stage == null || this.stage.stageListener == null) {
            return null;
        }
        switch (this.type) {
            case WindowEvent.RESIZE /* 511 */:
                float pScale = this.window.getPlatformScale();
                this.stage.stageListener.changedSize(this.window.getWidth() / pScale, this.window.getHeight() / pScale);
                break;
            case 512:
                float pScale2 = this.window.getPlatformScale();
                Screen screen = this.window.getScreen();
                float sx = screen == null ? 0.0f : screen.getX();
                float sy = screen == null ? 0.0f : screen.getY();
                float wx = this.window.getX();
                float wy = this.window.getY();
                float newx = sx + ((wx - sx) / pScale2);
                float newy = sy + ((wy - sy) / pScale2);
                this.stage.stageListener.changedLocation(newx, newy);
                if (!Application.GetApplication().hasWindowManager()) {
                    QuantumToolkit.runWithRenderLock(() -> {
                        GlassScene scene = this.stage.getScene();
                        if (scene != null) {
                            scene.updateSceneState();
                            return null;
                        }
                        return null;
                    });
                    break;
                }
                break;
            case 513:
            case 514:
            case 515:
            case 516:
            case 517:
            case 518:
            case 519:
            case 520:
            case 523:
            case 524:
            case 525:
            case 526:
            case 527:
            case 528:
            case 529:
            case FTPReply.NOT_LOGGED_IN /* 530 */:
            case FTPReply.REQUEST_DENIED /* 534 */:
            case FTPReply.FAILED_SECURITY_CHECK /* 535 */:
            case FTPReply.REQUESTED_PROT_LEVEL_NOT_SUPPORTED /* 536 */:
            case 537:
            case 538:
            case 539:
            case 540:
            default:
                if (QuantumToolkit.verbose) {
                    System.err.println("GlassWindowEventHandler: unknown type: " + this.type);
                    break;
                }
                break;
            case 521:
                this.stage.stageListener.closing();
                break;
            case 522:
                this.stage.setPlatformWindowClosed();
                this.stage.stageListener.closed();
                break;
            case WindowEvent.MINIMIZE /* 531 */:
                this.stage.stageListener.changedIconified(true);
                break;
            case 532:
                this.stage.stageListener.changedIconified(false);
                this.stage.stageListener.changedMaximized(true);
                break;
            case 533:
                this.stage.stageListener.changedIconified(false);
                this.stage.stageListener.changedMaximized(false);
                break;
            case 541:
                this.stage.stageListener.changedFocused(false, FocusCause.DEACTIVATED);
                break;
            case WindowEvent.FOCUS_GAINED /* 542 */:
                WindowStage.addActiveWindow(this.stage);
                this.stage.stageListener.changedFocused(true, FocusCause.ACTIVATED);
                break;
            case WindowEvent.FOCUS_GAINED_FORWARD /* 543 */:
                WindowStage.addActiveWindow(this.stage);
                this.stage.stageListener.changedFocused(true, FocusCause.TRAVERSED_FORWARD);
                break;
            case 544:
                WindowStage.addActiveWindow(this.stage);
                this.stage.stageListener.changedFocused(true, FocusCause.TRAVERSED_BACKWARD);
                break;
            case WindowEvent.FOCUS_DISABLED /* 545 */:
                this.stage.handleFocusDisabled();
                break;
            case WindowEvent.FOCUS_UNGRAB /* 546 */:
                this.stage.stageListener.focusUngrab();
                break;
        }
        return null;
    }

    @Override // com.sun.glass.ui.Window.EventHandler
    public void handleLevelEvent(int level) {
        QuantumToolkit.runWithoutRenderLock(() -> {
            AccessControlContext acc = this.stage.getAccessControlContext();
            return (Void) AccessController.doPrivileged(() -> {
                this.stage.stageListener.changedAlwaysOnTop(level != 1);
                return (Void) null;
            }, acc);
        });
    }

    @Override // com.sun.glass.ui.Window.EventHandler
    public void handleWindowEvent(Window window, long time, int type) {
        this.window = window;
        this.type = type;
        QuantumToolkit.runWithoutRenderLock(() -> {
            AccessControlContext acc = this.stage.getAccessControlContext();
            return (Void) AccessController.doPrivileged(this, acc);
        });
    }

    @Override // com.sun.glass.ui.Window.EventHandler
    public void handleScreenChangedEvent(Window window, long time, Screen oldScreen, Screen newScreen) {
        GlassScene scene = this.stage.getScene();
        if (scene != null) {
            QuantumToolkit.runWithRenderLock(() -> {
                scene.entireSceneNeedsRepaint();
                scene.updateSceneState();
                return null;
            });
        }
        QuantumToolkit.runWithoutRenderLock(() -> {
            AccessControlContext acc = this.stage.getAccessControlContext();
            return (Void) AccessController.doPrivileged(() -> {
                this.stage.stageListener.changedScreen(oldScreen, newScreen);
                return (Void) null;
            }, acc);
        });
    }
}
