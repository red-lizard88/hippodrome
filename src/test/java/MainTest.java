import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

public class MainTest {


    @Disabled
    @Timeout(value = 22)
    @Test
    public void timeout() throws Exception {
        Main.main(null);
    }

}
