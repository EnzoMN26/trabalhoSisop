import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        ArrayList<Processo> ready = new ArrayList<>();
        ArrayList<Processo> blocked = new ArrayList<>();
        ArrayList<Processo> aguardando = new ArrayList<>();
        ArrayList<Processo> deadlineAtingida = new ArrayList<>();
        ArrayList<Processo> finalizados = new ArrayList<>();
        ArrayList<String> registroDeadline = new ArrayList<>();
        int contadorTempo = 0;
        String programa;
        int periodo;
        int tempComp;
        int tempoAguardando;
        boolean execucaoValida;
        boolean entraInterface = true;

        // ready.add(new Processo("./prog1.txt", 10, 9));
        // ready.add(new Processo("./prog2.txt", 15, 5));
        // ready.add(new Processo("./prog3.txt", 17, 9));
        

        Scanner teclado = new Scanner(System.in);
        System.out.println("Bem vindo ao macXP");

        while(entraInterface){
            System.out.print("Digite o nome do programa que deseja executar: ");
            programa = teclado.nextLine();

            System.out.print("Digite o seu período: ");
            periodo = teclado.nextInt();

            System.out.print("Digite o seu tempo de computação: ");
            tempComp = teclado.nextInt();

            System.out.print("Digite o tempo de espera para iniciar: ");
            tempoAguardando = teclado.nextInt();
            if(tempoAguardando > 0 ) 
                aguardando.add(new Processo(programa+".txt", periodo, tempComp, tempoAguardando));
            else
                ready.add(new Processo(programa+".txt", periodo, tempComp, 0));
            System.out.println("Deseja adicionar outro programa?");
            System.out.println("1- Sim   0- Não");
            int saida = teclado.nextInt();
            teclado.nextLine();
            entraInterface = saida == 0 ? false : true;
            System.out.println("\n\n\n");
        }

        while(ready.size() > 0 || blocked.size() > 0 || deadlineAtingida.size() > 0 || aguardando.size() > 0){
            execucaoValida = true;
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
                    if(!prioritario.getDeadline()){
                        registroDeadline.add(prioritario.getPath() + " | Perda de Deadline em: " + contadorTempo);
                    }
                    prioritario.setaDeadline();
                    execucaoValida = false;
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
                    System.out.println("\nContadorTempo : "+contadorTempo);
                    System.out.println("Rodando : " + prioritario.toString());
                    System.out.println("--------------------\nDEADLINE : " + prioritario.getPeriodo());
                    System.out.println("PRONTOS : " + ready.toString());
                    System.out.println("BLOQUEADOS : "+blocked.toString());
                    System.out.println("ATINGIRAM DEADLINES : " + deadlineAtingida.toString());
                    System.out.println("FINALIZADOS : " + finalizados.toString());
                    System.out.println("\n-------------------------------------------------------------------");
                }
            } 

            if(execucaoValida){
                for(int i = 0; i<blocked.size();i++){

                    System.out.println("\n------------"+blocked.get(i).toString() + " Tempo bloqueado :" + blocked.get(i).getTempoBloqueado()+"------------");
                    blocked.get(i).reduzTempoBloqueado();

                    if(blocked.get(i).getTempoBloqueado() == 0){
                        ready.add(blocked.get(i));
                        blocked.remove(i);
                        i--;
                    }
                }  

                for(int i = 0; i<aguardando.size();i++){

                    System.out.println("\n------------"+aguardando.get(i).toString() + " Tempo Aguardando :" + aguardando.get(i).getTempoAguardando()+"------------");
                    aguardando.get(i).reduzTempoAguardando();

                    if(aguardando.get(i).getTempoAguardando() == 0){
                        ready.add(aguardando.get(i));
                        aguardando.remove(i);
                        i--;
                    }
                }  
            }
        }
        System.out.println(registroDeadline.toString());
        System.out.println("-------FIM-------");
    }
    public static void sort(ArrayList<Processo> list) {
 
        list.sort((p1, p2)
                  -> p1.getPeriodo().compareTo(
                      p2.getPeriodo()));

    }
}