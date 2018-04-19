package vibration.JSF;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import vibration.EJB.UserManagementEJBLocal;
import vibration.JPA.Personen;
import vibration.JPA.Project;

@Named("searchController")
@ViewScoped
public class SearchController implements Serializable {

	private static final long serialVersionUID = 7810257527103870852L;

	@EJB
	private UserManagementEJBLocal userEJB;

	private String gebruikersnaam;
	private String gebruikerBio;
	private Personen gebruiker = new Personen();
	private String naam;
	private String voornaam;
	private String school;
	private String opleiding;
	private static final String NIETBESCHIKBAAR="Niet beschikbaar";
	
	// zoekfuncties
	private List<Project> projecten = new ArrayList<Project>();
	private List<Project> userProjects;
	private String projectZoekNaam;
	private String userZoekNaam;

	// admin functie
	public void findAllProjects() {
		setProjecten(userEJB.findProjects(null));
	}

	// publieke pagina's
	public void maakGebruiker() {
		gebruiker = userEJB.findPerson(gebruiker.getIdpersonen());
		if (gebruiker != null) {
			gebruikersnaam = gebruiker.getGebruikersnaam();
			setGebruikerBio(gebruiker.getBio());
			setNaam(gebruiker.getNaam());
			setVoornaam(gebruiker.getVoornaam());
			setSchool(gebruiker.getSchool());
			setOpleiding(gebruiker.getOpleiding());
		}
	}

	// zoekproject&users
	public void findUserPublicProjects() {

		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		int userId = -1;
		if (externalContext.getSessionMap().get("id") != null) {
			userId = (int) externalContext.getSessionMap().get("id");
		}

		if (gebruiker != null) {
			setUserProjects(userEJB.findUserPublicProjects(gebruiker.getIdpersonen(), userId));
		}
	}
	//Wordt deze ooit opgeroepen?
	public void zoekProjectenAjax(AjaxBehaviorEvent event) {
		setProjecten(userEJB.zoekProjecten(projectZoekNaam));
	}

	public void zoekProjecten() {
		setProjecten(userEJB.zoekProjecten(projectZoekNaam));
	}

	// getters en setters
	public List<Personen> zoekUsers() {
		return userEJB.zoekGebruikers(userZoekNaam);
	}

	public String getGebruikersnaam() {
		return gebruikersnaam;
	}

	public void setGebruikersnaam(String gebruikersnaam) {
		this.gebruikersnaam = gebruikersnaam;
	}

	public List<Project> getProjecten() {
		return projecten;
	}

	public void setProjecten(List<Project> projecten) {
		this.projecten = projecten;
	}

	public List<Project> getUserProjects() {
		return userProjects;
	}

	public void setUserProjects(List<Project> userProjects) {
		this.userProjects = userProjects;
	}

	public String getProjectZoekNaam() {
		return projectZoekNaam;
	}

	public void setProjectZoekNaam(String projectZoekNaam) {
		this.projectZoekNaam = projectZoekNaam;
	}

	public String getUserZoekNaam() {
		return userZoekNaam;
	}

	public void setUserZoekNaam(String userZoekNaam) {
		this.userZoekNaam = userZoekNaam;
	}

	public Personen getGebruiker() {
		return gebruiker;
	}

	public void setGebruiker(Personen gebruiker) {
		this.gebruiker = gebruiker;
	}

	public String getGebruikerBio() {
		return gebruikerBio;
	}

	public void setGebruikerBio(String gebruikerBio) {
		this.gebruikerBio = gebruikerBio;
	}

	public String getNaam() {
		return naam;
	}

	public void setNaam(String naam) {
		if(naam==null){this.naam=NIETBESCHIKBAAR;}
		else{this.naam = naam;}
	}

	public String getVoornaam() {
		return voornaam;
	}

	public void setVoornaam(String voornaam) {
		if(voornaam==null){this.voornaam=NIETBESCHIKBAAR;}
		else{this.voornaam = voornaam;}
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		if(school==null){this.school=NIETBESCHIKBAAR;}
		else{this.school = school;}
	}

	public String getOpleiding() {
		return opleiding;
	}

	public void setOpleiding(String opleiding) {
		if(opleiding==null){this.opleiding=NIETBESCHIKBAAR;}
		else{this.opleiding = opleiding;}
	}
}
