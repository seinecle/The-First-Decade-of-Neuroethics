/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BibTex;

import Utils.Author;
import com.google.common.collect.Multiset;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author C. Levallois
 */
public class NodesListWriter {

    static Author currAuthor;
    static int countCurrAuthor;
    static StringBuilder sb = new StringBuilder();

    public void write(Multiset<Author> multisetAuthors, Map<Author, Integer> mapAuthorsCounter,BufferedWriter bw, int minOccurrenceAuthor) throws IOException {

        Iterator<Author> multisetAuthorsIterator = multisetAuthors.elementSet().iterator();
        Map<Author, Integer> mapCountAuthors = mapAuthorsCounter;
        int counter = 0;

        sb.append("id;Label;firstName;lastname;count\n");


        int min = minOccurrenceAuthor;
        while (multisetAuthorsIterator.hasNext()) {
            currAuthor = multisetAuthorsIterator.next();
            countCurrAuthor = mapCountAuthors.get(currAuthor);
            if (countCurrAuthor >= min) {
                sb.append(currAuthor.getFullnameWithComma()).append(";").append(currAuthor.getFullnameWithComma()).append(";").append(currAuthor.getForename()).append(";").append(currAuthor.getSurname()).append(";").append(multisetAuthors.count(currAuthor)).append(".0\n");
                counter++;
            }
        }

        bw.write(sb.toString());
        bw.close();
        System.out.println("number of nodes written in the file: " + counter);
    }
}
