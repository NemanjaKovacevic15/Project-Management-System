<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="half-width container project-detail-container" >
    <a href='<c:url value="/project" />' class="btn btn-warning back-btn" type="button">Back</a>
    <h1>${project.name}</h1>

    <hr/>
    <p>
        Project Code: <span class="badge">${project.projectCode}</span> 
    </p>
    <p>
        Project Manager: <span class="badge">${project.projectManager.username}</span>
    </p>
    <hr/>
    <c:set var="ctr" scope="page" value="0"/>
    <p>
        <c:forEach var="details" items="${project1}">
                <c:set var="ctr" scope="page" value="${ctr + 1}" />
            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                <div class="panel panel-default">
                    <div class="panel-heading" role="tab" id="headingOne">
                        <h4 class="panel-title">
                            <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse<c:out value = "${ctr}"/>" aria-expanded="true" aria-controls="collapseOne">
                                <p>Project Tasks: ${details.tasks.id}</p>
                            </a>
                        </h4>
                    </div>
                </div>
            </div>
        </div>
</c:forEach>
</p>

<div class="clearfix"></div>
</div>