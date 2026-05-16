package seng201.team67.models.enums;

public enum RandomEvent {

    VOCAL_STRAIN("Vocal Strain",
            "The relentless tour schedule has taken a toll. This artist's stamina takes a hit.",
            40, EventType.STAT, -20, "stamina"),

    GYM_ARC("Gym Arc",
            "The artist spent their days off working on their fitness. Stamina increases slightly.",
            30, EventType.STAT, 15, "stamina"),

    BAD_PRESS("Bad Press",
            "A tabloid expose has damaged the artist's public image. Star power has dropped.",
            20, EventType.STAT, -1, "star_power"),

    VIRAL_MOMENT("Viral Moment",
            "A clip from the last concert exploded online. The artist's star power surges.",
            10, EventType.STAT, 2, "star_power"),

    FAN_INSPIRATION("Fan Inspiration",
            "A heartfelt conversation with a superfan has reignited the artist's passion. +1 to their skill.",
            40, EventType.SKILL, 1, null),

    STUDIO_SESSION("Studio Session",
            "A veteran producer sat in on a private session and shared some trade secrets. +1 to a skill.",
            30, EventType.SKILL, 1, null),

    CREATIVE_BREAKTHROUGH("Creative Breakthrough",
            "Somewhere between cities, inspiration struck. The artist has refined their craft. +1 to a skill.",
            20, EventType.SKILL, 1, null),

    LEGENDARY_MENTORSHIP("Legendary Mentorship",
            "A legendary industry figure has taken notice and offered personal mentorship. +2 to a skill.",
            10, EventType.SKILL, 2, null),

    AMICABLE_EXIT("Amicable Exit",
            "The artist has decided to step back from touring to focus on their personal life. They leave on good terms.",
            40, EventType.RETIREMENT, 500, null),

    BURNOUT("Burnout",
            "The grind has become too much. The artist announces they're 'taking a break from the industry' indefinitely.",
            30, EventType.RETIREMENT, 250, null),

    RIVAL_LABEL_POACHING("Rival Label Poaching",
            "A rival label made an offer too good to refuse. The artist has signed elsewhere — effective immediately.",
            20, EventType.RETIREMENT, 100, null),

    WENT_OFF_GRID("Went Off Grid",
            "No calls, no texts. The artist has deleted all their socials and vanished. No refund forthcoming.",
            10, EventType.RETIREMENT, 0, null);

    private final String name;

    private final String description;

    private final int weight;

    private final EventType type;

    private final int value;

    private final String stat;

    RandomEvent(String name, String description, int weight, EventType type, int value, String stat) {
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.type = type;
        this.value = value;
        this.stat = stat;
    }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public int getWeight() { return weight; }

    public EventType getType() { return type; }

    public int getValue() { return value; }

    public String getStat() { return stat; }
}
