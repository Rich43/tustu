package sun.java2d.pipe;

import java.awt.BasicStroke;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import sun.java2d.ReentrantContext;
import sun.java2d.ReentrantContextProvider;
import sun.java2d.ReentrantContextProviderTL;
import sun.java2d.SunGraphics2D;

/* loaded from: rt.jar:sun/java2d/pipe/AAShapePipe.class */
public final class AAShapePipe implements ShapeDrawPipe, ParallelogramPipe {
    static final RenderingEngine renderengine = RenderingEngine.getInstance();
    private static final ReentrantContextProvider<TileState> tileStateProvider = new ReentrantContextProviderTL<TileState>(0) { // from class: sun.java2d.pipe.AAShapePipe.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // sun.java2d.ReentrantContextProvider
        public TileState newContext() {
            return new TileState();
        }
    };
    final CompositePipe outpipe;

    public AAShapePipe(CompositePipe compositePipe) {
        this.outpipe = compositePipe;
    }

    @Override // sun.java2d.pipe.ShapeDrawPipe
    public void draw(SunGraphics2D sunGraphics2D, Shape shape) {
        BasicStroke basicStroke;
        if (sunGraphics2D.stroke instanceof BasicStroke) {
            basicStroke = (BasicStroke) sunGraphics2D.stroke;
        } else {
            shape = sunGraphics2D.stroke.createStrokedShape(shape);
            basicStroke = null;
        }
        renderPath(sunGraphics2D, shape, basicStroke);
    }

    @Override // sun.java2d.pipe.ShapeDrawPipe
    public void fill(SunGraphics2D sunGraphics2D, Shape shape) {
        renderPath(sunGraphics2D, shape, null);
    }

    @Override // sun.java2d.pipe.ParallelogramPipe
    public void fillParallelogram(SunGraphics2D sunGraphics2D, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11) {
        TileState tileState = (TileState) tileStateProvider.acquire();
        try {
            int[] iArr = tileState.abox;
            AATileGenerator aATileGenerator = renderengine.getAATileGenerator(d6, d7, d8, d9, d10, d11, 0.0d, 0.0d, sunGraphics2D.getCompClip(), iArr);
            if (aATileGenerator != null) {
                renderTiles(sunGraphics2D, tileState.computeBBox(d2, d3, d4, d5), aATileGenerator, iArr, tileState);
            }
            tileStateProvider.release(tileState);
        } catch (Throwable th) {
            tileStateProvider.release(tileState);
            throw th;
        }
    }

    @Override // sun.java2d.pipe.ParallelogramPipe
    public void drawParallelogram(SunGraphics2D sunGraphics2D, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13) {
        TileState tileState = (TileState) tileStateProvider.acquire();
        try {
            int[] iArr = tileState.abox;
            AATileGenerator aATileGenerator = renderengine.getAATileGenerator(d6, d7, d8, d9, d10, d11, d12, d13, sunGraphics2D.getCompClip(), iArr);
            if (aATileGenerator != null) {
                renderTiles(sunGraphics2D, tileState.computeBBox(d2, d3, d4, d5), aATileGenerator, iArr, tileState);
            }
            tileStateProvider.release(tileState);
        } catch (Throwable th) {
            tileStateProvider.release(tileState);
            throw th;
        }
    }

    public void renderPath(SunGraphics2D sunGraphics2D, Shape shape, BasicStroke basicStroke) {
        boolean z2 = (basicStroke == null || sunGraphics2D.strokeHint == 2) ? false : true;
        boolean z3 = sunGraphics2D.strokeState <= 1;
        TileState tileState = (TileState) tileStateProvider.acquire();
        try {
            int[] iArr = tileState.abox;
            AATileGenerator aATileGenerator = renderengine.getAATileGenerator(shape, sunGraphics2D.transform, sunGraphics2D.getCompClip(), basicStroke, z3, z2, iArr);
            if (aATileGenerator != null) {
                renderTiles(sunGraphics2D, shape, aATileGenerator, iArr, tileState);
            }
            tileStateProvider.release(tileState);
        } catch (Throwable th) {
            tileStateProvider.release(tileState);
            throw th;
        }
    }

