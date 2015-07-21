/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BibTex;

import Utils.Author;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 *
 * @author C. Levallois
 */
public class EditedLabelsIO {

    String wk;
    Map<Author, Author> mapEdits;

    public EditedLabelsIO(Map<Author, Author> mapEdits,String wk) {
        this.wk = wk;
        this.mapEdits = mapEdits;
    }

    public void write(Multimap<Author, Author> mapFalsePositiveMatches) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(wk + "namesEdited.txt"));

        Iterator<Entry<Author, Author>> mapLabelsIterator = mapEdits.entrySet().iterator();
        Entry<Author, Author> entry;
        while (mapLabelsIterator.hasNext()) {
            entry = mapLabelsIterator.next();
            bw.write(entry.getKey().getFullnameWithComma() + " -> " + entry.getValue().getFullnameWithComma());
            bw.newLine();
        }
        Iterator<Entry<Author, Author>> mapFalsePositiveMatchesIterator = mapFalsePositiveMatches.entries().iterator();
        while (mapFalsePositiveMatchesIterator.hasNext()) {
            entry = mapFalsePositiveMatchesIterator.next();
            bw.write(entry.getKey().getFullnameWithComma() + " <> " + entry.getValue().getFullnameWithComma());
            bw.newLine();
        }
        bw.flush();
        bw.close();
    }

    public Map<Author, Author> populateMapEdits() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(wk + "namesEdited.txt"));
        String string;
        Map<Author, Author> mapEdits = new TreeMap();
        while ((string = br.readLine()) != null) {
            if (string.contains(" -> ")) {
                String[] fields = string.split(" -> ");
                Author author1 = new Author();
                author1.setFullnameWithComma(fields[0]);
                Author author2 = new Author();
                author2.setFullnameWithComma(fields[1]);
                mapEdits.put(author1, author2);
//                if (multisetAuthors.contains(author1)) {
//                    int count = multisetAuthors.count(author1);
//                    multisetAuthors.remove(author1, count);
//                    multisetAuthors.add(author2, count);
//                }
            }
        }
        br.close();
        return mapEdits;
    }

    public Multimap<Author, Author> populateMapFalsePositives() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(wk + "namesEdited.txt"));
        String string;
        Multimap<Author, Author> mapFalsePositives = HashMultimap.create();
        while ((string = br.readLine()) != null) {
            if (string.contains(" <> ")) {
                String[] fields = string.split(" <> ");
                Author author1 = new Author();
                author1.setFullnameWithComma(fields[0]);
                Author author2 = new Author();
                author2.setFullnameWithComma(fields[1]);
                mapFalsePositives.put(author1, author2);
            }

        }
        br.close();
        return mapFalsePositives;
    }

    public void updateMapEdits(Author author1, Author author2, boolean is,Multimap<Author, Author> mapFalsePositiveMatches) {
        if (is) {
            Iterator<Entry<Author, Author>> mapEditsIterator = mapEdits.entrySet().iterator();
            Entry<Author, Author> entry;

            if (mapEdits.containsValue(author1)) {
                while (mapEditsIterator.hasNext()) {
                    entry = mapEditsIterator.next();
                    if (entry.getValue().equals(author1)) {
                        mapEdits.put(entry.getKey(), author2);
                    }
                }
            }
            if (!mapEdits.containsKey(author1)) {
                mapEdits.put(author1, author2);
            }
        } else {
            mapFalsePositiveMatches.put(author1, author2);
            mapFalsePositiveMatches.put(author2, author1);
        }
    }
}
