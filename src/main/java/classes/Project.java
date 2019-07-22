package classes;

public class Project {
	
	private int id;
	private String title;
	private String abbreviation;
	private String description;
	
	public Project(String title, String abbreviation, String description) {
		this.title = title;
		this.abbreviation = abbreviation;
		this.description = description;
	}
	
	public Project(int id, String title, String abbreviation, String description) {
		this.id = id;
		this.title = title;
		this.abbreviation = abbreviation;
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}	
	
}
