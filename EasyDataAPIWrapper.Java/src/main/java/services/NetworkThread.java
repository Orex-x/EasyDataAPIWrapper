package services;

import okhttp3.*;

import java.io.IOException;

public class NetworkThread extends Thread {
    private String result;
    private String urlString, body, method;

    public MediaType JSON;

    public NetworkThread(String urlString, String body, String method, String charset) {
        this.urlString = urlString;
        this.body = body;
        this.method = method;
        JSON = MediaType.get("application/json; charset=" + charset);
    }

    public String getResult() {
        return result;
    }

    String post(String url, String json) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    @Override
    public void run() {
        try {
            result = post(urlString, body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}