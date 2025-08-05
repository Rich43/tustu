package com.sun.corba.se.impl.interceptors;

import com.sun.corba.se.impl.logging.InterceptorsSystemException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import org.omg.PortableInterceptor.ClientRequestInterceptor;
import org.omg.PortableInterceptor.IORInterceptor;
import org.omg.PortableInterceptor.Interceptor;
import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;
import org.omg.PortableInterceptor.ServerRequestInterceptor;

/* loaded from: rt.jar:com/sun/corba/se/impl/interceptors/InterceptorList.class */
public class InterceptorList {
    static final int INTERCEPTOR_TYPE_CLIENT = 0;
    static final int INTERCEPTOR_TYPE_SERVER = 1;
    static final int INTERCEPTOR_TYPE_IOR = 2;
    static final int NUM_INTERCEPTOR_TYPES = 3;
    static final Class[] classTypes = {ClientRequestInterceptor.class, ServerRequestInterceptor.class, IORInterceptor.class};
    private InterceptorsSystemException wrapper;
    private boolean locked = false;
    private Interceptor[][] interceptors = new Interceptor[3];

    /* JADX WARN: Type inference failed for: r1v2, types: [org.omg.PortableInterceptor.Interceptor[], org.omg.PortableInterceptor.Interceptor[][]] */
    InterceptorList(InterceptorsSystemException interceptorsSystemException) {
        this.wrapper = interceptorsSystemException;
        initInterceptorArrays();
    }

    void register_interceptor(Interceptor interceptor, int i2) throws DuplicateName {
        if (this.locked) {
            throw this.wrapper.interceptorListLocked();
        }
        String strName = interceptor.name();
        boolean zEquals = strName.equals("");
        boolean z2 = false;
        Interceptor[] interceptorArr = this.interceptors[i2];
        if (!zEquals) {
            int length = interceptorArr.length;
            int i3 = 0;
            while (true) {
                if (i3 >= length) {
                    break;
                }
                if (!interceptorArr[i3].name().equals(strName)) {
                    i3++;
                } else {
                    z2 = true;
                    break;
                }
            }
        }
        if (!z2) {
            growInterceptorArray(i2);
            this.interceptors[i2][this.interceptors[i2].length - 1] = interceptor;
            return;
        }
        throw new DuplicateName(strName);
    }

    void lock() {
        this.locked = true;
    }

    Interceptor[] getInterceptors(int i2) {
        return this.interceptors[i2];
    }

    boolean hasInterceptorsOfType(int i2) {
        return this.interceptors[i2].length > 0;
    }

    private void initInterceptorArrays() {
        for (int i2 = 0; i2 < 3; i2++) {
            this.interceptors[i2] = (Interceptor[]) Array.newInstance((Class<?>) classTypes[i2], 0);
        }
    }

    private void growInterceptorArray(int i2) {
        Class cls = classTypes[i2];
        int length = this.interceptors[i2].length;
        Interceptor[] interceptorArr = (Interceptor[]) Array.newInstance((Class<?>) cls, length + 1);
        System.arraycopy(this.interceptors[i2], 0, interceptorArr, 0, length);
        this.interceptors[i2] = interceptorArr;
    }

    void destroyAll() {
        int length = this.interceptors.length;
        for (int i2 = 0; i2 < length; i2++) {
            int length2 = this.interceptors[i2].length;
            for (int i3 = 0; i3 < length2; i3++) {
                this.interceptors[i2][i3].destroy();
            }
        }
    }

    void sortInterceptors() {
        ArrayList arrayList = null;
        ArrayList arrayList2 = null;
        int length = this.interceptors.length;
        for (int i2 = 0; i2 < length; i2++) {
            int length2 = this.interceptors[i2].length;
            if (length2 > 0) {
                arrayList = new ArrayList();
                arrayList2 = new ArrayList();
            }
            for (int i3 = 0; i3 < length2; i3++) {
                Interceptor interceptor = this.interceptors[i2][i3];
                if (interceptor instanceof Comparable) {
                    arrayList.add(interceptor);
                } else {
                    arrayList2.add(interceptor);
                }
            }
            if (length2 > 0 && arrayList.size() > 0) {
                Collections.sort(arrayList);
                Iterator it = arrayList.iterator();
                Iterator it2 = arrayList2.iterator();
                for (int i4 = 0; i4 < length2; i4++) {
                    if (it.hasNext()) {
                        this.interceptors[i2][i4] = (Interceptor) it.next();
                    } else if (it2.hasNext()) {
                        this.interceptors[i2][i4] = (Interceptor) it2.next();
                    } else {
                        throw this.wrapper.sortSizeMismatch();
                    }
                }
            }
        }
    }
}
