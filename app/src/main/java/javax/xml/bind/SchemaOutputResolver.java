package javax.xml.bind;

import java.io.IOException;
import javax.xml.transform.Result;

/* loaded from: rt.jar:javax/xml/bind/SchemaOutputResolver.class */
public abstract class SchemaOutputResolver {
    public abstract Result createOutput(String str, String str2) throws IOException;
}
