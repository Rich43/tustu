package org.apache.commons.math3.ode.events;

import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/events/Transformer.class */
enum Transformer {
    UNINITIALIZED { // from class: org.apache.commons.math3.ode.events.Transformer.1
        @Override // org.apache.commons.math3.ode.events.Transformer
        protected double transformed(double g2) {
            return 0.0d;
        }
    },
    PLUS { // from class: org.apache.commons.math3.ode.events.Transformer.2
        @Override // org.apache.commons.math3.ode.events.Transformer
        protected double transformed(double g2) {
            return g2;
        }
    },
    MINUS { // from class: org.apache.commons.math3.ode.events.Transformer.3
        @Override // org.apache.commons.math3.ode.events.Transformer
        protected double transformed(double g2) {
            return -g2;
        }
    },
    MIN { // from class: org.apache.commons.math3.ode.events.Transformer.4
        @Override // org.apache.commons.math3.ode.events.Transformer
        protected double transformed(double g2) {
            return FastMath.min(-Precision.SAFE_MIN, FastMath.min(-g2, g2));
        }
    },
    MAX { // from class: org.apache.commons.math3.ode.events.Transformer.5
        @Override // org.apache.commons.math3.ode.events.Transformer
        protected double transformed(double g2) {
            return FastMath.max(Precision.SAFE_MIN, FastMath.max(-g2, g2));
        }
    };

    protected abstract double transformed(double d2);
}
