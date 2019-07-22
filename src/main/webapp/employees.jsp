<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>

<html>

<head>
	<link type="text/css" rel="stylesheet" href="css/menu-navigation-bar.css">
	<link type="text/css" rel="stylesheet" href="css/style.css">
</head>

<body>

	<ul>
	  <li><a href="projectControllerServlet">Projects</a></li>
	  <li><a href="problemControllerServlet">Problems</a></li>
	  <li><a class="active" href="employeeControllerServlet">Employees</a></li>
	</ul>

	<div style="padding:20px;margin-top:30px;;height:1500px;">
  		<div>
			<div>
				<h2>Employee List</h2>
			</div>
		</div>
		
		<div class="row">
			<div class="col-md-6">
			
				<form action="employeeControllerServlet" method="get">
					<input type="hidden" name="command" value="ADD-EMPLOYEE-FORM" />
					<input type="submit" value="Add Employee" class="add-button">
				</form>
				
				<table class="table table-striped">
					<tr>
						<th>ID</th>
						<th>Last Name</th>
						<th>First Name</th>
						<th>Patronymic</th>
						<th>Position</th>
						<th>Action</th>
					</tr>
					
					<c:forEach var="tempEmployee" items="${requestScope.EMPLOYEE_LIST}">
					  
						<c:url var="editLink" value="employeeControllerServlet">
							<c:param name="command" value="LOAD"/>
							<c:param name="employeeId" value="${tempEmployee.id}"/>
						</c:url>
					
						<c:url var="deleteLink" value="employeeControllerServlet">
							<c:param name="command" value="DELETE"/>
							<c:param name="employeeId" value="${tempEmployee.id}"/>
							
						</c:url>
						
						<tr>
							<td> ${tempEmployee.id} </td>
							<td> ${tempEmployee.lastName} </td>
							<td> ${tempEmployee.firstName} </td>
							<td> ${tempEmployee.patronymic} </td>
							<td> ${tempEmployee.position} </td>
							<td> <a href="${editLink}">Edit</a>
							 |
							<a href="${deleteLink}"
							onclick="if (!(confirm('Are you sure you want to delete this employee?'))) return false">Delete</a>
							</td>
						</tr>
					</c:forEach>
					
		
				</table>
			
			
			</div>
		
		</div>
	</div>

</body>

</html>
