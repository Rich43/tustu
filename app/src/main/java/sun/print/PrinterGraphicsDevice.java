package sun.print;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Window;

/* loaded from: rt.jar:sun/print/PrinterGraphicsDevice.class */
public final class PrinterGraphicsDevice extends GraphicsDevice {
    String printerID;
    GraphicsConfiguration graphicsConf;

    protected PrinterGraphicsDevice(GraphicsConfiguration graphicsConfiguration, String str) {
        this.printerID = str;
        this.graphicsConf = graphicsConfiguration;
    }

    @Override // java.awt.GraphicsDevice
    public int getType() {
        return 1;
    }

    @Override // java.awt.GraphicsDevice
    public String getIDstring() {
        return this.printerID;
    }

    @Override // java.awt.GraphicsDevice
    public GraphicsConfiguration[] getConfigurations() {
        return new GraphicsConfiguration[]{this.graphicsConf};
    }

    @Override // java.awt.GraphicsDevice
    public GraphicsConfiguration getDefaultConfiguration() {
        return this.graphicsConf;
    }

    @Override // java.awt.GraphicsDevice
    public void setFullScreenWindow(Window window) {
    }

    @Override // java.awt.GraphicsDevice
    public Window getFullScreenWindow() {
        return null;
    }
}
