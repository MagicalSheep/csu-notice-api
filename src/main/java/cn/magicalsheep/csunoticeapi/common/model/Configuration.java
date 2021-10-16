package cn.magicalsheep.csunoticeapi.common.model;

public class Configuration {

    private String user;
    private String pwd;

    private String rootUri;
    private String cseUri;
    private String chromePath;

    private int updateNumPerPages;
    private boolean initDb = false;
    private boolean school = true;
    private boolean cse = false;

    public boolean isCse() {
        return cse;
    }

    public boolean isSchool() {
        return school;
    }

    public String getChromePath() {
        return chromePath;
    }

    public int getUpdateNumPerPages() {
        return updateNumPerPages;
    }

    public boolean isInitDb() {
        return initDb;
    }

    public String getUser() {
        return user;
    }

    public String getPwd() {
        return pwd;
    }

    public String getCseUri() {
        return cseUri;
    }

    public String getRootUri() {
        return rootUri;
    }

    public void setCse(boolean cse) {
        this.cse = cse;
    }

    public void setSchool(boolean school) {
        this.school = school;
    }

    public void setInitDb(boolean initDb) {
        this.initDb = initDb;
    }

    public void setUpdateNumPerPages(int update_num_per_pages) {
        this.updateNumPerPages = update_num_per_pages;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setRootUri(String rootUri) {
        this.rootUri = rootUri;
    }

    public void setCseUri(String cseUri) {
        this.cseUri = cseUri;
    }

    public void setChromePath(String chromePath) {
        this.chromePath = chromePath;
    }
}
