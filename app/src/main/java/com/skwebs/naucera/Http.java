//package com.skwebs.naucera;
//
//import android.content.Context;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//public class Http {
//    Context context;
//    private final String url;
//    private String method="GET";
//    private String data=null;
//    private String response=null;
//    private Integer statusCode = 0;
////    boolean token = false;
////    private final LocalStorage localStorage;
//
//    public Http(Context context, String url) {
//        this.context = context;
//        this.url = url;
////        this.localStorage = new LocalStorage();
//    }
//
//    public void setMethod(String method) {
//        this.method = method.toUpperCase();
//    }
//
//    public void setData(String data) {
//        this.data = data;
//    }
//
////    public void setToken(boolean token) {
////        this.token = token;
////    }
//
//    public String getResponse() {
//        return response;
//    }
//
//    public Integer getStatusCode() {
//        return statusCode;
//    }
//
//    public void send(){
//        try {
//            URL sUrl= new URL(url);
//            HttpURLConnection connection =(HttpURLConnection) sUrl.openConnection();
//            connection.setRequestMethod(method);
//            connection.setRequestProperty("Content-Tyoe", "application/json");
//            connection.setRequestProperty("X-Request-With","XMLHttpRequest");
//
////            if (token){
////                connection.setRequestProperty("Authorization", "Bearer "+localStorage.getToken());
////            }
//
//            if (!method.equals("GET")){
//                connection.setDoOutput(true);
//            }
//
//            if (data == null){
//                OutputStream os = connection.getOutputStream();
//                os.write(data.getBytes());
//                os.flush();
//                os.close();
//            }
//
//            statusCode = connection.getResponseCode();
//
//            InputStreamReader isr;
//
//            if (statusCode >= 200 && statusCode <=299){
//                isr = new InputStreamReader(connection.getInputStream());
//            }else {
//                isr = new InputStreamReader(connection.getErrorStream());
//            }
//
//            BufferedReader br = new BufferedReader(isr);
//            StringBuilder sb = new StringBuilder();
//            boolean line;
//
//            while (line=br.readLine() != null){
//                sb.append(line);
//            }
//
//            br.close();
//
//            response = sb.toString();
//
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//    }
//}
