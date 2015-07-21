/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BibTex;

import ResourceLoaders.JournalAbbreviationsMapping;
import Categorization.Category;
import Categorization.Edge;
import Categorization.Item;
import Utils.Author;
import Utils.FindAllPairs;
import Utils.Pair;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.collect.TreeMultiset;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/*
 Copyright 2008-2013 Clement Levallois
 Authors : Clement Levallois <clementlevallois@gmail.com>
 Website : http://www.clementlevallois.net


 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

 Copyright 2013 Clement Levallois. All rights reserved.

 The contents of this file are subject to the terms of either the GNU
 General Public License Version 3 only ("GPL") or the Common
 Development and Distribution License("CDDL") (collectively, the
 "License"). You may not use this file except in compliance with the
 License. You can obtain a copy of the License at
 http://gephi.org/about/legal/license-notice/
 or /cddl-1.0.txt and /gpl-3.0.txt. See the License for the
 specific language governing permissions and limitations under the
 License.  When distributing the software, include this License Header
 Notice in each file and include the License files at
 /cddl-1.0.txt and /gpl-3.0.txt. If applicable, add the following below the
 License Header, with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"

 If you wish your version of this file to be governed by only the CDDL
 or only the GPL Version 3, indicate your decision by adding
 "[Contributor] elects to include this software in this distribution
 under the [CDDL or GPL Version 3] license." If you do not indicate a
 single choice of license, a recipient has the option to distribute
 your version of this file under either the CDDL, the GPL Version 3 or
 to extend the choice of license to its licensees as provided above.
 However, if you add GPL Version 3 code and therefore, elected the GPL
 Version 3 license, then the option applies only if the new code is
 made subject to such option by the copyright holder.

 Contributor(s): Clement Levallois

 */
public class IOmethods {

    String folder = "H:\\files\\NESSHI\\Project with Elisabeth\\Bibliography\\Topic Classification\\";
    List<Item> items;

