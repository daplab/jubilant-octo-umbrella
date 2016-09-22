/**
 * Created by cbovigny on 8/7/15.
 */
import java.lang.Math;

public class LocalPi {

    public static void main(String[] args) {
    int count=0;
        for(int i=0; i< 100000; i++) {
            double x = Math.random()*2-1;
            double y = Math.random()*2-1;
            if(x*x+y*y < 1) {
                count += 1;
            }
        }
        System.out.println("Pi is roughly " + 4 * count / 100000.0);

    }

}
