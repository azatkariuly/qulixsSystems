<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>

<head>
	<title>Update Project</title>
	
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
			<h3>Update Project</h3>
		
			<form action="projectControllerServlet" method="get">
			
				<input type="hidden" name="command" value="UPDATE" />
				<input type="hidden" name="projectId" value="${THE_ID}" />
				
				<table>
					<tbody>
					<tr>
						<td><label>ID:</label></td>
						<td><input style="background-color: #f6f6f6" type="text" value="${THE_ID}" name="" readonly></td>
					</tr>
					<tr>
						<td><label>Title:</label></td>
						<td><input type="text" name="title"
							value="${THE_TITLE}"></td>
					</tr>
					<tr>
						<td><label>Abbreviation:</label></td>
						<td><input type="text" name="abbreviation"
							value="${THE_ABBREVIATION}"></td>
					</tr>
					<tr>
						<td><label>Description:</label></td>
						<td><input type="text" name="description"
							value="${THE_DESCRIPTION}"></td>
					</tr>
					<tr>
						<td><label>Problems:</label></td>
						<td> <input id="myBtn" type="button" value="Add Problem"></td>
					</tr>
					<tr>
						<td></td>
						<td>
							<table class="table table-striped">
								<tr>
									<th>ID</th>
									<th>Title</th>
									<th>Start Date</th>
									<th>End Date</th>
									<th>Executor</th>
									<th>Status</th>
									<th>Action</th>
								</tr> 
								
								<c:forEach var="tempProblem" items="${PROJECT_PROBLEMS}">
							    
									<c:url var="removeLink" value="projectControllerServlet">
										<c:param name="command" value="REMOVEPROBLEM"/>
										<c:param name="problemId" value="${tempProblem.id}"/>
										<c:param name="projectId" value="${THE_ID}"/>
									</c:url>
									
									<tr>
										<td> ${tempProblem.id} </td>
										<td> ${tempProblem.title} </td>
										<td> ${tempProblem.startDate} </td>
										<td> ${tempProblem.endDate} </td>
										<td> 
											<c:forEach var="tempEmployee" items="${tempProblem.employees}">
												${tempEmployee.lastName} ${tempEmployee.firstName} ${tempEmployee.patronymic} <br/>
											</c:forEach>
										 </td>
										<td> ${tempProblem.status} </td>
										<td> <a href="${removeLink}">Remove</a>
										</td>
									</tr>
								</c:forEach>
								
				
							</table>
						</td>
					</tr>
					
					<tr>
						<td><label></label></td>
						<td></td>
					</tr>
					
					</tbody>
				</table>
				
				<br/><br/>
				<input type="submit" value="Save" class="add-button"> <button onclick="location.href='projectControllerServlet'" type="button" class="add-button">Cancel</button>
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
								<th>Title</th>
								<th>Star Date</th>
								<th>End Date</th>
								<th>Executor</th>
								<th>Status</th>
								<th>Action</th>
							</tr> 
							<c:forEach var="tempProblem" items="${FREE_PROBLEMS}">
							    
								<c:url var="addLink" value="projectControllerServlet">
									<c:param name="command" value="ADDPROBLEM"/>
									<c:param name="problemId" value="${tempProblem.id}"/>
									<c:param name="projectId" value="${THE_ID}"/>
								</c:url>
								
								<tr>
									<td> ${tempProblem.id} </td>
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
									<td> <a href="${addLink}">Add</a>
									</td>
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