    public void initialize() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        folder = folder + date.toString() + "_" + time.toSecondOfDay() + "\\";
        new File(folder).mkdirs();

    }

    public void writePapersAndTheirCategories(Set<BibTexRef> refs) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(folder + "classified abstracts.csv"));
        StringBuilder sb = new StringBuilder();
        int itemNoCategory = 0;
        int countPapersWithJustOneCategory = 0;

        for (BibTexRef ref : refs) {

            //to print only non categorized abstracts
//            if (!ref.getCategories().isEmpty()) {
//                continue;
//            }
            int cutoff = Math.min(ref.getTitle().length(), 120);
            System.out.println("item: " + ref.getTitle().substring(0, cutoff));
            System.out.print("categories: ");

            sb.append(ref.getTitle()).append("|").append(ref.getAbs()).append("|");
            if (ref.getCategories().isEmpty()) {
                System.out.println("NO CATEGORY");
                itemNoCategory++;
                sb.append("\n");
            } else {
                for (Category category : ref.getCategories()) {
                    if (ref.getCategories().size() == 1) {
                        countPapersWithJustOneCategory++;
                    }

                    System.out.print(category.getCategoryName());
                    System.out.print(" (");
                    System.out.print(category.getConfidence());
                    System.out.print(")");
                    sb.append(category.getCategoryName()).append(" (").append(category.getConfidence()).append("), ");
                }
                System.out.print("\n");
                sb.append("\n");
            }
        }

        System.out.println("");
        System.out.println("items: " + refs.size());
        System.out.println("items without category: " + itemNoCategory);
        System.out.println("items with at least one category: " + (refs.size() - itemNoCategory));
        System.out.println("items with just one category: " + countPapersWithJustOneCategory);
        bw.write(sb.toString());
        bw.close();

    }

    public void writeCategoriesAndTheirPapersToFile(Set<BibTexRef> refs) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(folder + "refs per categories.csv"));
        StringBuilder sb = new StringBuilder();
        String sep = "|";

        //creation of 2 convenient data structures for I/O
        Map<String, Multiset<String>> categoriesToPapers = new TreeMap();
        List<String> categoryNames = new ArrayList();

        for (BibTexRef ref : refs) {
            Set<Category> categories = ref.getCategories();

            for (Category category : categories) {
                if (!categoryNames.contains(category.getCategoryName())) {
                    categoryNames.add(category.getCategoryName());
                }
                if (categoriesToPapers.containsKey(category.getCategoryName())) {
                    categoriesToPapers.get(category.getCategoryName()).add(ref.toBibliographicalFormattedString());
                } else {
                    Multiset<String> papersForOneCategory = HashMultiset.create();
                    papersForOneCategory.add(ref.toBibliographicalFormattedString());

                    categoriesToPapers.put(category.getCategoryName(), papersForOneCategory);
                }
            }

        }
        Collections.sort(categoryNames);

        //writing of the first line of the csv: headers of the categories.
        for (String categoryName : categoryNames) {
            sb.append(categoryName);
            sb.append(sep);
        }
        sb.append("\n");

        //writing of all subsequent lines: one per year
        int countCategoriesdone = 0;
        boolean continueLoop = true;
        while (continueLoop) {

            for (Iterator<String> it = categoriesToPapers.keySet().iterator(); it.hasNext();) {
                String category = it.next();
                Multiset<String> papersForOneCategory = categoriesToPapers.get(category);
                Iterator<String> papersIterator = papersForOneCategory.iterator();
                if (papersIterator.hasNext()) {
                    String string = papersIterator.next();
                    sb.append(string).append(sep);
                    papersIterator.remove();
                } else {
                    sb.append(sep);
                }
            }
            sb.append("\n");

            for (String cat : categoriesToPapers.keySet()) {
                if (categoriesToPapers.get(cat).isEmpty()) {
                    countCategoriesdone++;
                }
            }
            if (countCategoriesdone == categoryNames.size()) {
                continueLoop = false;
            } else {
                countCategoriesdone = 0;
            }

        }

        bw.write(sb.toString());
        bw.close();

    }

    public void writeTopContributorByCategoryToFile(Set<BibTexRef> refs) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(folder + "top contributors per categories.csv"));
        StringBuilder sb = new StringBuilder();
        String sep = "|";

        //creation of 2 convenient data structures for I/O
        Map<String, Multiset<Author>> categoriesToAuthors = new TreeMap();
        List<String> categoryNames = new ArrayList();

        for (BibTexRef ref : refs) {
            Set<Category> categories = ref.getCategories();

            for (Category category : categories) {
                if (!categoryNames.contains(category.getCategoryName())) {
                    categoryNames.add(category.getCategoryName());
                }
                if (categoriesToAuthors.containsKey(category.getCategoryName())) {
                    categoriesToAuthors.get(category.getCategoryName()).addAll(ref.getAuthors());
                } else {
                    Multiset<Author> authorsForOneCategory = HashMultiset.create();
                    authorsForOneCategory.addAll(ref.getAuthors());

                    categoriesToAuthors.put(category.getCategoryName(), authorsForOneCategory);
                }
            }

        }
        Collections.sort(categoryNames);

        //writing of the first line of the csv: headers of the categories.
        for (String categoryName : categoryNames) {
            sb.append(categoryName);
            sb.append(sep);
        }
        sb.append("\n");

        //writing of all subsequent lines: one per year
        int countCategoriesdone = 0;
        boolean continueLoop = true;
        while (continueLoop) {

            for (Iterator<String> it = categoriesToAuthors.keySet().iterator(); it.hasNext();) {
                String category = it.next();
                Multiset<Author> authorsForOneCategory = categoriesToAuthors.get(category);

                Iterator<Author> authorsIterator = Multisets.copyHighestCountFirst(authorsForOneCategory).elementSet().iterator();
                if (authorsIterator.hasNext()) {
                    Author author = authorsIterator.next();
                    sb.append(author.getFullname()).append("(").append(authorsForOneCategory.count(author)).append(")").append(sep);
                    authorsForOneCategory.remove(author, authorsForOneCategory.count(author));
                } else {
                    sb.append(sep);
                }
            }
            sb.append("\n");

            for (String cat : categoriesToAuthors.keySet()) {
                if (categoriesToAuthors.get(cat).isEmpty()) {
                    countCategoriesdone++;
                }
            }
            if (countCategoriesdone == categoryNames.size()) {
                continueLoop = false;
            } else {
                countCategoriesdone = 0;
            }

        }

        bw.write(sb.toString());
        bw.close();

    }

    public void writeCategoriesPerYearInCsv(Set<BibTexRef> refs) throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(folder + "years and categories.csv"));
        StringBuilder sb = new StringBuilder();

        String sep = "|";

        //creation of 2 convenient data structures for I/O
        Map<String, Multiset<String>> yearsToCategories = new HashMap();
        List<String> categoryNames = new ArrayList();

        for (BibTexRef ref : refs) {
            String year = ref.getYear();
            Set<Category> categories = ref.getCategories();

            for (Category category : categories) {
                if (!categoryNames.contains(category.getCategoryName())) {
                    categoryNames.add(category.getCategoryName());
                }
            }

            if (yearsToCategories.containsKey(year)) {
                for (Category category : categories) {
                    yearsToCategories.get(year).add(category.getCategoryName());
                }
            } else {
                Multiset<String> categoriesForOneYear = HashMultiset.create();
                for (Category category : categories) {
                    categoriesForOneYear.add(category.getCategoryName());
                }
                yearsToCategories.put(year, categoriesForOneYear);
            }
        }

        //writing of the first line of the csv: headers of the categories.
        // first cell is empty. This is the first column, and will serve for the headers of the rows (which are the years)
        sb.append(sep);

        for (String categoryName : categoryNames) {
            sb.append(categoryName);
            sb.append(sep);
        }
        sb.append("\n");

        //writing of all subsequent lines: one per year
        for (String year : yearsToCategories.keySet()) {
            sb.append(year).append(sep);
            Multiset<String> categoriesForOneYear = yearsToCategories.get(year);
            for (String categoryName : categoryNames) {
                int count = categoriesForOneYear.count(categoryName);
                sb.append(count).append(sep);
            }
            sb.append("\n");
        }
        bw.write(sb.toString());
        bw.close();
    }

    public void writeNumberPapersPerYear(Set<BibTexRef> refs) throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(folder + "papers per year.csv"));
        StringBuilder sb = new StringBuilder();

        String sep = "|";

        //creation of the data structures for I/O
        Multiset<String> years = TreeMultiset.create();

        for (BibTexRef ref : refs) {
            String year = ref.getYear();
            years.add(year);
        }

        for (String year : years.elementSet()) {
            sb.append(year);
            sb.append(sep);
        }
        sb.append("\n");

        for (String year : years.elementSet()) {
            sb.append(years.count(year));
            sb.append(sep);
        }
        sb.append("\n");

        bw.write(sb.toString());
        bw.close();
    }

    public void writeSuperCategoriesPerYearInCsv(Set<BibTexRef> refs) throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(folder + "years and suppercategories.csv"));
        StringBuilder sb = new StringBuilder();

        String sep = "|";

        //creation of 2 convenient data structures for I/O
        Map<String, Multiset<String>> yearsToCategories = new HashMap();
        List<String> categoryNames = new ArrayList();

        for (BibTexRef ref : refs) {
            String year = ref.getYear();
            Set<Category> categories = ref.getCategories();

            for (Category category : categories) {
                if (!categoryNames.contains(category.getSuperCategory())) {
                    categoryNames.add(category.getSuperCategory());
                }
            }

            if (yearsToCategories.containsKey(year)) {
                for (Category category : categories) {
                    yearsToCategories.get(year).add(category.getSuperCategory());
                }
            } else {
                Multiset<String> categoriesForOneYear = HashMultiset.create();
                for (Category category : categories) {
                    categoriesForOneYear.add(category.getSuperCategory());
                }
                yearsToCategories.put(year, categoriesForOneYear);
            }
        }

        //writing of the first line of the csv: headers of the categories.
        // first cell is empty. This is the first column, and will serve for the headers of the rows (which are the years)
        sb.append(sep);

        for (String categoryName : categoryNames) {
            sb.append(categoryName);
            sb.append(sep);
        }
        sb.append("\n");

        //writing of all subsequent lines: one per year
        for (String year : yearsToCategories.keySet()) {
            sb.append(year).append(sep);
            Multiset<String> categoriesForOneYear = yearsToCategories.get(year);
            for (String categoryName : categoryNames) {
                int count = categoriesForOneYear.count(categoryName);
                sb.append(count).append(sep);
            }
            sb.append("\n");
        }
        bw.write(sb.toString());
        bw.close();
    }

    public void writeConnectedCategories(Set<BibTexRef> refs) throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(folder + "connected categories.csv"));
        StringBuilder sb = new StringBuilder();
        int maxCountCategory = 0;
        sb.append("Source,Target,Type,Weight").append("\n");

        //creation of convenient data structures for I/O
        Multiset<Edge> edges = HashMultiset.create();
        Multiset<String> multisetCategoryNames = HashMultiset.create();

        for (BibTexRef ref : refs) {
            Set<Category> categories = ref.getCategories();
            Set<String> categoriesNames = new HashSet();

            for (Category category : categories) {
                categoriesNames.add(category.getCategoryName());
                multisetCategoryNames.add(category.getCategoryName());
            }

            FindAllPairs findAllPairs = new FindAllPairs();
            List<Pair<String>> pairs = findAllPairs.getAllUndirectedPairsAsList(categoriesNames);

            for (Pair<String> pair : pairs) {
                Edge edge = new Edge();
                edge.setNode1(pair.getLeft());
                edge.setNode2(pair.getRight());
                edges.add(edge);

            }

        }

        //finding the max number for a category, for normalization purposes
        for (String string : multisetCategoryNames.elementSet()) {
            if (maxCountCategory < multisetCategoryNames.count(string)) {
                maxCountCategory = multisetCategoryNames.count(string);
            }
        }

        //writing of the first line of the csv: headers of the categories.
        for (Edge edge : edges.elementSet()) {
            //we devalue the weight of an edge by how frequent the 2 nodes of the edge are.
            float weight = edges.count(edge) / (float) (multisetCategoryNames.count(edge.getNode1()) * multisetCategoryNames.count(edge.getNode2()));
//            float weight = edges.count(edge);
            //normalization to a 0 -> 10 scale to visualize the weight on Gephi
            weight = weight * 10 / (float) maxCountCategory * 100000;
            sb.append(edge.getNode1()).append(",").append(edge.getNode2()).append(",Undirected,").append(weight);
            sb.append("\n");
        }
        bw.write(sb.toString());
        bw.close();
    }

    public void writeJournalsAndTheirCategories(Set<BibTexRef> refs, Integer minNumber) throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(folder + "journals and their categories.csv"));
