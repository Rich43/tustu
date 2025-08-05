package javafx.scene;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.NodeBuilder;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.RotateEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.transform.Transform;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/NodeBuilder.class */
public abstract class NodeBuilder<B extends NodeBuilder<B>> {
    BitSet __set = new BitSet();
    private BlendMode blendMode;
    private boolean cache;
    private CacheHint cacheHint;
    private Node clip;
    private Cursor cursor;
    private DepthTest depthTest;
    private boolean disable;
    private Effect effect;
    private EventDispatcher eventDispatcher;
    private boolean focusTraversable;
    private String id;
    private InputMethodRequests inputMethodRequests;
    private double layoutX;
    private double layoutY;
    private boolean managed;
    private boolean mouseTransparent;
    private EventHandler<? super ContextMenuEvent> onContextMenuRequested;
    private EventHandler<? super MouseEvent> onDragDetected;
    private EventHandler<? super DragEvent> onDragDone;
    private EventHandler<? super DragEvent> onDragDropped;
    private EventHandler<? super DragEvent> onDragEntered;
    private EventHandler<? super DragEvent> onDragExited;
    private EventHandler<? super DragEvent> onDragOver;
    private EventHandler<? super InputMethodEvent> onInputMethodTextChanged;
    private EventHandler<? super KeyEvent> onKeyPressed;
    private EventHandler<? super KeyEvent> onKeyReleased;
    private EventHandler<? super KeyEvent> onKeyTyped;
    private EventHandler<? super MouseEvent> onMouseClicked;
    private EventHandler<? super MouseDragEvent> onMouseDragEntered;
    private EventHandler<? super MouseDragEvent> onMouseDragExited;
    private EventHandler<? super MouseEvent> onMouseDragged;
    private EventHandler<? super MouseDragEvent> onMouseDragOver;
    private EventHandler<? super MouseDragEvent> onMouseDragReleased;
    private EventHandler<? super MouseEvent> onMouseEntered;
    private EventHandler<? super MouseEvent> onMouseExited;
    private EventHandler<? super MouseEvent> onMouseMoved;
    private EventHandler<? super MouseEvent> onMousePressed;
    private EventHandler<? super MouseEvent> onMouseReleased;
    private EventHandler<? super RotateEvent> onRotate;
    private EventHandler<? super RotateEvent> onRotationFinished;
    private EventHandler<? super RotateEvent> onRotationStarted;
    private EventHandler<? super ScrollEvent> onScroll;
    private EventHandler<? super ScrollEvent> onScrollFinished;
    private EventHandler<? super ScrollEvent> onScrollStarted;
    private EventHandler<? super SwipeEvent> onSwipeDown;
    private EventHandler<? super SwipeEvent> onSwipeLeft;
    private EventHandler<? super SwipeEvent> onSwipeRight;
    private EventHandler<? super SwipeEvent> onSwipeUp;
    private EventHandler<? super TouchEvent> onTouchMoved;
    private EventHandler<? super TouchEvent> onTouchPressed;
    private EventHandler<? super TouchEvent> onTouchReleased;
    private EventHandler<? super TouchEvent> onTouchStationary;
    private EventHandler<? super ZoomEvent> onZoom;
    private EventHandler<? super ZoomEvent> onZoomFinished;
    private EventHandler<? super ZoomEvent> onZoomStarted;
    private double opacity;
    private boolean pickOnBounds;
    private double rotate;
    private Point3D rotationAxis;
    private double scaleX;
    private double scaleY;
    private double scaleZ;
    private String style;
    private Collection<? extends String> styleClass;
    private Collection<? extends Transform> transforms;
    private double translateX;
    private double translateY;
    private double translateZ;
    private Object userData;
    private boolean visible;

    protected NodeBuilder() {
    }

    private void __set(int i2) {
        this.__set.set(i2);
    }

