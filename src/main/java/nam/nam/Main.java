package nam.nam;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Need <github-username> to run the program");
            return;
        }

        String username = args[0];
        String apiUrl = "https://api.github.com/users/" + username + "/events";

        try {
            String json = fetch(apiUrl);
            extractAndPrintEvents(json);
        } catch (IOException e) {
            System.err.println("Could not fetch GitHub events: " + e.getMessage());
            System.exit(1);
        } catch (InterruptedException e) {
            System.err.println("Operation was interrupted.");
            System.exit(1);
        }catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            System.exit(1);
        }
    }

    // Fetch data using modern HttpClient
    private static String fetch(String apiUrl) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Accept", "application/vnd.github.v3+json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("GitHub API returned status: " + response.statusCode());
        }

        return response.body();
    }


    // Extract basic info from JSON manually
    private static void extractAndPrintEvents(String json) {
        // Split events roughly (array of JSON objects)
        String[] events = json.split("\\},\\{");

        for (String event : events) {
            if (event.contains("\"type\"")) {
                String type = extractValue(event, "\"type\":\"", "\"");
                String repo = extractValue(event, "\"name\":\"", "\"");
                String date = extractValue(event, "\"created_at\":\"", "\"");

                System.out.println("Repository: " + repo);
                System.out.println("\tEvent Type: " + type);
                System.out.println("\tDate: " + date);
                System.out.println("----------------------------");
            }
        }
    }

    // Helper: extract value between "key" and end quote
    private static String extractValue(String text, String key, String endChar) {
        int start = text.indexOf(key);
        if (start == -1) {
            return "(not found)"; // Key not present
        }

        start += key.length(); // Move to the start of the value

        int end = text.indexOf(endChar, start);
        if (end == -1) {
            return "(not found)"; // Closing character not present
        }
        return text.substring(start, end);
    }
}