//        BufferedWriter bwJournals = new BufferedWriter(new FileWriter(folder + "journals.csv"));
        StringBuilder sb = new StringBuilder();
        String sep = "|";

        //creation of convenient data structures for I/O
        Map<String, Multiset<String>> journalsAndTheirCategories = new HashMap();
        Multiset journals = HashMultiset.create();

        JournalAbbreviationsMapping jmap = new JournalAbbreviationsMapping();
        jmap.loadMap();

        for (BibTexRef ref : refs) {
            Set<Category> categories = ref.getCategories();
            String title = ref.getJournal();
            if (title == null || title.isEmpty()) {
                continue;
            }
            title = title.toLowerCase();

            Set<String> abbrev = (Set<String>) jmap.getJournalsToAbbrev().get(title);
            if (abbrev == null || abbrev.isEmpty()) {
                abbrev = new HashSet();
                abbrev.add(title);
            }

            String abbreviation = abbrev.iterator().next();

            journals.add(abbreviation);
            if (!journalsAndTheirCategories.containsKey(abbreviation)) {
                Multiset<String> cats = HashMultiset.create();
                journalsAndTheirCategories.put(abbreviation, cats);
            }

            for (Category category : categories) {
                journalsAndTheirCategories.get(abbreviation).add(category.getCategoryName());
            }
        }

        for (String journal : journalsAndTheirCategories.keySet()) {
            if (journals.count(journal) < minNumber) {
                continue;
            }

            for (String category : journalsAndTheirCategories.get(journal).elementSet()) {
                sb.append(journal).append(sep).append(category).append(sep).append(journalsAndTheirCategories.get(journal).count(category)).append("\n");
            }
        }
        bw.write(sb.toString());
        bw.close();
