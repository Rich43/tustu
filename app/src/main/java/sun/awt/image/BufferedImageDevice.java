package sun.awt.image;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;

/* loaded from: rt.jar:sun/awt/image/BufferedImageDevice.class */
public class BufferedImageDevice extends GraphicsDevice {
    GraphicsConfiguration gc;

    public BufferedImageDevice(BufferedImageGraphicsConfig bufferedImageGraphicsConfig) {
        this.gc = bufferedImageGraphicsConfig;
    }

    @Override // java.awt.GraphicsDevice
    public int getType() {
        return 2;
    }

    @Override // java.awt.GraphicsDevice
    public String getIDstring() {
        return "BufferedImage";
    }

    @Override // java.awt.GraphicsDevice
    public GraphicsConfiguration[] getConfigurations() {
        return new GraphicsConfiguration[]{this.gc};
    }

    @Override // java.awt.GraphicsDevice
    public GraphicsConfiguration getDefaultConfiguration() {
        return this.gc;
    }
}
