<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="half-width container">
    <div class="panel panel-default">
        <div class="panel-heading">
            <strong>${task.title}</strong>

            <span class="badge">${task.status}</span>
            <div class="btn-group pull-right">
                <button type="button"
                        class="btn btn-primary dropdown-toggle btn-xs"
                        data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    Action <span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                    <c:if test="${task.status == 'CREATED'}">
                        <c:url value="/dev/status" var="statusChangeUrl">
                            <c:param name="status" value="INPROGRESS"/>
                            <c:param name="id" value="${task.id}"/>
                        </c:url>
                        <li><a href="${statusChangeUrl}">Start</a></li>
                    </c:if>
                    <c:if test="${task.status == 'CREATED'|| task.status == 'INPROGRESS'}">
                        <c:url value="/dev/status" var="statusChangeUrl">
                            <c:param name="status" value="RESOLVED"/>
                            <c:param name="id" value="${task.id}"/>
                        </c:url>
                        <li><a href="${statusChangeUrl}">Resolve</a></li>
                    </c:if>
                    <c:if test="${task.status == 'RESOLVED'}">
                        <c:url value="/dev/status" var="statusChangeUrl">
                            <c:param name="status" value="CLOSED"/>
                            <c:param name="id" value="${task.id}"/>
                        </c:url>
                        <li><a href="${statusChangeUrl}">Close</a></li>
                    </c:if>

                    <li role="separator" class="divider"></li>
                    <c:url value="/dev/list" var="backToList"/>
                    <li><a href="${backToList}">Back</a></li>
                </ul>
            </div>

        </div>

        <ul class="list-group">
            <li class="list-group-item">
                Developer
                <span class="badge">${task.developer.firstName}</span>
            </li>
            <li class="list-group-item">
                Status
                <span class="badge">${task.status}</span>
            </li>
            <li class="list-group-item">
                Deadline
                <span class="badge">
                    <fmt:formatDate value="${task.deadLine}" pattern="MM-dd-yyyy"/>
                </span>
            </li>
            
            <li class="list-group-item">
                Total Progress
                <span class="badge">
                    <c:choose>
                        <c:when test="${task.progressPercentage == null}">
                            0%
                        </c:when>
                        <c:otherwise>
                            ${task.progressPercentage}%
                        </c:otherwise>
                    </c:choose>
                </span>
            </li>
           
        </ul>
   
    </div>

</div>

