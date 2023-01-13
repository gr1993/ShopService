package park.shop.web.order.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PaymentService {

    @Value("${imp_key}")
    private String impKey;

    @Value("${imp_secret}")
    private String impSecret;

    public String getToken() throws IOException {
        HttpURLConnection conn = null;

        URL url = new URL("https://api.iamport.kr/users/getToken");

        conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        // OutputStream 으로 POST 데이터를 넘겨주는 옵션
        conn.setDoOutput(true);

        JsonObject json = new JsonObject();
        json.addProperty("imp_key", impKey);
        json.addProperty("imp_secret", impSecret);

        //Request Body 에 Data 를 담기위해 OutputStream 객체를 생성;
        OutputStream os = conn.getOutputStream();
        //문자 스트림에서 바이트 스트림으로 변환하기 위해 write 생성
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

        //body 에 바이트로 요청값 작성
        bw.write(json.toString());
        bw.flush();
        bw.close();

        //응답 받은 Body 에 InputStream 객체 생성
        InputStream is = conn.getInputStream();
        //InputStream 으로 데이터를 넘겨 받는다.(바이트를 문자 스트림으로 변환)
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));

        Gson gson = new Gson();
        //응답 json 으로 변환
        String response = gson.fromJson(br.readLine(), Map.class).get("response").toString();

        log.info("import getToken response={}", response);

        String token = gson.fromJson(response, Map.class).get("access_token").toString();

        br.close();
        conn.disconnect();

        return token;
    }

    public void paymentCancel(
            String access_token,
            String merchant_uid,
            int amount,
            String reason) throws IOException {
        log.info("결제 취소 요청 시작 : merchant_uid={}", merchant_uid);

        HttpURLConnection conn = null;
        URL url = new URL("https://api.iamport.kr/payments/cancel");

        conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", access_token);
        conn.setDoOutput(true);

        JsonObject json = new JsonObject();

        //imp_uid는 아임포트에서 만들어주는 고유 번호 지만 merchant_uid 로 관리되고 있어서 안보내도 됨
        //json.addProperty("imp_uid", imp_uid);
        json.addProperty("merchant_uid", merchant_uid);
        json.addProperty("amount", amount);
        json.addProperty("checksum", amount); //취소 가능 잔액 일치여부 확인용
        json.addProperty("reason", reason);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

        bw.write(json.toString());
        bw.flush();
        bw.close();

//        log.info("import cancel responseCode={}", conn.getResponseCode());
//        log.info("import cancel responseMessage={}", conn.getResponseMessage());

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "euc-kr"));

        String response = br.lines().collect(Collectors.joining());
        //왜 인지 모르겠는데 Unicode Escape 처리되어 있어서 한번 더 decode 해주어야함..
        response = org.apache.commons.lang3.StringEscapeUtils.unescapeJava(response);
        log.info("import cancel response={}", response);

        br.close();
        conn.disconnect();
    }
}
