package ShardingWithRunnable;

/**
 * Created by seven on 1/21/15.
 */
enum Command {

    CREAT(1, "Create"), READ(2, "Read"), UPDATE(3, "Update"), DELETE(4, "Delete");

    int code;
    String description;

    Command(int c, String d){
        code = c;
        description = d;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static int getCodeByDescription(String descr){
        for(Command c : Command.values() ){
            if(descr == c.description)
                return c.code;
        }
        return 0;
    }

    public static String getCommandByCode(int code){
        for(Command c : Command.values())
            if(code == c.code)
                return c.description;
        return "";
    }
}
