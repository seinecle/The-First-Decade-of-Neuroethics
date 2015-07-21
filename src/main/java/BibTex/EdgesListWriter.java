/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BibTex;

import Utils.Author;
import Utils.DirectedPair;
import com.google.common.collect.TreeMultiset;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author C. Levallois
 */
public class EdgesListWriter {

    static Author currAuthor;
    static int countCurrAuthor;

    public void write(TreeMultiset<DirectedPair<Author, Author>> pairsAuthors, BufferedWriter bw,int minOccurrenceAuthor,Map<Author, Integer> mapAuthorsCounter) throws IOException {

        Iterator<DirectedPair<Author, Author>> pairsIterator;
        StringBuilder sb = new StringBuilder();
        pairsIterator = pairsAuthors.elementSet().iterator();
        DirectedPair<Author, Author> currPair;
        sb.append("Source;Target;Weight;Type").append("\n");
        Author author1;
        Author author2;
        int min = minOccurrenceAuthor;


        while (pairsIterator.hasNext()) {
            currPair = pairsIterator.next();
            author1 = currPair.getLeft();
            author2 = currPair.getRight();
            if (mapAuthorsCounter.get(author1) < min || mapAuthorsCounter.get(author2) < min) {
                continue;
            }
            sb.append(currPair.getLeft().getFullnameWithComma()).append(";").append(currPair.getRight().getFullnameWithComma()).append(";").append(pairsAuthors.count(currPair)).append(";undirected\n");
        }

        bw.write(sb.toString());
        bw.close();
    }
}
