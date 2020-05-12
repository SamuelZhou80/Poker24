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
 * 物体检测服务接口
 * 
 * @author Administrator
 *
 */
public class ObjectDetectService {

    /**
     * 对照片文件进行物体检测, 获取检测到的物体标签和位置信息
     * 
     * @param accessToken
     *            物体检测服务的访问令牌环
     * @param base64Data
     *            待检测图片的Base64数据体
     * @param threshold
     *            检测阈值, 如果为0则默认为0.3
     * @return JSON对象, 包含检测到的物体标签和位置信息等
     */
    public static JSONObject getObjectDetection(String accessToken, String base64Data, double threshold) {
        // 服务地址
        String serviceHost = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/detection/mbes_sku_test";
        serviceHost += "?access_token=" + accessToken;
        try {
            JSONObject dataObj = new JSONObject();
            dataObj.put("image", base64Data);
            dataObj.put("threshold", threshold);
            byte[] bodyData = dataObj.toString().getBytes();

            URL realUrl = new URL(serviceHost);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            // 设置HTTP连接超时时间
            connection.setConnectTimeout(15 * 1000);
            // connection.setReadTimeout(overTime * 1000);

            // 设置允许HTTP读写
            connection.setDoOutput(true);
            connection.setDoInput(true);
            // POST请求不能使用缓存
            connection.setUseCaches(false);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Length", String.valueOf(bodyData.length));
            connection.connect();

            // 写入待上传数据

            OutputStream outStream = connection.getOutputStream();
            if (outStream != null) {
                // 要上传的内容写入流中
                outStream.write(bodyData);
                // 刷新关闭
                outStream.flush();
                outStream.close();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

            }

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
            JSONObject jsonObject = new JSONObject(result);
            return jsonObject;
        } catch (Exception e) {
            System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }
}
