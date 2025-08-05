package com.sun.corba.se.impl.presentation.rmi;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.presentation.rmi.DynamicMethodMarshaller;
import com.sun.corba.se.spi.presentation.rmi.PresentationManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.Remote;
import javax.rmi.CORBA.Tie;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;
import org.omg.CORBA.portable.UnknownException;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import org.omg.PortableServer.Servant;

/* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/ReflectiveTie.class */
public final class ReflectiveTie extends Servant implements Tie {
    private PresentationManager pm;
    private ORBUtilSystemException wrapper;
    private Remote target = null;
    private PresentationManager.ClassData classData = null;

    public ReflectiveTie(PresentationManager presentationManager, ORBUtilSystemException oRBUtilSystemException) {
        this.wrapper = null;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new DynamicAccessPermission("access"));
        }
        this.pm = presentationManager;
        this.wrapper = oRBUtilSystemException;
    }

    @Override // org.omg.PortableServer.Servant
    public String[] _all_interfaces(POA poa, byte[] bArr) {
        return this.classData.getTypeIds();
    }

    @Override // javax.rmi.CORBA.Tie
    public void setTarget(Remote remote) {
        this.target = remote;
        if (remote == null) {
            this.classData = null;
        } else {
            this.classData = this.pm.getClassData(remote.getClass());
        }
    }

    @Override // javax.rmi.CORBA.Tie
    public Remote getTarget() {
        return this.target;
    }

    @Override // javax.rmi.CORBA.Tie
    public Object thisObject() {
        return _this_object();
    }

    @Override // javax.rmi.CORBA.Tie
    public void deactivate() {
        try {
            _poa().deactivate_object(_poa().servant_to_id(this));
        } catch (ObjectNotActive e2) {
        } catch (ServantNotActive e3) {
        } catch (WrongPolicy e4) {
        }
    }

    @Override // javax.rmi.CORBA.Tie
    public ORB orb() {
        return _orb();
    }

    @Override // javax.rmi.CORBA.Tie
    public void orb(ORB orb) {
        try {
            ((org.omg.CORBA_2_3.ORB) orb).set_delegate(this);
        } catch (ClassCastException e2) {
            throw this.wrapper.badOrbForServant(e2);
        }
    }

    @Override // org.omg.CORBA.portable.InvokeHandler
    public OutputStream _invoke(String str, InputStream inputStream, ResponseHandler responseHandler) {
        Method method = null;
        DynamicMethodMarshaller dynamicMethodMarshaller = null;
        try {
            org.omg.CORBA_2_3.portable.InputStream inputStream2 = (org.omg.CORBA_2_3.portable.InputStream) inputStream;
            Method method2 = this.classData.getIDLNameTranslator().getMethod(str);
            if (method2 == null) {
                throw this.wrapper.methodNotFoundInTie(str, this.target.getClass().getName());
            }
            DynamicMethodMarshaller dynamicMethodMarshaller2 = this.pm.getDynamicMethodMarshaller(method2);
            Object objInvoke = method2.invoke(this.target, dynamicMethodMarshaller2.readArguments(inputStream2));
            org.omg.CORBA_2_3.portable.OutputStream outputStream = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createReply();
            dynamicMethodMarshaller2.writeResult(outputStream, objInvoke);
            return outputStream;
        } catch (IllegalAccessException e2) {
            throw this.wrapper.invocationErrorInReflectiveTie(e2, method.getName(), method.getDeclaringClass().getName());
        } catch (IllegalArgumentException e3) {
            throw this.wrapper.invocationErrorInReflectiveTie(e3, method.getName(), method.getDeclaringClass().getName());
        } catch (InvocationTargetException e4) {
            Throwable cause = e4.getCause();
            if (cause instanceof SystemException) {
                throw ((SystemException) cause);
            }
            if ((cause instanceof Exception) && dynamicMethodMarshaller.isDeclaredException(cause)) {
                org.omg.CORBA_2_3.portable.OutputStream outputStream2 = (org.omg.CORBA_2_3.portable.OutputStream) responseHandler.createExceptionReply();
                dynamicMethodMarshaller.writeException(outputStream2, (Exception) cause);
                return outputStream2;
            }
            throw new UnknownException(cause);
        }
    }
}