    public void applyTo(Node x2) {
        BitSet set = this.__set;
        int i2 = -1;
        while (true) {
            int iNextSetBit = set.nextSetBit(i2 + 1);
            i2 = iNextSetBit;
            if (iNextSetBit >= 0) {
                switch (i2) {
                    case 0:
                        x2.setBlendMode(this.blendMode);
                        break;
                    case 1:
                        x2.setCache(this.cache);
                        break;
                    case 2:
                        x2.setCacheHint(this.cacheHint);
                        break;
                    case 3:
                        x2.setClip(this.clip);
                        break;
                    case 4:
                        x2.setCursor(this.cursor);
                        break;
                    case 5:
                        x2.setDepthTest(this.depthTest);
                        break;
                    case 6:
                        x2.setDisable(this.disable);
                        break;
                    case 7:
                        x2.setEffect(this.effect);
                        break;
                    case 8:
                        x2.setEventDispatcher(this.eventDispatcher);
                        break;
                    case 9:
                        x2.setFocusTraversable(this.focusTraversable);
                        break;
                    case 10:
                        x2.setId(this.id);
                        break;
                    case 11:
                        x2.setInputMethodRequests(this.inputMethodRequests);
                        break;
                    case 12:
                        x2.setLayoutX(this.layoutX);
                        break;
                    case 13:
                        x2.setLayoutY(this.layoutY);
                        break;
                    case 14:
                        x2.setManaged(this.managed);
                        break;
                    case 15:
                        x2.setMouseTransparent(this.mouseTransparent);
                        break;
                    case 16:
                        x2.setOnContextMenuRequested(this.onContextMenuRequested);
                        break;
                    case 17:
                        x2.setOnDragDetected(this.onDragDetected);
                        break;
                    case 18:
                        x2.setOnDragDone(this.onDragDone);
                        break;
                    case 19:
                        x2.setOnDragDropped(this.onDragDropped);
                        break;
                    case 20:
                        x2.setOnDragEntered(this.onDragEntered);
                        break;
                    case 21:
                        x2.setOnDragExited(this.onDragExited);
                        break;
                    case 22:
                        x2.setOnDragOver(this.onDragOver);
                        break;
                    case 23:
                        x2.setOnInputMethodTextChanged(this.onInputMethodTextChanged);
                        break;
                    case 24:
                        x2.setOnKeyPressed(this.onKeyPressed);
                        break;
                    case 25:
                        x2.setOnKeyReleased(this.onKeyReleased);
                        break;
                    case 26:
                        x2.setOnKeyTyped(this.onKeyTyped);
                        break;
                    case 27:
                        x2.setOnMouseClicked(this.onMouseClicked);
                        break;
                    case 28:
                        x2.setOnMouseDragEntered(this.onMouseDragEntered);
                        break;
                    case 29:
                        x2.setOnMouseDragExited(this.onMouseDragExited);
                        break;
                    case 30:
                        x2.setOnMouseDragged(this.onMouseDragged);
                        break;
                    case 31:
                        x2.setOnMouseDragOver(this.onMouseDragOver);
                        break;
                    case 32:
                        x2.setOnMouseDragReleased(this.onMouseDragReleased);
                        break;
                    case 33:
                        x2.setOnMouseEntered(this.onMouseEntered);
                        break;
                    case 34:
                        x2.setOnMouseExited(this.onMouseExited);
                        break;
                    case 35:
                        x2.setOnMouseMoved(this.onMouseMoved);
                        break;
                    case 36:
                        x2.setOnMousePressed(this.onMousePressed);
                        break;
                    case 37:
                        x2.setOnMouseReleased(this.onMouseReleased);
                        break;
                    case 38:
                        x2.setOnRotate(this.onRotate);
                        break;
                    case 39:
                        x2.setOnRotationFinished(this.onRotationFinished);
                        break;
                    case 40:
                        x2.setOnRotationStarted(this.onRotationStarted);
                        break;
                    case 41:
                        x2.setOnScroll(this.onScroll);
                        break;
                    case 42:
                        x2.setOnScrollFinished(this.onScrollFinished);
                        break;
                    case 43:
                        x2.setOnScrollStarted(this.onScrollStarted);
                        break;
                    case 44:
                        x2.setOnSwipeDown(this.onSwipeDown);
                        break;
                    case 45:
                        x2.setOnSwipeLeft(this.onSwipeLeft);
                        break;
                    case 46:
                        x2.setOnSwipeRight(this.onSwipeRight);
                        break;
                    case 47:
                        x2.setOnSwipeUp(this.onSwipeUp);
                        break;
                    case 48:
                        x2.setOnTouchMoved(this.onTouchMoved);
                        break;
                    case 49:
                        x2.setOnTouchPressed(this.onTouchPressed);
                        break;
                    case 50:
                        x2.setOnTouchReleased(this.onTouchReleased);
                        break;
                    case 51:
                        x2.setOnTouchStationary(this.onTouchStationary);
                        break;
                    case 52:
                        x2.setOnZoom(this.onZoom);
                        break;
                    case 53:
                        x2.setOnZoomFinished(this.onZoomFinished);
                        break;
                    case 54:
                        x2.setOnZoomStarted(this.onZoomStarted);
                        break;
                    case 55:
                        x2.setOpacity(this.opacity);
                        break;
                    case 56:
                        x2.setPickOnBounds(this.pickOnBounds);
                        break;
                    case 57:
                        x2.setRotate(this.rotate);
                        break;
                    case 58:
                        x2.setRotationAxis(this.rotationAxis);
                        break;
                    case 59:
                        x2.setScaleX(this.scaleX);
                        break;
                    case 60:
                        x2.setScaleY(this.scaleY);
                        break;
                    case 61:
                        x2.setScaleZ(this.scaleZ);
                        break;
                    case 62:
                        x2.setStyle(this.style);
                        break;
                    case 63:
                        x2.getStyleClass().addAll(this.styleClass);
                        break;
                    case 64:
                        x2.getTransforms().addAll(this.transforms);
                        break;
                    case 65:
                        x2.setTranslateX(this.translateX);
                        break;
                    case 66:
                        x2.setTranslateY(this.translateY);
                        break;
                    case 67:
                        x2.setTranslateZ(this.translateZ);
                        break;
                    case 68:
                        x2.setUserData(this.userData);
                        break;
                    case 69:
                        x2.setVisible(this.visible);
                        break;
                }
            } else {
                return;
            }
        }
    }

