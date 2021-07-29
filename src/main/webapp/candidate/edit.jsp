<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="ru.job4j.dream.model.Candidate" %>
<%@ page import="ru.job4j.dream.store.PsqlStore" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>

    <title>Работа мечты</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script>
        function submitData() {
            if ($('#name').val() === "") {
                alert($('#name').attr('title'));
                return false;
            }
            if ($('#select').val() === "0") {
                alert($('#select').attr('title'));
                return false;
            }
            return true;
        }
        $(document).ready(function () {
            $.ajax({
                type: 'GET',
                url: 'http://localhost:8080/job4j_dreamjob/city',
                dataType: 'json'
            }).done(function (data) {
                for(let i = 0; i < data.length; i++) {
                    $('#select option:last').after('<option value=' + (i+1) +'>' + data[i] + '</option>');
                }
            })
        })
    </script>
</head>
<body>
<div class="container pt-3">
    <div class="row">
        <ul class="nav">
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/posts.do">Вакансии</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/candidates.do">Кандидаты</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/post/edit.jsp">Добавить вакансию</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/candidate/edit.jsp">Добавить кандидата</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/login.jsp">
                    <c:if test="${user == null}">
                        <c:out value="Войти"/>
                    </c:if>
                    <c:if test="${user != null}">
                        <c:out value="${user.name}"/> | Выйти
                    </c:if>
                </a>
            </li>
        </ul>
    </div>
</div>
<div class="container pt-3">
    <div class="row">
        <div class="card" style="width: 100%">
            <div class="card-header">
                Новый кандидат.
            </div>
            <div class="card-body">
                <form <% if (request.getParameter("id") == null) { %>
                        action="<%=request.getContextPath()%>/candidates.do?id=0"
                        <%} else { %>
                        action="<%=request.getContextPath()%>/candidates.do?id=<%=request.getParameter("id")%>"
                        <% }%>
                        method="post">
                    <div class="form-group">
                        <label>Имя кандидата</label>
                        <input type="text" class="form-control" id="name" name="name" title="Введите имя.">
                    </div>
                    <div class="form-group">
                        <label>город</label>
                        <select class="form-control" id="select" name="city" title="Выбирите город.">
                            <option value="0">Выберите город</option>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary" onclick="return submitData()">Сохранить</button>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>