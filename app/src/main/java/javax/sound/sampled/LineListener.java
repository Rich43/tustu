package javax.sound.sampled;

import java.util.EventListener;

/* loaded from: rt.jar:javax/sound/sampled/LineListener.class */
public interface LineListener extends EventListener {
    void update(LineEvent lineEvent);
}
