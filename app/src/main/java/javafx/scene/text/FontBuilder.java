package javafx.scene.text;

import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/text/FontBuilder.class */
public final class FontBuilder implements Builder<Font> {
    private String name;
    private double size;

    protected FontBuilder() {
    }

    public static FontBuilder create() {
        return new FontBuilder();
    }

    public FontBuilder name(String x2) {
        this.name = x2;
        return this;
    }

    public FontBuilder size(double x2) {
        this.size = x2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Font build2() {
        Font x2 = new Font(this.name, this.size);
        return x2;
    }
}
