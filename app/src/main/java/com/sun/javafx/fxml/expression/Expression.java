package com.sun.javafx.fxml.expression;

import com.sun.javafx.fxml.BeanAdapter;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/javafx/fxml/expression/Expression.class */
public abstract class Expression<T> {
    private static final String NULL_KEYWORD = "null";
    private static final String TRUE_KEYWORD = "true";
    private static final String FALSE_KEYWORD = "false";

    public abstract T evaluate(Object obj);

    public abstract void update(Object obj, T t2);

    public abstract boolean isDefined(Object obj);

    public abstract boolean isLValue();

    protected abstract void getArguments(List<KeyPath> list);

    /* loaded from: jfxrt.jar:com/sun/javafx/fxml/expression/Expression$Parser.class */
    private static class Parser {

        /* renamed from: c, reason: collision with root package name */
        private int f11892c;
        private char[] pushbackBuffer;
        private static final int PUSHBACK_BUFFER_SIZE = 6;

        /* loaded from: jfxrt.jar:com/sun/javafx/fxml/expression/Expression$Parser$TokenType.class */
        public enum TokenType {
            LITERAL,
            VARIABLE,
            FUNCTION,
            UNARY_OPERATOR,
            BINARY_OPERATOR,
            BEGIN_GROUP,
            END_GROUP
        }

        private Parser() {
            this.f11892c = -1;
            this.pushbackBuffer = new char[6];
        }

        /* loaded from: jfxrt.jar:com/sun/javafx/fxml/expression/Expression$Parser$Token.class */
        public static class Token {
            public final TokenType type;
            public final Object value;

            public Token(TokenType type, Object value) {
                this.type = type;
                this.value = value;
            }

            public String toString() {
                return this.value.toString();
            }
        }

        public Expression parse(Reader reader) throws IOException {
            Expression<?> expressionOr;
            LinkedList<Token> tokens = tokenize(new PushbackReader(reader, 6));
            LinkedList<Expression> stack = new LinkedList<>();
            Iterator<Token> it = tokens.iterator();
            while (it.hasNext()) {
                Token token = it.next();
                switch (token.type) {
                    case LITERAL:
                        expressionOr = new LiteralExpression<>(token.value);
                        break;
                    case VARIABLE:
                        expressionOr = new VariableExpression((KeyPath) token.value);
                        break;
                    case FUNCTION:
                        expressionOr = null;
                        break;
                    case UNARY_OPERATOR:
                        Operator operator = (Operator) token.value;
                        Expression operand = stack.pop();
                        switch (operator) {
                            case NEGATE:
                                expressionOr = Expression.negate(operand);
                                break;
                            case NOT:
                                expressionOr = Expression.not(operand);
                                break;
                            default:
                                throw new UnsupportedOperationException();
                        }
                    case BINARY_OPERATOR:
                        Operator operator2 = (Operator) token.value;
                        Expression right = stack.pop();
                        Expression left = stack.pop();
                        switch (operator2) {
                            case ADD:
                                expressionOr = Expression.add(left, right);
                                break;
                            case SUBTRACT:
                                expressionOr = Expression.subtract(left, right);
                                break;
                            case MULTIPLY:
                                expressionOr = Expression.multiply(left, right);
                                break;
                            case DIVIDE:
                                expressionOr = Expression.divide(left, right);
                                break;
                            case MODULO:
                                expressionOr = Expression.modulo(left, right);
                                break;
                            case GREATER_THAN:
                                expressionOr = Expression.greaterThan(left, right);
                                break;
                            case GREATER_THAN_OR_EQUAL_TO:
                                expressionOr = Expression.greaterThanOrEqualTo(left, right);
                                break;
                            case LESS_THAN:
                                expressionOr = Expression.lessThan(left, right);
                                break;
                            case LESS_THAN_OR_EQUAL_TO:
                                expressionOr = Expression.lessThanOrEqualTo(left, right);
                                break;
                            case EQUAL_TO:
                                expressionOr = Expression.equalTo(left, right);
                                break;
                            case NOT_EQUAL_TO:
                                expressionOr = Expression.notEqualTo(left, right);
                                break;
                            case AND:
                                expressionOr = Expression.and(left, right);
                                break;
                            case OR:
                                expressionOr = Expression.or(left, right);
                                break;
                            default:
                                throw new UnsupportedOperationException();
                        }
                    default:
                        throw new UnsupportedOperationException();
                }
                Expression<?> expression = expressionOr;
                stack.push(expression);
            }
            if (stack.size() != 1) {
                throw new IllegalArgumentException("Invalid expression.");
            }
            return stack.peek();
        }

