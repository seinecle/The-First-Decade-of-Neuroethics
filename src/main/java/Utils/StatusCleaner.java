/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import com.google.common.collect.Multiset;
import java.util.Iterator;

public class StatusCleaner {

    public static String clean(String status) {
        if (status == null) {
            return "";
        }
        status = status.replace("...", " ");
        status = status.replace(",", " ");
        status = status.replace("..", " ");
//            System.out.println(status);
        status = status.replaceAll("http[^ ]*", " ");
//        status = status.replaceAll("\".*\"", " ");
        status = status.replaceAll("http.*[\r|\n]*", " ");
        status = status.replaceAll(" +", " ");

        return status;
    }

    public static String removePunctuationSigns(String string) {
        if (string == null) {
            return "";
        }
        string = string.replace("'s", " ");
        string = string.replace("’s", " ");
        string = string.replace("l'", " ");
        string = string.replace("l’", " ");
        String punctuation = "!?.@'’`+<>\"«»:-+,|$;_/~&()[]{}#=*";
        char[] chars = punctuation.toCharArray();
        for (char currChar : chars) {
            string = string.replace(String.valueOf(currChar), " ");
        }
        return string.trim();
    }

    public Multiset<String> removeSmallWords(Multiset<String> terms) {

        Iterator<String> it = terms.iterator();
        while (it.hasNext()) {
            String string = it.next();
            if (string.length() < 3 | string.matches(".*\\d.*")) {
                it.remove();
            }
        }
        return terms;

    }
}
