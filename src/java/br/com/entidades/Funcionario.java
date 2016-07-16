package br.com.entidades;

public class Funcionario { 

    private int matricula;
    private String nome;
    private String tel;    
    private String usuario;
    private String senha;

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    
    public String getUsuario() {
        return usuario;
    }

   
    public String getSenha() {
        return senha;
    }

    
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    public void setSenha(String senha) {
        this.senha = senha;
    }

}
