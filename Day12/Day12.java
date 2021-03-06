import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * Created by la0rg on 27.12.2015.
 */
public class Day12 {
    public static void main(String[] args) throws IOException {

        String json = readFile("./Day12/Day12data.txt", StandardCharsets.US_ASCII);
        System.out.println(json);
        // part 1
        // What is the sum of all numbers in the document?
        String[] ar = json.replaceAll("[^-0-9]+", " ").trim().split(" ");
        int sum = Arrays.stream(ar).mapToInt(Integer::parseInt).sum();
        System.out.println(sum);

        // Part 2
        // Ignore any object (and all of its children) which has any property with the value "red".
        // Do this only for objects ({...}), not arrays ([...]).
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);
        System.out.println("Sum2: " + evalElement(element));
    }

    static int evalElement(JsonElement element) {
        int sum = 0;
        if (element.isJsonObject()) {
            Set<Map.Entry<String, JsonElement>> entries = element.getAsJsonObject().entrySet();
            if (entries != null) {
                for (Map.Entry<String, JsonElement> entry : entries) {
                    if (entry.getValue().isJsonPrimitive()
                            && entry.getValue().getAsJsonPrimitive().getAsString().equals("red")) {
                        return 0;
                    }
                    sum += evalElement(entry.getValue());
                }
            }
        } else if (element.isJsonArray()) {
            for (JsonElement elem : element.getAsJsonArray()) {
                sum += evalElement(elem);
            }
        } else if (element.isJsonPrimitive()) {
            if (element.getAsJsonPrimitive().isNumber()) {
                sum += element.getAsJsonPrimitive().getAsNumber().intValue();
            }
        }
        return sum;
    }

    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
