package ru.textanalysis.synonims.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.textanalysis.synonims.service.TextProcessor;
import ru.textanalysis.synonims.data.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@Controller
public class SynonimsController {

    @Autowired
    private TextProcessor textProcessor = null;
    private final Logger logger = Logger.getLogger("system-log");

    @GetMapping("/")
    public String index(Model model, @RequestParam(value="name", required=false, defaultValue="World") String name) {
        model.addAttribute("name", name);
        return "index";
    }

    @GetMapping("/getsynonims")
    public String getSynonims(Model model, @RequestParam(value="word") String word, @RequestParam(value="dictionary") String dictionary) {
//        model.addAttribute("name", name);

        try {

            logger.info("Start servlet");
//            request.setCharacterEncoding("UTF-8");
//            response.setContentType("text/html");
//            String word = (String) request.getParameter("word");
            logger.warning("Recieved from client " + word);
            word = textProcessor.getInitialForm(word);
//            String idTemp = (String) request.getParameter("dictionary");
            HashMap <Integer, ArrayList <Word>> tempRes = new HashMap <Integer, ArrayList <Word>>();

            Pattern SPLITTER = Pattern.compile(",");
            String[] strings = SPLITTER.split(dictionary);
            for (String i : strings) {
                tempRes.put(Integer.parseInt(i),null);
            }

            if (!this.textProcessor.checkSimbol(word)) {
                model.addAttribute("word", word);
                model.addAttribute("err", "simbol");
                logger.info("Return: false simbol");
                return "resultError";
//                request.getRequestDispatcher("/WEB-INF/resultError.jsp").forward(request, response);
            } else if (!this.textProcessor.checkPronoun(word)) {
                model.addAttribute("word", word);
                model.addAttribute("err", "pronoun");
                logger.info("Return: false pronoun");
                return "resultError";
//                model.RequestDispatcher("/WEB-INF/resultError.jsp").forward(request, response);
            } else if (!this.textProcessor.checkObscene(word)) {
                model.addAttribute("word", word);
                model.addAttribute("err", "obscene");
                logger.info("Reture: false obscene");
                return "resultError";
//                model.getRequestDispatcher("/WEB-INF/resultError.jsp").forward(request, response);
            } else if (!this.textProcessor.checkNumbers(word)) {
                if (tempRes.containsKey(11)) {
                    ArrayList<Word> tempList = this.textProcessor.getSynonimsRows().contain(word, 10);
                    if (tempList.isEmpty()) {
                        model.addAttribute("word", word);
                        logger.info("Return: empty list");
//                        request.getRequestDispatcher("/WEB-INF/resultNull.jsp").forward(request, response);
                        return "resultNull";
                    } else {
                        model.addAttribute("word", word);
                        model.addAttribute("result", toString(tempList));
                        logger.info("Return: number list");
                        return "result";
//                        request.getRequestDispatcher("/WEB-INF/result.jsp").forward(request, response);
                    }
                } else {
                    model.addAttribute("word", word);
                    model.addAttribute("err", "number");
                    logger.info("Return: false number");
                    return "resultError";
//                    request.getRequestDispatcher("/WEB-INF/resultError.jsp").forward(request, response);
                }
            } else {
                for (int i = 0; i < this.textProcessor.getSynonimsRows().getIntegerMapToDictionaries().size(); i++) {
                    if (tempRes.containsKey(i)) {
                        tempRes.put(i, this.textProcessor.getSynonimsRows().contain(word, i));
                    }
                }
                String synResult = new String();
                for (int i = 0; i < this.textProcessor.getSynonimsRows().getIntegerMapToDictionaries().size(); ++i) {
                    if (tempRes.containsKey(i)) {
                        String nameSyn = textProcessor.getSynonimsRows().getIntegerMapToDictionaries().get(i);
                        nameSyn = nameSyn.replace("_", " ");
                        nameSyn = nameSyn.replace(".txt", "");
                        if (tempRes.get(i).size() != 0) {
                            synResult = synResult + "<br>" + nameSyn + toString(tempRes.get(i));
                        } else {
                            synResult = synResult + "<br>" + nameSyn + "<br>К сожалению, в этом словаре нет синонимов к слову " + word + "<br>";
                        }
                    }
                }
                model.addAttribute("word", word);
                model.addAttribute("result", synResult);
                logger.info("Return: list");
                return "result";
/*
                if (tempList.isEmpty()) {
                    model.addAttribute("word", word);
                    logger.info("Return: null");
                    return "resultNull";
                } else {*/

//                request.getRequestDispatcher("/WEB-INF/result.jsp").forward(request, response);
                //  }
            }
        }
        catch(Exception e) {
            model.addAttribute("result", "Что-то пошло не так...");
            return "resultError";
        }
    }

    private String toString(ArrayList<Word> list) {
        if (!list.isEmpty()) {
            String row = new String("<ul>");
            if (list.size() < 10) {
                for (Word word : list) {
                    row = row + "<li>" + word.getWord() + "</li>";
                }
            } else {
                int count = 0;
                while (count < 10) {
                    row = row + "<li>" + list.get(count).getWord() + "</li>";
                    count++;
                }
            }
            return row + "</ul>";
        } else {
            return null;
        }
    }
}
