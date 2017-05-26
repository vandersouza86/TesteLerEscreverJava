package br.com.model;

import br.com.util.OrdenaMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

public class ImprimeEmail_Status {
	private Logger logger = Logger.getLogger(LerTXT.class);

	public ImprimeEmail_Status() {
	}

	public List imprimirEmail(String localArquivo) {
		File arquivo = new File(localArquivo);
		String linha = new String();
		String[] textoSeparado = null;
		List<String> emailList = new ArrayList();
		List nLista = null;
		if (arquivo.exists()) {
			try {
				FileReader leitor = new FileReader(localArquivo);
				BufferedReader buffer = new BufferedReader(leitor);
				while (linha != null) {
					linha = buffer.readLine();
					if ((linha != null) && (linha.contains("level=info response_body=\"\""))) {
						linha = linha.substring(linha.indexOf("level=info"), linha.length());
						linha = linha.replaceAll(" ", ";");
						textoSeparado = linha.split(";");
						String email = null;

						if ((textoSeparado[2] != null) || (!textoSeparado[2].isEmpty())) {
							email = textoSeparado[2];
							emailList.add(email);
						}
					}
				}

				Collections.sort(emailList);

				Map<String, Integer> listaEmail = new HashMap();
				String aux;
				for (int i = 0; i < emailList.size(); i++) {
					aux = (String) emailList.get(i);

					if (listaEmail.get(aux) != null) {
						listaEmail.put(aux, Integer.valueOf(((Integer) listaEmail.get(aux)).intValue() + 1));
					} else {
						listaEmail.put(aux, Integer.valueOf(1));
					}
				}

				nLista = OrdenaMap.entriesSortedByValues(listaEmail);
				for (int a = 0; a < 3; a++) {
					aux = nLista.get(a).toString().replace("request_to=", " ");
				}
			} catch (Exception e) {
				logger.error(e);
			}
		}
		return nLista;
	}

	public List imprimirResponseStatus(String localArquivo) {
		File arquivo = new File(localArquivo);
		String linha = new String();
		String[] textoSeparado = null;
		List<String> statuslList = new ArrayList();
		List nLista = null;
		if (arquivo.exists()) {
			try {
				FileReader leitor = new FileReader(localArquivo);
				BufferedReader buffer = new BufferedReader(leitor);
				while (linha != null) {
					linha = buffer.readLine();
					if ((linha != null) && (linha.contains("response_status"))) {
						linha = linha.substring(linha.indexOf("response_status"), linha.length());
						linha = linha.replaceAll(" ", ";");
						textoSeparado = linha.split(";");
						String status = null;

						if ((textoSeparado[0] != null) || (!textoSeparado[0].isEmpty())) {
							status = textoSeparado[0];
							status = status.replace("response_status=", " ");
							statuslList.add(status);
						}
					}
				}

				Collections.sort(statuslList);

				Map<String, Integer> listaStatus = new HashMap();
				String aux;
				for (int i = 0; i < statuslList.size(); i++) {
					aux = (String) statuslList.get(i);

					if (listaStatus.get(aux) != null) {
						listaStatus.put(aux, Integer.valueOf(((Integer) listaStatus.get(aux)).intValue() + 1));
					} else {
						listaStatus.put(aux, Integer.valueOf(1));
					}
				}

				nLista = OrdenaMap.entriesSortedByValues(listaStatus);
				for (int a = 0; a < listaStatus.size(); a++) {
					aux = nLista.get(a).toString();
				}

			} catch (Exception e) {
				logger.error(e);
			}
		}

		return nLista;
	}

	public void gravaArquivo() throws IOException {
		String nomeArquivo = JOptionPane.showInputDialog("Informe o caminho do arquivo:");
		String localArquivo = JOptionPane.showInputDialog("Caminho da resposta a ser criada:");
		if ((nomeArquivo == null) || (localArquivo == null)) {
			JOptionPane.showMessageDialog(null, "Os caminhos não foram informados.");
		} else {
			File file = new File(localArquivo);
			long begin = System.currentTimeMillis();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write("Arquivo gravado em : " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date()));
			writer.newLine();
			writer.write("Caminho da gravação: " + localArquivo);
			writer.newLine();
			long end = System.currentTimeMillis();
			writer.write("Tempo de gravação: " + (end - begin) + "ms.");
			writer.newLine();

			for (int a = 0; a < 3; a++) {
				String lRep1 = imprimirEmail(nomeArquivo).get(a).toString().replace("request_to=", " ");
				writer.write(lRep1);
				writer.newLine();
			}

			for (int a = 0; a < imprimirResponseStatus(nomeArquivo).size(); a++) {
				String lRep = imprimirResponseStatus(nomeArquivo).get(a).toString();
				writer.write(lRep);
				writer.newLine();
			}
			writer.flush();

			writer.close();
		}
	}
}