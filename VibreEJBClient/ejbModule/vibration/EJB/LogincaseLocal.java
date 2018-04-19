package vibration.EJB;

import javax.ejb.Local;

import vibration.JPA.Personen;

@Local
public interface LogincaseLocal {
	
	public Personen findUser(String login);
	
	public String loginUser(String email);


	String returnName(Personen persoon);

	void registerUser(String voornaam, String naam, String wachtwoord, String email, String school, String bio);


	void loginGlassfish(String email, String wachtwoord);

	boolean loginCheck();

	String createHash(String pass);

}
