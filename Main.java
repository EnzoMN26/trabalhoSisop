import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws Exception {
        ArrayList<Processo> ready = new ArrayList<>();
        ArrayList<Processo> blocked = new ArrayList<>();
        ArrayList<Processo> deadlineAtingida = new ArrayList<>();
        int contadorTempo = 0;

        ready.add(new Processo("./prog1.txt", 10));
        ready.add(new Processo("./prog2.txt", 5));
        ready.add(new Processo("./prog3.txt", 3));

        while(ready.size() > 0 && blocked.size() > 0){

            sort(ready); //ordena os processos em ready

            Processo prioritario = ready.get(0);
            
            if(prioritario.getPeriodo() > contadorTempo){ //se o processo tiver atingido seu deadline é removido do ready e adicionado na lista de vencidos
                deadlineAtingida.add(prioritario);
                ready.remove(0);
            }
            else{
                prioritario.rodaInstrucao(); //executa uma instrucao do processo

                if(prioritario.getStatus() == 0){ //se o programa tiver sido finalizado remove das listas
                    ready.remove(0);
                }
                else if(prioritario.getStatus() == 2){ //se o programa tiver sido bloqueado remove da lista de prontos e add na lista de bloqueados
                    blocked.add(prioritario);
                    ready.remove(0);
                }
                contadorTempo++;
            }
        }  
    }
    public static void sort(ArrayList<Processo> list) {
 
        list.sort((p1, p2)
                  -> p1.getPeriodo().compareTo(
                      p2.getPeriodo()));

    }
}