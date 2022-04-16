import java.awt.*;
import java.net.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.SourceDataLine;

class Principal{
	static int q1=0,q2=0,q3=0,k=0,r=0,processes_count;
	
	public static void main (String args[]){
		
		InfoProcesor[] listaInfo = citire();
		
		scrieInHtml(listaInfo);
		
		int indexUnitateTimp=0;//momentul actual de timp
		int indexProcesActiv=-1;//indexul procesului care urmeaza sa fie rulat
		int nrOperatiiRamase=0;//cate operatii mai are procesorul din cele puse la dispozitie(q1/q2/q3)
		int indexProcesIOActiv=-1;//index pentru procesul activ pe I/O
		
		ArrayList<Integer> coada1 = new ArrayList<>();
		ArrayList<Integer> coada2 = new ArrayList<>();
		ArrayList<Integer> coada3 = new ArrayList<>(); 
		ArrayList<Integer> coadaIO = new ArrayList<>(); 
		
		System.out.println("<BR><BR><BR><BR><BR>");
		System.out.println("<A NAME=\"sod\"></A><P ALIGN=left><B>SIMULATION OUTPUT DATA</B></P>");
		System.out.println("<TABLE WIDTH=100% BORDER=1>");
		System.out.println("<THEAD ALIGN=center>");
		System.out.println("<TR>");
		System.out.println("<TH>TIME</TH>");
		System.out.println("<TH>CPU</TH>");
		System.out.println("<TH>I/O</TH>");
		System.out.println("<TH>Q1 Queue</TH>");
		System.out.println("<TH>Q2 Queue</TH>");
		System.out.println("<TH>Q3 Queue</TH>");
		System.out.println("<TH>I/O Queue</TH>");
		System.out.println("</TR>");
		System.out.println("</THEAD>");
		System.out.println("<TBODY ALIGN=center>");

		
		while(true) {
			
			System.out.println("<TR>");
			System.out.println("<TD><FONT COLOR=blue>"+String.format("%06d", indexUnitateTimp)+"</FONT></TD>");
			
			
			for(int i=0;i<listaInfo.length;i++){
				if(listaInfo[i].TIME_START==indexUnitateTimp)
				coada1.add(i);
				if(listaInfo[i].getTbAdauagatInCoada()==true){
					if(listaInfo[i].getNr_coada() == 1) coada1.add(i);
                    else if(listaInfo[i].getNr_coada() == 2) coada2.add(i);
                    else coada3.add(i);
                    listaInfo[i].setTbAdauagatInCoada(false);
				}
			}
			
			if(indexProcesActiv==-1){
				if(coada1.size()!=0){indexProcesActiv=coada1.get(0); nrOperatiiRamase = q1;}
				else if(coada2.size()!=0) {indexProcesActiv=coada2.get(0);nrOperatiiRamase = q2;}
				else if(coada3.size()!=0) {indexProcesActiv=coada3.get(0);nrOperatiiRamase = q3;}
			}
			
			//============================
			if(indexProcesActiv != -1){
				if(listaInfo[indexProcesActiv].getNr_coada() == 3 && coada1.size()!=0){
					indexProcesActiv=coada1.get(0); nrOperatiiRamase = q1;
				}
				
				if(listaInfo[indexProcesActiv].getNr_coada() == 3 && coada2.size()!=0){
					indexProcesActiv=coada2.get(0); nrOperatiiRamase = q2;
				}
				
				if(listaInfo[indexProcesActiv].getNr_coada() == 2 && coada1.size()!=0){
					indexProcesActiv=coada1.get(0); nrOperatiiRamase = q1;
				}
			}
			//=============================
			
			if(indexProcesActiv == -1)System.out.println("<TD>CPU: -</TD>");
			else System.out.println("<TD>CPU: <A HREF=\"#p1\">"+listaInfo[indexProcesActiv].alias+"</A></TD>");
			
			if(indexProcesIOActiv == -1)
			if(coadaIO.size() != 0) indexProcesIOActiv = coadaIO.get(0);//pana aici=>
			
			if(indexProcesIOActiv == -1)System.out.println("<TD>I/O: -</TD>");
			else System.out.println("<TD>I/O: <A HREF=\"#p1\">"+listaInfo[indexProcesIOActiv].alias+"</A></TD>");
			
			
			if(coada1.size()!=0 && coada1.size()!=1){
				System.out.print("<TD>");
				for(int s=0;s<coada1.size();s++){
					if(coada1.get(s)!=indexProcesActiv){
						System.out.print(listaInfo[coada1.get(s)].getAlias());
					}
				}
				
				System.out.print("</TD>");
			}
			else System.out.println("<TD>-</TD>");
			
			if (coada2.size()==0||(coada1.size()==0 && coada2.size()==1))
				System.out.println("<TD>-</TD>");
			else{
				System.out.print("<TD>");
				for(int s=0;s<coada2.size();s++){
					if(coada2.get(s)!=indexProcesActiv){
						System.out.print(listaInfo[coada2.get(s)].getAlias());
					}
				}
				
				System.out.print("</TD>");
			}
			
			if (coada3.size()==0||(coada1.size()==0 && coada2.size()==0 && coada3.size()==1))
				System.out.println("<TD>-</TD>");
			else{
				System.out.print("<TD>");
				for(int s=0;s<coada2.size();s++){
					if(coada2.get(s)!=indexProcesActiv){
						System.out.print(listaInfo[coada2.get(s)].getAlias());
					}
				}
				
				System.out.print("</TD>");
			}
			
			if(coadaIO.size()!=0 && coadaIO.size()!=1){
				System.out.print("<TD>");
				for(int s=0;s<coadaIO.size();s++){
					if(coadaIO.get(s)!=indexProcesIOActiv){
						System.out.print(listaInfo[coadaIO.get(s)].getAlias());
					}
				}
				
				System.out.print("</TD>");
			}
			else System.out.println("<TD>-</TD>");
			
			
			System.out.println("</TR>");
			

			/* System.out.print(indexProcesActiv);
			System.out.print(" ");
			System.out.print(indexProcesIOActiv);
			System.out.println(""); */
			
			if(indexProcesActiv != -1){
				nrOperatiiRamase--;
				if(listaInfo[indexProcesActiv].getProcessFinished()==true && listaInfo[indexProcesActiv].getNr_coada()!=0)// un proces activ; indicele unui proces terminat=0
				{//procesele care au executat pentru a incheia tot
					if(listaInfo[indexProcesActiv].getNr_coada() == 1) coada1.remove(0);
					else if(listaInfo[indexProcesActiv].getNr_coada() == 2) coada2.remove(0);
					else coada3.remove(0);
					listaInfo[indexProcesActiv].setNr_coada(0);
					
					//randul rosu cu terminarea procesului
					System.out.println("<TR>");
					System.out.println("<TD COLSPAN=7 BGCOLOR=red>Process #"+String.valueOf(indexProcesActiv+1)+" is finished.</TD>");
					System.out.println("</TR>");
					
					indexProcesActiv=-1;
				}
				else{
					int stare = listaInfo[indexProcesActiv].incrementeaza();
					if(stare==0){
						//nu s-au terminat de executat toate operatiile CPU necesare repetitiei
						
						if(nrOperatiiRamase==0){
							//procesul primeste penalizare
							listaInfo[indexProcesActiv].setContorPenalizari();

							//se scoate procesul din coada din care face parte acum
							if(listaInfo[indexProcesActiv].getNr_coada() == 1) coada1.remove(0);
							else if(listaInfo[indexProcesActiv].getNr_coada() == 2) coada2.remove(0);
							else coada3.remove(0);
							
							//se apeleaza functia care actualizeaza automat coada din care face parte procesul
							listaInfo[indexProcesActiv].verifica_recompense(k,r);

							//se adauga procesul in coada din care face parte
							/* if(listaInfo[indexProcesActiv].getNr_coada() == 1) coada1.add(indexProcesActiv);
							else if(listaInfo[indexProcesActiv].getNr_coada() == 2) coada2.add(indexProcesActiv);
							else coada3.add(indexProcesActiv); */
								listaInfo[indexProcesActiv].setTbAdauagatInCoada(true);
							
							// indexProcesActiv e setat la -1
							indexProcesActiv=-1;
						}
					}
					
					else if(stare==1){
						//procesul primeste recompensa
						if(nrOperatiiRamase>0)
						listaInfo[indexProcesActiv].setContorRecompense();
						
						//adaugam procesul in coada I/O
						coadaIO.add(indexProcesActiv); 
							

						//se scoate procesul din coada din care face parte acum
						if(listaInfo[indexProcesActiv].getNr_coada() == 1) coada1.remove(0);
						else if(listaInfo[indexProcesActiv].getNr_coada() == 2) coada2.remove(0);
						else coada3.remove(0);
						
						//se apeleaza functia care actualizeaza automat coada din care face parte procesul
						listaInfo[indexProcesActiv].verifica_recompense(k,r);

						// indexProcesActiv e setat la -1
						indexProcesActiv=-1;
					}
				}
			}
			//aici tb sa ma ocup de IO
			
			if(indexProcesIOActiv!=-1){
				
				int stare = listaInfo[indexProcesIOActiv].incrementeaza();
				//daca starea e 0, inseamna ca nu s-au executat toate operatiile I/O din repetitie, nu se intampla nimic
				
				if(stare==2){
					//se adauga procesul in coada CPU din care face parte
					/* if(listaInfo[indexProcesIOActiv].getNr_coada() == 1) coada1.add(indexProcesIOActiv);
					else if(listaInfo[indexProcesIOActiv].getNr_coada() == 2) coada2.add(indexProcesIOActiv);
					else coada3.add(indexProcesIOActiv); */
						listaInfo[indexProcesIOActiv].setTbAdauagatInCoada(true);
						

					// se sterge procesul din coada IO
					coadaIO.remove(0);

					// indexProcesIOActiv e setat la -1
					indexProcesIOActiv=-1;
				}
				
				if(stare==3){
					//s-a terminat faza curenta=>se intampla acelasi lucru ca la 2
					/* if(listaInfo[indexProcesIOActiv].getNr_coada() == 1) coada1.add(indexProcesIOActiv);
					else if(listaInfo[indexProcesIOActiv].getNr_coada() == 2) coada2.add(indexProcesIOActiv);
					else coada3.add(indexProcesIOActiv); */
						listaInfo[indexProcesIOActiv].setTbAdauagatInCoada(true);
						

					// se sterge procesul din coada IO
					coadaIO.remove(0);

					//ae adauga randul galben
					
					System.out.println("<TR>");
					System.out.println("<TD COLSPAN=7 BGCOLOR=yellow>Phase #"+String.valueOf(listaInfo[indexProcesIOActiv].getFazaCurenta())+" of the Process #"+String.valueOf(indexProcesIOActiv+1)+" is finished.</TD>");
					System.out.println("</TR>");

					// indexProcesIOActiv e setat la -1
					indexProcesIOActiv=-1;
					
					
					
				}
			}
			//conditia de oprire
			
			int i;
			for(i=0;i<listaInfo.length && listaInfo[i].getNr_coada()==0;i++);	
			if(i==listaInfo.length)break;
			
			indexUnitateTimp++;
		}
		
		//randul rosu, s-a terminat simularea
		//System.out.println("");
		System.out.println("<TR>");
		System.out.println("<TD COLSPAN=7 BGCOLOR=red>Simulation is finished.</TD>");
		System.out.println("</TR>");
		System.out.println("</TBODY>");
		System.out.println("</TABLE>");
		System.out.println("</CENTER><A HREF=#top>top</A><CENTER>");
		System.out.println("</BODY>");
		System.out.println("</HTML>");
	}
	
	
	public static void scrieInHtml(InfoProcesor[] procese){
		System.out.println("<HTML>");
		System.out.println("<HEAD>");
		System.out.println("<TITLE>THREADS SIMULATION</TITLE>");
		System.out.println("</HEAD>");
		System.out.println("<BODY BACKGROUND=\"bkg.jpg\" BGPROPERTIES=\"fixed\">");
		System.out.println("<A NAME=\"top\"></A><BR><BR><CENTER>");
		System.out.println("<H1><B>THREADS SIMULATION</B></H1>");
		System.out.println("<BR><BR>");
		System.out.println("<TABLE WIDTH=30%>");
		System.out.println("<TR>");
		System.out.println("<TD><A HREF=\"#sid\">Simulation Input Data</A></TD>");
		System.out.println("</TR>");
		System.out.println("<TR>");
		System.out.println("<TD VALIGN=top><A HREF=\"#pd\">Processes Data</A></TD>");
		System.out.println("<TD>");
		
		for (int i = 0; i < processes_count; i++){
			System.out.println("<a href=\"#p" + String.valueOf(i+1) +"\">Process #" + String.valueOf(i+1) + "</a><br>");
		}
		
		System.out.println("</TD>");
		System.out.println("</TR>");
		System.out.println("<TR>");
		System.out.println("<TD><A HREF=\"#sod\">Simulation Output Data</A></TD>");
		System.out.println("</TR>");
		System.out.println("</TABLE>");
		System.out.println("<BR><BR><BR><BR><BR>");
		System.out.println("<A NAME=\"sid\"></A><P ALIGN=left><B>SIMULATION INPUT DATA</B></P>");
		System.out.println("<TABLE WIDTH=100% BORDER=1>"); 
		System.out.println("<THEAD ALIGN=center>"); 
		System.out.println("<TR>"); 
		System.out.println("<TH>MAX PRIORITY</TH>"); 
		System.out.println("<TH>NORMAL PRIORITY</TH>"); 
		System.out.println("<TH>MIN PRIORITY</TH>"); 
		System.out.println("<TH>PENALTY LIMIT</TH>"); 
		System.out.println("<TH>AWARD LIMIT</TH>"); 
		System.out.println("</TR>"); 
		System.out.println("</THEAD>"); 
		System.out.println("<TBODY ALIGN=center>"); 
		System.out.println("<TR>"); 
		
		System.out.println("<TD>q1 ="+q1+"</TD>");
		System.out.println("<TD>q2 ="+q2+"</TD>");
		System.out.println("<TD>q3 ="+q3+"</TD>");
		System.out.println("<TD>k ="+k+"</TD>");
		System.out.println("<TD>r ="+r+"</TD>");
		
		System.out.println("</TR>"); 
		System.out.println("</TBODY>"); 
		System.out.println("</TABLE>"); 
		System.out.println("</CENTER><A HREF=#top>top</A><CENTER>"); 
		System.out.println("<BR><BR><BR><BR><BR>"); 
		System.out.println("<A NAME=\"pd\"></A><P ALIGN=left><B>PROCESSES DATA</B></P>"); 
		System.out.println("<P ALIGN=left>Processes_Count = 5</P>"); 
		
		for(int i=0;i<processes_count;i++){
			
			System.out.println("<A NAME=\"p"+String.valueOf(i+1)+"\"></A><P ALIGN=left><B>PROCESS #"+String.valueOf(i+1)+"</B></P>");
			System.out.println("<TABLE WIDTH=100% BORDER=1>");
			System.out.println("<THEAD ALIGN=center>");
			System.out.println("<TR>");
			System.out.println("<TH>NAME</TH>");
			System.out.println("<TH>ALIAS</TH>");
			System.out.println("<TH>START TIME</TH>");
			System.out.println("<TH>PHASES COUNT</TH>");
			System.out.println("</TR>");
			System.out.println("</THEAD>");
			System.out.println("<TBODY ALIGN=center>");
			System.out.println("<TR>");
			System.out.println("<TD>"+procese[i].nume+"</TD>");
			System.out.println("<TD>"+procese[i].alias+"</TD>");
			System.out.println("<TD>"+procese[i].TIME_START+"</TD>");
			System.out.println("<TD>"+procese[i].PHASES_COUNT+"</TD>");
			System.out.println("</TBODY>");
			System.out.println("<THEAD ALIGN=center>");
			System.out.println("<TR>");
			System.out.println("<TH>PHASE COUNT</TH>");
			System.out.println("<TH>CPU TIMES COUNT</TH>");
			System.out.println("<TH>I/O TIMES COUNT</TH>");
			System.out.println("<TH>REPEAT COUNT</TH>");
			System.out.println("</TR>");
			System.out.println("</THEAD>");
			System.out.println("<TBODY ALIGN=center>");
			for(int j=0;j<procese[i].PHASES_COUNT;j++){
				
				System.out.println("<TR>");
				System.out.println("<TD>"+String.valueOf(j+1)+"</TD>");
				System.out.println("<TD>"+procese[i].PHASES[j][0]+"</TD>");
				System.out.println("<TD>"+procese[i].PHASES[j][1]+"</TD>");
				System.out.println("<TD>"+procese[i].PHASES[j][2]+"</TD>");
				System.out.println("</TR>");
				
			}
			System.out.println("</TBODY>");
			
			System.out.println("</TABLE>");
			System.out.println("</CENTER><A HREF=#top>top</A><CENTER>");
			System.out.println("<BR>");
		}
		
	}
	
