/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import BibTex.Controller;
import com.google.common.collect.Multimap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author C. Levallois
 */
public class CloseMatchesDetector {

    private Set<CloseMatch> setCloseMatches = new HashSet();
    char a;
    char b;
    String c;
    String d;
    String author1Forename;
    String author2Forename;
    String[] author1ForenameTerms;
    String[] author2ForenameTerms;
    String author1Surname;
    String author2Surname;
    boolean identicalSurnames = false;
    Author author1;
    Author author2;
    Author author3;

    public Set<CloseMatch> check(Set<Author> setAuthorsWithEdits,Multimap<Author, Author> mapFalsePositiveMatches) {

        Clock findingAllPairsClock = new Clock(" --- finding all pairs of authors ---");
        List<Pair<Author>> listPairs = new FindAllPairs().getAllUndirectedPairsAsList(setAuthorsWithEdits);
        findingAllPairsClock.closeAndPrintClock();

        LinkedList<DiffSpelling.Diff> diffs;
        CloseMatch cmb;

        Clock iteratingThroughAllPairsClock = new Clock(" --- iteration through all pairs of authors ---");
        for (Pair<Author> pair : listPairs) {
            author1 = pair.getLeft();
            author2 = pair.getRight();

            //if both close matches have already been found to be false positive, skip
            if (mapFalsePositiveMatches.get(author1).contains(author2) && mapFalsePositiveMatches.get(author2).contains(author1)) {
                continue;
            }

//            if ("Katy".equals(author1.getForename()) & "Katy".equals(author2.getForename())) {
//                System.out.println("author1: " + author1.getFullname());
//                System.out.println("author2: " + author2.getFullname());
//                System.out.println(computeWeightedLD(StringUtils.getLevenshteinDistance(author1.getSurname(), author2.getSurname()), author1.getSurname(), author2.getSurname()) > 0.3);
//            }

            int match = thresholdLD(author1, author2);


            if (match != -1) {

                // ----------------------------

                author3 = getSuggestion(author1, author2, match);

                cmb = new CloseMatch();
                cmb.setAuthor1(author1.getFullnameWithComma());
                cmb.setAuthor2(author2.getFullnameWithComma());
                cmb.setAuthor3(author3.getFullnameWithComma());
                diffs = new DiffSpelling().diff_main(author1.getFullnameWithComma(), author2.getFullnameWithComma());
                cmb.setAuthor1Displayed(new DiffSpelling().diff_text1Custom(diffs));
                cmb.setAuthor2Displayed(new DiffSpelling().diff_text2Custom(diffs));
//                System.out.println("displayed 1" + cmb.getAuthor1Displayed());
//                System.out.println("displayed 2" + cmb.getAuthor2Displayed());
//                setCloseMatches.add(new Author(cmb.getAuthor1()));
//                setCloseMatches.add(new Author(cmb.getAuthor2()));
                setCloseMatches.add(cmb);

            }
        }
        iteratingThroughAllPairsClock.closeAndPrintClock();
        return setCloseMatches;
    }

    private float computeWeightedLD(Integer ld, String one, String two) {
        return (float) ld / Math.min(one.length(), two.length());
    }

