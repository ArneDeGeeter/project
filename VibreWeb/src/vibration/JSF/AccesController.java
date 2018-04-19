package vibration.JSF;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

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
					System.out.println("acces maar bestaat niet");

				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/VibreWeb/error/onbestaandePaginaError.xhtml");

			}

			// omleiding naar het publieke experiment/project
			else if (id > 0) {
				System.out.println("id bestaat" + id);
				if (experimentId > 0 && experiment.getProject().getPublic_()) {
					System.out.println("naar public experiment: " + experiment.getProject().getPublic_());
					FacesContext.getCurrentInstance().getExternalContext()
							.redirect("/VibreWeb/publicExperiment.xhtml?experiment=" + experimentId);
				} else if (projectId > 0 && project.getPublic_()) {
					System.out.println("naar public project: " + project.getPublic_());
					FacesContext.getCurrentInstance().getExternalContext()
							.redirect("/VibreWeb/publicProject.xhtml?project=" + projectId);
				} else if (userEJB.geefPersoon(userId).isAdmin()) {
					System.out.println("Admin ziet alles, ook dit project : " + project.getPublic_());
					FacesContext.getCurrentInstance().getExternalContext()
							.redirect("/VibreWeb/publicProject.xhtml?project=" + projectId);
				} else {
					System.out.println(userEJB.geefPersoon(userId).getRol());
					System.out.println("niet public of geen van beide bestaat");
					FacesContext.getCurrentInstance().getExternalContext().redirect("/VibreWeb/index.xhtml");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				System.out.println("id bestaat" + id);
				if (experimentId > 0 && experiment.getProject().getPublic_()) {
					System.out.println("naar public experiment: " + experiment.getProject().getPublic_());
					return;
				} else if (projectId > 0 && project.getPublic_()) {
					System.out.println("naar public project: " + project.getPublic_());
					return;
				} else if (userEJB.geefPersoon(userId).isAdmin()) {
					System.out.println("naar public project: " + project.getPublic_());
					return;
				} else {
					System.out.println("niet public of geen van beide bestaat");
					FacesContext.getCurrentInstance().getExternalContext().redirect("/VibreWeb/index.xhtml");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void checkExist() throws IOException {

		// controle of experiment of project bestaat
		// experiment in url
		if (experimentId > 0 && projectId == -1) {
			experiment = userEJB.findExperiment(experimentId);

			if (experiment != null) {
				System.out.println("experiment datum" + experiment.getDate());
				id = experiment.getProject().getPersonen().getIdpersonen();
			} else {
				System.out.println("experiment = 0");
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/VibreWeb/error/onbestaandePaginaError.xhtml");
			}
		} // project in url
		else if (projectId > 0 && experimentId == -1) {
			project = userEJB.findProject(projectId);
			if (project != null) {
				System.out.println("project datum" + project.getAanmaakDatum());
				id = project.getPersonen().getIdpersonen();
			} else {
				System.out.println("project = null");
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/VibreWeb/error/onbestaandePaginaError.xhtml");
			}
		} // experiment en project of geen van beide in url
		else {
			System.out.println("geen van beide meegegeven");
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/VibreWeb/error/onbestaandePaginaError.xhtml");
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
