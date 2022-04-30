package IgorDouradoDellEstagio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class AppTesta {

    @Test
    public void testaQuestao1True() {
        App.carregaCSV();// Aqui também estou testando se a leitura do CSV correta e é necessário esse
                         // método aqui para o funcionamente dos testes que envolvem a leitura e as listas que derivam dela
        assertEquals(1, App.questao1(2013));// Verifica se acha e mantém esse padrão de devolver 1 caso ache
    }

    @Test
    public void testaQuestao1False() {
        App.carregaCSV();
        assertEquals(-1, App.questao1(2009));// Verifica se não acha e mantém esse padrão de devolver -1 caso não ache
    }

    @Test
    public void testaQuestao2True() {
        App.carregaCSV();
        assertEquals(1, App.questao2("LAISE", true));// Nomes não completo mas ele consegue achar corresponência, para ser possível pesquisar com sobrenome e espaços é necessários outros métodos do programa
    }

    @Test
    public void testaQuestao2False() {
        App.carregaCSV();
        assertEquals(-1, App.questao2("LAISEE", true));// Não há esse nome na lista(com um 'E' a mais no LAISE), então dará como -1
    }

    @Test
    public void testaQuestao3True() {
        App.carregaCSV();
        assertEquals(1, App.questao3(2015));// Ano que consta
    }

    @Test
    public void testaQuestao3False() {
        App.carregaCSV();
        assertEquals(-1, App.questao3(2011));// Ano que não constam bolsas
    }

    @Test
    public void testaQuestao4Opcao1() {
        App.carregaCSV();
        assertEquals("LIAMARA SCORTEGAGNA", App.questao4(1));// Primeiro nome em questão
    }

    @Test
    public void testaQuestao4Opcao2() {
        App.carregaCSV();
        assertEquals("ROSILENE DE LIMA", App.questao4(2));// Primeiro nome em questão
    }

    @Test
    public void testaRemoverDeAcento() {
        assertEquals("acentuada", App.removeAcentos("àçêñtúádã"));// Troca as letras acentuadas por suas letras
                                                                  // correspondentes sem acentuação, elas serão
                                                                  // colocadas em maiusculas no metodo de mascara
    }

    // Fiz 4 testes a seguir, com as 3 palavras de exemplo do teste e mais o meu
    // sobrenome como exemplo
    // Para o metodo de codificadorRecursivo e o de mascara
    // as palavras serão tratadas NECESSARIAMENTE em maiusculas no metodo mascara,
    // metodo interligado com o anterior e esse próximo
    @Test
    public void testaCodificadorRecursivo1() {
        assertEquals(new StringBuilder("PGIREO").toString(),
                App.codificaRecursivo(0, new StringBuilder("PERIGO")).toString());//
    }

    @Test
    public void testaCodificadorRecursivo2() {
        assertEquals(new StringBuilder("FGUA").toString(),
                App.codificaRecursivo(0, new StringBuilder("FUGA")).toString());//
    }

    @Test
    public void testaCodificadorRecursivo3() {
        assertEquals(new StringBuilder("ZAP").toString(),
                App.codificaRecursivo(0, new StringBuilder("PAZ")).toString());//
    }

    @Test
    public void testaCodificadorRecursivo4() {
        assertEquals(new StringBuilder("DDARUOO").toString(),
                App.codificaRecursivo(0, new StringBuilder("DOURADO")).toString());//
    }

    @Test
    public void testaMascara1() {
        assertEquals(new StringBuilder("QHJSFP").toString(),
                App.mascara(App.codificaRecursivo(0, new StringBuilder("perigo"))).toString());//
    }

    @Test
    public void testaMascara2() {
        assertEquals(new StringBuilder("GHVB").toString(),
                App.mascara(App.codificaRecursivo(0, new StringBuilder("fuGA"))).toString());// metade minuscula e
                                                                                             // metade maiuscula para
                                                                                             // testar
    }

    @Test
    public void testaMascara3() {
        assertEquals(new StringBuilder("ABQ").toString(),
                App.mascara(App.codificaRecursivo(0, new StringBuilder("paz"))).toString());//
    }

    @Test
    public void testaMascara4() {
        assertEquals(new StringBuilder("EEBSVPP").toString(),
                App.mascara(App.codificaRecursivo(0, new StringBuilder("dourado"))).toString());//
    }

    @Test
    public void testaFormata() {
        assertEquals("902,37", App.formata(902.37813));// Formata para um padrão monetário
    }

    // O verificadorNumerico depende de um imput do teclado do usuário para
    // funcionar, por isso não foi testado, mas ele
    // foi implicítamente testado junto com outros metodos acima.

}
