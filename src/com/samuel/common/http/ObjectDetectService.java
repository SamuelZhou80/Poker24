package com.samuel.common.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

/**
 * ���������ӿ�
 * 
 * @author Administrator
 *
 */
public class ObjectDetectService {

    /**
     * ����Ƭ�ļ�����������, ��ȡ��⵽�������ǩ��λ����Ϣ
     * 
     * @param accessToken
     *            ���������ķ������ƻ�
     * @param base64Data
     *            �����ͼƬ��Base64������
     * @param threshold
     *            �����ֵ, ���Ϊ0��Ĭ��Ϊ0.3
     * @return JSON����, ������⵽�������ǩ��λ����Ϣ��
     */
    public static JSONObject getObjectDetection(String accessToken, String base64Data, double threshold) {
        // �����ַ
        String serviceHost = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/detection/mbes_sku_test";
        serviceHost += "?access_token=" + accessToken;
        try {
            JSONObject dataObj = new JSONObject();
            dataObj.put("image", base64Data);
            dataObj.put("threshold", threshold);
            byte[] bodyData = dataObj.toString().getBytes();

            URL realUrl = new URL(serviceHost);
            // �򿪺�URL֮�������
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            // ����HTTP���ӳ�ʱʱ��
            connection.setConnectTimeout(15 * 1000);
            // connection.setReadTimeout(overTime * 1000);

            // ��������HTTP��д
            connection.setDoOutput(true);
            connection.setDoInput(true);
            // POST������ʹ�û���
            connection.setUseCaches(false);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Length", String.valueOf(bodyData.length));
            connection.connect();

            // д����ϴ�����

            OutputStream outStream = connection.getOutputStream();
            if (outStream != null) {
                // Ҫ�ϴ�������д������
                outStream.write(bodyData);
                // ˢ�¹ر�
                outStream.flush();
                outStream.close();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

            }

            // ��ȡ������Ӧͷ�ֶ�
            Map<String, List<String>> map = connection.getHeaderFields();
            // �������е���Ӧͷ�ֶ�
            for (String key : map.keySet()) {
                System.err.println(key + "--->" + map.get(key));
            }
            // ���� BufferedReader����������ȡURL����Ӧ
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }

            // ���ؽ��ʾ��
            JSONObject jsonObject = new JSONObject(result);
            return jsonObject;
        } catch (Exception e) {
            System.err.printf("��ȡtokenʧ�ܣ�");
            e.printStackTrace(System.err);
        }
        return null;
    }
}