        private LinkedList<Token> tokenize(PushbackReader reader) throws IOException {
            Token token;
            Number value;
            LinkedList<Token> linkedList = new LinkedList<>();
            LinkedList<Token> stack = new LinkedList<>();
            this.f11892c = reader.read();
            boolean unary = true;
            while (this.f11892c != -1) {
                while (this.f11892c != -1 && Character.isWhitespace(this.f11892c)) {
                    this.f11892c = reader.read();
                }
                if (this.f11892c != -1) {
                    if (this.f11892c == 110) {
                        if (readKeyword(reader, "null")) {
                            token = new Token(TokenType.LITERAL, null);
                        } else {
                            token = new Token(TokenType.VARIABLE, KeyPath.parse(reader));
                            this.f11892c = reader.read();
                        }
                    } else if (this.f11892c == 34 || this.f11892c == 39) {
                        StringBuilder stringBuilder = new StringBuilder();
                        int t2 = this.f11892c;
                        this.f11892c = reader.read();
                        while (this.f11892c != -1 && this.f11892c != t2) {
                            if (!Character.isISOControl(this.f11892c)) {
                                if (this.f11892c == 92) {
                                    this.f11892c = reader.read();
                                    if (this.f11892c == 98) {
                                        this.f11892c = 8;
                                    } else if (this.f11892c == 102) {
                                        this.f11892c = 12;
                                    } else if (this.f11892c == 110) {
                                        this.f11892c = 10;
                                    } else if (this.f11892c == 114) {
                                        this.f11892c = 13;
                                    } else if (this.f11892c == 116) {
                                        this.f11892c = 9;
                                    } else if (this.f11892c == 117) {
                                        StringBuilder unicodeValueBuilder = new StringBuilder();
                                        while (unicodeValueBuilder.length() < 4) {
                                            this.f11892c = reader.read();
                                            unicodeValueBuilder.append((char) this.f11892c);
                                        }
                                        String unicodeValue = unicodeValueBuilder.toString();
                                        this.f11892c = (char) Integer.parseInt(unicodeValue, 16);
                                    } else if (this.f11892c != 92 && this.f11892c != 47 && this.f11892c != 34 && this.f11892c != 39 && this.f11892c != t2) {
                                        throw new IllegalArgumentException("Unsupported escape sequence.");
                                    }
                                }
                                stringBuilder.append((char) this.f11892c);
                            }
                            this.f11892c = reader.read();
                        }
                        if (this.f11892c != t2) {
                            throw new IllegalArgumentException("Unterminated string.");
                        }
                        this.f11892c = reader.read();
                        token = new Token(TokenType.LITERAL, stringBuilder.toString());
                    } else if (Character.isDigit(this.f11892c)) {
                        StringBuilder numberBuilder = new StringBuilder();
                        boolean integer = true;
                        while (this.f11892c != -1 && (Character.isDigit(this.f11892c) || this.f11892c == 46 || this.f11892c == 101 || this.f11892c == 69)) {
                            numberBuilder.append((char) this.f11892c);
                            integer &= this.f11892c != 46;
                            this.f11892c = reader.read();
                        }
                        if (integer) {
                            value = Long.valueOf(Long.parseLong(numberBuilder.toString()));
                        } else {
                            value = Double.valueOf(Double.parseDouble(numberBuilder.toString()));
                        }
                        token = new Token(TokenType.LITERAL, value);
                    } else if (this.f11892c == 116) {
                        if (readKeyword(reader, "true")) {
                            token = new Token(TokenType.LITERAL, true);
                        } else {
                            token = new Token(TokenType.VARIABLE, KeyPath.parse(reader));
                            this.f11892c = reader.read();
                        }
                    } else if (this.f11892c == 102) {
                        if (readKeyword(reader, "false")) {
                            token = new Token(TokenType.LITERAL, false);
                        } else {
                            token = new Token(TokenType.VARIABLE, KeyPath.parse(reader));
                            this.f11892c = reader.read();
                        }
                    } else if (Character.isJavaIdentifierStart(this.f11892c)) {
                        reader.unread(this.f11892c);
                        token = new Token(TokenType.VARIABLE, KeyPath.parse(reader));
                        this.f11892c = reader.read();
                    } else {
                        boolean readNext = true;
                        if (unary) {
                            switch (this.f11892c) {
                                case 33:
                                    token = new Token(TokenType.UNARY_OPERATOR, Operator.NOT);
                                    break;
                                case 40:
                                    token = new Token(TokenType.BEGIN_GROUP, null);
                                    break;
                                case 45:
                                    token = new Token(TokenType.UNARY_OPERATOR, Operator.NEGATE);
                                    break;
                                default:
                                    throw new IllegalArgumentException("Unexpected character in expression.");
                            }
                        } else {
                            switch (this.f11892c) {
                                case 33:
                                    this.f11892c = reader.read();
                                    if (this.f11892c == 61) {
                                        token = new Token(TokenType.BINARY_OPERATOR, Operator.NOT_EQUAL_TO);
                                        break;
                                    } else {
                                        throw new IllegalArgumentException("Unexpected character in expression.");
                                    }
                                case 37:
                                    token = new Token(TokenType.BINARY_OPERATOR, Operator.MODULO);
                                    break;
                                case 38:
                                    this.f11892c = reader.read();
                                    if (this.f11892c == 38) {
                                        token = new Token(TokenType.BINARY_OPERATOR, Operator.AND);
                                        break;
                                    } else {
                                        throw new IllegalArgumentException("Unexpected character in expression.");
                                    }
                                case 41:
                                    token = new Token(TokenType.END_GROUP, null);
                                    break;
                                case 42:
                                    token = new Token(TokenType.BINARY_OPERATOR, Operator.MULTIPLY);
                                    break;
                                case 43:
                                    token = new Token(TokenType.BINARY_OPERATOR, Operator.ADD);
                                    break;
                                case 45:
                                    token = new Token(TokenType.BINARY_OPERATOR, Operator.SUBTRACT);
                                    break;
                                case 47:
                                    token = new Token(TokenType.BINARY_OPERATOR, Operator.DIVIDE);
                                    break;
                                case 60:
                                    this.f11892c = reader.read();
                                    if (this.f11892c == 61) {
                                        token = new Token(TokenType.BINARY_OPERATOR, Operator.LESS_THAN_OR_EQUAL_TO);
                                        break;
                                    } else {
                                        readNext = false;
                                        token = new Token(TokenType.BINARY_OPERATOR, Operator.LESS_THAN);
                                        break;
                                    }
                                case 61:
                                    this.f11892c = reader.read();
                                    if (this.f11892c == 61) {
                                        token = new Token(TokenType.BINARY_OPERATOR, Operator.EQUAL_TO);
                                        break;
                                    } else {
                                        throw new IllegalArgumentException("Unexpected character in expression.");
                                    }
                                case 62:
                                    this.f11892c = reader.read();
                                    if (this.f11892c == 61) {
                                        token = new Token(TokenType.BINARY_OPERATOR, Operator.GREATER_THAN_OR_EQUAL_TO);
                                        break;
                                    } else {
                                        readNext = false;
                                        token = new Token(TokenType.BINARY_OPERATOR, Operator.GREATER_THAN);
                                        break;
                                    }
                                case 124:
                                    this.f11892c = reader.read();
                                    if (this.f11892c == 124) {
                                        token = new Token(TokenType.BINARY_OPERATOR, Operator.OR);
                                        break;
                                    } else {
                                        throw new IllegalArgumentException("Unexpected character in expression.");
                                    }
                                default:
                                    throw new IllegalArgumentException("Unexpected character in expression.");
                            }
                        }
                        if (readNext) {
                            this.f11892c = reader.read();
                        }
                    }
                    switch (token.type) {
                        case LITERAL:
                        case VARIABLE:
                            linkedList.add(token);
                            break;
                        case FUNCTION:
                        default:
                            throw new UnsupportedOperationException();
                        case UNARY_OPERATOR:
                        case BINARY_OPERATOR:
                            int priority = ((Operator) token.value).getPriority();
                            while (!stack.isEmpty() && stack.peek().type != TokenType.BEGIN_GROUP && ((Operator) stack.peek().value).getPriority() >= priority && ((Operator) stack.peek().value).getPriority() != 6) {
                                linkedList.add(stack.pop());
                            }
                            stack.push(token);
                            break;
                        case BEGIN_GROUP:
                            stack.push(token);
                            break;
                        case END_GROUP:
                            Token tokenPop = stack.pop();
                            while (true) {
                                Token t3 = tokenPop;
                                if (t3.type == TokenType.BEGIN_GROUP) {
                                    break;
                                } else {
                                    linkedList.add(t3);
                                    tokenPop = stack.pop();
                                }
                            }
                    }
                    unary = (token.type == TokenType.LITERAL || token.type == TokenType.VARIABLE || token.type == TokenType.END_GROUP) ? false : true;
                }
            }
            while (!stack.isEmpty()) {
                linkedList.add(stack.pop());
            }
            return linkedList;
        }

