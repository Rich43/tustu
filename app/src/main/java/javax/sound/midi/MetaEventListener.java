package javax.sound.midi;

import java.util.EventListener;

/* loaded from: rt.jar:javax/sound/midi/MetaEventListener.class */
public interface MetaEventListener extends EventListener {
    void meta(MetaMessage metaMessage);
}
