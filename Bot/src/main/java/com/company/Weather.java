package com.company;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;

public class Weather {
    //token = 4baca2ad981ef9ee5574356c6511c3cc

    public static String getWeather(Message message, Model1 model) throws IOException {
        URL url;
        if (message.hasLocation()) {
            url = new URL
                    ("https://api.openweathermap.org/data/2.5/weather?lat=" + message.getLocation().getLatitude() +
                            "&lon=" + message.getLocation().getLongitude() + "&units=metric&appid=4baca2ad981ef9ee5574356c6511c3cc");
        } else {
            String answer = URLEncoder.encode(message.getText().toString(), "UTF-8");
            url = new URL
                    ("https://api.openweathermap.org/data/2.5/weather?q=" + answer + "&units=metric&appid=4baca2ad981ef9ee5574356c6511c3cc");
        }

        URLConnection urlConnection = url.openConnection();

       /* Scanner in = new Scanner((InputStream) url.getContent());
        String result = "";
        while (in.hasNext()) {
            result += in.nextLine();
        }
        JSONObject object = new JSONObject(result);
        model.setName(object.getString("name"));

        JSONObject main = object.getJSONObject("main");
        model.setTemp(main.getDouble("temp"));
        model.setHumidity(main.getDouble("humidity"));

        JSONArray getArray = object.getJSONArray("weather");
        for (int i = 0; i < getArray.length(); i++) {
            JSONObject obj = getArray.getJSONObject(i);
            model.setIcon((String) obj.get("icon"));
            model.setMain((String) obj.get("main"));
        }*/
        Gson gson = new Gson();
        model = gson.fromJson(new InputStreamReader(urlConnection.getInputStream()), Model1.class);
       /* Weather1[] weather1 = model.weather.toArray(new Weather1[0]);
        String[]main=null;
        String[]icon=null;
        for (int i = 0; i < weather1.length; i++) {
            main[i] = weather1[i].main;
            icon[i] = weather1[i].icon;
        }*/
        return "City: " + model.name + "\n" +
                "Temperatura: " + model.main.temp + "C" + "\n" +
                "Humidity: " + model.main.humidity + "%" + "\n" +
                "Weather: " + model.weather.get(0).main + "\n" +
                "http://openweathermap.org/img/w/" + model.weather.get(0).icon + ".png";
        /*"City: " + model.getName() + "\n" +
                "Temperatura: " + model.getTemp() + "C" + "\n" +
                "Humidity: " + model.getHumidity() + "%" + "\n" +
                "Weather: " + model.getMain() + "\n" +
                "http://openweathermap.org/img/w/" + model.getIcon() + ".png";*/
    }

    public static String getSubWeather(String message, Model1 model) throws IOException {
        //message = URLEncoder.encode(message, "UTF-8");
        URL url = new URL
                ("https://api.openweathermap.org/data/2.5/weather?lat=" + message +
                        "&units=metric&appid=4baca2ad981ef9ee5574356c6511c3cc");

        URLConnection urlConnection = url.openConnection();
        Gson gson = new Gson();
        model = gson.fromJson(new InputStreamReader(urlConnection.getInputStream()), Model1.class);

        return "City: " + model.name + "\n" +
                "Temperatura: " + model.main.temp + "C" + "\n" +
                "Humidity: " + model.main.humidity + "%" + "\n" +
                "Weather: " + model.weather.get(0).main + "\n" +
                "http://openweathermap.org/img/w/" + model.weather.get(0).icon + ".png";
    }
}
