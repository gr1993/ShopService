package park.shop.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class EncryptUtil {
    public static String getSalt() {
        SecureRandom r = new SecureRandom();
        byte[] salt = new byte[20];

        //난수 생성
        r.nextBytes(salt);

        //10진수 문자열로 변환
        return convertByteToString(salt);
    }

    public static String getEncrypt(String pwd, String salt) {
        try {
            //SHA256 알고리즘 객체 생성
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            //SHA 256 적용
            md.update((pwd+salt).getBytes());
            byte[] pwdWithSalt = md.digest();

            //10진수 문자열로 변환
            return convertByteToString(pwdWithSalt);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA 암호화 오류 발생", e);
        }
    }

    private static String convertByteToString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for(byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
