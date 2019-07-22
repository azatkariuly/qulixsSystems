<!DOCTYPE html>

<html>

<head>
	<title>Add Employee</title>
	
	<link type="text/css" rel="stylesheet" href="css/menu-navigation-bar.css">
	<link type="text/css" rel="stylesheet" href="css/style.css">
	
</head>
<body>

	<ul>
	  <li><a href="projectControllerServlet">Projects</a></li>
	  <li><a href="problemControllerServlet">Problems</a></li>
	  <li><a class="active" href="employeeControllerServlet">Employees</a></li>
	</ul>

	<div style="padding:20px; margin-top:50px;height:600px;">
		<div id="container">
		<h3>Add Employee</h3>
		
		<form action="employeeControllerServlet" method="get">
		
			<input type="hidden" name="command" value="ADD" />
			
			<table>
				<tbody>
				<tr>
					<td><label>ID:</label></td>
					<td><input style="background-color: #f6f6f6" type="text" value="${MAX_ID}" name="maxID" readonly></td>
				</tr>
				<tr>
					<td><label>Last Name:</label></td>
					<td><input type="text" name="lastName"></td>
				</tr>
				<tr>
					<td><label>First Name:</label></td>
					<td><input type="text" name="firstName"></td>
				</tr>
				<tr>
					<td><label>Patronymic:</label></td>
					<td><input type="text" name="patronymic"></td>
				</tr>
				<tr>
					<td><label>Position:</label></td>
					<td><input type="text" name="position"></td>
				</tr>
				
				
				</tbody>
			</table>
			
			<br/><br/>
			<input type="submit" value="Save" class="add-button"> <button onclick="location.href='employeeControllerServlet'" type="button" class="add-button">Cancel</button>
		</form>
		
		
	</div>
	</div>

	
</body>
</html>