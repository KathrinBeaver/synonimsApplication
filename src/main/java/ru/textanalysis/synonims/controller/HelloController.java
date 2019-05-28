package ru.textanalysis.synonims.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.textanalysis.synonims.service.TextProcessor;
import ru.textanalysis.synonims.data.Word;

import java.util.ArrayList;
import java.util.logging.Logger;

@Controller
public class HelloController {

    @Autowired
    private TextProcessor textProcessor = null;
    private final Logger logger = Logger.getLogger("system-log");

    @GetMapping("/hello")
    public String hello(Model model, @RequestParam(value="name", required=false, defaultValue="World") String name) {
        model.addAttribute("name", name);
        return "hello";
    }

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
            word = textProcessor.getInitialForm(word);
//            String idTemp = (String) request.getParameter("dictionary");
            int id = Integer.parseInt(dictionary);

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
                if (id == 10) {
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
                ArrayList<Word> tempList = this.textProcessor.getSynonimsRows().contain(word, id);
                model.addAttribute("word", word);
                model.addAttribute("result", toString(tempList));
                logger.info("Return: list");
                return "result";
//                request.getRequestDispatcher("/WEB-INF/result.jsp").forward(request, response);
            }
        }
        catch(Exception e) {
            model.addAttribute("result", "Что-то пошло не так...");
            return "resultError";
        }
    }

    protected ArrayList <String> toString(ArrayList<Word> list) {
        ArrayList <String> row = new ArrayList <String> ();
        int count = 0;

        while (row.size() < 10 && list.size() > 0) {
            row.add(list.get(count).toString());
            count++;
        }
        return row;
    }
}
