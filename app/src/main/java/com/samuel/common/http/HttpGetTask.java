package com.samuel.common.http;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public class HttpGetTask extends AsyncTask<Object, Void, Object> {
    public static final String GET_ACCESS_TOKEN = "AccessToken";
    public static final String GET_OBJECT_DETECTION = "ObjectDetect";
    private Handler mHandler = null;

    /**
     * 构造函数
     * 
     * @param handler
     */
    public HttpGetTask(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    protected Object doInBackground(Object... params) {
        String type = GET_ACCESS_TOKEN;
        if (params != null && params.length > 0) {
            type = (String) params[0];
        }
        if (type == GET_ACCESS_TOKEN) {
            return AuthService.getAuth();
        } else if (type == GET_OBJECT_DETECTION) {
            if (params.length > 3) {
                String accessToken = (String) params[1];
                String base64Data = (String) params[2];
                double threshold = (Double) params[3];
                return ObjectDetectService.getObjectDetection(accessToken, base64Data, threshold);
            } else {
                return null;
            }
        } else {
            String hostUrl = (String) params[1];
            return CommonHttpRequest.getHttp(hostUrl);
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        if (mHandler != null) {
            Message msg = mHandler.obtainMessage();
            msg.what = 1;
            msg.obj = result;
            mHandler.sendMessage(msg);
            mHandler = null;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (mHandler != null) {
            mHandler = null;
        }
    }
}
