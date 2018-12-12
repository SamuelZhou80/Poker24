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
     * 执行Http Get命令
     * 
     * @param hostUrl
     *            服务器地址
     * @return 服务器应答数据
     */
    public static String getHttp(String hostUrl) {
        try {
            URL realUrl = new URL(hostUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setRequestProperty("Content-Type" , "application/octet-stream");
//            connection.connect();

            
            connection.setRequestMethod("GET");
//            conn.setRequestProperty("User-Agent", "UNTRUSTED/1.0");
//            conn.setRequestProperty("Accept", "*/*");
            connection.setConnectTimeout(30 * 1000);
            //get请求用不到conn.getOutputStream()，因为参数直接追加在地址后面，因此默认是false。
            //否则会出现 405错误
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.err.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }

            // 返回结果示例
//            JSONObject jsonObject = new JSONObject(result);
//            String access_token = jsonObject.getString("access_token");
            return result;
        } catch (Exception e) {
            System.err.printf("获取token失败！");
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
     *            URL地址
     * @param secondTimeout
     *            超时时间
     * @param dataBody
     *            ：数据体
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
            // POST请求不能使用缓存
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
            // 发送数据
//            OutputStream outStream;
//            try {
//                outStream = httpurlconnection.getOutputStream();
//                if (outStream != null) {
//                    // 要上传的内容写入流中
//                    outStream.write(dataBody);
//                    // 刷新关闭
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
