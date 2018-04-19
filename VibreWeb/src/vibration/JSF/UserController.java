package vibration.JSF;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.PhaseId;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import vibration.EJB.LogincaseLocal;
import vibration.EJB.UserManagementEJBLocal;
import vibration.JPA.Foto;
import vibration.JPA.Personen;

@Named("userController")
@Stateless
public class UserController implements Serializable {

	private static final long serialVersionUID = 2097139407940670401L;

	@EJB
	private UserManagementEJBLocal userEJB;

	@EJB
	private LogincaseLocal loginEJB;

	// registreren
	private String voornaam;
	private String login;
	private String wachtwoord;
	private String email;
	private String school;
	private String rol;
	private String bio;
	private String gebruikersnaam;
	private String opleiding;
	private Personen person;
	private static final String NIETBESCHIKBAAR="Niet beschikbaar";

	// inloggen
	private String username;
	private String password;

	// admin selectie
	private List<Personen> users;

	public String currentUser() {
		return userEJB.currentUser();
	}

	public String userPage() {
		return userEJB.userPage();
	}

	// foto ophalen
	public StreamedContent getFoto() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			return new DefaultStreamedContent();
		} else {

			String id = context.getExternalContext().getRequestParameterMap().get("id");
			if (id.equals("0") || id.equals("-1")) {
				InputStream stream1 = this.getClass().getResourceAsStream("VibreWeb/resources/images/noImage.png");
				return new DefaultStreamedContent(stream1, "image/png");

			} else {
				Foto picture = userEJB.vindFoto(Integer.valueOf(id));
				byte[] image = picture.getFoto();
				return new DefaultStreamedContent(new ByteArrayInputStream(image));
			}

		}
	}

	// profielSettings
	public void geefPersoon() {
		int id = userEJB.currentUserId();
		person = userEJB.geefPersoon(id);
		login = person.getNaam();
		email = person.getEmail();
		school = person.getSchool();
		bio = person.getBio();
		voornaam = person.getVoornaam();
		rol = person.getRol();
	}

	public void wijzigUser() {
		userEJB.updatePersoon(voornaam, gebruikersnaam, login, school, person, opleiding, bio );
	}
	
	public String toonBio() {
		int id = userEJB.currentUserId();
		person = userEJB.geefPersoon(id);
		return person.getBio();
	}

	
	
	public String toonVoornaam() {
		int id = userEJB.currentUserId();
		person = userEJB.geefPersoon(id);
		if(person.getVoornaam()==null){return NIETBESCHIKBAAR;}
		else{return person.getVoornaam();}
	}
	
	public String toonNaam() {
		int id = userEJB.currentUserId();
		person = userEJB.geefPersoon(id);
		if(person.getNaam()==null){return NIETBESCHIKBAAR;}
		else{return person.getNaam();}
	}
	
	public String toonSchool() {
		int id = userEJB.currentUserId();
		person = userEJB.geefPersoon(id);
		if(person.getSchool()==null){return NIETBESCHIKBAAR;}
		else{return person.getSchool();}
	}
	
	public String toonOpleiding() {
		int id = userEJB.currentUserId();
		person = userEJB.geefPersoon(id);
		if(person.getOpleiding()==null){return NIETBESCHIKBAAR;}
		else{return person.getOpleiding();}
	}

	// registreren en login/logout
	public void registerUser() {
		loginEJB.registerUser(voornaam, login, wachtwoord, email, school, bio);
	}

	public void login() throws IOException {
		loginEJB.loginGlassfish(username, password);
	}

	public void logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		try {
			request.logout();
			externalContext.redirect("/VibreWeb/index.xhtml");
		} catch (ServletException | IOException e) {
			context.addMessage(null, new FacesMessage("Logout failed."));
		}
	}

	public void checkEmail(AjaxBehaviorEvent event) {
		userEJB.checkEmail(username);
	}

	public List<Personen> findAllUsers() {
		return userEJB.findAllUsers();
	}
	
	public void verwijderSelectedUsers() {
		for (Personen pers : users) {
			userEJB.verwijderUser(pers.getIdpersonen());
		}
	}

	// getters en setters

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setPersoon(Personen p) {
		person = p;
	}

	public Personen getPersoon() {
		return person;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getWachtwoord() {
		return wachtwoord;
	}

	public void setWachtwoord(String wachtwoord) {
		this.wachtwoord = wachtwoord;
	}

	public String getVoornaam() {
		return voornaam;
	}

	public void setVoornaam(String voornaam) {
		this.voornaam = voornaam;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public List<Personen> getUsers() {
		return users;
	}

	public void setUsers(List<Personen> users) {
		this.users = users;
	}

	public String getGebruikersnaam() {
		return gebruikersnaam;
	}

	public void setGebruikersnaam(String gebruikersnaam) {
		this.gebruikersnaam = gebruikersnaam;
	}

	public String getOpleiding() {
		return opleiding;
	}

	public void setOpleiding(String opleiding) {
		this.opleiding = opleiding;
	}

}
