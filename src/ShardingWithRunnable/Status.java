package ShardingWithRunnable;

/**
 * Created by seven on 1/21/15.
 */
enum Status {

    OK(200, "OK"), FAILED(404, "FAILED");

    private int code;

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    private String description;

    Status(int c, String d){
        code = c;
        description = d;
    }

    public static String getStatusByCode(int code){
        for(Status s : Status.values())
            if(code == s.code)
                return s.description;
        return "";
    }
}
