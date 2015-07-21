/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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
public class Stopwords {

    private static final String[] twitterStopWords = {"rt","w/"};
    private static final String[] commonStopWords = {"1","2","3","4","5","6","7","8","9","10","20","25","30","40","50","100","1000"};

//    private static final String enStopWords = "i,twitter feed,twitter time,twitter tm,twitter account,twitter page,official twitter,occasional,specialized,specializing,expert,expertise,views,own,my own, own views,employer,opinions,opinion,lover,email,gmail,com,mail,don t,organization,official,service,services,executive,operational,manager,staff,organization,corporation,company,limited,holding,inc,tm,gmbh,bv,corp,stuff,director,associate,president,employee,colleague,visiting,assistant,professor,researcher,click,www,http,cgi,html,quote,href,url,com,org,the,of,and,to,in,i,that,was,his,he,it,van,und,with,is,for,as,had,you,not,be,her,on,at,by,which,have,were,or,from,this,him,but,all,she,they,were,my,are,me,one,their,so,an,said,them,we,who,would,been,will,no,when,there,if,more,out,up,into,do,any,your,what,has,man,could,other,than,our,some,very,time,upon,about,may,its,only,now,like,little,then,can,should,made,did,us,such,a,great,before,must,two,these,see,know,over,much,down,after,first,mr,good,men,own,never,most,old,shall,day,where,those,came,come,himself,way,work,life,without,go,make,well,through,being,long,say,might,how,am,m,ve,too,even,def,again,many,back,here,think,every,people,went,same,last,thought,away,under,take,found,still,place,while,just,also,yet,though,against,things,get,ever,give,years,off,nothing,right,once,another,left,part,saw,three,took,new,during,always,mrs,put,each,between,tell,mind,few,because,thing,whom,far,seemed,looked,called,whole,de,set,both,got,find,done,heard,look,name,days,told,let,asked,going,seen,better,p,having,home,knew,side,something,moment,among,course,enough,words,soon,full,end,gave,room,almost,small,thou,cannot,want,however,light,quite,brought,nor,word,whose,given,best,turned,taken,does,use,myself,felt,until,since,themselves,used,rather,began,present,others,works,less,next,stood,form,within,together,till,large,matter,kind,often,certain,herself,year,half,order,round,true,anything,keep,sent,means,believe,passed,feet,near,state,hundred,thus,hope,alone,above,case,dear,thee,says,person,high,already,received,fact,gone,known,least,perhap,perhaps,sure,indeed,open,itself,along,return,leave,answered,either,help,lay,point,four,2,number,therefore,hour,held,several,whether,er,manner,second,replied,united,call,general,why,behind,became,become,forth,looking,really,towards,kept,short,following,five,able,live,jan,feb,mar,apr,may,jun,jul,aug,sep,sept,oct,nov,dec,around,ask,beautiful,character,sort,ten,fine,ready,common,talk,account,mark,interest,written,can't,necessary,age,else,idea,longer,spoke,across,early,ought,sometimes,line,saying,appeared,continued,information,later,everything,reached,suddenly,past,hours,strange,deep,change,miles,feeling,further,purpose,happy,added,seem,taking,beyond,neither,forward,i've,i'd,i'll,i'm,you're,don't,aren't,etc,position,none,entered,clear,late,stand,suppose,la,und,real,nearly,mine,comes,toward,bad,cut,copy,six,living,didn't,low,effect,fall,service,below,except,pass,doing,opened,an',4,note,although,wanted,makes,tried,front,big,dr,lived,certainly,receive,unto,placed,probably,glad,important,especially,greater,yourself,fellow,ago,remained,latter,wrong,various,persons,particular,according,deal,follow,try,u,won't,march,whatever,instead,occasion,merely,twenty,easily,afterwards,usual,born,generally,getting,worth,simple,remain,result,started,mere,beside,perfect,goes,entirely,east,quickly,meant,somewhat,sudden,direction,due,date,lives,considered,scarcely,greatest,success,proper,opportunity,considerable,seven,top,discovered,begin,";
//    private static final String frStopWords = "alors,au,aucuns,aussi,autre,avant,avec,avoir,bon,car,ce,cela,ces,ceux,chaque,ci,comme,comment,dans,des,du,dedans,dehors,depuis,deux,devrait,doit,donc,dos,droite,début,elle,elles,en,encore,essai,est,et,eu,fait,faites,fois,font,force,haut,hors,ici,il,ils,je,juste,la,le,les,leur,là,ma,maintenant,mais,mes,mine,moins,mon,mot,même,ni,nommés,notre,nous,nouveaux,ou,où,par,parce,parole,pas,personnes,peut,peu,pièce,plupart,pour,pourquoi,quand,que,quel,quelle,quelles,quels,qui,sa,sans,ses,seulement,si,sien,son,sont,sous,soyez,sujet,sur,ta,tandis,tellement,tels,tes,ton,tous,tout,trop,très,tu,valeur,voie,voient,vont,votre,vous,vu,ça,étaient,état,étions,été,être";
//    private static final String arStopWords = "فى,في,كل,لم,لن,له,من,هو,هي,قوة,كما,لها,منذ,وقد,ولا,نفسه,لقاء,مقابل,هناك,وقال,وكان,نهاية,وقالت,وكانت,للامم,فيه,كلم,لكن,وفي,وقف,ولم,ومن,وهو,وهي,يوم,فيها,منها,مليار,لوكالة,يكون,يمكن,مليونحيث,اكد,الا,اما,امس,السابق,التى,التي,اكثر,ايار,ايضا,ثلاثة,الذاتي,الاخيرة,الثاني,الثانية,الذى,الذي,الان,امام,ايام,خلال,حوالى,الذين,الاول,الاولى,بين,ذلك,دون,حول,حين,الف,الى,انه,اول,ضمن,انها,جميع,الماضي,الوقت,المقبل,اليوم,ـ,ف,و,و6,قد,لا,ما,مع,مساء,هذا,واحد,واضاف,واضافت,فان,قبل,قال,كان,لدى,نحو,هذه,وان,واكد,كانت,واوضح,مايو,ب,ا,أ,،,عشر,عدد,عدة,عشرة,عدم,عام,عاما,عن,عند,عندما,على,عليه,عليها,زيارة,سنة,سنوات,تم,ضد,بعد,بعض,اعادة,اعلنت,بسبب,حتى,اذا,احد,اثر,برس,باسم,غدا,شخصا,صباح,اطار,اربعة,اخرى,بان,اجل,غير,بشكل,حاليا,بن,به,ثم,اف,ان,او,اي,بها,صفر";
//    private static final String deStopWords = "aber,als,am,an,auch,auf,aus,bei,bin,bis,bist,da,dadurch,daher,darum,das,daß,dass,dein,deine,dem,den,der,des,dessen,deshalb,die,dies,dieser,dieses,doch,dort,du,durch,ein,eine,einem,einen,einer,eines,er,es,euer,eure,für,hatte,hatten,hattest,hattet,hier,hinter,ich,ihr,ihre,im,in,ist,ja,jede,jedem,jeden,jeder,jedes,jener,jenes,jetzt,kann,kannst,können,könnt,machen,mein,meine,mit,muß,mußt,musst,müssen,müßt,nach,nachdem,nein,nicht,nun,oder,seid,sein,seine,sich,sie,sind,soll,sollen,sollst,sollt,sonst,soweit,sowie,und,unser,unsere,unter,vom,von,vor,wann,warum,was,weiter,weitere,wenn,wer,werde,werden,werdet,weshalb,wie,wieder,wieso,wir,wird,wirst,wo,woher,wohin,zu,zum,zur,über";
//    private static final String nlStopWords = "aan,af,al,alles,als,altijd,andere,ben,bij,daar,dan,dat,de,der,deze,die,dit,doch,doen,door,dus,een,eens,en,er,ge,geen,geweest,haar,had,heb,hebben,heeft,hem,het,hier,hij,hoe,hun,iemand,iets,ik,in,is,ja,je,je ,kan,kon,kunnen,maar,me,meer,men,met,mij,mijn,moet,na,naar,niet,niets,nog,nu,of,om,omdat,ons,ook,op,over,reeds,te,tegen,toch,toen,tot,u,uit,uw,van,veel,voor,want,waren,was,wat,we,wel,werd,wezen,wie,wij,wil,worden,zal,ze,zei,zelf,zich,zij,zijn,zo,zonder,zou";

    public static Set<String> getStopWords(String lang) {

        Set<String> words = new HashSet();
        BufferedReader br = null;
        File file = new File("H:\\Docs Pro Clement\\E-humanities\\TextMining\\stop-words");

        for (File f : file.listFiles()) {
            if (f.getName().contains(lang + ".txt")) {
                try {
                    br = new BufferedReader(new FileReader(f));
                    String line;
                    while ((line = br.readLine()) != null) {
                        words.add(line);
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Stopwords.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Stopwords.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        br.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Stopwords.class.getName()).log(Level.SEVERE, null, ex);
                    }
                };
            }
        }
        words.addAll(Arrays.asList(twitterStopWords));

        return words;
    }

}
