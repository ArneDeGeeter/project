package vibration.JSF;

import java.io.IOException;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.sun.media.jfxmedia.logging.Logger;

import vibration.EJB.LogincaseLocal;
import vibration.EJB.UserManagementEJBLocal;
import vibration.JPA.Experimenten;
import vibration.JPA.Project;

@Named("accesController")
@ViewScoped
public class AccesController implements Serializable {

	private static final long serialVersionUID = -1519889915343128400L;

	@EJB
	private UserManagementEJBLocal userEJB;

	@EJB
	private LogincaseLocal loginEJB;

	private int experimentId = -1;
	private int projectId = -1;
	int id = -1;
	Experimenten experiment = new Experimenten();
	Project project = new Project();

	private String errorPagina="/VibreWeb/error/onbestaandePaginaError.xhtml";
	
	public void accesCheck() {
		try {

			checkExist();

			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();
			int userId = (int) externalContext.getSessionMap().get("id");

			// experiment of project is van user
			if (userId == id) {
				if (experimentId > 0) {
					return;
				} else if (projectId > 0) {
					return;
				} else

				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(errorPagina);

			}

			// omleiding naar het publieke experiment/project
			else if (id > 0) {
				if (experimentId > 0 && experiment.getProject().getPublic_()) {
					FacesContext.getCurrentInstance().getExternalContext()
							.redirect("/VibreWeb/publicExperiment.xhtml?experiment=" + experimentId);
				} else if (projectId > 0 && project.getPublic_()) {
					FacesContext.getCurrentInstance().getExternalContext()
							.redirect("/VibreWeb/publicProject.xhtml?project=" + projectId);
				} else if (userEJB.geefPersoon(userId).isAdmin()) {
					FacesContext.getCurrentInstance().getExternalContext()
							.redirect("/VibreWeb/publicProject.xhtml?project=" + projectId);
				} else {
					FacesContext.getCurrentInstance().getExternalContext().redirect("/VibreWeb/index.xhtml");
				}
			}
		} catch (IOException e) {
Logger.logMsg(1, e.toString());
		}
	}

	public void publicAccesCheck() {
		try {
			checkExist();
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();
			int userId = -1;
			if (externalContext.getSessionMap().get("id") != null) {
				userId = (int) externalContext.getSessionMap().get("id");
			}
			if (id > 0) {
				if (experimentId > 0 && experiment.getProject().getPublic_()) {
					return;
				} else if (projectId > 0 && project.getPublic_()) {
					return;
				} else if (userEJB.geefPersoon(userId).isAdmin()) {
					return;
				} else {
					FacesContext.getCurrentInstance().getExternalContext().redirect("/VibreWeb/index.xhtml");
				}
			}
		} catch (IOException e) {
			Logger.logMsg(1, e.toString());
		}
	}

	private void checkExist() throws IOException {

		// controle of experiment of project bestaat
		// experiment in url
		if (experimentId > 0 && projectId == -1) {
			experiment = userEJB.findExperiment(experimentId);

			if (experiment != null) {
				id = experiment.getProject().getPersonen().getIdpersonen();
			} else {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(errorPagina);
			}
		} // project in url
		else if (projectId > 0 && experimentId == -1) {
			project = userEJB.findProject(projectId);
			if (project != null) {
				id = project.getPersonen().getIdpersonen();
			} else {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(errorPagina);
			}
		} // experiment en project of geen van beide in url
		else {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(errorPagina);
		}
		return;
	}

	public int getExperimentId() {
		return experimentId;
	}

	public void setExperimentId(int experimentId) {
		this.experimentId = experimentId;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
}