    public B blendMode(BlendMode x2) {
        this.blendMode = x2;
        __set(0);
        return this;
    }

    public B cache(boolean x2) {
        this.cache = x2;
        __set(1);
        return this;
    }

    public B cacheHint(CacheHint x2) {
        this.cacheHint = x2;
        __set(2);
        return this;
    }

    public B clip(Node x2) {
        this.clip = x2;
        __set(3);
        return this;
    }

    public B cursor(Cursor x2) {
        this.cursor = x2;
        __set(4);
        return this;
    }

    public B depthTest(DepthTest x2) {
        this.depthTest = x2;
        __set(5);
        return this;
    }

    public B disable(boolean x2) {
        this.disable = x2;
        __set(6);
        return this;
    }

    public B effect(Effect x2) {
        this.effect = x2;
        __set(7);
        return this;
    }

    public B eventDispatcher(EventDispatcher x2) {
        this.eventDispatcher = x2;
        __set(8);
        return this;
    }

    public B focusTraversable(boolean x2) {
        this.focusTraversable = x2;
        __set(9);
        return this;
    }

    public B id(String x2) {
        this.id = x2;
        __set(10);
        return this;
    }

    public B inputMethodRequests(InputMethodRequests x2) {
        this.inputMethodRequests = x2;
        __set(11);
        return this;
    }

    public B layoutX(double x2) {
        this.layoutX = x2;
        __set(12);
        return this;
    }

    public B layoutY(double x2) {
        this.layoutY = x2;
        __set(13);
        return this;
    }

    public B managed(boolean x2) {
        this.managed = x2;
        __set(14);
        return this;
    }

