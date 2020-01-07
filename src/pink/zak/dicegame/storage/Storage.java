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
        // Creates folder on the Desktop.
        this.baseDirectoryPath = this.makePathIfNotExists(FileSystemView.getFileSystemView()
                .getHomeDirectory().toPath().resolve("Zak's Dice Game"));
        // Creates folder for storing user data.
        this.userPath = this.makePathIfNotExists(this.baseDirectoryPath.resolve("userdata"));
    }

    public void cache() {
        // Gets the user cache for registering that a user is loaded.
        UserCache userCache = controller.getUserCache();

        // Loops through all of the files.
        //noinspection ConstantConditions <- IntelliJ annotation
        for (File file : this.userPath.toFile().listFiles()) {
            // Loads every user and registers them in the UserCache, removes .json from the file name
            userCache.cachedUser(this.loadUser(file.getName().replace(".json", "")));
        }
    }

    @SneakyThrows
    public User loadUser(String username) {
        // Get the user's file path.
        Path userPath = this.userPath.resolve(username + ".json");
        // Get the file path as a file.
        File userFile = userPath.toFile();
        // Return null if the user has not registered. This null is handled by the UserCache.
        if (!userFile.exists()) {
            return null;
        }
        // Create all that is required to read the JSON file.
        FileReader fileReader = new FileReader(userFile);
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(fileReader).getAsJsonObject();

        // Gets the password of the user.
        String password = jsonObject.get("password").getAsString();
        // Creates a set for the scores of the user.
        Multiset<Integer> gameScores = HashMultiset.create();
        // Loops through all of the scores of the user and adds them to the set.
        for (JsonElement element : jsonObject.get("scores").getAsJsonArray()) {
            gameScores.add(element.getAsInt());
        }
        // Creates a new User object and returns it so it is added to the UserCache's Map.
        return new User(username.toLowerCase(), password, gameScores);
    }

    @SneakyThrows
    public void saveUser(User user) {
        // Gets the user's file path.
        Path userPath = this.userPath.resolve(user.getUsername() + ".json");
        // Gets the file path as a File.
        File userFile = userPath.toFile();
        // Create a Gson, for use writing to the JSON file.
        Gson gson = new Gson();
        // Creates a JSON file for the user if it does not exist.
        if (!userFile.exists()) {
            userFile.createNewFile();
        }

        // Creates the Writer that will put all the data into the JSON file.
        Writer writer = Files.newBufferedWriter(userPath);
        JsonObject jsonObject = new JsonObject();
        // Array that is specifically used for the user's scores.
        JsonArray jsonArray = new JsonArray();
        jsonObject.addProperty("password", user.getPassword());
        // Loops through all of the user's scores and adds them to the JsonArray.
        for (int score : user.getGameScores()) {
            jsonArray.add(score);
        }
        // Adds the JsonArray to the JsonObject to be written.
        jsonObject.add("scores", jsonArray);
        // Writes the JsonObject to the user's path using the Writer.
        gson.toJson(jsonObject, writer);
        // Ends the writer -> general code practice.
        writer.close();
    }

    @SneakyThrows
    private Path makePathIfNotExists(Path path) {
        // Logic for checking if the path exists already.
        if (Files.exists(path) && (Files.isDirectory(path) || Files.isSymbolicLink(path))) {
            return path;
        }
        // Returns the path when it is made.
        return Files.createDirectory(path);
    }
}
