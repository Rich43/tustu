package org.omg.CosNaming;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.util.Dictionary;
import java.util.Hashtable;
import org.icepdf.core.util.PdfOps;
import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.DynamicImplementation;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.NVList;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.ServerRequest;
import org.omg.CORBA.TCKind;
import org.omg.CosNaming.NamingContextPackage.AlreadyBound;
import org.omg.CosNaming.NamingContextPackage.AlreadyBoundHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.CannotProceedHelper;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.InvalidNameHelper;
import org.omg.CosNaming.NamingContextPackage.NotEmpty;
import org.omg.CosNaming.NamingContextPackage.NotEmptyHelper;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.CosNaming.NamingContextPackage.NotFoundHelper;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:org/omg/CosNaming/_NamingContextImplBase.class */
public abstract class _NamingContextImplBase extends DynamicImplementation implements NamingContext {
    private static final String[] _type_ids = {"IDL:omg.org/CosNaming/NamingContext:1.0"};
    private static Dictionary _methods = new Hashtable();

    static {
        _methods.put("bind", new Integer(0));
        _methods.put("bind_context", new Integer(1));
        _methods.put("rebind", new Integer(2));
        _methods.put("rebind_context", new Integer(3));
        _methods.put(SecurityConstants.SOCKET_RESOLVE_ACTION, new Integer(4));
        _methods.put("unbind", new Integer(5));
        _methods.put(SchemaSymbols.ATTVAL_LIST, new Integer(6));
        _methods.put("new_context", new Integer(7));
        _methods.put("bind_new_context", new Integer(8));
        _methods.put("destroy", new Integer(9));
    }

    @Override // org.omg.CORBA.DynamicImplementation, org.omg.CORBA.portable.ObjectImpl
    public String[] _ids() {
        return (String[]) _type_ids.clone();
    }

