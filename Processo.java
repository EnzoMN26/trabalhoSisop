import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Random;

public class Processo {
    private String path;
    private int pc; //contator de em qual instrucao esta o programa
    private Integer periodo; //duracao do programa
    private int acc; //acumulador onde serao realizadas as operacoes
    private int status; // 0 = terminou | 1 = em execucao | 2 = bloqueado
    private HashMap<Integer, String> instrucoes; //codigo do programa
    private HashMap<String, Integer> dados; //dados de entrada do programa
    private HashMap<String, Integer> labels; //loops no programa
    private int tempoBloqueado;

    public Processo(String path, Integer periodo) {
        this.path = path;
        this.pc = 0;
        this.periodo = periodo;
        this.acc = 0;
        this.status = 1;
        this.tempoBloqueado = 0;
        instrucoes = new HashMap<Integer, String>();
        dados = new HashMap<String, Integer>();
        labels = new HashMap<String, Integer>();
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
                            labels.put(line.trim().replaceAll(":", ""), chave);
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
            //System.out.println(instrucoes);
            //System.out.println(dados);
            //System.out.println(labels);
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //funcao para limpar os comentarios de cada linha do programa
    public String cleanLine(String line){
        return line.replaceAll("# .*", "").trim();
    }

    //roda a instrucao para qual o pc esta apontando
    public void rodaInstrucao() {
        Random rand = new Random();
        String[] instrucao = instrucoes.get(pc).split(" ");
        String acao = instrucao[0].trim();
        String op = instrucao[1].trim();

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
                break;
            case "brpos":
                if(acc > 0){
                    pc = labels.get(op);
                }
                break;
            case "brzero":
                if(acc == 0){
                    pc = labels.get(op);
                }
                break;
            case "brneg":
                if(acc < 0){
                    pc = labels.get(op);
                }
                break;
            case "syscall":
                if(Integer.parseInt(op) == 0){
                    status = 0;
                }
                else if(Integer.parseInt(op) == 1){
                    System.out.println(acc);
                    status = 2;
                }
                else if(Integer.parseInt(op) == 2){
                    tempoBloqueado = rand.nextInt(1,3);
                    status = 2;
                }
                break;
            default:
                return;
        }
        pc++;
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

    public void reduzTempoBloqueado() {
        this.tempoBloqueado = tempoBloqueado-1;
    }
}