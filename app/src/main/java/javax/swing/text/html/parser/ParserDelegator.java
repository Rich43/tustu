package javax.swing.text.html.parser;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.swing.text.html.HTMLEditorKit;
import sun.awt.AppContext;

/* loaded from: rt.jar:javax/swing/text/html/parser/ParserDelegator.class */
public class ParserDelegator extends HTMLEditorKit.Parser implements Serializable {
    private static final Object DTD_KEY = new Object();

    protected static void setDefaultDTD() {
        getDefaultDTD();
    }

    private static synchronized DTD getDefaultDTD() {
        AppContext appContext = AppContext.getAppContext();
        DTD dtdCreateDTD = (DTD) appContext.get(DTD_KEY);
        if (dtdCreateDTD == null) {
            DTD dtd = null;
            try {
                dtd = DTD.getDTD("html32");
            } catch (IOException e2) {
                System.out.println("Throw an exception: could not get default dtd: html32");
            }
            dtdCreateDTD = createDTD(dtd, "html32");
            appContext.put(DTD_KEY, dtdCreateDTD);
        }
        return dtdCreateDTD;
    }

    protected static DTD createDTD(DTD dtd, String str) {
        try {
            InputStream resourceAsStream = getResourceAsStream(str + ".bdtd");
            if (resourceAsStream != null) {
                dtd.read(new DataInputStream(new BufferedInputStream(resourceAsStream)));
                DTD.putDTDHash(str, dtd);
            }
        } catch (Exception e2) {
            System.out.println(e2);
        }
        return dtd;
    }

    public ParserDelegator() {
        setDefaultDTD();
    }

    @Override // javax.swing.text.html.HTMLEditorKit.Parser
    public void parse(Reader reader, HTMLEditorKit.ParserCallback parserCallback, boolean z2) throws IOException {
        new DocumentParser(getDefaultDTD()).parse(reader, parserCallback, z2);
    }

    static InputStream getResourceAsStream(final String str) {
        return (InputStream) AccessController.doPrivileged(new PrivilegedAction<InputStream>() { // from class: javax.swing.text.html.parser.ParserDelegator.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public InputStream run2() {
                return ParserDelegator.class.getResourceAsStream(str);
            }
        });
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        setDefaultDTD();
    }
}
