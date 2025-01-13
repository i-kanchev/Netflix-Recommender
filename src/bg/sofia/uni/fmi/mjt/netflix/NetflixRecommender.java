package bg.sofia.uni.fmi.mjt.netflix;

import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NetflixRecommender {

    private final static double SENSITIVITY_THRESHOLD = 10_000;
    private List<Content> contentList;

    /**
     * Loads the dataset from the given {@code reader}.
     *
     * @param reader Reader from which the dataset can be read.
     */
    public NetflixRecommender(Reader reader) {
        this.contentList = new ArrayList<>();

        var scanner = new Scanner(reader);
        scanner.nextLine();

        while (scanner.hasNextLine()) {
            this.contentList.add(Content.of(scanner.nextLine()));
        }
    }

    /**
     * Returns all movies and shows from the dataset in undefined order as an unmodifiable List.
     * If the dataset is empty, returns an empty List.
     *
     * @return the list of all movies and shows.
     */
    public List<Content> getAllContent() {
        return Collections.unmodifiableList(contentList);
    }

    /**
     * Returns a list of all unique genres of movies and shows in the dataset in undefined order.
     * If the dataset is empty, returns an empty List.
     *
     * @return the list of all genres
     */
    public List<String> getAllGenres() {
        return contentList.stream()
            .flatMap(content -> content.genres().stream())
            .distinct()
            .toList();
    }

    /**
     * Returns the movie with the longest duration / run time. If there are two or more movies
     * with equal maximum run time, returns any of them. Shows in the dataset are not considered by this method.
     *
     * @return the movie with the longest run time
     * @throws NoSuchElementException in case there are no movies in the dataset.
     */
    public Content getTheLongestMovie() {
        return contentList.stream()
            .filter(content -> content.type() == ContentType.MOVIE)
            .max(Comparator.comparingInt(Content::runtime))
            .orElseThrow(() -> new NoSuchElementException("No movies in the dataset"));
    }

    /**
     * Returns a breakdown of content by type (movie or show).
     *
     * @return a Map with key: a ContentType and value: the set of movies or shows on the dataset, in undefined order.
     */
    public Map<ContentType, Set<Content>> groupContentByType() {
        return contentList.stream()
            .collect(Collectors.groupingBy(Content::type, Collectors.toSet()));
    }

    /**
     * Returns the top N movies and shows sorted by weighed IMDB rating in descending order.
     * If there are fewer movies and shows than {@code n} in the dataset, return all of them.
     * If {@code n} is zero, returns an empty list.
     * <p>
     * The weighed rating is calculated by the following formula:
     * Weighted Rating (WR) = (v ÷ (v + m)) × R + (m ÷ (v + m)) × C
     * where
     * R is the content's own average rating across all votes. If it has no votes, its R is 0.
     * C is the average rating of content across the dataset
     * v is the number of votes for a content
     * m is a tunable parameter: sensitivity threshold. In our algorithm, it's a constant equal to 10_000.
     * <p>
     * Check https://stackoverflow.com/questions/1411199/what-is-a-better-way-to-sort-by-a-5-star-rating for details.
     *
     * @param n the number of the top-rated movies and shows to return
     * @return the list of the top-rated movies and shows
     * @throws IllegalArgumentException if {@code n} is negative.
     */
    public List<Content> getTopNRatedContent(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Number should be positive");
        }

        double average = contentList.stream()
            .mapToDouble(Content::imdbScore)
            .average()
            .orElse(0);

        return contentList.stream()
            .sorted((c1, c2) -> Double.compare(
                (c2.imdbVotes() / (c2.imdbVotes() + SENSITIVITY_THRESHOLD) * c2.imdbScore()
                    + (SENSITIVITY_THRESHOLD / (c2.imdbVotes() + SENSITIVITY_THRESHOLD)) * average),
                (c1.imdbVotes() / (c1.imdbVotes() + SENSITIVITY_THRESHOLD) * c1.imdbScore()
                    + (SENSITIVITY_THRESHOLD / (c1.imdbVotes() + SENSITIVITY_THRESHOLD)) * average)))
            .limit(n)
            .toList();
    }

    /**
     * Returns a list of content similar to the specified one sorted by similarity is descending order.
     * Two contents are considered similar, only if they are of the same type (movie or show).
     * The used measure of similarity is the number of genres two contents share.
     * If two contents have equal number of common genres with the specified one, their mutual oder
     * in the result is undefined.
     *
     * @param content the specified movie or show.
     * @return the sorted list of content similar to the specified one.
     */
    public List<Content> getSimilarContent(Content content) {
        return contentList.stream()
            .filter(t -> t.type() == content.type())
            .sorted((g1, g2) -> Integer.compare(
                commonGenres(g2.genres(), content.genres()),
                commonGenres(g1.genres(), content.genres())))
            .toList();
    }

    /**
     * Searches content by keywords in the description (case-insensitive).
     *
     * @param keywords the keywords to search for
     * @return an unmodifiable set of movies and shows whose description contains all specified keywords.
     */
    public Set<Content> getContentByKeywords(String... keywords) {
        return contentList.stream()
            .filter(content -> includeKeywords(content.description(), keywords))
            .collect(Collectors.toUnmodifiableSet());
    }

    private int commonGenres(List<String> g1, List<String> g2) {
        int counter = 0;

        Set<String> temp = new HashSet<>(g1);

        for (String g : g2) {
            if (temp.contains(g)) {
                counter++;
            }
        }

        return counter;
    }

    private boolean includeKeywords(String description, String... keywords) {
        Set<String> words = new HashSet<>(Arrays.stream(
            description.toLowerCase().split("[\\p{IsPunctuation}\\s]+")).toList());

        for (String keyword : keywords) {
            if (!words.contains(keyword.toLowerCase())) {
                return false;
            }
        }

        return true;
    }
}