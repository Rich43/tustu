package javafx.scene;

import java.util.Arrays;
import java.util.Collection;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.scene.SceneBuilder;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.RotateEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.paint.Paint;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/SceneBuilder.class */
public class SceneBuilder<B extends SceneBuilder<B>> implements Builder<Scene> {
    private long __set;
    private Camera camera;
    private Cursor cursor;
    private boolean depthBuffer;
    private EventDispatcher eventDispatcher;
    private Paint fill;
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
    private Parent root;
    private Collection<? extends String> stylesheets;
    private double height = -1.0d;
    private double width = -1.0d;

    protected SceneBuilder() {
    }

    public static SceneBuilder<?> create() {
        return new SceneBuilder<>();
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(Scene x2) {
        long set = this.__set;
        while (set != 0) {
            int i2 = Long.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setCamera(this.camera);
                    break;
                case 1:
                    x2.setCursor(this.cursor);
                    break;
                case 2:
                    x2.setEventDispatcher(this.eventDispatcher);
                    break;
                case 3:
                    x2.setFill(this.fill);
                    break;
                case 4:
                    x2.setOnContextMenuRequested(this.onContextMenuRequested);
                    break;
                case 5:
                    x2.setOnDragDetected(this.onDragDetected);
                    break;
                case 6:
                    x2.setOnDragDone(this.onDragDone);
                    break;
                case 7:
                    x2.setOnDragDropped(this.onDragDropped);
                    break;
                case 8:
                    x2.setOnDragEntered(this.onDragEntered);
                    break;
                case 9:
                    x2.setOnDragExited(this.onDragExited);
                    break;
                case 10:
                    x2.setOnDragOver(this.onDragOver);
                    break;
                case 11:
                    x2.setOnInputMethodTextChanged(this.onInputMethodTextChanged);
                    break;
                case 12:
                    x2.setOnKeyPressed(this.onKeyPressed);
                    break;
                case 13:
                    x2.setOnKeyReleased(this.onKeyReleased);
                    break;
                case 14:
                    x2.setOnKeyTyped(this.onKeyTyped);
                    break;
                case 15:
                    x2.setOnMouseClicked(this.onMouseClicked);
                    break;
                case 16:
                    x2.setOnMouseDragEntered(this.onMouseDragEntered);
                    break;
                case 17:
                    x2.setOnMouseDragExited(this.onMouseDragExited);
                    break;
                case 18:
                    x2.setOnMouseDragged(this.onMouseDragged);
                    break;
                case 19:
                    x2.setOnMouseDragOver(this.onMouseDragOver);
                    break;
                case 20:
                    x2.setOnMouseDragReleased(this.onMouseDragReleased);
                    break;
                case 21:
                    x2.setOnMouseEntered(this.onMouseEntered);
                    break;
                case 22:
                    x2.setOnMouseExited(this.onMouseExited);
                    break;
                case 23:
                    x2.setOnMouseMoved(this.onMouseMoved);
                    break;
                case 24:
                    x2.setOnMousePressed(this.onMousePressed);
                    break;
                case 25:
                    x2.setOnMouseReleased(this.onMouseReleased);
                    break;
                case 26:
                    x2.setOnRotate(this.onRotate);
                    break;
                case 27:
                    x2.setOnRotationFinished(this.onRotationFinished);
                    break;
                case 28:
                    x2.setOnRotationStarted(this.onRotationStarted);
                    break;
                case 29:
                    x2.setOnScroll(this.onScroll);
                    break;
                case 30:
                    x2.setOnScrollFinished(this.onScrollFinished);
                    break;
                case 31:
                    x2.setOnScrollStarted(this.onScrollStarted);
                    break;
                case 32:
                    x2.setOnSwipeDown(this.onSwipeDown);
                    break;
                case 33:
                    x2.setOnSwipeLeft(this.onSwipeLeft);
                    break;
                case 34:
                    x2.setOnSwipeRight(this.onSwipeRight);
                    break;
                case 35:
                    x2.setOnSwipeUp(this.onSwipeUp);
                    break;
                case 36:
                    x2.setOnTouchMoved(this.onTouchMoved);
                    break;
                case 37:
                    x2.setOnTouchPressed(this.onTouchPressed);
                    break;
                case 38:
                    x2.setOnTouchReleased(this.onTouchReleased);
                    break;
                case 39:
                    x2.setOnTouchStationary(this.onTouchStationary);
                    break;
                case 40:
                    x2.setOnZoom(this.onZoom);
                    break;
                case 41:
                    x2.setOnZoomFinished(this.onZoomFinished);
                    break;
                case 42:
                    x2.setOnZoomStarted(this.onZoomStarted);
                    break;
                case 43:
                    x2.getStylesheets().addAll(this.stylesheets);
                    break;
            }
        }
    }

