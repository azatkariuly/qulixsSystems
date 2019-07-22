<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>

<head>
	<title>Add Project</title>
	
	<link type="text/css" rel="stylesheet" href="css/menu-navigation-bar.css">
	<link type="text/css" rel="stylesheet" href="css/style.css">
	<link type="text/css" rel="stylesheet" href="css/temp.css">
	
</head>
<body>

	<ul>
	  <li><a class="active" href="projectControllerServlet">Projects</a></li>
	  <li><a href="problemControllerServlet">Problems</a></li>
	  <li><a href="employeeControllerServlet">Employees</a></li>
	</ul>

	<div style="padding:20px; margin-top:50px;height:600px;">
		<div id="container">
		<h3>Add Project</h3>
		
		<form action="projectControllerServlet" method="get">
		
			<input type="hidden" name="command" value="ADD" />
			
			<table>
				<tbody>
					<tr>
						<td><label>ID:</label></td>
						<td><input style="background-color: #f6f6f6" type="text" value="${MAX_ID}" name="maxID" readonly></td>
					</tr>
					<tr>
						<td><label>Title:</label></td>
						<td><input type="text" name="title"></td>
					</tr>
					<tr>
						<td><label>Abbreviation:</label></td>
						<td><input type="text" name="abbreviation"></td>
					</tr>
					<tr>
						<td><label>Description:</label></td>
						<td><input type="text" name="description"></td>
					</tr>
				</tbody>
			</table>
			
			<br/><br/>
			<input type="submit" value="Save" class="add-button"> <button onclick="location.href='projectControllerServlet'" type="button" class="add-button">Cancel</button>
			
		</form>
		
	</div>
	</div>

</body>
</html>