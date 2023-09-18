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

        while(ready.size() > 0 || blocked.size() > 0){
            for(int i = 0; i<blocked.size();i++){
                    if(blocked.get(i).getTempoBloqueado() == 0){
                        ready.add(blocked.get(i));
                        blocked.remove(i);
                    }else{
                        blocked.get(i).reduzTempoBloqueado();
                        System.out.println("------------"+blocked.get(i).toString() + " tempo bloqueado :" + blocked.get(i).getTempoBloqueado()+"------------");
                    }
                }    
            if(ready.isEmpty()){
                    contadorTempo++;
            }else{
                sort(ready); //ordena os processos em ready

                Processo prioritario = ready.get(0); 

                System.out.println("Rodando : " + prioritario.toString());
                if(prioritario.getPeriodo() <= contadorTempo){ //se o processo tiver atingido seu deadline é removido do ready e adicionado na lista de vencidos
                    deadlineAtingida.add(prioritario);
                    ready.remove(0);
                }else{
                    prioritario.rodaInstrucao(); //executa uma instrucao do processo

                    if(prioritario.getStatus() == 0){ //se o programa tiver sido finalizado remove das listas
                        ready.remove(0);
                        //System.out.println("REMOVIDO");
                    }
                    else if(prioritario.getStatus() == 2){ //se o programa tiver sido bloqueado remove da lista de prontos e add na lista de bloqueados
                        blocked.add(prioritario);
                        ready.remove(0);
                    }
                    contadorTempo++;
                    System.out.println("ContadorTempo : "+contadorTempo);
                }
                System.out.println("-------------------- periodo : " + prioritario.getPeriodo());
            }  
            System.out.println("PRONTOS : " + ready.toString());
            System.out.println("BLOQUEADOS : "+blocked.toString());
            System.out.println("ATINGIRAM DEADLINES : " + deadlineAtingida.toString());
        }
        System.out.println("-------FIM-------");
    }
    public static void sort(ArrayList<Processo> list) {
 
        list.sort((p1, p2)
                  -> p1.getPeriodo().compareTo(
                      p2.getPeriodo()));

    }
}