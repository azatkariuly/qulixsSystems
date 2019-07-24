<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>

<head>
	<title>Update Problem</title>
	
	<link type="text/css" rel="stylesheet" href="css/menu-navigation-bar.css">
	<link type="text/css" rel="stylesheet" href="css/style.css">
	<link type="text/css" rel="stylesheet" href="css/temp.css">
	
</head>
<body>

	<ul>
	  <li><a href="projectServlet">Projects</a></li>
	  <li><a class="active" href="problemServlet">Problems</a></li>
	  <li><a href="employeeServlet">Employees</a></li>
	</ul>
 
	<div style="padding:20px; margin-top:50px;height:600px;">
		<div id="container">
		<h3>Update Problem</h3>
		
		<form action="problemServlet" method="get">
		
			<input type="hidden" name="command" value="UPDATE" />
			<input type="hidden" name="problemId" value="${THE_ID}" />
			
			<table>
				<tbody>
				<tr>
					<td><label>ID:</label></td>
					<td><input style="background-color: #f6f6f6" type="text" value="${THE_ID}" name="" readonly></td>
				</tr>
				<tr>
					<td><label>Project:</label></td>
					<td><input style="background-color: #f6f6f6" type="text" name="" value="${THE_PROJECT_ID}" readonly></td>
				</tr>
				<tr>
					<td><label>Title:</label></td>
					<td><input type="text" name="title"
						value="${THE_TITLE}"></td>
				</tr>
				<tr>
					<td><label>Work Hour:</label></td>
					<td><input type="number" name="workHour"
						value="${THE_WORKHOUR}"></td>
				</tr>
				<tr>
					<td><label>Start Date:</label></td>
					<td><input type="date" name="startDate"
						value="${THE_STARTDATE}"></td>
				</tr>
				<tr>
					<td><label>End Date:</label></td>
					<td><input type="date" name="endDate"
						value="${THE_ENDDATE}"></td>
				</tr>
				<tr>
					<td><label>Status:</label></td>
					<td>
						<select name="status" data-selected="${THE_STATUS}">
							<option>not started</option>
							<option>in progress</option>
							<option>completed</option>
							<option>postponed</option>	
						</select>	
					</td>
				</tr>
				<tr>
					<td><label>Executors:</label></td>
					<td> <input id="myBtn" type="button" value="Add Executor"></td>
				</tr>
				
				<tr>
					<td></td>
					<td>
						<table class="table table-striped">
							<tr>
								<th>ID</th>
								<th>Last Name</th>
								<th>First Name</th>
								<th>Patronymic</th>
								<th>Position</th>
								<th>Action</th>
							</tr> 
							
							<c:forEach var="tempEmployee" items="${THE_PROBLEM_EMPLOYEES}">
							    
							    <c:url var="removeLink" value="problemServlet">
									<c:param name="command" value="REMOVEEMPLOYEE"/>
									<c:param name="employeeId" value="${tempEmployee.id}"/>
									<c:param name="problemId" value="${THE_ID}"/>
								</c:url>
							    
								<tr>
									<td> ${tempEmployee.id} </td>
									<td> ${tempEmployee.lastName} </td>
									<td> ${tempEmployee.firstName} </td>
									<td> ${tempEmployee.patronymic} </td>
									<td> ${tempEmployee.position} </td>
									
									<td> <a href="${removeLink}">Remove</a>
									</td>
									
								</tr>
						
							</c:forEach>
							
						</table>
					</td>
				</tr>				
				</tbody>
			</table>
			
			<br/><br/>
			<input type="submit" value="Save" class="add-button"> <button onclick="location.href='problemServlet'" type="button" class="add-button">Cancel</button>
		</form>
		
	</div>
	</div>

	<!-- The Modal -->
		<div id="myModal" class="modal">
		
		  <!-- Modal content -->
		  <div class="modal-content">
		    <span class="close">&times;</span>
		    
						<table class="table table-striped">
							<tr>
								<th>ID</th>
								<th>Last Name</th>
								<th>First Name</th>
								<th>Patronymic</th>
								<th>Position</th>
								<th>Action</th>
							</tr> 
							
							<c:forEach var="tempFreeEmployee" items="${THE_FREE_PROBLEM_EMPLOYEES}">
							    
							    <c:url var="addLink" value="problemServlet">
									<c:param name="command" value="ADDEMPLOYEE"/>
									<c:param name="employeeId" value="${tempFreeEmployee.id}"/>
									<c:param name="problemId" value="${THE_ID}"/>
								</c:url>
							    
								<tr>
									<td> ${tempFreeEmployee.id} </td>
									<td> ${tempFreeEmployee.lastName} </td>
									<td> ${tempFreeEmployee.firstName} </td>
									<td> ${tempFreeEmployee.patronymic} </td>
									<td> ${tempFreeEmployee.position} </td>
									
									<td> <a href="${addLink}">Add</a>
									
								</tr>
						
							</c:forEach>
				
						</table>

		  </div>
		
		</div>
	
	<script>
		// Get the modal
		var modal = document.getElementById("myModal");
		
		// Get the button that opens the modal
		var btn = document.getElementById("myBtn");
		
		// Get the <span> element that closes the modal
		var span = document.getElementsByClassName("close")[0];
		
		// When the user clicks the button, open the modal 
		btn.onclick = function() {
		  modal.style.display = "block";
		}
		
		// When the user clicks on <span> (x), close the modal
		span.onclick = function() {
		  modal.style.display = "none";
		}
		
		// When the user clicks anywhere outside of the modal, close it
		window.onclick = function(event) {
		  if (event.target == modal) {
		    modal.style.display = "none";
		  }
		}
	</script>
	
</body>
</html>