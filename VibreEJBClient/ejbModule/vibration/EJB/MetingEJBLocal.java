package vibration.EJB;

import java.util.List;

import javax.ejb.Local;

import vibration.JPA.Experimenten;
import vibration.JPA.Meting;

@Local
public interface MetingEJBLocal {
	
	public List<Meting> getMetingen();

	void verwijderExperiment(int id);

	public String geefOpmerking(int id);

	Experimenten findExperiment(int id);

	void berekenGrafiek(Meting m, Experimenten e);

	boolean checkSTEM(Experimenten experiment);

}
