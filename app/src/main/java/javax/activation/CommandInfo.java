package javax.activation;

import java.beans.Beans;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/* loaded from: rt.jar:javax/activation/CommandInfo.class */
public class CommandInfo {
    private String verb;
    private String className;

    public CommandInfo(String verb, String className) {
        this.verb = verb;
        this.className = className;
    }

    public String getCommandName() {
        return this.verb;
    }

    public String getCommandClass() {
        return this.className;
    }

    public Object getCommandObject(DataHandler dh, ClassLoader loader) throws IOException, ClassNotFoundException {
        InputStream is;
        Object new_bean = Beans.instantiate(loader, this.className);
        if (new_bean != null) {
            if (new_bean instanceof CommandObject) {
                ((CommandObject) new_bean).setCommandContext(this.verb, dh);
            } else if ((new_bean instanceof Externalizable) && dh != null && (is = dh.getInputStream()) != null) {
                ((Externalizable) new_bean).readExternal(new ObjectInputStream(is));
            }
        }
        return new_bean;
    }
}
