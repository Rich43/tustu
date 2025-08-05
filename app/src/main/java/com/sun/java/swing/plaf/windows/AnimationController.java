package com.sun.java.swing.plaf.windows;

import com.sun.java.swing.plaf.windows.TMSchema;
import com.sun.java.swing.plaf.windows.XPStyle;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import sun.awt.AppContext;
import sun.security.action.GetBooleanAction;
import sun.swing.UIClientPropertyKey;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/AnimationController.class */
class AnimationController implements ActionListener, PropertyChangeListener {
    private static final boolean VISTA_ANIMATION_DISABLED = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("swing.disablevistaanimation"))).booleanValue();
    private static final Object ANIMATION_CONTROLLER_KEY = new StringBuilder("ANIMATION_CONTROLLER_KEY");
    private final Map<JComponent, Map<TMSchema.Part, AnimationState>> animationStateMap = new WeakHashMap();
    private final Timer timer = new Timer(33, this);

    private static synchronized AnimationController getAnimationController() {
        AppContext appContext = AppContext.getAppContext();
        Object animationController = appContext.get(ANIMATION_CONTROLLER_KEY);
        if (animationController == null) {
            animationController = new AnimationController();
            appContext.put(ANIMATION_CONTROLLER_KEY, animationController);
        }
        return (AnimationController) animationController;
    }

    private AnimationController() {
        this.timer.setRepeats(true);
        this.timer.setCoalesce(true);
        UIManager.addPropertyChangeListener(this);
    }

    private static void triggerAnimation(JComponent jComponent, TMSchema.Part part, TMSchema.State state) {
        long themeTransitionDuration;
        if ((jComponent instanceof JTabbedPane) || part == TMSchema.Part.TP_BUTTON) {
            return;
        }
        AnimationController animationController = getAnimationController();
        TMSchema.State state2 = animationController.getState(jComponent, part);
        if (state2 != state) {
            animationController.putState(jComponent, part, state);
            if (state == TMSchema.State.DEFAULTED) {
                state2 = TMSchema.State.HOT;
            }
            if (state2 != null) {
                if (state == TMSchema.State.DEFAULTED) {
                    themeTransitionDuration = 1000;
                } else {
                    XPStyle xp = XPStyle.getXP();
                    themeTransitionDuration = xp != null ? xp.getThemeTransitionDuration(jComponent, part, normalizeState(state2), normalizeState(state), TMSchema.Prop.TRANSITIONDURATIONS) : 1000L;
                }
                animationController.startAnimation(jComponent, part, state2, state, themeTransitionDuration);
            }
        }
    }

    private static TMSchema.State normalizeState(TMSchema.State state) {
        TMSchema.State state2;
        switch (state) {
            case DOWNPRESSED:
            case LEFTPRESSED:
            case RIGHTPRESSED:
                state2 = TMSchema.State.UPPRESSED;
                break;
            case DOWNDISABLED:
            case LEFTDISABLED:
            case RIGHTDISABLED:
                state2 = TMSchema.State.UPDISABLED;
                break;
            case DOWNHOT:
            case LEFTHOT:
            case RIGHTHOT:
                state2 = TMSchema.State.UPHOT;
                break;
            case DOWNNORMAL:
            case LEFTNORMAL:
            case RIGHTNORMAL:
                state2 = TMSchema.State.UPNORMAL;
                break;
            default:
                state2 = state;
                break;
        }
        return state2;
    }

    private synchronized TMSchema.State getState(JComponent jComponent, TMSchema.Part part) {
        TMSchema.State state = null;
        Object clientProperty = jComponent.getClientProperty(PartUIClientPropertyKey.getKey(part));
        if (clientProperty instanceof TMSchema.State) {
            state = (TMSchema.State) clientProperty;
        }
        return state;
    }

    private synchronized void putState(JComponent jComponent, TMSchema.Part part, TMSchema.State state) {
        jComponent.putClientProperty(PartUIClientPropertyKey.getKey(part), state);
    }

    private synchronized void startAnimation(JComponent jComponent, TMSchema.Part part, TMSchema.State state, TMSchema.State state2, long j2) {
        boolean z2 = false;
        if (state2 == TMSchema.State.DEFAULTED) {
            z2 = true;
        }
        Map<TMSchema.Part, AnimationState> enumMap = this.animationStateMap.get(jComponent);
        if (j2 <= 0) {
            if (enumMap != null) {
                enumMap.remove(part);
                if (enumMap.size() == 0) {
                    this.animationStateMap.remove(jComponent);
                    return;
                }
                return;
            }
            return;
        }
        if (enumMap == null) {
            enumMap = new EnumMap(TMSchema.Part.class);
            this.animationStateMap.put(jComponent, enumMap);
        }
        enumMap.put(part, new AnimationState(state, j2, z2));
        if (!this.timer.isRunning()) {
            this.timer.start();
        }
    }

    static void paintSkin(JComponent jComponent, XPStyle.Skin skin, Graphics graphics, int i2, int i3, int i4, int i5, TMSchema.State state) {
        if (VISTA_ANIMATION_DISABLED) {
            skin.paintSkinRaw(graphics, i2, i3, i4, i5, state);
            return;
        }
        triggerAnimation(jComponent, skin.part, state);
        AnimationController animationController = getAnimationController();
        synchronized (animationController) {
            AnimationState animationState = null;
            Map<TMSchema.Part, AnimationState> map = animationController.animationStateMap.get(jComponent);
            if (map != null) {
                animationState = map.get(skin.part);
            }
            if (animationState != null) {
                animationState.paintSkin(skin, graphics, i2, i3, i4, i5, state);
            } else {
                skin.paintSkinRaw(graphics, i2, i3, i4, i5, state);
            }
        }
    }

    @Override // java.beans.PropertyChangeListener
    public synchronized void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if ("lookAndFeel" == propertyChangeEvent.getPropertyName() && !(propertyChangeEvent.getNewValue() instanceof WindowsLookAndFeel)) {
            dispose();
        }
    }

    @Override // java.awt.event.ActionListener
    public synchronized void actionPerformed(ActionEvent actionEvent) {
        ArrayList arrayList = null;
        ArrayList arrayList2 = null;
        for (JComponent jComponent : this.animationStateMap.keySet()) {
            jComponent.repaint();
            if (arrayList2 != null) {
                arrayList2.clear();
            }
            Map<TMSchema.Part, AnimationState> map = this.animationStateMap.get(jComponent);
            if (!jComponent.isShowing() || map == null || map.size() == 0) {
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                arrayList.add(jComponent);
            } else {
                for (TMSchema.Part part : map.keySet()) {
                    if (map.get(part).isDone()) {
                        if (arrayList2 == null) {
                            arrayList2 = new ArrayList();
                        }
                        arrayList2.add(part);
                    }
                }
                if (arrayList2 != null) {
                    if (arrayList2.size() == map.size()) {
                        if (arrayList == null) {
                            arrayList = new ArrayList();
                        }
                        arrayList.add(jComponent);
                    } else {
                        Iterator it = arrayList2.iterator();
                        while (it.hasNext()) {
                            map.remove((TMSchema.Part) it.next());
                        }
                    }
                }
            }
        }
        if (arrayList != null) {
            Iterator it2 = arrayList.iterator();
            while (it2.hasNext()) {
                this.animationStateMap.remove((JComponent) it2.next());
            }
        }
        if (this.animationStateMap.size() == 0) {
            this.timer.stop();
        }
    }

    private synchronized void dispose() {
        this.timer.stop();
        UIManager.removePropertyChangeListener(this);
        synchronized (AnimationController.class) {
            AppContext.getAppContext().put(ANIMATION_CONTROLLER_KEY, null);
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/AnimationController$AnimationState.class */
    private static class AnimationState {
        private final TMSchema.State startState;
        private final long duration;
        private long startTime;
        private boolean isForward = true;
        private boolean isForwardAndReverse;
        private float progress;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !AnimationController.class.desiredAssertionStatus();
        }

        AnimationState(TMSchema.State state, long j2, boolean z2) {
            if (!$assertionsDisabled && (state == null || j2 <= 0)) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && !SwingUtilities.isEventDispatchThread()) {
                throw new AssertionError();
            }
            this.startState = state;
            this.duration = j2 * 1000000;
            this.startTime = System.nanoTime();
            this.isForwardAndReverse = z2;
            this.progress = 0.0f;
        }

        private void updateProgress() {
            if (!$assertionsDisabled && !SwingUtilities.isEventDispatchThread()) {
                throw new AssertionError();
            }
            if (isDone()) {
                return;
            }
            long jNanoTime = System.nanoTime();
            this.progress = (jNanoTime - this.startTime) / this.duration;
            this.progress = Math.max(this.progress, 0.0f);
            if (this.progress >= 1.0f) {
                this.progress = 1.0f;
                if (this.isForwardAndReverse) {
                    this.startTime = jNanoTime;
                    this.progress = 0.0f;
                    this.isForward = !this.isForward;
                }
            }
        }

        void paintSkin(XPStyle.Skin skin, Graphics graphics, int i2, int i3, int i4, int i5, TMSchema.State state) {
            float f2;
            if (!$assertionsDisabled && !SwingUtilities.isEventDispatchThread()) {
                throw new AssertionError();
            }
            updateProgress();
            if (!isDone()) {
                Graphics2D graphics2D = (Graphics2D) graphics.create();
                skin.paintSkinRaw(graphics2D, i2, i3, i4, i5, this.startState);
                if (this.isForward) {
                    f2 = this.progress;
                } else {
                    f2 = 1.0f - this.progress;
                }
                graphics2D.setComposite(AlphaComposite.SrcOver.derive(f2));
                skin.paintSkinRaw(graphics2D, i2, i3, i4, i5, state);
                graphics2D.dispose();
                return;
            }
            skin.paintSkinRaw(graphics, i2, i3, i4, i5, state);
        }

        boolean isDone() {
            if ($assertionsDisabled || SwingUtilities.isEventDispatchThread()) {
                return this.progress >= 1.0f;
            }
            throw new AssertionError();
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/AnimationController$PartUIClientPropertyKey.class */
    private static class PartUIClientPropertyKey implements UIClientPropertyKey {
        private static final Map<TMSchema.Part, PartUIClientPropertyKey> map = new EnumMap(TMSchema.Part.class);
        private final TMSchema.Part part;

        static synchronized PartUIClientPropertyKey getKey(TMSchema.Part part) {
            PartUIClientPropertyKey partUIClientPropertyKey = map.get(part);
            if (partUIClientPropertyKey == null) {
                partUIClientPropertyKey = new PartUIClientPropertyKey(part);
                map.put(part, partUIClientPropertyKey);
            }
            return partUIClientPropertyKey;
        }

        private PartUIClientPropertyKey(TMSchema.Part part) {
            this.part = part;
        }

        public String toString() {
            return this.part.toString();
        }
    }
}
