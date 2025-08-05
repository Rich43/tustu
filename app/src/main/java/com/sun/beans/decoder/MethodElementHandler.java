package com.sun.beans.decoder;

import com.sun.beans.finder.MethodFinder;
import java.lang.reflect.Method;
import sun.reflect.misc.MethodUtil;

/* loaded from: rt.jar:com/sun/beans/decoder/MethodElementHandler.class */
final class MethodElementHandler extends NewElementHandler {
    private String name;

    MethodElementHandler() {
    }

    @Override // com.sun.beans.decoder.NewElementHandler, com.sun.beans.decoder.ElementHandler
    public void addAttribute(String str, String str2) {
        if (str.equals("name")) {
            this.name = str2;
        } else {
            super.addAttribute(str, str2);
        }
    }

    @Override // com.sun.beans.decoder.NewElementHandler
    protected ValueObject getValueObject(Class<?> cls, Object[] objArr) throws Exception {
        Method methodFindMethod;
        Object contextBean = getContextBean();
        Class<?>[] argumentTypes = getArgumentTypes(objArr);
        if (cls != null) {
            methodFindMethod = MethodFinder.findStaticMethod(cls, this.name, argumentTypes);
        } else {
            methodFindMethod = MethodFinder.findMethod(contextBean.getClass(), this.name, argumentTypes);
        }
        Method method = methodFindMethod;
        if (method.isVarArgs()) {
            objArr = getArguments(objArr, method.getParameterTypes());
        }
        return method.getReturnType().equals(Void.TYPE) ? ValueObjectImpl.VOID : ValueObjectImpl.create(MethodUtil.invoke(method, contextBean, objArr));
    }
}
