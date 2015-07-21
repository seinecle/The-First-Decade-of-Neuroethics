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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class CategoriesLoader {

    private static final String fileName = "H:\\files\\NESSHI\\Project with Elisabeth\\Bibliography\\Topic Classification\\keywords for classification\\keywords_revised_18JAN2013.xlsx";
    private static List<Category> categories = new ArrayList();
    private static final int maxColKeyWords = 32;

    public static void echoAsCSV(Sheet sheet) throws IOException {
        Row row;
        int startingRow = 2;
        Category category;
        boolean breakNow = false;
        for (int i = startingRow; i <= sheet.getLastRowNum(); i++) {
            if (breakNow) {
                break;
            }
            row = sheet.getRow(i);
            if (row == null) {
                break;
            }
            category = new Category();

            for (int j = 1; j < row.getLastCellNum(); j++) {

                //label of the category
                if (j == 1) {
                    if (row.getCell(j).getStringCellValue().isEmpty() || row.getCell(j).getStringCellValue() == null) {
                        breakNow = true;
                        break;
                    }
                    category.setCategoryName("CAT_"+row.getCell(j).getStringCellValue());
                }
                //if a cell is null, the row is empty.
                if (row.getCell(j) == null) {
                    continue;
                }

                //check the keywords
                if (j > 1 & j <= maxColKeyWords) {
                    System.out.println(row.getCell(j).getStringCellValue());
                    if (row.getCell(j).getStringCellValue().startsWith("NOT ")) {
                        category.addExclusionKeyword(row.getCell(j).getStringCellValue().toLowerCase().substring(4).trim());
                    } else {
                        category.addKeyword(row.getCell(j).getStringCellValue().toLowerCase().trim());
                    }
                }
                //check the min number of keywords that should be present in the text to match a classification
                if (j == maxColKeyWords + 1) {
                    System.out.println("min Words: "+row.getCell(j).getStringCellValue());
                    category.setMinNumberKeywords(Integer.parseInt(row.getCell(j).getStringCellValue()));
                }

                //check if single terms can lead to a direct classification
                if (j == maxColKeyWords + 2) {
                    String[] directWords = row.getCell(j).getStringCellValue().split(";");
                    for (String string : directWords) {
                        category.addDecisiveKeyword(string.toLowerCase().trim());
                    }
                }
                //what supercategory the category belongs to
                if (j == maxColKeyWords + 3) {
                        category.setSuperCategory(row.getCell(j).getStringCellValue());
                    }
                
            }
            if (!category.getMinNumberKeywords().equals(0)) {
                categories.add(category);
            }
        }
    }

    public static void load() throws FileNotFoundException, IOException, InvalidFormatException {
        InputStream inp;
        inp = new FileInputStream(fileName);
        Workbook wb = WorkbookFactory.create(inp);

//        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
        for (int i = 0; i < 2; i++) {
            echoAsCSV(wb.getSheetAt(i));
        }
        inp.close();
    }

    public static List<Category> getCategories() {
        return categories;
    }

    public static void setCategories(List<Category> categories) {
        CategoriesLoader.categories = categories;
    }
}
