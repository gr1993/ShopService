package park.shop.web.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MerchantUidUtil {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static String getTodayMerchantUid() {
        return "ORD" + LocalDate.now().format(formatter);
    }

    public static String getFirstMerchantUid() {
        return getTodayMerchantUid() + "_0000001";
    }

    public static String getNextMerchantUid(String uid) {
        String[] split = uid.split("_");
        Integer nextNum = Integer.parseInt(split[1]) + 1;

        return split[0] + "_" + String.format("%07d", nextNum);
    }
}
