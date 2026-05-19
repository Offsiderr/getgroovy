package seng201.team67.services.data;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import seng201.team67.GameEnvironment;

import java.io.File;
import java.io.IOException;

public class GameSaveService {

    public void saveGame(GameEnvironment gameEnvironment, int gameSave)
    {
        ObjectMapper mapper = new ObjectMapper();

        try
        {
            mapper.writeValue(new File(Integer.toString(gameSave) + "gamesave"), gameEnvironment);

        } catch (StreamWriteException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
