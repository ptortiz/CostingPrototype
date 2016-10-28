package co.informatix.erp.humanResources.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.humanResources.entities.AssistControl;

/**
 * 
 * This class is all the logic related to the creation, updating, and deleting
 * the assist control that may exist.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class AssistControlDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method consult assist control list with a certain range sent as a
	 * parameter and filtering the information by the values of sent search.
	 * 
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<AssistControl>: assist control list
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<AssistControl> consultAssistControl(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ac FROM AssistControl ac ");
		query.append("JOIN FETCH ac.hr h ");
		query.append(consult);
		query.append("ORDER BY h.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		List<AssistControl> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

}