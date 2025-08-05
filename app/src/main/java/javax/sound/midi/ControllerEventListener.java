package javax.sound.midi;

import java.util.EventListener;

/* loaded from: rt.jar:javax/sound/midi/ControllerEventListener.class */
public interface ControllerEventListener extends EventListener {
    void controlChange(ShortMessage shortMessage);
}
