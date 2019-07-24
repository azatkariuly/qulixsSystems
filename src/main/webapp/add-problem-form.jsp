<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>

<head>
	<title>Add Problem</title>
	
	<link type="text/css" rel="stylesheet" href="css/menu-navigation-bar.css">
	<link type="text/css" rel="stylesheet" href="css/style.css">
	
</head>
<body>

	<ul>
	  <li><a href="projectServlet">Projects</a></li>
	  <li><a class="active" href="problemServlet">Problems</a></li>
	  <li><a href="employeeServlet">Employees</a></li>
	</ul>

	<div style="padding:20px; margin-top:50px;height:600px;">
		<div id="container">
		<h3>Add Problem</h3>
		
		<form action="problemServlet" method="get">
		
			<input type="hidden" name="command" value="ADD" />
			
			<table>
				<tbody>
				<tr>
					<td><label>ID:</label></td>
					<td><input style="background-color: #f6f6f6" type="text" value="${MAX_ID}" name="maxID" readonly></td>
				</tr>
				<tr>
					<td><label>Project:</label></td>
					<td>
						<select name="projectAbbreviation"> 
								<option></option>
							<c:forEach var="project" items="${PROJECT_LIST}">
							    <option>${project.abbreviation}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td><label>Title:</label></td>
					<td><input type="text" name="title"></td>
				</tr>
				<tr>
					<td><label>Work Hour:</label></td>
					<td><input type="number" name="workHour"></td>
				</tr>
				<tr>
					<td><label>Start Date:</label></td>
					<td><input type="date" name="startDate"></td>
				</tr>
				<tr>
					<td><label>End Date:</label></td>
					<td><input type="date" name="endDate"></td>
				</tr>
				<tr>
					<td><label>Status:</label></td>
					<!--  <td><input type="text" name="status"></td>-->
					<td>
						<select name="status">
							<option>not started</option>
							<option>in progress</option>
							<option>completed</option>
							<option>postponed</option>	
						</select>
					</td>
				</tr>				
				</tbody>
			</table>
			
			<br/><br/>
			<input type="submit" value="Save" class="add-button"> <button onclick="location.href='problemServlet'" type="button" class="add-button">Cancel</button>
			
		</form>
		
	</div>
	</div>
	
</body>
</html>