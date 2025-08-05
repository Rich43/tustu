package com.sun.corba.se.impl.orbutil.fsm;

import com.sun.corba.se.spi.orbutil.fsm.Action;
import com.sun.corba.se.spi.orbutil.fsm.Guard;
import com.sun.corba.se.spi.orbutil.fsm.Input;
import com.sun.corba.se.spi.orbutil.fsm.State;
import java.util.StringTokenizer;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/fsm/NameBase.class */
public class NameBase {
    private String name;
    private String toStringName;

    private String getClassName() {
        StringTokenizer stringTokenizer = new StringTokenizer(getClass().getName(), ".");
        String strNextToken = stringTokenizer.nextToken();
        while (true) {
            String str = strNextToken;
            if (stringTokenizer.hasMoreTokens()) {
                strNextToken = stringTokenizer.nextToken();
            } else {
                return str;
            }
        }
    }

    private String getPreferredClassName() {
        if (this instanceof Action) {
            return "Action";
        }
        if (this instanceof State) {
            return "State";
        }
        if (this instanceof Guard) {
            return "Guard";
        }
        if (this instanceof Input) {
            return "Input";
        }
        return getClassName();
    }

    public NameBase(String str) {
        this.name = str;
        this.toStringName = getPreferredClassName() + "[" + str + "]";
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.toStringName;
    }
}
