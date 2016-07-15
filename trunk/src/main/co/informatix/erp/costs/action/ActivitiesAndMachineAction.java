package co.informatix.erp.costs.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import co.informatix.erp.costs.dao.ActivitiesAndMachineDao;
import co.informatix.erp.costs.dao.ActivitiesDao;
import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.costs.entities.ActivityMachine;
import co.informatix.erp.costs.entities.ActivityMachinePK;
import co.informatix.erp.lifeCycle.action.RecordActivitiesActualsAction;
import co.informatix.erp.machines.dao.MachineTypesDao;
import co.informatix.erp.machines.entities.MachineTypes;
import co.informatix.erp.machines.entities.Machines;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControladorFechas;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class allows the logic of the relationship between the activities and
 * machines in the database. The logic is to record the relationship and
 * machines activities.
 * 
 * @author Gerardo.Herrera
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ActivitiesAndMachineAction implements Serializable {

	@EJB
	private MachineTypesDao machineTypesDao;
	@EJB
	private ActivitiesAndMachineDao activitiesAndMachineDao;
	@EJB
	private ActivitiesDao activitiesDao;

	private boolean fromModal = false;
	private boolean stateAddMachine = false;

	private Paginador pagination = new Paginador();
	private Activities selectedActivity;
	private Machines machine;
	private ActivityMachine activityMachine;

	private List<ActivityMachine> listActivityMachineTemp;
	private List<ActivityMachine> listActivityMachine;
	private List<SelectItem> itemsMachineTypes;

	/**
	 * @return fromModal: Flag from detected if this class is called from
	 *         differents views
	 */
	public boolean isFromModal() {
		return fromModal;
	}

	/**
	 * @param fromModal
	 *            :Flag from detected if this class is called from differents
	 *            views
	 */
	public void setFromModal(boolean fromModal) {
		this.fromModal = fromModal;
	}

	/**
	 * @return stateAddMachine: This state detected if the machine is going to
	 *         add or remove
	 */
	public boolean isStateAddMachine() {
		return stateAddMachine;
	}

	/**
	 * @param stateAddMachine
	 *            : This state detected if the machine is going to add or remove
	 */
	public void setStateAddMachine(boolean stateAddMachine) {
		this.stateAddMachine = stateAddMachine;
	}

	/**
	 * @return pagination: Management paged list of activities.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paged list of activities.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return selectedActivity: Activity selected for operations
	 */
	public Activities getSelectedActivity() {
		return selectedActivity;
	}

	/**
	 * @param selectedActivity
	 *            : Activity selected for operations
	 */
	public void setSelectedActivity(Activities selectedActivity) {
		this.selectedActivity = selectedActivity;
	}

	/**
	 * @return machine: Machine object
	 */
	public Machines getMachine() {
		return machine;
	}

	/**
	 * @param machine
	 *            : Machine object
	 */
	public void setMachine(Machines machine) {
		this.machine = machine;
	}

	/**
	 * @return activityMachine: Its a relation between activities and machines
	 */
	public ActivityMachine getActivityMachine() {
		return activityMachine;
	}

	/**
	 * @param activityMachine
	 *            : Its a relation between activities and machines
	 */
	public void setActivityMachine(ActivityMachine activityMachine) {
		this.activityMachine = activityMachine;
	}

	/**
	 * @return listActivityMachineTemp: Relation list between activities and
	 *         machines temporal
	 */
	public List<ActivityMachine> getListActivityMachineTemp() {
		return listActivityMachineTemp;
	}

	/**
	 * @param listActivityMachineTemp
	 *            : Relation list between activities and machines temporal
	 */
	public void setListActivityMachineTemp(
			List<ActivityMachine> listActivityMachineTemp) {
		this.listActivityMachineTemp = listActivityMachineTemp;
	}

	/**
	 * @return listActivityMachine: Relation list between activities and
	 *         machines
	 */
	public List<ActivityMachine> getListActivityMachine() {
		return listActivityMachine;
	}

	/**
	 * @param listActivityMachine
	 *            : Relation list between activities and machines
	 */
	public void setListActivityMachine(List<ActivityMachine> listActivityMachine) {
		this.listActivityMachine = listActivityMachine;
	}

	/**
	 * @return itemsMachineTypes: Diferents machine types
	 */
	public List<SelectItem> getItemsMachineTypes() {
		return itemsMachineTypes;
	}

	/**
	 * @param itemsMachineTypes
	 *            : Diferents machine types
	 */
	public void setItemsMachineTypes(List<SelectItem> itemsMachineTypes) {
		this.itemsMachineTypes = itemsMachineTypes;
	}

	/**
	 * Method that loads a machinesType list.
	 */
	public void listMachineTypes() {
		try {
			this.listActivityMachineTemp = new ArrayList<ActivityMachine>();
			this.itemsMachineTypes = new ArrayList<SelectItem>();
			List<MachineTypes> listMachineType = machineTypesDao
					.listMachineType();
			if (listMachineType != null) {
				for (MachineTypes machineType : listMachineType) {
					itemsMachineTypes.add(new SelectItem(machineType
							.getIdMachineType(), machineType.getName()));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Check the relations between activities and human resources.
	 * 
	 */
	public void showActivitiesAndMachineForActivity() {
		ValidacionesAction validation = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		this.listActivityMachine = new ArrayList<ActivityMachine>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder queryBuilder = new StringBuilder();
		String SearchMessage = "";
		try {
			if (fromModal) {
				RecordActivitiesActualsAction recordActivitiesActualsAction = ControladorContexto
						.getContextBean(RecordActivitiesActualsAction.class);
				this.selectedActivity = recordActivitiesActualsAction
						.getSelectedActivity();
			}
			advancedSearchActivitiesAndMachine(queryBuilder, parameters);
			Long amount = activitiesAndMachineDao.quantityActivitiesAndMachine(
					queryBuilder, parameters);
			if (amount != null) {
				if (amount > 5) {
					pagination.paginarRangoDefinido(amount, 5);
				} else {
					pagination.paginar(amount);
				}
				this.listActivityMachine = activitiesAndMachineDao
						.consultingActivitiesAndMachine(pagination.getInicio(),
								pagination.getRango(), queryBuilder, parameters);
			}
			validation.setMensajeBusqueda(SearchMessage);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method builds the query for an advanced search for relations between
	 * activities and machines, it also builds display messages depending on the
	 * search criteria selected by the user.
	 * 
	 * @param query
	 *            : query to concatenate.
	 * @param parameters
	 *            : list of the search parameters.
	 */
	private void advancedSearchActivitiesAndMachine(StringBuilder query,
			List<SelectItem> parameters) {
		query.append("WHERE ac.idActivity = :id ");
		SelectItem item = new SelectItem(this.selectedActivity.getIdActivity(),
				"id");
		parameters.add(item);
	}

	/**
	 * Select and deselect the machines in a list.
	 * 
	 * @param machine
	 *            : Machine object.
	 */
	public void machineSelection(Machines machine) {
		try {
			this.stateAddMachine = false;
			this.machine = machine;
			this.activityMachine = new ActivityMachine();
			if (!machine.isSelection()) {
				if (this.selectedActivity.getDurationBudget() != null) {
					this.activityMachine
							.setDurationBudget(this.selectedActivity
									.getDurationBudget());
				} else {
					Date initialDate = this.selectedActivity
							.getInitialDtBudget();
					Date finalDate = this.selectedActivity.getFinalDtBudget();
					Double duration = ControladorFechas.restarFechas(
							initialDate, finalDate);
					this.activityMachine.setDurationBudget(duration);
				}
			} else {
				ActivityMachine activityMachine = new ActivityMachine();
				ActivityMachinePK activitiesMachinesPK = new ActivityMachinePK();
				activitiesMachinesPK.setMachines(machine);
				activitiesMachinesPK.setActivities(this.selectedActivity);
				for (ActivityMachine activityMachines : listActivityMachineTemp) {
					if (activityMachines.getActivityMachinePK().equals(
							activitiesMachinesPK)) {
						activityMachine = activityMachines;
					}
				}
				this.listActivityMachineTemp.remove(activityMachine);
				this.stateAddMachine = true;
				this.machine.setSelection(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Add machines and create activityMachine List.
	 */
	public void addMachines() {
		ActivityMachinePK activityMachinePK = new ActivityMachinePK();
		this.machine.setSelection(true);
		this.activityMachine.setInitialDateTime(selectedActivity
				.getInitialDtBudget());
		this.activityMachine.setFinalDateTime(selectedActivity
				.getFinalDtBudget());
		activityMachinePK.setActivities(selectedActivity);
		activityMachinePK.setMachines(this.machine);
		this.activityMachine.setActivityMachinePK(activityMachinePK);
		calculateConsumableCost(this.activityMachine);
		this.listActivityMachineTemp.add(this.activityMachine);
	}

	/**
	 * Calculate the cost of consumption of the machine.
	 * 
	 * @param activityMachine
	 *            : ActivityMachine object.
	 */
	public void calculateConsumableCost(ActivityMachine activityMachine) {
		Double fuelConsumption = activityMachine.getActivityMachinePK()
				.getMachines().getFuelConsumption();
		if (fuelConsumption > 0) {
			Double consumableCostBudget = activityMachine.getDurationBudget()
					* fuelConsumption;
			this.activityMachine.setConsumablesCostBudget(consumableCostBudget);
		} else {
			this.activityMachine.setConsumablesCostBudget(0.0d);
		}
	}

	/**
	 * Save the relations between activities and machines.
	 */
	public void saveActivitiesAndMachine() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String registerMessage = "message_registro_guardar";
		Double costConsumableMachine = 0.0;
		try {
			if (this.listActivityMachineTemp != null
					&& this.listActivityMachineTemp.size() > 0) {
				for (ActivityMachine activityMachine : listActivityMachineTemp) {
					activitiesAndMachineDao
							.saveActivitiesAndMachine(activityMachine);
					if (activityMachine.getConsumablesCostBudget() == null)
						activityMachine.setConsumablesCostBudget(0.0);
					costConsumableMachine += activityMachine
							.getConsumablesCostBudget();
					ControladorContexto.mensajeInformacion(null, MessageFormat
							.format(bundle.getString(registerMessage),
									activityMachine.getActivityMachinePK()
											.getMachines().getName()));
				}
				if (selectedActivity.getCostMachinesEqBudget() == null) {
					selectedActivity.setCostMachinesEqBudget(0.0);
				}
				costConsumableMachine += selectedActivity
						.getCostMachinesEqBudget();
				selectedActivity.setCostMachinesEqBudget(costConsumableMachine);
				activitiesDao.editActivities(this.selectedActivity);
				setListActivityMachineTemp(null);
				showActivitiesAndMachineForActivity();
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Update the relations between activities and machines.
	 */
	public void updateActivityMachine() {
		try {
			ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
			String registerMessage = "message_registro_modificar";
			activitiesAndMachineDao
					.editActivitiesAndMachine(this.activityMachine);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(registerMessage), this.activityMachine
							.getActivityMachinePK().getMachines().getName()));
			showActivitiesAndMachineForActivity();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Delete the relations between activities and machines.
	 */
	public void deleteActivitiesAndMachine() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String message = "message_registro_eliminar";
		try {
			activitiesAndMachineDao
					.deleteActivitiesAndMachine(this.activityMachine);
			if (this.activityMachine.getConsumablesCostBudget() == null)
				this.activityMachine.setConsumablesCostBudget(0.0);
			Double costMachineBudget = this.selectedActivity
					.getCostMachinesEqBudget()
					- this.activityMachine.getConsumablesCostBudget();
			this.selectedActivity.setCostMachinesEqBudget(costMachineBudget);
			this.activitiesDao.editActivities(this.selectedActivity);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(message), activityMachine
							.getActivityMachinePK().getMachines().getName()));
			showActivitiesAndMachineForActivity();
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					this.activityMachine.getActivityMachinePK().getMachines()
							.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * It is responsible for validating that the time duration entered does not
	 * exceed the total time of the activity.
	 * 
	 * @param context
	 *            : Context for the view.
	 * @param toValidate
	 *            : Validate component.
	 * @param value
	 *            : Component value.
	 */
	public void validateDuration(FacesContext context, UIComponent toValidate,
			Object value) {
		String clientId = toValidate.getClientId(context);
		String flag = (String) toValidate.getAttributes().get("flag");
		boolean flagValue = (flag != null && "si".equals(flag)) ? true : false;
		Double duration = (Double) value;
		Double durationActivity = 0.0d;
		if (!flagValue) {
			durationActivity = (Double) ControladorFechas.restarFechas(
					selectedActivity.getInitialDtBudget(),
					selectedActivity.getFinalDtBudget());
		} else {
			durationActivity = (Double) ControladorFechas.restarFechas(
					activityMachine.getInitialDateTime(),
					activityMachine.getFinalDateTime());
		}
		if (duration > 0 && duration != null) {
			if (duration.compareTo(durationActivity) > 0) {
				String message = "message_activity_duration";
				ControladorContexto.mensajeErrorEspecifico(clientId, message,
						"mensaje");
				((UIInput) toValidate).setValid(false);
			}
		} else {
			String message = "message_greater_zero";
			ControladorContexto.mensajeErrorEspecifico(clientId, message,
					"mensaje");
			((UIInput) toValidate).setValid(false);
		}
	}

	/**
	 * It will calculate the length considering the different two dates for an
	 * activity machine.
	 */
	public void calculateDuration() {
		try {
			Double durationBudget = ControladorFechas.restarFechas(
					activityMachine.getInitialDateTime(),
					activityMachine.getFinalDateTime());
			activityMachine.setDurationBudget(durationBudget);
			calculateConsumableCost(this.activityMachine);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

}
