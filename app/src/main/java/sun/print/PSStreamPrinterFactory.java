package sun.print;

import java.io.OutputStream;
import javax.print.DocFlavor;
import javax.print.StreamPrintService;
import javax.print.StreamPrintServiceFactory;

/* loaded from: rt.jar:sun/print/PSStreamPrinterFactory.class */
public class PSStreamPrinterFactory extends StreamPrintServiceFactory {
    static final String psMimeType = "application/postscript";
    static final DocFlavor[] supportedDocFlavors = {DocFlavor.SERVICE_FORMATTED.PAGEABLE, DocFlavor.SERVICE_FORMATTED.PRINTABLE, DocFlavor.BYTE_ARRAY.GIF, DocFlavor.INPUT_STREAM.GIF, DocFlavor.URL.GIF, DocFlavor.BYTE_ARRAY.JPEG, DocFlavor.INPUT_STREAM.JPEG, DocFlavor.URL.JPEG, DocFlavor.BYTE_ARRAY.PNG, DocFlavor.INPUT_STREAM.PNG, DocFlavor.URL.PNG};

    @Override // javax.print.StreamPrintServiceFactory
    public String getOutputFormat() {
        return psMimeType;
    }

    @Override // javax.print.StreamPrintServiceFactory
    public DocFlavor[] getSupportedDocFlavors() {
        return getFlavors();
    }

    static DocFlavor[] getFlavors() {
        DocFlavor[] docFlavorArr = new DocFlavor[supportedDocFlavors.length];
        System.arraycopy(supportedDocFlavors, 0, docFlavorArr, 0, docFlavorArr.length);
        return docFlavorArr;
    }

    @Override // javax.print.StreamPrintServiceFactory
    public StreamPrintService getPrintService(OutputStream outputStream) {
        return new PSStreamPrintService(outputStream);
    }
}
