package vibration.EJB;

import java.util.List;

import javax.ejb.Local;
import javax.persistence.Query;

import vibration.JPA.Experimenten;
import vibration.JPA.Foto;
import vibration.JPA.Meting;
import vibration.JPA.Personen;
import vibration.JPA.Project;

@Local
public interface UserManagementEJBLocal {

	public void uploadenFoto(byte[] fotos,String naam);

	public Foto vindFoto(int id);

	public Project findProject(int i);
	
	public Experimenten findExperiment(int id);

	public List<Experimenten> findExperimenten(Project p);
	
	public String getLocation();

	public List<Foto> fotoOphalen(int id);
	
	public boolean isExperiment();
	
	public void setExperiment(boolean experiment);
	
	public boolean isProject();
	
	public void setProject(boolean project);
	
	public Personen geefPersoon();

	public List<Project> zoekProjecten(String projectZoekNaam);

	public List<Personen> zoekGebruikers(String userZoekNaam);

	public List<Project> findUserProjects(Personen p);

	public String getGebruikersnaam(int idpersonen);

	public Personen findPerson(int id);
	
	public void addMeting(Meting m);
	
	public String currentUser();
	
	public String userPage();

	public Personen findPerson(String login);

	void addProject(Project p);

	void addMetingExperiment(Meting meting, Experimenten e);

	void addExperiment(Experimenten e);
	
	public void uploadenFoto(byte[] fotos,String naam,int id,int id2);

	List<Project> findProjects(Personen persoon);

	Personen geefPersoon(int id);

	List<Experimenten> findExperiment(Project p);

	int currentUserId();

	Meting findMeting(int i);

	void checkEmail(String username);

	List<Project> findUserPublicProjects(int idpersonen, int userId);

	List<Personen> findAllUsers();

	void verwijderUser(int id);

	Experimenten findExperimentByID(int id);

	Meting findMeting(int i, int id);

	void veranderStatus(Project project);

	boolean checkStatus(int id);

	void updatePersoon(String voornaam, String gebruikersnaam, String login, String school, Personen person,
			String opleiding, String bio);

	
}
