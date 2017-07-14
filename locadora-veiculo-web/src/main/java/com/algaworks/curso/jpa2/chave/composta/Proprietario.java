package com.algaworks.curso.jpa2.chave.composta;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;

@Embeddable
public class Proprietario {
    
    @Column(name = "nome_proprietario")
    private String nome;
    
    
    @ElementCollection
    @CollectionTable(name="proprietario_telefone", 
        joinColumns = { @JoinColumn(name = "placa", referencedColumnName = "placa"), 
                @JoinColumn(name = "cidade", referencedColumnName = "cidade")})
    @Column(name = "numero_telefone")
    private List<String> telefones = new ArrayList<>();

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<String> getTelefones() {
        return telefones;
    }

    public void setTelefones(List<String> telefones) {
        this.telefones = telefones;
    }

//    public String getCpf() {
//        return cpf;
//    }
//
//    public void setCpf(String cpf) {
//        this.cpf = cpf;
//    }
}
