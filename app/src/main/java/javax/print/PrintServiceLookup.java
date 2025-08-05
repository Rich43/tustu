package javax.print;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import javax.print.attribute.AttributeSet;
import sun.awt.AppContext;

/* loaded from: rt.jar:javax/print/PrintServiceLookup.class */
public abstract class PrintServiceLookup {
    public abstract PrintService[] getPrintServices(DocFlavor docFlavor, AttributeSet attributeSet);

    public abstract PrintService[] getPrintServices();

    public abstract MultiDocPrintService[] getMultiDocPrintServices(DocFlavor[] docFlavorArr, AttributeSet attributeSet);

    public abstract PrintService getDefaultPrintService();

    /* loaded from: rt.jar:javax/print/PrintServiceLookup$Services.class */
    static class Services {
        private ArrayList listOfLookupServices = null;
        private ArrayList registeredServices = null;

        Services() {
        }
    }

    private static Services getServicesForContext() {
        Services services = (Services) AppContext.getAppContext().get(Services.class);
        if (services == null) {
            services = new Services();
            AppContext.getAppContext().put(Services.class, services);
        }
        return services;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ArrayList getListOfLookupServices() {
        return getServicesForContext().listOfLookupServices;
    }

    private static ArrayList initListOfLookupServices() {
        ArrayList arrayList = new ArrayList();
        getServicesForContext().listOfLookupServices = arrayList;
        return arrayList;
    }

    private static ArrayList getRegisteredServices() {
        return getServicesForContext().registeredServices;
    }

    private static ArrayList initRegisteredServices() {
        ArrayList arrayList = new ArrayList();
        getServicesForContext().registeredServices = arrayList;
        return arrayList;
    }

    public static final PrintService[] lookupPrintServices(DocFlavor docFlavor, AttributeSet attributeSet) {
        ArrayList services = getServices(docFlavor, attributeSet);
        return (PrintService[]) services.toArray(new PrintService[services.size()]);
    }

    public static final MultiDocPrintService[] lookupMultiDocPrintServices(DocFlavor[] docFlavorArr, AttributeSet attributeSet) {
        ArrayList multiDocServices = getMultiDocServices(docFlavorArr, attributeSet);
        return (MultiDocPrintService[]) multiDocServices.toArray(new MultiDocPrintService[multiDocServices.size()]);
    }

    public static final PrintService lookupDefaultPrintService() {
        PrintService defaultPrintService;
        Iterator it = getAllLookupServices().iterator();
        while (it.hasNext()) {
            try {
                defaultPrintService = ((PrintServiceLookup) it.next()).getDefaultPrintService();
            } catch (Exception e2) {
            }
            if (defaultPrintService != null) {
                return defaultPrintService;
            }
        }
        return null;
    }

    public static boolean registerServiceProvider(PrintServiceLookup printServiceLookup) {
        synchronized (PrintServiceLookup.class) {
            Iterator it = getAllLookupServices().iterator();
            while (it.hasNext()) {
                if (it.next().getClass() == printServiceLookup.getClass()) {
                    return false;
                }
            }
            getListOfLookupServices().add(printServiceLookup);
            return true;
        }
    }

    public static boolean registerService(PrintService printService) {
        synchronized (PrintServiceLookup.class) {
            if (printService instanceof StreamPrintService) {
                return false;
            }
            ArrayList registeredServices = getRegisteredServices();
            if (registeredServices == null) {
                registeredServices = initRegisteredServices();
            } else if (registeredServices.contains(printService)) {
                return false;
            }
            registeredServices.add(printService);
            return true;
        }
    }

    private static ArrayList getAllLookupServices() {
        synchronized (PrintServiceLookup.class) {
            ArrayList listOfLookupServices = getListOfLookupServices();
            if (listOfLookupServices != null) {
                return listOfLookupServices;
            }
            ArrayList arrayListInitListOfLookupServices = initListOfLookupServices();
            try {
                AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: javax.print.PrintServiceLookup.1
                    @Override // java.security.PrivilegedExceptionAction
                    public Object run() {
                        Iterator it = ServiceLoader.load(PrintServiceLookup.class).iterator();
                        ArrayList listOfLookupServices2 = PrintServiceLookup.getListOfLookupServices();
                        while (it.hasNext()) {
                            try {
                                listOfLookupServices2.add(it.next());
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
            return arrayListInitListOfLookupServices;
        }
    }

    private static ArrayList getServices(DocFlavor docFlavor, AttributeSet attributeSet) {
        ArrayList arrayList = new ArrayList();
        Iterator it = getAllLookupServices().iterator();
        while (it.hasNext()) {
            try {
                PrintServiceLookup printServiceLookup = (PrintServiceLookup) it.next();
                PrintService[] printServices = null;
                if (docFlavor == null && attributeSet == null) {
                    try {
                        printServices = printServiceLookup.getPrintServices();
                    } catch (Throwable th) {
                    }
                } else {
                    printServices = printServiceLookup.getPrintServices(docFlavor, attributeSet);
                }
                if (printServices != null) {
                    for (PrintService printService : printServices) {
                        arrayList.add(printService);
                    }
                }
            } catch (Exception e2) {
            }
        }
        ArrayList registeredServices = null;
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkPrintJobAccess();
            }
            registeredServices = getRegisteredServices();
        } catch (SecurityException e3) {
        }
        if (registeredServices != null) {
            PrintService[] printServiceArr = (PrintService[]) registeredServices.toArray(new PrintService[registeredServices.size()]);
            for (int i2 = 0; i2 < printServiceArr.length; i2++) {
                if (!arrayList.contains(printServiceArr[i2])) {
                    if (docFlavor == null && attributeSet == null) {
                        arrayList.add(printServiceArr[i2]);
                    } else if (((docFlavor != null && printServiceArr[i2].isDocFlavorSupported(docFlavor)) || docFlavor == null) && null == printServiceArr[i2].getUnsupportedAttributes(docFlavor, attributeSet)) {
                        arrayList.add(printServiceArr[i2]);
                    }
                }
            }
        }
        return arrayList;
    }

    private static ArrayList getMultiDocServices(DocFlavor[] docFlavorArr, AttributeSet attributeSet) {
        ArrayList arrayList = new ArrayList();
        Iterator it = getAllLookupServices().iterator();
        while (it.hasNext()) {
            try {
                MultiDocPrintService[] multiDocPrintServices = ((PrintServiceLookup) it.next()).getMultiDocPrintServices(docFlavorArr, attributeSet);
                if (multiDocPrintServices != null) {
                    for (MultiDocPrintService multiDocPrintService : multiDocPrintServices) {
                        arrayList.add(multiDocPrintService);
                    }
                }
            } catch (Exception e2) {
            }
        }
        ArrayList registeredServices = null;
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkPrintJobAccess();
            }
            registeredServices = getRegisteredServices();
        } catch (Exception e3) {
        }
        if (registeredServices != null) {
            PrintService[] printServiceArr = (PrintService[]) registeredServices.toArray(new PrintService[registeredServices.size()]);
            for (int i2 = 0; i2 < printServiceArr.length; i2++) {
                if ((printServiceArr[i2] instanceof MultiDocPrintService) && !arrayList.contains(printServiceArr[i2])) {
                    if (docFlavorArr == null || docFlavorArr.length == 0) {
                        arrayList.add(printServiceArr[i2]);
                    } else {
                        boolean z2 = true;
                        int i3 = 0;
                        while (true) {
                            if (i3 >= docFlavorArr.length) {
                                break;
                            }
                            if (printServiceArr[i2].isDocFlavorSupported(docFlavorArr[i3])) {
                                if (printServiceArr[i2].getUnsupportedAttributes(docFlavorArr[i3], attributeSet) == null) {
                                    i3++;
                                } else {
                                    z2 = false;
                                    break;
                                }
                            } else {
                                z2 = false;
                                break;
                            }
                        }
                        if (z2) {
                            arrayList.add(printServiceArr[i2]);
                        }
                    }
                }
            }
        }
        return arrayList;
    }
}
