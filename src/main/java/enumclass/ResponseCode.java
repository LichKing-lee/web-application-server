package enumclass;

/**
 * Created by LichKing on 2016. 12. 18..
 */
public enum ResponseCode {
    OK(200), FOUND(302);

    private final Integer code;

    ResponseCode(Integer code) {
        this.code = code;
    }

    public Integer getCode(){
        return this.code;
    }

    public String getMessage(){
        return this.code + " " + this.name();
    }
}
