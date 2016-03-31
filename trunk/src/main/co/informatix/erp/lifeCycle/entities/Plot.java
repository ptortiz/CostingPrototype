package co.informatix.erp.lifeCycle.entities;

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
import javax.persistence.Transient;

/**
 * This class maps the table plot database
 * 
 * @author Johnatan.Naranjo
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "plot", schema = "life_cycle")
public class Plot implements Serializable {
	private int idPlot;
	private String name;
	private String description;
	private Double locationLinkToMap;
	private Double size;
	private Integer numberOfTrees;
	private Farm farm;
	private boolean seleccionado;

	/**
	 * @return idPlot: plot identifier
	 */
	@Id
	@Column(name = "idplot", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdPlot() {
		return idPlot;
	}

	/**
	 * @param idPlot
	 *            :plot identifier
	 */
	public void setIdPlot(int idPlot) {
		this.idPlot = idPlot;
	}

	/**
	 * @return name: plot name
	 */
	@Column(name = "name", length = 70, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            :plot name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return description: That is the plot description.
	 */
	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            :That is the plot description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return locationLinkToMap: link location plot
	 */
	@Column(name = "location_link_to_map")
	public Double getLocationLinkToMap() {
		return locationLinkToMap;
	}

	/**
	 * @param locationLinkToMap
	 *            :link location plot
	 * 
	 */
	public void setLocationLinkToMap(Double locationLinkToMap) {
		this.locationLinkToMap = locationLinkToMap;
	}

	/**
	 * @return size: plot size
	 */
	@Column(name = "size")
	public Double getSize() {
		return size;
	}

	/**
	 * @param size
	 *            :plot size
	 * 
	 */
	public void setSize(Double size) {
		this.size = size;
	}

	/**
	 * @return numberOfTrees: Number of trees that have the plot.
	 */
	@Column(name = "number_of_trees")
	public Integer getNumberOfTrees() {
		return numberOfTrees;
	}

	/**
	 * @param numberOfTrees
	 *            :Number of trees that have the plot.
	 */
	public void setNumberOfTrees(Integer numberOfTrees) {
		this.numberOfTrees = numberOfTrees;
	}

	/**
	 * @return farm: farm relate with a plot
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_farm", referencedColumnName = "idfarm", nullable = false)
	public Farm getFarm() {
		return farm;
	}

	/**
	 * @param farm
	 *            : farm relate with a plot
	 */
	public void setFarm(Farm farm) {
		this.farm = farm;
	}

	/**
	 * @return seleccionado: object selected from a list of plot
	 */
	@Transient
	public boolean isSeleccionado() {
		return seleccionado;
	}

	/**
	 * @param seleccionado
	 *            : object selected from a list of plot
	 */
	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idPlot;
		result = prime
				* result
				+ ((locationLinkToMap == null) ? 0 : locationLinkToMap
						.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((size == null) ? 0 : size.hashCode());
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
		Plot other = (Plot) obj;
		if (idPlot != other.idPlot)
			return false;
		if (locationLinkToMap == null) {
			if (other.locationLinkToMap != null)
				return false;
		} else if (!locationLinkToMap.equals(other.locationLinkToMap))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (size == null) {
			if (other.size != null)
				return false;
		} else if (!size.equals(other.size))
			return false;
		return true;
	}

}