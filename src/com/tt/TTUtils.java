package com.tt;

import com.profesorfalken.jsensors.JSensors;
import com.profesorfalken.jsensors.model.components.Components;
import com.profesorfalken.jsensors.model.components.Cpu;
import com.profesorfalken.jsensors.model.sensors.Temperature;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TTUtils {
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
        return getAnyKeyValueAsString(jsonObject, responseKey);
    }

    public static String[] getAnyKeyValueAsString(JSONObject json, String[] keys){
        String[] values = new String[keys.length];

            for (int k = 0 ; k < keys.length ; k++) {
                Object value;
                if(keys[k].contains(".")){
                    String[] objName = keys[k].split("\\.");
                    if (objName.length >= 3){
                        JSONArray jsonArray = json.getJSONArray(objName[0]);
                        JSONObject jsonObject = jsonArray.getJSONObject(Integer.parseInt(objName[1]));
                        value = jsonObject.get(objName[2]);
                    }else{
                        JSONObject jsonObject = json.getJSONObject(objName[0]);
                        value = jsonObject.get(objName[1]);
                    }
                }else {
                    value = json.get(keys[k]);
                }

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

    /**
     * temp.value.intValue() casts Double to int, getting rid of the decimal in all options.
     */

    protected static String getDeviceTemperature() {
        Components components = JSensors.get.components();
        java.util.List<Cpu> cpus = components.cpus;
        StringBuilder sysMetaData = new StringBuilder();
        if (cpus != null) {
            int cpuCount = 0;
            for (final Cpu cpu : cpus) {
                cpuCount++;
                sysMetaData.append("CPU COMPONENT: ").append(cpu.name).append(",");
                if (cpu.sensors != null) {
                    //Print temperatures
                    java.util.List<Temperature> temps = cpu.sensors.temperatures;
                    for (final Temperature temp : temps) {
                        sysMetaData.append(temp.name).append(": ").append(temp.value.intValue()).append("°C,");
                    }
                    sysMetaData.deleteCharAt(sysMetaData.length() - 1);
                    if (cpuCount != cpus.size()) {
                        sysMetaData.append("|");
                    }
                }
            }
        }

        //System.out.println(sysMetaData);
        return sysMetaData.toString();
    }

    protected static Color getPanelColorAccordingToTemperature(String temp) {
        float floatTemp = Float.parseFloat(temp.trim());
        if (floatTemp <= 22 && floatTemp >= 10) { //cold
            return new Color(0.0f,0.0f,1.0f,0.8f);
        } else if (floatTemp > 22 && floatTemp <=30) { // slight hot
            return new Color(1.0f,0.6f,0.0f,0.8f);
        } else if (floatTemp >= 40 && floatTemp <= 50) {// more hot
            return new Color(1.0f,0.2f,0.0f,0.8f);
        } else if (floatTemp < 10) { //very cold
            return new Color(0.0f,0.0f,0.8f,0.8f);
        } else { //very hot
            return new Color(1.0f,0.0f,0.0f,0.8f);
        }
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

    public static int getCurrentWeekOfMonth(){

        return 0;
    }


}