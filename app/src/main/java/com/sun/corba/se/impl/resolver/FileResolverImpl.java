package com.sun.corba.se.impl.resolver;

import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.resolver.Resolver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import org.omg.CORBA.Object;

/* loaded from: rt.jar:com/sun/corba/se/impl/resolver/FileResolverImpl.class */
public class FileResolverImpl implements Resolver {
    private ORB orb;
    private File file;
    private long fileModified = 0;
    private Properties savedProps = new Properties();

    public FileResolverImpl(ORB orb, File file) {
        this.orb = orb;
        this.file = file;
    }

    @Override // com.sun.corba.se.spi.resolver.Resolver
    public Object resolve(String str) {
        check();
        String property = this.savedProps.getProperty(str);
        if (property == null) {
            return null;
        }
        return this.orb.string_to_object(property);
    }

    @Override // com.sun.corba.se.spi.resolver.Resolver
    public Set list() {
        check();
        HashSet hashSet = new HashSet();
        Enumeration<?> enumerationPropertyNames = this.savedProps.propertyNames();
        while (enumerationPropertyNames.hasMoreElements()) {
            hashSet.add(enumerationPropertyNames.nextElement2());
        }
        return hashSet;
    }

    private void check() {
        if (this.file == null) {
            return;
        }
        long jLastModified = this.file.lastModified();
        if (jLastModified > this.fileModified) {
            try {
                FileInputStream fileInputStream = new FileInputStream(this.file);
                this.savedProps.clear();
                this.savedProps.load(fileInputStream);
                fileInputStream.close();
                this.fileModified = jLastModified;
            } catch (FileNotFoundException e2) {
                System.err.println(CorbaResourceUtil.getText("bootstrap.filenotfound", this.file.getAbsolutePath()));
            } catch (IOException e3) {
                System.err.println(CorbaResourceUtil.getText("bootstrap.exception", this.file.getAbsolutePath(), e3.toString()));
            }
        }
    }
}
