package IgorDouradoDellEstagio;


public class Bolsista implements Comparable<Bolsista>{
    //Para ocupar menos memória, captei apenas os dados do CSV que variam entre os bolsistas e podem ser úteis
    private String nome = "";//Instanciei como private para encapsular os objetos
    private String cpf = "";//Inicialização da string
    private String nomeEntEnsino = "";
    private int anoReferencia;
    private int cdSGB;
    private String dsPagamento= "";
    private double valorPagamento; //Por se tratar de uma moeda que pode ter representação decimal importante para a análise, coloquei como tipo "double"
    public Bolsista(String nome, String cpf, String nomeEntEnsino, int anoReferencia, int cdSGB,
            String dsPagamento, double valorPagamento) {//Instancio o construtor
        this.nome = nome;
        this.cpf = cpf;
        this.nomeEntEnsino = nomeEntEnsino;
        this.anoReferencia = anoReferencia;
        this.cdSGB = cdSGB;
        this.dsPagamento = dsPagamento;
        this.valorPagamento = valorPagamento;
    }

    @Override 
    public int compareTo(Bolsista ordenador) {//Para exercer o comparador entre os bolsistas baseado no valor de pagamento da bolsa, fiz para ficar na ordem decrescente, dos maiores valores de bolsa até os menores
        if (this.valorPagamento > ordenador.getValorPagamento()) { 
          return -1; 
          } 
          if (this.valorPagamento < ordenador.getValorPagamento()) { 
          return 1; 
          } 
          return 0; 
         }


    //Abaixo os Gets e os Sets para cada atributo do objeto
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public String getNomeEntEnsino() {
        return nomeEntEnsino;
    }
    public void setNomeEntEnsino(String nomeEntEnsino) {
        this.nomeEntEnsino = nomeEntEnsino;
    }
    public int getAnoReferencia() {
        return anoReferencia;
    }
    public void setAnoReferencia(int anoReferencia) {
        this.anoReferencia = anoReferencia;
    }
    public int getCdSGB() {
        return cdSGB;
    }
    public void setCdSGB(int cdSGB) {
        this.cdSGB = cdSGB;
    }
    public String getDsPagamento() {
        return dsPagamento;
    }
    public void setDsPagamento(String dsPagamento) {
        this.dsPagamento = dsPagamento;
    }
    public double getValorPagamento() {
        return valorPagamento;
    }
    public void setValorPagamento(double valorPagamento) {
        this.valorPagamento = valorPagamento;
    }
    
}
