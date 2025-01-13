package bg.sofia.uni.fmi.mjt.netflix;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public record Content(String id, String title, ContentType type, String description, int releaseYear, int runtime,
                      List<String> genres, int seasons, String imdbId, double imdbScore, double imdbVotes) {
    private static final String ATTRIBUTE_DELIMITER = ",";

    public static Content of(String line) {
        StringTokenizer tokens = new StringTokenizer(line, ATTRIBUTE_DELIMITER);

        return new Content(tokens.nextToken(), tokens.nextToken(), ContentType.valueOf(tokens.nextToken()),
            tokens.nextToken(), Integer.parseInt(tokens.nextToken()), Integer.parseInt(tokens.nextToken()),
            Arrays.stream(tokens.nextToken().replace("['", "").replace("']", "").split("'; '")).toList(),
            Integer.parseInt(tokens.nextToken()), tokens.nextToken(),
            Double.parseDouble(tokens.nextToken()), Double.parseDouble(tokens.nextToken()));
    }
}
