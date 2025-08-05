package javafx.stage;

import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.stage.WindowBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/stage/WindowBuilder.class */
public abstract class WindowBuilder<B extends WindowBuilder<B>> {
    private int __set;
    private EventDispatcher eventDispatcher;
    private boolean focused;
    private double height;
    private EventHandler<WindowEvent> onCloseRequest;
    private EventHandler<WindowEvent> onHidden;
    private EventHandler<WindowEvent> onHiding;
    private EventHandler<WindowEvent> onShowing;
    private EventHandler<WindowEvent> onShown;
    private double opacity;
    private double width;

    /* renamed from: x, reason: collision with root package name */
    private double f12767x;

    /* renamed from: y, reason: collision with root package name */
    private double f12768y;

    protected WindowBuilder() {
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(Window x2) {
        int set = this.__set;
        while (set != 0) {
            int i2 = Integer.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setEventDispatcher(this.eventDispatcher);
                    break;
                case 1:
                    x2.setFocused(this.focused);
                    break;
                case 2:
                    x2.setHeight(this.height);
                    break;
                case 3:
                    x2.setOnCloseRequest(this.onCloseRequest);
                    break;
                case 4:
                    x2.setOnHidden(this.onHidden);
                    break;
                case 5:
                    x2.setOnHiding(this.onHiding);
                    break;
                case 6:
                    x2.setOnShowing(this.onShowing);
                    break;
                case 7:
                    x2.setOnShown(this.onShown);
                    break;
                case 8:
                    x2.setOpacity(this.opacity);
                    break;
                case 9:
                    x2.setWidth(this.width);
                    break;
                case 10:
                    x2.setX(this.f12767x);
                    break;
                case 11:
                    x2.setY(this.f12768y);
                    break;
            }
        }
    }

    public B eventDispatcher(EventDispatcher x2) {
        this.eventDispatcher = x2;
        __set(0);
        return this;
    }

    public B focused(boolean x2) {
        this.focused = x2;
        __set(1);
        return this;
    }

    public B height(double x2) {
        this.height = x2;
        __set(2);
        return this;
    }

    public B onCloseRequest(EventHandler<WindowEvent> x2) {
        this.onCloseRequest = x2;
        __set(3);
        return this;
    }

    public B onHidden(EventHandler<WindowEvent> x2) {
        this.onHidden = x2;
        __set(4);
        return this;
    }

    public B onHiding(EventHandler<WindowEvent> x2) {
        this.onHiding = x2;
        __set(5);
        return this;
    }

    public B onShowing(EventHandler<WindowEvent> x2) {
        this.onShowing = x2;
        __set(6);
        return this;
    }

    public B onShown(EventHandler<WindowEvent> x2) {
        this.onShown = x2;
        __set(7);
        return this;
    }

    public B opacity(double x2) {
        this.opacity = x2;
        __set(8);
        return this;
    }

    public B width(double x2) {
        this.width = x2;
        __set(9);
        return this;
    }

    public B x(double x2) {
        this.f12767x = x2;
        __set(10);
        return this;
    }

    public B y(double x2) {
        this.f12768y = x2;
        __set(11);
        return this;
    }
}
