package com.kobi.paymentservice.service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class VnPayUtils {
    public static String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");

            final SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);

            final byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            final byte[] result = hmac512.doFinal(dataBytes);

            return bytesToHex(result); // Dùng lại hàm bytesToHex của bạn
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo chữ ký HmacSHA512", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static String getQueryUrl(Map<String, String> vnp_Params, String hashSecret) {
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                query.append('&');
                hashData.append('&');
            }
        }
        query.setLength(query.length() - 1); // remove last &
        hashData.setLength(hashData.length() - 1); // remove last &

        String vnp_SecureHash = hmacSHA512(hashSecret, hashData.toString());
        return query.toString() + "&vnp_SecureHash=" + vnp_SecureHash;
    }

    // Thêm hàm này vào lớp VnpayUtils.java
    public static String getHashData(Map<String, String> vnp_Params) {
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                hashData.append('&');
            }
        }
        hashData.setLength(hashData.length() - 1); // remove last &
        return hashData.toString();
    }
    public static String getPipeDelimitedHashData(Map<String, String> vnp_Params) {
        // Thứ tự các trường này phải tuân thủ nghiêm ngặt theo tài liệu của VNPAY cho API JSON.
        String[] fieldOrder = new String[]{
                "vnp_RequestId", "vnp_Version", "vnp_Command", "vnp_TmnCode",
                "vnp_TransactionType", "vnp_TxnRef", "vnp_Amount", "vnp_TransactionNo",
                "vnp_TransactionDate", "vnp_CreateBy", "vnp_CreateDate", "vnp_IpAddr", "vnp_OrderInfo"
        };

        StringBuilder hashData = new StringBuilder();
        for (String fieldName : fieldOrder) {
            if (vnp_Params.containsKey(fieldName)) {
                String fieldValue = vnp_Params.get(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    hashData.append(fieldValue).append("|");
                }
            }
        }
        // Xóa dấu '|' cuối cùng
        if (hashData.length() > 0) {
            hashData.setLength(hashData.length() - 1);
        }
        return hashData.toString();
    }
}
