package com.sun.deploy.uitoolkit.impl.fx;

import com.sun.javafx.application.HostServicesDelegate;
import javafx.application.Application;

/* loaded from: jfxrt.jar:com/sun/deploy/uitoolkit/impl/fx/HostServicesFactory.class */
public class HostServicesFactory {
    public static HostServicesDelegate getInstance(Application app) {
        return HostServicesDelegate.getInstance(app);
    }
}
