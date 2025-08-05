package javafx.scene.control;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPaneBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/ScrollPaneBuilder.class */
public class ScrollPaneBuilder<B extends ScrollPaneBuilder<B>> extends ControlBuilder<B> implements Builder<ScrollPane> {
    private int __set;
    private Node content;
    private boolean fitToHeight;
    private boolean fitToWidth;
    private ScrollPane.ScrollBarPolicy hbarPolicy;
    private double hmax;
    private double hmin;
    private double hvalue;
    private boolean pannable;
    private double prefViewportHeight;
    private double prefViewportWidth;
    private ScrollPane.ScrollBarPolicy vbarPolicy;
    private Bounds viewportBounds;
    private double vmax;
    private double vmin;
    private double vvalue;

    protected ScrollPaneBuilder() {
    }

    public static ScrollPaneBuilder<?> create() {
        return new ScrollPaneBuilder<>();
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(ScrollPane x2) {
        super.applyTo((Control) x2);
        int set = this.__set;
        while (set != 0) {
            int i2 = Integer.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setContent(this.content);
                    break;
                case 1:
                    x2.setFitToHeight(this.fitToHeight);
                    break;
                case 2:
                    x2.setFitToWidth(this.fitToWidth);
                    break;
                case 3:
                    x2.setHbarPolicy(this.hbarPolicy);
                    break;
                case 4:
                    x2.setHmax(this.hmax);
                    break;
                case 5:
                    x2.setHmin(this.hmin);
                    break;
                case 6:
                    x2.setHvalue(this.hvalue);
                    break;
                case 7:
                    x2.setPannable(this.pannable);
                    break;
                case 8:
                    x2.setPrefViewportHeight(this.prefViewportHeight);
                    break;
                case 9:
                    x2.setPrefViewportWidth(this.prefViewportWidth);
                    break;
                case 10:
                    x2.setVbarPolicy(this.vbarPolicy);
                    break;
                case 11:
                    x2.setViewportBounds(this.viewportBounds);
                    break;
                case 12:
                    x2.setVmax(this.vmax);
                    break;
                case 13:
                    x2.setVmin(this.vmin);
                    break;
                case 14:
                    x2.setVvalue(this.vvalue);
                    break;
            }
        }
    }

    public B content(Node x2) {
        this.content = x2;
        __set(0);
        return this;
    }

    public B fitToHeight(boolean x2) {
        this.fitToHeight = x2;
        __set(1);
        return this;
    }

    public B fitToWidth(boolean x2) {
        this.fitToWidth = x2;
        __set(2);
        return this;
    }

    public B hbarPolicy(ScrollPane.ScrollBarPolicy x2) {
        this.hbarPolicy = x2;
        __set(3);
        return this;
    }

    public B hmax(double x2) {
        this.hmax = x2;
        __set(4);
        return this;
    }

    public B hmin(double x2) {
        this.hmin = x2;
        __set(5);
        return this;
    }

    public B hvalue(double x2) {
        this.hvalue = x2;
        __set(6);
        return this;
    }

    public B pannable(boolean x2) {
        this.pannable = x2;
        __set(7);
        return this;
    }

    public B prefViewportHeight(double x2) {
        this.prefViewportHeight = x2;
        __set(8);
        return this;
    }

    public B prefViewportWidth(double x2) {
        this.prefViewportWidth = x2;
        __set(9);
        return this;
    }

    public B vbarPolicy(ScrollPane.ScrollBarPolicy x2) {
        this.vbarPolicy = x2;
        __set(10);
        return this;
    }

    public B viewportBounds(Bounds x2) {
        this.viewportBounds = x2;
        __set(11);
        return this;
    }

    public B vmax(double x2) {
        this.vmax = x2;
        __set(12);
        return this;
    }

    public B vmin(double x2) {
        this.vmin = x2;
        __set(13);
        return this;
    }

    public B vvalue(double x2) {
        this.vvalue = x2;
        __set(14);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public ScrollPane build2() {
        ScrollPane x2 = new ScrollPane();
        applyTo(x2);
        return x2;
    }
}
