package app.mma.locationlistenerapp.net;


public class ApiRes<DATA> {
    private boolean error;
    private String msg = "";
    private DATA data;

    public ApiRes(boolean error, String msg, DATA data){
        this.error = error;
        this.msg = msg;
        this.data = data;
    }

    public boolean isError() {
        return error;
    }

    public String getMsg() {
        return msg;
    }

    public DATA getData() {
        return data;
    }


}