    public B mouseTransparent(boolean x2) {
        this.mouseTransparent = x2;
        __set(15);
        return this;
    }

    public B onContextMenuRequested(EventHandler<? super ContextMenuEvent> x2) {
        this.onContextMenuRequested = x2;
        __set(16);
        return this;
    }

    public B onDragDetected(EventHandler<? super MouseEvent> x2) {
        this.onDragDetected = x2;
        __set(17);
        return this;
    }

    public B onDragDone(EventHandler<? super DragEvent> x2) {
        this.onDragDone = x2;
        __set(18);
        return this;
    }

    public B onDragDropped(EventHandler<? super DragEvent> x2) {
        this.onDragDropped = x2;
        __set(19);
        return this;
    }

    public B onDragEntered(EventHandler<? super DragEvent> x2) {
        this.onDragEntered = x2;
        __set(20);
        return this;
    }

    public B onDragExited(EventHandler<? super DragEvent> x2) {
        this.onDragExited = x2;
        __set(21);
        return this;
    }

    public B onDragOver(EventHandler<? super DragEvent> x2) {
        this.onDragOver = x2;
        __set(22);
        return this;
    }

    public B onInputMethodTextChanged(EventHandler<? super InputMethodEvent> x2) {
        this.onInputMethodTextChanged = x2;
        __set(23);
        return this;
    }

    public B onKeyPressed(EventHandler<? super KeyEvent> x2) {
        this.onKeyPressed = x2;
        __set(24);
        return this;
    }

    public B onKeyReleased(EventHandler<? super KeyEvent> x2) {
        this.onKeyReleased = x2;
        __set(25);
        return this;
    }

    public B onKeyTyped(EventHandler<? super KeyEvent> x2) {
        this.onKeyTyped = x2;
        __set(26);
        return this;
    }

    public B onMouseClicked(EventHandler<? super MouseEvent> x2) {
        this.onMouseClicked = x2;
        __set(27);
        return this;
    }

    public B onMouseDragEntered(EventHandler<? super MouseDragEvent> x2) {
        this.onMouseDragEntered = x2;
        __set(28);
        return this;
    }

    public B onMouseDragExited(EventHandler<? super MouseDragEvent> x2) {
        this.onMouseDragExited = x2;
        __set(29);
        return this;
    }

    public B onMouseDragged(EventHandler<? super MouseEvent> x2) {
        this.onMouseDragged = x2;
        __set(30);
        return this;
    }

    public B onMouseDragOver(EventHandler<? super MouseDragEvent> x2) {
        this.onMouseDragOver = x2;
        __set(31);
        return this;
    }

    public B onMouseDragReleased(EventHandler<? super MouseDragEvent> x2) {
        this.onMouseDragReleased = x2;
        __set(32);
        return this;
    }

    public B onMouseEntered(EventHandler<? super MouseEvent> x2) {
        this.onMouseEntered = x2;
        __set(33);
        return this;
    }

    public B onMouseExited(EventHandler<? super MouseEvent> x2) {
        this.onMouseExited = x2;
        __set(34);
        return this;
    }

    public B onMouseMoved(EventHandler<? super MouseEvent> x2) {
        this.onMouseMoved = x2;
        __set(35);
        return this;
    }

    public B onMousePressed(EventHandler<? super MouseEvent> x2) {
        this.onMousePressed = x2;
        __set(36);
        return this;
    }

    public B onMouseReleased(EventHandler<? super MouseEvent> x2) {
        this.onMouseReleased = x2;
        __set(37);
        return this;
    }

    public B onRotate(EventHandler<? super RotateEvent> x2) {
        this.onRotate = x2;
        __set(38);
        return this;
    }

    public B onRotationFinished(EventHandler<? super RotateEvent> x2) {
        this.onRotationFinished = x2;
        __set(39);
        return this;
    }

    public B onRotationStarted(EventHandler<? super RotateEvent> x2) {
        this.onRotationStarted = x2;
        __set(40);
        return this;
    }

