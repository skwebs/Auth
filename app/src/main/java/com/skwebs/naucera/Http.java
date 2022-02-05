package com.skwebs.naucera;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Http {
    Context context;
    private String url, method="GET",data=null,response=null;
    private Integer statusCode = 0;
    private Boolean token = false;
    private final LocalStorage localStorage;

    public Http(Context context, String url) {
        this.context = context;
        this.url = url;
        localStorage = new LocalStorage(context);
    }

    public void setMethod(@NonNull String method) {
        this.method = method.toUpperCase();
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setToken(Boolean token) {
        this.token = token;
    }

    public String getResponse() {
        return response;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void send(){
        try {
            URL sendUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) sendUrl.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type","application/json");
            connection.setRequestProperty("Accept","application/json");
            connection.setRequestProperty("X-Requested-With","XMLHttpRequest");

            if (token){
                connection.setRequestProperty("Authorization", "Bearer " + localStorage.getToken());
            }
            if (!method.equals("GET")){
                connection.setDoOutput(true);
            }
            if (data != null){
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(data.getBytes());
                outputStream.flush();
                outputStream.close();
            }
            statusCode= connection.getResponseCode();
            InputStreamReader inputStreamReader;

            if (statusCode >= 200 && statusCode <=299){
//                if success response
                inputStreamReader = new InputStreamReader(connection.getInputStream());
            }else {
//                if error response
                inputStreamReader = new InputStreamReader(connection.getErrorStream());
            }
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuffer = new StringBuilder();
            String line;
            while ((line=bufferedReader.readLine())!= null){
                stringBuffer.append(line);
            }
            bufferedReader.close();
            response = stringBuffer.toString();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
