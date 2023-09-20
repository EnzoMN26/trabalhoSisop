import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws Exception {
        ArrayList<Processo> ready = new ArrayList<>();
        ArrayList<Processo> blocked = new ArrayList<>();
        ArrayList<Processo> deadlineAtingida = new ArrayList<>();
        ArrayList<Processo> finalizados = new ArrayList<>();
        int contadorTempo = 0;

        ready.add(new Processo("./prog1.txt", 100, 100));
        ready.add(new Processo("./prog2.txt",100, 100));
        ready.add(new Processo("./prog3.txt", 100, 100));

        while(ready.size() > 0 || blocked.size() > 0 || deadlineAtingida.size() > 0){
              
            if(ready.isEmpty() && deadlineAtingida.isEmpty()){
                    contadorTempo++;
            }else{
                
                Processo prioritario;
                ArrayList<Processo> fila;
                boolean prioridadeMaxima;

                if(deadlineAtingida.isEmpty()){
                    sort(ready); //ordena os processos em ready
                    prioritario = ready.get(0); // seta o prioritario com lista de ready
                    fila = ready;
                    prioridadeMaxima = false;
                }
                else{
                    sort(deadlineAtingida); //ordena os processos em deadlineatingida
                    prioritario = deadlineAtingida.get(0); // seta o prioritario com lista de deadlineatingida
                    fila = deadlineAtingida;
                    prioridadeMaxima = true;
                }
     
                if(prioritario.getPeriodo() <= contadorTempo && prioridadeMaxima == false){ //se o processo tiver atingido seu deadline é removido do ready e adicionado na lista de vencidos
                    deadlineAtingida.add(prioritario);
                    ready.remove(0);
                }else{
                    
                    prioritario.rodaInstrucao(); //executa uma instrucao do processo

                    if(prioritario.getStatus() == 0){ //se o programa tiver sido finalizado remove das listas
                        fila.remove(0);
                        finalizados.add(prioritario);
                        //System.out.println("REMOVIDO");
                    }
                    else if(prioritario.getStatus() == 2){ //se o programa tiver sido bloqueado remove da lista de prontos (ou deadline) e add na lista de bloqueados
                        blocked.add(prioritario);
                        fila.remove(0);
                    }
                    contadorTempo++;
                    System.out.println("ContadorTempo : "+contadorTempo);
                    System.out.println("Rodando : " + prioritario.toString());
                    System.out.println("-------------------- periodo : " + prioritario.getPeriodo());
                    System.out.println("PRONTOS : " + ready.toString());
                    System.out.println("BLOQUEADOS : "+blocked.toString());
                    System.out.println("ATINGIRAM DEADLINES : " + deadlineAtingida.toString());
                    System.out.println("FINALIZADOS : " + finalizados.toString());
                    System.out.println("\n\n\n\n\n");
                }
            }  

            for(int i = 0; i<blocked.size();i++){

                System.out.println("------------"+blocked.get(i).toString() + " tempo bloqueado :" + blocked.get(i).getTempoBloqueado()+"------------\n");
                blocked.get(i).reduzTempoBloqueado();

                if(blocked.get(i).getTempoBloqueado() == 0){
                    ready.add(blocked.get(i));
                    blocked.remove(i);
                }
            }  
            
        }
        System.out.println("-------FIM-------");
    }
    public static void sort(ArrayList<Processo> list) {
 
        list.sort((p1, p2)
                  -> p1.getPeriodo().compareTo(
                      p2.getPeriodo()));

    }
}