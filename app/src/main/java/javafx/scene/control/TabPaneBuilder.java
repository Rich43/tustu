package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.geometry.Side;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPaneBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/TabPaneBuilder.class */
public class TabPaneBuilder<B extends TabPaneBuilder<B>> extends ControlBuilder<B> implements Builder<TabPane> {
    private int __set;
    private boolean rotateGraphic;
    private SingleSelectionModel<Tab> selectionModel;
    private Side side;
    private TabPane.TabClosingPolicy tabClosingPolicy;
    private double tabMaxHeight;
    private double tabMaxWidth;
    private double tabMinHeight;
    private double tabMinWidth;
    private Collection<? extends Tab> tabs;

    protected TabPaneBuilder() {
    }

    public static TabPaneBuilder<?> create() {
        return new TabPaneBuilder<>();
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(TabPane x2) {
        super.applyTo((Control) x2);
        int set = this.__set;
        while (set != 0) {
            int i2 = Integer.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setRotateGraphic(this.rotateGraphic);
                    break;
                case 1:
                    x2.setSelectionModel(this.selectionModel);
                    break;
                case 2:
                    x2.setSide(this.side);
                    break;
                case 3:
                    x2.setTabClosingPolicy(this.tabClosingPolicy);
                    break;
                case 4:
                    x2.setTabMaxHeight(this.tabMaxHeight);
                    break;
                case 5:
                    x2.setTabMaxWidth(this.tabMaxWidth);
                    break;
                case 6:
                    x2.setTabMinHeight(this.tabMinHeight);
                    break;
                case 7:
                    x2.setTabMinWidth(this.tabMinWidth);
                    break;
                case 8:
                    x2.getTabs().addAll(this.tabs);
                    break;
            }
        }
    }

    public B rotateGraphic(boolean x2) {
        this.rotateGraphic = x2;
        __set(0);
        return this;
    }

    public B selectionModel(SingleSelectionModel<Tab> x2) {
        this.selectionModel = x2;
        __set(1);
        return this;
    }

    public B side(Side x2) {
        this.side = x2;
        __set(2);
        return this;
    }

    public B tabClosingPolicy(TabPane.TabClosingPolicy x2) {
        this.tabClosingPolicy = x2;
        __set(3);
        return this;
    }

    public B tabMaxHeight(double x2) {
        this.tabMaxHeight = x2;
        __set(4);
        return this;
    }

    public B tabMaxWidth(double x2) {
        this.tabMaxWidth = x2;
        __set(5);
        return this;
    }

    public B tabMinHeight(double x2) {
        this.tabMinHeight = x2;
        __set(6);
        return this;
    }

    public B tabMinWidth(double x2) {
        this.tabMinWidth = x2;
        __set(7);
        return this;
    }

    public B tabs(Collection<? extends Tab> x2) {
        this.tabs = x2;
        __set(8);
        return this;
    }

    public B tabs(Tab... tabArr) {
        return (B) tabs(Arrays.asList(tabArr));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public TabPane build2() {
        TabPane x2 = new TabPane();
        applyTo(x2);
        return x2;
    }
}
