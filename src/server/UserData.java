package server;

import java.io.Serializable;
import java.util.Objects;

public class UserData implements Serializable {
    private String name;
    private String password;
    private int access;//0 -->viewer, 1-->manufacturer

    public UserData(String name, String password, int access) {
        this.name = name;
        this.password = password;
        this.access=access;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getAccess() {
        return access;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==this) return true;
        if(obj==null || obj.getClass()!=UserData.class) return false;
        UserData data=(UserData) obj;
        return name.equalsIgnoreCase(data.name) && password.equals(data.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password);
    }

    @Override
    public String toString() {
        return "UserData{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", access=" + access +
                '}';
    }
}
