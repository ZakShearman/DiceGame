package pink.zak.dicegame.storage;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.gson.*;
import lombok.SneakyThrows;
import pink.zak.dicegame.Controller;
import pink.zak.dicegame.cache.UserCache;
import pink.zak.dicegame.objects.User;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileReader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class Storage {
    private final Controller controller;
    private final Path baseDirectoryPath;
    private final Path userPath;

    public Storage(Controller controller) {
        this.controller = controller;
        this.baseDirectoryPath = this.makePathIfNotExists(FileSystemView.getFileSystemView().getHomeDirectory().toPath().resolve("Zak's Dice Game"));
        this.userPath = this.makePathIfNotExists(this.baseDirectoryPath.resolve("userdata"));
    }

    public void cache() {
        UserCache userCache = controller.getUserCache();
        File[] userFiles = this.userPath.toFile().listFiles();

        if (userFiles == null) {
            return;
        }
        //noinspection ConstantConditions
        for (File file : this.userPath.toFile().listFiles()) {
            userCache.cachedUser(this.loadUser(file.getName().replace(".json", "")));
        }
    }

    @SneakyThrows
    public User loadUser(String username) {
        System.out.println("LOADING USER " + username);
        Path userPath = this.userPath.resolve(username + ".json");
        File userFile = userPath.toFile();
        if (!userFile.exists()) {
            return null;
        }
        FileReader fileReader = new FileReader(userFile);
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(fileReader).getAsJsonObject();

        String password = jsonObject.get("password").getAsString();
        Multiset<Integer> gameScores = HashMultiset.create();
        for (JsonElement element : jsonObject.get("scores").getAsJsonArray()) {
            gameScores.add(element.getAsInt());
            System.out.println("User: " + username + " game score added: " + element.getAsInt());
        }
        System.out.println("User: " + username + " password is " + password);
        return new User(username.toLowerCase(), password, gameScores);
    }

    @SneakyThrows
    public void saveUser(User user) {
        Path userPath = this.userPath.resolve(user.getUsername() + ".json");
        File userFile = userPath.toFile();
        Gson gson = new Gson();
        if (!userFile.exists()) {
            userFile.createNewFile();
        }

        Writer writer = Files.newBufferedWriter(userPath);
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        jsonObject.addProperty("password", user.getPassword());
        for (int score : user.getGameScores()) {
            jsonArray.add(score);
        }
        jsonObject.add("scores", jsonArray);
        gson.toJson(jsonObject, writer);
        writer.close();
    }

    @SneakyThrows
    private Path makePathIfNotExists(Path path) {
        if (Files.exists(path) && (Files.isDirectory(path) || Files.isSymbolicLink(path))) {
            return path;
        }
        Files.createDirectory(path);

        return path;
    }
}
