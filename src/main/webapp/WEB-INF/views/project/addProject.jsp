<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="half-width container">

    <div class="well lead">
        <c:choose>
            <c:when test="${action eq 'edit'}">
                Edit Project
            </c:when>
            <c:otherwise>
                Create Project
            </c:otherwise>
        </c:choose>
    </div>


    <form:form modelAttribute="newProject" cssClass="form-horizontal">
        <c:choose>
            <c:when test="${action eq 'edit'}">
                <form:hidden path="id" id="id"/>
            </c:when>
        </c:choose>
        <div class="form-group">
            <label class="control-label col-sm-3" for="name">Name:</label>
            <div class="col-sm-9">
                <form:input  path="name" class="form-control input-sm" id="name"  />
                <div class="has-error">
                    <form:errors path="name" />
                </div>
            </div>
        </div>

        <div class="form-group">
            <label class="control-label col-sm-3" for="projectCode">Project Code:</label>
            <div class="col-sm-9">
                <form:input type="text" path="projectCode" class="form-control input-sm" id="projectCode"  />
                <div class="has-error">
                    <form:errors path="projectCode" />
                </div>
            </div>
        </div>


        <div class="form-group">
            <label class="col-sm-3 control-label " for="projectManager">Project Manager:</label>
            <div class="col-sm-9">
                <form:select class="form-control input-sm" id = "projectManager" path="projectManager.id"  >
                    <form:option value="1" label="Select User" />
                    <form:options items="${projectManagerList}" itemValue="id" itemLabel="firstName" />
                </form:select>

                <div class="has-error">
                    <form:errors path="projectManager.id" />
                </div>
            </div>
        </div>

       
        <div class="form-actions floatRight">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${action eq 'edit'}">
                        <button type="submit" class="btn btn-primary btn-sm">Edit</button>
                    </c:when>
                    <c:otherwise>
                        <button type="submit" class="btn btn-primary btn-sm">Add</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</div>
