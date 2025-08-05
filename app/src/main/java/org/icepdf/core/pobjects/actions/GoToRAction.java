package org.icepdf.core.pobjects.actions;

import java.util.HashMap;
import org.icepdf.core.pobjects.Destination;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.StringObject;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PdfOps;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/actions/GoToRAction.class */
public class GoToRAction extends Action {
    public static final Name F_KEY = new Name(PdfOps.F_TOKEN);
    public static final Name NEW_WINDOW_KEY = new Name("NewWindow");
    private String externalFile;
    private FileSpecification fileSpecification;
    private Destination externalDestination;
    private Boolean isNewWindow;

    public GoToRAction(Library l2, HashMap h2) {
        super(l2, h2);
        this.externalDestination = new Destination(this.library, this.library.getObject(this.entries, Destination.D_KEY));
        Object tmp = this.library.getObject(this.entries, F_KEY);
        if (tmp instanceof HashMap) {
            this.fileSpecification = new FileSpecification(this.library, (HashMap) tmp);
        } else if (tmp instanceof StringObject) {
            this.externalFile = ((StringObject) tmp).getDecryptedLiteralString(this.library.getSecurityManager());
        }
        this.isNewWindow = this.library.getBoolean(this.entries, NEW_WINDOW_KEY);
    }

    public Destination getDestination() {
        return this.externalDestination;
    }

    public String getFile() {
        return this.externalFile;
    }

    public FileSpecification getFileSpecification() {
        return this.fileSpecification;
    }

    public Boolean isNewWindow() {
        return this.isNewWindow;
    }
}
