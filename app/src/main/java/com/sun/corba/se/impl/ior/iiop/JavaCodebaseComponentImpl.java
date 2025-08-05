package com.sun.corba.se.impl.ior.iiop;

import com.sun.corba.se.spi.ior.TaggedComponentBase;
import com.sun.corba.se.spi.ior.iiop.JavaCodebaseComponent;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/iiop/JavaCodebaseComponentImpl.class */
public class JavaCodebaseComponentImpl extends TaggedComponentBase implements JavaCodebaseComponent {
    private String URLs;

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof JavaCodebaseComponentImpl)) {
            return false;
        }
        return this.URLs.equals(((JavaCodebaseComponentImpl) obj).getURLs());
    }

    public int hashCode() {
        return this.URLs.hashCode();
    }

    public String toString() {
        return "JavaCodebaseComponentImpl[URLs=" + this.URLs + "]";
    }

    @Override // com.sun.corba.se.spi.ior.iiop.JavaCodebaseComponent
    public String getURLs() {
        return this.URLs;
    }

    public JavaCodebaseComponentImpl(String str) {
        this.URLs = str;
    }

    @Override // com.sun.corba.se.spi.ior.WriteContents
    public void writeContents(OutputStream outputStream) {
        outputStream.write_string(this.URLs);
    }

    @Override // com.sun.corba.se.spi.ior.Identifiable
    public int getId() {
        return 25;
    }
}
