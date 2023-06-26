import com.google.gson.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class JsonPlaceholderClient {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static void createUser() {
        try {
            URL url = new URL(BASE_URL + "/users");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            String requestBody = "{\"name\":\"John Doe\",\"username\":\"johndoe\",\"email\":\"johndoe@example.com\"}";

            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(requestBody.getBytes());
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                System.out.println("Created user:");
                System.out.println(response.toString());
            } else {
                System.out.println("Failed to create user. Response Code: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateUser(int userId) {
        try {
            URL url = new URL(BASE_URL + "/users/" + userId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");

            String requestBody = "{\"name\":\"Updated Name\",\"username\":\"updatedusername\",\"email\":\"updatedemail@example.com\"}";

            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(requestBody.getBytes());
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                System.out.println("Updated user:");
                System.out.println(response.toString());
            } else {
                System.out.println("Failed to update user. Response Code: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteUser(int userId) {
        try {
            URL url = new URL(BASE_URL + "/users/" + userId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("User with ID " + userId + " has been deleted.");
            } else {
                System.out.println("Failed to delete user. Response Code: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printUsers() {
        try {
            URL url = new URL(BASE_URL + "/users");
            printInfo(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printUser(int userId) {
        try {
            URL url = new URL(BASE_URL + "/users/" + userId);
            printInfo(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printUser(String username) {
        try {
            URL url = new URL(BASE_URL + "/users?username=" + username);
            printInfo(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printInfo(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
            reader.close();

            System.out.println("Users:");
            System.out.println(response.toString());
        } else {
            System.out.println("Failed to fetch users. Response Code: " + responseCode);
        }

        connection.disconnect();
    }

    public static void saveLatestPostComments(int userId) {
        try {
            int postId = findLatestPostId(userId);
            URL url = new URL(BASE_URL + "/posts/" + postId + "/comments");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new FileWriter("resources//user-" + userId + "-post-" + postId + "-comments.json"));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    writer.write(line + "\n");
                    response.append(line).append("\n");
                }
                reader.close();
                writer.close();

                System.out.println("Post comments:");
                System.out.println(response.toString());
            } else {
                System.out.println("Failed to fetch users. Response Code: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int findLatestPostId(int userId) throws IOException {
        URL url = new URL(BASE_URL + "/users/" + userId + "/posts");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int maxId = -1;
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
            reader.close();

            JsonParser parser = new JsonParser();
            JsonArray jsonArray = parser.parse(response.toString()).getAsJsonArray();

            for (JsonElement element : jsonArray) {
                int id = element.getAsJsonObject().get("id").getAsInt();
                if (id > maxId) {
                    maxId = id;
                }
            }
        } else {
            System.out.println("Failed to fetch users. Response Code: " + responseCode);
        }
        connection.disconnect();
        return maxId;
    }

    public static void printOpenTasks(int userId) {
        try {
            URL url = new URL(BASE_URL + "/users/" + userId + "/todos");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line).append("\n");
                }
                reader.close();

                JsonParser parser = new JsonParser();
                JsonArray jsonArray = parser.parse(response.toString()).getAsJsonArray();

                for (JsonElement element : jsonArray) {
                    boolean completed = element.getAsJsonObject().get("completed").getAsBoolean();
                    if (!completed) {
                        System.out.println(element.toString());
                    }
                }
            } else {
                System.out.println("Failed to fetch users. Response Code: " + responseCode);
            }
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
