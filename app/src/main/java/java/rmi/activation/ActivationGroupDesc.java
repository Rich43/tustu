package java.rmi.activation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.rmi.MarshalledObject;
import java.util.Arrays;
import java.util.Properties;

/* loaded from: rt.jar:java/rmi/activation/ActivationGroupDesc.class */
public final class ActivationGroupDesc implements Serializable {
    private String className;
    private String location;
    private MarshalledObject<?> data;
    private CommandEnvironment env;
    private Properties props;
    private static final long serialVersionUID = -4936225423168276595L;

    public ActivationGroupDesc(Properties properties, CommandEnvironment commandEnvironment) {
        this(null, null, null, properties, commandEnvironment);
    }

    public ActivationGroupDesc(String str, String str2, MarshalledObject<?> marshalledObject, Properties properties, CommandEnvironment commandEnvironment) {
        this.props = properties;
        this.env = commandEnvironment;
        this.data = marshalledObject;
        this.location = str2;
        this.className = str;
    }

    public String getClassName() {
        return this.className;
    }

    public String getLocation() {
        return this.location;
    }

    public MarshalledObject<?> getData() {
        return this.data;
    }

    public Properties getPropertyOverrides() {
        if (this.props != null) {
            return (Properties) this.props.clone();
        }
        return null;
    }

    public CommandEnvironment getCommandEnvironment() {
        return this.env;
    }

    /* loaded from: rt.jar:java/rmi/activation/ActivationGroupDesc$CommandEnvironment.class */
    public static class CommandEnvironment implements Serializable {
        private static final long serialVersionUID = 6165754737887770191L;
        private String command;
        private String[] options;

        public CommandEnvironment(String str, String[] strArr) {
            this.command = str;
            if (strArr == null) {
                this.options = new String[0];
            } else {
                this.options = new String[strArr.length];
                System.arraycopy(strArr, 0, this.options, 0, strArr.length);
            }
        }

        public String getCommandPath() {
            return this.command;
        }

        public String[] getCommandOptions() {
            return (String[]) this.options.clone();
        }

        public boolean equals(Object obj) {
            if (obj instanceof CommandEnvironment) {
                CommandEnvironment commandEnvironment = (CommandEnvironment) obj;
                if (this.command != null ? this.command.equals(commandEnvironment.command) : commandEnvironment.command == null) {
                    if (Arrays.equals(this.options, commandEnvironment.options)) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        }

        public int hashCode() {
            if (this.command == null) {
                return 0;
            }
            return this.command.hashCode();
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
            if (this.options == null) {
                this.options = new String[0];
            }
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof ActivationGroupDesc) {
            ActivationGroupDesc activationGroupDesc = (ActivationGroupDesc) obj;
            if (this.className != null ? this.className.equals(activationGroupDesc.className) : activationGroupDesc.className == null) {
                if (this.location != null ? this.location.equals(activationGroupDesc.location) : activationGroupDesc.location == null) {
                    if (this.data != null ? this.data.equals(activationGroupDesc.data) : activationGroupDesc.data == null) {
                        if (this.env != null ? this.env.equals(activationGroupDesc.env) : activationGroupDesc.env == null) {
                            if (this.props != null ? this.props.equals(activationGroupDesc.props) : activationGroupDesc.props == null) {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        return (((this.location == null ? 0 : this.location.hashCode() << 24) ^ (this.env == null ? 0 : this.env.hashCode() << 16)) ^ (this.className == null ? 0 : this.className.hashCode() << 8)) ^ (this.data == null ? 0 : this.data.hashCode());
    }
}
