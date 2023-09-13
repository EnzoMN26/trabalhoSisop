import java.io.*;
 
public class Main {
 
    public static void main(String[] args) throws Exception
    {

        File file = new File("*caminho do arquivo aqui*");

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;

        while ((st = br.readLine()) != null){
            System.out.println(st);
        }
    }
}