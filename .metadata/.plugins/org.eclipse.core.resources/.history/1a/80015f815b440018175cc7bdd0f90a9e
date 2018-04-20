package vibration.JSF;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import vibration.EJB.LogincaseLocal;
import vibration.EJB.UserManagementEJBLocal;
import vibration.JPA.Personen;

@Named("validatorBean")
@Stateless
public class ValidatorBean {
	
	@EJB
	private UserManagementEJBLocal userEJB;
	
	@EJB
	private LogincaseLocal loginEJB;
	
	private String name;
	private Personen p;

	public void validateEmail(FacesContext context, UIComponent comp, Object username){
		name= (String) username;
		p = userEJB.findPerson(name);
		if (p==null){
			((UIInput) comp).setValid(false);
			FacesMessage msg = 
					new FacesMessage("E-mail validation failed.", 
							"Invalid E-mail format.");
				context.addMessage(comp.getClientId(context),msg);
		}
	}
	
	public void validatePassword(FacesContext context, UIComponent comp, Object passwoord){
		if(p!=null){
			if(!(p.getWachtwoord().equals(loginEJB.createHash((String)passwoord)))){
				
				((UIInput) comp).setValid(false);
				FacesMessage msg = 
						new FacesMessage("Passwoord validation failed.", 
								"verkeerd wachtwoord");
					context.addMessage(comp.getClientId(context),msg);

			}
		}
	}
	
}
