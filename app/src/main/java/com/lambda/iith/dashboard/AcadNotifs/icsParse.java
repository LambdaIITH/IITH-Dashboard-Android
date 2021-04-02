package com.lambda.iith.dashboard.AcadNotifs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class icsParse {
    private final JSONArray AcadCal;
    public icsParse(String response) {
        System.out.println("HHHH");
        AcadCal= new JSONArray();


            String[] temp = response.split("\n");

            int i = 0;

            while (i < temp.length) {
                if (temp[i].contains("BEGIN:VEVENT")) {
                    i++;
                    JSONObject JO = new JSONObject();
                    while (!temp[i].contains("END:VEVENT")) {
                        String[] eve = temp[i].split(":");
                        try {
                        if (eve[0].contains("DTSTART;VALUE=DATE")) {

                            JO.put("StartDate", eve[1]);
                        }
                        if (eve[0].contains("SUMMARY")) {

                            JO.put("Title", eve[1]);
                        }
                        i++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    AcadCal.put(JO);
                }
                i++;

            }


        System.out.println("HHHH" + AcadCal.toString());
    }

    public JSONArray getArray(){
        return AcadCal;
    }
}
