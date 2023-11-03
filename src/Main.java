import components.java.classes.Driver;
import components.java.classes.pages.RTP;

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


        Driver Chrome = new Driver("Chrome",flags.get("Chrome"));
        RTP rtp = new RTP(Chrome);
        rtp.channelListing();

        rtp.accessChannel("RTP1");
        System.out.println("RTP1 is " + rtp.checkPlayerActive());
        rtp.returnMainPage();
        rtp.accessChannel("RTP3");
        System.out.println("RTP3 is " + rtp.checkPlayerActive());
        rtp.returnMainPage();
        System.out.println("Voice is " + rtp.checkEpisodesActive());
        rtp.teardown();
    }
}