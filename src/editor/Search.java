package editor;

import javax.swing.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Search extends SwingWorker<Void, Void> {

   private final boolean WITH_REGEX;
   private Map<Integer, String> matchResults;
   private List<Integer> keys;
   private String textContent;
   private String searchText;
   private int index;

    public Search(String textContent, String searchText, boolean withRegex) {
        this.WITH_REGEX = withRegex;
        this.index = 0;
        this.textContent = textContent;
        this.searchText = searchText;
        this.matchResults = new LinkedHashMap<>(); // LinkedHashMap maintains insertion order of keys!
    }

    @Override
    protected Void doInBackground() {
        if(this.searchText != null && this.textContent != null) {
                Pattern javaPattern = Pattern.compile(searchText, Pattern.CASE_INSENSITIVE);
                Matcher matcher = javaPattern.matcher(textContent);
                while (matcher.find()) {
                    matchResults.put(matcher.start(), matcher.group());
                }
                this.keys = new ArrayList<>(matchResults.keySet());
            }
        return null;
    }

    @Override
    protected void done() {
        System.out.println("Search progress done!");
    }

    public void goNext() {
        if(keys.size() > this.index + 1) {
            this.index++;
        }
        // if caret is on last position go to the first
        else if(this.index == keys.size() -1) {
            this.index = 0;
        }
    }

    public void goPrevious() {
        if(this.index > 0) {
            this.index--;
        }
        // if caret is on first position go to the last
        else if(this.index == 0){
            this.index = keys.size() - 1;
        }
    }

    public String getMatchResult(int keyIndex) {
        return matchResults.get(keyIndex);
    }

    public int getKeyFromIndex() {
        return keys.get(this.index);
    }
}
