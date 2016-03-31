package co.informatix.erp.warehouse.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.lifeCycle.dao.FarmDao;
import co.informatix.erp.lifeCycle.entities.Farm;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.warehouse.dao.DepositsDao;
import co.informatix.erp.warehouse.dao.MaterialsDao;
import co.informatix.erp.warehouse.dao.MeasurementUnitsDao;
import co.informatix.erp.warehouse.dao.PurchaseInvoicesDao;
import co.informatix.erp.warehouse.entities.Deposits;
import co.informatix.erp.warehouse.entities.Materials;
import co.informatix.erp.warehouse.entities.MeasurementUnits;
import co.informatix.erp.warehouse.entities.PurchaseInvoices;

/**
 * This class is all related logic with creating, updating and removal of
 * deposits.
 * 
 * @author Sergio.Ortiz
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class DepositsAction implements Serializable {

	@EJB
	private DepositsDao depositsDao;
	@EJB
	private MaterialsDao materialsDao;
	@EJB
	private FarmDao farmDao;
	@EJB
	private MeasurementUnitsDao measurementUnitsDao;
	@EJB
	private PurchaseInvoicesDao purchaseInvoicesDao;

	private Paginador paginador = new Paginador();

	private Deposits deposits;

	private Date fechaInicioBuscar;
	private Date fechaFinBuscar;

	private List<Deposits> listaDeposits;
	private List<SelectItem> itemsMaterial;
	private List<SelectItem> itemsFarm;
	private List<SelectItem> itemsMeasurementUnits;

	/**
	 * @return paginador: The paging controller object.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            :The paging controller object.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return deposits: deposits stored in data base
	 */
	public Deposits getDeposits() {
		return deposits;
	}

	/**
	 * @param deposits
	 *            : deposits stored in data base
	 */
	public void setDeposits(Deposits deposits) {
		this.deposits = deposits;
	}

	/**
	 * @return listaDeposits: list with all deposits stored in data base
	 */
	public List<Deposits> getListaDeposits() {
		return listaDeposits;
	}

	/**
	 * @param listaDeposits
	 *            : list with all deposits stored in data base
	 */
	public void setListaDeposits(List<Deposits> listaDeposits) {
		this.listaDeposits = listaDeposits;
	}

	/**
	 * @return itemsMaterial: List of materials that are loaded into the user
	 *         interface.
	 */
	public List<SelectItem> getItemsMaterial() {
		return itemsMaterial;
	}

	/**
	 * @param itemsMaterial
	 *            : List of materials that are loaded into the user interface.
	 */
	public void setItemsMaterial(List<SelectItem> itemsMaterial) {
		this.itemsMaterial = itemsMaterial;
	}

	/**
	 * @return itemsFarm: List of farms that are loaded into the user interface.
	 */
	public List<SelectItem> getItemsFarm() {
		return itemsFarm;
	}

	/**
	 * @param itemsFarm
	 *            : List of farms that are loaded into the user interface.
	 */
	public void setItemsFarm(List<SelectItem> itemsFarm) {
		this.itemsFarm = itemsFarm;
	}

	/**
	 * @return itemsMeasurementUnits: List of MeasurementUnits that are loaded
	 *         into the user interface.
	 */
	public List<SelectItem> getItemsMeasurementUnits() {
		return itemsMeasurementUnits;
	}

	/**
	 * @param itemsMeasurementUnits
	 *            : List of MeasurementUnits that are loaded into the user
	 *            interface.
	 */
	public void setItemsMeasurementUnits(List<SelectItem> itemsMeasurementUnits) {
		this.itemsMeasurementUnits = itemsMeasurementUnits;
	}

	/**
	 * @return fechaInicioBuscar: Determines the initial range to search for
	 *         deposits in the system
	 */
	public Date getFechaInicioBuscar() {
		return fechaInicioBuscar;
	}

	/**
	 * @param fechaInicioBuscar
	 *            : Determines the initial range to search for deposits in the
	 *            system
	 */
	public void setFechaInicioBuscar(Date fechaInicioBuscar) {
		this.fechaInicioBuscar = fechaInicioBuscar;
	}

	/**
	 * @return fechaFinBuscar: Determines the final range to search for deposits
	 *         in the system
	 */
	public Date getFechaFinBuscar() {
		return fechaFinBuscar;
	}

	/**
	 * @param fechaFinBuscar
	 *            : Determines the final range to search for deposits in the
	 *            system
	 */
	public void setFechaFinBuscar(Date fechaFinBuscar) {
		this.fechaFinBuscar = fechaFinBuscar;
	}

	/**
	 * Method to initialize the fields in the search.
	 * 
	 * @author Sergio.Ortiz
	 * @return consultarMateriales: Materials consulting method and redirects to
	 *         the template to manage materials.
	 */
	public String inicializarBusqueda() {
		this.fechaInicioBuscar = null;
		this.fechaFinBuscar = null;
		this.deposits = new Deposits();
		return consultarDeposits();
	}

	/**
	 * Consult the list of Deposits
	 * 
	 * @author Sergio.Ortiz
	 * 
	 * @return gesDeposits: Navigation rule that redirects to manage deposits
	 */
	public String consultarDeposits() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleRecursosHumanos = ControladorContexto
				.getBundle("mensajeWarehouse");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listaDeposits = new ArrayList<Deposits>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = depositsDao.cantidadDeposits(consulta, parametros);
			if (cantidad != null) {
				paginador.paginar(cantidad);
			}
			if (cantidad != null && cantidad > 0) {
				listaDeposits = depositsDao.consultarDeposits(
						paginador.getInicio(), paginador.getRango(), consulta,
						parametros);
			}
			if ((listaDeposits == null || listaDeposits.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaDeposits == null || listaDeposits.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleRecursosHumanos
										.getString("deposits_label"),
								unionMensajesBusqueda);
			}
			cargarDetallesDeposits();
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesDeposits";
	}

	/**
	 * This method allows to build the query to the advanced search and allows
	 * to construct messages displayed depending on the search criteria selected
	 * by the user.
	 * 
	 * @author Sergio.Ortiz
	 * @param consult
	 *            : query to concatenate
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags
	 * @param unionMessagesSearch
	 *            : message search
	 */
	private void busquedaAvanzada(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		SimpleDateFormat formato = new SimpleDateFormat(
				Constantes.DATE_FORMAT_MESSAGE_SIMPLE);

		if (this.fechaInicioBuscar != null && this.fechaFinBuscar != null) {
			consult.append("WHERE d.dateTime BETWEEN :fechaInicioBuscar AND :fechaFinBuscar ");
			SelectItem item = new SelectItem(fechaInicioBuscar,
					"fechaInicioBuscar");
			parameters.add(item);
			SelectItem item2 = new SelectItem(fechaFinBuscar, "fechaFinBuscar");
			parameters.add(item2);
			String dateFrom = bundle.getString("label_fecha_inicio") + ": "
					+ '"' + formato.format(this.fechaInicioBuscar) + '"' + ", ";
			unionMessagesSearch.append(dateFrom);

			String dateTo = bundle.getString("label_fecha_finalizacion") + ": "
					+ '"' + formato.format(fechaFinBuscar) + '"';
			unionMessagesSearch.append(dateTo);
			parameters.add(item2);

		}
	}

	/**
	 * Method of uploading the details of the list of deposits or relationships
	 * with other objects in the database.
	 * 
	 * @author Sergio.Ortiz
	 * @throws Exception
	 */
	private void cargarDetallesDeposits() throws Exception {
		if (this.listaDeposits != null) {
			for (Deposits deposits : this.listaDeposits) {
				cargarDetallesDeposits(deposits);
			}
		}
	}

	/**
	 * Method of uploading the details of a deposits or relationships with other
	 * objects in the database.
	 * 
	 * @author Sergio.Ortiz
	 * @param deposits
	 *            : deposits to load the details.
	 * @throws Exception
	 */
	public void cargarDetallesDeposits(Deposits deposits) throws Exception {
		int idDeposits = deposits.getIdDeposit();

		Materials materials = (Materials) depositsDao.consultarObjetoDeposits(
				"materials", idDeposits);
		PurchaseInvoices purchaseInvoices = (PurchaseInvoices) depositsDao
				.consultarObjetoDeposits("purchaseInvoices", idDeposits);
		Farm farm = (Farm) depositsDao.consultarObjetoDeposits("farm",
				idDeposits);
		MeasurementUnits measurementUnits = (MeasurementUnits) depositsDao
				.consultarObjetoDeposits("measurementUnits", idDeposits);

		deposits.setMaterials(materials);
		deposits.setPurchaseInvoices(purchaseInvoices);
		deposits.setFarm(farm);
		deposits.setMeasurementUnits(measurementUnits);

	}

	/**
	 * Method to edit or create a new deposits.
	 * 
	 * @author Sergio.Ortiz
	 * @param deposits
	 *            :deposit are adding or editing
	 * 
	 * @return "regDeposits":redirected to the template record deposits.
	 */
	public String agregarEditarDeposits(Deposits deposits) {
		try {
			if (deposits != null) {
				this.deposits = deposits;

			} else {
				this.deposits = new Deposits();
				this.deposits.setMaterials(new Materials());
				this.deposits.setFarm(new Farm());
				this.deposits.setMeasurementUnits(new MeasurementUnits());
				this.deposits.setPurchaseInvoices(new PurchaseInvoices());
				List<PurchaseInvoices> purchaseInvoicesList = purchaseInvoicesDao
						.consultPurchaseInvoices();
				this.deposits.setPurchaseInvoices(purchaseInvoicesList.get(0));
			}
			cargarCombos();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regDeposits";
	}

	/**
	 * This method allows you to load combo interface for registering a new
	 * deposits.
	 * 
	 * @author Sergio.Ortiz
	 * @throws Exception
	 */
	private void cargarCombos() throws Exception {
		itemsMaterial = new ArrayList<SelectItem>();
		itemsFarm = new ArrayList<SelectItem>();
		itemsMeasurementUnits = new ArrayList<SelectItem>();
		List<Materials> materials = materialsDao.consultarAllMateriales();
		List<Farm> farmsList = farmDao.consultarAllFarm();
		List<MeasurementUnits> measurementUnitsList = measurementUnitsDao
				.consultarMeasurementsUnits();
		if (materials != null) {
			for (Materials material : materials) {
				this.itemsMaterial.add(new SelectItem(material.getIdMaterial(),
						material.getName()));
			}
		}
		if (farmsList != null) {
			for (Farm farms : farmsList) {
				itemsFarm
						.add(new SelectItem(farms.getIdFarm(), farms.getName()));
			}
		}
		if (measurementUnitsList != null) {
			for (MeasurementUnits measurement : measurementUnitsList) {
				itemsMeasurementUnits.add(new SelectItem(measurement
						.getIdMeasurementUnits(), measurement.getName()));
			}
		}
	}

	/**
	 * Method used to save or edit the deposits
	 * 
	 * @author Sergio.Ortiz
	 * @return consultarDeposits: Redirects to manage deposits with a list of
	 *         updated deposits
	 */
	public String guardarDeposits() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {

			if (deposits.getIdDeposit() != 0) {
				depositsDao.editDeposits(deposits);
			} else {
				mensajeRegistro = "message_registro_guardar";
				depositsDao.saveDeposits(deposits);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro), deposits.getDateTime()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarDeposits();
	}

	/**
	 * Method that allows deposits to delete one database
	 * 
	 * @author Sergio.Ortiz
	 * @return consultarDeposits: Consult the list of deposits and returns to
	 *         manage deposits
	 */
	public String deleteDeposits() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			depositsDao.deleteDeposits(deposits);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					deposits.getDateTime()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					deposits.getDateTime());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return consultarDeposits();
	}

}