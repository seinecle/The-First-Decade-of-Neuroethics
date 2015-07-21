/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BibTex;

import Utils.Author;
import Utils.DirectedPair;
import Utils.findAllPairsAuthorsInSet;
import com.google.common.collect.TreeMultiset;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author C. Levallois
 */
public class PairsAuthorsExtractor {

    public TreeMultiset<DirectedPair<Author, Author>> extract(Set<BibTexRef> setBibTexRefs) throws IOException {
        List<Author> listAuthors;
        TreeSet<DirectedPair<Author, Author>> currPairs;
        TreeMultiset<DirectedPair<Author, Author>> totalPairs = TreeMultiset.create();

        Iterator<BibTexRef> setBibTexRefsIterator = setBibTexRefs.iterator();
        BibTexRef bibTexRef;
        Set<Author> setAuthors;
        while (setBibTexRefsIterator.hasNext()) {
            bibTexRef = setBibTexRefsIterator.next();
            listAuthors = bibTexRef.getAuthors();
            setAuthors = new HashSet();
            setAuthors.addAll(listAuthors);
            currPairs = findAllPairsAuthorsInSet.getAllPairs(setAuthors);
            totalPairs.addAll(currPairs);
        }

        return totalPairs;
    }
}
