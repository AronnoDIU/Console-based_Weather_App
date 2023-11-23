import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherApp {
    // API key for OpenWeatherMap API (https://openweathermap.org/api)
    private static final String API_KEY = "a2c6df140276e327b29e4b62852a8762";
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";

    public static void main(String[] args) {
        try {
            // Get city name from user input and construct API URL with API key and city name as parameters
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter the city name: ");
            String city = reader.readLine(); // e.g. "London" or "New York"

            // Construct API URL with API key and city name as parameters and get weather data
            String apiUrl = String.format(API_URL, city, API_KEY);
            String jsonResponse = getWeatherData(apiUrl);
            /* e.g. {"coord":{"lon":-0.1257,"lat":51.5085},"weather":[{"id":804,"main":"Clouds",
             "description":"overcast clouds","icon":"04d"}],"base":"stations",
             "main":{"temp":286.15,"feels_like":285.43,"temp_min":284.82,
             "temp_max":287.04,"pressure":1016,"humidity":87},
             "visibility":10000,"wind":{"speed":1.54,"deg":0},
             "clouds":{"all":90},"dt":1630549870,
             "sys":{"type":2,"id":2019646,
             "country":"GB","sunrise":1630528490,"sunset":1630576273},
             "timezone":3600,"id":2643743,"name":"London","cod":200}*/

            displayWeather(jsonResponse); // Display weather information

        } catch (IOException e) { // Catch any IOExceptions that may occur
            e.printStackTrace();
        }
    }

    // Get weather data from OpenWeatherMap API
    private static String getWeatherData(String apiUrl) throws IOException {
        StringBuilder response = new StringBuilder(); // StringBuilder is used to append strings efficiently

        URL url = new URL(apiUrl); // Create URL object from apiUrl string

        // Open connection to URL and get input stream from connection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Read input stream from connection and append to a response StringBuilder object
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;

            // Read each line of input stream and append to a response StringBuilder object
            while ((line = reader.readLine()) != null) {
                response.append(line); // Append line to a response StringBuilder object
            }
        } finally { // Finally block is executed regardless of whether an exception is thrown or not
            connection.disconnect(); // Disconnect connection
        }

        return response.toString(); // Return response as a string
    }

    // Display weather information from JSON response
    private static void displayWeather(String jsonResponse) {
        // Parse the JSON response and display relevant weather information,
        // You may use a JSON library like Jackson or Gson for parsing,
        // but for simplicity, I'll use a basic approach here.
        // Please note: In a real-world application, error handling and more robust JSON parsing should be implemented.

        String temperature = ""; // Temperature in degrees Celsius
        String description = ""; // Weather description (e.g. "cloudy")

        // Check if jsonResponse contains "temp": before attempting to split and access index
        if (jsonResponse.contains("\"temp\":")) { // e.g. "temp":286.15,"feels_like":285.43...

            // Split jsonResponse by "temp": and get the second element of the resulting array
            temperature = jsonResponse.split("\"temp\":")[1].split(",")[0];
        }

        // Check if jsonResponse contains "description":" before attempting to split and access index
        if (jsonResponse.contains("\"description\":\"")) { // e.g. "description":"overcast clouds"...

            // Split jsonResponse by "description":" and get the second element of the resulting array
            description = jsonResponse.split("\"description\":\"")[1].split("\"")[0];
        }

        System.out.println("Weather Information:");
        System.out.println("Temperature: " + temperature + "°F");
        System.out.println("Description: " + description);
    }
}

/*
 Expected Output:

 Enter the city name: london
Weather Information:
Temperature: 282.78°F
Description: scattered clouds

*/