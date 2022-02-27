package com.zachbearse.notquiterandom;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JsonParser {

    static List<Fighter> getFighterSet(Context context) {
        List<Fighter> fightersList = new ArrayList<>();
        InputStream fightersInputStream = context.getResources().openRawResource(R.raw.ssbu_fighters);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fightersInputStream, StandardCharsets.UTF_16));
        StringBuilder builder = new StringBuilder();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String jsonString = builder.toString();
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            //Log.d("DEBUG", jsonString);
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray fighterJsonArray = jsonObject.getJSONArray("fighters");
            for (int i = 0; i < fighterJsonArray.length(); i++) {
                JSONObject fighterJson = fighterJsonArray.getJSONObject(i);
                String name = fighterJson.getString("name");
                String formattedName = null;
                if(name.equalsIgnoreCase("Wii Fit Trainer") || name.equalsIgnoreCase("Mii Fighter")){
                    formattedName = name;
                } else {
                    formattedName = name.toUpperCase();
                }
                String gameSeries = fighterJson.getString("series");
                String number = fighterJson.getString("number");
                String image = fighterJson.getString("image");
                fightersList.add(new Fighter(formattedName, gameSeries, number, image));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fightersList;
    }
}
