<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>

<html>

<head>
	<link type="text/css" rel="stylesheet" href="css/menu-navigation-bar.css">
	<link type="text/css" rel="stylesheet" href="css/style.css">
</head>
<body>
	<ul>
	  <li><a href="projectServlet">Projects</a></li>
	  <li><a class="active" href="problemServlet">Problems</a></li>
	  <li><a href="employeeServlet">Employees</a></li>
	</ul>

	<div style="padding:20px;margin-top:30px;;height:1500px;">
  		<div>
			<div>
				<h2>Problem List</h2>
			</div>
		</div>
		 
		<div class="row">
			<div class="col-md-6">
				<form action="problemServlet" method="get">
					<input type="hidden" name="command" value="ADD-PROBLEM-FORM" />
					<input type="submit" value="Add Problem" class="add-button">
				</form>
				
				<table class="table table-striped">
					<tr>
						<th>ID</th>
						<th>Project (Abbreviation)</th>
						<th>Title</th>
						<!-- <th>Work Hour</th> -->
						<th>Star Date</th>
						<th>End Date</th>
						<th>Executor</th>
						<th>Status</th>
						<th>Action</th>
					</tr>
					
					<c:forEach var="tempProblem" items="${requestScope.PROBLEM_LIST}">
					    
						<c:url var="editLink" value="problemServlet">
							<c:param name="command" value="LOAD"/>
							<c:param name="problemId" value="${tempProblem.id}"/>
						</c:url>
					
						<c:url var="deleteLink" value="problemServlet">
							<c:param name="command" value="DELETE"/>
							<c:param name="problemId" value="${tempProblem.id}"/>
							
						</c:url>
						
						<tr>
							<td> ${tempProblem.id} </td>
							<td> ${tempProblem.abbreviation} </td>
							<td> ${tempProblem.title} </td>
							<!-- <td> ${tempProblem.workHour} </td> -->
							<td> ${tempProblem.startDate} </td>
							<td> ${tempProblem.endDate} </td>
							
							<td> 
								<c:forEach var="tempEmployee" items="${tempProblem.employees}">
									${tempEmployee.lastName} ${tempEmployee.firstName} ${tempEmployee.patronymic} <br/>
								</c:forEach>
							</td>
							 
							<td> ${tempProblem.status} </td>
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
