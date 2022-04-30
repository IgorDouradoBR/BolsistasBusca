package IgorDouradoDellEstagio;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.nio.file.Paths;
import java.text.*;
import java.util.*;

public class App {
    private static List<Bolsista> bolsistas;//Instanciei como private para encapsular os objetos
    private static List<Bolsista> ordenado;//Servirá para ordenar pelo valor da bolsa sem prejudicar a ordem da lista original
    private static Map<Integer, ArrayList<Bolsista>> bolsistaZero;// para cada ano eu terei uma lista de bolsistas associados
                                                          // para a questao 1 e 3
    private static Scanner teclado;
    private static Scanner tecladoAux;//Criado para não dar conflito no Switch


    public static void main(String[] args) {
        carregaCSV();
        
        while (true) {
            teclado = new Scanner(System.in, "cp850");// Utilizei-me do cp850 para caso o nome tenha acento que o
                                                      // console tenha problema em printar, essa configuração irá normatizar
            tecladoAux = new Scanner(System.in, "cp850");
            System.out.println("---------------------------------- MENU ----------------------------------");//Linha para criar a divisória para o usuário se situar
            System.out.println("[1]- : Para saber informações sobre o primeiro bolsista de um determinado ano ");
            System.out.println("[2]- : Para saber como ficaria o nome de um determinado bolsista codificado ");
            System.out.println("[3]- : Para ter acesso à media dos valores das bolsas de um ano específico ");
            System.out.println("[4]- : Para exibir os 3 alunos com valores da bolsa mais altos e/ou mais baixos ");
            System.out.println("[5]- : Para finalizar a sessão do programa ");
            System.out.println("Digite a opção desejada: ");
            int opcao = verificaNumerico(1);//Esse método irá garantir que o usuário não digite nenhuma letra ou caractere especial
            
            switch (opcao) {
                case 1:
                    System.out.println("Informe o ano que deseja saber: ");
                    int anoDigitado = verificaNumerico();//Chamei esse método em toda interação com o usuário que venha a requerer apenas números
                    questao1(anoDigitado);//Criei os métodos das questões fora do Main para não ficar muito extenso e melhor distribuído
                    espera(720);//Delay proposital para maior fluidez e para o usuário não se "perder", isso se repetirá no prosseguir do código
                    break;
                case 2:
                    System.out.println("Informe o nome de um bolsista ou parte dele, acharemos correspondência se houver: ");
                    String buscou = teclado.nextLine();//não chameo o verificaNumerico pois se trata de uma entrada de letras

                    String semAcento = removeAcentos(buscou).toUpperCase().replaceAll("[^A-Z]+", "");//Irá trocar os acentos da palavra digitada pelo o usuário por seus respectivos caracteres equivalentes
                    while(semAcento.length()==0){//Verifica para que o usuário digite ao menos uma LETRA, caso digite apenas números e/ou caracteres especiais, o programa travará nessa verificação também até alguma letra ser digitada
                    System.out.println("nenhuma letra foi digitada, digite novamente a seguir: ");
                    buscou = teclado.nextLine();
                    semAcento = removeAcentos(buscou).toUpperCase().replaceAll("[^A-Z]+", "");
                    }
                    questao2(semAcento,false);//E só então o método da questão é chamado
                    espera(1280);//De novo o delay para maior fluidez para o usuário
                    break;
                case 3:
                    System.out.println("Informe o ano que deseja consultar: ");
                    int escolha = verificaNumerico();//Faz a verificação para haja apenas números
                    questao3(escolha);
                    espera(780);
                    break;
                case 4: // fala "aluno" mas nas modalidades nao consta "aluno", entao eu considerei todo
                        // mundo como um candidato possível
                    System.out.println("[1]- : Para saber as três bolsas com os valores mais altos\n[2]- : Para saber as três com os valores mais baixos \n[3]- : Para ambos acima ");
                    System.out.print("Informe o número da opção: ");//Dá a possibilidade de visualizar separadamente ou de forma conjuntas os mais baixos e mais altos valores
                    int choose = verificaNumerico();
                    while (choose != 1 && choose != 2 && choose != 3) {//Garante que uma das 3 opções seja digitada
                        System.out.println("Escolha apenas entre as opções de 1 a 3: ");
                        choose = verificaNumerico();
                    }
                    if(choose==3){//Se ele escolhar para ver ambas, chama o metodo 2 vezes com 2 entradas diferentes para visualizar as duas opções de uma só vez
                        questao4(1);
                        questao4(2);
                    }
                    else
                        questao4(choose);//Caso contrário chama apenas 1 vez com a forma escolhida pelo usuário
                    espera(1080);
                    break;
                case 5:
                    System.out.println("Obrigado por usar o programa, volte sempre!");//Mensagem para quando o usuário sair do programa :)
                    System.exit(0);//Comando para fechar a execuçao
                    break;
                default:
                    System.out.println("*Informe um número referente apenas a uma das 5 opções do menu abaixo* ");//Caso nenhuma das 5 opções seja escolhida pelo o usuário, ele chama novamente o menu
                    break;
            }
            espera(680);//Mais um delay constante, só que mais rápido
        }

    }

