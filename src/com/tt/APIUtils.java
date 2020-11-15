package com.tt;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIUtils {
    private static JSONObject jsonObject;

    public static String getKeyValue(String apiURL, String... responseKey) {
        try {
            URL url = new URL(apiURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            StringBuilder response = new StringBuilder();
            while ((output = br.readLine()) != null) {
                response.append(output);
            }
            conn.disconnect();

            jsonObject = new JSONObject(response.toString());
            System.out.println(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String response="";
        for (String key: responseKey) {
            response = response + " " + jsonObject.getString(key);
        }
        return response;
    }


}