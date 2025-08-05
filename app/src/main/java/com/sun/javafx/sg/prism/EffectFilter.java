package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Graphics;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.prism.PrEffectHelper;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/EffectFilter.class */
public class EffectFilter {
    private Effect effect;
    private NodeEffectInput nodeInput;

    EffectFilter(Effect effect, NGNode node) {
        this.effect = effect;
        this.nodeInput = new NodeEffectInput(node);
    }

    Effect getEffect() {
        return this.effect;
    }

    NodeEffectInput getNodeInput() {
        return this.nodeInput;
    }

    void dispose() {
        this.effect = null;
        this.nodeInput.setNode(null);
        this.nodeInput = null;
    }

    BaseBounds getBounds(BaseBounds bounds, BaseTransform xform) {
        BaseBounds r2 = getEffect().getBounds(xform, this.nodeInput);
        return bounds.deriveWithNewBounds(r2);
    }

    void render(Graphics g2) {
        NodeEffectInput nodeInput = getNodeInput();
        PrEffectHelper.render(getEffect(), g2, 0.0f, 0.0f, nodeInput);
        nodeInput.flush();
    }
}
