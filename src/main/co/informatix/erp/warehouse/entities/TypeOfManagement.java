package co.informatix.erp.warehouse.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class containing the record of the types of management
 * 
 * @author Mabell.Boada
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "type_of_management", schema = "warehouse")
public class TypeOfManagement implements Serializable {
	private int idTypeOfManagement;
	private String name;
	private String description;

	/**
	 * @return idTypeOfManagement: Identifier management types
	 */
	@Id
	@Column(name = "idtypeofmanagement", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdTypeOfManagement() {
		return idTypeOfManagement;
	}

	/**
	 * @param idTypeOfManagement
	 *            : Identifier management types
	 */
	public void setIdTypeOfManagement(int idTypeOfManagement) {
		this.idTypeOfManagement = idTypeOfManagement;
	}

	/**
	 * @return name: Name the types of management
	 */
	@Column(name = "name", length = 100, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            : Name the types of management
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return description: Description of the types of management
	 */
	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            : Description of the types of management
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + idTypeOfManagement;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		TypeOfManagement other = (TypeOfManagement) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (idTypeOfManagement != other.idTypeOfManagement)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
