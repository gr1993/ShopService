package park.shop.web.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MerchantUidUtilTest {
    @Test
    void todayMerchantUid() {
        String uid = MerchantUidUtil.getTodayMerchantUid();
        System.out.println("uid = " + uid);
    }

    @Test
    void getMerchantUid() {
        String firstUid = MerchantUidUtil.getFirstMerchantUid();
        System.out.println("firstUid = " + firstUid);

        String nextUid = MerchantUidUtil.getNextMerchantUid(firstUid);
        System.out.println("nextUid = " + nextUid);
    }
}