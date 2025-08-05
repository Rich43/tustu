package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Pixels;
import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.embed.AbstractEvents;
import com.sun.javafx.embed.EmbeddedSceneDTInterface;
import com.sun.javafx.embed.EmbeddedSceneInterface;
import com.sun.javafx.embed.HostDragStartListener;
import com.sun.javafx.embed.HostInterface;
import com.sun.javafx.scene.input.KeyCodeMap;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.TKClipboard;
import com.sun.javafx.tk.Toolkit;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;
import java.nio.IntBuffer;
import java.security.AccessController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.input.InputMethodTextRun;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/EmbeddedScene.class */
final class EmbeddedScene extends GlassScene implements EmbeddedSceneInterface {
    private HostInterface host;
    private UploadingPainter painter;
    private PaintRenderJob paintRenderJob;
    private float renderScale;
    private final EmbeddedSceneDnD embeddedDnD;
    private volatile IntBuffer texBits;
    private volatile int texLineStride;
    private volatile float texScaleFactor;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !EmbeddedScene.class.desiredAssertionStatus();
    }

    public EmbeddedScene(HostInterface host, boolean depthBuffer, boolean msaa) {
        super(depthBuffer, msaa);
        this.texScaleFactor = 1.0f;
        this.sceneState = new EmbeddedState(this);
        this.host = host;
        this.embeddedDnD = new EmbeddedSceneDnD(this);
        PaintCollector collector = PaintCollector.getInstance();
        this.painter = new UploadingPainter(this);
        this.paintRenderJob = new PaintRenderJob(this, collector.getRendered(), this.painter);
    }

    @Override // com.sun.javafx.tk.quantum.GlassScene, com.sun.javafx.tk.TKScene
    public void dispose() {
        if (!$assertionsDisabled && this.host == null) {
            throw new AssertionError();
        }
        QuantumToolkit.runWithRenderLock(() -> {
            this.host.setEmbeddedScene(null);
            this.host = null;
            updateSceneState();
            this.painter = null;
            this.paintRenderJob = null;
            this.texBits = null;
            return null;
        });
        super.dispose();
    }

    @Override // com.sun.javafx.tk.quantum.GlassScene
    void setStage(GlassStage stage) {
        super.setStage(stage);
        if (!$assertionsDisabled && this.host == null) {
            throw new AssertionError();
        }
        this.host.setEmbeddedScene(stage != null ? this : null);
    }

    @Override // com.sun.javafx.tk.quantum.GlassScene
    protected boolean isSynchronous() {
        return false;
    }

    @Override // com.sun.javafx.tk.quantum.GlassScene, com.sun.javafx.tk.TKScene
    public void setRoot(NGNode root) {
        super.setRoot(root);
        this.painter.setRoot(root);
    }

    @Override // com.sun.javafx.tk.quantum.GlassScene, com.sun.javafx.tk.TKScene
    public TKClipboard createDragboard(boolean isDragSource) {
        return this.embeddedDnD.createDragboard(isDragSource);
    }

    @Override // com.sun.javafx.tk.TKScene
    public void enableInputMethodEvents(boolean enable) {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedScene.enableInputMethodEvents " + enable);
        }
    }

    @Override // com.sun.javafx.tk.TKScene
    public void finishInputMethodComposition() {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedScene.finishInputMethodComposition");
        }
    }

    @Override // com.sun.javafx.embed.EmbeddedSceneInterface
    public void setPixelScaleFactor(float scale) {
        this.renderScale = scale;
        entireSceneNeedsRepaint();
    }

    public float getRenderScale() {
        return this.renderScale;
    }

    void uploadPixels(Pixels pixels) {
        this.texBits = (IntBuffer) pixels.getPixels();
        this.texLineStride = pixels.getWidthUnsafe();
        this.texScaleFactor = pixels.getScaleUnsafe();
        if (this.host != null) {
            this.host.repaint();
        }
    }

    @Override // com.sun.javafx.tk.quantum.GlassScene
    public void repaint() {
        Toolkit tk = Toolkit.getToolkit();
        tk.addRenderJob(this.paintRenderJob);
    }

    @Override // com.sun.javafx.embed.EmbeddedSceneInterface
    public boolean traverseOut(Direction dir) {
        if (dir == Direction.NEXT) {
            return this.host.traverseFocusOut(true);
        }
        if (dir == Direction.PREVIOUS) {
            return this.host.traverseFocusOut(false);
        }
        return false;
    }

    @Override // com.sun.javafx.embed.EmbeddedSceneInterface
    public void setSize(int width, int height) {
        Platform.runLater(() -> {
            AccessController.doPrivileged(() -> {
                if (this.sceneListener != null) {
                    this.sceneListener.changedSize(width, height);
                    return null;
                }
                return null;
            }, getAccessControlContext());
        });
    }

    @Override // com.sun.javafx.embed.EmbeddedSceneInterface
    public boolean getPixels(IntBuffer dest, int width, int height) {
        return ((Boolean) QuantumToolkit.runWithRenderLock(() -> {
            if (getRenderScale() != this.texScaleFactor || this.texBits == null) {
                return false;
            }
            int scaledWidth = Math.round(width * this.texScaleFactor);
            int scaledHeight = Math.round(height * this.texScaleFactor);
            dest.rewind();
            this.texBits.rewind();
            if (dest.capacity() != this.texBits.capacity()) {
                int w2 = Math.min(scaledWidth, this.texLineStride);
                int h2 = Math.min(scaledHeight, this.texBits.capacity() / this.texLineStride);
                int[] linebuf = new int[w2];
                for (int i2 = 0; i2 < h2; i2++) {
                    this.texBits.position(i2 * this.texLineStride);
                    this.texBits.get(linebuf, 0, w2);
                    dest.position(i2 * scaledWidth);
                    dest.put(linebuf);
                }
                return true;
            }
            dest.put(this.texBits);
            return true;
        })).booleanValue();
    }

    @Override // com.sun.javafx.tk.quantum.GlassScene
    protected Color getClearColor() {
        if (this.fillPaint != null && this.fillPaint.getType() == Paint.Type.COLOR && ((Color) this.fillPaint).getAlpha() == 0.0f) {
            return (Color) this.fillPaint;
        }
        return super.getClearColor();
    }

    @Override // com.sun.javafx.embed.EmbeddedSceneInterface
    public void mouseEvent(int type, int button, boolean primaryBtnDown, boolean middleBtnDown, boolean secondaryBtnDown, int x2, int y2, int xAbs, int yAbs, boolean shift, boolean ctrl, boolean alt, boolean meta, int wheelRotation, boolean popupTrigger) {
        Platform.runLater(() -> {
            AccessController.doPrivileged(() -> {
                if (this.sceneListener == null) {
                    return null;
                }
                if (!$assertionsDisabled && type == 2) {
                    throw new AssertionError();
                }
                if (type == 7) {
                    this.sceneListener.scrollEvent(ScrollEvent.SCROLL, 0.0d, -wheelRotation, 0.0d, 0.0d, 40.0d, 40.0d, 0, 0, 0, 0, 0, x2, y2, xAbs, yAbs, shift, ctrl, alt, meta, false, false);
                    return null;
                }
                EventType<MouseEvent> eventType = AbstractEvents.mouseIDToFXEventID(type);
                this.sceneListener.mouseEvent(eventType, x2, y2, xAbs, yAbs, AbstractEvents.mouseButtonToFXMouseButton(button), popupTrigger, false, shift, ctrl, alt, meta, primaryBtnDown, middleBtnDown, secondaryBtnDown);
                return null;
            }, getAccessControlContext());
        });
    }

    @Override // com.sun.javafx.embed.EmbeddedSceneInterface
    public void inputMethodEvent(EventType<InputMethodEvent> type, ObservableList<InputMethodTextRun> composed, String committed, int caretPosition) {
        Platform.runLater(() -> {
            AccessController.doPrivileged(() -> {
                if (this.sceneListener != null) {
                    this.sceneListener.inputMethodEvent(type, composed, committed, caretPosition);
                    return null;
                }
                return null;
            });
        });
    }

    @Override // com.sun.javafx.embed.EmbeddedSceneInterface
    public void menuEvent(int x2, int y2, int xAbs, int yAbs, boolean isKeyboardTrigger) {
        Platform.runLater(() -> {
            AccessController.doPrivileged(() -> {
                if (this.sceneListener != null) {
                    this.sceneListener.menuEvent(x2, y2, xAbs, yAbs, isKeyboardTrigger);
                    return null;
                }
                return null;
            }, getAccessControlContext());
        });
    }

    @Override // com.sun.javafx.embed.EmbeddedSceneInterface
    public void keyEvent(int type, int key, char[] ch, int modifiers) {
        Platform.runLater(() -> {
            AccessController.doPrivileged(() -> {
                if (this.sceneListener != null) {
                    boolean shiftDown = (modifiers & 1) != 0;
                    boolean controlDown = (modifiers & 2) != 0;
                    boolean altDown = (modifiers & 4) != 0;
                    boolean metaDown = (modifiers & 8) != 0;
                    String str = new String(ch);
                    KeyEvent keyEvent = new KeyEvent(AbstractEvents.keyIDToFXEventType(type), str, str, KeyCodeMap.valueOf(key), shiftDown, controlDown, altDown, metaDown);
                    this.sceneListener.keyEvent(keyEvent);
                    return null;
                }
                return null;
            }, getAccessControlContext());
        });
    }

    @Override // com.sun.javafx.tk.quantum.GlassScene, com.sun.javafx.tk.TKScene
    public void setCursor(Object cursor) {
        super.setCursor(cursor);
        this.host.setCursor((CursorFrame) cursor);
    }

    @Override // com.sun.javafx.embed.EmbeddedSceneInterface
    public void setDragStartListener(HostDragStartListener l2) {
        this.embeddedDnD.setDragStartListener(l2);
    }

    @Override // com.sun.javafx.embed.EmbeddedSceneInterface
    public EmbeddedSceneDTInterface createDropTarget() {
        return this.embeddedDnD.createDropTarget();
    }

    @Override // com.sun.javafx.embed.EmbeddedSceneInterface
    public InputMethodRequests getInputMethodRequests() {
        return this.inputMethodRequests;
    }
}
