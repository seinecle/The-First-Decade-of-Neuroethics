/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BibTex;

import Utils.Author;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author C. Levallois
 */
public class ReferenceModifier {

    private String wk;

    public ReferenceModifier(String wk) {
        this.wk = wk;
    }

    public void runFilter(BufferedReader br, int start, int end) throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(wk + "fileTimeRangeFiltered_" + start + "_" + end + ".bib"));

        String currLine;
        StringBuffer sb = new StringBuffer();
        String extractedDate;
        boolean yearFound = false;
        boolean match;
        HashSet<String> setUuids = new HashSet();
        boolean isNewUuid = true;

        while ((currLine = br.readLine()) != null) {
//            if (currLine.contains("Where Brain, Body and World Collide")) {
//                System.out.println("here!");
//            }
            currLine = currLine.replace("\"", "'");
            currLine = currLine.replaceAll("(^[^\\@].*)(\\@)(.*)", "$1\\\\@$3");
//            System.out.println("currLine: "+currLine);
            currLine = currLine.replace("\\{", "\"");
            if (currLine.length() > 1) {
                currLine = currLine.replace("}", "\"");
            }

//            System.out.println("currLine: "+currLine);
            //starts to record a new entry
            if (currLine.startsWith("@")) {

                sb = new StringBuffer();
                yearFound = false;
                isNewUuid = setUuids.add(currLine);
            }

            //records every line
            sb.append(currLine).append("\n");

            //tries to detect the year of the entry
            if (!yearFound) {
                match = currLine.matches(".*year.*(\\d\\d\\d\\d).*");
                //System.out.println("extractedDated: " + extractedDate);
                if (!match) {
                    continue;
                }
                extractedDate = currLine.replaceFirst(".*year.*(\\d\\d\\d\\d).*", "$1");
                if (Integer.parseInt(extractedDate) < start || Integer.parseInt(extractedDate) > end) {
                    continue;
                } else {
                    yearFound = true;
                }
            }
            if (currLine.trim().equals("}") & isNewUuid) {
                bw.write(sb.toString());
            }
        }
        bw.close();
    }

    public Set<BibTexRef> timeFilter(Set<BibTexRef> setRefs, int start, int end) {

        Iterator<BibTexRef> setRefsIterator = setRefs.iterator();
        BibTexRef ref;
        while (setRefsIterator.hasNext()) {
            ref = setRefsIterator.next();
            try {
                int year = Integer.parseInt(ref.getYear());
                if (year < start || year > end) {
                    setRefsIterator.remove();
                }
            } catch (NullPointerException e) {
                System.out.println("??: " + ref.getYear());
            }

        }
        return setRefs;

    }

    public Set<BibTexRef> authorNamesUpdater(Set<BibTexRef> setRefs, Map<Author, Author> mapEdits, AuthorCounterTracker authorCounterTracker) {

        Set<BibTexRef> setUpdatedRefs = new HashSet();
        Iterator<BibTexRef> setRefsIterator = setRefs.iterator();
        BibTexRef ref;
        List<Author> listAuthorsOriginal;
        List<Author> listAuthorsUpdated;
        Iterator<Author> listAuthorsOriginalIterator;
        Author author;
        while (setRefsIterator.hasNext()) {
            ref = setRefsIterator.next();
            listAuthorsOriginal = ref.getAuthors();
            listAuthorsUpdated = new ArrayList();
            listAuthorsOriginalIterator = listAuthorsOriginal.iterator();
            while (listAuthorsOriginalIterator.hasNext()) {
                author = listAuthorsOriginalIterator.next();
                if (mapEdits.keySet().contains(author)) {
                    //updates the author, which will be reintegrated in the setRefs
                    listAuthorsUpdated.add(mapEdits.get(author));
                    //updates the map that keeps track of authors numbers;
                    authorCounterTracker.updateTrackerWithEditedAuthor(author);
                } else {
                    listAuthorsUpdated.add(author);
                }
            }
            ref.setAuthors(listAuthorsUpdated);
            setUpdatedRefs.add(ref);

        }
        return setUpdatedRefs;

    }

    public Set<BibTexRef> idFilter(Set<BibTexRef> setRefs) throws FileNotFoundException, IOException {

        BufferedReader br = new BufferedReader(new FileReader(wk + "refs to exclude.txt"));
        String string;
        Set<String> setIdsToBeDeleted = new HashSet();
        while ((string = br.readLine()) != null) {
            setIdsToBeDeleted.add(string);
        }

        Iterator<BibTexRef> setRefsIterator = setRefs.iterator();
        BibTexRef ref;
        while (setRefsIterator.hasNext()) {
            ref = setRefsIterator.next();
            try {
                if (setIdsToBeDeleted.contains(ref.getId())) {
                    setRefsIterator.remove();
                }
            } catch (NullPointerException e) {
                System.out.println("??: " + ref.getYear());
            }

        }
        return setRefs;

    }

    public Set<BibTexRef> abstractAndKeywordsFilter(Set<BibTexRef> setRefs) throws FileNotFoundException, IOException {

        Iterator<BibTexRef> setRefsIterator = setRefs.iterator();
        BibTexRef ref;
        while (setRefsIterator.hasNext()) {
            ref = setRefsIterator.next();
//            if ((ref.getAbs() == null || ref.getAbs().isEmpty())) {
//                setRefsIterator.remove();
//            }
            if ((ref.getAbs() == null || ref.getAbs().isEmpty()) & (ref.getKeywords() == null || ref.getKeywords().isEmpty())) {
                setRefsIterator.remove();
            }
        }
        return setRefs;

    }
}
