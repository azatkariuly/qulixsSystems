<!DOCTYPE html>
<html>

<head>
	<title>Update Employee</title>
	
	<link type="text/css" rel="stylesheet" href="css/menu-navigation-bar.css">
	<link type="text/css" rel="stylesheet" href="css/style.css">
	
</head>
<body>

	<ul>
	  <li><a href="projectServlet">Projects</a></li>
	  <li><a href="problemServlet">Problems</a></li>
	  <li><a class="active" href="employeeServlet">Employees</a></li>
	</ul>
 
	<div style="padding:20px; margin-top:50px;height:600px;">
		<div id="container">
		<h3>Update Employee</h3>
		
		<form action="employeeServlet" method="get">
		
			<input type="hidden" name="command" value="UPDATE" />
			<input type="hidden" name="employeeId" value="${THE_ID}" />
			
			<table>
				<tbody>
				<tr>
					<td><label>ID:</label></td>
					<td><input style="background-color: #f6f6f6" type="text" value="${THE_ID}" name="" readonly></td>
				</tr>
				<tr>
					<td><label>Last Name:</label></td>
					<td><input type="text" name="lastName"
						value="${THE_LASTNAME}"></td>
				</tr>
				<tr>
					<td><label>First Name:</label></td>
					<td><input type="text" name="firstName"
						value="${THE_FIRSTNAME}"></td>
				</tr>
				<tr>
					<td><label>Patronymic:</label></td>
					<td><input type="text" name="patronymic"
						value="${THE_PATRONYMIC}"></td>
				</tr>
				<tr>
					<td><label>Position:</label></td>
					<td><input type="text" name="position"
						value="${THE_POSITION}"></td>
				</tr>
				
				</tbody>
			</table>
			
			<br/><br/>
			<input type="submit" value="Save" class="add-button"> <button onclick="location.href='employeeServlet'" type="button" class="add-button">Cancel</button>
		
		</form>
		
	</div>
	</div>

	
</body>
</html>