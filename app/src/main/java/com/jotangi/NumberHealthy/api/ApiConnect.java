package com.jotangi.NumberHealthy.api;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ApiConnect {

    private String TAG = ApiConnect.class.getSimpleName() + "(TAG)";

    private ApiConnect.resultListener listener;

    public interface resultListener {
        void onSuccess(String message);

        void onFailure(String task, String message);
    }

    private String runTask = "";


    public void getWeather(
            String adminArea,
            String timeFrom,
            String timeTo,
            resultListener listener
    ) {

        this.listener = listener;
        runTask = ApiConstant.TASK_weather;

        String url = ApiConstant.WEATHER_URL + adminArea
                + ApiConstant.ITEM_TIMEFROM + timeFrom
                + ApiConstant.ITEM_TIMETO + timeTo;
        Log.d(TAG, "URL: " + url);
        Log.d(TAG, "adminArea: " + adminArea);
        Log.d(TAG, "timeFrom: " + timeFrom);
        Log.d(TAG, "timeTo: " + timeTo);

        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();

        okClinet(request);
    }

    // 足壓量測
    public void getDataByMobile(
            String tel,
            resultListener listener
    ) {

        this.listener = listener;
        runTask = ApiConstant.TASK_fpm;

        String url = ApiConstant.ASIAFOOT_URL + ApiConstant.get_data_by_mobile;
        Log.d(TAG, "URL: " + url);

        JSONObject object = new JSONObject();
        try {
            object.put("mobile", tel);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "object: " + object);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        RequestBody requestBody = RequestBody.create(object.toString(), mediaType);

        runExecute(url, requestBody);
    }

    private void okClinet(Request request) {

        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                listener.onFailure(runTask, "連線失敗");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                ResponseBody responseBody = response.body();

                if (responseBody != null) {
                    String body = responseBody.string();
                    Log.d(TAG, runTask + " body: " + body);

                    processBody(body);
                } else {
                    listener.onFailure(runTask, "無回傳資料");
                }
            }
        });
    }

    private void runExecute(String url, RequestBody requestBody) {

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        String body;

        try {
            Response response = new OkHttpClient().newCall(request).execute();

            if (response.isSuccessful()) {

                ResponseBody responseBody = response.body();

                if (responseBody != null) {
                    body = responseBody.string();
                    Log.w(TAG, runTask + " - body: " + body);

                    processBody(body);
                } else {

                    listener.onFailure(runTask, "無回傳資料");
                }

            } else {

                listener.onFailure(runTask, "連線失敗");
            }

        } catch (IOException e) {
            e.printStackTrace();
            listener.onFailure(runTask, "意外錯誤");
        }
    }


    private void processBody(String body) {

        switch (runTask) {
            case ApiConstant.TASK_weather:
                taskWeather_getWeather(body);
                break;
            case ApiConstant.TASK_fpm:
                taskFpm_getDataByMobile(body);
                break;
        }
    }

    private void taskWeather_getWeather(String body) {

        try {
            JSONObject jsonObject = new JSONObject(body);
            String success = jsonObject.getString("success");

            if ("true".equals(success)) {
                JSONArray locations = jsonObject
                        .getJSONObject("records")
                        .getJSONArray("locations").getJSONObject(0)
                        .getJSONArray("location").getJSONObject(0)
                        .getJSONArray("weatherElement");
                listener.onSuccess(locations.toString());
            } else {
                listener.onFailure("失敗", "連線失敗");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            listener.onFailure(runTask, "回傳欄位不存在");
        }
    }

    private void taskFpm_getDataByMobile(String body) {

        try {
            JSONObject jsonObject = new JSONObject(body);
            String resultCode = jsonObject.getString("resultCode");

            if ("200".equals(resultCode)) {
                String events = jsonObject.getString("events");
                listener.onSuccess(events);
            } else {
                listener.onFailure("失敗", resultCode);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            listener.onFailure(runTask, "回傳欄位不存在");
        }
    }
}
