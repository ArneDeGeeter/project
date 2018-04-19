package vibration.JSF;

import java.io.IOException;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import vibration.EJB.LogincaseLocal;
import vibration.EJB.UserManagementEJBLocal;
import vibration.JPA.Experimenten;
import vibration.JPA.Project;

@Named("navigationController")
@ViewScoped
public class NavigationController implements Serializable {

	private static final long serialVersionUID = 5677946698282685059L;

	@EJB
	private LogincaseLocal loginEJB;

	public String home() {
		return "index.faces?faces-redirect=true";
	}

	public void homedirect() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/VibreWeb/index.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void veranderProfiel() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/VibreWeb/users/profielSettings.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String profiel() {
		return "/spotter/profielpagina.faces?faces-redirect=true";
	}

	public String doorzoekProjecten() {
		return "projectenZoekpagina.faces?faces-redirect=true";
	}

	public String doorzoekUsers() {
		return "userZoekPagina.faces?faces-redirect=true";
	}

	public String loginPage() {
		if (!loginEJB.loginCheck()) {
			return "re-login.faces?faces-redirect=true";
		} else {
			return "login.faces?faces-redirect=true";
		}
	}

	public String register() {
		return "register.faces?faces-redirect=true";
	}

	public String createProjectPage() {
		return "/users/createProject.faces?faces-redirect=true";
	}

}
