package javafx.scene;

import com.sun.javafx.sg.prism.NGAmbientLight;
import com.sun.javafx.sg.prism.NGNode;
import javafx.scene.paint.Color;

/* loaded from: jfxrt.jar:javafx/scene/AmbientLight.class */
public class AmbientLight extends LightBase {
    public AmbientLight() {
    }

    public AmbientLight(Color color) {
        super(color);
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGAmbientLight();
    }
}
