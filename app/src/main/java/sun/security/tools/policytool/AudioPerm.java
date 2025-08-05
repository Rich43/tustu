package sun.security.tools.policytool;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/AudioPerm.class */
class AudioPerm extends Perm {
    public AudioPerm() {
        super("AudioPermission", "javax.sound.sampled.AudioPermission", new String[]{"play", "record"}, null);
    }
}
