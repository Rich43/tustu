package java.awt;

import java.io.Serializable;

/* loaded from: rt.jar:java/awt/GraphicsConfigTemplate.class */
public abstract class GraphicsConfigTemplate implements Serializable {
    private static final long serialVersionUID = -8061369279557787079L;
    public static final int REQUIRED = 1;
    public static final int PREFERRED = 2;
    public static final int UNNECESSARY = 3;

    public abstract GraphicsConfiguration getBestConfiguration(GraphicsConfiguration[] graphicsConfigurationArr);

    public abstract boolean isGraphicsConfigSupported(GraphicsConfiguration graphicsConfiguration);
}
