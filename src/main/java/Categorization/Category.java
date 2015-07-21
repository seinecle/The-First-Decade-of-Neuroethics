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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Category {

    private String categoryName;
    private List<String> allKeywords;
    private List<String> allExclusionKeywords;
    private List<String> necessaryKeywords;
    private Integer minNumberKeywords;
    private Set<String> decisiveKeywords;
    private Integer confidence;
    private String superCategory;

    public Category() {
        this.allKeywords = new ArrayList();
        this.allExclusionKeywords = new ArrayList();
        this.decisiveKeywords = new HashSet();
    }

    public Category(String categoryName, List<String> allKeyWords, List<String> allExclusionKeywords, List<String> necessaryKeyWords, Integer minNumberKeywords, Set<String> decisiveKeywords) {
        this.categoryName = categoryName;
        this.allKeywords = allKeyWords;
        this.necessaryKeywords = necessaryKeyWords;
        this.minNumberKeywords = minNumberKeywords;
        this.decisiveKeywords = decisiveKeywords;
        this.allExclusionKeywords = allExclusionKeywords;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<String> getAllKeyWords() {
        return allKeywords;
    }

    public void setAllKeyWords(List<String> allKeyWords) {
        this.allKeywords = allKeyWords;
    }

    public List<String> getAllExclusionKeywords() {
        return allExclusionKeywords;
    }

    public void setAllExclusionKeywords(List<String> allExclusionKeywords) {
        this.allExclusionKeywords = allExclusionKeywords;
    }

    public void addKeyword(String keyword) {
        allKeywords.add(keyword);
    }

    public void addExclusionKeyword(String keyword) {
        allExclusionKeywords.add(keyword);
    }

    public void addNecessaryKeyword(String necessaryKeyword) {
        necessaryKeywords.add(necessaryKeyword);
    }

    public void addDecisiveKeyword(String necessaryKeyword) {
        decisiveKeywords.add(necessaryKeyword);
    }

    public List<String> getNecessaryKeyWords() {
        return necessaryKeywords;
    }

    public void setNecessaryKeyWords(List<String> necessaryKeyWords) {
        this.necessaryKeywords = necessaryKeyWords;
    }

    public Integer getMinNumberKeywords() {
        if (minNumberKeywords == null) {
            minNumberKeywords = 0;
        }
        return minNumberKeywords;
    }

    public void setMinNumberKeywords(Integer minNumberKeywords) {
        this.minNumberKeywords = minNumberKeywords;
    }

    public Set<String> getDecisiveKeywords() {
        return decisiveKeywords;
    }

    public void setDecisiveKeywords(Set<String> decisiveKeywords) {
        this.decisiveKeywords = decisiveKeywords;
    }

        
    public Integer getConfidence() {
        return confidence;
    }

    public void setConfidence(Integer confidence) {
        this.confidence = confidence;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + (this.categoryName != null ? this.categoryName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Category other = (Category) obj;
        if ((this.categoryName == null) ? (other.categoryName != null) : !this.categoryName.equals(other.categoryName)) {
            return false;
        }
        return true;
    }

    public String getSuperCategory() {
        return superCategory;
    }

    public void setSuperCategory(String superCategory) {
        this.superCategory = superCategory;
    }
    
    
    
    
}
