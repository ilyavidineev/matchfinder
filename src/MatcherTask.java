import java.util.ArrayList;
import java.util.concurrent.Callable;

class MatcherTask implements Callable<ArrayList<WordPosition>> {
    private final String word;
    private final String string;
    private final int lineNumber;

    public MatcherTask(String word, String string, int lineNumber) {
        this.word = word;
        this.string = string;
        this.lineNumber = lineNumber;
    }

    public ArrayList<WordPosition> call() throws Exception {

        ArrayList<WordPosition> positionsList = new ArrayList<>();

        int offset;
        int lastIndex = 0;

        while (true) {
            offset = string.indexOf(word, lastIndex);
            if (offset != -1) {
                positionsList.add(new WordPosition(lineNumber, offset));
                lastIndex = offset + 1;

            } else break;
        }
        return positionsList;
    }
}
