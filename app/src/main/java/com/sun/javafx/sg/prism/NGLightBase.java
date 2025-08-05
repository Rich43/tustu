package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Graphics;
import com.sun.prism.paint.Color;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGLightBase.class */
public class NGLightBase extends NGNode {
    private Affine3D worldTransform;
    private Color color = Color.WHITE;
    private boolean lightOn = true;
    Object[] scopedNodes = null;

    protected NGLightBase() {
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    public void setTransformMatrix(BaseTransform tx) {
        super.setTransformMatrix(tx);
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected void doRender(Graphics g2) {
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected void renderContent(Graphics g2) {
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected boolean hasOverlappingContents() {
        return false;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Object value) {
        if (!this.color.equals(value)) {
            this.color = (Color) value;
            visualsChanged();
        }
    }

    public boolean isLightOn() {
        return this.lightOn;
    }

    public void setLightOn(boolean value) {
        if (this.lightOn != value) {
            visualsChanged();
            this.lightOn = value;
        }
    }

    public Affine3D getWorldTransform() {
        return this.worldTransform;
    }

    public void setWorldTransform(Affine3D localToSceneTx) {
        this.worldTransform = localToSceneTx;
    }

    public void setScope(Object[] scopedNodes) {
        if (this.scopedNodes != scopedNodes) {
            this.scopedNodes = scopedNodes;
            visualsChanged();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x0052, code lost:
    
        continue;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    final boolean affects(com.sun.javafx.sg.prism.NGShape3D r4) {
        /*
            r3 = this;
            r0 = r3
            boolean r0 = r0.lightOn
            if (r0 != 0) goto L9
            r0 = 0
            return r0
        L9:
            r0 = r3
            java.lang.Object[] r0 = r0.scopedNodes
            if (r0 != 0) goto L12
            r0 = 1
            return r0
        L12:
            r0 = 0
            r5 = r0
        L14:
            r0 = r5
            r1 = r3
            java.lang.Object[] r1 = r1.scopedNodes
            int r1 = r1.length
            if (r0 >= r1) goto L58
            r0 = r3
            java.lang.Object[] r0 = r0.scopedNodes
            r1 = r5
            r0 = r0[r1]
            r6 = r0
            r0 = r6
            boolean r0 = r0 instanceof com.sun.javafx.sg.prism.NGGroup
            if (r0 == 0) goto L4b
            r0 = r4
            com.sun.javafx.sg.prism.NGNode r0 = r0.getParent()
            r7 = r0
        L31:
            r0 = r7
            if (r0 == 0) goto L48
            r0 = r6
            r1 = r7
            if (r0 != r1) goto L3e
            r0 = 1
            return r0
        L3e:
            r0 = r7
            com.sun.javafx.sg.prism.NGNode r0 = r0.getParent()
            r7 = r0
            goto L31
        L48:
            goto L52
        L4b:
            r0 = r6
            r1 = r4
            if (r0 != r1) goto L52
            r0 = 1
            return r0
        L52:
            int r5 = r5 + 1
            goto L14
        L58:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.sg.prism.NGLightBase.affects(com.sun.javafx.sg.prism.NGShape3D):boolean");
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    public void release() {
    }
}
