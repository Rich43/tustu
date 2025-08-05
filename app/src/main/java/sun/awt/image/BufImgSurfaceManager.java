package sun.awt.image;

import java.awt.image.BufferedImage;
import sun.java2d.SurfaceData;

/* loaded from: rt.jar:sun/awt/image/BufImgSurfaceManager.class */
public class BufImgSurfaceManager extends SurfaceManager {
    protected BufferedImage bImg;
    protected SurfaceData sdDefault;

    public BufImgSurfaceManager(BufferedImage bufferedImage) {
        this.bImg = bufferedImage;
        this.sdDefault = BufImgSurfaceData.createData(bufferedImage);
    }

    @Override // sun.awt.image.SurfaceManager
    public SurfaceData getPrimarySurfaceData() {
        return this.sdDefault;
    }

    @Override // sun.awt.image.SurfaceManager
    public SurfaceData restoreContents() {
        return this.sdDefault;
    }
}
