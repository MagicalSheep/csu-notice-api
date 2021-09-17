package cn.magicalsheep.csunoticeapi.model;

public class Configuration {

    private String user;
    private String pwd;
    private String root_uri;
    private int update_num_per_pages;
    private boolean init_db = false;

    public int getUpdate_num_per_pages() {
        return update_num_per_pages;
    }

    public boolean isInit_db() {
        return init_db;
    }

    public String getUser() {
        return user;
    }

    public String getPwd() {
        return pwd;
    }

    public String getRoot_uri() {
        return root_uri;
    }

    public void setInit_db(boolean init_db) {
        this.init_db = init_db;
    }

    public void setUpdate_num_per_pages(int update_num_per_pages) {
        this.update_num_per_pages = update_num_per_pages;
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
