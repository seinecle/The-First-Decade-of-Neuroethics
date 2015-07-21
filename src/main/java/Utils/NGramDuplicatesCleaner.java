///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package Utils;
//
//import Utils.Clock;
//import com.google.common.collect.HashMultiset;
//import com.google.common.collect.Multiset;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.Set;
//import org.apache.commons.lang3.StringUtils;
//
///**
// *
// * @author C. Levallois
// */
//public class NGramDuplicatesCleaner {
//
//    public HashMultiset<String> removeDuplicates(Multiset<String> setNGrams, String lang, int maxGrams) {
//
//        Set<String> stopWords = Stopwords.getStopWords(lang);
//
//        HashMultiset multisetWords = HashMultiset.create();
//
//        Iterator<Multiset.Entry<String>> itFreqList;
//        Set<String> wordsToBeRemoved = new HashSet();
//        Multiset.Entry<String> entry;
//        String currWord;
//        Set<String> setCurrentSubNGrams;
//        Iterator<String> setCurrentSubNGramsIterator;
//        String string;
//        String[] termsInBigram;
//        for (int i = maxGrams - 1; i > 0; i--) {
//            itFreqList = setNGrams.entrySet().iterator();
//            while (itFreqList.hasNext()) {
//                entry = itFreqList.next();
//                currWord = entry.getElement().trim();
//                if (StringUtils.countMatches(currWord, " ") == i) {
//                    //special condition for i = 1 since this is a very simple case that does not need a heavy duty n-gram detection approach
//                    if (i == 1) {
//                        termsInBigram = currWord.split(" ");
//                        for (int j = 0; j <= 1; j++) {
//                            string = termsInBigram[j];
//                            if (!setNGrams.contains(string)) {
//                                continue;
//                            } else if (setNGrams.count(string) < entry.getCount() * 2) {
//                                wordsToBeRemoved.add(string);
//                            }
//                        }
//
//                    } else {
//                        setCurrentSubNGrams = NGramFinderCowoVersion.ngrams(i, currWord);
//                        setCurrentSubNGramsIterator = setCurrentSubNGrams.iterator();
//                        while (setCurrentSubNGramsIterator.hasNext()) {
//                            string = setCurrentSubNGramsIterator.next().trim();
//
//                            if (!setNGrams.contains(string)) {
//                                continue;
//                            } else if (setNGrams.count(string) < entry.getCount() * 2) {
//                                wordsToBeRemoved.add(string);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        System.out.println("number of terms to be removed: " + wordsToBeRemoved.size());
//
//        itFreqList = setNGrams.entrySet().iterator();
//        while (itFreqList.hasNext()) {
//            boolean toRemain;
//            entry = itFreqList.next();
//            currWord = entry.getElement();
//            toRemain = wordsToBeRemoved.add(currWord);
//
//            if (toRemain & !stopWords.contains(currWord)) {
//                multisetWords.add(entry.getElement(), entry.getCount());
//            }
//
//        }
//
//        return multisetWords;
//
//    }
//}
