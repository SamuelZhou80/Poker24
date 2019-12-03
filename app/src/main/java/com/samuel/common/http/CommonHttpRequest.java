package com.samuel.common.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.samuel.main.GpsUtils;

public class CommonHttpRequest {

    /**
     * ִ��Http Get����
     * 
     * @param hostUrl
     *            ��������ַ
     * @return ������Ӧ������
     */
    public static String getHttp(String hostUrl) {
        try {
            URL realUrl = new URL(hostUrl);
            // �򿪺�URL֮�������
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setRequestProperty("Content-Type" , "application/octet-stream");
//            connection.connect();

            
            connection.setRequestMethod("GET");
//            conn.setRequestProperty("User-Agent", "UNTRUSTED/1.0");
//            conn.setRequestProperty("Accept", "*/*");
            connection.setConnectTimeout(30 * 1000);
            //get�����ò���conn.getOutputStream()����Ϊ����ֱ��׷���ڵ�ַ���棬���Ĭ����false��
            //�������� 405����
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.connect();
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
//            JSONObject jsonObject = new JSONObject(result);
//            String access_token = jsonObject.getString("access_token");
            return result;
        } catch (Exception e) {
            System.err.printf("��ȡtokenʧ�ܣ�");
            e.printStackTrace(System.err);
        }
        return null;
    }
    
    public static byte[] postHttp(String path) {
        byte[] result = null;
        InputStream is = null;
        HttpURLConnection connect = getHttpConnection(path, 30);
        try {
            if (connect != null) {
                is = connect.getInputStream();
                result = GpsUtils.inputStreamToByteArray(is);
                connect.disconnect();
                connect = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 
     * @param path
     *            URL��ַ
     * @param secondTimeout
     *            ��ʱʱ��
     * @param dataBody
     *            ��������
     * @return
     */
    private static HttpURLConnection getHttpConnection(String path, int secondTimeout) {
        HttpURLConnection httpurlconnection = null;
        URL url = null;

        if (path == null) {
            return null;
        }

        try {
            url = new URL(path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        try {
            httpurlconnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if (httpurlconnection != null) {
            httpurlconnection.setConnectTimeout(30 * 1000);
            httpurlconnection.setReadTimeout(secondTimeout * 1000);
            httpurlconnection.setDoOutput(false);
            httpurlconnection.setDoInput(true);
            // POST������ʹ�û���
            httpurlconnection.setUseCaches(false);
            httpurlconnection.setInstanceFollowRedirects(true);

            try {
                httpurlconnection.setRequestMethod("POST");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }

            if (android.os.Build.VERSION.SDK_INT > 7) {
                httpurlconnection.setRequestProperty("Connection", "Keep-Alive");
                httpurlconnection.setRequestProperty("Proxy-Connection", "Keep-Alive");
            }
            httpurlconnection.setRequestProperty("Content-Type", "application/octet-stream");
//            httpurlconnection.setRequestProperty("Content-Length", String.valueOf(dataBody.length));
            // ��������
//            OutputStream outStream;
//            try {
//                outStream = httpurlconnection.getOutputStream();
//                if (outStream != null) {
//                    // Ҫ�ϴ�������д������
//                    outStream.write(dataBody);
//                    // ˢ�¹ر�
//                    outStream.flush();
//                    outStream.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
        return httpurlconnection;
    }
}
