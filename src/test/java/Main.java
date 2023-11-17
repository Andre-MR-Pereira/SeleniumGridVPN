import java.net.MalformedURLException;
import java.util.Dictionary;
import java.util.Hashtable;

public class Main {
    public static Dictionary<String, String> flags = new Hashtable<>();

    public static void main(String[] args) throws MalformedURLException {
        //Section: extracting CLI arguments
        for (int i = 0; i < args.length; i++) {
            if ("-c".equals(args[i]) || "--ChromePath".equals(args[i])) {
                if (i + 1 < args.length) {
                    flags.put("Chrome",args[++i]);
                } else {
                    System.err.println("Error: missing path for Chrome browser");
                    System.exit(1);
                }
            } else if ("-f".equals(args[i]) || "--FirefoxPath".equals(args[i])) {
                if (i + 1 < args.length) {
                    flags.put("Firefox",args[++i]);
                } else {
                    System.err.println("Error: missing path for Firefox browser");
                    System.exit(1);
                }
            } else if ("-v".equals(args[i]) || "--VPN".equals(args[i])) {
                if (i + 1 < args.length) {
                    flags.put("VPN",args[++i]);
                } else {
                    System.err.println("Error: missing VPN status");
                    System.exit(1);
                }
            }
            else {
                System.err.println("Error: unrecognized argument: " + args[i]);
                System.exit(1);
            }
        }
        System.out.println("The Dictionary is: " + flags);


    }

    private static void bannerMessage(String message, String color){
        colorPicker(color);
        System.out.println("#####################################");
        System.out.println(message);
        System.out.println("#####################################");
        System.out.println((char)27 + "[39m");
    }

    private static void bannerMessage(String messageTop, String message, String color){
        System.out.println((char) 27 + "[36m" + messageTop);
        colorPicker(color);
        System.out.println("#####################################");
        System.out.println(message);
        System.out.println("#####################################");
        System.out.println((char)27 + "[39m");
    }

    private static void colorPicker(String color){
        switch (color) {
            case "green", "G" -> System.out.println((char) 27 + "[32m");
            case "blue", "B" -> System.out.println((char) 27 + "[34m");
            case "yellow", "Y" -> System.out.println((char) 27 + "[33m");
            case "magenta", "M" -> System.out.println((char) 27 + "[35m");
            case "cyan", "C" -> System.out.println((char) 27 + "[36m");
            default -> System.out.println((char) 27 + "[31m");
        }
    }
}