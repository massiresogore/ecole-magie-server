package emcer.cg.fr.gestionutilisateuronline.system;

public class Result {
    private boolean flag;
    private String message;
    private Object data;
    private Integer code;



    public Result(boolean flag, String message,Integer code, Object data ) {
        this.flag = flag;
        this.message = message;
        this.data = data;
        this.code = code;
    }
    public Result(boolean flag, String message, Integer code) {
        this.flag = flag;
        this.message = message;
        this.code = code;
    }
    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
