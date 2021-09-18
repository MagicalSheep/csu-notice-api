package cn.magicalsheep.csunoticeapi.model;

public class Configuration {

    private String user;
    private String pwd;

    private String root_uri;
    private String cse_uri;

    private int update_num_per_pages;
    private boolean init_db = false;
    private boolean school = true;
    private boolean cse = false;

    public boolean isCse() {
        return cse;
    }

    public boolean isSchool() {
        return school;
    }

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

    public String getCse_uri() {
        return cse_uri;
    }

    public String getRoot_uri() {
        return root_uri;
    }

    public void setCse(boolean cse) {
        this.cse = cse;
    }

    public void setSchool(boolean school) {
        this.school = school;
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

    public void setCse_uri(String cse_uri) {
        this.cse_uri = cse_uri;
    }
}