    private int thresholdLD(Author author1, Author author2) {
//        arrayTermsInFullnameInAuthor1 = author1.getFullname().split(" ");
//        arrayTermsInFullnameInAuthor2 = author2.getFullname().split(" ");

//        if (author1.getSurname().equals(author2.getSurname())) {
//            System.out.println("here!");
//        }

        author1Forename = author1.getForename();
        author2Forename = author2.getForename();
        author1Surname = author1.getSurname();
        author2Surname = author2.getSurname();
        identicalSurnames = false;

        // not the same first initial for first name? => not a match
        a = author1Forename.charAt(0);
        b = author2Forename.charAt(0);
        if (a != b) {
            return -1;
        }

        //if both first names are made of 2 initials, and these two are different,then we don't have a match
        c = StringUtils.replace(author1Forename, " ", "");
        d = StringUtils.replace(author2Forename, " ", "");
        if (c.length() == 2 && d.length() == 2 && !c.equals(d)) {
            return -1;
        }


        //if both first names are made of two terms (initials or not), and the second term is not identical, then we don't have a match
        author1ForenameTerms = author1Forename.split(" ");
        author2ForenameTerms = author2Forename.split(" ");
        if (author1ForenameTerms.length >= 2 && author2ForenameTerms.length >= 2 && !author1ForenameTerms[1].equals(author2ForenameTerms[1])) {
            return -1;
        }


        int distSurname = StringUtils.getLevenshteinDistance(author1Surname, author2Surname);
        boolean singleTermSurNames = !author1Surname.contains(" ") && !author2Surname.contains(" ");

        //if both surnames have a single term, and the ld between these terms is above 1, then we don't have a match
        if (singleTermSurNames && distSurname > 1) {
            return -1;

        }

        //if both surnames have a single term, and the ld between these terms is  = 1, and both these surnames are all ansii, we don't have a match
        boolean allANSII = (StringUtils.isAsciiPrintable(author1Surname) && StringUtils.isAsciiPrintable(author2Surname));
        if (distSurname == 1 && allANSII) {
            return -1;
        }
        
        if (distSurname == 1 && !allANSII) {
            identicalSurnames = true;
        }
        if (distSurname == 0) {
            identicalSurnames = true;
        }






        //detects matches such as "Perreau, Adrian" & "Pinninck, Adrian Perreau De" 	
//        if (arrayTermsInFullnameInAuthor1.length != arrayTermsInFullnameInAuthor2.length) {
//            int element1;
//            int element2;
//            intersectFullNamesAuthor1And2 = new HashSet();
//            setTermsInSurnameAuthor1 = new HashSet();
//            setTermsInSurnameAuthor2 = new HashSet();
//            setTermsInFullnameAuthor1 = new HashSet();
//            setTermsInFullnameAuthor2 = new HashSet();
////            System.out.println("author1.getFullname() " + author1.getFullname());
////            System.out.println("author2.getFullname() " + author2.getFullname());
//
//            setTermsInFullnameAuthor1.addAll(Arrays.asList(arrayTermsInFullnameInAuthor1));
//            setTermsInFullnameAuthor2.addAll(Arrays.asList(arrayTermsInFullnameInAuthor2));
//            Sets.intersection(setTermsInFullnameAuthor1, setTermsInFullnameAuthor2).copyInto(intersectFullNamesAuthor1And2);
//            int intersectFullNamesSize = intersectFullNamesAuthor1And2.size();
//            if (intersectFullNamesSize > 1) {
//
//                element1 = intersectFullNamesAuthor1And2.toArray()[0].toString().length();
//                element2 = intersectFullNamesAuthor1And2.toArray()[1].toString().length();
//
//                arrayTermsInSurnameInAuthor1 = author1.getSurname().split(" ");
//                arrayTermsInSurnameInAuthor2 = author2.getSurname().split(" ");
//
//                setTermsInSurnameAuthor1.addAll(Arrays.asList(arrayTermsInSurnameInAuthor1));
//                setTermsInSurnameAuthor2.addAll(Arrays.asList(arrayTermsInSurnameInAuthor2));
//                int intersectSurnames = Sets.intersection(setTermsInSurnameAuthor1, setTermsInSurnameAuthor2).size();
//
//                if ((intersectFullNamesSize > 1 & intersectSurnames > 0) | (intersectFullNamesSize > 1 && element1 > 1 && element2 > 1)) {
//                    return 1;
//                }
//            }
//
//        }


        int length1 = author1Forename.length();
        int length2 = author2Forename.length();
        // if surnames are identical, and the first name of one is the (unique) initial of the other, we have a match
        if (identicalSurnames & length1 == 1 & length2 > 1) {
            if ((author1Forename.equals(author2Forename.subSequence(0, 1)))) {
                return 0;
            }
        }

        // if surnames are identical, and the first name of one is the (unique) initial of the other we have a match
        if (identicalSurnames & length2 == 1 & length1 > 1) {
            if ((author2Forename.equals(author1Forename.subSequence(0, 1)))) {
                return 0;
            }
        }

        // matching Smith, J J and Smith, John J
        String[] termsForeName1 = author1Forename.split(" ");
        String[] termsForeName2 = author2Forename.split(" ");
        if (author1Surname.equals("Fins") && author2Surname.equals("Fins")){
            System.out.println("eee");
        }
        if (identicalSurnames && termsForeName1.length >= 2 && termsForeName2.length >= 2) {
            if (termsForeName1[0].length() == 1 && termsForeName1[1].length() == 1) {
                if (termsForeName2[0].subSequence(0, 1).equals(termsForeName1[0])) {
                    return 0;
                }
            }
            if (termsForeName2[0].length() == 1 && termsForeName2[1].length() == 1) {
                if (termsForeName1[0].subSequence(0, 1).equals(termsForeName2[0])) {
                    return 0;
                }
            }
        }


        // matching Smith, J J and Smith, J John
        if (identicalSurnames & length2 == 1 & length1 > 1) {
            if ((author2Forename.equals(author1Forename.subSequence(0, 1)))) {
                return 0;
            }
        }


        int levenDistance = StringUtils.getLevenshteinDistance(author1.getFullnameWithComma(), author2.getFullnameWithComma());
        float weightedLd = computeWeightedLD(levenDistance, author1.getFullnameWithComma(), author2.getFullnameWithComma());

        if (weightedLd
                > 0.45) {
            return -1;
        } else if ((computeWeightedLD(StringUtils.getLevenshteinDistance(author1Forename, author2Forename), author1Forename, author2Forename) < 0.3)
                & (computeWeightedLD(StringUtils.getLevenshteinDistance(author1Surname, author2Surname), author1Surname, author2Surname) > 0.35)) {
            return -1;
        }

        //still here?! Then return 0 to indicate that it might be a match.
        return 0;


    }

