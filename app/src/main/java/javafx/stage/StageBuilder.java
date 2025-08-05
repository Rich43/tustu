package javafx.stage;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.StageBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/stage/StageBuilder.class */
public class StageBuilder<B extends StageBuilder<B>> extends WindowBuilder<B> implements Builder<Stage> {
    private int __set;
    private boolean fullScreen;
    private boolean iconified;
    private Collection<? extends Image> icons;
    private double maxHeight;
    private double maxWidth;
    private double minHeight;
    private double minWidth;
    private boolean resizable;
    private Scene scene;
    private StageStyle style = StageStyle.DECORATED;
    private String title;

    protected StageBuilder() {
    }

    public static StageBuilder<?> create() {
        return new StageBuilder<>();
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(Stage x2) {
        super.applyTo((Window) x2);
        int set = this.__set;
        while (set != 0) {
            int i2 = Integer.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setFullScreen(this.fullScreen);
                    break;
                case 1:
                    x2.setIconified(this.iconified);
                    break;
                case 2:
                    x2.getIcons().addAll(this.icons);
                    break;
                case 3:
                    x2.setMaxHeight(this.maxHeight);
                    break;
                case 4:
                    x2.setMaxWidth(this.maxWidth);
                    break;
                case 5:
                    x2.setMinHeight(this.minHeight);
                    break;
                case 6:
                    x2.setMinWidth(this.minWidth);
                    break;
                case 7:
                    x2.setResizable(this.resizable);
                    break;
                case 8:
                    x2.setScene(this.scene);
                    break;
                case 9:
                    x2.setTitle(this.title);
                    break;
            }
        }
    }

    public B fullScreen(boolean x2) {
        this.fullScreen = x2;
        __set(0);
        return this;
    }

    public B iconified(boolean x2) {
        this.iconified = x2;
        __set(1);
        return this;
    }

    public B icons(Collection<? extends Image> x2) {
        this.icons = x2;
        __set(2);
        return this;
    }

    public B icons(Image... imageArr) {
        return (B) icons(Arrays.asList(imageArr));
    }

    public B maxHeight(double x2) {
        this.maxHeight = x2;
        __set(3);
        return this;
    }

    public B maxWidth(double x2) {
        this.maxWidth = x2;
        __set(4);
        return this;
    }

    public B minHeight(double x2) {
        this.minHeight = x2;
        __set(5);
        return this;
    }

    public B minWidth(double x2) {
        this.minWidth = x2;
        __set(6);
        return this;
    }

    public B resizable(boolean x2) {
        this.resizable = x2;
        __set(7);
        return this;
    }

    public B scene(Scene x2) {
        this.scene = x2;
        __set(8);
        return this;
    }

    public B style(StageStyle x2) {
        this.style = x2;
        return this;
    }

    public B title(String x2) {
        this.title = x2;
        __set(9);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Stage build2() {
        Stage x2 = new Stage(this.style);
        applyTo(x2);
        return x2;
    }
}