    public static void carregaCSV() {
        String currDir = Paths.get("").toAbsolutePath().toString();
        try (BufferedReader ler = new BufferedReader(
                new InputStreamReader(new FileInputStream(currDir + "\\" + "br-capes-bolsistas-uab.csv"), "utf-8"));) {

            bolsistas = new ArrayList<>();// //optei por ArrayList ao invés de LinkedList por tá fazendo mais operações
                                          // de get e add, mas nenhuma de remove, e no ArrayList tem complexidade apenas O(1) ao contrário do LinkedList
            bolsistaZero = new HashMap<>();// //pelas operaçoes de get e put serem O(1) escolhi HashMap ao invés de
                                           // TreeMap
            ler.readLine();//Garante que ele pule a primeira linha que é o cabeçalho do CSV
            String novaLinha;
            while ((novaLinha = ler.readLine()) != null) {//Enquanto houver linha, ele estará lendo
                String linha = novaLinha;
                String dados[] = linha.split(";");//O CSV em questão é quebrado por ;
                String nome = dados[0];//Daqui em diante peguei apenas os valores que variam entre as linhas CSV
                String cpf = dados[1];
                String nomeEntEnsino = dados[2];
                int anoReferencia = Integer.parseInt(dados[4]);
                int cdSGB = Integer.parseInt(dados[7]);
                String dsPagamento = dados[8];
                double valorPagamento = Double.parseDouble(dados[10]);//Considerei um double, pois como se trata de moeda poderia ter alguma entrada como 200.50 por exemplo

                Bolsista bols = new Bolsista(nome, cpf, nomeEntEnsino, anoReferencia, cdSGB, dsPagamento,
                        valorPagamento);//Instancio o objeto que será adicionado
                bolsistas.add(bols);
                if (bolsistaZero.get(anoReferencia) == null)//Aqui eu criei um dicionário para associar determinado ano a uma lista de bolsistas no qual o bolsista tem esse ano como ano de refefência. Foi minha estratégia pensada para a questão 1 e 3 que usam de anos e fazer uso de poucas operações(e de baixa complexidade de tempo) para deixar o código mais eficiente e rápido nas operações
                    bolsistaZero.put(anoReferencia, new ArrayList<Bolsista>());//Caso não já tenha o ano no dicionário, ele cadastrará, criando um Array e associando àquele ano
                else
                    bolsistaZero.get(anoReferencia).add(bols);//Caso já tenha o ano, ele realizará a operação para adicionar ao Array daquele determinado ano em questão
            }
            ordenado = new ArrayList<>(bolsistas);//Criei uma cópia da lista que contém os bolsistas para realizar o ordenamento pelo VALOR da bolsa e assim então não prejudicará a lista com a ordem original
            Collections.sort(ordenado);//Faço a ordenação com o comparador implementado na classe Bolsista
        } catch (IOException x) {
            System.err.format("Ocorreu o seguinte erro na leitura: %s%n", x);//Caso ele não consiga ler o CSV
        }
    }