    @Override // org.omg.CORBA.DynamicImplementation
    public void invoke(ServerRequest serverRequest) throws MARSHAL, BAD_OPERATION {
        switch (((Integer) _methods.get(serverRequest.op_name())).intValue()) {
            case 0:
                NVList nVListCreate_list = _orb().create_list(0);
                Any anyCreate_any = _orb().create_any();
                anyCreate_any.type(NameHelper.type());
                nVListCreate_list.add_value(PdfOps.n_TOKEN, anyCreate_any, 1);
                Any anyCreate_any2 = _orb().create_any();
                anyCreate_any2.type(ORB.init().get_primitive_tc(TCKind.tk_objref));
                nVListCreate_list.add_value("obj", anyCreate_any2, 1);
                serverRequest.params(nVListCreate_list);
                try {
                    bind(NameHelper.extract(anyCreate_any), anyCreate_any2.extract_Object());
                    Any anyCreate_any3 = _orb().create_any();
                    anyCreate_any3.type(_orb().get_primitive_tc(TCKind.tk_void));
                    serverRequest.result(anyCreate_any3);
                    return;
                } catch (AlreadyBound e2) {
                    Any anyCreate_any4 = _orb().create_any();
                    AlreadyBoundHelper.insert(anyCreate_any4, e2);
                    serverRequest.except(anyCreate_any4);
                    return;
                } catch (CannotProceed e3) {
                    Any anyCreate_any5 = _orb().create_any();
                    CannotProceedHelper.insert(anyCreate_any5, e3);
                    serverRequest.except(anyCreate_any5);
                    return;
                } catch (InvalidName e4) {
                    Any anyCreate_any6 = _orb().create_any();
                    InvalidNameHelper.insert(anyCreate_any6, e4);
                    serverRequest.except(anyCreate_any6);
                    return;
                } catch (NotFound e5) {
                    Any anyCreate_any7 = _orb().create_any();
                    NotFoundHelper.insert(anyCreate_any7, e5);
                    serverRequest.except(anyCreate_any7);
                    return;
                }
            case 1:
                NVList nVListCreate_list2 = _orb().create_list(0);
                Any anyCreate_any8 = _orb().create_any();
                anyCreate_any8.type(NameHelper.type());
                nVListCreate_list2.add_value(PdfOps.n_TOKEN, anyCreate_any8, 1);
                Any anyCreate_any9 = _orb().create_any();
                anyCreate_any9.type(NamingContextHelper.type());
                nVListCreate_list2.add_value("nc", anyCreate_any9, 1);
                serverRequest.params(nVListCreate_list2);
                try {
                    bind_context(NameHelper.extract(anyCreate_any8), NamingContextHelper.extract(anyCreate_any9));
                    Any anyCreate_any10 = _orb().create_any();
                    anyCreate_any10.type(_orb().get_primitive_tc(TCKind.tk_void));
                    serverRequest.result(anyCreate_any10);
                    return;
                } catch (AlreadyBound e6) {
                    Any anyCreate_any11 = _orb().create_any();
                    AlreadyBoundHelper.insert(anyCreate_any11, e6);
                    serverRequest.except(anyCreate_any11);
                    return;
                } catch (CannotProceed e7) {
                    Any anyCreate_any12 = _orb().create_any();
                    CannotProceedHelper.insert(anyCreate_any12, e7);
                    serverRequest.except(anyCreate_any12);
                    return;
                } catch (InvalidName e8) {
                    Any anyCreate_any13 = _orb().create_any();
                    InvalidNameHelper.insert(anyCreate_any13, e8);
                    serverRequest.except(anyCreate_any13);
                    return;
                } catch (NotFound e9) {
                    Any anyCreate_any14 = _orb().create_any();
                    NotFoundHelper.insert(anyCreate_any14, e9);
                    serverRequest.except(anyCreate_any14);
                    return;
                }
            case 2:
                NVList nVListCreate_list3 = _orb().create_list(0);
                Any anyCreate_any15 = _orb().create_any();
                anyCreate_any15.type(NameHelper.type());
                nVListCreate_list3.add_value(PdfOps.n_TOKEN, anyCreate_any15, 1);
                Any anyCreate_any16 = _orb().create_any();
                anyCreate_any16.type(ORB.init().get_primitive_tc(TCKind.tk_objref));
                nVListCreate_list3.add_value("obj", anyCreate_any16, 1);
                serverRequest.params(nVListCreate_list3);
                try {
                    rebind(NameHelper.extract(anyCreate_any15), anyCreate_any16.extract_Object());
                    Any anyCreate_any17 = _orb().create_any();
                    anyCreate_any17.type(_orb().get_primitive_tc(TCKind.tk_void));
                    serverRequest.result(anyCreate_any17);
                    return;
                } catch (CannotProceed e10) {
                    Any anyCreate_any18 = _orb().create_any();
                    CannotProceedHelper.insert(anyCreate_any18, e10);
                    serverRequest.except(anyCreate_any18);
                    return;
                } catch (InvalidName e11) {
                    Any anyCreate_any19 = _orb().create_any();
                    InvalidNameHelper.insert(anyCreate_any19, e11);
                    serverRequest.except(anyCreate_any19);
                    return;
                } catch (NotFound e12) {
                    Any anyCreate_any20 = _orb().create_any();
                    NotFoundHelper.insert(anyCreate_any20, e12);
                    serverRequest.except(anyCreate_any20);
                    return;
                }
            case 3:
                NVList nVListCreate_list4 = _orb().create_list(0);
                Any anyCreate_any21 = _orb().create_any();
                anyCreate_any21.type(NameHelper.type());
                nVListCreate_list4.add_value(PdfOps.n_TOKEN, anyCreate_any21, 1);
                Any anyCreate_any22 = _orb().create_any();
                anyCreate_any22.type(NamingContextHelper.type());
                nVListCreate_list4.add_value("nc", anyCreate_any22, 1);
                serverRequest.params(nVListCreate_list4);
                try {
                    rebind_context(NameHelper.extract(anyCreate_any21), NamingContextHelper.extract(anyCreate_any22));
                    Any anyCreate_any23 = _orb().create_any();
                    anyCreate_any23.type(_orb().get_primitive_tc(TCKind.tk_void));
                    serverRequest.result(anyCreate_any23);
                    return;
                } catch (CannotProceed e13) {
                    Any anyCreate_any24 = _orb().create_any();
                    CannotProceedHelper.insert(anyCreate_any24, e13);
                    serverRequest.except(anyCreate_any24);
                    return;
                } catch (InvalidName e14) {
                    Any anyCreate_any25 = _orb().create_any();
                    InvalidNameHelper.insert(anyCreate_any25, e14);
                    serverRequest.except(anyCreate_any25);
                    return;
                } catch (NotFound e15) {
                    Any anyCreate_any26 = _orb().create_any();
                    NotFoundHelper.insert(anyCreate_any26, e15);
                    serverRequest.except(anyCreate_any26);
                    return;
                }
            case 4:
                NVList nVListCreate_list5 = _orb().create_list(0);
                Any anyCreate_any27 = _orb().create_any();
                anyCreate_any27.type(NameHelper.type());
                nVListCreate_list5.add_value(PdfOps.n_TOKEN, anyCreate_any27, 1);
                serverRequest.params(nVListCreate_list5);
                try {
                    Object objectResolve = resolve(NameHelper.extract(anyCreate_any27));
                    Any anyCreate_any28 = _orb().create_any();
                    anyCreate_any28.insert_Object(objectResolve);
                    serverRequest.result(anyCreate_any28);
                    return;
                } catch (CannotProceed e16) {
                    Any anyCreate_any29 = _orb().create_any();
                    CannotProceedHelper.insert(anyCreate_any29, e16);
                    serverRequest.except(anyCreate_any29);
                    return;
                } catch (InvalidName e17) {
                    Any anyCreate_any30 = _orb().create_any();
                    InvalidNameHelper.insert(anyCreate_any30, e17);
                    serverRequest.except(anyCreate_any30);
                    return;
                } catch (NotFound e18) {
                    Any anyCreate_any31 = _orb().create_any();
                    NotFoundHelper.insert(anyCreate_any31, e18);
                    serverRequest.except(anyCreate_any31);
                    return;
                }
            case 5:
                NVList nVListCreate_list6 = _orb().create_list(0);
                Any anyCreate_any32 = _orb().create_any();
                anyCreate_any32.type(NameHelper.type());
                nVListCreate_list6.add_value(PdfOps.n_TOKEN, anyCreate_any32, 1);
                serverRequest.params(nVListCreate_list6);
                try {
                    unbind(NameHelper.extract(anyCreate_any32));
                    Any anyCreate_any33 = _orb().create_any();
                    anyCreate_any33.type(_orb().get_primitive_tc(TCKind.tk_void));
                    serverRequest.result(anyCreate_any33);
                    return;
                } catch (CannotProceed e19) {
                    Any anyCreate_any34 = _orb().create_any();
                    CannotProceedHelper.insert(anyCreate_any34, e19);
                    serverRequest.except(anyCreate_any34);
                    return;
                } catch (InvalidName e20) {
                    Any anyCreate_any35 = _orb().create_any();
                    InvalidNameHelper.insert(anyCreate_any35, e20);
                    serverRequest.except(anyCreate_any35);
                    return;
                } catch (NotFound e21) {
                    Any anyCreate_any36 = _orb().create_any();
                    NotFoundHelper.insert(anyCreate_any36, e21);
                    serverRequest.except(anyCreate_any36);
                    return;
                }
            case 6:
                NVList nVListCreate_list7 = _orb().create_list(0);
                Any anyCreate_any37 = _orb().create_any();
                anyCreate_any37.type(ORB.init().get_primitive_tc(TCKind.tk_ulong));
                nVListCreate_list7.add_value("how_many", anyCreate_any37, 1);
                Any anyCreate_any38 = _orb().create_any();
                anyCreate_any38.type(BindingListHelper.type());
                nVListCreate_list7.add_value("bl", anyCreate_any38, 2);
                Any anyCreate_any39 = _orb().create_any();
                anyCreate_any39.type(BindingIteratorHelper.type());
                nVListCreate_list7.add_value("bi", anyCreate_any39, 2);
                serverRequest.params(nVListCreate_list7);
                int iExtract_ulong = anyCreate_any37.extract_ulong();
                BindingListHolder bindingListHolder = new BindingListHolder();
                BindingIteratorHolder bindingIteratorHolder = new BindingIteratorHolder();
                list(iExtract_ulong, bindingListHolder, bindingIteratorHolder);
                BindingListHelper.insert(anyCreate_any38, bindingListHolder.value);
                BindingIteratorHelper.insert(anyCreate_any39, bindingIteratorHolder.value);
                Any anyCreate_any40 = _orb().create_any();
                anyCreate_any40.type(_orb().get_primitive_tc(TCKind.tk_void));
                serverRequest.result(anyCreate_any40);
                return;
            case 7:
                serverRequest.params(_orb().create_list(0));
                NamingContext namingContextNew_context = new_context();
                Any anyCreate_any41 = _orb().create_any();
                NamingContextHelper.insert(anyCreate_any41, namingContextNew_context);
                serverRequest.result(anyCreate_any41);
                return;
            case 8:
                NVList nVListCreate_list8 = _orb().create_list(0);
                Any anyCreate_any42 = _orb().create_any();
                anyCreate_any42.type(NameHelper.type());
                nVListCreate_list8.add_value(PdfOps.n_TOKEN, anyCreate_any42, 1);
                serverRequest.params(nVListCreate_list8);
                try {
                    NamingContext namingContextBind_new_context = bind_new_context(NameHelper.extract(anyCreate_any42));
                    Any anyCreate_any43 = _orb().create_any();
                    NamingContextHelper.insert(anyCreate_any43, namingContextBind_new_context);
                    serverRequest.result(anyCreate_any43);
                    return;
                } catch (AlreadyBound e22) {
                    Any anyCreate_any44 = _orb().create_any();
                    AlreadyBoundHelper.insert(anyCreate_any44, e22);
                    serverRequest.except(anyCreate_any44);
                    return;
                } catch (CannotProceed e23) {
                    Any anyCreate_any45 = _orb().create_any();
                    CannotProceedHelper.insert(anyCreate_any45, e23);
                    serverRequest.except(anyCreate_any45);
                    return;
                } catch (InvalidName e24) {
                    Any anyCreate_any46 = _orb().create_any();
                    InvalidNameHelper.insert(anyCreate_any46, e24);
                    serverRequest.except(anyCreate_any46);
                    return;
                } catch (NotFound e25) {
                    Any anyCreate_any47 = _orb().create_any();
                    NotFoundHelper.insert(anyCreate_any47, e25);
                    serverRequest.except(anyCreate_any47);
                    return;
                }
            case 9:
                serverRequest.params(_orb().create_list(0));
                try {
                    destroy();
                    Any anyCreate_any48 = _orb().create_any();
                    anyCreate_any48.type(_orb().get_primitive_tc(TCKind.tk_void));
                    serverRequest.result(anyCreate_any48);
                    return;
                } catch (NotEmpty e26) {
                    Any anyCreate_any49 = _orb().create_any();
                    NotEmptyHelper.insert(anyCreate_any49, e26);
                    serverRequest.except(anyCreate_any49);
                    return;
                }
            default:
                throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
        }
    }
}
