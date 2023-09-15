import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class Programa {
    private String path;
    private int pc;
    private HashMap<Integer, String> instrucoes;
    private HashMap<Integer, String> dados;

    public Programa(String path) {
        this.path = path;
        this.pc = 0;
        instrucoes = new HashMap<Integer, String>();
        dados = new HashMap<Integer, String>();
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

            while (!line.contains(".enddata")) {
                line = br.readLine();
                if (line.contains(".code")) {
                    line = br.readLine().trim();
                    while (!line.contains(".endcode")) {
                        // cria hashmap para ler todas as instrucoes
                        instrucoes.put(chave++, line);

                        // atualiza a proxima linha
                        line = br.readLine().trim();
                    }
                }

                chave = 0;

                if (line.contains(".data")) {
                    line = br.readLine().replace("\r", "").trim();
                    while (!line.contains(".enddata")) {
                        // cria hashmap para ler todas as instrucoes
                        dados.put(chave++, line);

                        // atualiza a proxima linha
                        line = br.readLine();
                    }
                }
            }
            System.out.println(instrucoes);
            System.out.println(dados);
            br.close();

            verificaInstrucoes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void verificaInstrucoes() {
        instrucoes.values().forEach(i -> rodaInstrucao(i));
        System.out.println(pc);
    }

    public void rodaInstrucao(String value) {

        // switch verifica instrucao e, caso exista, incrementa o pc
        // caso nao exista ele retorna e nao incrementa o pc
        switch (value.split(" ")[0]) {
            case "add":
                System.out.println("add");
                break;
            case "sub":
                System.out.println("sub");
                break;
            case "mult":

                break;
            case "div":

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
