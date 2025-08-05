package javafx.scene;

import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGPointLight;
import javafx.scene.paint.Color;

/* loaded from: jfxrt.jar:javafx/scene/PointLight.class */
public class PointLight extends LightBase {
    public PointLight() {
    }

    public PointLight(Color color) {
        super(color);
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGPointLight();
    }
}
