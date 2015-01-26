package ShardingWithRunnable;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by seven on 1/20/15.
 */
public class TargetObj implements Serializable {

    @Override
    public int hashCode() {
        return a;
    }

    Random r = new Random();
    int a = r.nextInt(Integer.MAX_VALUE);
}
