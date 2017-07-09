package com.algaworks.curso.jpa2.criteria;

import com.algaworks.curso.jpa2.modelo.Carro;
import com.algaworks.curso.jpa2.modelo.ModeloCarro;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ExemploCascata {

    private static EntityManagerFactory emf;

    private EntityManager em;

    static {
        emf = Persistence.createEntityManagerFactory("locadoraVeiculoPU");
    }

    @Before
    public void setUp() throws Exception {
        em = emf.createEntityManager();
    }

    @After
    public void tearDown() throws Exception {
        em.close();
    }

    @Test
    public void exemploEntidadeTransiente() throws Exception {
        Carro carro = new Carro();
        carro.setCor("Preto");
        carro.setPlaca("AAAA-1111");

        ModeloCarro modelo = new ModeloCarro();
        modelo.setDescricao("Ferrari");

        carro.setModelo(modelo);

        em.getTransaction().begin();
        em.persist(carro);
        em.getTransaction().commit();
    }

}