    public B camera(Camera x2) {
        this.camera = x2;
        __set(0);
        return this;
    }

    public B cursor(Cursor x2) {
        this.cursor = x2;
        __set(1);
        return this;
    }

    public B depthBuffer(boolean x2) {
        this.depthBuffer = x2;
        return this;
    }

    public B eventDispatcher(EventDispatcher x2) {
        this.eventDispatcher = x2;
        __set(2);
        return this;
    }

    public B fill(Paint x2) {
        this.fill = x2;
        __set(3);
        return this;
    }

    public B height(double x2) {
        this.height = x2;
        return this;
    }

    public B onContextMenuRequested(EventHandler<? super ContextMenuEvent> x2) {
        this.onContextMenuRequested = x2;
        __set(4);
        return this;
    }

    public B onDragDetected(EventHandler<? super MouseEvent> x2) {
        this.onDragDetected = x2;
        __set(5);
        return this;
    }

    public B onDragDone(EventHandler<? super DragEvent> x2) {
        this.onDragDone = x2;
        __set(6);
        return this;
    }

    public B onDragDropped(EventHandler<? super DragEvent> x2) {
        this.onDragDropped = x2;
        __set(7);
        return this;
    }

    public B onDragEntered(EventHandler<? super DragEvent> x2) {
        this.onDragEntered = x2;
        __set(8);
        return this;
    }

    public B onDragExited(EventHandler<? super DragEvent> x2) {
        this.onDragExited = x2;
        __set(9);
        return this;
    }

    public B onDragOver(EventHandler<? super DragEvent> x2) {
        this.onDragOver = x2;
        __set(10);
        return this;
    }

    public B onInputMethodTextChanged(EventHandler<? super InputMethodEvent> x2) {
        this.onInputMethodTextChanged = x2;
        __set(11);
        return this;
    }

    public B onKeyPressed(EventHandler<? super KeyEvent> x2) {
        this.onKeyPressed = x2;
        __set(12);
        return this;
    }

    public B onKeyReleased(EventHandler<? super KeyEvent> x2) {
        this.onKeyReleased = x2;
        __set(13);
        return this;
    }

    public B onKeyTyped(EventHandler<? super KeyEvent> x2) {
        this.onKeyTyped = x2;
        __set(14);
        return this;
    }

    public B onMouseClicked(EventHandler<? super MouseEvent> x2) {
        this.onMouseClicked = x2;
        __set(15);
        return this;
    }

    public B onMouseDragEntered(EventHandler<? super MouseDragEvent> x2) {
        this.onMouseDragEntered = x2;
        __set(16);
        return this;
    }

    public B onMouseDragExited(EventHandler<? super MouseDragEvent> x2) {
        this.onMouseDragExited = x2;
        __set(17);
        return this;
    }

    public B onMouseDragged(EventHandler<? super MouseEvent> x2) {
        this.onMouseDragged = x2;
        __set(18);
        return this;
    }

    public B onMouseDragOver(EventHandler<? super MouseDragEvent> x2) {
        this.onMouseDragOver = x2;
        __set(19);
        return this;
    }

