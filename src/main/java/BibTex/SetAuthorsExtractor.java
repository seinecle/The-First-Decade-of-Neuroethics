/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BibTex;

import Utils.Author;
import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author C. Levallois
 */
public class SetAuthorsExtractor {

    static TreeMultiset<Author> doIt(BufferedReader br) throws IOException {

        TreeMultiset<Author> multisetAuthors = TreeMultiset.create();
        String currLine;
        String stringCurrAuthors;
        String[] arrayCurrAuthors;
        String[] arrayTermsInCurrAuthor;
        Author currAuthor;
        while ((currLine = br.readLine()) != null) {

            if (currLine.trim().startsWith("author") & currLine.contains(" and ")) {
                stringCurrAuthors = currLine.substring(currLine.indexOf("\"") + 1, currLine.length() - 2);
//                System.out.println("stringCurrAuthors: " + stringCurrAuthors);
                arrayCurrAuthors = stringCurrAuthors.split(" and ");
                for (String element : arrayCurrAuthors) {
                    element = StringUtils.capitalize(element.replaceAll("-\\.", " ").trim());
                    arrayTermsInCurrAuthor = element.split(",");
                    if (arrayTermsInCurrAuthor.length == 1) {
                        continue;
                    }
                    if (arrayTermsInCurrAuthor[0].trim().length() == 0 || arrayTermsInCurrAuthor[1].trim().length() == 0) {
                        continue;
                    }
                    currAuthor = new Author(arrayTermsInCurrAuthor[1].trim(), arrayTermsInCurrAuthor[0].trim());
//                    System.out.println("currAuthor: " + currAuthor.getFullname());
                    multisetAuthors.add(currAuthor);
                }
            }
        }

        System.out.println("number of unique authors (before spell check): " + multisetAuthors.elementSet().size());
        return multisetAuthors;
    }

    public static Multiset<Author> extract(Set<BibTexRef> setRefs) {

        List<Author> listAuthors;
        Multiset authors = TreeMultiset.create();
        Iterator<BibTexRef> setRefsIterator = setRefs.iterator();
        BibTexRef ref;
        while (setRefsIterator.hasNext()) {
            ref = setRefsIterator.next();
            listAuthors = ref.getAuthors();
            authors.addAll(listAuthors);
        }
        System.out.println("number of unique authors: " + authors.elementSet().size());

        return authors;


    }
}
