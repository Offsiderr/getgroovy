package seng201.team67.services.data;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import seng201.team67.GameEnvironment;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GameSaveService {

    private static final Path SAVE_DIRECTORY =
            Paths.get(System.getProperty("user.home"), "get_groovy");
    private final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public void saveGame(GameEnvironment gameEnvironment, int gameSave)
    {
        try
        {
            Files.createDirectories(SAVE_DIRECTORY);
            File saveFile = SAVE_DIRECTORY.resolve("save" + gameSave + ".json").toFile();
            mapper.writeValue(saveFile, gameEnvironment);

        } catch (StreamWriteException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public GameEnvironment loadGame(int gameSave)
    {
        try
        {
            File saveFile = getSaveFile(gameSave);
            if (!saveFile.exists()) {
                return null;
            }
            return mapper.readValue(saveFile, GameEnvironment.class);
        } catch (IOException e) {
            return null;
        }
    }

    public boolean hasSave(int gameSave)
    {
        return getSaveFile(gameSave).exists();
    }

    private File getSaveFile(int gameSave)
    {
        return SAVE_DIRECTORY.resolve("save" + gameSave + ".json").toFile();
    }
}
