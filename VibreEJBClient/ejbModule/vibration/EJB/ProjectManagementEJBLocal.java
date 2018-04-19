package vibration.EJB;

import java.util.List;

import javax.ejb.Local;

import vibration.JPA.Experimenten;
import vibration.JPA.Project;

@Local
public interface ProjectManagementEJBLocal {

	public List<Experimenten> findExperiments(int id);

	public Experimenten findExperiment(int id);

	public String createProject(String titel, boolean teacher, int persoonId);

	public void verwijderProject(int id);

	String createProjectPlusLocation(String titel, String teacher, String publiek, String beschrijving, String naam,
			String adres, double lat, double lng);

	Project vindProject(int i);

	public List<Project> findAllProjects();

	Project vindProjectByToken(String token);

	String geefToken(int id);

	String createToken();
}
