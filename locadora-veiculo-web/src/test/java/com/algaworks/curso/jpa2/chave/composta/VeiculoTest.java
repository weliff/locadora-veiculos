package com.algaworks.curso.jpa2.chave.composta;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class VeiculoTest {
    
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
    public void deveSalvarVeiculoComChaveComposta() throws Exception {
        Veiculo veiculo = new Veiculo();
        veiculo.setId(new VeiculoId("ABC-1234", "Tomé"));
        veiculo.setFabricante("Chevrolet");
        veiculo.setModelo("Camaro");
        
        em.getTransaction().begin();
        
        em.persist(veiculo);
        
        em.getTransaction().commit();
        
    }
    
}
