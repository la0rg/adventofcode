import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

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
    }

    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
