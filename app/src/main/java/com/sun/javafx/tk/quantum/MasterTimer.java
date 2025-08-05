package com.sun.javafx.tk.quantum;

import com.sun.javafx.tk.Toolkit;
import com.sun.scenario.DelayedRunnable;
import com.sun.scenario.Settings;
import com.sun.scenario.animation.AbstractMasterTimer;
import com.sun.scenario.animation.AnimationPulse;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/MasterTimer.class */
public final class MasterTimer extends AbstractMasterTimer {
    private static final Object MASTER_TIMER_KEY = new StringBuilder("MasterTimerKey");

    private MasterTimer() {
    }

    public static synchronized MasterTimer getInstance() {
        Map<Object, Object> contextMap = Toolkit.getToolkit().getContextMap();
        MasterTimer instance = (MasterTimer) contextMap.get(MASTER_TIMER_KEY);
        if (instance == null) {
            instance = new MasterTimer();
            contextMap.put(MASTER_TIMER_KEY, instance);
            if (Settings.getBoolean("com.sun.scenario.animation.AnimationMBean.enabled", false)) {
                AnimationPulse.getDefaultBean().setEnabled(true);
            }
        }
        return instance;
    }

    @Override // com.sun.scenario.animation.AbstractMasterTimer
    protected int getPulseDuration(int precision) {
        int retVal = precision / 60;
        if (Settings.get("javafx.animation.framerate") != null) {
            int overrideHz = Settings.getInt("javafx.animation.framerate", 60);
            if (overrideHz > 0) {
                retVal = precision / overrideHz;
            }
        } else if (Settings.get("javafx.animation.pulse") != null) {
            int overrideHz2 = Settings.getInt("javafx.animation.pulse", 60);
            if (overrideHz2 > 0) {
                retVal = precision / overrideHz2;
            }
        } else {
            int rate = Toolkit.getToolkit().getRefreshRate();
            if (rate > 0) {
                retVal = precision / rate;
            }
        }
        return retVal;
    }

    @Override // com.sun.scenario.animation.AbstractMasterTimer
    protected void postUpdateAnimationRunnable(DelayedRunnable animationRunnable) {
        Toolkit.getToolkit().setAnimationRunnable(animationRunnable);
    }

    @Override // com.sun.scenario.animation.AbstractMasterTimer
    protected void recordStart(long shiftMillis) {
        AnimationPulse.getDefaultBean().recordStart(shiftMillis);
    }

    @Override // com.sun.scenario.animation.AbstractMasterTimer
    protected void recordEnd() {
        AnimationPulse.getDefaultBean().recordEnd();
    }

    @Override // com.sun.scenario.animation.AbstractMasterTimer
    protected void recordAnimationEnd() {
        AnimationPulse.getDefaultBean().recordAnimationEnd();
    }
}