    public B onScroll(EventHandler<? super ScrollEvent> x2) {
        this.onScroll = x2;
        __set(41);
        return this;
    }

    public B onScrollFinished(EventHandler<? super ScrollEvent> x2) {
        this.onScrollFinished = x2;
        __set(42);
        return this;
    }

    public B onScrollStarted(EventHandler<? super ScrollEvent> x2) {
        this.onScrollStarted = x2;
        __set(43);
        return this;
    }

    public B onSwipeDown(EventHandler<? super SwipeEvent> x2) {
        this.onSwipeDown = x2;
        __set(44);
        return this;
    }

    public B onSwipeLeft(EventHandler<? super SwipeEvent> x2) {
        this.onSwipeLeft = x2;
        __set(45);
        return this;
    }

    public B onSwipeRight(EventHandler<? super SwipeEvent> x2) {
        this.onSwipeRight = x2;
        __set(46);
        return this;
    }

    public B onSwipeUp(EventHandler<? super SwipeEvent> x2) {
        this.onSwipeUp = x2;
        __set(47);
        return this;
    }

    public B onTouchMoved(EventHandler<? super TouchEvent> x2) {
        this.onTouchMoved = x2;
        __set(48);
        return this;
    }

    public B onTouchPressed(EventHandler<? super TouchEvent> x2) {
        this.onTouchPressed = x2;
        __set(49);
        return this;
    }

    public B onTouchReleased(EventHandler<? super TouchEvent> x2) {
        this.onTouchReleased = x2;
        __set(50);
        return this;
    }

    public B onTouchStationary(EventHandler<? super TouchEvent> x2) {
        this.onTouchStationary = x2;
        __set(51);
        return this;
    }

    public B onZoom(EventHandler<? super ZoomEvent> x2) {
        this.onZoom = x2;
        __set(52);
        return this;
    }

    public B onZoomFinished(EventHandler<? super ZoomEvent> x2) {
        this.onZoomFinished = x2;
        __set(53);
        return this;
    }

    public B onZoomStarted(EventHandler<? super ZoomEvent> x2) {
        this.onZoomStarted = x2;
        __set(54);
        return this;
    }

    public B opacity(double x2) {
        this.opacity = x2;
        __set(55);
        return this;
    }

    public B pickOnBounds(boolean x2) {
        this.pickOnBounds = x2;
        __set(56);
        return this;
    }

    public B rotate(double x2) {
        this.rotate = x2;
        __set(57);
        return this;
    }

    public B rotationAxis(Point3D x2) {
        this.rotationAxis = x2;
        __set(58);
        return this;
    }

    public B scaleX(double x2) {
        this.scaleX = x2;
        __set(59);
        return this;
    }

    public B scaleY(double x2) {
        this.scaleY = x2;
        __set(60);
        return this;
    }

    public B scaleZ(double x2) {
        this.scaleZ = x2;
        __set(61);
        return this;
    }

    public B style(String x2) {
        this.style = x2;
        __set(62);
        return this;
    }

    public B styleClass(Collection<? extends String> x2) {
        this.styleClass = x2;
        __set(63);
        return this;
    }

    public B styleClass(String... strArr) {
        return (B) styleClass(Arrays.asList(strArr));
    }

    public B transforms(Collection<? extends Transform> x2) {
        this.transforms = x2;
        __set(64);
        return this;
    }

    public B transforms(Transform... transformArr) {
        return (B) transforms(Arrays.asList(transformArr));
    }

    public B translateX(double x2) {
        this.translateX = x2;
        __set(65);
        return this;
    }

    public B translateY(double x2) {
        this.translateY = x2;
        __set(66);
        return this;
    }

    public B translateZ(double x2) {
        this.translateZ = x2;
        __set(67);
        return this;
    }

    public B userData(Object x2) {
        this.userData = x2;
        __set(68);
        return this;
    }

    public B visible(boolean x2) {
        this.visible = x2;
        __set(69);
        return this;
    }
}
