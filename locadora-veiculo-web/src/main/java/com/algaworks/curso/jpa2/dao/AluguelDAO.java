package com.algaworks.curso.jpa2.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.algaworks.curso.jpa2.modelo.Aluguel;
import com.algaworks.curso.jpa2.modelo.ModeloCarro;

public class AluguelDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public void salvar(Aluguel aluguel) {
		manager.merge(aluguel);
	}
	
	public List<Aluguel> buscarPorDataDeEntregaEModeloCarro(Date dataEntrega, ModeloCarro modeloCarro) {
	    CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
	    CriteriaQuery<Aluguel> criteriaQuery = criteriaBuilder.createQuery(Aluguel.class);
	    Root<Aluguel> a = criteriaQuery.from(Aluguel.class);
	    criteriaQuery.select(a);
	    
	    List<Predicate> predicates = new ArrayList<>();
	    if (dataEntrega != null) {
	        ParameterExpression<Date> dataEntregaInicial = criteriaBuilder.parameter(Date.class, "dataEntregaInicial");
	        ParameterExpression<Date> dataEntregaFinal = criteriaBuilder.parameter(Date.class, "dataEntregaFinal");

	        predicates.add(criteriaBuilder.between(a.<Date>get("dataEntrega"), dataEntregaInicial, dataEntregaFinal));
	    }
	    
	    if (modeloCarro != null) {
	        ParameterExpression<ModeloCarro> modeloExpression = criteriaBuilder.parameter(ModeloCarro.class, "modelo");
            predicates.add(criteriaBuilder.equal(a.get("carro").get("modelo"), modeloExpression));
	    }
	    
	    criteriaQuery.where(predicates.toArray(new Predicate[0]));
	    
	    TypedQuery<Aluguel> query = manager.createQuery(criteriaQuery);
	    
	    if (dataEntrega != null) {
	       Calendar dataInicialEntrega = Calendar.getInstance();
	       dataInicialEntrega.set(Calendar.HOUR_OF_DAY, 0);
	       dataInicialEntrega.set(Calendar.MINUTE, 0);
	       dataInicialEntrega.set(Calendar.SECOND, 0);
	       
	       Calendar dataFinalEntrega = Calendar.getInstance();
	       dataFinalEntrega.set(Calendar.HOUR_OF_DAY, 23);
	       dataFinalEntrega.set(Calendar.MINUTE, 59);
	       dataFinalEntrega.set(Calendar.SECOND, 59);
	       
	       query.setParameter("dataEntregaInicial", dataInicialEntrega.getTime());
	       query.setParameter("dataEntregaFinal", dataFinalEntrega.getTime());
	    }
	    
	    if (modeloCarro != null) {
	        query.setParameter("modelo", modeloCarro);
	    }
	    
	    return query.getResultList();
	    
	}

}
