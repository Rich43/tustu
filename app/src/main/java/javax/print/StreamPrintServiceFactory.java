package javax.print;

import java.io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import sun.awt.AppContext;

/* loaded from: rt.jar:javax/print/StreamPrintServiceFactory.class */
public abstract class StreamPrintServiceFactory {
    public abstract String getOutputFormat();

    public abstract DocFlavor[] getSupportedDocFlavors();

    public abstract StreamPrintService getPrintService(OutputStream outputStream);

    /* loaded from: rt.jar:javax/print/StreamPrintServiceFactory$Services.class */
    static class Services {
        private ArrayList listOfFactories = null;

        Services() {
        }
    }

    private static Services getServices() {
        Services services = (Services) AppContext.getAppContext().get(Services.class);
        if (services == null) {
            services = new Services();
            AppContext.getAppContext().put(Services.class, services);
        }
        return services;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ArrayList getListOfFactories() {
        return getServices().listOfFactories;
    }

    private static ArrayList initListOfFactories() {
        ArrayList arrayList = new ArrayList();
        getServices().listOfFactories = arrayList;
        return arrayList;
    }

    public static StreamPrintServiceFactory[] lookupStreamPrintServiceFactories(DocFlavor docFlavor, String str) {
        ArrayList factories = getFactories(docFlavor, str);
        return (StreamPrintServiceFactory[]) factories.toArray(new StreamPrintServiceFactory[factories.size()]);
    }

    private static ArrayList getAllFactories() {
        synchronized (StreamPrintServiceFactory.class) {
            ArrayList listOfFactories = getListOfFactories();
            if (listOfFactories != null) {
                return listOfFactories;
            }
            ArrayList arrayListInitListOfFactories = initListOfFactories();
            try {
                AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: javax.print.StreamPrintServiceFactory.1
                    @Override // java.security.PrivilegedExceptionAction
                    public Object run() {
                        Iterator it = ServiceLoader.load(StreamPrintServiceFactory.class).iterator();
                        ArrayList listOfFactories2 = StreamPrintServiceFactory.getListOfFactories();
                        while (it.hasNext()) {
                            try {
                                listOfFactories2.add(it.next());
                            } catch (ServiceConfigurationError e2) {
                                if (System.getSecurityManager() != null) {
                                    e2.printStackTrace();
                                } else {
                                    throw e2;
                                }
                            }
                        }
                        return null;
                    }
                });
            } catch (PrivilegedActionException e2) {
            }
            return arrayListInitListOfFactories;
        }
    }

    private static boolean isMember(DocFlavor docFlavor, DocFlavor[] docFlavorArr) {
        for (DocFlavor docFlavor2 : docFlavorArr) {
            if (docFlavor.equals(docFlavor2)) {
                return true;
            }
        }
        return false;
    }

    private static ArrayList getFactories(DocFlavor docFlavor, String str) {
        if (docFlavor == null && str == null) {
            return getAllFactories();
        }
        ArrayList arrayList = new ArrayList();
        Iterator it = getAllFactories().iterator();
        while (it.hasNext()) {
            StreamPrintServiceFactory streamPrintServiceFactory = (StreamPrintServiceFactory) it.next();
            if (str == null || str.equalsIgnoreCase(streamPrintServiceFactory.getOutputFormat())) {
                if (docFlavor == null || isMember(docFlavor, streamPrintServiceFactory.getSupportedDocFlavors())) {
                    arrayList.add(streamPrintServiceFactory);
                }
            }
        }
        return arrayList;
    }
}
