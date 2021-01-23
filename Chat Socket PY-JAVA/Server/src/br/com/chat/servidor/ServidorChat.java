package br.com.chat.servidor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ServidorChat extends Thread {
	/**
	Cria uma classe servidor do tipo ServerSocket
	*/
	private ServerSocket servidor; 
	/**
	 * Cria uma variavel para guardar o IP, InetAddress pega o IP local da maquina
	 */
	private InetAddress IP;
	/**
	 * Cria um conjunto(set) que ficara guardado a conexao dos usuarios
	 */
	private Set<Socket> usuariosConectados = new HashSet<>();

	public ServidorChat(int porta) throws IOException {
		/**
		 * Cria um servidor que ficara ouvindo na porta
		 */
		IP = InetAddress.getLocalHost(); /**
		Pega o IP da maquina
		*/

		servidor = new ServerSocket(porta);
		/**
		 * Cria um servidor na porta X
		 */
	}

	public void run() {
		while (true) {
			try {

				/**
				 * Servidor ficara aguardando conexoes de seus clientes
				 */
				System.out.println("=> Servidor aguardando conexoes porta " + IP.getLocalHost() + ":" + 4444);
				Socket conexaoComOCliente = servidor.accept();

				/**
				 * Alguem conectou
				 */
				System.out.println("=> Cliente conectou: " + conexaoComOCliente.getInetAddress());
				/**
				 * Adiciona a conexao do cliente ao conjunto(set) de conexoes (Set<Socket>)
				 */
				usuariosConectados.add(conexaoComOCliente);
				/**
				 * Cria e inicia uma thread para ficar recebendo dados do cliente e enviando para os demais
				 */
				new Thread(new RecebimentoDadosCliente(conexaoComOCliente, usuariosConectados)).start();


			} catch (IOException e) {
				e.printStackTrace();
				break;
				/**
				 * Encerra o codigo caso tenha algum problema
				 */
			}
		}

	}

	public static void main(String[] args) {
		/**
		 * Metodo Main inicia o codigo, criando um servidor na porta 4444
		 */
		try {
			ServidorChat servidor = new ServidorChat(4444);
			servidor.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public class RecebimentoDadosCliente implements Runnable {
		/**
		 * Define o metodo que sera executado na thread de recebimento de dados
		 */
		private Socket conexaoCliente;
		private Set<Socket> usuariosConectados;

		public RecebimentoDadosCliente(Socket conexaoCliente, Set<Socket> usuariosConectados) {
			this.conexaoCliente = conexaoCliente;
			this.usuariosConectados = usuariosConectados;
		}

		@Override
		public void run() {
			try {
				/**
				 * Cria uma Stream para receber os dados do cliente
				 */
				BufferedReader entradaDados = new BufferedReader(new InputStreamReader(conexaoCliente.getInputStream()));
				/**
				 * Recebe os dados e caso eles não sejam nulos, manda para todos os outros usuarios.
				 */
				while (true) {
					String dadosRecebidos = entradaDados.readLine();
					if (dadosRecebidos != null) {
						System.out.println(dadosRecebidos);
						for (Socket usuarioConectado : usuariosConectados) {
							if (usuarioConectado != conexaoCliente) {
								
								BufferedWriter saidaDados = new BufferedWriter(new OutputStreamWriter(usuarioConectado.getOutputStream()));
								saidaDados.write(dadosRecebidos);
								saidaDados.flush();

							}
						}
					}

					

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
