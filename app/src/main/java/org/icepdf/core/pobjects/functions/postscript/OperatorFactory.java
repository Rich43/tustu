package org.icepdf.core.pobjects.functions.postscript;

import java.util.HashMap;
import java.util.Stack;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/functions/postscript/OperatorFactory.class */
public class OperatorFactory {
    private static HashMap<Integer, Operator> operatorCache = new HashMap<>();

    public static Operator getOperator(char[] ch, int offset, int length) {
        Operator operator;
        final int operatorType = OperatorNames.getType(ch, offset, length);
        Operator operator2 = operatorCache.get(Integer.valueOf(operatorType));
        if (operator2 != null) {
            return operator2;
        }
        switch (operatorType) {
            case 1:
                operator = new Operator(1) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.1
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        Float num = (Float) stack.pop();
                        stack.push(Float.valueOf(Math.abs(num.floatValue())));
                    }
                };
                break;
            case 2:
                operator = new Operator(2) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.2
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        Float num2 = (Float) stack.pop();
                        Float num1 = (Float) stack.pop();
                        stack.push(Float.valueOf(num1.floatValue() + num2.floatValue()));
                    }
                };
                break;
            case 3:
                operator = new Operator(3) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.3
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        Object value = stack.pop();
                        if (value instanceof Boolean) {
                            boolean bool2 = ((Boolean) value).booleanValue();
                            boolean bool1 = ((Boolean) stack.pop()).booleanValue();
                            stack.push(Boolean.valueOf(bool1 && bool2));
                        } else {
                            int val1 = ((Float) value).intValue();
                            int val2 = ((Float) stack.pop()).intValue();
                            stack.push(Integer.valueOf(val1 & val2));
                        }
                    }
                };
                break;
            case 4:
                operator = new Operator(4) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.4
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        float den = ((Float) stack.pop()).floatValue();
                        float num = ((Float) stack.pop()).floatValue();
                        stack.push(Float.valueOf(Double.valueOf(Math.toDegrees(Math.atan(num / den))).floatValue()));
                    }
                };
                break;
            case 5:
                operator = new Operator(5) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.5
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        long shift = ((Long) stack.pop()).longValue();
                        long int1 = ((Long) stack.pop()).longValue();
                        stack.push(Long.valueOf(int1 << ((int) shift)));
                    }
                };
                break;
            case 6:
                operator = new Operator(6) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.6
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        float num1 = ((Float) stack.pop()).floatValue();
                        stack.push(Float.valueOf(Double.valueOf(Math.ceil(num1)).floatValue()));
                    }
                };
                break;
            case 7:
                operator = new Operator(7) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.7
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        float aAngle = ((Float) stack.pop()).floatValue();
                        stack.push(Float.valueOf(Double.valueOf(Math.cos(aAngle)).floatValue()));
                    }
                };
                break;
            case 8:
                operator = new Operator(8) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.8
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        int n2 = ((Float) stack.pop()).intValue();
                        int top = stack.size();
                        for (int i2 = top - n2; i2 < top; i2++) {
                            stack.push(stack.get(i2));
                        }
                    }
                };
                break;
            case 9:
                operator = new Operator(9) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.9
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        int number = ((Float) stack.pop()).intValue();
                        stack.push(Integer.valueOf(number));
                    }
                };
                break;
            case 10:
                operator = new Operator(10) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.10
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        float number = ((Float) stack.pop()).floatValue();
                        stack.push(Float.valueOf(number));
                    }
                };
                break;
            case 11:
                operator = new Operator(11) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.11
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        float num2 = ((Float) stack.pop()).floatValue();
                        float num1 = ((Float) stack.pop()).floatValue();
                        stack.push(Float.valueOf(num1 / num2));
                    }
                };
                break;
            case 12:
                operator = new Operator(12) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.12
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        stack.push(stack.peek());
                    }
                };
                break;
            case 13:
                operator = new Operator(13) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.13
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        Object any2 = stack.pop();
                        Object any1 = stack.pop();
                        stack.push(Boolean.valueOf(any1.equals(any2)));
                    }
                };
                break;
            case 14:
                operator = new Operator(14) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.14
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        Object any2 = stack.pop();
                        Object any1 = stack.pop();
                        stack.push(any2);
                        stack.push(any1);
                    }
                };
                break;
            case 15:
                operator = new Operator(15) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.15
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        float exponent = ((Float) stack.pop()).floatValue();
                        float base = ((Float) stack.pop()).floatValue();
                        stack.push(Float.valueOf(Double.valueOf(Math.pow(base, exponent)).floatValue()));
                    }
                };
                break;
            case 16:
            case 40:
            default:
                operator = new Operator(0) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.41
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        System.out.println(operatorType + " not implemented ");
                    }
                };
                break;
            case 17:
                operator = new Operator(17) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.16
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        float num1 = ((Float) stack.pop()).floatValue();
                        stack.push(Float.valueOf(Double.valueOf(Math.floor(num1)).floatValue()));
                    }
                };
                break;
            case 18:
                operator = new Operator(18) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.17
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        float num2 = ((Float) stack.pop()).floatValue();
                        float num1 = ((Float) stack.pop()).floatValue();
                        stack.push(Boolean.valueOf(num1 >= num2));
                    }
                };
                break;
            case 19:
                operator = new Operator(19) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.18
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        float num2 = ((Float) stack.pop()).floatValue();
                        float num1 = ((Float) stack.pop()).floatValue();
                        stack.push(Boolean.valueOf(num1 > num2));
                    }
                };
                break;
            case 20:
                operator = new Operator(20) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.19
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        float num2 = ((Float) stack.pop()).floatValue();
                        float num1 = ((Float) stack.pop()).floatValue();
                        stack.push(Integer.valueOf((int) (num1 / num2)));
                    }
                };
                break;
            case 21:
                operator = new Operator(21) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.20
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        Procedure proc1 = null;
                        if (stack.peek() instanceof Procedure) {
                            proc1 = (Procedure) stack.pop();
                        }
                        boolean bool = ((Boolean) stack.pop()).booleanValue();
                        if (bool) {
                            proc1.eval(stack);
                        }
                    }
                };
                break;
            case 22:
                operator = new Operator(22) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.21
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        Procedure proc2 = null;
                        Procedure proc1 = null;
                        if (stack.peek() instanceof Procedure) {
                            proc2 = (Procedure) stack.pop();
                        }
                        if (stack.peek() instanceof Procedure) {
                            proc1 = (Procedure) stack.pop();
                        }
                        boolean bool = ((Boolean) stack.pop()).booleanValue();
                        if (bool) {
                            proc1.eval(stack);
                        } else {
                            proc2.eval(stack);
                        }
                    }
                };
                break;
            case 23:
                operator = new Operator(23) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.24
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        float num = ((Float) stack.pop()).floatValue();
                        stack.push(Float.valueOf(Double.valueOf(Math.log(num)).floatValue()));
                    }
                };
                break;
            case 24:
                operator = new Operator(24) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.22
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        float n2 = ((Float) stack.pop()).floatValue();
                        stack.push(stack.get((int) ((stack.size() - 1) - n2)));
                    }
                };
                break;
            case 25:
                operator = new Operator(25) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.23
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        float num2 = ((Float) stack.pop()).floatValue();
                        float num1 = ((Float) stack.pop()).floatValue();
                        stack.push(Boolean.valueOf(num1 <= num2));
                    }
                };
                break;
            case 26:
                operator = new Operator(26) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.25
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        float num = ((Float) stack.pop()).floatValue();
                        stack.push(Float.valueOf(Double.valueOf(Math.log10(num)).floatValue()));
                    }
                };
                break;
            case 27:
                operator = new Operator(27) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.26
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        float num2 = ((Float) stack.pop()).floatValue();
                        float num1 = ((Float) stack.pop()).floatValue();
                        stack.push(Boolean.valueOf(num1 < num2));
                    }
                };
                break;
            case 28:
                operator = new Operator(28) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.27
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        float num2 = ((Float) stack.pop()).floatValue();
                        float num1 = ((Float) stack.pop()).floatValue();
                        stack.push(Float.valueOf(num1 % num2));
                    }
                };
                break;
            case 29:
                operator = new Operator(29) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.28
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        float num2 = ((Float) stack.pop()).floatValue();
                        float num1 = ((Float) stack.pop()).floatValue();
                        stack.push(Float.valueOf(num1 * num2));
                    }
                };
                break;
            case 30:
                operator = new Operator(30) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.29
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        float num2 = ((Float) stack.pop()).floatValue();
                        float num1 = ((Float) stack.pop()).floatValue();
                        stack.push(Boolean.valueOf(num1 != num2));
                    }
                };
                break;
            case 31:
                operator = new Operator(31) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.30
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        float num1 = ((Float) stack.pop()).floatValue();
                        stack.push(Float.valueOf(-num1));
                    }
                };
                break;
            case 32:
                operator = new Operator(32) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.31
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        boolean num1 = ((Boolean) stack.pop()).booleanValue();
                        stack.push(Boolean.valueOf(!num1));
                    }
                };
                break;
            case 33:
                operator = new Operator(33) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.32
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        boolean bool2 = ((Boolean) stack.pop()).booleanValue();
                        boolean bool1 = ((Boolean) stack.pop()).booleanValue();
                        stack.push(Boolean.valueOf(bool1 || bool2));
                    }
                };
                break;
            case 34:
                operator = new Operator(34) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.33
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        stack.pop();
                    }
                };
                break;
            case 35:
                operator = new Operator(35) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.34
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        float j2 = ((Float) stack.pop()).floatValue();
                        float n2 = ((Float) stack.pop()).floatValue();
                        if (j2 > 0.0f) {
                            for (int i2 = 0; i2 < j2; i2++) {
                                stack.insertElementAt(stack.lastElement(), (int) (stack.size() - n2));
                                stack.pop();
                            }
                            return;
                        }
                        if (j2 < 0.0f) {
                            int max = (int) (-j2);
                            for (int i3 = 0; i3 < max; i3++) {
                                stack.push(stack.remove((int) (stack.size() - n2)));
                            }
                        }
                    }
                };
                break;
            case 36:
                operator = new Operator(36) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.35
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        float num1 = ((Float) stack.pop()).floatValue();
                        stack.push(Float.valueOf(Integer.valueOf(Math.round(num1)).floatValue()));
                    }
                };
                break;
            case 37:
                operator = new Operator(37) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.36
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        float aAngle = ((Float) stack.pop()).floatValue();
                        stack.push(Float.valueOf(Double.valueOf(Math.sin(aAngle)).floatValue()));
                    }
                };
                break;
            case 38:
                operator = new Operator(38) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.37
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        float num = ((Float) stack.pop()).floatValue();
                        stack.push(Float.valueOf(Double.valueOf(Math.sqrt(num)).floatValue()));
                    }
                };
                break;
            case 39:
                operator = new Operator(39) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.38
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        float num2 = ((Float) stack.pop()).floatValue();
                        float num1 = ((Float) stack.pop()).floatValue();
                        stack.push(Float.valueOf(num1 - num2));
                    }
                };
                break;
            case 41:
                operator = new Operator(41) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.39
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        float num1 = ((Float) stack.pop()).floatValue();
                        stack.push(Float.valueOf(Double.valueOf(Math.floor(num1)).floatValue()));
                    }
                };
                break;
            case 42:
                operator = new Operator(42) { // from class: org.icepdf.core.pobjects.functions.postscript.OperatorFactory.40
                    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
                    public void eval(Stack stack) {
                        Object obj2 = stack.pop();
                        if (obj2 instanceof Number) {
                            float num2 = ((Float) obj2).floatValue();
                            float num1 = ((Float) stack.pop()).floatValue();
                            stack.push(Integer.valueOf(((int) num1) ^ ((int) num2)));
                        } else if (obj2 instanceof Boolean) {
                            boolean bool2 = ((Boolean) obj2).booleanValue();
                            boolean bool1 = ((Boolean) stack.pop()).booleanValue();
                            stack.push(Boolean.valueOf(bool1 ^ bool2));
                        }
                    }
                };
                break;
            case 43:
                operator = new Expression(43);
                break;
            case 44:
                operator = new Expression(44);
                break;
        }
        if (operator != null) {
            operatorCache.put(Integer.valueOf(operator.getType()), operator);
        }
        return operator;
    }
}
