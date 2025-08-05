package javafx.embed.swing;

import javafx.embed.swing.JFXPanelBuilder;
import javafx.scene.Scene;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/embed/swing/JFXPanelBuilder.class */
public class JFXPanelBuilder<B extends JFXPanelBuilder<B>> implements Builder<JFXPanel> {
    private int __set;
    private boolean opaque;
    private Scene scene;

    protected JFXPanelBuilder() {
    }

    public static JFXPanelBuilder<?> create() {
        return new JFXPanelBuilder<>();
    }

    public void applyTo(JFXPanel x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setOpaque(this.opaque);
        }
        if ((set & 2) != 0) {
            x2.setScene(this.scene);
        }
    }

    public B opaque(boolean x2) {
        this.opaque = x2;
        this.__set |= 1;
        return this;
    }

    public B scene(Scene x2) {
        this.scene = x2;
        this.__set |= 2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public JFXPanel build2() {
        JFXPanel x2 = new JFXPanel();
        applyTo(x2);
        return x2;
    }
}
