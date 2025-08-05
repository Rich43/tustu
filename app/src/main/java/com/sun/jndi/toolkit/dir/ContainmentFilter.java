package com.sun.jndi.toolkit.dir;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

/* loaded from: rt.jar:com/sun/jndi/toolkit/dir/ContainmentFilter.class */
public class ContainmentFilter implements AttrFilter {
    private Attributes matchingAttrs;

    public ContainmentFilter(Attributes attributes) {
        this.matchingAttrs = attributes;
    }

    @Override // com.sun.jndi.toolkit.dir.AttrFilter
    public boolean check(Attributes attributes) throws NamingException {
        return this.matchingAttrs == null || this.matchingAttrs.size() == 0 || contains(attributes, this.matchingAttrs);
    }

    public static boolean contains(Attributes attributes, Attributes attributes2) throws NamingException {
        if (attributes2 == null) {
            return true;
        }
        NamingEnumeration<? extends Attribute> all = attributes2.getAll();
        while (all.hasMore()) {
            if (attributes == null) {
                return false;
            }
            Attribute next = all.next();
            Attribute attribute = attributes.get(next.getID());
            if (attribute == null) {
                return false;
            }
            if (next.size() > 0) {
                NamingEnumeration<?> all2 = next.getAll();
                while (all2.hasMore()) {
                    if (!attribute.contains(all2.next())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
