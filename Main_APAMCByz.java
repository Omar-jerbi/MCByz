import java.util.Random;

public class Main_APAMCByz {	
	public static final int V = 1;	//valore di v sul quale ci si vuole accordare
	public static final int notV = 1 - V;
	
	public static final int T = 1;
	
	public static int MONETA;
	
	//PROCESSI: 0	1	2	3 (3 è inaffidabile)
	//array dei processi : [ [....], [....], [....], [....] ]
	// struttura: [ P0, P1, P2, P3 ] dove, per esempio P2 = [ bitRicevutoDaP0, bitRicevutoDaP1, bitInviatoDiP2,  bitRicevutoDaP3 ] 
	public static int[][] PROCs;

	//contiene la scelta che farà il processo i (i = 0, 1, 2, 3) al round successivo
	public static int[] NextRoundValue ;
	
	//procedura che inizializza le scelte dei processi
	public static void init() {
		PROCs = new int[4][4];
		int[] aux = {notV, V, V, -1}; //bit iniziali dei processi
		NextRoundValue = aux; 
				
		for(int i = 0; i<3; i++) {
			PROCs[i][i] = NextRoundValue[i];
		}

		//processo disonesto "invia" -1(non lo invia veramente), solo una notazione per dire che invia messaggi diversi agli altri processi
		PROCs[3][3] = -1;
	}

	public static int tally(int m, int j) {
		int sum = 0;
		for(int i = 0; i<4;i++) {
			if(PROCs[j][i] == m) sum++;
		}
		
		return sum;
	}
	
	public static int maj(int j) {
		int num0 = 0; //quanti zeri 
		int num1 = 0; //quanti uni
		for(int i = 0; i<4; i++) {
			if(PROCs[j][i] == 0) num0 +=1;
			if(PROCs[j][i] ==1) num1 +=1;			
		}
		
		if(num0>num1) return 0;
		else if (num0<num1)return 1;
		else {//se sono uguali return 0 o 1 a caso
			var r = new Random();
			return r.nextInt(2);
		}
	}
	
	public static void displayProcs() {
		for(var arrayLoc: PROCs) {
			for(var x : arrayLoc) {
				System.out.print(x + " ");
			}
		System.out.print("  |  ");
		}
		System.out.println();
	}
	
	
	
	public static void MCbyz(int proc_j) {
			//proc_j PROCESSO ONESTO			
			for(int i = 0; i<4; i++) {			
				PROCs[i][proc_j] = PROCs[proc_j][proc_j]; //trasmissione a tutti i processi
				PROCs[proc_j][i] = PROCs[i][i]; //ricezione da tutti i processi
				
				//azione del disonesto: spedisce il bit 1-(decisione di proc_j) a proc_j
				if(i==3) PROCs[proc_j][3] = 1-PROCs[proc_j][proc_j]; 
			}
						
			
			
			int maj = maj(proc_j);
			int tally = tally(maj, proc_j);
			
			
			//decisione per il round successivo:
			if(tally >= 2*T+1) {
				NextRoundValue[proc_j]= maj;
			}else {
				NextRoundValue[proc_j] = MONETA;//0 o 1
			}

	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) {
			
		int[][] RES = new int[1024][2]; //RES := [ [risultato 0/1, quanti round] , ....]
		for(int r = 0; r<1024; r++) {
			init();
			int round = 0;
			while(true) {//inizio round
				var ra = new Random();
				MONETA = ra.nextInt(2);
				
				
				//processi onesti + azione del disonesto
				for(int i = 0; i<3; i++) {
					MCbyz(i);			
				}
								
				//se si trova accordo => fine 
				if(PROCs[0][0]==PROCs[1][1] && PROCs[1][1] == PROCs[2][2]) break;

				//se non si trova accordo => ogni processo modifica la propria scelta per il round successivo 
				for(int i = 0; i<3; i++) {
					PROCs[i][i] = NextRoundValue[i];
				}

				round++;
			}//fine round
	

			RES[r][0] = PROCs[2][2];
			RES[r][1] = round;

		}
		

		//calcola il massimo numero di round in caso di raggiungimento dell'accordo V
		int MAXrounds = 0;
		for(var ar: RES) {
			if(ar[0] == V && ar[1]>=MAXrounds) MAXrounds = ar[1]; 
		}
				
		////////////////////////////////////
		
		Histogram histogram = new Histogram(MAXrounds+1); //histogramma di MAXrounds bin
		
		int[] count_rounds = new int[MAXrounds+1];
		for(var ar: RES) {
			if(ar[0] == V ) count_rounds[ar[1]]++;
		}
		
		int i = 0;
		for(var x : count_rounds) System.out.println("Numero round: " + (i++)+ " --- Runs:"+  x );
		
		
		for(var ar:RES) {
			if(ar[0] == V) histogram.addDataPoint(ar[1]);
		}
		
		
		StdDraw.setCanvasSize(1000,400);
		histogram.draw();
		
		
		
}

}
