package ru.textanalysis.synonims.controller;

import ru.textanalysis.synonims.service.TextProcessor;
import ru.textanalysis.synonims.data.Word;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class DictionaryOfSynonimServlet extends HttpServlet {
    
    private TextProcessor textProcessor = null;
    private ServletConfig config;
    private final Logger logger = Logger.getLogger("system-log");
        
    @Override
    public void init(ServletConfig config) {
        System.out.println("1");
//        textProcessor.logger.info("Start init");

        this.config = config;
        ServletContext sc = config.getServletContext();  

//        textProcessor.logger.info((Supplier<String>) sc);
        textProcessor = TextProcessor.getInstance();            
        sc.log("Started OK!");
//        textProcessor.logger.info("Start OK!");
        System.out.println("1");
    }
    
    public DictionaryOfSynonimServlet() {
//	textProcessor = TextProcessor.getInstance();            
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            logger.info("Start servlet");
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            String word = (String) request.getParameter("word");
            word = textProcessor.getInitialForm(word);
            String idTemp = (String) request.getParameter("dictionary");
            int id = Integer.parseInt(idTemp);

            if (this.textProcessor.checkSimbol(word)) {
                request.setAttribute("word", word);
                request.setAttribute("err", "simbol");
                request.getRequestDispatcher("/WEB-INF/resultError.jsp").forward(request, response);
                logger.info("Return: false simbol");
            } else if (this.textProcessor.checkPronoun(word)) {
                request.setAttribute("word", word);
                request.setAttribute("err", "pronoun");
                request.getRequestDispatcher("/WEB-INF/resultError.jsp").forward(request, response);
                logger.info("Return: false pronoun");
            } else if (this.textProcessor.checkObscene(word)) {
                request.setAttribute("word", word);
                request.setAttribute("err", "obscene");
                request.getRequestDispatcher("/WEB-INF/resultError.jsp").forward(request, response);
                logger.info("Reture: false obscene");
            } else if (this.textProcessor.checkNumbers(word)) {
                if (id == 10) {
                    ArrayList<Word> tempList = this.textProcessor.getSynonimsRows().contain(word, 10);
                    if (tempList.isEmpty()) {
                        request.setAttribute("word", word);
                        request.getRequestDispatcher("/WEB-INF/resultNull.jsp").forward(request, response);
                        logger.info("Return: empty list");
                    } else {
                        request.setAttribute("word", word);
                        request.setAttribute("result", toString(tempList));
                        request.getRequestDispatcher("/WEB-INF/result.jsp").forward(request, response);
                        logger.info("Return: number list");
                    }
                } else {
                    request.setAttribute("word", word);
                    request.setAttribute("err", "number");
                    request.getRequestDispatcher("/WEB-INF/resultError.jsp").forward(request, response);
                    logger.info("Return: false number");
                }
            } else {
                ArrayList<Word> tempList = this.textProcessor.getSynonimsRows().contain(word, id);
                request.setAttribute("word", word);
                request.setAttribute("result", toString(tempList));
                request.getRequestDispatcher("/WEB-INF/result.jsp").forward(request, response);
                logger.info("Return: list");
            }

        } catch (ServletException ex) {
            logger.log(Level.SEVERE, "Error: DictionaryOfSynonimServlet, doGet", ex);
        } catch (UnsupportedEncodingException ex) {
            logger.log(Level.SEVERE, "Error: DictionaryOfSynonimServlet, doGet", ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error: DictionaryOfSynonimServlet, doGet", ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        doGet(request, response);

    }
    
    protected ArrayList <String> toString(ArrayList<Word> list) {
        if (!list.isEmpty()) {
            ArrayList<String> row = new ArrayList<String>();
            int count = 0;

            while (row.size() < 10) {
                row.add(list.get(count).toString());
                count++;
            }
            return row;
        } else {
            return null;
        }
    }
}
