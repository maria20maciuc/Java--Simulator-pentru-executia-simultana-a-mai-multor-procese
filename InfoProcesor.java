import java.awt.*;
import java.net.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class InfoProcesor{
	String nume;
	char alias;
	int TIME_START;
	int PHASES_COUNT;
	int[][] PHASES;
	int contorRecompense, contorPenalizari;//pt penalizari/recompense
	int nr_coada;//poate lua valorile 1, 2 sau 3
	int fazaCurenta, repetitiaCurenta, CPUcountCurent,IOcountCurent;
	boolean processFinished;
	boolean tbAdauagatInCoada;
	
	public InfoProcesor(String nume, char alias, int TIME_START, int PHASES_COUNT, int[][] PHASES){
		this.nume=nume;
		this.alias=alias;
		this.TIME_START=TIME_START;
		this.PHASES_COUNT=PHASES_COUNT;
		this.PHASES=new int[PHASES_COUNT][3];
		
		this.contorRecompense = 0;
		this.contorPenalizari = 0;
		this.nr_coada=1;
		
		this.fazaCurenta = 0;
		this.repetitiaCurenta = 0;
		this.CPUcountCurent = 0;
		this.IOcountCurent = 0;

		this.processFinished = false;
		this.tbAdauagatInCoada = false;
	}

	public boolean getTbAdauagatInCoada() {
		return tbAdauagatInCoada;
	}

	public void setTbAdauagatInCoada(boolean value) {
		tbAdauagatInCoada = value;
	}


	public boolean getProcessFinished(){
		return processFinished;
	}

	public int getContorRecompense(){
		return contorRecompense;
	}

	public int getContorPenalizari(){
		return contorPenalizari;
	}

	public int getNr_coada(){
		return nr_coada;
	}

	public void setContorRecompense(){
		contorRecompense++;
	}

	public void setContorPenalizari(){
		contorPenalizari++;
	}

	public void setNr_coada(int nou){
		nr_coada=nou;
	}

	public int getRepetitiaCurenta() {
		return repetitiaCurenta;
	}

	public int getFazaCurenta() {
		return fazaCurenta;
	}

	public int getCPUcountCurent() {
		return CPUcountCurent;
	}

	public int getIOcountCurent() {
		return IOcountCurent;
	}


	public String getName(){
		return nume;
	}
	
	public char getAlias(){
		return alias;
	}

	public int getTIME_START(){
		return TIME_START;
	}

	public int getPHASES_COUNT(){
		return PHASES_COUNT;
	}

	public int[][] getPHASES(){
		return PHASES;
	}
	
	public void adaugaPhase(int i, int CPU, int IO, int REPEAT){
		//actualizare linia i
		PHASES[i][0]=CPU;
		PHASES[i][1]=IO;
		PHASES[i][2]=REPEAT;
	}

	public void verifica_recompense(int k, int r){
		while(true) {
			if(contorPenalizari < k && contorRecompense < r)
				break;

			boolean schimbare = false;

			if(contorPenalizari >= k) {
				if(nr_coada < 3 && nr_coada != 0) {
					nr_coada++;
					contorPenalizari -= k;
					schimbare = true;
				}
			}

			if(contorRecompense >= r) {
				if(nr_coada > 1) {
					nr_coada--;
					contorRecompense -= r;
					schimbare = true;
				}
			}

			if(!schimbare)
				break;
			
		}
	}
	
	int incrementeaza() {
		
		// daca mai trebuie executate operatii pt CPU, intram in if
		if(CPUcountCurent < PHASES[fazaCurenta][0]) {
			CPUcountCurent++;
			
			// daca am executat toate operatiile pe CPU necesare repetitiei, returnam 1
			if(CPUcountCurent == PHASES[fazaCurenta][0])
			return 1;
			
			// altfel returnam 0
			return 0;
		}
		// daca mai trebuie executate operatii pt I/O, intram in if
		if (IOcountCurent < PHASES[fazaCurenta][1]) {
			IOcountCurent++;
		}
		
		// daca nu am executat toate operatiile pe I/O necesare repetitiei, returnam 0
		// ce inseamna ca nu a terminat inca
		if(IOcountCurent != PHASES[fazaCurenta][1])
		return 0;
		
		// daca s-au terminat si CPU si I/o, actualizam nr repetitii
		if (repetitiaCurenta < PHASES[fazaCurenta][2]) {
			repetitiaCurenta++;
			CPUcountCurent = 0;
			IOcountCurent = 0;
			
			
		} 
		
		if (repetitiaCurenta == PHASES[fazaCurenta][2]) { 
			// daca nr de repetitii a fost deja atins, actualizam indexul fazei
			if (fazaCurenta < PHASES_COUNT) {
				fazaCurenta++;
				repetitiaCurenta = 0;
				CPUcountCurent = 0;
				IOcountCurent = 0;
				
				
			} 
			
			// daca nr de faze deja a fost atins, am terminat de executat fazele proceslui
			if (fazaCurenta == PHASES_COUNT) {
				processFinished = true;
			}
			
			return 3;
		}
		
		return 2;
	}

}