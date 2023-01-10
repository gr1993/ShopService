package park.shop.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class EncryptUtilTest {

    @Test
    void encrypt() {
        String pwd = "test123";
        String salt = EncryptUtil.getSalt();

        String encryptedPwd = EncryptUtil.getEncrypt(pwd, salt);
        log.info("salt={}, encryptedPwd={}", salt, encryptedPwd);
    }
}