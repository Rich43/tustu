package com.sun.prism.d3d;

import com.sun.javafx.geom.transform.Affine3D;
import com.sun.prism.CompositeMode;
import com.sun.prism.Graphics;
import com.sun.prism.RenderTarget;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.ps.BaseShaderGraphics;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;

/* loaded from: jfxrt.jar:com/sun/prism/d3d/D3DGraphics.class */
class D3DGraphics extends BaseShaderGraphics implements D3DContextSource {
    private D3DContext context;

    private static native int nClear(long j2, int i2, boolean z2, boolean z3);

    private D3DGraphics(D3DContext context, RenderTarget target) {
        super(context, target);
        this.context = context;
    }

    @Override // com.sun.prism.impl.ps.BaseShaderGraphics, com.sun.prism.ps.ShaderGraphics
    public void getPaintShaderTransform(Affine3D ret) {
        super.getPaintShaderTransform(ret);
        ret.preTranslate(-0.5d, -0.5d, 0.0d);
    }

    static Graphics create(RenderTarget target, D3DContext context) {
        if (target == null) {
            return null;
        }
        long resourceHandle = ((D3DRenderTarget) target).getResourceHandle();
        if (resourceHandle == 0) {
            return null;
        }
        if (PrismSettings.verbose && context.isLost()) {
            System.err.println("Create graphics while the device is lost");
        }
        return new D3DGraphics(context, target);
    }

    @Override // com.sun.prism.Graphics
    public void clearQuad(float x1, float y1, float x2, float y2) {
        this.context.setRenderTarget(this);
        this.context.flushVertexBuffer();
        CompositeMode oldMode = getCompositeMode();
        setCompositeMode(CompositeMode.CLEAR);
        Paint oldPaint = getPaint();
        setPaint(Color.BLACK);
        fillQuad(x1, y1, x2, y2);
        this.context.flushVertexBuffer();
        setPaint(oldPaint);
        setCompositeMode(oldMode);
    }

    @Override // com.sun.prism.Graphics
    public void clear(Color color) {
        this.context.validateClearOp(this);
        getRenderTarget().setOpaque(color.isOpaque());
        int res = nClear(this.context.getContextHandle(), color.getIntArgbPre(), isDepthBuffer(), false);
        D3DContext d3DContext = this.context;
        D3DContext.validate(res);
    }

    @Override // com.sun.prism.Graphics
    public void sync() {
        this.context.flushVertexBuffer();
    }

    @Override // com.sun.prism.d3d.D3DContextSource
    public D3DContext getContext() {
        return this.context;
    }
}
