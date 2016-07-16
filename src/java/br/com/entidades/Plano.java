package br.com.entidades;

import java.io.File;

/**
 *
 * @author Luiz Guilherme
 */
public class Plano {

    private int idLinha;
    private String nome;
    private String arquivo;
    private int versao;

    public int getIdLinha() {
        return idLinha;
    }

    public void setIdLinha(int idLinha) {
        this.idLinha = idLinha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getVersao() {
        return versao;
    }

    public void setVersao(int versao) {
        this.versao = versao;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

}
