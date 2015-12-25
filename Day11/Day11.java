import java.util.LinkedList;

/**
 * Created by la0rg on 24.12.2015.
 */
public class Day11 {
    public static void main(String[] args) {
        String str = "hxbxxyzz";
        do {
            str = numberToAlpha(alphaToNumber(str) + 1);
        } while (!passRequirements(str));
        System.out.println(str);
    }

    static boolean passRequirements(String pass) {
        // Passwords must include one increasing straight of at least three letters
        boolean r1 = false;
        for (int i = 0; i < pass.length() - 2; i++) {
            if (pass.charAt(i) == pass.charAt(i + 1) - 1 &&
                    pass.charAt(i) == pass.charAt(i + 2) - 2) {
                r1 = true;
                break;
            }
        }

        // Passwords may not contain the letters i, o, or l
        boolean r2 = pass.chars().noneMatch(ch -> ch == 'i' || ch == 'o' || ch == 'l');

        // Passwords must contain at least two different, non-overlapping pairs of letters
        boolean r3 = false;
        LinkedList<Integer> indices = new LinkedList<>();
        for (int i = 0; i < pass.length() - 1; i++) {
            if (pass.charAt(i) == pass.charAt(i + 1)) {
                indices.add(i);
            }
        }
        // indices're flowing incrementally,
        // so if the first and last are far enough then found a good pair
        if (!indices.isEmpty() && indices.peekLast() - indices.peekFirst() >= 2) {
            r3 = true;
        }

        return r1 && r2 && r3;
    }

    static String numberToAlpha(long number) {
        String result = "";
        // Repeatedly divide the number to 26 and
        // convert remainder to appropriate character
        while (number > 0) {
            number--;
            long remainder = number % 26;
            char ch = (char) (remainder + 97); // shift to a character (97)
            result = ch + result;
            number = number / 26;
        }
        return result;
    }

    static long alphaToNumber(String alph) {
        long result = 0;
        String ralph = new StringBuilder(alph).reverse().toString();
        for (int i = 0; i < ralph.length(); i++) {
            result += Math.pow(26.0, i) * (ralph.charAt(i) - 96);
        }
        return result;
    }
}
