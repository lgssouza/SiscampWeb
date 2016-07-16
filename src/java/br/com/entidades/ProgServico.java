package br.com.entidades;

public class ProgServico {

    private long idProgServico;
    private int idLinhaDist;
    private String descServico;
    private int semanaExec;
    private String dataExec;
    private int qtdPessoas;
    private int idPle;

    public long getIdProgServico() {
        return idProgServico;
    }

    public void setIdProgServico(long idProgServico) {
        this.idProgServico = idProgServico;
    }

    public int getIdLinhaDist() {
        return idLinhaDist;
    }

    public void setIdLinhaDist(int idLinhaDist) {
        this.idLinhaDist = idLinhaDist;
    }

    public String getDescServico() {
        return descServico;
    }

    public void setDescServico(String descServico) {
        this.descServico = descServico;
    }

    public int getSemanaExec() {
        return semanaExec;
    }

    public void setSemanaExec(int semanaExec) {
        this.semanaExec = semanaExec;
    }

    public String getDataExec() {
        return dataExec;
    }

    public void setDataExec(String string) {
        this.dataExec = string;
    }

    public int getQtdPessoas() {
        return qtdPessoas;
    }

    public void setQtdPessoas(int qtdPessoas) {
        this.qtdPessoas = qtdPessoas;
    }

    public int getIdPle() {
        return idPle;
    }

    public void setIdPle(int idPle) {
        this.idPle = idPle;
    }

}
