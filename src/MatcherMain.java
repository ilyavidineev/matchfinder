import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class MatcherMain {

    private static Map<String, ArrayList<WordPosition>> resultMap = new ConcurrentHashMap<>();

    private static final ArrayList<String> words = new ArrayList<>(Arrays.asList("James", "John", "Robert", "Michael",
            "William", "David", "Richard", "Charles", "Joseph", "Thomas", "Christopher", "Daniel", "Paul", "Mark",
            "Donald", "George", "Kenneth", "Steven", "Edward", "Brian", "Ronald", "Anthony", "Kevin", "Jason",
            "Matthew", "Gary", "Timothy", "Jose", "Larry", "Jeffrey", "Frank", "Scott", "Eric", "Stephen", "Andrew",
            "Raymond", "Gregory", "Joshua", "Jerry", "Dennis", "Walter", "Patrick", "Peter", "Harold", "Douglas",
            "Henry", "Carl", "Arthur", "Ryan", "Roger"));


    public static void main(String[] args) throws Exception {

        long timeOfStart = System.currentTimeMillis();
        BufferedReader reader = new BufferedReader(new FileReader(
                "/Users/ilia/IdeaProjects/matchers/src/big.txt")); //TODO: Change path to your file

        List<String> stringsVolume = new ArrayList<>();
        int currentLineOffset = 0;

        while (reader.ready()) {
            stringsVolume.add(reader.readLine());
            currentLineOffset++;

            if (currentLineOffset % 1000 == 0 || !reader.ready()) {
                findMatches(stringsVolume, currentLineOffset);
            }
        }
        printResultMap(resultMap);

        reader.close();
        long timeOfFinish = System.currentTimeMillis();
        System.out.println("Execution time: " + (double) (timeOfFinish - timeOfStart) / 1000);
    }

    private static void findMatches(List<String> stringsVolume, int currentLineOffset) throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        HashMap<String, MatcherTask> taskList = new HashMap<>();

        String string = String.join("", stringsVolume);

        for (String word : words) {
            taskList.put(word, new MatcherTask(word, string, currentLineOffset - 1000));
        }
        for (Map.Entry<String, MatcherTask> entry : taskList.entrySet()) {
            service.submit(entry.getValue());
        }
        for (String word : words) {
            addToResultMap(word, taskList.get(word).call());
        }
        stringsVolume.clear();
        service.shutdown();
    }

    private static void addToResultMap(String word, ArrayList<WordPosition> wordPositions) {
        if (!resultMap.containsKey(word)) {
            resultMap.put(word, wordPositions);
        } else {
            resultMap.get(word).addAll(wordPositions);
        }
    }

    private static void printResultMap(Map<String, ArrayList<WordPosition>> result) {
        for (Map.Entry<String, ArrayList<WordPosition>> entry : result.entrySet()) {

            if (entry.getValue().size() != 0) {
                String matchesString = String.join(", ", entry.getValue().subList(0, entry.getValue().size()).toString());
                System.out.printf("%s --> %s\n", entry.getKey(), matchesString);
            }
        }
    }
}

