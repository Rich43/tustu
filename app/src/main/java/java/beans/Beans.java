package java.beans;

import com.sun.beans.finder.ClassFinder;
import java.applet.Applet;
import java.beans.beancontext.BeanContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.lang.reflect.Modifier;
import java.net.URL;

/* loaded from: rt.jar:java/beans/Beans.class */
public class Beans {
    public static Object instantiate(ClassLoader classLoader, String str) throws IOException, ClassNotFoundException {
        return instantiate(classLoader, str, null, null);
    }

    public static Object instantiate(ClassLoader classLoader, String str, BeanContext beanContext) throws IOException, ClassNotFoundException {
        return instantiate(classLoader, str, beanContext, null);
    }

    public static Object instantiate(ClassLoader classLoader, String str, BeanContext beanContext, AppletInitializer appletInitializer) throws IOException, ClassNotFoundException {
        InputStream resourceAsStream;
        String strConcat;
        URL resource;
        ObjectInput objectInputStreamWithLoader;
        Object objNewInstance = null;
        boolean z2 = false;
        IOException iOException = null;
        if (classLoader == null) {
            try {
                classLoader = ClassLoader.getSystemClassLoader();
            } catch (SecurityException e2) {
            }
        }
        String strConcat2 = str.replace('.', '/').concat(".ser");
        if (classLoader == null) {
            resourceAsStream = ClassLoader.getSystemResourceAsStream(strConcat2);
        } else {
            resourceAsStream = classLoader.getResourceAsStream(strConcat2);
        }
        if (resourceAsStream != null) {
            try {
                if (classLoader == null) {
                    objectInputStreamWithLoader = new ObjectInputStream(resourceAsStream);
                } else {
                    objectInputStreamWithLoader = new ObjectInputStreamWithLoader(resourceAsStream, classLoader);
                }
                objNewInstance = objectInputStreamWithLoader.readObject();
                z2 = true;
                objectInputStreamWithLoader.close();
            } catch (IOException e3) {
                resourceAsStream.close();
                iOException = e3;
            } catch (ClassNotFoundException e4) {
                resourceAsStream.close();
                throw e4;
            }
        }
        if (objNewInstance == null) {
            try {
                Class<?> clsFindClass = ClassFinder.findClass(str, classLoader);
                if (!Modifier.isPublic(clsFindClass.getModifiers())) {
                    throw new ClassNotFoundException("" + ((Object) clsFindClass) + " : no public access");
                }
                try {
                    objNewInstance = clsFindClass.newInstance();
                } catch (Exception e5) {
                    throw new ClassNotFoundException("" + ((Object) clsFindClass) + " : " + ((Object) e5), e5);
                }
            } catch (ClassNotFoundException e6) {
                if (iOException != null) {
                    throw iOException;
                }
                throw e6;
            }
        }
        if (objNewInstance != null) {
            BeansAppletStub beansAppletStub = null;
            if (objNewInstance instanceof Applet) {
                Applet applet = (Applet) objNewInstance;
                boolean z3 = appletInitializer == null;
                if (z3) {
                    if (z2) {
                        strConcat = str.replace('.', '/').concat(".ser");
                    } else {
                        strConcat = str.replace('.', '/').concat(".class");
                    }
                    URL url = null;
                    URL url2 = null;
                    if (classLoader == null) {
                        resource = ClassLoader.getSystemResource(strConcat);
                    } else {
                        resource = classLoader.getResource(strConcat);
                    }
                    if (resource != null) {
                        String externalForm = resource.toExternalForm();
                        if (externalForm.endsWith(strConcat)) {
                            url = new URL(externalForm.substring(0, externalForm.length() - strConcat.length()));
                            url2 = url;
                            int iLastIndexOf = externalForm.lastIndexOf(47);
                            if (iLastIndexOf >= 0) {
                                url2 = new URL(externalForm.substring(0, iLastIndexOf + 1));
                            }
                        }
                    }
                    beansAppletStub = new BeansAppletStub(applet, new BeansAppletContext(applet), url, url2);
                    applet.setStub(beansAppletStub);
                } else {
                    appletInitializer.initialize(applet, beanContext);
                }
                if (beanContext != null) {
                    unsafeBeanContextAdd(beanContext, objNewInstance);
                }
                if (!z2) {
                    applet.setSize(100, 100);
                    applet.init();
                }
                if (z3) {
                    beansAppletStub.active = true;
                } else {
                    appletInitializer.activate(applet);
                }
            } else if (beanContext != null) {
                unsafeBeanContextAdd(beanContext, objNewInstance);
            }
        }
        return objNewInstance;
    }

    private static void unsafeBeanContextAdd(BeanContext beanContext, Object obj) {
        beanContext.add(obj);
    }

    public static Object getInstanceOf(Object obj, Class<?> cls) {
        return obj;
    }

    public static boolean isInstanceOf(Object obj, Class<?> cls) {
        return Introspector.isSubclass(obj.getClass(), cls);
    }

    public static boolean isDesignTime() {
        return ThreadGroupContext.getContext().isDesignTime();
    }

    public static boolean isGuiAvailable() {
        return ThreadGroupContext.getContext().isGuiAvailable();
    }

    public static void setDesignTime(boolean z2) throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPropertiesAccess();
        }
        ThreadGroupContext.getContext().setDesignTime(z2);
    }

    public static void setGuiAvailable(boolean z2) throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPropertiesAccess();
        }
        ThreadGroupContext.getContext().setGuiAvailable(z2);
    }
}
