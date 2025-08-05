package com.sun.corba.se.impl.presentation.rmi;

import com.sun.corba.se.pept.transport.ContactInfoList;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orbutil.proxy.LinkedInvocationHandler;
import com.sun.corba.se.spi.presentation.rmi.DynamicMethodMarshaller;
import com.sun.corba.se.spi.presentation.rmi.PresentationManager;
import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
import com.sun.corba.se.spi.protocol.CorbaClientDelegate;
import com.sun.corba.se.spi.transport.CorbaContactInfoList;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.rmi.CORBA.Util;
import org.omg.CORBA.Object;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.Delegate;
import org.omg.CORBA.portable.RemarshalException;
import org.omg.CORBA.portable.ServantObject;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/StubInvocationHandlerImpl.class */
public final class StubInvocationHandlerImpl implements LinkedInvocationHandler {
    private transient PresentationManager.ClassData classData;
    private transient PresentationManager pm;
    private transient Object stub;
    private transient Proxy self;

    @Override // com.sun.corba.se.spi.orbutil.proxy.LinkedInvocationHandler
    public void setProxy(Proxy proxy) {
        this.self = proxy;
    }

    @Override // com.sun.corba.se.spi.orbutil.proxy.LinkedInvocationHandler
    public Proxy getProxy() {
        return this.self;
    }

    public StubInvocationHandlerImpl(PresentationManager presentationManager, PresentationManager.ClassData classData, Object object) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new DynamicAccessPermission("access"));
        }
        this.classData = classData;
        this.pm = presentationManager;
        this.stub = object;
    }

    private boolean isLocal() {
        boolean zUseLocalInvocation = false;
        Delegate delegate = StubAdapter.getDelegate(this.stub);
        if (delegate instanceof CorbaClientDelegate) {
            ContactInfoList contactInfoList = ((CorbaClientDelegate) delegate).getContactInfoList();
            if (contactInfoList instanceof CorbaContactInfoList) {
                zUseLocalInvocation = ((CorbaContactInfoList) contactInfoList).getLocalClientRequestDispatcher().useLocalInvocation(null);
            }
        }
        return zUseLocalInvocation;
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object obj, final Method method, Object[] objArr) throws Throwable {
        String iDLName = this.classData.getIDLNameTranslator().getIDLName(method);
        DynamicMethodMarshaller dynamicMethodMarshaller = this.pm.getDynamicMethodMarshaller(method);
        try {
            Delegate delegate = StubAdapter.getDelegate(this.stub);
            if (!isLocal()) {
                InputStream inputStream = null;
                try {
                    try {
                        try {
                            OutputStream outputStream = (OutputStream) delegate.request(this.stub, iDLName, true);
                            dynamicMethodMarshaller.writeArguments(outputStream, objArr);
                            inputStream = (InputStream) delegate.invoke(this.stub, outputStream);
                            Object result = dynamicMethodMarshaller.readResult(inputStream);
                            delegate.releaseReply(this.stub, inputStream);
                            return result;
                        } catch (ApplicationException e2) {
                            throw dynamicMethodMarshaller.readException(e2);
                        } catch (RemarshalException e3) {
                            Object objInvoke = invoke(obj, method, objArr);
                            delegate.releaseReply(this.stub, inputStream);
                            return objInvoke;
                        }
                    } catch (Throwable th) {
                        delegate.releaseReply(this.stub, null);
                        throw th;
                    }
                } catch (SystemException e4) {
                    throw Util.mapSystemException(e4);
                }
            }
            ORB orb = (ORB) delegate.orb(this.stub);
            ServantObject servantObjectServant_preinvoke = delegate.servant_preinvoke(this.stub, iDLName, method.getDeclaringClass());
            try {
                if (servantObjectServant_preinvoke == null) {
                    return invoke(this.stub, method, objArr);
                }
                try {
                    Object[] objArrCopyArguments = dynamicMethodMarshaller.copyArguments(objArr, orb);
                    if (!method.isAccessible()) {
                        AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.corba.se.impl.presentation.rmi.StubInvocationHandlerImpl.1
                            @Override // java.security.PrivilegedAction
                            /* renamed from: run */
                            public Object run2() {
                                method.setAccessible(true);
                                return null;
                            }
                        });
                    }
                    Object objCopyResult = dynamicMethodMarshaller.copyResult(method.invoke(servantObjectServant_preinvoke.servant, objArrCopyArguments), orb);
                    delegate.servant_postinvoke(this.stub, servantObjectServant_preinvoke);
                    return objCopyResult;
                } catch (InvocationTargetException e5) {
                    Throwable th2 = (Throwable) Util.copyObject(e5.getCause(), orb);
                    if (dynamicMethodMarshaller.isDeclaredException(th2)) {
                        throw th2;
                    }
                    throw Util.wrapException(th2);
                } catch (Throwable th3) {
                    if (th3 instanceof ThreadDeath) {
                        throw ((ThreadDeath) th3);
                    }
                    throw Util.wrapException(th3);
                }
            } catch (Throwable th4) {
                delegate.servant_postinvoke(this.stub, servantObjectServant_preinvoke);
                throw th4;
            }
        } catch (SystemException e6) {
            throw Util.mapSystemException(e6);
        }
    }
}