        private boolean readKeyword(PushbackReader reader, String keyword) throws IOException {
            boolean result;
            int n2 = keyword.length();
            int i2 = 0;
            while (this.f11892c != -1 && i2 < n2) {
                this.pushbackBuffer[i2] = (char) this.f11892c;
                if (keyword.charAt(i2) != this.f11892c) {
                    break;
                }
                this.f11892c = reader.read();
                i2++;
            }
            if (i2 < n2) {
                reader.unread(this.pushbackBuffer, 0, i2 + 1);
                result = false;
            } else {
                result = true;
            }
            return result;
        }
    }

    public List<KeyPath> getArguments() {
        ArrayList<KeyPath> arguments = new ArrayList<>();
        getArguments(arguments);
        return arguments;
    }

    public static <T> T get(Object obj, KeyPath keyPath) {
        if (keyPath == null) {
            throw new NullPointerException();
        }
        return (T) get(obj, keyPath.iterator());
    }

    private static <T> T get(Object obj, Iterator<String> it) {
        Object obj2;
        if (it == null) {
            throw new NullPointerException();
        }
        if (it.hasNext()) {
            obj2 = get(get(obj, it.next()), it);
        } else {
            obj2 = obj;
        }
        return (T) obj2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v11, types: [java.util.Map] */
    public static <T> T get(Object obj, String str) {
        Object obj2;
        BeanAdapter beanAdapter;
        if (str == null) {
            throw new NullPointerException();
        }
        if (obj instanceof List) {
            obj2 = ((List) obj).get(Integer.parseInt(str));
        } else if (obj != null) {
            if (obj instanceof Map) {
                beanAdapter = (Map) obj;
            } else {
                beanAdapter = new BeanAdapter(obj);
            }
            obj2 = beanAdapter.get((Object) str);
        } else {
            obj2 = null;
        }
        return (T) obj2;
    }

    public static void set(Object namespace, KeyPath keyPath, Object value) {
        if (keyPath == null) {
            throw new NullPointerException();
        }
        set(namespace, keyPath.iterator(), value);
    }

    private static void set(Object namespace, Iterator<String> keyPathIterator, Object value) {
        if (keyPathIterator == null) {
            throw new NullPointerException();
        }
        if (!keyPathIterator.hasNext()) {
            throw new IllegalArgumentException();
        }
        String key = keyPathIterator.next();
        if (keyPathIterator.hasNext()) {
            set(get(namespace, key), keyPathIterator, value);
        } else {
            set(namespace, key, value);
        }
    }

    public static void set(Object namespace, String key, Object value) {
        Map<String, Object> map;
        if (key == null) {
            throw new NullPointerException();
        }
        if (namespace instanceof List) {
            List<Object> list = (List) namespace;
            list.set(Integer.parseInt(key), value);
        } else {
            if (namespace != null) {
                if (namespace instanceof Map) {
                    map = (Map) namespace;
                } else {
                    map = new BeanAdapter(namespace);
                }
                map.put(key, value);
                return;
            }
            throw new IllegalArgumentException();
        }
    }

    public static boolean isDefined(Object namespace, KeyPath keyPath) {
        if (keyPath == null) {
            throw new NullPointerException();
        }
        return isDefined(namespace, keyPath.iterator());
    }

    private static boolean isDefined(Object namespace, Iterator<String> keyPathIterator) {
        boolean defined;
        if (keyPathIterator == null) {
            throw new NullPointerException();
        }
        if (!keyPathIterator.hasNext()) {
            throw new IllegalArgumentException();
        }
        String key = keyPathIterator.next();
        if (keyPathIterator.hasNext()) {
            defined = isDefined(get(namespace, key), keyPathIterator);
        } else {
            defined = isDefined(namespace, key);
        }
        return defined;
    }

    public static boolean isDefined(Object namespace, String key) {
        boolean defined;
        Map<String, Object> map;
        if (key == null) {
            throw new NullPointerException();
        }
        if (namespace instanceof List) {
            List<Object> list = (List) namespace;
            defined = Integer.parseInt(key) < list.size();
        } else if (namespace != null) {
            if (namespace instanceof Map) {
                map = (Map) namespace;
            } else {
                map = new BeanAdapter(namespace);
            }
            defined = map.containsKey(key);
        } else {
            defined = false;
        }
        return defined;
    }

    public static BinaryExpression add(Expression left, Expression right) {
        return new BinaryExpression(left, right, (leftValue, rightValue) -> {
            Object value;
            if ((leftValue instanceof String) || (rightValue instanceof String)) {
                value = leftValue.toString().concat(rightValue.toString());
            } else {
                Number leftNumber = (Number) leftValue;
                Number rightNumber = (Number) rightValue;
                if ((leftNumber instanceof Double) || (rightNumber instanceof Double)) {
                    value = Double.valueOf(leftNumber.doubleValue() + rightNumber.doubleValue());
                } else if ((leftNumber instanceof Float) || (rightNumber instanceof Float)) {
                    value = Float.valueOf(leftNumber.floatValue() + rightNumber.floatValue());
                } else if ((leftNumber instanceof Long) || (rightNumber instanceof Long)) {
                    value = Long.valueOf(leftNumber.longValue() + rightNumber.longValue());
                } else if ((leftNumber instanceof Integer) || (rightNumber instanceof Integer)) {
                    value = Integer.valueOf(leftNumber.intValue() + rightNumber.intValue());
                } else if ((leftNumber instanceof Short) || (rightNumber instanceof Short)) {
                    value = Integer.valueOf(leftNumber.shortValue() + rightNumber.shortValue());
                } else if ((leftNumber instanceof Byte) || (rightNumber instanceof Byte)) {
                    value = Integer.valueOf(leftNumber.byteValue() + rightNumber.byteValue());
                } else {
                    throw new UnsupportedOperationException();
                }
            }
            return value;
        });
    }

    public static BinaryExpression add(Expression left, Object right) {
        return add(left, (Expression) new LiteralExpression(right));
    }

    public static BinaryExpression add(Object left, Expression right) {
        return add((Expression) new LiteralExpression(left), right);
    }

    public static BinaryExpression add(Object left, Object right) {
        return add((Expression) new LiteralExpression(left), (Expression) new LiteralExpression(right));
    }

    public static BinaryExpression subtract(Expression left, Expression right) {
        return new BinaryExpression(left, right, (leftValue, rightValue) -> {
            Number value;
            if ((leftValue instanceof Double) || (rightValue instanceof Double)) {
                value = Double.valueOf(leftValue.doubleValue() - rightValue.doubleValue());
            } else if ((leftValue instanceof Float) || (rightValue instanceof Float)) {
                value = Float.valueOf(leftValue.floatValue() - rightValue.floatValue());
            } else if ((leftValue instanceof Long) || (rightValue instanceof Long)) {
                value = Long.valueOf(leftValue.longValue() - rightValue.longValue());
            } else if ((leftValue instanceof Integer) || (rightValue instanceof Integer)) {
                value = Integer.valueOf(leftValue.intValue() - rightValue.intValue());
            } else if ((leftValue instanceof Short) || (rightValue instanceof Short)) {
                value = Integer.valueOf(leftValue.shortValue() - rightValue.shortValue());
            } else if ((leftValue instanceof Byte) || (rightValue instanceof Byte)) {
                value = Integer.valueOf(leftValue.byteValue() - rightValue.byteValue());
            } else {
                throw new UnsupportedOperationException();
            }
            return value;
        });
    }

    public static BinaryExpression subtract(Expression left, Number right) {
        return subtract(left, new LiteralExpression(right));
    }

    public static BinaryExpression subtract(Number left, Expression right) {
        return subtract(new LiteralExpression(left), right);
    }

    public static BinaryExpression subtract(Number left, Number right) {
        return subtract(new LiteralExpression(left), new LiteralExpression(right));
    }

    public static BinaryExpression multiply(Expression left, Expression right) {
        return new BinaryExpression(left, right, (leftValue, rightValue) -> {
            Number value;
            if ((leftValue instanceof Double) || (rightValue instanceof Double)) {
                value = Double.valueOf(leftValue.doubleValue() * rightValue.doubleValue());
            } else if ((leftValue instanceof Float) || (rightValue instanceof Float)) {
                value = Float.valueOf(leftValue.floatValue() * rightValue.floatValue());
            } else if ((leftValue instanceof Long) || (rightValue instanceof Long)) {
                value = Long.valueOf(leftValue.longValue() * rightValue.longValue());
            } else if ((leftValue instanceof Integer) || (rightValue instanceof Integer)) {
                value = Integer.valueOf(leftValue.intValue() * rightValue.intValue());
            } else if ((leftValue instanceof Short) || (rightValue instanceof Short)) {
                value = Integer.valueOf(leftValue.shortValue() * rightValue.shortValue());
            } else if ((leftValue instanceof Byte) || (rightValue instanceof Byte)) {
                value = Integer.valueOf(leftValue.byteValue() * rightValue.byteValue());
            } else {
                throw new UnsupportedOperationException();
            }
            return value;
        });
    }

    public static BinaryExpression multiply(Expression left, Number right) {
        return multiply(left, new LiteralExpression(right));
    }

    public static BinaryExpression multiply(Number left, Expression right) {
        return multiply(new LiteralExpression(left), right);
    }

    public static BinaryExpression multiply(Number left, Number right) {
        return multiply(new LiteralExpression(left), new LiteralExpression(right));
    }

    public static BinaryExpression divide(Expression left, Expression right) {
        return new BinaryExpression(left, right, (leftValue, rightValue) -> {
            Number value;
            if ((leftValue instanceof Double) || (rightValue instanceof Double)) {
                value = Double.valueOf(leftValue.doubleValue() / rightValue.doubleValue());
            } else if ((leftValue instanceof Float) || (rightValue instanceof Float)) {
                value = Float.valueOf(leftValue.floatValue() / rightValue.floatValue());
            } else if ((leftValue instanceof Long) || (rightValue instanceof Long)) {
                value = Long.valueOf(leftValue.longValue() / rightValue.longValue());
            } else if ((leftValue instanceof Integer) || (rightValue instanceof Integer)) {
                value = Integer.valueOf(leftValue.intValue() / rightValue.intValue());
            } else if ((leftValue instanceof Short) || (rightValue instanceof Short)) {
                value = Integer.valueOf(leftValue.shortValue() / rightValue.shortValue());
            } else if ((leftValue instanceof Byte) || (rightValue instanceof Byte)) {
                value = Integer.valueOf(leftValue.byteValue() / rightValue.byteValue());
            } else {
                throw new UnsupportedOperationException();
            }
            return value;
        });
    }

    public static BinaryExpression divide(Expression left, Number right) {
        return divide(left, new LiteralExpression(right));
    }

    public static BinaryExpression divide(Number left, Expression<Number> right) {
        return divide(new LiteralExpression(left), right);
    }

    public static BinaryExpression divide(Number left, Number right) {
        return divide(new LiteralExpression(left), new LiteralExpression(right));
    }

    public static BinaryExpression modulo(Expression left, Expression right) {
        return new BinaryExpression(left, right, (leftValue, rightValue) -> {
            Number value;
            if ((leftValue instanceof Double) || (rightValue instanceof Double)) {
                value = Double.valueOf(leftValue.doubleValue() % rightValue.doubleValue());
            } else if ((leftValue instanceof Float) || (rightValue instanceof Float)) {
                value = Float.valueOf(leftValue.floatValue() % rightValue.floatValue());
            } else if ((leftValue instanceof Long) || (rightValue instanceof Long)) {
                value = Long.valueOf(leftValue.longValue() % rightValue.longValue());
            } else if ((leftValue instanceof Integer) || (rightValue instanceof Integer)) {
                value = Integer.valueOf(leftValue.intValue() % rightValue.intValue());
            } else if ((leftValue instanceof Short) || (rightValue instanceof Short)) {
                value = Integer.valueOf(leftValue.shortValue() % rightValue.shortValue());
            } else if ((leftValue instanceof Byte) || (rightValue instanceof Byte)) {
                value = Integer.valueOf(leftValue.byteValue() % rightValue.byteValue());
            } else {
                throw new UnsupportedOperationException();
            }
            return value;
        });
    }

    public static BinaryExpression modulo(Expression<Number> left, Number right) {
        return modulo(left, new LiteralExpression(right));
    }

    public static BinaryExpression modulo(Number left, Expression<Number> right) {
        return modulo(new LiteralExpression(left), right);
    }

    public static BinaryExpression modulo(Number left, Number right) {
        return modulo(new LiteralExpression(left), new LiteralExpression(right));
    }

    public static BinaryExpression equalTo(Expression left, Expression right) {
        return new BinaryExpression(left, right, (leftValue, rightValue) -> {
            return Boolean.valueOf(leftValue.compareTo(rightValue) == 0);
        });
    }

    public static BinaryExpression equalTo(Expression left, Object right) {
        return equalTo(left, (Expression) new LiteralExpression(right));
    }

    public static BinaryExpression equalTo(Object left, Expression right) {
        return equalTo((Expression) new LiteralExpression(left), right);
    }

    public static BinaryExpression equalTo(Object left, Object right) {
        return equalTo((Expression) new LiteralExpression(left), (Expression) new LiteralExpression(right));
    }

    public static BinaryExpression notEqualTo(Expression left, Expression right) {
        return new BinaryExpression(left, right, (leftValue, rightValue) -> {
            return Boolean.valueOf(leftValue.compareTo(rightValue) != 0);
        });
    }

    public static BinaryExpression notEqualTo(Expression left, Object right) {
        return notEqualTo(left, (Expression) new LiteralExpression(right));
    }

    public static BinaryExpression notEqualTo(Object left, Expression right) {
        return notEqualTo((Expression) new LiteralExpression(left), right);
    }

    public static BinaryExpression notEqualTo(Object left, Object right) {
        return notEqualTo((Expression) new LiteralExpression(left), (Expression) new LiteralExpression(right));
    }

    public static BinaryExpression greaterThan(Expression left, Expression right) {
        return new BinaryExpression(left, right, (leftValue, rightValue) -> {
            return Boolean.valueOf(leftValue.compareTo(rightValue) > 0);
        });
    }

    public static BinaryExpression greaterThan(Expression left, Object right) {
        return greaterThan(left, (Expression) new LiteralExpression(right));
    }

    public static BinaryExpression greaterThan(Object left, Expression right) {
        return greaterThan((Expression) new LiteralExpression(left), right);
    }

    public static BinaryExpression greaterThan(Object left, Object right) {
        return greaterThan((Expression) new LiteralExpression(left), (Expression) new LiteralExpression(right));
    }

    public static BinaryExpression greaterThanOrEqualTo(Expression left, Expression right) {
        return new BinaryExpression(left, right, (leftValue, rightValue) -> {
            return Boolean.valueOf(leftValue.compareTo(rightValue) >= 0);
        });
    }

    public static BinaryExpression greaterThanOrEqualTo(Expression left, Object right) {
        return greaterThanOrEqualTo(left, (Expression) new LiteralExpression(right));
    }

    public static BinaryExpression greaterThanOrEqualTo(Object left, Expression right) {
        return greaterThanOrEqualTo((Expression) new LiteralExpression(left), right);
    }

    public static BinaryExpression greaterThanOrEqualTo(Object left, Object right) {
        return greaterThanOrEqualTo((Expression) new LiteralExpression(left), (Expression) new LiteralExpression(right));
    }

    public static BinaryExpression lessThan(Expression left, Expression right) {
        return new BinaryExpression(left, right, (leftValue, rightValue) -> {
            return Boolean.valueOf(leftValue.compareTo(rightValue) < 0);
        });
    }

    public static BinaryExpression lessThan(Expression left, Object right) {
        return lessThan(left, (Expression) new LiteralExpression(right));
    }

    public static BinaryExpression lessThan(Object left, Expression right) {
        return lessThan((Expression) new LiteralExpression(left), right);
    }

    public static BinaryExpression lessThan(Object left, Object right) {
        return lessThan((Expression) new LiteralExpression(left), (Expression) new LiteralExpression(right));
    }

    public static BinaryExpression lessThanOrEqualTo(Expression left, Expression right) {
        return new BinaryExpression(left, right, (leftValue, rightValue) -> {
            return Boolean.valueOf(leftValue.compareTo(rightValue) <= 0);
        });
    }

    public static BinaryExpression lessThanOrEqualTo(Expression left, Object right) {
        return lessThanOrEqualTo(left, (Expression) new LiteralExpression(right));
    }

    public static BinaryExpression lessThanOrEqualTo(Object left, Expression right) {
        return lessThanOrEqualTo((Expression) new LiteralExpression(left), right);
    }

    public static BinaryExpression lessThanOrEqualTo(Object left, Object right) {
        return lessThanOrEqualTo((Expression) new LiteralExpression(left), (Expression) new LiteralExpression(right));
    }

    public static BinaryExpression and(Expression left, Expression right) {
        return new BinaryExpression(left, right, (leftValue, rightValue) -> {
            return Boolean.valueOf(leftValue.booleanValue() && rightValue.booleanValue());
        });
    }

    public static BinaryExpression and(Expression left, Boolean right) {
        return and(left, new LiteralExpression(right));
    }

    public static BinaryExpression and(Boolean left, Expression right) {
        return and(new LiteralExpression(left), right);
    }

    public static BinaryExpression and(Boolean left, Boolean right) {
        return and(new LiteralExpression(left), new LiteralExpression(right));
    }

    public static BinaryExpression or(Expression left, Expression right) {
        return new BinaryExpression(left, right, (leftValue, rightValue) -> {
            return Boolean.valueOf(leftValue.booleanValue() || rightValue.booleanValue());
        });
    }

    public static BinaryExpression or(Expression left, Boolean right) {
        return or(left, new LiteralExpression(right));
    }

    public static BinaryExpression or(Boolean left, Expression right) {
        return or(new LiteralExpression(left), right);
    }

    public static BinaryExpression or(Boolean left, Boolean right) {
        return or(new LiteralExpression(left), new LiteralExpression(right));
    }

    public static UnaryExpression negate(Expression operand) {
        return new UnaryExpression(operand, value -> {
            Class<?> cls = value.getClass();
            if (cls == Byte.class) {
                return Integer.valueOf(-value.byteValue());
            }
            if (cls == Short.class) {
                return Integer.valueOf(-value.shortValue());
            }
            if (cls == Integer.class) {
                return Integer.valueOf(-value.intValue());
            }
            if (cls == Long.class) {
                return Long.valueOf(-value.longValue());
            }
            if (cls == Float.class) {
                return Float.valueOf(-value.floatValue());
            }
            if (cls == Double.class) {
                return Double.valueOf(-value.doubleValue());
            }
            throw new UnsupportedOperationException();
        });
    }

    public static UnaryExpression negate(Number operand) {
        return negate(new LiteralExpression(operand));
    }

    public static UnaryExpression not(Expression operand) {
        return new UnaryExpression(operand, value -> {
            return Boolean.valueOf(!value.booleanValue());
        });
    }

    public static UnaryExpression not(Boolean operand) {
        return not(new LiteralExpression(operand));
    }

    public static Expression valueOf(String value) {
        if (value == null) {
            throw new NullPointerException();
        }
        Parser parser = new Parser();
        try {
            Expression expression = parser.parse(new StringReader(value));
            return expression;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
