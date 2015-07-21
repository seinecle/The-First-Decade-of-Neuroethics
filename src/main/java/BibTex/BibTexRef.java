/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BibTex;

import Categorization.Category;
import Utils.Author;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author C. Levallois
 */
public class BibTexRef {

    String abs;
    String annote;
    List<Author> authors;
    String bookTitle;
    String chapter;
    String crossref;
    String edition;
    List<Author> editors;
    String eprint;
    String howpublished;
    String id;
    String institution;
    String journal;
    Set<String> keywords;
    String month;
    String note;
    String number;
    String organization;
    String paperTitle;
    String pages;
    String publisher;
    String school;
    String series;
    String title;
    String type;
    String volume;
    String url;
    String year;
    String outlet;
    String crLf = Character.toString((char)13) + Character.toString((char)10);

    
    
    private Set<Category> categories;

    public BibTexRef() {
        authors = new ArrayList();
        editors = new ArrayList();
        keywords = new HashSet();
        categories = new HashSet();

    }

    public String getAbs() {
        return abs;
    }

    public void setAbs(String abs) {
        abs = StringUtils.removeEnd(abs, "}");
        abs = StringUtils.removeStart(abs, "\\{");
        this.abs = abs;
    }

    public String getAnnote() {
        return annote;
    }

    public void setAnnote(String annote) {
        annote = StringUtils.removeEnd(annote, "}");
        annote = StringUtils.removeStart(annote, "\\{");

        this.annote = annote;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public void addAuthor(Author author) {
        this.authors.add(author);
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        bookTitle = StringUtils.removeEnd(bookTitle, "}");
        bookTitle = StringUtils.removeStart(bookTitle, "\\{");
        this.bookTitle = bookTitle;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        chapter = StringUtils.removeEnd(chapter, "}");
        chapter = StringUtils.removeStart(chapter, "\\{");

        this.chapter = chapter;
    }

    public String getCrossref() {
        return crossref;
    }

    public void setCrossref(String crossref) {
        crossref = StringUtils.removeEnd(crossref, "}");
        crossref = StringUtils.removeStart(crossref, "\\{");
        this.crossref = crossref;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        edition = StringUtils.removeEnd(edition, "}");
        edition = StringUtils.removeStart(edition, "\\{");

        this.edition = edition;
    }

    public List<Author> getEditors() {
        return editors;
    }

    public void setEditors(List<Author> editors) {
        this.editors = editors;
    }

    public String getEprint() {
        return eprint;
    }

    public void setEprint(String eprint) {
        this.eprint = eprint;
    }

    public String getHowpublished() {
        return howpublished;
    }

    public void setHowpublished(String howpublished) {
        this.howpublished = howpublished;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        journal = StringUtils.removeEnd(journal, "}");
        journal = StringUtils.removeStart(journal, "\\{");
        this.journal = journal;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        month = StringUtils.removeEnd(month, "}");
        month = StringUtils.removeStart(month, "\\{");
        this.month = month.trim();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        organization = StringUtils.removeEnd(organization, "}");
        organization = StringUtils.removeStart(organization, "\\{");
        this.organization = organization.trim();
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        pages = StringUtils.removeEnd(pages, "}");
        pages = StringUtils.removeStart(pages, "\\{");
        this.pages = pages.trim();
    }

    public String getPaperTitle() {
        return paperTitle;
    }

    public void setPaperTitle(String paperTitle) {
        paperTitle = StringUtils.removeEnd(paperTitle, "}");
        paperTitle = StringUtils.removeStart(paperTitle, "\\{");
        this.paperTitle = paperTitle.trim();
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        publisher = StringUtils.removeEnd(publisher, "}");
        publisher = StringUtils.removeStart(publisher, "\\{");
        this.publisher = publisher.trim();
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getTitle() {
        if (this.getChapter() != null) {
            return this.getChapter();
        }
        if (this.getPaperTitle() != null) {
            return this.getPaperTitle();
        }
        return "";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        url = StringUtils.removeEnd(url, "}");
        url = StringUtils.removeStart(url, "\\{");
        this.url = url.trim();
    }

    public String getYear() {
        if (year == null) {
            return "null";
        } else {
            return year;
        }
    }

    public void setYear(String year) {
        year = StringUtils.removeEnd(year, "}");
        year = StringUtils.removeStart(year, "\\{");
        this.year = year.trim();
    }

    public String getOutlet() {
        if (this.getBookTitle() != null) {
            return this.getBookTitle();
        }
        if (this.getJournal() != null) {
            return this.getJournal();
        }
        return "";
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public String toBibliographicalFormattedString() {
        StringBuilder sb = new StringBuilder();
        for (Author author : authors) {
            sb.append(author.getFullnameWithComma()).append(", ");
        }
        if (sb.length() > 1) {
            sb.substring(0, sb.length() - 2);
        }
        sb.append(". ");
        sb.append(this.getYear());
        sb.append(". ");
        sb.append(this.getTitle());
        sb.append(". ");
        sb.append(this.getOutlet());
        sb.append(". \t\t");
        sb.append(this.getAbs());
        return sb.toString();
    }

}
