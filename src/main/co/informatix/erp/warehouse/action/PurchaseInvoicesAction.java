package co.informatix.erp.warehouse.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.warehouse.dao.PurchaseInvoicesDao;
import co.informatix.erp.warehouse.dao.SuppliersDao;
import co.informatix.erp.warehouse.entities.PurchaseInvoices;
import co.informatix.erp.warehouse.entities.Suppliers;

/**
 * This class is all related logic with creating, updating and removal of
 * purchase invoices.
 * 
 * @author Liseth.Jimenez
 * @modify 28/03/2016 Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class PurchaseInvoicesAction implements Serializable {

	@EJB
	private PurchaseInvoicesDao purchaseInvoicesDao;
	@EJB
	private SuppliersDao suppliersDao;

	private String searchNumber;
	private String searchSupplier;

	private List<PurchaseInvoices> listInovoices;
	private PurchaseInvoices invoices;
	private PurchaseInvoices invoicesActualSelected;
	private List<SelectItem> itemsSupplier;

	private Date initialDateSearch;
	private Date finalDateSearch;

	private Paginador pagerForm = new Paginador();
	private Paginador pagination = new Paginador();

	/**
	 * @return searchNumber: gets the number the invoices by which you want to
	 *         consult purchases invoices
	 */
	public String getSearchNumber() {
		return searchNumber;
	}

	/**
	 * @param searchNumber
	 *            : sets the number the invoices by which you want to consult
	 *            purchases invoices
	 */
	public void setSearchNumber(String searchNumber) {
		this.searchNumber = searchNumber;
	}

	/**
	 * @return searchSupplier: gets the supplier name by which you want to
	 *         consult purchases invoices
	 */
	public String getSearchSupplier() {
		return searchSupplier;
	}

	/**
	 * @param searchSupplier
	 *            : gets the supplier name by which you want to consult
	 *            purchases invoices
	 */
	public void setSearchSupplier(String searchSupplier) {
		this.searchSupplier = searchSupplier;
	}

	/**
	 * @return listInovoices: gets the list of purchases invoices
	 */
	public List<PurchaseInvoices> getListInovoices() {
		return listInovoices;
	}

	/**
	 * @param listInovoices
	 *            : sets the list of purchases invoices
	 */
	public void setListInovoices(List<PurchaseInvoices> listInovoices) {
		this.listInovoices = listInovoices;
	}

	/**
	 * @return invoice: sets object of purchases invoices
	 */
	public PurchaseInvoices getInvoices() {
		return invoices;
	}

	/**
	 * @param invoice
	 *            :gets object of purchases invoices
	 */
	public void setInvoices(PurchaseInvoices invoices) {
		this.invoices = invoices;
	}

	/**
	 * @return itemsSupplier: List of supplier that are loaded into the user
	 *         interface.
	 */
	public List<SelectItem> getItemsSupplier() {
		return itemsSupplier;
	}

	/**
	 * @param itemsSupplier
	 *            :List of supplier that are loaded into the user interface.
	 */
	public void setItemsSupplier(List<SelectItem> itemsSupplier) {
		this.itemsSupplier = itemsSupplier;
	}

	/**
	 * @return initialDateSearch: gets the initial date of the supplier to
	 *         search in a range.
	 */
	public Date getInitialDateSearch() {
		return initialDateSearch;
	}

	/**
	 * @param initialDateSearch
	 *            :sets the initial date of the supplier to search in a range.
	 */
	public void setInitialDateSearch(Date initialDateSearch) {
		this.initialDateSearch = initialDateSearch;
	}

	/**
	 * @return finalDateSearch: gets the final date of the supplier to search in
	 *         a range.
	 */
	public Date getFinalDateSearch() {
		return finalDateSearch;
	}

	/**
	 * @param finalDateSearch
	 *            :sets the final date of the supplier to search in a range.
	 */
	public void setFinalDateSearch(Date finalDateSearch) {
		this.finalDateSearch = finalDateSearch;
	}

	/**
	 * @return pagerForm: Management purchases invoices paged list from search
	 *         engine.
	 */
	public Paginador getPagerForm() {
		return pagerForm;
	}

	/**
	 * @param pagerForm
	 *            : Management purchases invoices paged list from search engine.
	 */
	public void setPagerForm(Paginador pagerForm) {
		this.pagerForm = pagerForm;
	}

	/**
	 * @return pagination : Paginated list of purchase invoices which can be in
	 *         view
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Paginated list of purchase invoices which can be in view
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * Method to initialize the fields in the search.
	 * 
	 * @modify 29/03/2016 Wilhelm.Boada
	 * 
	 * @return consultInvoices: Method that consultation purchase invoices and
	 *         load the template with the information found
	 */
	public String searchInitialize() {
		this.searchNumber = "";
		this.searchSupplier = "";
		this.initialDateSearch = null;
		this.finalDateSearch = null;
		return consultInvoices();
	}

	/**
	 * This method allows consult a list of purchase invoices
	 * 
	 * @return giveBack: Navigation rule that redirects to manage purchase
	 *         invoices according condition
	 */
	public String consultInvoices() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleWarehouse = ControladorContexto
				.getBundle("mensajeWarehouse");
		ValidacionesAction validations = (ValidacionesAction) ControladorContexto
				.getContextBean(ValidacionesAction.class);
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder unionSearchMessages = new StringBuilder();
		String searchMessages = "";
		String param2 = ControladorContexto.getParam("param2");
		boolean fromModal = (param2 != null && "si".equals(param2)) ? true
				: false;
		String giveBack = fromModal ? "" : "manInvoices";
		this.listInovoices = new ArrayList<PurchaseInvoices>();
		try {
			advancedSearch(consult, parameters, bundle, bundleWarehouse,
					unionSearchMessages);
			Long count = purchaseInvoicesDao.countPurchaseInvoices(consult,
					parameters);
			if (count != null) {
				if (fromModal) {
					pagerForm.paginarRangoDefinido(count, 5);
					this.listInovoices = purchaseInvoicesDao
							.listPurchaseInvoices(pagerForm.getInicio(),
									pagerForm.getRango(), consult, parameters);
				} else {
					pagination.paginar(count);
					this.listInovoices = purchaseInvoicesDao
							.listPurchaseInvoices(pagination.getInicio(),
									pagination.getRango(), consult, parameters);
				}
			}

			if ((listInovoices == null || listInovoices.size() <= 0)
					&& !"".equals(unionSearchMessages.toString())) {
				searchMessages = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionSearchMessages);
			} else if (listInovoices == null || listInovoices.size() <= 0) {
				searchMessages = bundle
						.getString("message_no_existen_registros");
			} else if (!"".equals(unionSearchMessages.toString())) {
				String message = bundle
						.getString("message_existen_registros_criterio_busqueda");
				searchMessages = MessageFormat.format(message,
						bundleWarehouse.getString("purchase_invoice_label"),
						unionSearchMessages);
			}

			if (fromModal) {
				validations.setMensajeBusquedaPopUp(searchMessages);
			} else {
				validations.setMensajeBusqueda(searchMessages);
			}

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return giveBack;
	}

	/**
	 * This method constructs the query to the advanced search also allows
	 * assemble messages to display depending on the search criteria selected by
	 * the user.
	 * 
	 * @modify 29/03/2016 Wilhelm.Boada
	 * 
	 * @param consult
	 *            : query to concatenate
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            : access language tags
	 * @param bundleWarehouse
	 *            : access language tags warehouse
	 * @param unionSearchMessages
	 *            : Message search
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			ResourceBundle bundleWarehouse, StringBuilder unionSearchMessages) {
		boolean flag = false;
		SimpleDateFormat formatDate = new SimpleDateFormat(
				Constantes.DATE_FORMAT_MESSAGE_SIMPLE);

		if ((this.searchSupplier != null && !"".equals(this.searchSupplier))) {
			consult.append("WHERE ");
			consult.append(" UPPER(s.name) LIKE UPPER(:keywordSupplier) ");
			SelectItem itemNombre = new SelectItem("%" + this.searchSupplier
					+ "%", "keywordSupplier");
			parameters.add(itemNombre);
			unionSearchMessages.append(bundleWarehouse
					.getString("suppliers_label_nombre")
					+ ": "
					+ '"'
					+ this.searchSupplier + '"' + " ");
			flag = true;
		}

		if ((this.searchNumber != null && !"".equals(this.searchNumber))) {
			if (flag) {
				consult.append("AND ");
			} else {
				consult.append("WHERE ");
			}
			consult.append(" UPPER(pi.invoiceNumber) LIKE UPPER(:keywordNumber) ");
			SelectItem itemNombre = new SelectItem("%" + this.searchNumber
					+ "%", "keywordNumber");
			parameters.add(itemNombre);
			unionSearchMessages.append(bundleWarehouse
					.getString("purchase_invoice_label_number")
					+ ": "
					+ '"'
					+ this.searchNumber + '"' + " ");
			flag = true;
		}

		if (this.initialDateSearch != null && this.finalDateSearch != null) {
			if (flag) {
				consult.append("AND ");
			} else {
				consult.append("WHERE ");
			}
			consult.append("pi.dateTime BETWEEN :initialDateSearch AND :finalDateSearch ");
			SelectItem item = new SelectItem(initialDateSearch,
					"initialDateSearch");
			parameters.add(item);
			SelectItem item2 = new SelectItem(finalDateSearch,
					"finalDateSearch");
			parameters.add(item2);
			String dateFrom = bundle.getString("label_fecha_inicio") + ": "
					+ '"' + formatDate.format(this.initialDateSearch) + '"'
					+ " ";
			unionSearchMessages.append(dateFrom);

			String dateTo = bundle.getString("label_fecha_finalizacion") + ": "
					+ '"' + formatDate.format(finalDateSearch) + '"' + " ";
			unionSearchMessages.append(dateTo);
		}
		flag = false;
	}

	/**
	 * Method to edit or create a new invoice.
	 * 
	 * @param invoices
	 *            :invoice are adding or editing
	 * 
	 * @return "regInvoice":redirected to the template record invoice.
	 */
	public String addEditInvoices(PurchaseInvoices invoices) {
		try {
			loadSuppliers();
			if (invoices != null) {
				this.invoices = invoices;
			} else {
				this.invoices = new PurchaseInvoices();
				this.invoices.setSuppliers(new Suppliers());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regInvoice";
	}

	/**
	 * This method allows you to load the suppliers in interface for registering
	 * a new invoice.
	 * 
	 * 
	 * @throws Exception
	 */
	private void loadSuppliers() throws Exception {
		itemsSupplier = new ArrayList<SelectItem>();
		List<Suppliers> supplierList = suppliersDao.querySuppliers();
		if (supplierList != null) {
			for (Suppliers supplier : supplierList) {
				itemsSupplier.add(new SelectItem(supplier.getIdSupplier(),
						supplier.getName()));
			}
		}
	}

	/**
	 * Selects a single purchase invoice for display the associated transaction
	 * 
	 * @author Wilhelm.Boada
	 * 
	 * @param invoiceSelected
	 *            : invoice selected on the view
	 */
	public void selectInvoice(PurchaseInvoices invoiceSelected) {
		this.invoicesActualSelected = new PurchaseInvoices();
		invoicesActualSelected.setSelected(true);
		for (PurchaseInvoices invoice : listInovoices) {
			if (invoice.isSelected()) {
				if (invoice.getIdPurchaseInvoice() == invoicesActualSelected
						.getIdPurchaseInvoice()) {
					this.invoicesActualSelected = invoice;
				} else {
					invoice.setSelected(false);
				}
			}
		}
	}
}