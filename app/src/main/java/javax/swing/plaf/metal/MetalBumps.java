package javax.swing.plaf.metal;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import sun.awt.AppContext;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalBumps.class */
class MetalBumps implements Icon {
    protected int xBumps;
    protected int yBumps;
    protected Color topColor;
    protected Color shadowColor;
    protected Color backColor;
    protected BumpBuffer buffer;
    static final Color ALPHA = new Color(0, 0, 0, 0);
    private static final Object METAL_BUMPS = new Object();

    public MetalBumps(int i2, int i3, Color color, Color color2, Color color3) {
        setBumpArea(i2, i3);
        setBumpColors(color, color2, color3);
    }

    private static BumpBuffer createBuffer(GraphicsConfiguration graphicsConfiguration, Color color, Color color2, Color color3) {
        AppContext appContext = AppContext.getAppContext();
        List<BumpBuffer> arrayList = (List) appContext.get(METAL_BUMPS);
        if (arrayList == null) {
            arrayList = new ArrayList();
            appContext.put(METAL_BUMPS, arrayList);
        }
        for (BumpBuffer bumpBuffer : arrayList) {
            if (bumpBuffer.hasSameConfiguration(graphicsConfiguration, color, color2, color3)) {
                return bumpBuffer;
            }
        }
        BumpBuffer bumpBuffer2 = new BumpBuffer(graphicsConfiguration, color, color2, color3);
        arrayList.add(bumpBuffer2);
        return bumpBuffer2;
    }

    public void setBumpArea(Dimension dimension) {
        setBumpArea(dimension.width, dimension.height);
    }

    public void setBumpArea(int i2, int i3) {
        this.xBumps = i2 / 2;
        this.yBumps = i3 / 2;
    }

    public void setBumpColors(Color color, Color color2, Color color3) {
        this.topColor = color;
        this.shadowColor = color2;
        if (color3 == null) {
            this.backColor = ALPHA;
        } else {
            this.backColor = color3;
        }
    }

    @Override // javax.swing.Icon
    public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
        GraphicsConfiguration deviceConfiguration = graphics instanceof Graphics2D ? ((Graphics2D) graphics).getDeviceConfiguration() : null;
        if (this.buffer == null || !this.buffer.hasSameConfiguration(deviceConfiguration, this.topColor, this.shadowColor, this.backColor)) {
            this.buffer = createBuffer(deviceConfiguration, this.topColor, this.shadowColor, this.backColor);
        }
        int iconWidth = i2 + getIconWidth();
        int iconHeight = i3 + getIconHeight();
        while (i3 < iconHeight) {
            int iMin = Math.min(iconHeight - i3, 64);
            int i4 = i2;
            while (true) {
                int i5 = i4;
                if (i5 < iconWidth) {
                    int iMin2 = Math.min(iconWidth - i5, 64);
                    graphics.drawImage(this.buffer.getImage(), i5, i3, i5 + iMin2, i3 + iMin, 0, 0, iMin2, iMin, null);
                    i4 = i5 + 64;
                }
            }
            i3 += 64;
        }
    }

    @Override // javax.swing.Icon
    public int getIconWidth() {
        return this.xBumps * 2;
    }

    @Override // javax.swing.Icon
    public int getIconHeight() {
        return this.yBumps * 2;
    }
}
