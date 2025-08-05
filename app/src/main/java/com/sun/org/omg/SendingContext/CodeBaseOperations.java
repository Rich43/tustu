package com.sun.org.omg.SendingContext;

import com.sun.org.omg.CORBA.Repository;
import com.sun.org.omg.CORBA.ValueDefPackage.FullValueDescription;
import org.omg.SendingContext.RunTimeOperations;

/* loaded from: rt.jar:com/sun/org/omg/SendingContext/CodeBaseOperations.class */
public interface CodeBaseOperations extends RunTimeOperations {
    Repository get_ir();

    String implementation(String str);

    String[] implementations(String[] strArr);

    FullValueDescription meta(String str);

    FullValueDescription[] metas(String[] strArr);

    String[] bases(String str);
}
