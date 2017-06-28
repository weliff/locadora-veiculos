package com.algaworks.curso.jpa2.criteria;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.algaworks.curso.jpa2.modelo.Carro;

public class ExemplosCriteria {
    
    private static EntityManagerFactory emf;
    
    private EntityManager em;
    
    static {
        emf = Persistence.createEntityManagerFactory("locadoraVeiculoPU");
    }
    
    @Before
    public void setUp() {
        em = emf.createEntityManager();
    }
    
    @After
    public void tearDown() {
        this.em.close();
    }
    
    @Test
    public void projecoes() throws Exception {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<String> criteriaQuery = builder.createQuery(String.class);
        
        Root<Carro> carro = criteriaQuery.from(Carro.class);
        criteriaQuery.select(carro.<String>get("placa"));
        
        TypedQuery<String> query = em.createQuery(criteriaQuery);
        List<String> placas = query.getResultList();
        
        for (String placa : placas) {
            System.out.println(placa);
        }
        
    }
    
    
}
