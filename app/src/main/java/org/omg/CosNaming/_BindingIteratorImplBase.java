package org.omg.CosNaming;

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
import org.omg.CORBA.ServerRequest;
import org.omg.CORBA.TCKind;

/* loaded from: rt.jar:org/omg/CosNaming/_BindingIteratorImplBase.class */
public abstract class _BindingIteratorImplBase extends DynamicImplementation implements BindingIterator {
    private static final String[] _type_ids = {"IDL:omg.org/CosNaming/BindingIterator:1.0"};
    private static Dictionary _methods = new Hashtable();

    static {
        _methods.put("next_one", new Integer(0));
        _methods.put("next_n", new Integer(1));
        _methods.put("destroy", new Integer(2));
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
                anyCreate_any.type(BindingHelper.type());
                nVListCreate_list.add_value(PdfOps.b_TOKEN, anyCreate_any, 2);
                serverRequest.params(nVListCreate_list);
                BindingHolder bindingHolder = new BindingHolder();
                boolean zNext_one = next_one(bindingHolder);
                BindingHelper.insert(anyCreate_any, bindingHolder.value);
                Any anyCreate_any2 = _orb().create_any();
                anyCreate_any2.insert_boolean(zNext_one);
                serverRequest.result(anyCreate_any2);
                return;
            case 1:
                NVList nVListCreate_list2 = _orb().create_list(0);
                Any anyCreate_any3 = _orb().create_any();
                anyCreate_any3.type(ORB.init().get_primitive_tc(TCKind.tk_ulong));
                nVListCreate_list2.add_value("how_many", anyCreate_any3, 1);
                Any anyCreate_any4 = _orb().create_any();
                anyCreate_any4.type(BindingListHelper.type());
                nVListCreate_list2.add_value("bl", anyCreate_any4, 2);
                serverRequest.params(nVListCreate_list2);
                int iExtract_ulong = anyCreate_any3.extract_ulong();
                BindingListHolder bindingListHolder = new BindingListHolder();
                boolean zNext_n = next_n(iExtract_ulong, bindingListHolder);
                BindingListHelper.insert(anyCreate_any4, bindingListHolder.value);
                Any anyCreate_any5 = _orb().create_any();
                anyCreate_any5.insert_boolean(zNext_n);
                serverRequest.result(anyCreate_any5);
                return;
            case 2:
                serverRequest.params(_orb().create_list(0));
                destroy();
                Any anyCreate_any6 = _orb().create_any();
                anyCreate_any6.type(_orb().get_primitive_tc(TCKind.tk_void));
                serverRequest.result(anyCreate_any6);
                return;
            default:
                throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
        }
    }
}
