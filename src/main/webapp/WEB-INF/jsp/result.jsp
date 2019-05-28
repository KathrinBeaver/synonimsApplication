<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Словарь синонимов</title>
        <style type="text/css">
            body {
                background: #FEDFC0;
                font-family: 'Times New Roman', Times, serif;
            }
            #header {
                height: 50px;
                background: #FEDFC0; border-bottom: 2px solid #7B5427;
                text-align:  center;
            }
            #description {
                background: transparent;
                text-align: center;
                padding: 1%;
            }
            #finding {
                width: 45%;
                background: transparent;
                position: absolute;
                left: 10%;
                padding: 1%
            }
            #result {
                float: right;
                position: relative;
                width: 24%;
                right: 20%;
                top: 1%;
            }
            ul {
                margin-left: 13px; /* Отступ слева в браузере IE и Opera */
                padding-left: 13px; /* Отступ слева в браузере Firefox, Safari, Chrome */
            }
        </style>
    </head>

    <body>
        <div id="header"><h1>Словарь синонимов с системой фильтрации по тематике</h1></div>
        <div id="description">
            <font size="4">
            Данный словарь предназначен для поиска русских синонимов по предметным областям. Ведите слово и выберете словари,
            по которым будет проводиться поиск.
            <br>Система не предназначена для обработки нецензурных выражений.
            </font>  
        </div>
        <div id="result">
            Синонимы к слову " <%  out.println(request.getAttribute("word")); %>"
                <%  out.println(request.getAttribute("result")); %>
        </div>
        <form action="getsynonims" method="GET">
            <div id = "finding">
                <font size="4">
                    Введите слово:
                </font>
                <input type="text" size="50" name="word">  <input type="submit" value="Найти" />
                <font size="4">
                    <br>
                    Выберите словари:
                </font>
                <br>
                <label><input type="checkbox" name="dictionary" value="0" checked/> Общий словарь синонимов</label>
                <br>
                <label><input type="checkbox" name="dictionary" value="1"/> География </label>
                <br>
                <label><input type="checkbox" name="dictionary" value="2"/> Гуманитарные науки </label>
                <br>
                <label><input type="checkbox" name="dictionary" value="3"/> Естественные науки </label>
                <br>
                <label><input type="checkbox" name="dictionary" value="4"/> Журналистика </label>
                <br>
                <label><input type="checkbox" name="dictionary" value="5"/> Закон </label>
                <br>
                <label><input type="checkbox" name="dictionary" value="6"/> История </label>
                <br>
                <label><input type="checkbox" name="dictionary" value="7"/> Кулинария </label>
                <br>
                <label><input type="checkbox" name="dictionary" value="8"/> Менеджмент </label>
                <br>
                <label><input type="checkbox" name="dictionary" value="9"/> Точные науки </label>
                <br>
                <label><input type="checkbox" name="dictionary" value="10"/> Финансы </label>
                <br>
                <label><input type="checkbox" name="dictionary" value="11"/> Числительные </label>
            </div>
        </form>
    </body>
</html>