    public void renderTiles(SunGraphics2D sunGraphics2D, Shape shape, AATileGenerator aATileGenerator, int[] iArr, TileState tileState) {
        byte[] bArr;
        Object objStartSequence = null;
        try {
            objStartSequence = this.outpipe.startSequence(sunGraphics2D, shape, tileState.computeDevBox(iArr), iArr);
            int i2 = iArr[0];
            int i3 = iArr[1];
            int i4 = iArr[2];
            int i5 = iArr[3];
            int tileWidth = aATileGenerator.getTileWidth();
            int tileHeight = aATileGenerator.getTileHeight();
            byte[] alphaTile = tileState.getAlphaTile(tileWidth * tileHeight);
            for (int i6 = i3; i6 < i5; i6 += tileHeight) {
                int iMin = Math.min(tileHeight, i5 - i6);
                for (int i7 = i2; i7 < i4; i7 += tileWidth) {
                    int iMin2 = Math.min(tileWidth, i4 - i7);
                    int typicalAlpha = aATileGenerator.getTypicalAlpha();
                    if (typicalAlpha == 0 || !this.outpipe.needTile(objStartSequence, i7, i6, iMin2, iMin)) {
                        aATileGenerator.nextTile();
                        this.outpipe.skipTile(objStartSequence, i7, i6);
                    } else {
                        if (typicalAlpha == 255) {
                            bArr = null;
                            aATileGenerator.nextTile();
                        } else {
                            bArr = alphaTile;
                            aATileGenerator.getAlpha(alphaTile, 0, tileWidth);
                        }
                        this.outpipe.renderPathTile(objStartSequence, bArr, 0, tileWidth, i7, i6, iMin2, iMin);
                    }
                }
            }
            aATileGenerator.dispose();
            if (objStartSequence != null) {
                this.outpipe.endSequence(objStartSequence);
            }
        } catch (Throwable th) {
            aATileGenerator.dispose();
            if (objStartSequence != null) {
                this.outpipe.endSequence(objStartSequence);
            }
            throw th;
        }
    }

    /* loaded from: rt.jar:sun/java2d/pipe/AAShapePipe$TileState.class */
    static final class TileState extends ReentrantContext {
        private byte[] theTile = new byte[1024];
        final int[] abox = new int[4];
        private final Rectangle dev = new Rectangle();
        private final Rectangle2D.Double bbox2D = new Rectangle2D.Double();

        TileState() {
        }

        byte[] getAlphaTile(int i2) {
            byte[] bArr = this.theTile;
            if (bArr.length < i2) {
                byte[] bArr2 = new byte[i2];
                bArr = bArr2;
                this.theTile = bArr2;
            }
            return bArr;
        }

        Rectangle computeDevBox(int[] iArr) {
            Rectangle rectangle = this.dev;
            rectangle.f12372x = iArr[0];
            rectangle.f12373y = iArr[1];
            rectangle.width = iArr[2] - iArr[0];
            rectangle.height = iArr[3] - iArr[1];
            return rectangle;
        }

        Rectangle2D computeBBox(double d2, double d3, double d4, double d5) {
            double d6 = d4 - d2;
            double d7 = d6;
            if (d6 < 0.0d) {
                d2 += d7;
                d7 = -d7;
            }
            double d8 = d5 - d3;
            double d9 = d8;
            if (d8 < 0.0d) {
                d3 += d9;
                d9 = -d9;
            }
            Rectangle2D.Double r0 = this.bbox2D;
            r0.f12402x = d2;
            r0.f12403y = d3;
            r0.width = d7;
            r0.height = d9;
            return r0;
        }
    }
}
