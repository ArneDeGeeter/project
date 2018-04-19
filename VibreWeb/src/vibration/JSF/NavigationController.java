package vibration.JSF;

import java.io.IOException;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.sun.media.jfxmedia.logging.Logger;

import vibration.EJB.LogincaseLocal;

@Named("navigationController")
@ViewScoped
public class NavigationController implements Serializable {

	private static final long serialVersionUID = 5677946698282685059L;

	@EJB
	private LogincaseLocal loginEJB;
	private String home = "index.faces?faces-redirect=true";

	public String home() {
		return home;
	}

	public void homedirect() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/VibreWeb/index.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.logMsg(1, e.toString());
		}
	}

	public void veranderProfiel() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/VibreWeb/users/profielSettings.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.logMsg(1, e.toString());
		}

	}

	private String profiel = "/spotter/profielpagina.faces?faces-redirect=true";
	private String doorzoekenProjecten = "projectenZoekpagina.faces?faces-redirect=true";
	private String doorzoekenUsers = "userZoekPagina.faces?faces-redirect=true";
	private String register = "register.faces?faces-redirect=true";
	private String createProjectPage = "/users/createProject.faces?faces-redirect=true";

	public String profiel() {
		return profiel;
	}

	public String doorzoekProjecten() {
		return doorzoekenProjecten;
	}

	public String doorzoekUsers() {
		return doorzoekenUsers;
	}

	public String loginPage() {
		if (!loginEJB.loginCheck()) {
			return "re-login.faces?faces-redirect=true";
		} else {
			return "login.faces?faces-redirect=true";
		}
	}

	public String register() {
		return register;
	}

	public String createProjectPage() {
		return createProjectPage;
	}

}