	public static InfoProcesor[] citire(){
		int ok=0;
		InfoProcesor[] process={};
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("textfile.txt"));
			
			//citim prima linie, gasim q1,q2,q3,k,r
			String line = reader.readLine();
			
			String[] cuvinte = line.split(" ");
			for(int i=0;i<cuvinte.length;i++)
			{
				String[] cuvinte2=cuvinte[i].split("=");
				if(cuvinte2.length==2){ ok++;
					try{
						int number = Integer.parseInt(cuvinte2[1]);
						if(ok==1)q1=number;
						if(ok==2)q2=number;
						if(ok==3)q3=number;
						if(ok==4)k=number;
						if(ok==5)r=number;
					}
					catch (NumberFormatException ex){
						ex.printStackTrace();
					}
				}
			}
			
			//gasim processes_count + info procese
			line = reader.readLine();
			line = reader.readLine();
			line = reader.readLine();
			line = reader.readLine();
			String[] cuvinte3 = line.split("=");
			
			
			try{
				int number = Integer.parseInt(cuvinte3[1]);
				processes_count=number;
				
				//System.out.println("processes_count"+processes_count);
				
				process=new InfoProcesor[processes_count];
				
				line = reader.readLine();
				
				//determinam informatiile pentru fiecare proces
				for(int i=0;i<processes_count;i++){
					line = reader.readLine();
					line = reader.readLine();
					
					//linia 2, name
					String[] cuvinte_linia2 = line.split("=");
					String name=new String(cuvinte_linia2[1]);
					line = reader.readLine();
					
					//linia 3, start_time
					int start_time;
					String[] cuvinte_linia3 = line.split("=");
					
					int number1 = Integer.parseInt(cuvinte_linia3[1]);
					start_time=number1;
					
					line = reader.readLine();
					
					//linia 4, phases_count
					int phases_count;
					String[] cuvinte_linia4 = line.split("=");
					int number2 = Integer.parseInt(cuvinte_linia4[1]);
					phases_count=number2;
					
					//fazele unui proces
					int[][] faze=new int[phases_count][3];
					char alias = (char)('A' +i);
					InfoProcesor p=new InfoProcesor(name,alias,start_time,phases_count,faze);
					
					
					for(int j=0;j<phases_count;j++){
						int CPU=0,IO=0,REPEAT=0;
						line = reader.readLine();
						String[] cuvinte_linie_faza = line.split(" ");
						int nr=0;
						for(int t=0;t<cuvinte_linie_faza.length;t++)
						{
							String[] cuvinte4=cuvinte_linie_faza[t].split("=");
							if(cuvinte4.length==2){ 
								nr++;
								int number3 = Integer.parseInt(cuvinte4[1]);
								if(nr==1)CPU=number3;
								if(nr==2)IO=number3;
								if(nr==3)REPEAT=number3;
								
								
							}
						}
						p.adaugaPhase(j,CPU,IO,REPEAT);
					}
					
					process[i]=p;
					line = reader.readLine();
					
				}
				
				/* //afisare procese
				for(int i=0;i<processes_count;i++)
				{
					System.out.println(process[i].getName());
					System.out.println(process[i].getAlias());
					System.out.println(process[i].getTIME_START());
					System.out.println(process[i].getPHASES_COUNT());
					//System.out.println(process[i].getPHASES());
					
				} */
			}
			catch (NumberFormatException ex){
				ex.printStackTrace();
			}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return process;
		
		/* System.out.println("q1="+q1);
		System.out.println("q2="+q2);
		System.out.println("q3="+q3);
		System.out.println("k="+k);
		System.out.println("r="+r); */
	}
	
}