    public static int questao1(int ano) {

        try {
            bolsistaZero.get(ano).get(bolsistaZero.get(ano).size() - 1).getNome();// Operação de complexidade baixa apenas para verificar se ele passará no try_catch antes de printar algo
            //LÓGICA: como está ordenado por ano do mais recente até o ano mais antigo, interpretei que o primeiro bolsista cadastrado no programa foi o último a aparecer no CSV, logo ele seria o bolsista zero do GERAL e segui essa lógica para cada ano, pegando o último bolsista de cada ano no CSV original
            //Abaixo as informações correspondentes ao bolsista zero do ano em questão
            System.out.println("Informações sobre o primeiro bolsista do ano de " + ano + ": ");
            System.out.println("Nome: " + bolsistaZero.get(ano).get(bolsistaZero.get(ano).size() - 1).getNome());
            ;
            System.out.println("CPF: " + bolsistaZero.get(ano).get(bolsistaZero.get(ano).size() - 1).getCpf());
            System.out.println("Entidade de Ensino: "
                    + bolsistaZero.get(ano).get(bolsistaZero.get(ano).size() - 1).getNomeEntEnsino());
            System.out.println("Valor da Bolsa: "
                    + bolsistaZero.get(ano).get(bolsistaZero.get(ano).size() - 1).getValorPagamento());
            return 1;//Retorno para possibilitar o teste na classe de testes
        } catch (Exception e) {
            System.out.println("Não foi encontrado nenhum bolsista no ano de " + ano);//Caso tenha sido digitado um ano que não contenha bolsistas
            return -1;//Retorno para possibilitar o teste na classe de teste
        }

    }

    public static String removeAcentos(String string) {
        if (string != null) {//Garante que não entre uma string vazia
            string = Normalizer.normalize(string, Normalizer.Form.NFD);//Normaliza
            string = string.replaceAll("[^\\p{ASCII}]", "");//Troca os caracteres acentuados por seus respectivos caracteres sem acento e de acordo com a tabela e padrão ASCII
        }
        return string;//Retorna a string sem acento 
    }

    public static StringBuilder codificaRecursivo(int fator, StringBuilder palavra) {//Método recursivo que irá garantir a primeira parte da codificação do nome do bolsista
        int tamanho = palavra.length();//Fiz esse cálculo aqui para não precisar fazer repetida vezes lá na frente
        if (tamanho == 1)//caso tenha só uma letra a palavra, ele retorna de imedianto
            return palavra;
        if (fator == 0) {//Faz a primeira parte da codificação, que é inverter a primeira e a última letra da palavra(nome)
            //OBSERVAÇÃO IMPORTANTE: Escolhi StringBuilder ao invés de String como o tipo de "palavra" por conta das operações específicas e a mais que essa Classe oferece (isso vale para essa parte e para as outras mais adiante)
            char primeiro = palavra.charAt(0);//Seleciona e armazena os caracteres
            char ultimo = palavra.charAt(tamanho - 1);
            palavra.setCharAt(0, ultimo);//Faz a inversão
            palavra.setCharAt(tamanho - 1, primeiro);//Finaliza a inversão
        }
        if (fator == 0 && (tamanho == 2 || tamanho == 3)) {// Instrução lógica para retornar caso não tenha ocorrido ainda uma recursao e a palavra tenha tamanho 2 ou 3, ou seja, só precisa de 1 inversão ao todo
            return palavra;
        }

        if (fator < Math.floor(tamanho / 2)) {//Número que será necessário trocar
            char antes = palavra.charAt(fator);//Mesma lógica usada acima porém o "fator" da recursão irá determinar qual caractere será pegado
            char depois = palavra.charAt((tamanho - 1) - fator);//Pega o da posição inversalmente equivalente
            String str;// Cria instância sem precisar necessariamente ser um StringBuilder
            if (tamanho % 2 != 0 && fator == Math.floor(tamanho / 2) - 1) // É a última recursão e o tamanho da palavra é impar, ou seja, vai
                                                                          // sobrar um caractere no meio
                str = "" + depois + palavra.charAt((int) Math.floor(tamanho / 2)) + antes;
            else
                str = "" + depois + codificaRecursivo(fator + 1, palavra) + antes;//REALIZA A RECURSÃO, refazendo a palavra de forma inversa

            return new StringBuilder(str);//Retorna em formato de StringBuilder quando a recursão está terminada
        }
        return new StringBuilder("");//Retorno para que a palavra mantenha a ordem logicamente correta caso seja ímpar

    }

