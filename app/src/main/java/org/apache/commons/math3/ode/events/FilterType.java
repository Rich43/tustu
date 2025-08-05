package org.apache.commons.math3.ode.events;

import org.apache.commons.math3.exception.MathInternalError;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/events/FilterType.class */
public enum FilterType {
    TRIGGER_ONLY_DECREASING_EVENTS { // from class: org.apache.commons.math3.ode.events.FilterType.1
        @Override // org.apache.commons.math3.ode.events.FilterType
        protected boolean getTriggeredIncreasing() {
            return false;
        }

        @Override // org.apache.commons.math3.ode.events.FilterType
        protected Transformer selectTransformer(Transformer previous, double g2, boolean forward) {
            if (forward) {
                switch (AnonymousClass3.$SwitchMap$org$apache$commons$math3$ode$events$Transformer[previous.ordinal()]) {
                    case 1:
                        if (g2 > 0.0d) {
                            return Transformer.MAX;
                        }
                        if (g2 < 0.0d) {
                            return Transformer.PLUS;
                        }
                        return Transformer.UNINITIALIZED;
                    case 2:
                        if (g2 >= 0.0d) {
                            return Transformer.MIN;
                        }
                        return previous;
                    case 3:
                        if (g2 >= 0.0d) {
                            return Transformer.MAX;
                        }
                        return previous;
                    case 4:
                        if (g2 <= 0.0d) {
                            return Transformer.MINUS;
                        }
                        return previous;
                    case 5:
                        if (g2 <= 0.0d) {
                            return Transformer.PLUS;
                        }
                        return previous;
                    default:
                        throw new MathInternalError();
                }
            }
            switch (AnonymousClass3.$SwitchMap$org$apache$commons$math3$ode$events$Transformer[previous.ordinal()]) {
                case 1:
                    if (g2 > 0.0d) {
                        return Transformer.MINUS;
                    }
                    if (g2 < 0.0d) {
                        return Transformer.MIN;
                    }
                    return Transformer.UNINITIALIZED;
                case 2:
                    if (g2 <= 0.0d) {
                        return Transformer.MAX;
                    }
                    return previous;
                case 3:
                    if (g2 <= 0.0d) {
                        return Transformer.MIN;
                    }
                    return previous;
                case 4:
                    if (g2 >= 0.0d) {
                        return Transformer.PLUS;
                    }
                    return previous;
                case 5:
                    if (g2 >= 0.0d) {
                        return Transformer.MINUS;
                    }
                    return previous;
                default:
                    throw new MathInternalError();
            }
        }
    },
    TRIGGER_ONLY_INCREASING_EVENTS { // from class: org.apache.commons.math3.ode.events.FilterType.2
        @Override // org.apache.commons.math3.ode.events.FilterType
        protected boolean getTriggeredIncreasing() {
            return true;
        }

        @Override // org.apache.commons.math3.ode.events.FilterType
        protected Transformer selectTransformer(Transformer previous, double g2, boolean forward) {
            if (forward) {
                switch (AnonymousClass3.$SwitchMap$org$apache$commons$math3$ode$events$Transformer[previous.ordinal()]) {
                    case 1:
                        if (g2 > 0.0d) {
                            return Transformer.PLUS;
                        }
                        if (g2 < 0.0d) {
                            return Transformer.MIN;
                        }
                        return Transformer.UNINITIALIZED;
                    case 2:
                        if (g2 <= 0.0d) {
                            return Transformer.MAX;
                        }
                        return previous;
                    case 3:
                        if (g2 <= 0.0d) {
                            return Transformer.MIN;
                        }
                        return previous;
                    case 4:
                        if (g2 >= 0.0d) {
                            return Transformer.PLUS;
                        }
                        return previous;
                    case 5:
                        if (g2 >= 0.0d) {
                            return Transformer.MINUS;
                        }
                        return previous;
                    default:
                        throw new MathInternalError();
                }
            }
            switch (AnonymousClass3.$SwitchMap$org$apache$commons$math3$ode$events$Transformer[previous.ordinal()]) {
                case 1:
                    if (g2 > 0.0d) {
                        return Transformer.MAX;
                    }
                    if (g2 < 0.0d) {
                        return Transformer.MINUS;
                    }
                    return Transformer.UNINITIALIZED;
                case 2:
                    if (g2 >= 0.0d) {
                        return Transformer.MIN;
                    }
                    return previous;
                case 3:
                    if (g2 >= 0.0d) {
                        return Transformer.MAX;
                    }
                    return previous;
                case 4:
                    if (g2 <= 0.0d) {
                        return Transformer.MINUS;
                    }
                    return previous;
                case 5:
                    if (g2 <= 0.0d) {
                        return Transformer.PLUS;
                    }
                    return previous;
                default:
                    throw new MathInternalError();
            }
        }
    };

    protected abstract boolean getTriggeredIncreasing();

    protected abstract Transformer selectTransformer(Transformer transformer, double d2, boolean z2);

    /* renamed from: org.apache.commons.math3.ode.events.FilterType$3, reason: invalid class name */
    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/events/FilterType$3.class */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$org$apache$commons$math3$ode$events$Transformer = new int[Transformer.values().length];

        static {
            try {
                $SwitchMap$org$apache$commons$math3$ode$events$Transformer[Transformer.UNINITIALIZED.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$apache$commons$math3$ode$events$Transformer[Transformer.PLUS.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$apache$commons$math3$ode$events$Transformer[Transformer.MINUS.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$apache$commons$math3$ode$events$Transformer[Transformer.MIN.ordinal()] = 4;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$org$apache$commons$math3$ode$events$Transformer[Transformer.MAX.ordinal()] = 5;
            } catch (NoSuchFieldError e6) {
            }
        }
    }
}
