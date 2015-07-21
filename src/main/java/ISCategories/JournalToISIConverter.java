/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ISCategories;

import BibTex.BibTexRef;
import ResourceLoaders.JournalAbbreviationsMapping;
import ResourceLoaders.JournalToISICategoriesMapping;
import com.google.common.collect.Multimap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

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
public class JournalToISIConverter {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        new JournalToISIConverter().convertAbbreviatedJournalsToISI(null, null);
    }

    //this method takes a file as input. This file should contain one ABBREVIATED name of a journal per line, that's all
    public void convertAbbreviatedJournalsToISI(Set<BibTexRef> refs, String filePath) throws FileNotFoundException, IOException {

        BufferedReader br = new BufferedReader(new FileReader("H:\\files\\NESSHI\\Project with Elisabeth\\Bibliography\\Topic Classification\\two groups\\cluster0.txt"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("H:\\files\\NESSHI\\Project with Elisabeth\\Bibliography\\Topic Classification\\two groups\\cluster0_ISI.txt"));

        JournalToISICategoriesMapping ISIMapper = new JournalToISICategoriesMapping();
        ISIMapper.loadMap();
        Multimap<String, String> mapISI = ISIMapper.getJournalsToISI();

        JournalAbbreviationsMapping abbrevMapper = new JournalAbbreviationsMapping();
        abbrevMapper.loadMap();
        Multimap<String, String> mapAbbrevToJournals = abbrevMapper.getAbbrevToJournals();
        Multimap<String, String> mapJournalsToAbbrev = abbrevMapper.getJournalsToAbbrev();


        StringBuilder sb = new StringBuilder();

        String line;
        //for each journal name in the gephi cluster
        while ((line = br.readLine()) != null) {
            for (BibTexRef ref : refs) {
                String journalTitle = ref.getJournal();
                if (journalTitle == null || journalTitle.isEmpty()) {
                    continue;
                }
                journalTitle = journalTitle.toLowerCase();
                Set<String> journals = (Set<String>) mapAbbrevToJournals.get(line.toLowerCase());
                if (journals == null || journals.isEmpty()) {
                    continue;
                }

                boolean journalMatch = false;
                String match = null;
                for (String journal : journals) {
                    if (journalTitle.equals(journal)) {
                        match = journal;
                        journalMatch = true;
                        break;
                    }
                }
                if (!journalTitle.equals(line.toLowerCase()) & !journalMatch) {
                    continue;
                }

                Set<String> ISIs = (Set<String>) mapISI.get(match);
                if (ISIs == null) {
                    System.out.println("ISI not found for this journal: " + match);
                    continue;
                }

                for (String ISI : ISIs) {
                    sb.append(ISI).append("\n");
                }
            }
        }
        br.close();
        bw.write(sb.toString());
        bw.close();
    }
}
