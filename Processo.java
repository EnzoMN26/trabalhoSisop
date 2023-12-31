import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Processo {
    private String path;
    private int pc; //contator de em qual instrucao esta o programa
    private Integer periodo; //duracao do programa
    private int tempComp; // tempo de computacao
    private int acc; //acumulador onde serao realizadas as operacoes
    private int status; // 0 = terminou | 1 = em execucao | 2 = bloqueado
    private HashMap<Integer, String> instrucoes; //codigo do programa
    private HashMap<String, Integer> dados; //dados de entrada do programa
    private HashMap<String, Integer> labels; //loops no programa
    private int tempoBloqueado;
    private int tempoAguardando;
    private boolean deadlineVencido;
    private Scanner teclado;

    public Processo(String path, Integer periodo, int tempComp, int tempoAguardando) {
        this.path = path;
        this.pc = 0;
        this.periodo = periodo;
        this.acc = 0;
        this.status = 1;
        this.tempoBloqueado = 0;
        this.tempComp = tempComp;
        this.tempoAguardando = tempoAguardando;
        this.deadlineVencido = false;
        this.instrucoes = new HashMap<Integer, String>();
        this.dados = new HashMap<String, Integer>();
        this.labels = new HashMap<String, Integer>();
        this.teclado = new Scanner(System.in);
        lePrograma(path);
    }

    public void lePrograma(String path) {
        File file = new File(path);
        FileReader fr;
        int chave = 0;

        try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = "";

            //faz a leitura do arquivo até a liha .enddata
            while (!line.contains(".enddata")) {
                line = cleanLine(br.readLine());
                //faz a leitura da area .code
                if (line.contains(".code")) {
                    line = cleanLine(br.readLine());
                    while (!line.contains(".endcode")) {
                        // cria hashmap para ler todas as instrucoes
                        if(line.contains(":")){
                            String[] temp = line.trim().split(":");
                            if (temp.length > 1){
                                labels.put(temp[0].trim().replaceAll(":", ""), chave);
                                instrucoes.put(chave++, temp[1].trim());
                            }else{
                                labels.put(line.trim().replaceAll(":", ""), chave);
                            }
                        }else{
                            instrucoes.put(chave++, line);
                        }
                        // atualiza a proxima linha
                        line = cleanLine(br.readLine());
                    }
                }

                chave = 0;

                //faz a leitura da area .data
                if (line.contains(".data")) {
                    line = cleanLine(br.readLine());
                    while (!line.contains(".enddata")) {
                        // cria hashmap para ler todas as instrucoes
                        String[] aux = line.split(" ");
                        dados.put(aux[0], Integer.parseInt(aux[1]));

                        // atualiza a proxima linha
                        line = cleanLine(br.readLine());
                    }
                }
            }
            // System.out.println(instrucoes);
            // System.out.println(dados);
            // System.out.println(labels);
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //funcao para limpar os comentarios de cada linha do programa
    public String cleanLine(String line){
        return line.replaceAll("# .*", "").toLowerCase().trim();
    }

    //roda a instrucao para qual o pc esta apontando
    public void rodaInstrucao() {
        Random rand = new Random();
        String[] instrucao = instrucoes.get(pc).split(" ");
        String acao = instrucao[0].trim();
        String op = instrucao[1].trim();     
        status = 1;
        

        if(tempComp <= 0){
            status = 0;
            return;
        }

        // switch verifica instrucao e, caso exista, incrementa o pc
        // caso nao exista ele retorna e nao incrementa o pc
        switch (acao) {
            case "add":
                if(op.contains("#")){
                    acc += Integer.parseInt(op.replace("#", ""));
                }else{
                    acc += dados.get(op);
                }
                break;
            case "sub":
                if(op.contains("#")){
                    acc -= Integer.parseInt(op.replace("#", ""));
                }else{
                    acc -= dados.get(op);
                }
                break;
            case "mult":
                if(op.contains("#")){
                    acc *= Integer.parseInt(op.replace("#", ""));
                }else{
                    acc *= dados.get(op);
                }
                break;
            case "div":
                if(op.contains("#")){
                    acc /= Integer.parseInt(op.replace("#", ""));
                }else{
                    acc /= dados.get(op);
                }
                break;
            case "load":
                if(op.contains("#")){
                    acc = Integer.parseInt(op.replace("#", ""));
                }else{
                    acc = dados.get(op);
                }
                break;
            case "store":
                dados.put(op, acc);
                break;
            case "brany":
                pc = labels.get(op);
                pc--;
                break;
            case "brpos":
                if(acc > 0){
                    pc = labels.get(op);
                    pc--;
                }
                break;
            case "brzero":
                if(acc == 0){
                    pc = labels.get(op);
                    pc--;
                }
                break;
            case "brneg":
                if(acc < 0){
                    pc = labels.get(op);
                    pc--;
                }
                break;
            case "syscall":
                
                if(Integer.parseInt(op) == 0){
                    status = 0;
                }
                else if(Integer.parseInt(op) == 1){
                    System.out.println("Valor do acumulador: " + acc);
                    tempoBloqueado = rand.nextInt(1,3);
                    status = 2;
                }
                else if(Integer.parseInt(op) == 2){
                    tempoBloqueado = rand.nextInt(1,3);
                    status = 2;
                    System.out.println("Digite um numero inteiro: ");
                    acc = teclado.nextInt();
                }
                break;
            default:
                return;
        }
        tempComp--;
        pc++;
        if(tempComp <= 0){
            status = 0;
            return;
        }
    }

    public String getPath() {
        return path;
    }

    public int getStatus(){
        return status;
    }

    public Integer getPeriodo(){
        return periodo;
    }

    public int getTempoBloqueado() {
        return tempoBloqueado;
    }

    public int getTempoAguardando() {
        return tempoAguardando;
    }

    public void reduzTempoAguardando() {
        this.tempoAguardando = tempoAguardando-1;
    }

    public void reduzTempoBloqueado() {
        this.tempoBloqueado = tempoBloqueado-1;
    }

    public void setaDeadline(){
        this.deadlineVencido = true;
    }

    public boolean getDeadline() {
        return this.deadlineVencido;
    }

    public String toString(){
        return path;
    }
}