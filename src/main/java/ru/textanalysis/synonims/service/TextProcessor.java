package ru.textanalysis.synonims.service;

import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import ru.textanalysis.synonims.data.Word;
import ru.textanalysis.tfwwt.jmorfsdk.JMorfSdk;
import ru.textanalysis.tfwwt.jmorfsdk.load.JMorfSdkLoad;

@Component
public class TextProcessor {

    private JMorfSdk JMORFSDK = null;
//    private final String LINE_SEPARATOR = System.getProperty("line.separator");

//    private final String ANY_APOSTROPHE = "[’]";
//    private final String AVAILABLE_APOSTROPHE = "'";
//    private final String SPACES_MORE_ONE = "\\s{2,}";
//    private final String SPACE = " ";
//    private final Pattern SPLITTER = Pattern.compile(";");  
//    private final Pattern SPLITTER_FREQ = Pattern.compile(":");   
//    private final Pattern ALL_WORDS = Pattern.compile("Всего осмысленных слов");
//    private final Charset CURRENT_CHARSET = Charset.defaultCharset();
    private final Pattern LIBRARY_OBSCENE = Pattern.compile("[ оаъ]+[её]+ба[тлн]+|[ оаъ]+[её]+бл[тлн]+|пизд|хуй|хуя|хуи|хуе|анус|^трахн|[^ш]хера|херо|хрено|уеб|уёб|долбо[её]|за[её]+б|^ёба|^бля|мудак");
    private final Pattern NUMBERS = Pattern.compile("[0-9]+");
    private final Pattern SIMBOL = Pattern.compile("[a-z]");

    private final Logger logger = Logger.getLogger("system-log");
    private FileHandler fileLog = null;

    private static TextProcessor INSTANCE = null;
    private SynonimsRows synonimsRows = null;

    public JMorfSdk getJMORFSDK() {
        return JMORFSDK;
    }

    public void setJMORFSDK(JMorfSdk JMORFSDK) {
        this.JMORFSDK = JMORFSDK;
    }

    public SynonimsRows getSynonimsRows() {
        return synonimsRows;
    }

    public void setSynonimsRows(SynonimsRows synonimsRows) {
        this.synonimsRows = synonimsRows;
    }

    public static TextProcessor getInstance() {
        return INSTANCE;
    }
    
    private TextProcessor() {
        try {
            this.JMORFSDK = JMorfSdkLoad.loadFullLibrary();
            this.synonimsRows = new SynonimsRows();
            this.synonimsRows.setPathOfSyn();
            this.synonimsRows.setAllSyn();
            fileLog = new FileHandler("MyLogFile.txt", true);
            logger.addHandler(fileLog);
            logger.setLevel(Level.ALL);
            SimpleFormatter formatter = new SimpleFormatter();
            fileLog.setFormatter(formatter);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error init class TextProcessor", ex);
        }
    }

    public boolean checkPronoun(String data) {
        try {
            List<Byte> partOfSpeach = this.JMORFSDK.getTypeOfSpeechs(data);
            for (Byte part : partOfSpeach) {
                if (part == 31) {
                    return false;
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error: TextProcessor, checkPronoun", e);
            return false;
        }

        return true;
    }

    public boolean checkObscene(String data) {
        try {
            Matcher matcher = LIBRARY_OBSCENE.matcher(data);
            if (matcher.find()) {
                return false;
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error: TextProcessor, checkObscene", e);
            return false;
        }

        return true;
    }

    public boolean checkNumbers(String data) {
        try {
            List<Byte> partOfSpeach = this.JMORFSDK.getTypeOfSpeechs(data);
            for (Byte part : partOfSpeach) {
                if (part == 18) {
                    return false;
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error: TextProcessor, checkNumbers", e);
            return false;
        }

        return true;
    }

    public boolean checkSimbol(String data) {

        try {
            Matcher matcher = SIMBOL.matcher(data);
            if (matcher.find()) {
                return false;
            }
            matcher = NUMBERS.matcher(data);
            if (matcher.find()) {
                return false;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error: TextProcessor, checkSimbol", e);
            return false;
        }
        return true;
    }

    public List<Byte> getPartOfSpeach(Word word) {
        return this.JMORFSDK.getTypeOfSpeechs(word.getWord());
    }

    public String getInitialForm(String word) {
        List<String> list;
        try {
            list = this.JMORFSDK.getStringInitialForm(word);
            return list.get(0);
        } catch (Throwable e) {
            logger.log(Level.SEVERE, "Error: TextProcessor, getPartOfSpeach", e);
        }
        return null;
    }

}

