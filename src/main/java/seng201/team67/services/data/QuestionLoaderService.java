package seng201.team67.services.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import seng201.team67.models.questionmodels.Question;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides question loader operations for the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class QuestionLoaderService {

    //We need to load, common and tour specific questions here.
    //we use Jackson as we do with the artists.

    //Questions are loaded ONCE instead of for every concert.

    /** Text value for the common events resource path. */
    private static final String commonEventsResourcePath = "/data/events/common.json";
    /** Text value for the local events resource path. */
    private static final String localEventsResourcePath = "/data/events/local.json";
    /** Text value for the country events resource path. */
    private static final String countryEventsResourcePath = "/data/events/country.json";
    /** Text value for the world events resource path. */
    private static final String worldEventsResourcePath = "/data/events/world.json";

    /**
     * Loads the event pool.
     * It updates related state as needed while performing the operation.
     * @param type the text value for the type
     * @return A list containing the event pool.
     */
    public ArrayList<Question> loadEventPool(String type) {
        ArrayList<Question> pool = new ArrayList<>();

        switch (type)
        {
            case "common" -> pool.addAll(loadAll(commonEventsResourcePath));
            case "local" -> pool.addAll(loadAll(localEventsResourcePath));
            case "country" -> pool.addAll(loadAll(countryEventsResourcePath));
            case "world" -> pool.addAll(loadAll(worldEventsResourcePath));
        }
        Collections.shuffle(pool);
        return pool;
    }

    private ArrayList<Question> loadAll(String resourcePath) {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new RuntimeException("Event file not found at " + resourcePath);
            }
            /**
             * Represents the value used by the game.
             * @author Louie Campion
             * @author Keenan Aubrey
             */
            return mapper.readValue(is, new TypeReference<ArrayList<Question>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to load events from " + resourcePath + ": " + e.getMessage(), e);
        }
    }
}
