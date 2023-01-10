package park.shop.web.util.formatter;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class FormatterTest {

    LocalDateTimeFormatter dateTimeFormatter = new LocalDateTimeFormatter();
    NumberFormatter numberFormatter = new NumberFormatter();

    @Test
    void localDateTimeFormatterTest() throws ParseException {
        LocalDateTime dateTime = dateTimeFormatter.parse("2000-12-12 10:23:20", Locale.KOREA);
        System.out.println("dateTime = " + dateTime);
        String format = dateTimeFormatter.print(dateTime, Locale.KOREA);
        System.out.println("format = " + format);
        assertThat(format).isEqualTo("2000-12-12 10:23:20");
    }

    @Test
    void numberFormatterTest() throws ParseException {
        Number number = numberFormatter.parse("1,000", Locale.KOREA);
        assertThat(number).isEqualTo(1000L);

        String numberStr = numberFormatter.print(1000, Locale.KOREA);
        assertThat(numberStr).isEqualTo("1,000");
    }
}