/**
 * Created by la0rg on 23.12.2015.
 */
public class Day10 {
    public static void main(String[] args) throws Exception {
        String str = "3113322113";
        for (int i = 0; i < 50; i++) {
            str = lookAndSay(str);
        }
        System.out.println(str.length());
    }

    static String lookAndSay(String init) throws Exception {
        if (init == null || init.length() == 0) {
            throw new Exception("object is null or empty");
        }
        char prev = init.charAt(0);
        int counter = 1;
        StringBuilder result = new StringBuilder();
        for (int i = 1; i < init.length(); i++) {
            if (init.charAt(i) == prev) {
                counter++;
            } else {
                result.append(counter).append(prev);
                prev = init.charAt(i);
                counter = 1;
            }
        }
        result.append(counter).append(prev);
        return result.toString();
    }
}
