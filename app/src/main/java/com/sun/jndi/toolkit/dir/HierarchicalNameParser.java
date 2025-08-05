package com.sun.jndi.toolkit.dir;

import java.util.Properties;
import javafx.fxml.FXMLLoader;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;
import org.icepdf.core.util.PdfOps;

/* compiled from: HierMemDirCtx.java */
/* loaded from: rt.jar:com/sun/jndi/toolkit/dir/HierarchicalNameParser.class */
final class HierarchicalNameParser implements NameParser {
    static final Properties mySyntax = new Properties();

    HierarchicalNameParser() {
    }

    static {
        mySyntax.put("jndi.syntax.direction", "left_to_right");
        mySyntax.put("jndi.syntax.separator", "/");
        mySyntax.put("jndi.syntax.ignorecase", "true");
        mySyntax.put("jndi.syntax.escape", FXMLLoader.ESCAPE_PREFIX);
        mySyntax.put("jndi.syntax.beginquote", PdfOps.DOUBLE_QUOTE__TOKEN);
        mySyntax.put("jndi.syntax.trimblanks", "false");
    }

    @Override // javax.naming.NameParser
    public Name parse(String str) throws NamingException {
        return new HierarchicalName(str, mySyntax);
    }
}
