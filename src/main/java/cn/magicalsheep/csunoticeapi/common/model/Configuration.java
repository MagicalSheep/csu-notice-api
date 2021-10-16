package cn.magicalsheep.csunoticeapi.common.model;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

public class Configuration extends Properties {

    private static final String comment = "CSU Notice API Configuration\nCreated By MagicalSheep";
    private static final String path = System.getProperty("user.dir") +
            System.getProperty("file.separator") + "settings.properties";
    private static final Configuration configuration = new Configuration();

    public static void init() {
        init(configuration);
    }

    private static void init(Configuration config) {
        config.setProperty("user_name", "8200000000");
        config.setProperty("password", "example password");
        config.setProperty("school_notice_url", "http://tz.its.csu.edu.cn");
        config.setProperty("cse_notice_url", "https://cse.csu.edu.cn/index/tzgg.htm");
        config.setProperty("headmaster_mail_url", "http://oa.its.csu.edu.cn/webserver/mailbox/MailList_Pub.aspx");
        String os = System.getProperty("os.name");
        if (os.startsWith("Windows"))
            config.setProperty("chrome_path", "C:/Program Files/Google/Chrome/Application/chrome.exe");
        else if (os.startsWith("Linux"))
            config.setProperty("chrome_path", "/usr/bin/google-chrome");
        else if (os.startsWith("Mac"))
            config.setProperty("chrome_path", "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome");
        config.setProperty("max_thread_nums", "2");
        config.setProperty("update_pages_num", "5");
        config.setProperty("initialization", "false");
        config.setProperty("update_school_notice", "true");
        config.setProperty("update_cse_notice", "false");
        config.setProperty("update_headmaster_mail", "false");
        config.setProperty("token", "example token");
    }

    public static void save() throws IOException {
        configuration.store(new FileWriter(path), comment);
    }

    public static void load() throws Exception {
        configuration.load(new FileReader(path));
        validate();
    }

    public static String getPath() {
        return path;
    }

    public static int getIntegerProperties(String key) {
        return Integer.parseInt(configuration.getProperty(key));
    }

    public static boolean getBooleanProperties(String key) {
        return Boolean.parseBoolean(configuration.getProperty(key));
    }

    public static String getProperties(String key) {
        return configuration.getProperty(key);
    }

    public static void setProperties(String key, String value) {
        configuration.setProperty(key, value);
    }

    public static void setProperties(String key, int value) {
        configuration.setProperty(key, String.valueOf(value));
    }

    public static void setProperties(String key, boolean value) {
        configuration.setProperty(key, String.valueOf(value));
    }

    private static void validate() throws Exception {
        Configuration raw = new Configuration();
        init(raw);
        final HashSet<String> keys = new HashSet<>(raw.stringPropertyNames());
        Set<String> x = configuration.stringPropertyNames();
        for (String key : keys) {
            if (!x.contains(key))
                throw new Exception("Missing setting field: " + key);
        }
        for (String key : keys) {
            if (configuration.getProperty(key).isEmpty())
                throw new Exception("Setting field " + key + " is empty");
        }
        String value = configuration.getProperty("max_thread_nums");
        if (isNotInteger(value))
            throw new Exception("Setting field max_thread_nums is not a positive number");
        if (Integer.parseInt(value) <= 0)
            throw new Exception("Setting field max_thread_nums is not a positive number");
        value = configuration.getProperty("update_pages_num");
        if (isNotInteger(value))
            throw new Exception("Setting field update_pages_num is not a valid number");
        if (Integer.parseInt(value) < 0)
            throw new Exception("Setting field update_pages_num is not a valid number");
        value = configuration.getProperty("initialization");
        if (!(value.equals("false") || value.equals("true")))
            throw new Exception("Setting field initialization is not a valid value");
        value = configuration.getProperty("update_school_notice");
        if (!(value.equals("false") || value.equals("true")))
            throw new Exception("Setting field update_school_notice is not a valid value");
        value = configuration.getProperty("update_cse_notice");
        if (!(value.equals("false") || value.equals("true")))
            throw new Exception("Setting field update_cse_notice is not a valid value");
        value = configuration.getProperty("school_notice_url");
        if (!value.startsWith("http"))
            throw new Exception("Setting field school_notice_url is not a valid value");
        value = configuration.getProperty("cse_notice_url");
        if (!value.startsWith("http"))
            throw new Exception("Setting field cse_notice_url is not a valid value");
    }

    private static boolean isNotInteger(String str) {
        Pattern pattern = Pattern.compile("^[-+]?[\\d]*$");
        return !pattern.matcher(str).matches();
    }
}
