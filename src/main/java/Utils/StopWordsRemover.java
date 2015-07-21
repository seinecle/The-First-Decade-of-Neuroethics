/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import com.google.common.collect.HashMultiset;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author C. Levallois
 */
public final class StopWordsRemover {

    private String entryWord;
    private boolean multipleWord;
    private boolean useScientificStopWords;
    private int minWordLength;

    private final int maxAcceptedGarbage = 3;
    private final int nbStopWords = 5000;
    private final int nbStopWordsShort = 200;

    Set<String> setStopWordsScientificOrShort;
    Set<String> setStopWordsShort;
    Set<String> setStopWordsScientific;
    Set<String> setStopWords;
    Set<String> setKeepWords;

    public StopWordsRemover(boolean useScientificStopWords, int minWordLength) {
        this.useScientificStopWords = useScientificStopWords;
        this.minWordLength = minWordLength;
        try {
            init();
        } catch (IOException ex) {
            Logger.getLogger(StopWordsRemover.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init() throws IOException {
        InputStream in10000 = StopWordsRemover.class.getResourceAsStream("stopwords_10000_most_frequent_filtered.txt");
        InputStream inscientific = StopWordsRemover.class.getResourceAsStream("scientificstopwords.txt");
        InputStream inkeep = StopWordsRemover.class.getResourceAsStream("stopwords_tokeep.txt");

        setKeepWords = new HashSet();

        BufferedReader br;
        String[] arrayTerms;

        br = new BufferedReader(new InputStreamReader(inscientific));
        arrayTerms = br.readLine().split(",");
        setStopWordsScientific.addAll(Arrays.asList(arrayTerms));
        br.close();

        br = new BufferedReader(new InputStreamReader(in10000));
        arrayTerms = br.readLine().split(",");
        arrayTerms = Arrays.copyOf(arrayTerms, nbStopWords);
        br.close();

        if (useScientificStopWords) {
            arrayTerms = ArrayUtils.addAll(arrayTerms, setStopWordsScientific.toArray(new String[setStopWordsScientific.size()]));
        }

        setStopWords.addAll(Arrays.asList(arrayTerms));

        arrayTerms = Arrays.copyOf(arrayTerms, nbStopWordsShort);

        setStopWordsShort.addAll(Arrays.asList(arrayTerms));
        if (useScientificStopWords) {
            setStopWordsScientificOrShort.addAll(setStopWordsScientific);
        }

        setStopWordsScientificOrShort.addAll(setStopWordsShort);

    }

    public boolean shouldItBeRemoved(String term) {

        boolean write = true;

        if (useScientificStopWords) {
            multipleWord = entryWord.contains(" ");

            if (multipleWord) {
                String[] wordsNGrams = entryWord.split(" ");

                for (int n = 0; n < wordsNGrams.length; n++) {

                    if (wordsNGrams[n].length() < minWordLength) {
                        write = false;
                        break;
                    }

                }

                if (wordsNGrams.length == 2
                        && ((setStopWordsScientificOrShort.contains(wordsNGrams[0].toLowerCase().trim())
                        || setStopWordsScientificOrShort.contains(wordsNGrams[1].toLowerCase().trim())))) {
                    write = false;

                }

                if (wordsNGrams.length > 2) {
                    int scoreGarbage = 0;

                    for (int i = 0; i < wordsNGrams.length; i++) {

                        if ((i == 0 | i == (wordsNGrams.length - 1)) & setStopWordsScientificOrShort.contains(wordsNGrams[i].toLowerCase().trim())) {
                            scoreGarbage = maxAcceptedGarbage + 1;
                            continue;
                        }

                        if (setStopWordsShort.contains(wordsNGrams[i].toLowerCase().trim())) {
                            scoreGarbage = scoreGarbage + 3;
                            continue;
                        }

                        if (setStopWordsScientific.contains(wordsNGrams[i].toLowerCase().trim())) {
                            scoreGarbage = scoreGarbage + 2;
                            continue;
                        }

                    }

                    if (setStopWords.contains(entryWord)) {
                        scoreGarbage = maxAcceptedGarbage + 1;
                    }

                    if (scoreGarbage > maxAcceptedGarbage) {

                        write = false;
                    }
                }

            } else {
                if (setStopWords.contains(entryWord) & !setKeepWords.contains(entryWord)) {
                    write = false;
                }
            }

            if (setKeepWords.contains(entryWord)) {
                write = true;
            }

        } else {
            String[] wordsNGrams = entryWord.split(" ");
            for (int i = 0; i < wordsNGrams.length; i++) {
                if (setStopWords.contains(wordsNGrams[i])) {
                    write = false;
                }
            }
        } //end of else block       

        if (write) {
            return false;
        } else {
            return true;
        }

    }
}
