import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class Programa {
    private String path;
    private HashMap<Integer, String> instrucoes;
    private HashMap<Integer, String> dados;

    public Programa(String path) {
        this.path = path;
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
                    line = br.readLine().replaceAll("\u00A0", "").trim();
                    while (!line.contains(".endcode")) {
                        // cria hashmap para ler todas as instrucoes
                        instrucoes.put(chave++, line);

                        // atualiza a proxima linha
                        line = br.readLine();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPath() {
        return path;
    }
}
