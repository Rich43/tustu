package sun.misc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/* loaded from: rt.jar:sun/misc/VMSupport.class */
public class VMSupport {
    private static Properties agentProps = null;

    private static native Properties initAgentProperties(Properties properties);

    public static native String getVMTemporaryDirectory();

    public static synchronized Properties getAgentProperties() {
        if (agentProps == null) {
            agentProps = new Properties();
            initAgentProperties(agentProps);
        }
        return agentProps;
    }

    private static byte[] serializePropertiesToByteArray(Properties properties) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4096);
        Properties properties2 = new Properties();
        for (String str : properties.stringPropertyNames()) {
            properties2.put(str, properties.getProperty(str));
        }
        properties2.store(byteArrayOutputStream, (String) null);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] serializePropertiesToByteArray() throws IOException {
        return serializePropertiesToByteArray(System.getProperties());
    }

    public static byte[] serializeAgentPropertiesToByteArray() throws IOException {
        return serializePropertiesToByteArray(getAgentProperties());
    }

    public static boolean isClassPathAttributePresent(String str) {
        try {
            Manifest manifest = new JarFile(str).getManifest();
            if (manifest != null) {
                if (manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH) != null) {
                    return true;
                }
                return false;
            }
            return false;
        } catch (IOException e2) {
            throw new RuntimeException(e2.getMessage());
        }
    }
}
