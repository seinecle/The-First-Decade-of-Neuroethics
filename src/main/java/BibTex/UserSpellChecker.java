/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BibTex;

import Utils.Author;
import Utils.CloseMatch;
import com.google.common.collect.Multimap;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JOptionPane;

/**
 *
 * @author C. Levallois
 */
public class UserSpellChecker {

    boolean automatic;

    public UserSpellChecker(boolean automaticSpellChecker) {
        this.automatic = automaticSpellChecker;
    }

    public void execute(Set<CloseMatch> setCloseMatches, Multimap<Author, Author> mapFalsePositiveMatches, EditedLabelsIO editedLabelsIO) {

        Iterator<CloseMatch> setCloseMatchesIterator = setCloseMatches.iterator();
        int nbMatches = setCloseMatches.size();
        String[] options = {"Merge",
            "Delete both",
            "Keep Both"};

        while (setCloseMatchesIterator.hasNext()) {

            CloseMatch currMatch = setCloseMatchesIterator.next();
            Author author1 = new Author();
            author1.setFullnameWithComma(currMatch.getAuthor1());
            Author author2 = new Author();
            author2.setFullnameWithComma(currMatch.getAuthor2());

            //if both close matches have already been found to be false positive, skip
            if (mapFalsePositiveMatches.keySet().contains(author1) && mapFalsePositiveMatches.keySet().contains(author2)) {
                if (mapFalsePositiveMatches.get(author1).contains(author2) && mapFalsePositiveMatches.get(author2).contains(author1)) {
                    continue;
                }
            }

//            System.out.println("author1: " + currMatch.getAuthor1());
//            System.out.println("author2: " + currMatch.getAuthor2());
//            System.out.println("author3: " + currMatch.getAuthor3());

            if (!automatic) {

                int n = JOptionPane.showOptionDialog(null,
                        currMatch.getAuthor1()
                        + " looks suspiciously close to " + currMatch.getAuthor2(),
                        nbMatches-- + " left to examine.",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (n == 0) {
                    //merge them!

                    String s = (String) JOptionPane.showInputDialog(
                            null,
                            currMatch.getAuthor1() + " *** " + currMatch.getAuthor2() + "\n"
                            + "How should they be merged?\n",
                            "Write",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            currMatch.getAuthor3());
                    if (s != null) {
                        Author modifiedAuthor = new Author();
                        modifiedAuthor.setFullnameWithComma(s);
//                    mapLabels.put(newAuthor1, modifiedAuthor);
//                    mapLabels.put(newAuthor2, modifiedAuthor);
                        editedLabelsIO.updateMapEdits(author1, modifiedAuthor, true, mapFalsePositiveMatches);
                        editedLabelsIO.updateMapEdits(author2, modifiedAuthor, true, mapFalsePositiveMatches);
                    } else {
                        editedLabelsIO.updateMapEdits(author1, author2, false, mapFalsePositiveMatches);
                    }
                }
                if (n == 1) {
                    //delete both!

                    Author removedAuthor = new Author();
                    removedAuthor.setForename("removed");
//                mapLabels.remove(newAuthor1);
//                mapLabels.remove(newAuthor2);
                    editedLabelsIO.updateMapEdits(author1, removedAuthor, true, mapFalsePositiveMatches);
                    editedLabelsIO.updateMapEdits(author2, removedAuthor, true, mapFalsePositiveMatches);
                }
                if (n == 2) {
                    //keep both? Then don't modify the maps of labels.
                    editedLabelsIO.updateMapEdits(author1, author2, false, mapFalsePositiveMatches);
//                mapLabels.put(new Author(currMatch.getAuthor1()), new Author(currMatch.getAuthor1()));
//                mapLabels.put(new Author(currMatch.getAuthor2()), new Author(currMatch.getAuthor2()));
                }
            } else {
                //in automatic mode, the spell check correction merges the 2 candidates and use the merger spelling suggestion
                Author modifiedAuthor = new Author();
                modifiedAuthor.setFullnameWithComma(currMatch.getAuthor3());
//                    mapLabels.put(newAuthor1, modifiedAuthor);
//                    mapLabels.put(newAuthor2, modifiedAuthor);
                editedLabelsIO.updateMapEdits(author1, modifiedAuthor, true, mapFalsePositiveMatches);
                editedLabelsIO.updateMapEdits(author2, modifiedAuthor, true, mapFalsePositiveMatches);

            }
        }

    }
}
