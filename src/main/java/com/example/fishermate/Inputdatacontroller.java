package com.example.fishermate;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class Inputdatacontroller {

    final static int FORECASTQTY = 8;

    public static int realMaxDays(Inputs inputdata) {
        int numberOfForecasts = inputdata.getMaxDays() * FORECASTQTY;
        WeatherData data = null;
        for (int i = 0; i < numberOfForecasts; i++) {
            data = DBconnection.dataRetriever(inputdata);
            if (compare(data) == -1) {
                return i / FORECASTQTY;
            }
            inputdata.setDateTime(updateTime(inputdata));
        }

        return inputdata.getMaxDays();
    }
    public static int compare(WeatherData data) {

        if (data.getRainProbability() > 70 || data.getWindSpeed() > 50 || data.isStorms()
                || data.getVisibility() < 1000
                || data.isLightning()) {
            return -1;

        } else {
            return 1;
        }
    }

    public static String updateTime(Inputs inputData) {

        String updatedTime = null;

        Timestamp timestamp = Timestamp.valueOf(inputData.getDateTime());

        // Create Calendar to add 3 hours
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        cal.add(Calendar.HOUR_OF_DAY, 3);

        // Get new Timestamp
        Timestamp newTimestamp = new Timestamp(cal.getTimeInMillis());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Convert to LocalDateTime and format it
        updatedTime = newTimestamp.toLocalDateTime().format(formatter);

        return updatedTime;
    }

    public static void Consumption(Inputs inputData, int days) {
        System.out.println("Approximating the consumptions...");
        double water = inputData.getNoOfCrewMembers() * 25 * days;
        System.out.println("Maximum water Consumption: " + water + " liters");

        double food = inputData.getNoOfCrewMembers() * 2.5 * days;
        System.out.println("Maximum food Consumption: " + food + " kg");

        double fuel = 500 * days;
        System.out.println("Maximum fuel Consumption: " + fuel + " liters");

    }

    public static void finalDecision(Inputs inputdata) {
        WeatherData data = DBconnection.dataRetriever(inputdata);
        System.out.println();
        if (compare(data) == -1) {
            System.out.println("The trip is not recommended due to bad weather conditions.");
        } else {
            System.out.println("The trip is recommended based on the weather conditions.");
            if (realMaxDays(inputdata) == 0) {
                System.out.println(
                        "Unfortunately based on the weather condition of the rest of the day, you cannot stay for the whole day.");
            } else if (realMaxDays(inputdata) < inputdata.getMaxDays()) {
                System.out
                        .println("You can only stay safely for only " + realMaxDays(inputdata) + " days.");
                Consumption(inputdata, realMaxDays(inputdata));
            } else {
                System.out.println("You can stay your maximumly expected  " + inputdata.getMaxDays() + " days.");
                Consumption(inputdata, inputdata.getMaxDays());
            }

        }
    }

}
