package sun.print;

import javax.print.DocFlavor;
import javax.print.FlavorException;
import javax.print.PrintException;

/* loaded from: rt.jar:sun/print/PrintJobFlavorException.class */
class PrintJobFlavorException extends PrintException implements FlavorException {
    private DocFlavor flavor;

    PrintJobFlavorException(String str, DocFlavor docFlavor) {
        super(str);
        this.flavor = docFlavor;
    }

    @Override // javax.print.FlavorException
    public DocFlavor[] getUnsupportedFlavors() {
        return new DocFlavor[]{this.flavor};
    }
}
