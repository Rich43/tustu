package com.sun.jndi.toolkit.dir;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

/* loaded from: rt.jar:com/sun/jndi/toolkit/dir/AttrFilter.class */
public interface AttrFilter {
    boolean check(Attributes attributes) throws NamingException;
}
