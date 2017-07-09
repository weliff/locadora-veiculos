package com.algaworks.curso.jpa2.criteria;

import java.math.BigDecimal;

public class PrecoCarro {
    
    private String placa;
    private BigDecimal preco;

    public String getPlaca() {
        return placa;
    }
    public void setPlaca(String placa) {
        this.placa = placa;
    }
    public BigDecimal getPreco() {
        return preco;
    }
    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
    
}
