package sun.java2d.pipe.hw;

import java.awt.BufferCapabilities;
import java.awt.ImageCapabilities;

/* loaded from: rt.jar:sun/java2d/pipe/hw/ExtendedBufferCapabilities.class */
public class ExtendedBufferCapabilities extends BufferCapabilities {
    private VSyncType vsync;

    /* loaded from: rt.jar:sun/java2d/pipe/hw/ExtendedBufferCapabilities$VSyncType.class */
    public enum VSyncType {
        VSYNC_DEFAULT(0),
        VSYNC_ON(1),
        VSYNC_OFF(2);

        private int id;

        public int id() {
            return this.id;
        }

        VSyncType(int i2) {
            this.id = i2;
        }
    }

    public ExtendedBufferCapabilities(BufferCapabilities bufferCapabilities) {
        super(bufferCapabilities.getFrontBufferCapabilities(), bufferCapabilities.getBackBufferCapabilities(), bufferCapabilities.getFlipContents());
        this.vsync = VSyncType.VSYNC_DEFAULT;
    }

    public ExtendedBufferCapabilities(ImageCapabilities imageCapabilities, ImageCapabilities imageCapabilities2, BufferCapabilities.FlipContents flipContents) {
        super(imageCapabilities, imageCapabilities2, flipContents);
        this.vsync = VSyncType.VSYNC_DEFAULT;
    }

    public ExtendedBufferCapabilities(ImageCapabilities imageCapabilities, ImageCapabilities imageCapabilities2, BufferCapabilities.FlipContents flipContents, VSyncType vSyncType) {
        super(imageCapabilities, imageCapabilities2, flipContents);
        this.vsync = vSyncType;
    }

    public ExtendedBufferCapabilities(BufferCapabilities bufferCapabilities, VSyncType vSyncType) {
        super(bufferCapabilities.getFrontBufferCapabilities(), bufferCapabilities.getBackBufferCapabilities(), bufferCapabilities.getFlipContents());
        this.vsync = vSyncType;
    }

    public ExtendedBufferCapabilities derive(VSyncType vSyncType) {
        return new ExtendedBufferCapabilities(this, vSyncType);
    }

    public VSyncType getVSync() {
        return this.vsync;
    }

    @Override // java.awt.BufferCapabilities
    public final boolean isPageFlipping() {
        return true;
    }
}
