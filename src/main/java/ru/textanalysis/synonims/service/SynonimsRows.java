package ru.textanalysis.synonims.service;

import ru.textanalysis.synonims.data.Word;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class SynonimsRows {

    static long usedBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    static long time = System.currentTimeMillis();

    private static final Logger logger = Logger.getLogger("system-log");
    
    private final Pattern SPLITTER = Pattern.compile(";");  
    private final Pattern SPLITTER_FREQ = Pattern.compile(":");   
    // private final Charset CURRENT_CHARSET = Charset.defaultCharset();
    private final Charset CURRENT_CHARSET = Charset.forName("utf-8");

    private HashMap<Integer, HashMap<String, ArrayList<Word>>> synLists;
    private HashMap<Integer, String> integerMapToDictionaries;

    public SynonimsRows() {        
        this.integerMapToDictionaries = new HashMap<Integer, String>();
        this.synLists = new HashMap<Integer, HashMap<String, ArrayList<Word>>>();
    }

    public HashMap<Integer, HashMap<String, ArrayList<Word>>> getSynLists() {
        return synLists;
    }

    public HashMap<String, ArrayList<Word>> getCurrentList() {
        return synLists.get(integerMapToDictionaries.get(0));
    }

    public HashMap<String, ArrayList<Word>> setOfAllSyn(String path) {
        try {
            logger.info("Upload the common dictionary of synonyms");

            //InputStreamReader fr = new InputStreamReader(new FileInputStream(path));
            File words = new File(path);
            Scanner scan_s = new Scanner(words, "utf-8");

            HashMap<String, ArrayList<Word>> syn = new HashMap<String, ArrayList<Word>>();

            while (scan_s.hasNextLine()) {

                String line = scan_s.nextLine();
                String[] strings = SPLITTER.split(line);

                String key = null;
                ArrayList<Word> tempList = new ArrayList<Word>();

                for (int i = 0; i < strings.length; i++) {
                    Word temp = new Word();
                    if (i == 0) {
                        key = strings[0];
                    } else {
                        temp = new Word(strings[i]);
                        tempList.add(temp);
                    }
                }
                syn.put(key, tempList);
//                for (Word w : syn.get(key)){
//                    logger.info(w.getWord());
//                }
            }
            scan_s.close();
            //fr.close();
            
            logger.info("Upload the common dictionary of synonyms is successful");

            return syn;

        } catch (IOException ex) {
            logger.log(Level.SEVERE, "SynonimsRows, setOfAllSyn", ex);
            return null;
        }
    }

    public HashMap<String, ArrayList<Word>> setSyn(String path) {
        try {
            logger.info("Upload a dictionary of synonyms " + path);

            File words = new File(path);
            Scanner scan_s = new Scanner(words, "utf-8");

            HashMap<String, ArrayList<Word>> syn = new HashMap<String, ArrayList<Word>>();

            while (scan_s.hasNextLine()) {

                String line = scan_s.nextLine();

                String[] strings = SPLITTER.split(line);

                String key = null;
                ArrayList<Word> sub_list = new ArrayList<Word>();

                for (int i = 0; i < strings.length; i++) {

                    if (i == 0) {
                        String[] strings2 = SPLITTER_FREQ.split(strings[i]);
                        key = strings2[0];
                    } else {

                        String[] strings2 = SPLITTER_FREQ.split(strings[i]);
                        Word temp = new Word();
                        if (strings2.length == 1) {
                            temp = new Word(strings2[0]);
                        } else {
                            temp = new Word(strings2[0], Integer.parseInt(strings2[1]));
                        }
                        sub_list.add(temp);
                    }
                }
                syn.put(key, sub_list);
            }
            scan_s.close();
            //fr.close();
            
            logger.info("Upload a dictionary of synonyms " + path + "is successful");
            return syn;

        } catch (IOException ex) {
            logger.log(Level.SEVERE, "SynonimsRows, setSyn", ex);
            return null;
        }
    }

    public void setPathOfSyn() {
        try {
            logger.info("Upload the integerStringHashMap-table dictionary of synonyms");

            File file = new File("path.txt");
            String path = file.getAbsolutePath().toString();
            logger.info(path);
            InputStreamReader fr = new InputStreamReader(new FileInputStream("path.txt"), CURRENT_CHARSET);                       
            Scanner scan = new Scanner(fr);

            int count = 0;

            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                this.integerMapToDictionaries.put(count, line);
                count++;
                System.out.println(line);
            }

            scan.close();
            fr.close();
            logger.info("Upload the integerStringHashMap-table dictionary of synonyms is successful");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "SynonimsRows, setPathOfSyn", e);
        }
    }

    public void setAllSyn() {
        try {
            logger.info("Upload dictionaries of synonyms");

            for (int i = 0; i < this.integerMapToDictionaries.size(); i++) {
                if (i == 0) {
                    synLists.put(i, setOfAllSyn(this.integerMapToDictionaries.get(i)));
                } else {
                    synLists.put(i, setSyn(this.integerMapToDictionaries.get(i)));
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "SynonimsRows, setAllSyn", e);
        }
    }

    public ArrayList<Word> contain(String word, int idDicSyn) {
        try {
            logger.info("The beginning of the word search");
            HashMap<String, ArrayList<Word>> tempList = this.synLists.get(idDicSyn);

            ArrayList<Word> temp = tempList.get(word);
            if (temp != null) {
                return temp;
            } else {
                return new ArrayList<Word>();
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "SynonimsRows, contain", e);
            return new ArrayList<Word>();
        }
    }

    public void printSyn(String word, ArrayList<Word> synList) {
        try {
            if (synList.isEmpty()) {
                System.out.println("У слова \"" + word + "\" нет синонимов в данном словаре.");
            } else {
                System.out.println("Синонимы к слову \"" + word + "\": ");

                for (Word element : synList) {
                    System.out.println(element.getWord() + ";");
                }
            }
            System.out.println();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "SynonimsRows, printSyn", e);
        }
    }

    public HashMap<Integer, String> getIntegerMapToDictionaries() {
        return integerMapToDictionaries;
    }

    //    public static void main(String args[]) throws IOException {
//        System.out.println((System.currentTimeMillis() - time) / 360);
//
//        try {
//            SynonimsRows run = new SynonimsRows();
//            run.setPathOfSyn();
//            run.setAllSyn();
//            System.out.println((System.currentTimeMillis() - time) / 360);
//            String word = "глава";
//
//            for (int i = 0; i < run.integerStringHashMap.size(); i++) {
//                System.out.println(run.integerStringHashMap.get(i));
//                ArrayList<Word> syn = run.contain(word, i);
//                run.printSyn(word, syn);
//            }
//
//            System.out.println("Введено некорректное слово");
//
//        } catch (Exception e) {
//            System.out.println("Ошибка выполнения.");
//        }
//    }
}
