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
        this.baseDirectoryPath = this.makePathIfNotExists(FileSystemView.getFileSystemView().getHomeDirectory().toPath().resolve("Zak's Dice Game")); // Creates folder on the Desktop.
        this.userPath = this.makePathIfNotExists(this.baseDirectoryPath.resolve("userdata")); // Creates folder for storing user data.
    }

    public void cache() {
        UserCache userCache = controller.getUserCache(); // Gets the user cache for registering that a user is loaded.

        //noinspection ConstantConditions <- IntelliJ Annotation
        for (File file : this.userPath.toFile().listFiles()) { // Loops through all of the files.
            userCache.cachedUser(this.loadUser(file.getName().replace(".json", ""))); // Loads every user and registers them in the UserCache, removes .json from the file name
        }
    }

    @SneakyThrows
    public User loadUser(String username) {
        Path userPath = this.userPath.resolve(username + ".json"); // Get the user's file path.
        File userFile = userPath.toFile(); // Get the file path as a file.
        if (!userFile.exists()) { // Return null if the user has not registered. This null is handled by the UserCache
            return null;
        }
        FileReader fileReader = new FileReader(userFile); // Create all that is required to read the JSON file.
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(fileReader).getAsJsonObject();

        String password = jsonObject.get("password").getAsString(); // Gets the password of the user.
        Multiset<Integer> gameScores = HashMultiset.create(); // Creates a set for the scores of the user.
        for (JsonElement element : jsonObject.get("scores").getAsJsonArray()) { // Loops through all of the scores of the user and adds them to the set.
            gameScores.add(element.getAsInt());
        }
        return new User(username.toLowerCase(), password, gameScores); // Creates a new User object and returns it so it is added to the UserCache's Map.
    }

    @SneakyThrows
    public void saveUser(User user) {
        Path userPath = this.userPath.resolve(user.getUsername() + ".json"); // Gets the user's fine path.
        File userFile = userPath.toFile(); // Gets the file path as a File.
        Gson gson = new Gson(); // Create a Gson, for use writing to the JSON file.
        if (!userFile.exists()) { // Creates a JSON file for the user if it does not exist.
            userFile.createNewFile();
        }

        Writer writer = Files.newBufferedWriter(userPath); // Creates the Writer that will put all the data into the JSON file.
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray(); // Array that is specifically used for the user's scores.
        jsonObject.addProperty("password", user.getPassword());
        for (int score : user.getGameScores()) { // Loops through all of the user's scores and adds them to the JsonArray.
            jsonArray.add(score);
        }
        jsonObject.add("scores", jsonArray); // Adds the JsonArray to the JsonObject to be written.
        gson.toJson(jsonObject, writer); // Writes the JsonObject to the user's path using the Writer.
        writer.close(); // Ends the writer -> general code practice.
    }

    @SneakyThrows
    private Path makePathIfNotExists(Path path) {
        if (Files.exists(path) && (Files.isDirectory(path) || Files.isSymbolicLink(path))) { // Logic for checking if the path exists already.
            return path;
        }
        return Files.createDirectory(path); // Returns the path when it is made.
    }
}
