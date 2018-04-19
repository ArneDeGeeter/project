package vibration.EJB;

import java.util.List;

import javax.ejb.Local;

import vibration.JPA.Locaty;

@Local
public interface LocationEJBLocal {
	
	public List<Locaty> findLocaties();

	void persistLocaty(Locaty l);
	
	
}
