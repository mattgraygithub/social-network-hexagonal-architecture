import java.time.LocalDateTime;

public class Time {

    public static String now() {
        return LocalDateTime.now().toString();
    }
}
