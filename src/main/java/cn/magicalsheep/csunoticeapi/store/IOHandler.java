package cn.magicalsheep.csunoticeapi.store;

import cn.magicalsheep.csunoticeapi.Factory;
import cn.magicalsheep.csunoticeapi.model.Configuration;
import com.google.gson.Gson;

import java.io.*;

public class IOHandler {

    private static final File file = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "settings.json");
    private static final Gson gson = new Gson();

    public static Configuration loadConf() throws IOException {
        if (!file.exists()) {
            Factory.setFirstRun(true);
            Configuration configuration = initConf();
            saveConf(configuration);
            return configuration;
        }
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String tmp;
        StringBuilder json = new StringBuilder();
        while ((tmp = bufferedReader.readLine()) != null) json.append(tmp);
        return gson.fromJson(json.toString(), Configuration.class);
    }

    public static void saveConf(Configuration configuration) throws IOException {
        if (!file.exists()) initConf();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(gson.toJson(configuration));
        bufferedWriter.close();
    }

    public static Configuration initConf() throws IOException {
        if (file.createNewFile()) {
            Configuration configuration = new Configuration();
            configuration.setUser("exampleUser");
            configuration.setPwd("examplePassword");
            configuration.setRoot_uri("http://tz.its.csu.edu.cn");
            return configuration;
        } else {
            throw new IOException("Create file failed");
        }
    }
}
