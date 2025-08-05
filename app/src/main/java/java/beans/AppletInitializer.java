package java.beans;

import java.applet.Applet;
import java.beans.beancontext.BeanContext;

/* loaded from: rt.jar:java/beans/AppletInitializer.class */
public interface AppletInitializer {
    void initialize(Applet applet, BeanContext beanContext);

    void activate(Applet applet);
}
