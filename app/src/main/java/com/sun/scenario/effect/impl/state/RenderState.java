package com.sun.scenario.effect.impl.state;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/state/RenderState.class */
public interface RenderState {
    public static final RenderState UserSpaceRenderState = new RenderState() { // from class: com.sun.scenario.effect.impl.state.RenderState.1
        @Override // com.sun.scenario.effect.impl.state.RenderState
        public EffectCoordinateSpace getEffectTransformSpace() {
            return EffectCoordinateSpace.UserSpace;
        }

        @Override // com.sun.scenario.effect.impl.state.RenderState
        public BaseTransform getInputTransform(BaseTransform filterTransform) {
            return BaseTransform.IDENTITY_TRANSFORM;
        }

        @Override // com.sun.scenario.effect.impl.state.RenderState
        public BaseTransform getResultTransform(BaseTransform filterTransform) {
            return filterTransform;
        }

        @Override // com.sun.scenario.effect.impl.state.RenderState
        public Rectangle getInputClip(int i2, Rectangle filterClip) {
            return filterClip;
        }
    };
    public static final RenderState UnclippedUserSpaceRenderState = new RenderState() { // from class: com.sun.scenario.effect.impl.state.RenderState.2
        @Override // com.sun.scenario.effect.impl.state.RenderState
        public EffectCoordinateSpace getEffectTransformSpace() {
            return EffectCoordinateSpace.UserSpace;
        }

        @Override // com.sun.scenario.effect.impl.state.RenderState
        public BaseTransform getInputTransform(BaseTransform filterTransform) {
            return BaseTransform.IDENTITY_TRANSFORM;
        }

        @Override // com.sun.scenario.effect.impl.state.RenderState
        public BaseTransform getResultTransform(BaseTransform filterTransform) {
            return filterTransform;
        }

        @Override // com.sun.scenario.effect.impl.state.RenderState
        public Rectangle getInputClip(int i2, Rectangle filterClip) {
            return null;
        }
    };
    public static final RenderState RenderSpaceRenderState = new RenderState() { // from class: com.sun.scenario.effect.impl.state.RenderState.3
        @Override // com.sun.scenario.effect.impl.state.RenderState
        public EffectCoordinateSpace getEffectTransformSpace() {
            return EffectCoordinateSpace.RenderSpace;
        }

        @Override // com.sun.scenario.effect.impl.state.RenderState
        public BaseTransform getInputTransform(BaseTransform filterTransform) {
            return filterTransform;
        }

        @Override // com.sun.scenario.effect.impl.state.RenderState
        public BaseTransform getResultTransform(BaseTransform filterTransform) {
            return BaseTransform.IDENTITY_TRANSFORM;
        }

        @Override // com.sun.scenario.effect.impl.state.RenderState
        public Rectangle getInputClip(int i2, Rectangle filterClip) {
            return filterClip;
        }
    };

    /* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/state/RenderState$EffectCoordinateSpace.class */
    public enum EffectCoordinateSpace {
        UserSpace,
        CustomSpace,
        RenderSpace
    }

    EffectCoordinateSpace getEffectTransformSpace();

    BaseTransform getInputTransform(BaseTransform baseTransform);

    BaseTransform getResultTransform(BaseTransform baseTransform);

    Rectangle getInputClip(int i2, Rectangle rectangle);
}
