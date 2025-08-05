package java.util.prefs;

import java.util.EventListener;

@FunctionalInterface
/* loaded from: rt.jar:java/util/prefs/PreferenceChangeListener.class */
public interface PreferenceChangeListener extends EventListener {
    void preferenceChange(PreferenceChangeEvent preferenceChangeEvent);
}
