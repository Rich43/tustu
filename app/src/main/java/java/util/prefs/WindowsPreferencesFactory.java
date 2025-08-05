package java.util.prefs;

/* loaded from: rt.jar:java/util/prefs/WindowsPreferencesFactory.class */
class WindowsPreferencesFactory implements PreferencesFactory {
    WindowsPreferencesFactory() {
    }

    @Override // java.util.prefs.PreferencesFactory
    public Preferences userRoot() {
        return WindowsPreferences.getUserRoot();
    }

    @Override // java.util.prefs.PreferencesFactory
    public Preferences systemRoot() {
        return WindowsPreferences.getSystemRoot();
    }
}
