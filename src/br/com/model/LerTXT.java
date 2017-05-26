package br.com.model;

import java.io.IOException;

public class LerTXT {
	public static void main(String[] args) throws IOException {
		ImprimeEmail_Status imprime = new ImprimeEmail_Status();
		imprime.gravaArquivo();
	}
}
