package sun.awt.windows;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.awt.datatransfer.DataFlavor;

/* compiled from: WDataTransferer.java */
/* loaded from: rt.jar:sun/awt/windows/EHTMLReadMode.class */
enum EHTMLReadMode {
    HTML_READ_ALL,
    HTML_READ_FRAGMENT,
    HTML_READ_SELECTION;

    public static EHTMLReadMode getEHTMLReadMode(DataFlavor dataFlavor) {
        EHTMLReadMode eHTMLReadMode = HTML_READ_SELECTION;
        String parameter = dataFlavor.getParameter(Constants.DOCUMENT_PNAME);
        if ("all".equals(parameter)) {
            eHTMLReadMode = HTML_READ_ALL;
        } else if ("fragment".equals(parameter)) {
            eHTMLReadMode = HTML_READ_FRAGMENT;
        }
        return eHTMLReadMode;
    }
}
