package com.sun.media.sound;

/* loaded from: rt.jar:com/sun/media/sound/AudioSynthesizerPropertyInfo.class */
public final class AudioSynthesizerPropertyInfo {
    public String name;
    public Object value;
    public Class valueClass;
    public String description = null;
    public Object[] choices = null;

    public AudioSynthesizerPropertyInfo(String str, Object obj) {
        this.value = null;
        this.valueClass = null;
        this.name = str;
        if (obj instanceof Class) {
            this.valueClass = (Class) obj;
            return;
        }
        this.value = obj;
        if (obj != null) {
            this.valueClass = obj.getClass();
        }
    }
}