    public static StringBuilder mascara(StringBuilder palavra) {//Irá fazer a segunda parte da codificação, ao adicionar "+1" na lógica alfabética a cada caractere presente na palavra

        for (int i = 0; i < palavra.length(); i++) {
            char aux = Character.toUpperCase(palavra.charAt(i));//Coloca em maiusculo cada caractere que vai sendo tratado da palavra
            if (aux == 'Z')// Ocorre a exceção com o 'Z', então tratei manualmente
                aux = 'A';
            else
                aux++;//Pega a próxima letra do alfabeto
            palavra.setCharAt(i, aux);//Troca na palavra original e faz isso para cada caractere
        }
        return palavra;//Retorna a palavra com a mascara aplicada
    }

    public static int questao2(String semAcento, boolean teste) {

        // Set<Bolsista> tiraRepetidos = new HashSet<>();//Pensei em usar a Collection Set pois notei ter nomes repetidos no CSV e isso iria garantir que isso não ocorresse, mas os anos dos objetos são diferentes, entao não cabe um Set pois são objetos "diferentes"
        List<Bolsista> candidatos = new ArrayList<>();//Crio uma lista que contenha os resultados retornado da pesquisa, pois dependendo da pesquisa do usuário, vai ter mais de um resultado possível e novamente instancio como um ArrayList por ser mais vantajoso para as operções que irei usar
        for (Bolsista b : bolsistas) {//for para pecorrer toda a lista de bolsistas em busca de correspondências
            if (b.getNome().replaceAll("[^A-Z]+", "").contains(semAcento)) {//Isso aqui irá garantir que ele leve em conta somente as letras digitadas, mesmo que haja caracteres especiais ou números contidos na busca e vai verificando se cada nome na lista corresponde ou contém o que foi buscada, é uma operação rápida por conta da forma que implementei e forma da Collection que escolhi
                candidatos.add(b);// pessoas achadas com o nome ou parte do nome buscado são adicionadas a uma lista de possíveis candidadtos
            }
        }

        for (int i = 0; i < candidatos.size(); i++) {//for para listar todas as possibilidades encontrada na tela do usuário
            String[] vetorNome = candidatos.get(i).getNome().split(" ");//Cria um vetor com o nome e sobrenomes do candidato com um propósito que mostrarei 2 linhas abaixo
            int len = vetorNome.length;// para economizar espaco embaixo
            System.out.println((i + 1) + " " + vetorNome[len - 2] + " " + vetorNome[len - 1]);//Mostrará apenas os 2 últimos nomes do candidato, assim então garantindo o sigilo dos outros candidatos que não seja quem está pesquisando de uma forma que ainda assim ele saberá se localizar e achar o número correspondente a seu nome entre os candidatos. Fiz isso pois o sigilo se mostra algo importante nessa questão
        }
        if (candidatos.size() == 0 ) {
            System.out.println("Não foi encontrado pessoas com esse nome");//Se não for achado nenhum candidato significa que não há correspondência para a sequência de caracteres que foi digitado pelo usuário
            return -1;//Método acabaria em cima
        } else {
            if(teste==true){
                return 1; //como achou candidatos considero como true, visto que os outros métodos testarei mais a frente
            }
            System.out.println("Selecione o numero que contenha os 2 ultimos sobrenomes correspondentes ao nome buscado: ");
            int escolha = verificaNumerico();//Irá selecionar o número que contém seus dois últimos nomes para assim então ser codificado
            
            while(escolha<=0 || escolha>candidatos.size()){//Faz a persistência para garantir que seja escolhido um número entre os mostrados como opção na tela
                System.out.println("Escolha um número apenas entre as "+candidatos.size()+" opcões: ");
                escolha = verificaNumerico();
            }
            String[] completo = candidatos.get(escolha - 1).getNome().split(" ");//Irá dividir cada parte do nome individualmente para codificar
            System.out.print("Nome codificado: ");
            for (int i = 0; i < completo.length; i++) {
                System.out.print(mascara(codificaRecursivo(0, new StringBuilder(completo[i]))) + " ");//Para cada parte do nome ele irá chamar o método recursivo para codificar e assim então aplicando a mascara, para fazer a parte final da codificação
            }
            //Printa as outras informações do bolsista presentes no enunciado
            System.out.println("\nAno: " + candidatos.get(escolha - 1).getAnoReferencia());
            System.out.println("Entidade de Ensino: " + candidatos.get(escolha - 1).getNomeEntEnsino());
            System.out.println("Valor bolsa: " + candidatos.get(escolha - 1).getValorPagamento());
            return 0;
        }
    }

