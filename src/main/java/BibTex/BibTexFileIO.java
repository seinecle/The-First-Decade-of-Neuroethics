/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BibTex;

import Utils.Author;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author C. Levallois
 */
class BibTexFileIO {

    static Set<BibTexRef> read(BufferedReader br, int start, int end) throws IOException {


        String currLine;
        String[] terms;
        BibTexRef ref = new BibTexRef();
        Set<BibTexRef> setRefs = new HashSet();
        Author author;
        String[] termsInAuthor;
        Set<String> keywords;

        while ((currLine = br.readLine()) != null) {
            if (currLine.contains("The Neural Basis of Human Social Values")) {
                System.out.println("here!");
            }
            currLine = currLine.trim();
            currLine = StringUtils.removeEnd(currLine, ",");
            currLine = StringUtils.removeEnd(currLine, "}").trim();
//            System.out.println("currLine: " + currLine);

            if (currLine.startsWith("@")) {

                if (!ref.getYear().equals("null")) {
                    setRefs.add(ref);
                    if (ref.getJournal() != null & ref.getVolume() == null) {
//                        System.out.println("journal without volume: "+ref.getTitle());
                    }
                }
                ref = new BibTexRef();
                terms = currLine.split("\\{");
                ref.setId(StringUtils.removeEnd(terms[1].trim(), ","));
            }

            if (currLine.trim().startsWith("author")) {
                terms = currLine.split("\\=", 2);
                currLine = StringUtils.removeStart(terms[1].trim(), "\\{");
                terms = currLine.split(" and ");
                for (String element : terms) {
                    element = StringUtils.capitalize(element.replaceAll("-\\.", " ").trim());
                    termsInAuthor = element.split(",");
                    if (termsInAuthor.length == 1) {
                        continue;
                    }
                    if (termsInAuthor[0].trim().length() == 0 || termsInAuthor[1].trim().length() == 0) {
                        continue;
                    }
                    author = new Author(termsInAuthor[1].trim(), termsInAuthor[0].trim());
//                    System.out.println("currAuthor: " + currAuthor.getFullname());
                    ref.addAuthor(author);
                }
            }


            if (currLine.startsWith("title")) {
                terms = currLine.split("\\=", 2);
                currLine = StringUtils.removeStart(terms[1].trim(), "\\{").trim();
                ref.setPaperTitle(currLine);
            }

            if (currLine.startsWith("keywords")) {
                terms = currLine.split("\\=", 2);
                currLine = StringUtils.removeStart(terms[1].trim(), "\\{").trim();
                keywords = new HashSet();
                keywords.addAll(Arrays.asList(currLine.split(",")));
                ref.setKeywords(keywords);
            }

            if (currLine.startsWith("abstract")) {
                terms = currLine.split("\\=", 2);
                currLine = StringUtils.removeStart(terms[1].trim(), "\\{").trim();
                ref.setAbs(currLine);
            }

            if (currLine.startsWith("journal")) {
                terms = currLine.split("\\=", 2);
                currLine = StringUtils.removeStart(terms[1].trim(), "\\{").trim();
                ref.setJournal(currLine);
            }

            if (currLine.startsWith("volume")) {
                terms = currLine.split("\\=", 2);
                currLine = StringUtils.removeStart(terms[1].trim(), "\\{").trim();
                ref.setVolume(currLine);
            }

            if (currLine.startsWith("year")) {
                terms = currLine.split("\\=", 2);
                currLine = StringUtils.removeStart(terms[1].trim(), "\\{").trim();
                if (currLine.length() != 4) {
                    System.out.println("curr Year: " + currLine);
                }
                ref.setYear(currLine);
            }
        }
        setRefs.add(ref);
        br.close();
        return setRefs;
    }
}
