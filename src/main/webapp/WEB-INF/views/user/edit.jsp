<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="half-width container">
    <div class="well lead">User Edit Form</div>
    <c:url value="/edit-user-${username}" var="editUrl">
        <c:param name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </c:url>
    <form:form method="POST" modelAttribute="user"
               class="form-horizontal"
               action="${editUrl}"
               enctype="multipart/form-data">

        <form:input type="hidden" path="id" id="id"/>

        <div class="form-group">
            <label class="col-sm-3 control-label" for="firstName">First Name</label>
            <div class="col-sm-9">
                <form:input type="text" path="firstName" id="firstName" class="form-control input-sm"/>
                <div class="has-error">
                    <form:errors path="firstName" class="help-inline"/>
                </div>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label" for="lastName">Last Name</label>
            <div class="col-sm-9">
                <form:input type="text" path="lastName" id="lastName" class="form-control input-sm" />
                <div class="has-error">
                    <form:errors path="lastName" class="help-inline"/>
                </div>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label" for="username">Username</label>
            <div class="col-sm-9">
                <form:input type="username" path="username" id="username" class="form-control input-sm" />
                <div class="has-error">
                    <form:errors path="username" class="help-inline"/>
                </div>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label" for="password">Password</label>
            <div class="col-sm-7">
                <form:input type="password" path="password" id="password" class="form-control input-sm" />
                <div class="has-error">
                    <form:errors path="password" class="help-inline"/>
                </div>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label" for="email">Email</label>
            <div class="col-sm-9">
                <form:input type="email" path="email" id="email" class="form-control input-sm" />
                <div class="has-error">
                    <form:errors path="email" class="help-inline"/>
                </div>
            </div>
        </div>



        <div class="form-group">
            <label class="col-sm-3 control-label" for="userRoles">Roles</label>
            <div class="col-sm-9">
                <form:select path="userRoles" items="${roles}" multiple="true" itemValue="name" itemLabel="name" class="form-control input-sm" />
                <div class="has-error">
                    <form:errors path="userRoles" class="help-inline"/>
                </div>
            </div>
        </div>

        <div class="form-actions floatRight">
            <input type="submit" value="Update" class="btn btn-primary btn-sm"/> or <a href="<c:url value='/list' />">Cancel</a>
        </div>
    </form:form>
</div>
