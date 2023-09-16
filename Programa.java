import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class Programa {
    private String path;
    private int pc; //contator de em qual instrucao esta o programa
    private int periodo; //duracao do programa
    private int acc; //acumulador onde serao realizadas as operacoes
    private HashMap<Integer, String> instrucoes; //codigo do programa
    private HashMap<String, Integer> dados; //dados de entrada do programa
    private HashMap<String, Integer> labels; //loops no programa

    public Programa(String path) {
        this.path = path;
        this.pc = 0;
        this.periodo = 0;
        this.acc = 0;
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
                            periodo++;
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
            System.out.println(instrucoes);
            System.out.println(dados);
            System.out.println(labels);
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //funcao para limpar os comentarios de cada linha do programa
    public String cleanLine(String line){
        return line.replaceAll("# .*", "").trim();
    }

    public void rodaInstrucao() {

        String[] instrucao = instrucoes.get(pc).split(" ");
        String acao = instrucao[0];
        String op = instrucao[1];

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
                System.out.println("load");
                break;
            case "store":
                System.out.println("store");
                break;
            case "brany":

                break;
            case "brpos":

                break;
            case "brzero":

                break;
            case "brneg":

                break;
            case "syscall":

                break;
            default:
                return;
        }
        pc++;
    }

    public String getPath() {
        return path;
    }
}
