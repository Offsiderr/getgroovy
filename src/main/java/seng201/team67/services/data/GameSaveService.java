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

/**
 * Provides game save operations for the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class GameSaveService {

    /** The save directory. */
    private static final Path SAVE_DIRECTORY =
            Paths.get(System.getProperty("user.home"), "get_groovy");
    /** The mapper. */
    private final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * Saves the game.
     * It updates related state as needed while performing the operation.
     * @param gameEnvironment the active game environment
     * @param gameSave the numeric value for the game save
     */
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

    /**
     * Loads the game.
     * It updates related state as needed while performing the operation.
     * @param gameSave the numeric value for the game save
     * @return The resolved game environment, or `null` if no value is available.
     */
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

    /**
     * Returns whether save.
     * @param gameSave the numeric value for the game save
     * @return True if save, otherwise false.
     */
    public boolean hasSave(int gameSave)
    {
        return getSaveFile(gameSave).exists();
    }

    private File getSaveFile(int gameSave)
    {
        return SAVE_DIRECTORY.resolve("save" + gameSave + ".json").toFile();
    }
}