    public static int questao3(int ano) {
        double total = 0;
        try {
            bolsistaZero.get(ano);//verificador de complexidade O(1) para verificar no try_catch se o ano consta no Map antes de printar algo na tela para o usuário, caso não, encaminha para a parte do catch
            for (int i = 0; i < bolsistaZero.get(ano).size(); i++) //Fiz o for dessa forma pois o index será importante para a soma
                total += bolsistaZero.get(ano).get(i).getValorPagamento();//Vai adicionando para cada bolsista achado com o ano digitado
            System.out.println("TOTAL DA MEDIA EM REAIS: R$" + formata(((total) / (bolsistaZero.get(ano).size()))));//No final faz a divisão do total de valores somados dos bolsistas daquele ano pela quantidade de bolsistas do ano, pegando assim então a média aritimética dos valores das bolsas daquele ano
            return 1;//Retorna para o teste como sinal de que houve êxito e conseguiu encontrar alguém naquele ano
        }catch(NullPointerException semAno){
            System.out.println("Não foi encontrada nenhuma bolsa no ano de "+ ano);
            return -1;//Retorno para caso não tenha sido encontrado ninguém para àquele ano
        }
    }

    public static String formata(Double valor) {
        DecimalFormat duasCasas = new DecimalFormat("#,##0.00");//Formata para o seguinte padrão
        duasCasas.setRoundingMode(RoundingMode.DOWN);//Faz o round
        return duasCasas.format(valor);//Retorna com o valor padronizado
    }

    public static String questao4(int opcao) {
        System.out.println("-----------------------------------------------------------");//Divisória visual
        System.out.println("As 3 pessoas com os valores mais " + (opcao == 1 ? "altos" : "baixos") + " de bolsa");//Verifica qual forma de visualição o usuário escolheu
        for (int i = 0; i < 3; i++) {//Irá em busca dos 3 últimos ou 3 primeiros da lista ordenada por Valores de bolsa
            //Printa as informações referentes a ele(a)
            System.out.print("Nome: " + ordenado.get(opcao == 1 ? i : (ordenado.size() - 1 - i)).getNome());
            System.out.print(
                    " / Valor: R$ " + formata(ordenado.get(opcao == 1 ? i : (ordenado.size() - 1 - i)).getValorPagamento()) + "\n");
        }
        System.out.println("-----------------------------------------------------------");
        return opcao==1? ordenado.get(0).getNome():ordenado.get(ordenado.size()-1).getNome();//Retorna para o Teste 
    }

    public static int verificaNumerico() {
        while (true) {
            try {
                int trata = teclado.nextInt();//Pega o que foi digitado pelo usuário
                return trata;//Em caso de não haver letras ou caracteres especiais contidos no que foi digitado e apenas números, ele retorna
            } catch (InputMismatchException e1) {
                teclado.next();//Controla o Scanner
                System.out.println("Ops... você digitou caracteres. Precisamos que tente novamente digitando apenas números a seguir: ");
                espera(310);// achei mais fluido com 310, para o usuário conseguir ler a mensagem
                System.out.print("=>");//Aponta para onde deve ser digitado
            }
        }
    }

    //usei de Polimorfismo para tratar do teclado auxiliar (não padrão), mas segue a mesma lógica anterior
    public static int verificaNumerico(int opcao) {
        if(opcao==1){
        while (true) {
            try {
                int trata = tecladoAux.nextInt();
                return trata;
            } catch (InputMismatchException e1) {
                tecladoAux.next();
                System.out.println("Ops... você digitou caracteres. Precisamos que tente novamente digitando apenas números a seguir: ");
                espera(310);
                System.out.print("=>");
            }
        }
    }   
        return 0;
    }

    public static void espera(int ms) {//Método para fazer o Delay e o usuário conseguir ler as mensagens na tela
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    

}
