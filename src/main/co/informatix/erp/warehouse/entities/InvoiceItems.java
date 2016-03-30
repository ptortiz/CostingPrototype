package co.informatix.erp.warehouse.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Class that contains the records of InvoiceItems.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "invoice_items", schema = "warehouse")
public class InvoiceItems implements Serializable {

	private int idInvoiceItem;
	private String quantity;
	private double subtotal;
	private double shipping;
	private double packaging;
	private double taxes;
	private double discount;
	private double unitCost;
	private double total;
	private PurchaseInvoices purchaseInvoice;
	private Materials material;

	/**
	 * @return idInvoiceItem: Invoice item Identifier.
	 */
	@Id
	@Column(name = "id_invoice_item", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdInvoiceItem() {
		return idInvoiceItem;
	}

	/**
	 * @param idInvoiceItem
	 *            : Invoice item Identifier.
	 */
	public void setIdInvoiceItem(int idInvoiceItem) {
		this.idInvoiceItem = idInvoiceItem;
	}

	/**
	 * @return quantity: : Invoice item quantity.
	 */
	@Column(name = "quantity")
	public String getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            : Invoice item quantity.
	 */
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return subtotal: Invoice item subtotal.
	 */
	@Column(name = "subtotal")
	public double getSubtotal() {
		return subtotal;
	}

	/**
	 * @param subtotal
	 *            : Invoice item subtotal.
	 */
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	/**
	 * @return shipping: Invoice item shipping.
	 */
	@Column(name = "shipping")
	public double getShipping() {
		return shipping;
	}

	/**
	 * @param shipping
	 *            : Invoice item shipping.
	 */
	public void setShipping(double shipping) {
		this.shipping = shipping;
	}

	/**
	 * @return packaging: Invoice item packaging.
	 */
	@Column(name = "packaging")
	public double getPackaging() {
		return packaging;
	}

	/**
	 * @param packaging
	 *            : Invoice item packaging.
	 */
	public void setPackaging(double packaging) {
		this.packaging = packaging;
	}

	/**
	 * @return taxes: Invoice item taxes.
	 */
	@Column(name = "taxes")
	public double getTaxes() {
		return taxes;
	}

	/**
	 * @param taxes
	 *            : Invoice item taxes.
	 */
	public void setTaxes(double taxes) {
		this.taxes = taxes;
	}

	/**
	 * @return discount: Invoice item discount.
	 */
	@Column(name = "discount")
	public double getDiscount() {
		return discount;
	}

	/**
	 * @param discount
	 *            : Invoice item discount.
	 */
	public void setDiscount(double discount) {
		this.discount = discount;
	}

	/**
	 * @return unitCost: Invoice item unit cost.
	 */
	@Column(name = "unit_cost")
	public double getUnitCost() {
		return unitCost;
	}

	/**
	 * @param unitCost
	 *            : Invoice item unit cost.
	 */
	public void setUnitCost(double unitCost) {
		this.unitCost = unitCost;
	}

	/**
	 * @return total: Invoice item total.
	 */
	@Column(name = "total")
	public double getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            : Invoice item total.
	 */
	public void setTotal(double total) {
		this.total = total;
	}

	/**
	 * @return purchaseInvoice: Purchase invoice to which the invoice item
	 *         belong.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_purchase_invoice", referencedColumnName = "idpurchaseinvoice", nullable = false)
	public PurchaseInvoices getPurchaseInvoice() {
		return purchaseInvoice;
	}

	/**
	 * @param purchaseInvoice
	 *            : Purchase invoice to which the invoice item belong.
	 */
	public void setPurchaseInvoice(PurchaseInvoices purchaseInvoice) {
		this.purchaseInvoice = purchaseInvoice;
	}

	/**
	 * @return material: Material to which the invoice item belong.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_material", referencedColumnName = "idmaterial", nullable = false)
	public Materials getMaterial() {
		return material;
	}

	/**
	 * @param material
	 *            : material to which the invoice item belong.
	 */
	public void setMaterial(Materials material) {
		this.material = material;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(discount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + idInvoiceItem;
		temp = Double.doubleToLongBits(packaging);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((quantity == null) ? 0 : quantity.hashCode());
		temp = Double.doubleToLongBits(shipping);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(subtotal);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(taxes);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(total);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(unitCost);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InvoiceItems other = (InvoiceItems) obj;
		if (Double.doubleToLongBits(discount) != Double
				.doubleToLongBits(other.discount))
			return false;
		if (idInvoiceItem != other.idInvoiceItem)
			return false;
		if (Double.doubleToLongBits(packaging) != Double
				.doubleToLongBits(other.packaging))
			return false;
		if (quantity == null) {
			if (other.quantity != null)
				return false;
		} else if (!quantity.equals(other.quantity))
			return false;
		if (Double.doubleToLongBits(shipping) != Double
				.doubleToLongBits(other.shipping))
			return false;
		if (Double.doubleToLongBits(subtotal) != Double
				.doubleToLongBits(other.subtotal))
			return false;
		if (Double.doubleToLongBits(taxes) != Double
				.doubleToLongBits(other.taxes))
			return false;
		if (Double.doubleToLongBits(total) != Double
				.doubleToLongBits(other.total))
			return false;
		if (Double.doubleToLongBits(unitCost) != Double
				.doubleToLongBits(other.unitCost))
			return false;
		return true;
	}

}