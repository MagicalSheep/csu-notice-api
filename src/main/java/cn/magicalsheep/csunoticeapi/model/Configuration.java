package cn.magicalsheep.csunoticeapi.model;

public class Configuration {

    private String user;
    private String pwd;
    private String root_uri;

    public String getUser() {
        return user;
    }

    public String getPwd() {
        return pwd;
    }

    public String getRoot_uri() {
        return root_uri;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setRoot_uri(String root_uri) {
        this.root_uri = root_uri;
    }
}
