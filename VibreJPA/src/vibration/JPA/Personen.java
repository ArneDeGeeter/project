package vibration.JPA;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the personen database table.
 * 
 */
@Entity
@NamedQuery(name = "Personen.findAll", query = "SELECT p FROM Personen p")
public class Personen implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int idpersonen;

	private String bio;

	private String email;

	private String naam;

	private String rol;

	private String school;

	private String voornaam;

	private String wachtwoord;

	private String Opleiding;

	private String Gebruikersnaam;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	// bi-directional many-to-one association to Project
	@OneToMany(mappedBy = "personen")
	private List<Project> projects;

	public Personen() {
	}

	public Personen(String n, String w, String e, String s, String r) {
		naam = n;
		wachtwoord = w;
		email = e;
		school = s;
		rol = r;
	}

	public int getIdpersonen() {
		return this.idpersonen;
	}

	public void setIdpersonen(int idpersonen) {
		this.idpersonen = idpersonen;
	}

	public String getBio() {
		return this.bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNaam() {
		return this.naam;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}

	public String getRol() {
		return this.rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getSchool() {
		return this.school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getVoornaam() {
		return this.voornaam;
	}

	public void setVoornaam(String voornaam) {
		this.voornaam = voornaam;
	}

	public String getWachtwoord() {
		return this.wachtwoord;
	}

	public void setWachtwoord(String wachtwoord) {
		this.wachtwoord = wachtwoord;
	}

	public List<Project> getProjects() {
		return this.projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Project addProject(Project project) {
		getProjects().add(project);
		project.setPersonen(this);

		return project;
	}

	public Project removeProject(Project project) {
		getProjects().remove(project);
		project.setPersonen(null);

		return project;
	}

	public boolean isAdmin() {

		return rol.equals("Admin");
	}

	public String getOpleiding() {
		return Opleiding;
	}

	public void setOpleiding(String opleiding) {
		Opleiding = opleiding;
	}

	public String getGebruikersnaam() {
		return Gebruikersnaam;
	}

	public void setGebruikersnaam(String gebruikersnaam) {
		Gebruikersnaam = gebruikersnaam;
	}
}