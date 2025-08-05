package javafx.animation;

import com.sun.javafx.tk.Toolkit;
import com.sun.scenario.animation.AbstractMasterTimer;
import com.sun.scenario.animation.shared.TimerReceiver;
import java.security.AccessControlContext;
import java.security.AccessController;

/* loaded from: jfxrt.jar:javafx/animation/AnimationTimer.class */
public abstract class AnimationTimer {
    private final AbstractMasterTimer timer;
    private final AnimationTimerReceiver timerReceiver;
    private boolean active;
    private AccessControlContext accessCtrlCtx;

    public abstract void handle(long j2);

    /* loaded from: jfxrt.jar:javafx/animation/AnimationTimer$AnimationTimerReceiver.class */
    private class AnimationTimerReceiver implements TimerReceiver {
        private AnimationTimerReceiver() {
        }

        @Override // com.sun.scenario.animation.shared.TimerReceiver
        public void handle(long now) {
            if (AnimationTimer.this.accessCtrlCtx == null) {
                throw new IllegalStateException("Error: AccessControlContext not captured");
            }
            AccessController.doPrivileged(() -> {
                AnimationTimer.this.handle(now);
                return null;
            }, AnimationTimer.this.accessCtrlCtx);
        }
    }

    public AnimationTimer() {
        this.timerReceiver = new AnimationTimerReceiver();
        this.accessCtrlCtx = null;
        this.timer = Toolkit.getToolkit().getMasterTimer();
    }

    AnimationTimer(AbstractMasterTimer timer) {
        this.timerReceiver = new AnimationTimerReceiver();
        this.accessCtrlCtx = null;
        this.timer = timer;
    }

    public void start() {
        if (!this.active) {
            this.accessCtrlCtx = AccessController.getContext();
            this.timer.addAnimationTimer(this.timerReceiver);
            this.active = true;
        }
    }

    public void stop() {
        if (this.active) {
            this.timer.removeAnimationTimer(this.timerReceiver);
            this.active = false;
        }
    }
}
