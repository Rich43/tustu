package javafx.animation;

import com.sun.scenario.animation.shared.AnimationAccessor;

/* loaded from: jfxrt.jar:javafx/animation/AnimationAccessorImpl.class */
final class AnimationAccessorImpl extends AnimationAccessor {
    AnimationAccessorImpl() {
    }

    @Override // com.sun.scenario.animation.shared.AnimationAccessor
    public void setCurrentRate(Animation animation, double currentRate) {
        animation.impl_setCurrentRate(currentRate);
    }

    @Override // com.sun.scenario.animation.shared.AnimationAccessor
    public void playTo(Animation animation, long pos, long cycleTicks) {
        animation.impl_playTo(pos, cycleTicks);
    }

    @Override // com.sun.scenario.animation.shared.AnimationAccessor
    public void jumpTo(Animation animation, long pos, long cycleTicks, boolean forceJump) {
        animation.impl_jumpTo(pos, cycleTicks, forceJump);
    }

    @Override // com.sun.scenario.animation.shared.AnimationAccessor
    public void finished(Animation animation) {
        animation.impl_finished();
    }

    @Override // com.sun.scenario.animation.shared.AnimationAccessor
    public void setCurrentTicks(Animation animation, long ticks) {
        animation.impl_setCurrentTicks(ticks);
    }
}
