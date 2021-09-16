package cn.magicalsheep.csunoticeapi.model.response;

public class Response {

    private static final int version = 1;
    private int status;
    private String msg;

    public Response(int msg) {
        this.status = 1;
        this.msg = String.valueOf(msg);
    }

    public Response(String msg) {
        this.status = 1;
        this.msg = msg;
    }

    public Response() {
        this.status = 1;
        this.msg = "ok";
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public int getVersion() {
        return version;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
