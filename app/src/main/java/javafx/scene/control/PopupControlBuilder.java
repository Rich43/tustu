package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.PopupControlBuilder;
import javafx.stage.PopupWindow;
import javafx.stage.PopupWindowBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/PopupControlBuilder.class */
public class PopupControlBuilder<B extends PopupControlBuilder<B>> extends PopupWindowBuilder<B> implements Builder<PopupControl> {
    private int __set;
    private String id;
    private double maxHeight;
    private double maxWidth;
    private double minHeight;
    private double minWidth;
    private double prefHeight;
    private double prefWidth;
    private Skin<?> skin;
    private String style;
    private Collection<? extends String> styleClass;

    protected PopupControlBuilder() {
    }

    public static PopupControlBuilder<?> create() {
        return new PopupControlBuilder<>();
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(PopupControl x2) {
        super.applyTo((PopupWindow) x2);
        int set = this.__set;
        while (set != 0) {
            int i2 = Integer.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setId(this.id);
                    break;
                case 1:
                    x2.setMaxHeight(this.maxHeight);
                    break;
                case 2:
                    x2.setMaxWidth(this.maxWidth);
                    break;
                case 3:
                    x2.setMinHeight(this.minHeight);
                    break;
                case 4:
                    x2.setMinWidth(this.minWidth);
                    break;
                case 5:
                    x2.setPrefHeight(this.prefHeight);
                    break;
                case 6:
                    x2.setPrefWidth(this.prefWidth);
                    break;
                case 7:
                    x2.setSkin(this.skin);
                    break;
                case 8:
                    x2.setStyle(this.style);
                    break;
                case 9:
                    x2.getStyleClass().addAll(this.styleClass);
                    break;
            }
        }
    }

    public B id(String x2) {
        this.id = x2;
        __set(0);
        return this;
    }

    public B maxHeight(double x2) {
        this.maxHeight = x2;
        __set(1);
        return this;
    }

    public B maxWidth(double x2) {
        this.maxWidth = x2;
        __set(2);
        return this;
    }

    public B minHeight(double x2) {
        this.minHeight = x2;
        __set(3);
        return this;
    }

    public B minWidth(double x2) {
        this.minWidth = x2;
        __set(4);
        return this;
    }

    public B prefHeight(double x2) {
        this.prefHeight = x2;
        __set(5);
        return this;
    }

    public B prefWidth(double x2) {
        this.prefWidth = x2;
        __set(6);
        return this;
    }

    public B skin(Skin<?> x2) {
        this.skin = x2;
        __set(7);
        return this;
    }

    public B style(String x2) {
        this.style = x2;
        __set(8);
        return this;
    }

    public B styleClass(Collection<? extends String> x2) {
        this.styleClass = x2;
        __set(9);
        return this;
    }

    public B styleClass(String... strArr) {
        return (B) styleClass(Arrays.asList(strArr));
    }

    @Override // javafx.util.Builder
    /* renamed from: build */
    public PopupControl build2() {
        PopupControl x2 = new PopupControl();
        applyTo(x2);
        return x2;
    }
}
