package javax.sound.sampled;

import java.security.BasicPermission;

/* loaded from: rt.jar:javax/sound/sampled/AudioPermission.class */
public class AudioPermission extends BasicPermission {
    public AudioPermission(String str) {
        super(str);
    }

    public AudioPermission(String str, String str2) {
        super(str, str2);
    }
}
