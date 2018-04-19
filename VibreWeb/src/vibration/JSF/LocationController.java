package vibration.JSF;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import vibration.EJB.LocationEJBLocal;
import vibration.EJB.UserManagementEJBLocal;
import vibration.JPA.Locaty;

@ViewScoped
@Named("locationController")
public class LocationController implements Serializable {

	private static final long serialVersionUID = 5203702077089474640L;
	
	@EJB
	private LocationEJBLocal locationEJB;
	
	@EJB
	private UserManagementEJBLocal userEJB;
	
	private int fotoId=-1;
	private String locaties;
	private List<Locaty> locatie;
	private boolean render=false;
	private int refresh=0;

	
	
	@PostConstruct
	public void init(){
		getLocations();
	}

	
	public void getLocations(){
		setLocatie(locationEJB.findLocaties());
		Gson gson = new GsonBuilder().registerTypeAdapter(Locaty.class, new LocatyAdapter()).disableHtmlEscaping().create();
		locaties = gson.toJson(locationEJB.findLocaties()); 
	}

	public String getLocaties() {
		return locaties;
	}

	public void setLocaties(String locaties) {
		this.locaties = locaties;
	}

	public int getFotoId() {	
		return fotoId;
	}

	public void setFotoId(int fotoId) {
		this.fotoId = fotoId;
	}

	public void putFotoId(){
		FacesContext context = FacesContext.getCurrentInstance();
		
		fotoId =Integer.parseInt(context.getExternalContext().getRequestParameterMap().get("imageLoading:idPass"));
		setRender(true);
	}

	public List<Locaty> getLocatie() {
		return locatie;
	}

	public void setLocatie(List<Locaty> locatie) {
		this.locatie = locatie;
	}

	public boolean getRender() {
		return render;
	}

	public void setRender(boolean render) {
		this.render = render;
	}
	
	public void resetRender(){
		render=true;
	}
	
	public void dummy(){
		refresh++;
	}

	public int getRefresh() {
		return refresh;
	}

	public void setRefresh(int refresh) {
		this.refresh = refresh;
	}

}
