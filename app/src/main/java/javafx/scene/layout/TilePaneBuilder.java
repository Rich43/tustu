package javafx.scene.layout;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.layout.TilePaneBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/layout/TilePaneBuilder.class */
public class TilePaneBuilder<B extends TilePaneBuilder<B>> extends PaneBuilder<B> {
    private int __set;
    private Pos alignment;
    private double hgap;
    private Orientation orientation;
    private int prefColumns;
    private int prefRows;
    private double prefTileHeight;
    private double prefTileWidth;
    private Pos tileAlignment;
    private double vgap;

    protected TilePaneBuilder() {
    }

    public static TilePaneBuilder<?> create() {
        return new TilePaneBuilder<>();
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(TilePane x2) {
        super.applyTo((Pane) x2);
        int set = this.__set;
        while (set != 0) {
            int i2 = Integer.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setAlignment(this.alignment);
                    break;
                case 1:
                    x2.setHgap(this.hgap);
                    break;
                case 2:
                    x2.setOrientation(this.orientation);
                    break;
                case 3:
                    x2.setPrefColumns(this.prefColumns);
                    break;
                case 4:
                    x2.setPrefRows(this.prefRows);
                    break;
                case 5:
                    x2.setPrefTileHeight(this.prefTileHeight);
                    break;
                case 6:
                    x2.setPrefTileWidth(this.prefTileWidth);
                    break;
                case 7:
                    x2.setTileAlignment(this.tileAlignment);
                    break;
                case 8:
                    x2.setVgap(this.vgap);
                    break;
            }
        }
    }

    public B alignment(Pos x2) {
        this.alignment = x2;
        __set(0);
        return this;
    }

    public B hgap(double x2) {
        this.hgap = x2;
        __set(1);
        return this;
    }

    public B orientation(Orientation x2) {
        this.orientation = x2;
        __set(2);
        return this;
    }

    public B prefColumns(int x2) {
        this.prefColumns = x2;
        __set(3);
        return this;
    }

    public B prefRows(int x2) {
        this.prefRows = x2;
        __set(4);
        return this;
    }

    public B prefTileHeight(double x2) {
        this.prefTileHeight = x2;
        __set(5);
        return this;
    }

    public B prefTileWidth(double x2) {
        this.prefTileWidth = x2;
        __set(6);
        return this;
    }

    public B tileAlignment(Pos x2) {
        this.tileAlignment = x2;
        __set(7);
        return this;
    }

    public B vgap(double x2) {
        this.vgap = x2;
        __set(8);
        return this;
    }

    @Override // javafx.scene.layout.PaneBuilder, javafx.scene.layout.RegionBuilder, javafx.util.Builder
    /* renamed from: build */
    public TilePane build2() {
        TilePane x2 = new TilePane();
        applyTo(x2);
        return x2;
    }
}
