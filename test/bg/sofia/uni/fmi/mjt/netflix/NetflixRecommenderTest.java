package bg.sofia.uni.fmi.mjt.netflix;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class NetflixRecommenderTest {

    Path datasetPath;
    File datasetFile;

    private static Path tempDir;

    @BeforeAll
    public static void before() throws IOException {
        tempDir = Files.createTempDirectory(null);
    }

    @AfterAll
    public static void after() {
        tempDir.toFile().delete();
    }

    @BeforeEach
    public void setUp() {
        datasetPath = tempDir.resolve("dataset.csv");
        datasetFile = datasetPath.toFile();
    }

    @Test
    void testGetAllContent() throws IOException {
        FileWriter fileWriter = new FileWriter(datasetFile);
        BufferedWriter setUpDataset = new BufferedWriter(fileWriter);

        setUpDataset.write("id,title,type,description,release_year,runtime,genres,seasons,imdb_id,imdb_score,imdb_votes" + System.lineSeparator() +
            "tm84618,Taxi Driver,MOVIE,A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City where the perceived decadence and sleaze feed his urge for violent action.,1976,114,['drama'; 'crime'],-1,tt0075314,8.2,808582.0" + System.lineSeparator() +
            "tm154986,Deliverance,MOVIE,Intent on seeing the Cahulawassee River before it's turned into one huge lake; outdoor fanatic Lewis Medlock takes his friends on a river-rafting trip they'll never forget into the dangerous American back-country.,1972,109,['drama'; 'action'; 'thriller'; 'european'],-1,tt0068473,7.7,107673.0" + System.lineSeparator() +
            "tm127384,Monty Python and the Holy Grail,MOVIE,\"King Arthur; accompanied by his squire; recruits his Knights of the Round Table; including Sir Bedevere the Wise; Sir Lancelot the Brave; Sir Robin the Not-Quite-So-Brave-As-Sir-Lancelot and Sir Galahad the Pure. On the way; Arthur battles the Black Knight who; despite having had all his limbs chopped off; insists he can still fight. They reach Camelot; but Arthur decides not  to enter; as \"\"it is a silly place\"\".\",1975,91,['fantasy'; 'action'; 'comedy'],-1,tt0071853,8.2,534486.0");

        setUpDataset.close();

        NetflixRecommender test = new NetflixRecommender(new BufferedReader(new FileReader(datasetFile)));

        List<Content> expected = new ArrayList<>();
        expected.add(Content.of("tm84618,Taxi Driver,MOVIE,A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City where the perceived decadence and sleaze feed his urge for violent action.,1976,114,['drama'; 'crime'],-1,tt0075314,8.2,808582.0"));
        expected.add(Content.of("tm154986,Deliverance,MOVIE,Intent on seeing the Cahulawassee River before it's turned into one huge lake; outdoor fanatic Lewis Medlock takes his friends on a river-rafting trip they'll never forget into the dangerous American back-country.,1972,109,['drama'; 'action'; 'thriller'; 'european'],-1,tt0068473,7.7,107673.0"));
        expected.add(Content.of("tm127384,Monty Python and the Holy Grail,MOVIE,\"King Arthur; accompanied by his squire; recruits his Knights of the Round Table; including Sir Bedevere the Wise; Sir Lancelot the Brave; Sir Robin the Not-Quite-So-Brave-As-Sir-Lancelot and Sir Galahad the Pure. On the way; Arthur battles the Black Knight who; despite having had all his limbs chopped off; insists he can still fight. They reach Camelot; but Arthur decides not  to enter; as \"\"it is a silly place\"\".\",1975,91,['fantasy'; 'action'; 'comedy'],-1,tt0071853,8.2,534486.0"));

        assertNotSame(expected, test.getAllContent(),
            "Data not extracted properly");
    }

    @Test
    void testGetAllGenres() throws IOException {
        FileWriter fileWriter = new FileWriter(datasetFile);
        BufferedWriter setUpDataset = new BufferedWriter(fileWriter);

        setUpDataset.write("id,title,type,description,release_year,runtime,genres,seasons,imdb_id,imdb_score,imdb_votes" + System.lineSeparator() +
            "tm154986,Deliverance,MOVIE,Intent on seeing the Cahulawassee River before it's turned into one huge lake; outdoor fanatic Lewis Medlock takes his friends on a river-rafting trip they'll never forget into the dangerous American back-country.,1972,109,['drama'; 'action'; 'thriller'; 'european'],-1,tt0068473,7.7,107673.0" + System.lineSeparator() +
            "tm127384,Monty Python and the Holy Grail,MOVIE,\"King Arthur; accompanied by his squire; recruits his Knights of the Round Table; including Sir Bedevere the Wise; Sir Lancelot the Brave; Sir Robin the Not-Quite-So-Brave-As-Sir-Lancelot and Sir Galahad the Pure. On the way; Arthur battles the Black Knight who; despite having had all his limbs chopped off; insists he can still fight. They reach Camelot; but Arthur decides not  to enter; as \"\"it is a silly place\"\".\",1975,91,['fantasy'; 'action'; 'comedy'],-1,tt0071853,8.2,534486.0");

        setUpDataset.close();

        NetflixRecommender test = new NetflixRecommender(new BufferedReader(new FileReader(datasetFile)));

        List<String> expected = new ArrayList<>();
        expected.add("fantasy");
        expected.add("comedy");
        expected.add("drama");
        expected.add("action");
        expected.add("thriller");
        expected.add("european");

        assertNotSame(expected, test.getAllGenres(),
            "Wrong genres");
    }

    @Test
    void testGetTheLongestMovie() throws IOException {
        FileWriter fileWriter = new FileWriter(datasetFile);
        BufferedWriter setUpDataset = new BufferedWriter(fileWriter);

        setUpDataset.write("id,title,type,description,release_year,runtime,genres,seasons,imdb_id,imdb_score,imdb_votes" + System.lineSeparator() +
            "tm84618,Taxi Driver,MOVIE,A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City where the perceived decadence and sleaze feed his urge for violent action.,1976,114,['drama'; 'crime'],-1,tt0075314,8.2,808582.0" + System.lineSeparator() +
            "tm154986,Deliverance,MOVIE,Intent on seeing the Cahulawassee River before it's turned into one huge lake; outdoor fanatic Lewis Medlock takes his friends on a river-rafting trip they'll never forget into the dangerous American back-country.,1972,109,['drama'; 'action'; 'thriller'; 'european'],-1,tt0068473,7.7,107673.0" + System.lineSeparator() +
            "ts45948,Monty Python's Fliegender Zirkus,SHOW,Monty Python's Fliegender Zirkus consisted of two 45-minute Monty Python German television comedy specials produced by WDR for West German television. The two episodes were first broadcast in January and December 1972 and were shot entirely on film and mostly on location in Bavaria; with the first episode recorded in German and the second recorded in English and then dubbed into German.,1972,43,['comedy'],1,tt0202477,8.1,2151.0");

        setUpDataset.close();

        NetflixRecommender test = new NetflixRecommender(new BufferedReader(new FileReader(datasetFile)));

        Content expected = Content.of("tm84618,Taxi Driver,MOVIE,A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City where the perceived decadence and sleaze feed his urge for violent action.,1976,114,['drama'; 'crime'],-1,tt0075314,8.2,808582.0");

        assertEquals(expected, test.getTheLongestMovie(),
            "Wrong longest movie");
    }

    @Test
    void groupContentByType() throws IOException {
        FileWriter fileWriter = new FileWriter(datasetFile);
        BufferedWriter setUpDataset = new BufferedWriter(fileWriter);

        setUpDataset.write("id,title,type,description,release_year,runtime,genres,seasons,imdb_id,imdb_score,imdb_votes" + System.lineSeparator() +
            "tm84618,Taxi Driver,MOVIE,A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City where the perceived decadence and sleaze feed his urge for violent action.,1976,114,['drama'; 'crime'],-1,tt0075314,8.2,808582.0" + System.lineSeparator() +
            "tm154986,Deliverance,MOVIE,Intent on seeing the Cahulawassee River before it's turned into one huge lake; outdoor fanatic Lewis Medlock takes his friends on a river-rafting trip they'll never forget into the dangerous American back-country.,1972,109,['drama'; 'action'; 'thriller'; 'european'],-1,tt0068473,7.7,107673.0" + System.lineSeparator() +
            "ts45948,Monty Python's Fliegender Zirkus,SHOW,Monty Python's Fliegender Zirkus consisted of two 45-minute Monty Python German television comedy specials produced by WDR for West German television. The two episodes were first broadcast in January and December 1972 and were shot entirely on film and mostly on location in Bavaria; with the first episode recorded in German and the second recorded in English and then dubbed into German.,1972,43,['comedy'],1,tt0202477,8.1,2151.0");

        setUpDataset.close();

        NetflixRecommender test = new NetflixRecommender(new BufferedReader(new FileReader(datasetFile)));

        Map<ContentType, Set<Content>> expected = new HashMap<>();

        expected.put(ContentType.MOVIE, new HashSet<>());
        expected.put(ContentType.SHOW, new HashSet<>());

        expected.get(ContentType.MOVIE).add(Content.of("tm154986,Deliverance,MOVIE,Intent on seeing the Cahulawassee River before it's turned into one huge lake; outdoor fanatic Lewis Medlock takes his friends on a river-rafting trip they'll never forget into the dangerous American back-country.,1972,109,['drama'; 'action'; 'thriller'; 'european'],-1,tt0068473,7.7,107673.0"));
        expected.get(ContentType.SHOW).add(Content.of("ts45948,Monty Python's Fliegender Zirkus,SHOW,Monty Python's Fliegender Zirkus consisted of two 45-minute Monty Python German television comedy specials produced by WDR for West German television. The two episodes were first broadcast in January and December 1972 and were shot entirely on film and mostly on location in Bavaria; with the first episode recorded in German and the second recorded in English and then dubbed into German.,1972,43,['comedy'],1,tt0202477,8.1,2151.0"));
        expected.get(ContentType.MOVIE).add(Content.of("tm84618,Taxi Driver,MOVIE,A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City where the perceived decadence and sleaze feed his urge for violent action.,1976,114,['drama'; 'crime'],-1,tt0075314,8.2,808582.0"));

        assertNotSame(expected, test.groupContentByType(),
            "Wrong grouping");
    }

    @Test
    void testGetTopNRatedContent() throws IOException {
        FileWriter fileWriter = new FileWriter(datasetFile);
        BufferedWriter setUpDataset = new BufferedWriter(fileWriter);

        setUpDataset.write("id,title,type,description,release_year,runtime,genres,seasons,imdb_id,imdb_score,imdb_votes" + System.lineSeparator() +
            "tm84618,Taxi Driver,MOVIE,A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City where the perceived decadence and sleaze feed his urge for violent action.,1976,114,['drama'; 'crime'],-1,tt0075314,8.2,808582.0" + System.lineSeparator() +
            "tm154986,Deliverance,MOVIE,Intent on seeing the Cahulawassee River before it's turned into one huge lake; outdoor fanatic Lewis Medlock takes his friends on a river-rafting trip they'll never forget into the dangerous American back-country.,1972,109,['drama'; 'action'; 'thriller'; 'european'],-1,tt0068473,7.7,107673.0" + System.lineSeparator() +
            "tm127384,Monty Python and the Holy Grail,MOVIE,\"King Arthur; accompanied by his squire; recruits his Knights of the Round Table; including Sir Bedevere the Wise; Sir Lancelot the Brave; Sir Robin the Not-Quite-So-Brave-As-Sir-Lancelot and Sir Galahad the Pure. On the way; Arthur battles the Black Knight who; despite having had all his limbs chopped off; insists he can still fight. They reach Camelot; but Arthur decides not  to enter; as \"\"it is a silly place\"\".\",1975,91,['fantasy'; 'action'; 'comedy'],-1,tt0071853,8.2,534486.0" + System.lineSeparator() +
            "tm120801,The Dirty Dozen,MOVIE,12 American military prisoners in World War II are ordered to infiltrate a well-guarded enemy château and kill the Nazi officers vacationing there. The soldiers; most of whom are facing death sentences for a variety of violent crimes; agree to the mission and the possible commuting of their sentences.,1967,150,['war'; 'action'],-1,tt0061578,7.7,72662.0");

        setUpDataset.close();

        NetflixRecommender test = new NetflixRecommender(new BufferedReader(new FileReader(datasetFile)));

        List<Content> expected = new ArrayList<>();
        expected.add(Content.of("tm84618,Taxi Driver,MOVIE,A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City where the perceived decadence and sleaze feed his urge for violent action.,1976,114,['drama'; 'crime'],-1,tt0075314,8.2,808582.0"));
        expected.add(Content.of("tm127384,Monty Python and the Holy Grail,MOVIE,\"King Arthur; accompanied by his squire; recruits his Knights of the Round Table; including Sir Bedevere the Wise; Sir Lancelot the Brave; Sir Robin the Not-Quite-So-Brave-As-Sir-Lancelot and Sir Galahad the Pure. On the way; Arthur battles the Black Knight who; despite having had all his limbs chopped off; insists he can still fight. They reach Camelot; but Arthur decides not  to enter; as \"\"it is a silly place\"\".\",1975,91,['fantasy'; 'action'; 'comedy'],-1,tt0071853,8.2,534486.0"));

        assertArrayEquals(expected.toArray(), test.getTopNRatedContent(2).toArray(),
            "Wrong top rated");
    }

    @Test
    void testGetSimilarContent() throws IOException {
        FileWriter fileWriter = new FileWriter(datasetFile);
        BufferedWriter setUpDataset = new BufferedWriter(fileWriter);

        setUpDataset.write("id,title,type,description,release_year,runtime,genres,seasons,imdb_id,imdb_score,imdb_votes" + System.lineSeparator() +
            "tm120801,The Dirty Dozen,MOVIE,12 American military prisoners in World War II are ordered to infiltrate a well-guarded enemy château and kill the Nazi officers vacationing there. The soldiers; most of whom are facing death sentences for a variety of violent crimes; agree to the mission and the possible commuting of their sentences.,1967,150,['drama'; 'crime'],-1,tt0061578,7.7,72662.0" + System.lineSeparator() +
            "tm154986,Deliverance,MOVIE,Intent on seeing the Cahulawassee River before it's turned into one huge lake; outdoor fanatic Lewis Medlock takes his friends on a river-rafting trip they'll never forget into the dangerous American back-country.,1972,109,['drama'; 'action'; 'thriller'; 'european'],-1,tt0068473,7.7,107673.0" + System.lineSeparator() +
            "tm127384,Monty Python and the Holy Grail,MOVIE,\"King Arthur; accompanied by his squire; recruits his Knights of the Round Table; including Sir Bedevere the Wise; Sir Lancelot the Brave; Sir Robin the Not-Quite-So-Brave-As-Sir-Lancelot and Sir Galahad the Pure. On the way; Arthur battles the Black Knight who; despite having had all his limbs chopped off; insists he can still fight. They reach Camelot; but Arthur decides not  to enter; as \"\"it is a silly place\"\".\",1975,91,['fantasy'; 'action'; 'comedy'],-1,tt0071853,8.2,534486.0" + System.lineSeparator() +
            "ts45948,Monty Python's Fliegender Zirkus,SHOW,Monty Python's Fliegender Zirkus consisted of two 45-minute Monty Python German television comedy specials produced by WDR for West German television. The two episodes were first broadcast in January and December 1972 and were shot entirely on film and mostly on location in Bavaria; with the first episode recorded in German and the second recorded in English and then dubbed into German.,1972,43,['comedy'],1,tt0202477,8.1,2151.0");

        setUpDataset.close();

        NetflixRecommender test = new NetflixRecommender(new BufferedReader(new FileReader(datasetFile)));

        List<Content> expected = new ArrayList<>();
        expected.add(Content.of("tm120801,The Dirty Dozen,MOVIE,12 American military prisoners in World War II are ordered to infiltrate a well-guarded enemy château and kill the Nazi officers vacationing there. The soldiers; most of whom are facing death sentences for a variety of violent crimes; agree to the mission and the possible commuting of their sentences.,1967,150,['drama'; 'crime'],-1,tt0061578,7.7,72662.0"));
        expected.add(Content.of("tm154986,Deliverance,MOVIE,Intent on seeing the Cahulawassee River before it's turned into one huge lake; outdoor fanatic Lewis Medlock takes his friends on a river-rafting trip they'll never forget into the dangerous American back-country.,1972,109,['drama'; 'action'; 'thriller'; 'european'],-1,tt0068473,7.7,107673.0"));
        expected.add(Content.of("tm127384,Monty Python and the Holy Grail,MOVIE,\"King Arthur; accompanied by his squire; recruits his Knights of the Round Table; including Sir Bedevere the Wise; Sir Lancelot the Brave; Sir Robin the Not-Quite-So-Brave-As-Sir-Lancelot and Sir Galahad the Pure. On the way; Arthur battles the Black Knight who; despite having had all his limbs chopped off; insists he can still fight. They reach Camelot; but Arthur decides not  to enter; as \"\"it is a silly place\"\".\",1975,91,['fantasy'; 'action'; 'comedy'],-1,tt0071853,8.2,534486.0"));

        assertArrayEquals(expected.toArray(), test.getSimilarContent(Content.of("tm84618,Taxi Driver,MOVIE,A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City where the perceived decadence and sleaze feed his urge for violent action.,1976,114,['drama'; 'crime'],-1,tt0075314,8.2,808582.0")).toArray(),
            "Wrong similar content");
    }

    @Test
    void testGetContentByKeywords() throws IOException {
        FileWriter fileWriter = new FileWriter(datasetFile);
        BufferedWriter setUpDataset = new BufferedWriter(fileWriter);

        setUpDataset.write("id,title,type,description,release_year,runtime,genres,seasons,imdb_id,imdb_score,imdb_votes" + System.lineSeparator() +
            "tm84618,Taxi Driver,MOVIE,A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City where the perceived decadence and sleaze feed his urge for violent action.,1976,114,['drama'; 'crime'],-1,tt0075314,8.2,808582.0" + System.lineSeparator() +
            "tm154986,Deliverance,MOVIE,Intent on seeing the Cahulawassee River before it's turned into one huge lake; outdoor fanatic Lewis Medlock takes his friends on a river-rafting trip they'll never forget into the dangerous American back-country.,1972,109,['drama'; 'action'; 'thriller'; 'european'],-1,tt0068473,7.7,107673.0" + System.lineSeparator() +
            "tm127384,Monty Python and the Holy Grail,MOVIE,\"King Arthur; accompanied by his squire; recruits his Knights of the Round Table; including Sir Bedevere the Wise; Sir Lancelot the Brave; Sir Robin the Not-Quite-So-Brave-As-Sir-Lancelot and Sir Galahad the Pure. On the way; Arthur battles the Black Knight who; despite having had all his limbs chopped off; insists he can still fight. They reach Camelot; but Arthur decides not  to enter; as \"\"it is silly place\"\".\",1975,91,['fantasy'; 'action'; 'comedy'],-1,tt0071853,8.2,534486.0");

        setUpDataset.close();

        NetflixRecommender test = new NetflixRecommender(new BufferedReader(new FileReader(datasetFile)));

        Set<Content> expected = new HashSet<>();
        expected.add(Content.of("tm84618,Taxi Driver,MOVIE,A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City where the perceived decadence and sleaze feed his urge for violent action.,1976,114,['drama'; 'crime'],-1,tt0075314,8.2,808582.0"));
        expected.add(Content.of("tm154986,Deliverance,MOVIE,Intent on seeing the Cahulawassee River before it's turned into one huge lake; outdoor fanatic Lewis Medlock takes his friends on a river-rafting trip they'll never forget into the dangerous American back-country.,1972,109,['drama'; 'action'; 'thriller'; 'european'],-1,tt0068473,7.7,107673.0"));

        String[] keywords = {"a"};

        assertNotSame(expected, test.getContentByKeywords(keywords),
            "Data not extracted properly");
    }
}