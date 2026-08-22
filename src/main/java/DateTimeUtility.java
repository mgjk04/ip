import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Shared date-time formats so that input parsing and display stay
 * consistent across commands. Dates are entered as
 * {@code yyyy-MM-dd HHmm} (e.g., {@code 2019-12-02 1800}) and shown in
 * a friendlier style (e.g., {@code Dec 02 2019, 6:00 PM}).
 */
public class DateTimeUtility {
    // Accepts user-supplied dates, e.g. {@code 2019-12-02 1800}.
    public static final DateTimeFormatter INPUT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    // Renders dates for display, e.g. {@code Dec 02 2019, 6:00 PM}.
    public static final DateTimeFormatter DISPLAY = DateTimeFormatter
            .ofPattern("MMM dd yyyy, h:mm a")
            .withLocale(Locale.ENGLISH);
}
