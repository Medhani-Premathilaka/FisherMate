package com.example.fishermate;

//import org.json.JSONArray;
//import org.json.JSONObject;

import java.sql.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import org.json.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;


public class DBconnection {

    public Connection conn;

    static final String DB_URL = "jdbc:mysql://localhost:3306/fishermate";
    static final String DB_USER = "root";
    static final String DB_PASS = "";

    static final String API_KEY = "85b0108dcedddfd215efcd3d8561ed8e";
    static String[] locations = {
            "Galle", "Matara", "Hambantota", "Trincomalee", "Jaffna",
            "Negombo", "Colombo", "Batticaloa", "Kalpitiya"
    };

    public void updateWeatherData() {
        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            for (String city : locations) {
                String urlStr = "https://api.openweathermap.org/data/2.5/forecast?q=" + city +
                        ",LK&appid=" + API_KEY + "&units=metric";

                URL url = URI.create(urlStr).toURL();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                StringBuilder content = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                conn.disconnect();

                JSONObject json = new JSONObject(content.toString());
                JSONArray list = json.getJSONArray("list");

                // Connect to DB
                Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

                // Insert or update query
                String query = "INSERT INTO weather_data (location, forecast_time, wind_speed, rain_probability, visibility, weather_condition) "
                        +
                        "VALUES (?, ?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE wind_speed = VALUES(wind_speed), rain_probability = VALUES(rain_probability), "
                        +
                        "visibility = VALUES(visibility), weather_condition = VALUES(weather_condition)";

                PreparedStatement stmt = con.prepareStatement(query);

                // Collect current forecast times
                Set<String> currentTimestamps = new HashSet<>();

                for (int i = 0; i < list.length(); i++) {
                    JSONObject obj = list.getJSONObject(i);

                    String forecastTime = obj.getString("dt_txt");
                    currentTimestamps.add(forecastTime);

                    double wind = obj.getJSONObject("wind").getDouble("speed");
                    int visibility = json.has("visibility") ? json.getInt("visibility") : 10000;
                    double rainProb = obj.has("pop") ? obj.getDouble("pop") * 100 : 0;

                    JSONArray weatherArray = obj.getJSONArray("weather");
                    String condition = weatherArray.getJSONObject(0).getString("main");

                    stmt.setString(1, city);
                    stmt.setString(2, forecastTime);
                    stmt.setDouble(3, wind);
                    stmt.setDouble(4, rainProb);
                    stmt.setInt(5, visibility);
                    stmt.setString(6, condition);

                    stmt.executeUpdate();
                }

                // Delete outdated forecast times
                if (!currentTimestamps.isEmpty()) {
                    String placeholders = currentTimestamps.stream()
                            .map(t -> "?")
                            .collect(Collectors.joining(", "));

                    String deleteQuery = "DELETE FROM weather_data WHERE location = ? AND forecast_time NOT IN ("
                            + placeholders + ")";
                    PreparedStatement deleteStmt = con.prepareStatement(deleteQuery);
                    deleteStmt.setString(1, city);

                    int index = 2;
                    for (String time : currentTimestamps) {
                        deleteStmt.setString(index++, time);
                    }

                    deleteStmt.executeUpdate();
                    deleteStmt.close();
                }

                stmt.close();
                con.close();
                System.out.println("✅ Weather data updated for " + city);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/fishermate";
        String username = "root";
        String password = "";

        try {
            // Use the updated MySQL driver class
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found: " + e.getMessage());
        }
        return conn;
    }

    public static WeatherData dataRetriever(Inputs inputdata) {

        WeatherData weatherData = new WeatherData();

        String DB_URL = "jdbc:mysql://localhost:3306/fishermate";
        String DB_USER = "root";
        String DB_PASS = "";

        try {
            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            String sql = "SELECT * FROM weather_data WHERE location = ? AND forecast_time = ?";
            PreparedStatement stmt = con.prepareStatement(sql);

            String location = inputdata.getLocation();
            String forecastTime = inputdata.getDateTime();

            stmt.setString(1, location);

            stmt.setTimestamp(2, Timestamp.valueOf(forecastTime));

            ResultSet rs = stmt.executeQuery();

            String weatherCondition = null;
            if (rs.next()) {

                weatherData.setRainProbability(rs.getDouble("rain_probability"));
                weatherData.setWindSpeed(rs.getDouble("wind_speed"));
                weatherData.setVisibility(rs.getInt("visibility"));
                weatherCondition = rs.getString("weather_condition");
                weatherData.setLightning(weatherCondition.contains("thunder"));
                weatherData.setStorms(weatherCondition.contains("storm"));

            } else {
                return null; // No data found for the given location and time

            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return weatherData;
    }
}