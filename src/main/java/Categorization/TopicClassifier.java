/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 Copyright 2013 Clement Levallois
 Authors : Clement Levallois <clement.levallois@gephi.org>
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

 Contributor(s):

 Portions Copyrighted 2011 Gephi Consortium.
 */
package Categorization;

import BibTex.BibTexRef;
import Utils.NGramFinder;
import Utils.StatusCleaner;
import com.csvreader.CsvReader;
import com.google.common.collect.Multiset;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class TopicClassifier {

    String folder = "H:\\files\\NESSHI\\Project with Elisabeth\\Bibliography\\Topic Classification\\";
    Set<BibTexRef> setRefs;
    Multiset<String> ngrams;
    NGramFinder ngramFinder;
    List<Item> items = new ArrayList();
    List<BibTexRef> refs = new ArrayList();
    String abstractPaper;
    String titlePaper;
    String crLf = Character.toString((char) 13) + Character.toString((char) 10);

    public TopicClassifier() {
        try {
            CategoriesLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(TopicClassifier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
            Logger.getLogger(TopicClassifier.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void loadAbstractsFromCsv() throws FileNotFoundException, IOException {
        String fileName = "abstracts.csv";
        String fieldDelimiter = ",";
        String textDelimiter = "\"";

        //reads the csv file of abstracts
        CsvReader csvReader = new CsvReader(new BufferedReader(new FileReader(folder + fileName)), fieldDelimiter.charAt(0));
        csvReader.setTextQualifier(textDelimiter.charAt(0));
        csvReader.setUseTextQualifier(true);
        String[] values;

        //loops through abstracts
        while (csvReader.readRecord()) {
            values = csvReader.getValues();
            abstractPaper = values[2];

            titlePaper = values[1];
            if (abstractPaper.equals("null")) {
                continue;
            }
            Item item = new Item(abstractPaper);
            item.setTitlePaper(titlePaper);
            items.add(item);
        }
        csvReader.close();

    }

    public void loadAbstractsFromReferences(Set<BibTexRef> setRefs) {
        this.refs.addAll(setRefs);
        System.out.println("items: " + refs.size());
    }

    public void execute() throws FileNotFoundException, IOException, InvalidFormatException {

        for (BibTexRef ref : refs) {
            String[] keywords = null;
            if (!ref.getKeywords().isEmpty()) {
                keywords = ref.getKeywords().toArray(new String[ref.getKeywords().size()]);
            }
            abstractPaper = ref.getAbs() + " " + Arrays.toString(keywords) + " " + ref.getTitle();
            abstractPaper = StatusCleaner.removePunctuationSigns(abstractPaper).toLowerCase();

//            if (abstractPaper != null && abstractPaper.contains("activity in the anterior ventromedial prefrontal cortex correlated with pride and guilt")) {
//                System.out.println("fmri abstract!");
//            }

            ngramFinder = new NGramFinder(abstractPaper);
            ngrams = ngramFinder.runIt(4, true);

            Set<String> potentialCategories = new HashSet();

            //discover potential categories
            for (String string : ngrams.elementSet()) {
                string = string.trim().toLowerCase();
                if (string.isEmpty()) {
                    continue;
                }
//                if (string.equals("fmri")) {
//                    System.out.println("fmri!!");
//                }

                for (Category category : CategoriesLoader.getCategories()) {
                    String stringStem = null;

                    for (String keyword : category.getDecisiveKeywords()) {

                        if (keyword.endsWith("*")) {
                            int endChar = Math.min(string.length(), keyword.length() - 1);
                            stringStem = string.substring(0, endChar);
                            keyword = keyword.substring(0, keyword.length() - 1);

                        } else {
                            stringStem = string;
                        }

                        if (keyword.equals(stringStem)) {
                            category.setConfidence(5);
                            ref.addCategory(category);
                        }
                    }

                    for (String keyword : category.getAllKeyWords()) {
                        if (keyword.endsWith("*")) {
                            int endChar = Math.min(string.length(), keyword.length() - 1);
                            stringStem = string.substring(0, endChar);
                            keyword = keyword.substring(0, keyword.length() - 1);
                        } else {
                            stringStem = string;
                        }
//                        if (keyword.equals("fmri")) {
//                            System.out.println("allo fmri~");
//                        }
                        if (keyword.equals(stringStem)) {
                            potentialCategories.add(stringStem);
                        }
                    }

                    for (String keyword : category.getAllExclusionKeywords()) {
                        if (keyword.endsWith("*")) {
                            stringStem = string.substring(0, keyword.length() - 2);
                        } else {
                            stringStem = string;
                        }
                        if (keyword.equals(stringStem)) {
                            potentialCategories.remove(category);
                        }
                    }
                }
            }

            //checks that miminum thresholds are met.
            for (Category category : CategoriesLoader.getCategories()) {
                Set<String> setKeyWordsCategory = new HashSet();
                for (String keyword : category.getAllKeyWords()) {
                    if (keyword.endsWith("*")) {
                        keyword = keyword.substring(0, keyword.length() - 1);
                    }
                    setKeyWordsCategory.add(keyword);
                }
                int counter = 0;
                for (String detectedKeyWord : potentialCategories) {
                    if (setKeyWordsCategory.contains(detectedKeyWord)) {
                        counter++;
                    }
                }
                if (counter >= category.getMinNumberKeywords()) {
                    category.setConfidence(counter);
                    ref.addCategory(category);
                }
            }
        }
    }
}
