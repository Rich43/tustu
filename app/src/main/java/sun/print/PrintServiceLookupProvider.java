package sun.print;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import javax.print.DocFlavor;
import javax.print.MultiDocPrintService;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttribute;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;

/* loaded from: rt.jar:sun/print/PrintServiceLookupProvider.class */
public class PrintServiceLookupProvider extends PrintServiceLookup {
    private String defaultPrinter;
    private PrintService defaultPrintService;
    private String[] printers;
    private PrintService[] printServices;
    private static PrintServiceLookupProvider win32PrintLUS;

    private native String getDefaultPrinterName();

    private native String[] getAllPrinterNames();

    /* JADX INFO: Access modifiers changed from: private */
    public native void notifyLocalPrinterChange();

    /* JADX INFO: Access modifiers changed from: private */
    public native void notifyRemotePrinterChange();

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.print.PrintServiceLookupProvider.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                System.loadLibrary("awt");
                return null;
            }
        });
    }

    public static PrintServiceLookupProvider getWin32PrintLUS() {
        if (win32PrintLUS == null) {
            PrintServiceLookup.lookupDefaultPrintService();
        }
        return win32PrintLUS;
    }

    public PrintServiceLookupProvider() {
        if (win32PrintLUS == null) {
            win32PrintLUS = this;
            PrinterChangeListener printerChangeListener = new PrinterChangeListener();
            printerChangeListener.setDaemon(true);
            printerChangeListener.start();
            RemotePrinterChangeListener remotePrinterChangeListener = new RemotePrinterChangeListener();
            remotePrinterChangeListener.setDaemon(true);
            remotePrinterChangeListener.start();
        }
    }

    @Override // javax.print.PrintServiceLookup
    public synchronized PrintService[] getPrintServices() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPrintJobAccess();
        }
        if (this.printServices == null) {
            refreshServices();
        }
        return this.printServices;
    }

    private synchronized void refreshServices() {
        this.printers = getAllPrinterNames();
        if (this.printers == null) {
            this.printServices = new PrintService[0];
            return;
        }
        PrintService[] printServiceArr = new PrintService[this.printers.length];
        PrintService defaultPrintService = getDefaultPrintService();
        for (int i2 = 0; i2 < this.printers.length; i2++) {
            if (defaultPrintService != null && this.printers[i2].equals(defaultPrintService.getName())) {
                printServiceArr[i2] = defaultPrintService;
            } else if (this.printServices == null) {
                printServiceArr[i2] = new Win32PrintService(this.printers[i2]);
            } else {
                int i3 = 0;
                while (true) {
                    if (i3 >= this.printServices.length) {
                        break;
                    }
                    if (this.printServices[i3] == null || !this.printers[i2].equals(this.printServices[i3].getName())) {
                        i3++;
                    } else {
                        printServiceArr[i2] = this.printServices[i3];
                        this.printServices[i3] = null;
                        break;
                    }
                }
                if (i3 == this.printServices.length) {
                    printServiceArr[i2] = new Win32PrintService(this.printers[i2]);
                }
            }
        }
        if (this.printServices != null) {
            for (int i4 = 0; i4 < this.printServices.length; i4++) {
                if ((this.printServices[i4] instanceof Win32PrintService) && !this.printServices[i4].equals(this.defaultPrintService)) {
                    ((Win32PrintService) this.printServices[i4]).invalidateService();
                }
            }
        }
        this.printServices = printServiceArr;
    }

    public synchronized PrintService getPrintServiceByName(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        PrintService[] printServices = getPrintServices();
        for (int i2 = 0; i2 < printServices.length; i2++) {
            if (printServices[i2].getName().equals(str)) {
                return printServices[i2];
            }
        }
        return null;
    }

    boolean matchingService(PrintService printService, PrintServiceAttributeSet printServiceAttributeSet) {
        if (printServiceAttributeSet != null) {
            Attribute[] array = printServiceAttributeSet.toArray();
            for (int i2 = 0; i2 < array.length; i2++) {
                PrintServiceAttribute attribute = printService.getAttribute(array[i2].getCategory());
                if (attribute == null || !attribute.equals(array[i2])) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    @Override // javax.print.PrintServiceLookup
    public PrintService[] getPrintServices(DocFlavor docFlavor, AttributeSet attributeSet) {
        PrintService[] printServices;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPrintJobAccess();
        }
        HashPrintRequestAttributeSet hashPrintRequestAttributeSet = null;
        HashPrintServiceAttributeSet hashPrintServiceAttributeSet = null;
        if (attributeSet != null && !attributeSet.isEmpty()) {
            hashPrintRequestAttributeSet = new HashPrintRequestAttributeSet();
            hashPrintServiceAttributeSet = new HashPrintServiceAttributeSet();
            Attribute[] array = attributeSet.toArray();
            for (int i2 = 0; i2 < array.length; i2++) {
                if (array[i2] instanceof PrintRequestAttribute) {
                    hashPrintRequestAttributeSet.add(array[i2]);
                } else if (array[i2] instanceof PrintServiceAttribute) {
                    hashPrintServiceAttributeSet.add(array[i2]);
                }
            }
        }
        if (hashPrintServiceAttributeSet != null && hashPrintServiceAttributeSet.get(PrinterName.class) != null) {
            PrintService printServiceByName = getPrintServiceByName(((PrinterName) hashPrintServiceAttributeSet.get(PrinterName.class)).getValue());
            if (printServiceByName == null || !matchingService(printServiceByName, hashPrintServiceAttributeSet)) {
                printServices = new PrintService[0];
            } else {
                printServices = new PrintService[]{printServiceByName};
            }
        } else {
            printServices = getPrintServices();
        }
        if (printServices.length == 0) {
            return printServices;
        }
        ArrayList arrayList = new ArrayList();
        for (int i3 = 0; i3 < printServices.length; i3++) {
            try {
                if (printServices[i3].getUnsupportedAttributes(docFlavor, hashPrintRequestAttributeSet) == null) {
                    arrayList.add(printServices[i3]);
                }
            } catch (IllegalArgumentException e2) {
            }
        }
        return (PrintService[]) arrayList.toArray(new PrintService[arrayList.size()]);
    }

    @Override // javax.print.PrintServiceLookup
    public MultiDocPrintService[] getMultiDocPrintServices(DocFlavor[] docFlavorArr, AttributeSet attributeSet) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPrintJobAccess();
        }
        return new MultiDocPrintService[0];
    }

    @Override // javax.print.PrintServiceLookup
    public synchronized PrintService getDefaultPrintService() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPrintJobAccess();
        }
        this.defaultPrinter = getDefaultPrinterName();
        if (this.defaultPrinter == null) {
            return null;
        }
        if (this.defaultPrintService != null && this.defaultPrintService.getName().equals(this.defaultPrinter)) {
            return this.defaultPrintService;
        }
        this.defaultPrintService = null;
        if (this.printServices != null) {
            int i2 = 0;
            while (true) {
                if (i2 >= this.printServices.length) {
                    break;
                }
                if (!this.defaultPrinter.equals(this.printServices[i2].getName())) {
                    i2++;
                } else {
                    this.defaultPrintService = this.printServices[i2];
                    break;
                }
            }
        }
        if (this.defaultPrintService == null) {
            this.defaultPrintService = new Win32PrintService(this.defaultPrinter);
        }
        return this.defaultPrintService;
    }

    /* loaded from: rt.jar:sun/print/PrintServiceLookupProvider$PrinterChangeListener.class */
    private final class PrinterChangeListener extends Thread {
        private PrinterChangeListener() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            PrintServiceLookupProvider.this.notifyLocalPrinterChange();
        }
    }

    /* loaded from: rt.jar:sun/print/PrintServiceLookupProvider$RemotePrinterChangeListener.class */
    private final class RemotePrinterChangeListener extends Thread {
        private RemotePrinterChangeListener() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            PrintServiceLookupProvider.this.notifyRemotePrinterChange();
        }
    }
}
