package javafx.scene.control;

import javafx.scene.Parent;
import javafx.scene.ParentBuilder;
import javafx.scene.control.ControlBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/ControlBuilder.class */
public abstract class ControlBuilder<B extends ControlBuilder<B>> extends ParentBuilder<B> {
    private int __set;
    private ContextMenu contextMenu;
    private double maxHeight;
    private double maxWidth;
    private double minHeight;
    private double minWidth;
    private double prefHeight;
    private double prefWidth;
    private Skin<?> skin;
    private Tooltip tooltip;

    protected ControlBuilder() {
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(Control x2) {
        super.applyTo((Parent) x2);
        int set = this.__set;
        while (set != 0) {
            int i2 = Integer.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setContextMenu(this.contextMenu);
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
                    x2.setTooltip(this.tooltip);
                    break;
            }
        }
    }

    public B contextMenu(ContextMenu x2) {
        this.contextMenu = x2;
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

    public B tooltip(Tooltip x2) {
        this.tooltip = x2;
        __set(8);
        return this;
    }
}
