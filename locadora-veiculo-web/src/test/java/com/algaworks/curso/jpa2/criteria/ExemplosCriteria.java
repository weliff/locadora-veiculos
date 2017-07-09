package com.algaworks.curso.jpa2.criteria;

import com.algaworks.curso.jpa2.modelo.Aluguel;
import com.algaworks.curso.jpa2.modelo.Carro;
import com.algaworks.curso.jpa2.modelo.ModeloCarro;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.List;

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

    @Test
    public void funcoesAgregacoes() throws Exception {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> criteriaQuery = builder.createQuery(BigDecimal.class);

        Root<Aluguel> aluguel = criteriaQuery.from(Aluguel.class);
        criteriaQuery.select(builder.sum(aluguel.<BigDecimal>get("valorTotal")));

        TypedQuery<BigDecimal> query = em.createQuery(criteriaQuery);
        BigDecimal total = query.getSingleResult();

        System.out.println("Soma total dos alugueis: " + total);
    }

    @Test
    public void resultadoComplexo() throws Exception {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = builder.createQuery(Object[].class);

        Root<Carro> carro = criteriaQuery.from(Carro.class);
        criteriaQuery.multiselect(carro.get("placa"), carro.get("valorDiaria"));

        TypedQuery<Object[]> query = em.createQuery(criteriaQuery);
        List<Object[]> resultado = query.getResultList();

        for (Object[] valores : resultado) {
            System.out.println(valores[0] + "-" + valores[1]);
        }
    }

    @Test
    public void resultadoTupla() throws Exception {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = builder.createTupleQuery();

        Root<Carro> carro = criteriaQuery.from(Carro.class);
        criteriaQuery.multiselect(carro.get("placa").alias("placa"), carro.get("valorDiaria").alias("diaria"));

        TypedQuery<Tuple> query = em.createQuery(criteriaQuery);
        List<Tuple> resultados = query.getResultList();

        for (Tuple tuple : resultados) {
            System.out.println(tuple.get("placa") + " - " + tuple.get("diaria"));
        }
    }

    @Test
    public void resultadoConstrutores() throws Exception {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<PrecoCarro> criteriaQuery = builder.createQuery(PrecoCarro.class);

        Root<Carro> carro = criteriaQuery.from(Carro.class);
        criteriaQuery.select(builder.construct(PrecoCarro.class, carro.get("placa"), carro.get("valorDiaria")));

        TypedQuery<PrecoCarro> query = em.createQuery(criteriaQuery);
        List<PrecoCarro> resultados = query.getResultList();

        for (PrecoCarro precoCarro : resultados) {
            System.out.println(precoCarro.getPlaca() + " - " + precoCarro.getPreco());
        }

    }

    @Test
    public void exemploFuncao() throws Exception {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Carro> criteriaQuery = criteriaBuilder.createQuery(Carro.class);

        Root<Carro> carro = criteriaQuery.from(Carro.class);
        Predicate predicate = criteriaBuilder.equal(criteriaBuilder.upper(carro.<String>get("cor")),
                "prata".toUpperCase());

        criteriaQuery.select(carro);
        criteriaQuery.where(predicate);

        TypedQuery<Carro> query = em.createQuery(criteriaQuery);
        List<Carro> carros = query.getResultList();

        for (Carro c : carros) {
            System.out.println(c.getPlaca());
        }
    }

    @Test
    public void exemploOrdenacao() throws Exception {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Carro> criteriaQuery = criteriaBuilder.createQuery(Carro.class);

        Root<Carro> carro = criteriaQuery.from(Carro.class);

        Order order = criteriaBuilder.desc(carro.get("valorDiaria"));

        criteriaQuery.select(carro);
        criteriaQuery.orderBy(order);

        TypedQuery<Carro> query = em.createQuery(criteriaQuery);

        List<Carro> carros = query.getResultList();

        for (Carro c : carros) {
            System.out.println(c.getPlaca());
        }
    }

    @Test
    public void exemploJoinEFetch() throws Exception {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Carro> criteriaQuery = criteriaBuilder.createQuery(Carro.class);

        Root<Carro> carro = criteriaQuery.from(Carro.class);
        Join<Carro, ModeloCarro> join = (Join) carro.fetch("modelo");

        criteriaQuery.select(carro);
        criteriaQuery.where(criteriaBuilder.equal(join.get("descricao"), "Fit"));

        TypedQuery<Carro> query = em.createQuery(criteriaQuery);
        // Query query =
        // em.createNamedQuery("Carro.buscarComCarrosComAcessorios");

        List<Carro> carros = query.getResultList();

        for (Carro c : carros) {
            System.out.println(c.getPlaca() + c.getAcessorios());
        }
    }

    @Test
    public void subselect() throws Exception {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Carro> criteriaQuery = criteriaBuilder.createQuery(Carro.class);
        Subquery<Double> subquery = criteriaQuery.subquery(Double.class);

        Root<Carro> carro = criteriaQuery.from(Carro.class);
        Root<Carro> carroSub = subquery.from(Carro.class);

        subquery.select(criteriaBuilder.avg(carroSub.<Double>get("valorDiaria")));

        criteriaQuery.select(carro);
        criteriaQuery.where(criteriaBuilder.greaterThanOrEqualTo(carro.<Double>get("valorDiaria"), subquery));

        TypedQuery<Carro> query = em.createQuery(criteriaQuery);

        List<Carro> carros = query.getResultList();

        for (Carro c : carros) {
            System.out.println(c.getPlaca());
        }
        
    }
}
