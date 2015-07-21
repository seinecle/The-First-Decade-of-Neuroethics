/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BibTex;

import Categorization.TopicClassifier;
import ISCategories.JournalToISIConverter;
import Utils.Author;
import Utils.CloseMatch;
import com.google.common.collect.TreeMultiset;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import Utils.Clock;
import Utils.DirectedPair;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultimap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 *
 * @author C. Levallois
 */
public class Controller {

    public String wk;
    public Multiset multisetAuthors;
    public Set<CloseMatch> setCloseMatches = new HashSet();
    public Map<Author, Integer> mapAuthorsCounter = new HashMap();
//    static public Set<MapLabels> setMapLabels = new HashSet();
//    static public Map<Author, Author> mapLabels = new TreeMap();
    public Map<Author, Author> mapEdits = new TreeMap();
    public Multimap<Author, Author> mapFalsePositiveMatches = TreeMultimap.create();
    private int minOccurrenceAuthor;
    private Set<BibTexRef> refs;
    private TreeMultiset<DirectedPair<Author, Author>> pairsAuthors;
    private int start;
    private int end;

    public static void main(String[] args) throws IOException, FileNotFoundException, InvalidFormatException {

        new Controller().executeOperations();
    }

    public void executeOperations() throws FileNotFoundException, IOException, InvalidFormatException {

        wk = "H:\\files\\NESSHI\\Project with Elisabeth\\Bibliography\\20130130\\";
        String fileName = "neuroethics.bib";
        minOccurrenceAuthor = 1;
        start = 1995;
        end = 2012;

        Clock globalClock = new Clock("Global Time");

        //0. Reading the bibTexFile and populating a set of bibTexRefs
        Clock readBibTexClock = new Clock("Reading the bibTexFile and populating a set of bibTexRefs...");
        BufferedReader br1 = new BufferedReader(new FileReader(wk + fileName));
        refs = BibTexFileIO.read(br1, start, end);
        System.out.println("number of refs in the original bibtex file: " + refs.size());
        br1.close();
        readBibTexClock.closeAndPrintClock();

        //0. Initialize a map<Author,Integer> which will keep track of the counts of Authors through spelling changes
        Clock mapAuthorCounterClock = new Clock("Keeping track of author's number despite spell changes...");
        AuthorCounterTracker authorCounterTracker = new AuthorCounterTracker(mapAuthorsCounter, mapEdits);
        authorCounterTracker.countAuthorsFromRefs(refs);
//        System.out.println("count Hanfried Hemchlen: " + mapAuthorsCounter.get(new Author("Hanfried", "Helmchen")));
//        System.out.println("count H Hemchlen: " + mapAuthorsCounter.get(new Author("H", "Helmchen")));
        mapAuthorCounterClock.closeAndPrintClock();

        // 1. reading the set of refs and filtering it to a range of years
        Clock filteringTimeRange = new Clock("reducing to a user-defined time range...");
        ReferenceModifier referenceModifier = new ReferenceModifier(wk);
        refs = referenceModifier.timeFilter(refs, start, end);
        filteringTimeRange.closeAndPrintClock();
        System.out.println("number of refs after time filter: " + refs.size());

        // 2. reading the set of refs and filtering out some user defined ids
        Clock filteringIds = new Clock("filtering out some user defined ids...");
        refs = referenceModifier.idFilter(refs);
        filteringIds.closeAndPrintClock();
        System.out.println("number of refs after id filters: " + refs.size());

        // 2. reading the set of refs and filtering out those that have neither an abstract nor keywords
        Clock filteringAbstracts = new Clock("filtering out refs without an abstract...");
        refs = referenceModifier.abstractAndKeywordsFilter(refs);
        filteringIds.closeAndPrintClock();
        filteringAbstracts.closeAndPrintClock();
        System.out.println("number of refs after filters on absence of keywords, abstracts: " + refs.size());

        // 3. Read user corrections from a file and populate a map of edits  and map of false positives.
        Clock userCorrectionReader = new Clock("reading corrections from a file");
        EditedLabelsIO editedLabelsIO = new EditedLabelsIO(mapEdits, wk);
        mapEdits = editedLabelsIO.populateMapEdits();
        mapFalsePositiveMatches = editedLabelsIO.populateMapFalsePositives();
        userCorrectionReader.closeAndPrintClock();

        // 4. Update the set of refs with the user corrections just made
        Clock updateSetRefsClock = new Clock("Update the set of refs with the edits just loaded");
        refs = referenceModifier.authorNamesUpdater(refs, mapEdits, authorCounterTracker);
        updateSetRefsClock.closeAndPrintClock();
//        System.out.println("count Hanfried Hemchlen: " + mapAuthorsCounter.get(new Author("Hanfried", "Helmchen")));
//        System.out.println("count H Hemchlen: " + mapAuthorsCounter.get(new Author("H", "Helmchen")));

        // 5. extracting the authors into a multiset of Authors
        Clock extractingAuthors = new Clock("extracting the set of Authors");
        multisetAuthors = SetAuthorsExtractor.extract(refs);
        extractingAuthors.closeAndPrintClock();

        // 6. Detecting probable misspellings in authors names
        Clock spellCheckClock = new Clock("Doing spell check on the set of authors");
//        setCloseMatches = new CloseMatchesDetector().check(multisetAuthors.elementSet(),mapFalsePositiveMatches);
        spellCheckClock.closeAndPrintClock();

        // 7. User correction for the misspellings
        Clock userCorrection = new Clock("Prompt the user for corrections on suggested ambiguities");
        boolean automaticSpellChecking = true;
//        UserSpellChecker userSpellChecker = new UserSpellChecker(automaticSpellChecking);
//        userSpellChecker.execute(setCloseMatches, mapFalsePositiveMatches, editedLabelsIO);
        userCorrection.closeAndPrintClock();

        // 8. Update the set of refs with the user corrections just made
        updateSetRefsClock = new Clock("Update the set of refs with the user corrections just made");
//        setRefs = referenceModifier.authorNamesUpdater(setRefs, mapEdits, authorCounterTracker);
        updateSetRefsClock.closeAndPrintClock();

        // 9. Write user corrections to a file
        Clock userCorrectionWriter = new Clock("write corrections to a file");
//        editedLabelsIO.write(mapFalsePositiveMatches);
        userCorrectionWriter.closeAndPrintClock();

        // 10. extracting the authors into a multiset of Authors
        extractingAuthors = new Clock("extracting the set of Authors");
        multisetAuthors = SetAuthorsExtractor.extract(refs);
        extractingAuthors.closeAndPrintClock();

        // 11. Populates a set of all pairs of co-authors
        Clock pairsFinderClock = new Clock("Populate a set of all pairs of co-authors");
        pairsAuthors = new PairsAuthorsExtractor().extract(refs);
        pairsFinderClock.closeAndPrintClock();

        // 12. Writes edge list for import to Gephi's datalab
        Clock printEdges = new Clock("Print edges in a file");
//        BufferedWriter bw = new BufferedWriter(new FileWriter(wk + "edgesList_" + start + "_to_" + end + ".csv"));
//        EdgesListWriter edgesListWriter = new EdgesListWriter();
//        edgesListWriter.write(pairsAuthors, bw, minOccurrenceAuthor, mapAuthorsCounter);
        printEdges.closeAndPrintClock();

        // 13. Writes node list for import to Gephi's datalab
        Clock printNodes = new Clock("Print nodes in a file");
//        NodesListWriter nodesListWriter = new NodesListWriter();
//        bw = new BufferedWriter(new FileWriter(wk + "nodesList_" + start + "_to_" + end + ".csv"));
//        nodesListWriter.write(multisetAuthors, mapAuthorsCounter, bw, minOccurrenceAuthor);
        printNodes.closeAndPrintClock();

        // 14. Write a script which retrieves the title and year of each ref and writes a SQL query for them
        Clock printSQLQuery = new Clock("Print SQL Query for CWTS db");
//        bw = new BufferedWriter(new FileWriter(wk + "SQLQuery" + start + "_to_" + end + ".csv"));
//        FieldExtractor fe = new FieldExtractor(setRefs,bw);
//        fe.writeTitleAndYearToFile();
        printSQLQuery.closeAndPrintClock();

        //15. Find categories based on abstracts and analyze it in different ways.
        Clock topicClassifierClock = new Clock("classifying abstracts per topic");
        TopicClassifier topicClassifier = new TopicClassifier();
        topicClassifier.loadAbstractsFromReferences(refs);
        topicClassifier.execute();

        //write some results
        IOmethods output = new IOmethods();
        output.initialize();
        output.writeJournalsAndTheirCategories(refs, 10);
        output.writeJournalsPerCategories(refs);
        output.writePapersAndTheirCategories(refs);
        output.writeTopContributorByCategoryToFile(refs);
        output.writeNumberPapersPerYear(refs);
        output.writeCategoriesAndTheirPapersToFile(refs);
        output.writeCategoriesPerYearInCsv(refs);
        output.writeSuperCategoriesPerYearInCsv(refs);
        output.writeConnectedCategories(refs);
        
        JournalToISIConverter ISIwork = new JournalToISIConverter();
        ISIwork.convertAbbreviatedJournalsToISI(refs,null);

        topicClassifierClock.closeAndPrintClock();

        globalClock.closeAndPrintClock();

    }

    public void saveToSetCloseMatches(CloseMatch cmb) {
        setCloseMatches.add(cmb);
    }

    public int getMinOccurrenceAuthor() {
        return minOccurrenceAuthor;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public Map<Author, Integer> getMapAuthorsCounter() {
        return mapAuthorsCounter;
    }

    public void setMapAuthorsCounter(Map<Author, Integer> mapAuthorsCounter) {
        mapAuthorsCounter = mapAuthorsCounter;
    }
}
