package org.icepdf.core.pobjects.graphics;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;
import org.apache.commons.net.tftp.TFTP;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.graphics.commands.DrawCmd;
import org.icepdf.core.pobjects.graphics.commands.ImageDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.ShapesDrawCmd;
import org.icepdf.core.pobjects.graphics.text.PageText;
import org.icepdf.core.util.Defs;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/Shapes.class */
public class Shapes {
    private static final Logger logger = Logger.getLogger(Shapes.class.toString());
    private static int shapesInitialCapacity;
    protected boolean paintAlpha;
    private int rule;
    private float alpha;
    private boolean interrupted;
    protected ArrayList<DrawCmd> shapes;
    protected OptionalContentState optionalContentState;
    private Page parentPage;
    private PageText pageText;

    public Shapes() {
        this.paintAlpha = !Defs.sysPropertyBoolean("org.icepdf.core.paint.disableAlpha", false);
        this.shapes = new ArrayList<>(shapesInitialCapacity);
        this.optionalContentState = new OptionalContentState();
        this.pageText = new PageText();
    }

    static {
        shapesInitialCapacity = TFTP.DEFAULT_TIMEOUT;
        shapesInitialCapacity = Defs.sysPropertyInt("org.icepdf.core.shapes.initialCapacity", shapesInitialCapacity);
    }

    public PageText getPageText() {
        return this.pageText;
    }

    public int getShapesCount() {
        if (this.shapes != null) {
            return this.shapes.size();
        }
        return 0;
    }

    public ArrayList<DrawCmd> getShapes() {
        return this.shapes;
    }

    public void add(ArrayList<DrawCmd> shapes) {
        shapes.addAll(shapes);
    }

    public void setPageParent(Page parent) {
        this.parentPage = parent;
    }

    public void add(DrawCmd drawCmd) {
        this.shapes.add(drawCmd);
    }

    public boolean isPaintAlpha() {
        return this.paintAlpha;
    }

    public void setPaintAlpha(boolean paintAlpha) {
        this.paintAlpha = paintAlpha;
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x003c, code lost:
    
        r10.interrupted = false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void paint(java.awt.Graphics2D r11) {
        /*
            r10 = this;
            r0 = r10
            r1 = 0
            r0.interrupted = r1     // Catch: java.lang.Exception -> L77
            java.awt.geom.AffineTransform r0 = new java.awt.geom.AffineTransform     // Catch: java.lang.Exception -> L77
            r1 = r0
            r2 = r11
            java.awt.geom.AffineTransform r2 = r2.getTransform()     // Catch: java.lang.Exception -> L77
            r1.<init>(r2)     // Catch: java.lang.Exception -> L77
            r12 = r0
            r0 = r11
            java.awt.Shape r0 = r0.getClip()     // Catch: java.lang.Exception -> L77
            r13 = r0
            org.icepdf.core.pobjects.graphics.PaintTimer r0 = new org.icepdf.core.pobjects.graphics.PaintTimer     // Catch: java.lang.Exception -> L77
            r1 = r0
            r1.<init>()     // Catch: java.lang.Exception -> L77
            r14 = r0
            r0 = 0
            r15 = r0
            r0 = 0
            r17 = r0
            r0 = r10
            java.util.ArrayList<org.icepdf.core.pobjects.graphics.commands.DrawCmd> r0 = r0.shapes     // Catch: java.lang.Exception -> L77
            int r0 = r0.size()     // Catch: java.lang.Exception -> L77
            r18 = r0
        L2e:
            r0 = r17
            r1 = r18
            if (r0 >= r1) goto L74
            r0 = r10
            boolean r0 = r0.interrupted     // Catch: java.lang.Exception -> L77
            if (r0 == 0) goto L44
            r0 = r10
            r1 = 0
            r0.interrupted = r1     // Catch: java.lang.Exception -> L77
            goto L74
        L44:
            r0 = r10
            java.util.ArrayList<org.icepdf.core.pobjects.graphics.commands.DrawCmd> r0 = r0.shapes     // Catch: java.lang.Exception -> L77
            r1 = r17
            java.lang.Object r0 = r0.get(r1)     // Catch: java.lang.Exception -> L77
            org.icepdf.core.pobjects.graphics.commands.DrawCmd r0 = (org.icepdf.core.pobjects.graphics.commands.DrawCmd) r0     // Catch: java.lang.Exception -> L77
            r16 = r0
            r0 = r16
            r1 = r11
            r2 = r10
            org.icepdf.core.pobjects.Page r2 = r2.parentPage     // Catch: java.lang.Exception -> L77
            r3 = r15
            r4 = r13
            r5 = r12
            r6 = r10
            org.icepdf.core.pobjects.graphics.OptionalContentState r6 = r6.optionalContentState     // Catch: java.lang.Exception -> L77
            r7 = r10
            boolean r7 = r7.paintAlpha     // Catch: java.lang.Exception -> L77
            r8 = r14
            java.awt.Shape r0 = r0.paintOperand(r1, r2, r3, r4, r5, r6, r7, r8)     // Catch: java.lang.Exception -> L77
            r15 = r0
            int r17 = r17 + 1
            goto L2e
        L74:
            goto L84
        L77:
            r12 = move-exception
            java.util.logging.Logger r0 = org.icepdf.core.pobjects.graphics.Shapes.logger
            java.util.logging.Level r1 = java.util.logging.Level.FINE
            java.lang.String r2 = "Error painting shapes."
            r3 = r12
            r0.log(r1, r2, r3)
        L84:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.icepdf.core.pobjects.graphics.Shapes.paint(java.awt.Graphics2D):void");
    }

    public void interruptPaint() {
        this.interrupted = true;
    }

    public boolean isInterrupted() {
        return this.interrupted;
    }

    public ArrayList<Image> getImages() {
        ArrayList<Image> images = new ArrayList<>();
        Iterator i$ = this.shapes.iterator();
        while (i$.hasNext()) {
            Object object = (DrawCmd) i$.next();
            if (object instanceof ImageDrawCmd) {
                images.add(((ImageDrawCmd) object).getImage());
            } else if (object instanceof ShapesDrawCmd) {
                images.addAll(((ShapesDrawCmd) object).getShapes().getImages());
            }
        }
        return images;
    }

    public void contract() {
        if (this.shapes != null) {
            this.shapes.trimToSize();
        }
    }

    public int getRule() {
        return this.rule;
    }

    public void setRule(int rule) {
        this.rule = rule;
    }

    public float getAlpha() {
        return this.alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}
