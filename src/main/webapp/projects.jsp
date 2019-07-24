<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>

<head>
	<link type="text/css" rel="stylesheet" href="css/menu-navigation-bar.css">
	<link type="text/css" rel="stylesheet" href="css/style.css">
</head>

<body>
	 <ul>
	  <li><a class="active" href="projectServlet">Projects</a></li>
	  <li><a href="problemServlet">Problems</a></li>
	  <li><a href="employeeServlet">Employees</a></li>
	</ul>

	<div style="padding:20px;margin-top:30px;;height:1500px;">
	
		<div>
			<div>
				<h2>Project List</h2>
			</div>
		</div>
		
		<div class="row">
			<div class="col-md-6">
				
				<form action="projectServlet" method="get">
					<input type="hidden" name="command" value="ADD-PROJECT-FORM" />
					<input type="submit" value="Add Project" class="add-button">
				</form>
				
				
				<table id="table" class="table table-striped">
					<tr>
						<th>ID</th>
						<th>Title</th>
						<th>Abbreviation</th>
						<th>Description</th>
						<th>Action</th>
					</tr>
					
					<c:forEach var="tempProject" items="${PROJECT_LIST}">
					
						<c:url var="editLink" value="projectServlet">
							<c:param name="command" value="LOAD"/>
							<c:param name="projectId" value="${tempProject.id}"/>
						</c:url>
						<c:url var="deleteLink" value="projectServlet">
							<c:param name="command" value="DELETE"/>
							<c:param name="projectId" value="${tempProject.id}"/>	
						</c:url>
						
						<tr>
							<td> ${tempProject.id}</td>
							<td> ${tempProject.title}</td>
							<td> ${tempProject.abbreviation} </td>
							<td> ${tempProject.description} </td>
							<td> 
								<a href="${editLink}">Edit</a>
								 | 
								<a href="${deleteLink}"
								onclick="if (!(confirm('Are you sure you want to delete this project?'))) return false">Delete</a>
							</td>
						</tr>
					</c:forEach>
					
		
				</table>
			
			
			
			</div>
		
			${CHECK_STRING}
		</div>
	</div>
	
	

</body>
</html>
