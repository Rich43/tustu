package com.sun.xml.internal.bind.v2.runtime.property;

import com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo;
import com.sun.xml.internal.bind.v2.runtime.Name;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/property/TagAndType.class */
class TagAndType {
    final Name tagName;
    final JaxBeanInfo beanInfo;

    TagAndType(Name tagName, JaxBeanInfo beanInfo) {
        this.tagName = tagName;
        this.beanInfo = beanInfo;
    }
}
