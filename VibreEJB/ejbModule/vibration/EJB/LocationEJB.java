package vibration.EJB;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import vibration.JPA.Locaty;

/**
 * Session Bean implementation class LocationEJB
 */
@Stateless
public class LocationEJB implements LocationEJBLocal {

	@PersistenceContext(unitName = "VibreJPA")
	private EntityManager em;
	
    public LocationEJB() {
        // TODO Auto-generated constructor stub
    }
    
    public List<Locaty> findLocaties(){
    	Query q = em.createQuery("SELECT l FROM Locaty l WHERE l.project.public_ = TRUE");
    	return q.getResultList();
    }
    
    @Override
	public void persistLocaty(Locaty l) {
		em.joinTransaction();
		if (em.isJoinedToTransaction())
			em.persist(l);
	}

    
    

}
