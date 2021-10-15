package cn.magicalsheep.csunoticeapi.common.model.packet;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class LoginPacket implements Packet {

    private String user;
    private String pwd;

    public LoginPacket(String user, String pwd) {
        this.user = user;
        this.pwd = Base64.getEncoder().encodeToString(pwd.getBytes(StandardCharsets.UTF_8));
    }

    public String getPwd() {
        return pwd;
    }

    public String getUser() {
        return user;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public TYPE getType() {
        return TYPE.LOGIN;
    }
}
