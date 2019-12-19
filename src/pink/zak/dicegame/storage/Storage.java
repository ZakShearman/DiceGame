package pink.zak.dicegame.storage;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import pink.zak.dicegame.objects.User;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileReader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class Storage {
    private final Path baseDirectoryPath;
    private final Path userPath;

    public Storage() {
        this.baseDirectoryPath = this.makePathIfNotExists(FileSystemView.getFileSystemView().getHomeDirectory().toPath().resolve("Zak's Dice Game"));
        this.userPath = this.makePathIfNotExists(this.baseDirectoryPath.resolve("userdata"));
        this.cache();
    }

    public void cache() {
        File[] userFiles = this.userPath.toFile().listFiles();

        if (userFiles == null) {
            return;
        }
        //noinspection ConstantConditions
        for (File file : this.userPath.toFile().listFiles()) {
            this.loadUser(file.getName().replace(".json", ""));
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
        }
        return new User(username, password, gameScores);
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
        jsonObject.addProperty("password", user.getPassword());
        jsonObject.addProperty("scores", user.getGameScores().toString());
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
