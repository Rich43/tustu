package com.sun.prism.impl.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.util.Logging;
import com.sun.openpisces.AlphaConsumer;
import com.sun.openpisces.Renderer;
import com.sun.prism.BasicStroke;
import com.sun.prism.impl.PrismSettings;
import java.nio.ByteBuffer;
import java.util.Arrays;

/* loaded from: jfxrt.jar:com/sun/prism/impl/shape/OpenPiscesRasterizer.class */
public class OpenPiscesRasterizer implements ShapeRasterizer {
    private static MaskData emptyData = MaskData.create(new byte[1], 0, 0, 1, 1);
    private static Consumer savedConsumer;

    @Override // com.sun.prism.impl.shape.ShapeRasterizer
    public MaskData getMaskData(Shape shape, BasicStroke stroke, RectBounds xformBounds, BaseTransform xform, boolean close, boolean antialiasedShape) {
        if (stroke != null && stroke.getType() != 0) {
            shape = stroke.createStrokedShape(shape);
            stroke = null;
        }
        if (xformBounds == null) {
            if (stroke != null) {
                shape = stroke.createStrokedShape(shape);
                stroke = null;
            }
            xformBounds = (RectBounds) xform.transform(shape.getBounds(), new RectBounds());
        }
        Rectangle rclip = new Rectangle(xformBounds);
        if (rclip.isEmpty()) {
            return emptyData;
        }
        Renderer renderer = null;
        try {
            if (shape instanceof Path2D) {
                renderer = OpenPiscesPrismUtils.setupRenderer((Path2D) shape, stroke, xform, rclip, antialiasedShape);
            }
            if (renderer == null) {
                renderer = OpenPiscesPrismUtils.setupRenderer(shape, stroke, xform, rclip, antialiasedShape);
            }
            int outpix_xmin = renderer.getOutpixMinX();
            int outpix_ymin = renderer.getOutpixMinY();
            int outpix_xmax = renderer.getOutpixMaxX();
            int outpix_ymax = renderer.getOutpixMaxY();
            int w2 = outpix_xmax - outpix_xmin;
            int h2 = outpix_ymax - outpix_ymin;
            if (w2 <= 0 || h2 <= 0) {
                return emptyData;
            }
            Consumer consumer = savedConsumer;
            if (consumer == null || w2 * h2 > consumer.getAlphaLength()) {
                int csize = ((w2 * h2) + 4095) & (-4096);
                Consumer consumer2 = new Consumer(csize);
                consumer = consumer2;
                savedConsumer = consumer2;
                if (PrismSettings.verbose) {
                    System.out.println("new alphas");
                }
            }
            consumer.setBoundsNoClone(outpix_xmin, outpix_ymin, w2, h2);
            renderer.produceAlphas(consumer);
            return consumer.getMaskData();
        } catch (Throwable ex) {
            if (PrismSettings.verbose) {
                ex.printStackTrace();
            }
            Logging.getJavaFXLogger().warning("Cannot rasterize Shape: " + ex.toString());
            return emptyData;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/prism/impl/shape/OpenPiscesRasterizer$Consumer.class */
    private static class Consumer implements AlphaConsumer {
        static byte[] savedAlphaMap;

        /* renamed from: x, reason: collision with root package name */
        int f12022x;

        /* renamed from: y, reason: collision with root package name */
        int f12023y;
        int width;
        int height;
        byte[] alphas;
        byte[] alphaMap;
        ByteBuffer alphabuffer;
        MaskData maskdata = new MaskData();

        public Consumer(int alphalen) {
            this.alphas = new byte[alphalen];
            this.alphabuffer = ByteBuffer.wrap(this.alphas);
        }

        public void setBoundsNoClone(int x2, int y2, int w2, int h2) {
            this.f12022x = x2;
            this.f12023y = y2;
            this.width = w2;
            this.height = h2;
            this.maskdata.update(this.alphabuffer, x2, y2, w2, h2);
        }

        @Override // com.sun.openpisces.AlphaConsumer
        public int getOriginX() {
            return this.f12022x;
        }

        @Override // com.sun.openpisces.AlphaConsumer
        public int getOriginY() {
            return this.f12023y;
        }

        @Override // com.sun.openpisces.AlphaConsumer
        public int getWidth() {
            return this.width;
        }

        @Override // com.sun.openpisces.AlphaConsumer
        public int getHeight() {
            return this.height;
        }

        public byte[] getAlphasNoClone() {
            return this.alphas;
        }

        public int getAlphaLength() {
            return this.alphas.length;
        }

        public MaskData getMaskData() {
            return this.maskdata;
        }

        @Override // com.sun.openpisces.AlphaConsumer
        public void setMaxAlpha(int maxalpha) {
            byte[] map = savedAlphaMap;
            if (map == null || map.length != maxalpha + 1) {
                map = new byte[maxalpha + 1];
                for (int i2 = 0; i2 <= maxalpha; i2++) {
                    map[i2] = (byte) (((i2 * 255) + (maxalpha / 2)) / maxalpha);
                }
                savedAlphaMap = map;
            }
            this.alphaMap = map;
        }

        @Override // com.sun.openpisces.AlphaConsumer
        public void setAndClearRelativeAlphas(int[] alphaRow, int pix_y, int pix_from, int pix_to) {
            int w2 = this.width;
            int off = (pix_y - this.f12023y) * w2;
            byte[] out = this.alphas;
            byte[] map = this.alphaMap;
            int a2 = 0;
            for (int i2 = 0; i2 < w2; i2++) {
                a2 += alphaRow[i2];
                alphaRow[i2] = 0;
                out[off + i2] = map[a2];
            }
        }

        public void setAndClearRelativeAlphas2(int[] alphaDeltas, int pix_y, int pix_from, int pix_to) {
            if (pix_to >= pix_from) {
                byte[] out = this.alphas;
                byte[] map = this.alphaMap;
                int from = pix_from - this.f12022x;
                int to = pix_to - this.f12022x;
                int w2 = this.width;
                int off = (pix_y - this.f12023y) * w2;
                int i2 = 0;
                while (i2 < from) {
                    out[off + i2] = 0;
                    i2++;
                }
                int curAlpha = 0;
                while (i2 <= to) {
                    curAlpha += alphaDeltas[i2];
                    alphaDeltas[i2] = 0;
                    byte a2 = map[curAlpha];
                    out[off + i2] = a2;
                    i2++;
                }
                alphaDeltas[i2] = 0;
                while (i2 < w2) {
                    out[off + i2] = 0;
                    i2++;
                }
                return;
            }
            Arrays.fill(alphaDeltas, 0);
        }
    }
}