//        sb = new StringBuilder();
//        for (String journal : journalsAndTheirCategories.keySet()) {
//            Set<String> abbrev = (Set<String>) jmap.getJournalsToAbbrev().get(journal);
//            if (abbrev == null || abbrev.isEmpty()) {
//                abbrev = new HashSet();
//                abbrev.add(journal);
//            }
//            sb.append(journal).append(sep).append(abbrev.iterator().next()).append("\n");
//        }
//        bwJournals.write(sb.toString());
//        bwJournals.close();
    }

    public void writeJournalsPerCategories(Set<BibTexRef> refs) throws IOException {
        JournalAbbreviationsMapping jmap = new JournalAbbreviationsMapping();
        jmap.loadMap();

        BufferedWriter bw = new BufferedWriter(new FileWriter(folder + "journals per categories.csv"));

        StringBuilder sb = new StringBuilder();
        String sep = "|";

        //creation of 2 convenient data structures for I/O
        Map<String, Multiset<String>> categoriesToJournals = new TreeMap();
        List<String> categoryNames = new ArrayList();

        for (BibTexRef ref : refs) {
            Set<Category> categories = ref.getCategories();

            String title = ref.getJournal();
            if (title == null || title.isEmpty()) {
                continue;
            }
            title = title.toLowerCase();

            Set<String> abbrev = (Set<String>) jmap.getJournalsToAbbrev().get(title);
            if (abbrev == null || abbrev.isEmpty()) {
                abbrev = new HashSet();
                abbrev.add(title);
            }

            String abbreviation = abbrev.iterator().next();

            for (Category category : categories) {
                if (!categoryNames.contains(category.getCategoryName())) {
                    categoryNames.add(category.getCategoryName());
                }
                if (categoriesToJournals.containsKey(category.getCategoryName())) {
                    categoriesToJournals.get(category.getCategoryName()).add(abbreviation);
                } else {
                    Multiset<String> journalsForOneCategory = HashMultiset.create();
                    journalsForOneCategory.add(abbreviation);

                    categoriesToJournals.put(category.getCategoryName(), journalsForOneCategory);
                }
            }

        }
        Collections.sort(categoryNames);

        //writing of the first line of the csv: headers of the categories.
        for (String categoryName : categoryNames) {
            sb.append(categoryName);
            sb.append(sep);
        }
        sb.append("\n");

        //writing of all subsequent lines: one per year
        int countCategoriesdone = 0;
        boolean continueLoop = true;
        while (continueLoop) {

            for (Iterator<String> it = categoriesToJournals.keySet().iterator(); it.hasNext();) {
                String category = it.next();
                Multiset<String> journalsForOneCategory = categoriesToJournals.get(category);

                Iterator<String> journalsIterator = Multisets.copyHighestCountFirst(journalsForOneCategory).elementSet().iterator();
                if (journalsIterator.hasNext()) {
                    String journal = journalsIterator.next();
                    sb.append(journal).append(" (").append(journalsForOneCategory.count(journal)).append(")").append(sep);
                    journalsForOneCategory.remove(journal, journalsForOneCategory.count(journal));
                } else {
                    sb.append(sep);
                }
            }
            sb.append("\n");

            for (String cat : categoriesToJournals.keySet()) {
                if (categoriesToJournals.get(cat).isEmpty()) {
                    countCategoriesdone++;
                }
            }
            if (countCategoriesdone == categoryNames.size()) {
                continueLoop = false;
            } else {
                countCategoriesdone = 0;
            }

        }

        bw.write(sb.toString());
        bw.close();

    }
}
