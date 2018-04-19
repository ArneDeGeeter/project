package vibration.JSF;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import vibration.EJB.LocationEJBLocal;
import vibration.EJB.ProjectManagementEJBLocal;
import vibration.EJB.UserManagementEJBLocal;
import vibration.JPA.Experimenten;
import vibration.JPA.Personen;
import vibration.JPA.Project;

@Named("projectController")
@ViewScoped
public class ProjectController implements Serializable {

	private static final long serialVersionUID = 142635888265407617L;

	@EJB
	private ProjectManagementEJBLocal projectEJB;
	@EJB
	private LocationEJBLocal locationEJB;
	@EJB
	private UserManagementEJBLocal userEJB;

	// projecten tonen op profielpagina
	private Float selectLat = (float) 0;
	private Float selectLng = (float) 0;

	// project aanmaak
	private Project project = new Project();
	private String titel;
	private String teacher;
	private String publiek;
	private String beschrijving;
	private String foutmelding;
	
	// locatie aanmaken
	private int persoonId;
	private String naam;
	private String adres;
	private String latlng;
	private double lat;
	private double lng;
	
	

	// selecteren waarden uit tabellen
	private List<Project> projects = new ArrayList<Project>();
	private Project selectedProject;
	private List<Project> userProjects = new ArrayList<Project>();

	// weer te geven waarden
	public String toonBeschrijving() {
		project = projectEJB.vindProject(project.getId());
		if (project != null) {
			return project.getBeschrijving();
		} else
			return null;
	}
	
	
	public String checkProjectStatus(){

		boolean bool=userEJB.checkStatus(project.getId());
		
		if(bool){return "Zet project offline";}
		else{return "Zet project online";}
	}
	
	public void veranderStatus(){
		userEJB.veranderStatus(project);
	}

	public String geefToken(){
		return projectEJB.geefToken(project.getId());
	}

	public float getProjLat() {
		if (project != null) {
			return project.getLocaty().getLat();
		} else
			return 0;
	}

	public float getProjLng() {
		if (project != null) {
			return project.getLocaty().getLng();
		} else
			return 0;
	}

	public void selectProj(SelectEvent event) {
		project = (Project) event.getObject();
		setSelectLat(project.getLocaty().getLat());
		setSelectLng(project.getLocaty().getLng());
	}
	
	public void findUserProjects() {
		setUserProjects(userEJB.findUserProjects(null));
	}
	
	

	// creeren project
	public void setLocation() {
		FacesContext context = FacesContext.getCurrentInstance();

		Map<String, String> param = context.getExternalContext().getRequestParameterMap();

		StringBuilder sb = new StringBuilder();
		naam = param.get("locatie:naam");
		adres = param.get("locatie:adres");

		sb.append(param.get("locatie:latlng"));
		sb.deleteCharAt(0);

		String[] splitLatlng = sb.toString().split(",");

		sb.setLength(0);
		sb.append(splitLatlng[1]);
		sb.deleteCharAt(sb.length() - 1);

		setLat(Double.parseDouble(splitLatlng[0]));
		setLng(Double.parseDouble(sb.toString()));
	}
	
	public List<Project> findAllProjects(){
		return projectEJB.findAllProjects();
	}

	public String createProject() {
		
		System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");System.out.println("a");
		if(teacher==null||publiek==null||adres==null){
			
				foutmelding="Gelieve alle verplichte velden in te vullen";
				System.out.println(foutmelding);System.out.println(foutmelding);System.out.println(foutmelding);System.out.println(foutmelding);System.out.println(foutmelding);System.out.println(foutmelding);System.out.println(foutmelding);System.out.println(foutmelding);System.out.println(foutmelding);System.out.println(foutmelding);System.out.println(foutmelding);System.out.println(foutmelding);System.out.println(foutmelding);System.out.println(foutmelding);System.out.println(foutmelding);System.out.println(foutmelding);System.out.println(foutmelding);System.out.println(foutmelding);System.out.println(foutmelding);System.out.println(foutmelding);System.out.println(foutmelding);System.out.println(foutmelding);System.out.println(foutmelding);System.out.println(foutmelding);System.out.println(foutmelding);
				return "";
		}
		else{
			foutmelding=null;
		projectEJB.createProjectPlusLocation(titel, teacher, publiek, beschrijving, naam, adres, lat, lng);
		return "/spotter/profielpagina.faces?faces-redirect=true";
		}
	}

	// getters en setters
	public String getNaam() {
		return naam;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}

	public String getAdres() {
		return adres;
	}

	public void setAdres(String adres) {
		this.adres = adres;
	}

	public String getLatlng() {
		return latlng;
	}

	public void setLatlng(String latlng) {
		this.latlng = latlng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}
	
	public void setFoutmelding(String s){
		foutmelding=s;
	}

	public String getFoutmelding(){
		return foutmelding;
	}
	
	public void verwijderProject() {
		projectEJB.verwijderProject(project.getId());
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/VibreWeb/spotter/profielpagina.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void verwijderSelectedProject() {
		for (Project proj : projects) {
			projectEJB.verwijderProject(proj.getId());
		}
	}

	public int getPersoonId() {
		return persoonId;
	}

	public void setPersoonId(int persoonId) {
		this.persoonId = persoonId;
	}

	public List<Experimenten> findExperiments() {
		if (project != null) {
			return projectEJB.findExperiments(project.getId());
		} else
			return null;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public String getBeschrijving() {
		return beschrijving;
	}

	public void setBeschrijving(String beschrijving) {
		this.beschrijving = beschrijving;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getPubliek() {
		return publiek;
	}

	public void setPubliek(String publiek) {
		this.publiek = publiek;
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public Float getSelectLat() {
		return selectLat;
	}

	public void setSelectLat(Float selectLat) {
		this.selectLat = selectLat;
	}

	public Float getSelectLng() {
		return selectLng;
	}

	public void setSelectLng(Float selectlng) {
		this.selectLng = selectlng;
	}

	public Project getSelectedProject() {
		return selectedProject;
	}

	public void setSelectedProject(Project selectedProject) {
		this.selectedProject = selectedProject;
	}

	public List<Project> getUserProjects() {
		return userProjects;
	}

	public void setUserProjects(List<Project> userProjects) {
		this.userProjects = userProjects;
	}

}
