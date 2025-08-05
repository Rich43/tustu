package javax.print.attribute.standard;

import java.util.Locale;
import javax.print.attribute.Attribute;
import javax.print.attribute.DocAttribute;
import javax.print.attribute.TextSyntax;

/* loaded from: rt.jar:javax/print/attribute/standard/DocumentName.class */
public final class DocumentName extends TextSyntax implements DocAttribute {
    private static final long serialVersionUID = 7883105848533280430L;

    public DocumentName(String str, Locale locale) {
        super(str, locale);
    }

    @Override // javax.print.attribute.TextSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof DocumentName);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return DocumentName.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "document-name";
    }
}
