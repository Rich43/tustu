package javax.print.attribute.standard;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import javax.print.attribute.Attribute;
import javax.print.attribute.EnumSyntax;

/* loaded from: rt.jar:javax/print/attribute/standard/ReferenceUriSchemesSupported.class */
public class ReferenceUriSchemesSupported extends EnumSyntax implements Attribute {
    private static final long serialVersionUID = -8989076942813442805L;
    public static final ReferenceUriSchemesSupported FTP = new ReferenceUriSchemesSupported(0);
    public static final ReferenceUriSchemesSupported HTTP = new ReferenceUriSchemesSupported(1);
    public static final ReferenceUriSchemesSupported HTTPS = new ReferenceUriSchemesSupported(2);
    public static final ReferenceUriSchemesSupported GOPHER = new ReferenceUriSchemesSupported(3);
    public static final ReferenceUriSchemesSupported NEWS = new ReferenceUriSchemesSupported(4);
    public static final ReferenceUriSchemesSupported NNTP = new ReferenceUriSchemesSupported(5);
    public static final ReferenceUriSchemesSupported WAIS = new ReferenceUriSchemesSupported(6);
    public static final ReferenceUriSchemesSupported FILE = new ReferenceUriSchemesSupported(7);
    private static final String[] myStringTable = {"ftp", "http", "https", "gopher", "news", "nntp", "wais", DeploymentDescriptorParser.ATTR_FILE};
    private static final ReferenceUriSchemesSupported[] myEnumValueTable = {FTP, HTTP, HTTPS, GOPHER, NEWS, NNTP, WAIS, FILE};

    protected ReferenceUriSchemesSupported(int i2) {
        super(i2);
    }

    @Override // javax.print.attribute.EnumSyntax
    protected String[] getStringTable() {
        return (String[]) myStringTable.clone();
    }

    @Override // javax.print.attribute.EnumSyntax
    protected EnumSyntax[] getEnumValueTable() {
        return (EnumSyntax[]) myEnumValueTable.clone();
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return ReferenceUriSchemesSupported.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "reference-uri-schemes-supported";
    }
}