    private Author getSuggestion(Author author1, Author author2, int typeMatch) {
        int code = typeMatch;
        boolean suggestionMade = false;
        author1Forename = author1.getForename();
        author2Forename = author2.getForename();
        author1Surname = author1.getSurname();
        author2Surname = author2.getSurname();

        String suggestedForename = author1Forename;
        String suggestedSurname = author1Surname;


        String[] author1ForenameArray = author1Forename.split(" ");
        String[] author2ForenameArray = author2Forename.split(" ");
        String[] suggestedForenameArray;


        //makes a suggestion for matches such as "Perreau, Adrian" & "Pinninck, Adrian Perreau De" 	
        if (code == 1) {
            if (author1.getFullname().length() > author2.getFullname().length()) {
                author3 = author1;
            } else {
                author3 = author2;

            }
            return author3;
        }

        //first names which have different number of names: take the longer first name
        if (author1ForenameArray.length > author2ForenameArray.length) {
            suggestedForename = author1Forename;
            suggestedSurname = author1Surname;
            suggestionMade = true;
        } else if (author1ForenameArray.length < author2ForenameArray.length) {
            suggestedForename = author2Forename;
            suggestedSurname = author1Surname;
            suggestionMade = true;
        }

        // first names with FIRST initial instead of full first names: get the full name
        suggestedForenameArray = suggestedForename.split(" ");
        if (author1ForenameArray[0].length() == 1 & author2ForenameArray[0].length() > 1) {
            suggestedForenameArray[0] = author2ForenameArray[0];
            suggestedForename = "";
            for (String element : suggestedForenameArray) {
                suggestedForename = suggestedForename + element + " ";
            }
            suggestedForename = suggestedForename.trim();
            suggestionMade = true;
        } else if (author2ForenameArray[0].length() == 1 & author1ForenameArray[0].length() > 1) {
            suggestedForenameArray[0] = author1ForenameArray[0];
            suggestedForename = "";
            for (String element : suggestedForenameArray) {
                suggestedForename = suggestedForename + element + " ";
            }
            suggestionMade = true;
        }


        // first names with SECOND initial instead of full first names: get the full name
        suggestedForenameArray = suggestedForename.split(" ");
        if ((author1ForenameArray.length > 1 && author2ForenameArray.length > 1)) {
            if (author1ForenameArray[1].length() == 1 & author2ForenameArray[1].length() > 1) {
                suggestedForenameArray[1] = author2ForenameArray[1];
                suggestedForename = "";
                for (String element : suggestedForenameArray) {
                    suggestedForename = suggestedForename + element + " ";
                }
                suggestedForename = suggestedForename.trim();
                suggestionMade = true;
            } else if (author2ForenameArray[1].length() == 1 & author1ForenameArray[1].length() > 1) {
                suggestedForenameArray[1] = author1ForenameArray[1];
                suggestedForename = "";
                for (String element : suggestedForenameArray) {
                    suggestedForename = suggestedForename + element + " ";
                }
                suggestionMade = true;
            }
        }



        // replacing "oe" by "o", "ue" by "u", "ae" by "a" (suspected cases of mispellings due to umlauts)
        if ((author1Forename.contains("oe") && !author2Forename.contains("oe")) || (author2Forename.contains("oe") && !author1Forename.contains("oe"))) {
            suggestedForename = suggestedForename.replaceAll("oe", "ö");
            suggestionMade = true;
        }
        if ((author1Surname.contains("oe") && !author2Surname.contains("oe")) || (author2Surname.contains("oe") && !author1Surname.contains("oe"))) {
            suggestedSurname = suggestedSurname.replaceAll("oe", "ö");
            suggestionMade = true;
        }
        if ((author1Forename.contains("ue") && !author2Forename.contains("ue")) || (author2Forename.contains("ue") && !author1Forename.contains("ue"))) {
            suggestedForename = suggestedForename.replaceAll("ue", "ü");
            suggestionMade = true;
        }
        if ((author1Surname.contains("ue") && !author2Surname.contains("ue")) || (author2Surname.contains("ue") && !author1Surname.contains("ue"))) {
            suggestedSurname = suggestedSurname.replaceAll("ue", "ü");
            suggestionMade = true;
        }
//        if ((author1Forename.contains("ae") && !author2Forename.contains("ae")) || (author2Forename.contains("ae") && !author1Forename.contains("ae"))) {
//            suggestedForename = suggestedForename.replaceAll("ae", "a");
//            suggestionMade = true;
//        }
//        if ((author1Surname.contains("ae") && !author2Surname.contains("ae")) || (author2Surname.contains("ae") && !author1Surname.contains("ae"))) {
//            suggestedSurname = suggestedSurname.replaceAll("ae", "u");
//            suggestionMade = true;
//        }

        if (suggestionMade) {
            author3 = new Author(suggestedForename, suggestedSurname);
            return author3;
        } else {

            //IF WE HAVE NO SPECIFIC SUGGESTION TO MAKE, WE SUGGEST THE LONGER NAME
            if (author1.toString().length() > author2.toString().length()) {
                author3 = author1;
            } else {
                author3 = author2;
            }
        }
        return author3;

    }
}