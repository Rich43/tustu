package com.sun.jndi.cosnaming;

import com.sun.jndi.toolkit.corba.CorbaUtils;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import javax.naming.CannotProceedException;
import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.ContextNotEmptyException;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.NamingManager;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextPackage.AlreadyBound;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotEmpty;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.CosNaming.NamingContextPackage.NotFoundReason;

/* loaded from: rt.jar:com/sun/jndi/cosnaming/ExceptionMapper.class */
public final class ExceptionMapper {
    private static final boolean debug = false;

    private ExceptionMapper() {
    }

    public static final NamingException mapException(Exception exc, CNCtx cNCtx, NameComponent[] nameComponentArr) throws NamingException {
        NamingException namingException;
        if (exc instanceof NamingException) {
            return (NamingException) exc;
        }
        if (exc instanceof RuntimeException) {
            throw ((RuntimeException) exc);
        }
        if (exc instanceof NotFound) {
            if (cNCtx.federation) {
                return tryFed((NotFound) exc, cNCtx, nameComponentArr);
            }
            namingException = new NameNotFoundException();
        } else if (exc instanceof CannotProceed) {
            namingException = new CannotProceedException();
            NamingContext namingContext = ((CannotProceed) exc).cxt;
            NameComponent[] nameComponentArr2 = ((CannotProceed) exc).rest_of_name;
            if (nameComponentArr != null && nameComponentArr.length > nameComponentArr2.length) {
                NameComponent[] nameComponentArr3 = new NameComponent[nameComponentArr.length - nameComponentArr2.length];
                System.arraycopy(nameComponentArr, 0, nameComponentArr3, 0, nameComponentArr3.length);
                namingException.setResolvedObj(new CNCtx(cNCtx._orb, cNCtx.orbTracker, namingContext, cNCtx._env, cNCtx.makeFullName(nameComponentArr3)));
            } else {
                namingException.setResolvedObj(cNCtx);
            }
            namingException.setRemainingName(CNNameParser.cosNameToName(nameComponentArr2));
        } else if (exc instanceof InvalidName) {
            namingException = new InvalidNameException();
        } else if (exc instanceof AlreadyBound) {
            namingException = new NameAlreadyBoundException();
        } else if (exc instanceof NotEmpty) {
            namingException = new ContextNotEmptyException();
        } else {
            namingException = new NamingException("Unknown reasons");
        }
        namingException.setRootCause(exc);
        return namingException;
    }

    private static final NamingException tryFed(NotFound notFound, CNCtx cNCtx, NameComponent[] nameComponentArr) throws NamingException {
        int length;
        NameComponent[] nameComponentArr2 = notFound.rest_of_name;
        if (nameComponentArr2.length == 1 && nameComponentArr != null) {
            NameComponent nameComponent = nameComponentArr[nameComponentArr.length - 1];
            if (!nameComponentArr2[0].id.equals(nameComponent.id) || nameComponentArr2[0].kind == null || !nameComponentArr2[0].kind.equals(nameComponent.kind)) {
                NameNotFoundException nameNotFoundException = new NameNotFoundException();
                nameNotFoundException.setRemainingName(CNNameParser.cosNameToName(nameComponentArr2));
                nameNotFoundException.setRootCause(notFound);
                throw nameNotFoundException;
            }
        }
        NameComponent[] nameComponentArr3 = null;
        if (nameComponentArr != null && nameComponentArr.length >= nameComponentArr2.length) {
            if (notFound.why == NotFoundReason.not_context) {
                length = nameComponentArr.length - (nameComponentArr2.length - 1);
                if (nameComponentArr2.length == 1) {
                    nameComponentArr2 = null;
                } else {
                    NameComponent[] nameComponentArr4 = new NameComponent[nameComponentArr2.length - 1];
                    System.arraycopy(nameComponentArr2, 1, nameComponentArr4, 0, nameComponentArr4.length);
                    nameComponentArr2 = nameComponentArr4;
                }
            } else {
                length = nameComponentArr.length - nameComponentArr2.length;
            }
            if (length > 0) {
                nameComponentArr3 = new NameComponent[length];
                System.arraycopy(nameComponentArr, 0, nameComponentArr3, 0, length);
            }
        }
        CannotProceedException cannotProceedException = new CannotProceedException();
        cannotProceedException.setRootCause(notFound);
        if (nameComponentArr2 != null && nameComponentArr2.length > 0) {
            cannotProceedException.setRemainingName(CNNameParser.cosNameToName(nameComponentArr2));
        }
        cannotProceedException.setEnvironment(cNCtx._env);
        final Object objCallResolve = nameComponentArr3 != null ? cNCtx.callResolve(nameComponentArr3) : cNCtx;
        if (objCallResolve instanceof Context) {
            Reference reference = new Reference(Constants.OBJECT_CLASS, new RefAddr("nns") { // from class: com.sun.jndi.cosnaming.ExceptionMapper.1
                private static final long serialVersionUID = 669984699392133792L;

                @Override // javax.naming.RefAddr
                public Object getContent() {
                    return objCallResolve;
                }
            });
            CompositeName compositeName = new CompositeName();
            compositeName.add("");
            cannotProceedException.setResolvedObj(reference);
            cannotProceedException.setAltName(compositeName);
            cannotProceedException.setAltNameCtx((Context) objCallResolve);
            return cannotProceedException;
        }
        Name nameCosNameToName = CNNameParser.cosNameToName(nameComponentArr3);
        Object objectInstance = null;
        try {
            if (CorbaUtils.isObjectFactoryTrusted(objCallResolve)) {
                objectInstance = NamingManager.getObjectInstance(objCallResolve, nameCosNameToName, cNCtx, cNCtx._env);
            }
            if (objectInstance instanceof Context) {
                cannotProceedException.setResolvedObj(objectInstance);
            } else {
                nameCosNameToName.add("");
                cannotProceedException.setAltName(nameCosNameToName);
                final Object obj = objectInstance;
                cannotProceedException.setResolvedObj(new Reference(Constants.OBJECT_CLASS, new RefAddr("nns") { // from class: com.sun.jndi.cosnaming.ExceptionMapper.2
                    private static final long serialVersionUID = -785132553978269772L;

                    @Override // javax.naming.RefAddr
                    public Object getContent() {
                        return obj;
                    }
                }));
                cannotProceedException.setAltNameCtx(cNCtx);
            }
            return cannotProceedException;
        } catch (NamingException e2) {
            throw e2;
        } catch (Exception e3) {
            NamingException namingException = new NamingException("problem generating object using object factory");
            namingException.setRootCause(e3);
            throw namingException;
        }
    }
}
