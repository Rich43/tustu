package sun.applet;

/* loaded from: rt.jar:sun/applet/AppletEventMulticaster.class */
public class AppletEventMulticaster implements AppletListener {

    /* renamed from: a, reason: collision with root package name */
    private final AppletListener f13534a;

    /* renamed from: b, reason: collision with root package name */
    private final AppletListener f13535b;

    public AppletEventMulticaster(AppletListener appletListener, AppletListener appletListener2) {
        this.f13534a = appletListener;
        this.f13535b = appletListener2;
    }

    @Override // sun.applet.AppletListener
    public void appletStateChanged(AppletEvent appletEvent) {
        this.f13534a.appletStateChanged(appletEvent);
        this.f13535b.appletStateChanged(appletEvent);
    }

    public static AppletListener add(AppletListener appletListener, AppletListener appletListener2) {
        return addInternal(appletListener, appletListener2);
    }

    public static AppletListener remove(AppletListener appletListener, AppletListener appletListener2) {
        return removeInternal(appletListener, appletListener2);
    }

    private static AppletListener addInternal(AppletListener appletListener, AppletListener appletListener2) {
        return appletListener == null ? appletListener2 : appletListener2 == null ? appletListener : new AppletEventMulticaster(appletListener, appletListener2);
    }

    protected AppletListener remove(AppletListener appletListener) {
        if (appletListener == this.f13534a) {
            return this.f13535b;
        }
        if (appletListener == this.f13535b) {
            return this.f13534a;
        }
        AppletListener appletListenerRemoveInternal = removeInternal(this.f13534a, appletListener);
        AppletListener appletListenerRemoveInternal2 = removeInternal(this.f13535b, appletListener);
        if (appletListenerRemoveInternal == this.f13534a && appletListenerRemoveInternal2 == this.f13535b) {
            return this;
        }
        return addInternal(appletListenerRemoveInternal, appletListenerRemoveInternal2);
    }

    private static AppletListener removeInternal(AppletListener appletListener, AppletListener appletListener2) {
        if (appletListener == appletListener2 || appletListener == null) {
            return null;
        }
        if (appletListener instanceof AppletEventMulticaster) {
            return ((AppletEventMulticaster) appletListener).remove(appletListener2);
        }
        return appletListener;
    }
}
