package com.tt;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class APIUtils {
    private static JSONObject jsonObject;

    public static String[] getKeysFromAPIResponse(String apiURL, String... responseKey) {
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
//        String response="";
//        for (String key: responseKey) {
//            response = response + " " + jsonObject.getString(key);
//        }
        return getAnyKeyValueAsString(jsonObject, responseKey);
    }

    public static String[] getAnyKeyValueAsString(JSONObject json, String[] keys){
        String[] values = new String[keys.length];
            for (int k = 0 ; k < keys.length ; k++) {
                System.out.println(k);
                Object value = json.get(keys[k]);
                System.out.println(value);
                String dataType = value.getClass().getSimpleName();

                if (dataType.equalsIgnoreCase("String")) {
                    System.out.println("Key :" + keys[k] + " | type :string | value:" + value);
                    values[k] = (String) value;
                } else if (dataType.equalsIgnoreCase("Long")) {
                    System.out.println("Key :" + keys[k] + " | type :long | value:" + value);
                    values[k] = String.valueOf(value);

                } else if (dataType.equalsIgnoreCase("Float")) {
                    System.out.println("Key :" + keys[k] + " | type :float | value:" + value);
                    values[k] = String.valueOf(value);

                } else if (dataType.equalsIgnoreCase("Double")) {
                    System.out.println("Key :" + keys[k] + " | type :double | value:" + value);
                    values[k] = String.valueOf(value);

                } else if (dataType.equalsIgnoreCase("Boolean")) {
                    System.out.println("Key :" + keys[k] + " | type :bool | value:" + value);
                    values[k] = String.valueOf(value);

                } else if (dataType.equalsIgnoreCase("Integer")) {
                    System.out.println("Key :" + keys[k] + " | type :int | value:" + value);
                    values[k] = String.valueOf(value);

                }
            }

        return values;
    }

    public static String getIPAddress(){
        BufferedReader br;
        String ip = "";
        String zeroTo255
                = "(\\d{1,2}|(0|1)\\"
                + "d{2}|2[0-4]\\d|25[0-5])";
        String regex
                = zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255;
        Pattern p = Pattern.compile(regex);

        try {
            URL url = new URL("http://checkip.amazonaws.com/");
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            ip = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Matcher m = p.matcher(ip);
        return (m.matches()) ? ip : "";
    }


}