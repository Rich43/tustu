package com.sun.xml.internal.ws.policy.privateutil;

import com.sun.xml.internal.ws.policy.PolicyException;
import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/privateutil/PolicyUtils.class */
public final class PolicyUtils {
    private PolicyUtils() {
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/privateutil/PolicyUtils$Commons.class */
    public static class Commons {
        public static String getStackMethodName(int methodIndexInStack) {
            String methodName;
            StackTraceElement[] stack = Thread.currentThread().getStackTrace();
            if (stack.length > methodIndexInStack + 1) {
                methodName = stack[methodIndexInStack].getMethodName();
            } else {
                methodName = "UNKNOWN METHOD";
            }
            return methodName;
        }

        public static String getCallerMethodName() {
            String result = getStackMethodName(5);
            if (result.equals("invoke0")) {
                result = getStackMethodName(4);
            }
            return result;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/privateutil/PolicyUtils$IO.class */
    public static class IO {
        private static final PolicyLogger LOGGER = PolicyLogger.getLogger((Class<?>) IO.class);

        public static void closeResource(Closeable resource) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (IOException e2) {
                    LOGGER.warning(LocalizationMessages.WSP_0023_UNEXPECTED_ERROR_WHILE_CLOSING_RESOURCE(resource.toString()), e2);
                }
            }
        }

        public static void closeResource(XMLStreamReader reader) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (XMLStreamException e2) {
                    LOGGER.warning(LocalizationMessages.WSP_0023_UNEXPECTED_ERROR_WHILE_CLOSING_RESOURCE(reader.toString()), e2);
                }
            }
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/privateutil/PolicyUtils$Text.class */
    public static class Text {
        public static final String NEW_LINE = System.getProperty("line.separator");

        public static String createIndent(int indentLevel) {
            char[] charData = new char[indentLevel * 4];
            Arrays.fill(charData, ' ');
            return String.valueOf(charData);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/privateutil/PolicyUtils$Comparison.class */
    public static class Comparison {
        public static final Comparator<QName> QNAME_COMPARATOR = new Comparator<QName>() { // from class: com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Comparison.1
            @Override // java.util.Comparator
            public int compare(QName qn1, QName qn2) {
                if (qn1 == qn2 || qn1.equals(qn2)) {
                    return 0;
                }
                int result = qn1.getNamespaceURI().compareTo(qn2.getNamespaceURI());
                if (result != 0) {
                    return result;
                }
                return qn1.getLocalPart().compareTo(qn2.getLocalPart());
            }
        };

        public static int compareBoolean(boolean b1, boolean b2) {
            int i1 = b1 ? 1 : 0;
            int i2 = b2 ? 1 : 0;
            return i1 - i2;
        }

        public static int compareNullableStrings(String s1, String s2) {
            if (s1 == null) {
                return s2 == null ? 0 : -1;
            }
            if (s2 == null) {
                return 1;
            }
            return s1.compareTo(s2);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/privateutil/PolicyUtils$Collections.class */
    public static class Collections {
        public static <E, T extends Collection<? extends E>, U extends Collection<? extends E>> Collection<Collection<E>> combine(U initialBase, Collection<T> options, boolean ignoreEmptyOption) {
            List<Collection<E>> combinations = null;
            if (options == null || options.isEmpty()) {
                if (initialBase != null) {
                    combinations = new ArrayList<>(1);
                    combinations.add(new ArrayList<>(initialBase));
                }
                return combinations;
            }
            Collection<E> linkedList = new LinkedList<>();
            if (initialBase != null && !initialBase.isEmpty()) {
                linkedList.addAll(initialBase);
            }
            int finalCombinationsSize = 1;
            Queue<T> optionProcessingQueue = new LinkedList<>();
            for (T option : options) {
                int optionSize = option.size();
                if (optionSize == 0) {
                    if (!ignoreEmptyOption) {
                        return null;
                    }
                } else if (optionSize == 1) {
                    linkedList.addAll(option);
                } else {
                    optionProcessingQueue.offer(option);
                    finalCombinationsSize *= optionSize;
                }
            }
            List<Collection<E>> combinations2 = new ArrayList<>(finalCombinationsSize);
            combinations2.add(linkedList);
            if (finalCombinationsSize > 1) {
                while (true) {
                    Collection collection = (Collection) optionProcessingQueue.poll();
                    if (collection == null) {
                        break;
                    }
                    int actualSemiCombinationCollectionSize = combinations2.size();
                    int newSemiCombinationCollectionSize = actualSemiCombinationCollectionSize * collection.size();
                    int semiCombinationIndex = 0;
                    for (E optionElement : collection) {
                        for (int i2 = 0; i2 < actualSemiCombinationCollectionSize; i2++) {
                            Collection<E> semiCombination = combinations2.get(semiCombinationIndex);
                            if (semiCombinationIndex + actualSemiCombinationCollectionSize < newSemiCombinationCollectionSize) {
                                combinations2.add(new LinkedList<>(semiCombination));
                            }
                            semiCombination.add(optionElement);
                            semiCombinationIndex++;
                        }
                    }
                }
            }
            return combinations2;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/privateutil/PolicyUtils$Reflection.class */
    static class Reflection {
        private static final PolicyLogger LOGGER = PolicyLogger.getLogger((Class<?>) Reflection.class);

        Reflection() {
        }

        static <T> T invoke(Object obj, String str, Class<T> cls, Object... objArr) throws RuntimePolicyUtilsException {
            Class[] clsArr;
            if (objArr != null && objArr.length > 0) {
                clsArr = new Class[objArr.length];
                int i2 = 0;
                for (Object obj2 : objArr) {
                    int i3 = i2;
                    i2++;
                    clsArr[i3] = obj2.getClass();
                }
            } else {
                clsArr = null;
            }
            return (T) invoke(obj, str, cls, objArr, clsArr);
        }

        public static <T> T invoke(Object target, String methodName, Class<T> resultClass, Object[] parameters, Class[] parameterTypes) throws RuntimePolicyUtilsException {
            try {
                Method method = target.getClass().getMethod(methodName, parameterTypes);
                Object result = MethodUtil.invoke(target, method, parameters);
                return resultClass.cast(result);
            } catch (IllegalAccessException e2) {
                throw ((RuntimePolicyUtilsException) LOGGER.logSevereException(new RuntimePolicyUtilsException(createExceptionMessage(target, parameters, methodName), e2.getCause())));
            } catch (IllegalArgumentException e3) {
                throw ((RuntimePolicyUtilsException) LOGGER.logSevereException(new RuntimePolicyUtilsException(createExceptionMessage(target, parameters, methodName), e3)));
            } catch (NoSuchMethodException e4) {
                throw ((RuntimePolicyUtilsException) LOGGER.logSevereException(new RuntimePolicyUtilsException(createExceptionMessage(target, parameters, methodName), e4)));
            } catch (SecurityException e5) {
                throw ((RuntimePolicyUtilsException) LOGGER.logSevereException(new RuntimePolicyUtilsException(createExceptionMessage(target, parameters, methodName), e5)));
            } catch (InvocationTargetException e6) {
                throw ((RuntimePolicyUtilsException) LOGGER.logSevereException(new RuntimePolicyUtilsException(createExceptionMessage(target, parameters, methodName), e6)));
            }
        }

        private static String createExceptionMessage(Object target, Object[] parameters, String methodName) {
            return LocalizationMessages.WSP_0061_METHOD_INVOCATION_FAILED(target.getClass().getName(), methodName, parameters == null ? null : Arrays.asList(parameters).toString());
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/privateutil/PolicyUtils$ConfigFile.class */
    public static class ConfigFile {
        public static String generateFullName(String configFileIdentifier) throws PolicyException {
            if (configFileIdentifier != null) {
                StringBuffer buffer = new StringBuffer("wsit-");
                buffer.append(configFileIdentifier).append(".xml");
                return buffer.toString();
            }
            throw new PolicyException(LocalizationMessages.WSP_0080_IMPLEMENTATION_EXPECTED_NOT_NULL());
        }

        public static URL loadFromContext(String configFileName, Object context) {
            return (URL) Reflection.invoke(context, "getResource", URL.class, configFileName);
        }

        public static URL loadFromClasspath(String configFileName) {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl == null) {
                return ClassLoader.getSystemResource(configFileName);
            }
            return cl.getResource(configFileName);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/privateutil/PolicyUtils$ServiceProvider.class */
    public static class ServiceProvider {
        public static <T> T[] load(Class<T> cls, ClassLoader classLoader) {
            return (T[]) ServiceFinder.find(cls, classLoader).toArray();
        }

        public static <T> T[] load(Class<T> cls) {
            return (T[]) ServiceFinder.find(cls).toArray();
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/privateutil/PolicyUtils$Rfc2396.class */
    public static class Rfc2396 {
        private static final PolicyLogger LOGGER = PolicyLogger.getLogger((Class<?>) Reflection.class);

        public static String unquote(String quoted) {
            if (null == quoted) {
                return null;
            }
            byte[] unquoted = new byte[quoted.length()];
            int newLength = 0;
            int i2 = 0;
            while (i2 < quoted.length()) {
                char c2 = quoted.charAt(i2);
                if ('%' == c2) {
                    if (i2 + 2 >= quoted.length()) {
                        throw ((RuntimePolicyUtilsException) LOGGER.logSevereException((PolicyLogger) new RuntimePolicyUtilsException(LocalizationMessages.WSP_0079_ERROR_WHILE_RFC_2396_UNESCAPING(quoted)), false));
                    }
                    int i3 = i2 + 1;
                    int hi = Character.digit(quoted.charAt(i3), 16);
                    i2 = i3 + 1;
                    int lo = Character.digit(quoted.charAt(i2), 16);
                    if (0 > hi || 0 > lo) {
                        throw ((RuntimePolicyUtilsException) LOGGER.logSevereException((PolicyLogger) new RuntimePolicyUtilsException(LocalizationMessages.WSP_0079_ERROR_WHILE_RFC_2396_UNESCAPING(quoted)), false));
                    }
                    int i4 = newLength;
                    newLength++;
                    unquoted[i4] = (byte) ((hi * 16) + lo);
                } else {
                    int i5 = newLength;
                    newLength++;
                    unquoted[i5] = (byte) c2;
                }
                i2++;
            }
            try {
                return new String(unquoted, 0, newLength, "utf-8");
            } catch (UnsupportedEncodingException uee) {
                throw ((RuntimePolicyUtilsException) LOGGER.logSevereException(new RuntimePolicyUtilsException(LocalizationMessages.WSP_0079_ERROR_WHILE_RFC_2396_UNESCAPING(quoted), uee)));
            }
        }
    }
}
