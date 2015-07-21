/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BibTex;

import Utils.Author;
import com.google.common.collect.Multiset;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author C. Levallois
 */
public class AuthorCounterTracker {

    private Map<Author, Integer> mapAuthorsCounter;
    private Map<Author, Author> mapEdits;

    public AuthorCounterTracker(Map<Author, Integer> mapAuthorsCounter, Map<Author, Author> mapEdits) {
        this.mapAuthorsCounter = mapAuthorsCounter;
        this.mapEdits = mapEdits;
    }

    public void countAuthorsFromRefs(Set<BibTexRef> setRefs) {
        Multiset<Author> multisetAuthors = SetAuthorsExtractor.extract(setRefs);
        for (Author author : multisetAuthors.elementSet()) {
            mapAuthorsCounter.put(author, multisetAuthors.count(author));
        }
    }

    public void updateTrackerWithEditedAuthor(Author author) {

        Author authorEdited = mapEdits.get(author);
        if (author.equals(authorEdited)) {
            return;
        }
        if (!mapAuthorsCounter.keySet().contains(author)) {
            return;
        }
        Integer countOriginalAuthor = mapAuthorsCounter.get(author);
        Integer countEditedAuthor = mapAuthorsCounter.get(authorEdited);
        if (countEditedAuthor == null) {
            countEditedAuthor = 0;
        }
        mapAuthorsCounter.put(authorEdited, countOriginalAuthor + countEditedAuthor);
        mapAuthorsCounter.remove(author);

    }
}