    public B onMouseDragReleased(EventHandler<? super MouseDragEvent> x2) {
        this.onMouseDragReleased = x2;
        __set(20);
        return this;
    }

    public B onMouseEntered(EventHandler<? super MouseEvent> x2) {
        this.onMouseEntered = x2;
        __set(21);
        return this;
    }

    public B onMouseExited(EventHandler<? super MouseEvent> x2) {
        this.onMouseExited = x2;
        __set(22);
        return this;
    }

    public B onMouseMoved(EventHandler<? super MouseEvent> x2) {
        this.onMouseMoved = x2;
        __set(23);
        return this;
    }

    public B onMousePressed(EventHandler<? super MouseEvent> x2) {
        this.onMousePressed = x2;
        __set(24);
        return this;
    }

    public B onMouseReleased(EventHandler<? super MouseEvent> x2) {
        this.onMouseReleased = x2;
        __set(25);
        return this;
    }

    public B onRotate(EventHandler<? super RotateEvent> x2) {
        this.onRotate = x2;
        __set(26);
        return this;
    }

    public B onRotationFinished(EventHandler<? super RotateEvent> x2) {
        this.onRotationFinished = x2;
        __set(27);
        return this;
    }

    public B onRotationStarted(EventHandler<? super RotateEvent> x2) {
        this.onRotationStarted = x2;
        __set(28);
        return this;
    }

    public B onScroll(EventHandler<? super ScrollEvent> x2) {
        this.onScroll = x2;
        __set(29);
        return this;
    }

    public B onScrollFinished(EventHandler<? super ScrollEvent> x2) {
        this.onScrollFinished = x2;
        __set(30);
        return this;
    }

    public B onScrollStarted(EventHandler<? super ScrollEvent> x2) {
        this.onScrollStarted = x2;
        __set(31);
        return this;
    }

    public B onSwipeDown(EventHandler<? super SwipeEvent> x2) {
        this.onSwipeDown = x2;
        __set(32);
        return this;
    }

    public B onSwipeLeft(EventHandler<? super SwipeEvent> x2) {
        this.onSwipeLeft = x2;
        __set(33);
        return this;
    }

    public B onSwipeRight(EventHandler<? super SwipeEvent> x2) {
        this.onSwipeRight = x2;
        __set(34);
        return this;
    }

    public B onSwipeUp(EventHandler<? super SwipeEvent> x2) {
        this.onSwipeUp = x2;
        __set(35);
        return this;
    }

    public B onTouchMoved(EventHandler<? super TouchEvent> x2) {
        this.onTouchMoved = x2;
        __set(36);
        return this;
    }

    public B onTouchPressed(EventHandler<? super TouchEvent> x2) {
        this.onTouchPressed = x2;
        __set(37);
        return this;
    }

    public B onTouchReleased(EventHandler<? super TouchEvent> x2) {
        this.onTouchReleased = x2;
        __set(38);
        return this;
    }

    public B onTouchStationary(EventHandler<? super TouchEvent> x2) {
        this.onTouchStationary = x2;
        __set(39);
        return this;
    }

    public B onZoom(EventHandler<? super ZoomEvent> x2) {
        this.onZoom = x2;
        __set(40);
        return this;
    }

    public B onZoomFinished(EventHandler<? super ZoomEvent> x2) {
        this.onZoomFinished = x2;
        __set(41);
        return this;
    }

    public B onZoomStarted(EventHandler<? super ZoomEvent> x2) {
        this.onZoomStarted = x2;
        __set(42);
        return this;
    }

    public B root(Parent x2) {
        this.root = x2;
        return this;
    }

    public B stylesheets(Collection<? extends String> x2) {
        this.stylesheets = x2;
        __set(43);
        return this;
    }

    public B stylesheets(String... strArr) {
        return (B) stylesheets(Arrays.asList(strArr));
    }

    public B width(double x2) {
        this.width = x2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Scene build2() {
        Scene x2 = new Scene(this.root, this.width, this.height, this.depthBuffer);
        applyTo(x2);
        return x2;
    }
}
