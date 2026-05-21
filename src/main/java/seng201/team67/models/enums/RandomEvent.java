package seng201.team67.models.enums;

/**
 * Represents the available random events that can occur in the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public enum RandomEvent {

    /** The vocal strain. */
    VOCAL_STRAIN("Vocal Strain",
            "The relentless tour schedule has taken a toll. This artist's stamina takes a hit.",
            40, EventType.STAT, -20, "stamina"),

    /** The gym arc. */
    GYM_ARC("Gym Arc",
            "The artist spent their days off working on their fitness. Stamina increases slightly.",
            30, EventType.STAT, 15, "stamina"),

    /** The bad press. */
    BAD_PRESS("Bad Press",
            "A tabloid expose has damaged the artist's public image. Star power has dropped.",
            20, EventType.STAT, -1, "star_power"),

    /** The viral moment. */
    VIRAL_MOMENT("Viral Moment",
            "A clip from the last concert exploded online. The artist's star power surges.",
            10, EventType.STAT, 2, "star_power"),

    /** The fan inspiration. */
    FAN_INSPIRATION("Fan Inspiration",
            "A heartfelt conversation with a superfan has reignited the artist's passion. +1 to their skill.",
            40, EventType.SKILL, 1, null),

    /** The studio session. */
    STUDIO_SESSION("Studio Session",
            "A veteran producer sat in on a private session and shared some trade secrets. +1 to a skill.",
            30, EventType.SKILL, 1, null),

    /** The creative breakthrough. */
    CREATIVE_BREAKTHROUGH("Creative Breakthrough",
            "Somewhere between cities, inspiration struck. The artist has refined their craft. +1 to a skill.",
            20, EventType.SKILL, 1, null),

    /** The legendary mentorship. */
    LEGENDARY_MENTORSHIP("Legendary Mentorship",
            "A legendary industry figure has taken notice and offered personal mentorship. +2 to a skill.",
            10, EventType.SKILL, 2, null),

    /** The breakthrough session. */
    BREAKTHROUGH_SESSION("Breakthrough Session",
            "Something clicks after the tour, and the artist comes away with an entirely new skill.",
            2, EventType.SKILL, 0, null),

    /** The amicable exit. */
    AMICABLE_EXIT("Amicable Exit",
            "The artist has decided to step back from touring to focus on their personal life. They leave on good terms.",
            40, EventType.RETIREMENT, 500, null),

    /** The burnout. */
    BURNOUT("Burnout",
            "The grind has become too much. The artist announces they're 'taking a break from the industry' indefinitely.",
            30, EventType.RETIREMENT, 250, null),

    /** The rival label poaching. */
    RIVAL_LABEL_POACHING("Rival Label Poaching",
            "A rival label made an offer too good to refuse. The artist has signed elsewhere â€” effective immediately.",
            20, EventType.RETIREMENT, 100, null),

    /** The went off grid. */
    WENT_OFF_GRID("Went Off Grid",
            "No calls, no texts. The artist has deleted all their socials and vanished. No refund forthcoming.",
            10, EventType.RETIREMENT, 0, null);

    /** Text value for the name. */
    private final String name;

    /** Text value for the description. */
    private final String description;

    /** Numeric value for the weight. */
    private final int weight;

    /** The type. */
    private final EventType type;

    /** Numeric value for the value. */
    private final int value;

    /** Text value for the stat. */
    private final String stat;

    /**
     * Creates a new random event.
     * It initializes the state needed for the surrounding game flow.
     * @param name the name value to use
     * @param description the description text to use
     * @param weight the numeric value for the weight
     * @param type the type
     * @param value the numeric value for the value
     * @param stat the text value for the stat
     */
    RandomEvent(String name, String description, int weight, EventType type, int value, String stat) {
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.type = type;
        this.value = value;
        this.stat = stat;
    }

    /**
     * Returns the name.
     * @return The name.
     */
    public String getName() { return name; }

    /**
     * Returns the description.
     * @return The description.
     */
    public String getDescription() { return description; }

    /**
     * Returns the weight.
     * @return The weight.
     */
    public int getWeight() { return weight; }

    /**
     * Returns the type.
     * @return The type.
     */
    public EventType getType() { return type; }

    /**
     * Returns the value.
     * @return The value.
     */
    public int getValue() { return value; }

    /**
     * Returns the stat.
     * @return The stat.
     */
    public String getStat() { return stat; }
}
