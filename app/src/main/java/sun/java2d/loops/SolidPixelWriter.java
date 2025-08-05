package sun.java2d.loops;

/* compiled from: GeneralRenderer.java */
/* loaded from: rt.jar:sun/java2d/loops/SolidPixelWriter.class */
class SolidPixelWriter extends PixelWriter {
    protected Object srcData;

    SolidPixelWriter(Object obj) {
        this.srcData = obj;
    }

    @Override // sun.java2d.loops.PixelWriter
    public void writePixel(int i2, int i3) {
        this.dstRast.setDataElements(i2, i3, this.srcData);
    }
}
