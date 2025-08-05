package sun.java2d.pipe;

import java.awt.Rectangle;
import java.awt.Shape;
import sun.java2d.SunGraphics2D;

/* loaded from: rt.jar:sun/java2d/pipe/SpanClipRenderer.class */
public class SpanClipRenderer implements CompositePipe {
    CompositePipe outpipe;
    static Class RegionClass = Region.class;
    static Class RegionIteratorClass = RegionIterator.class;

    static native void initIDs(Class cls, Class cls2);

    public native void fillTile(RegionIterator regionIterator, byte[] bArr, int i2, int i3, int[] iArr);

    public native void eraseTile(RegionIterator regionIterator, byte[] bArr, int i2, int i3, int[] iArr);

    static {
        initIDs(RegionClass, RegionIteratorClass);
    }

    public SpanClipRenderer(CompositePipe compositePipe) {
        this.outpipe = compositePipe;
    }

    /* loaded from: rt.jar:sun/java2d/pipe/SpanClipRenderer$SCRcontext.class */
    class SCRcontext {
        RegionIterator iterator;
        Object outcontext;
        int[] band = new int[4];
        byte[] tile;

        public SCRcontext(RegionIterator regionIterator, Object obj) {
            this.iterator = regionIterator;
            this.outcontext = obj;
        }
    }

    @Override // sun.java2d.pipe.CompositePipe
    public Object startSequence(SunGraphics2D sunGraphics2D, Shape shape, Rectangle rectangle, int[] iArr) {
        return new SCRcontext(sunGraphics2D.clipRegion.getIterator(), this.outpipe.startSequence(sunGraphics2D, shape, rectangle, iArr));
    }

    @Override // sun.java2d.pipe.CompositePipe
    public boolean needTile(Object obj, int i2, int i3, int i4, int i5) {
        return this.outpipe.needTile(((SCRcontext) obj).outcontext, i2, i3, i4, i5);
    }

    public void renderPathTile(Object obj, byte[] bArr, int i2, int i3, int i4, int i5, int i6, int i7, ShapeSpanIterator shapeSpanIterator) {
        renderPathTile(obj, bArr, i2, i3, i4, i5, i6, i7);
    }

    @Override // sun.java2d.pipe.CompositePipe
    public void renderPathTile(Object obj, byte[] bArr, int i2, int i3, int i4, int i5, int i6, int i7) {
        SCRcontext sCRcontext = (SCRcontext) obj;
        RegionIterator regionIteratorCreateCopy = sCRcontext.iterator.createCopy();
        int[] iArr = sCRcontext.band;
        iArr[0] = i4;
        iArr[1] = i5;
        iArr[2] = i4 + i6;
        iArr[3] = i5 + i7;
        if (bArr == null) {
            int i8 = i6 * i7;
            bArr = sCRcontext.tile;
            if (bArr != null && bArr.length < i8) {
                bArr = null;
            }
            if (bArr == null) {
                bArr = new byte[i8];
                sCRcontext.tile = bArr;
            }
            i2 = 0;
            i3 = i6;
            fillTile(regionIteratorCreateCopy, bArr, 0, i3, iArr);
        } else {
            eraseTile(regionIteratorCreateCopy, bArr, i2, i3, iArr);
        }
        if (iArr[2] > iArr[0] && iArr[3] > iArr[1]) {
            this.outpipe.renderPathTile(sCRcontext.outcontext, bArr, i2 + ((iArr[1] - i5) * i3) + (iArr[0] - i4), i3, iArr[0], iArr[1], iArr[2] - iArr[0], iArr[3] - iArr[1]);
        }
    }

    @Override // sun.java2d.pipe.CompositePipe
    public void skipTile(Object obj, int i2, int i3) {
        this.outpipe.skipTile(((SCRcontext) obj).outcontext, i2, i3);
    }

    @Override // sun.java2d.pipe.CompositePipe
    public void endSequence(Object obj) {
        this.outpipe.endSequence(((SCRcontext) obj).outcontext);
    }
}
