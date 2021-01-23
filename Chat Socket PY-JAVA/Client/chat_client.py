import socket
import thread
import sys

SERVIDOR = '192.168.0.49'    # Servidor remoto  IP da maquina, checar servido no JAVA
PORTA = 4444           # Porta de conexao no servidor remoto
BUFFER_RECEBIMENTO=4096
nickname= raw_input("Digite um nickname")
def receber_dados():
    print ("Recebendo dados de outros clientes conectados no servidor")
    while 1:
        try: #Tenta receber dados do servidor
            dados_recebidos = conexao_servidor.recv(BUFFER_RECEBIMENTO)            
        except: 
            print ("Servidor fechou conexao.")
            thread.interrupt_main()
            break
        if not dados_recebidos: 
                print ("Servidor fechou a conexao.")
                thread.interrupt_main()
                break
        else: # Printa os dados recebidos do servidor
                sys.stdout.write(dados_recebidos)
                sys.stdout.write('\n')
                sys.stdout.write('<= '); sys.stdout.flush()

def enviar_dados():
    "Envia dados para outros clientes conectados no servidor"
    while 1:
        print ("Digite uma mensagem ou (q or Q para sair):")
        dados_envio = sys.stdin.readline() #Espera o usuario digitar uma mensagem para ser enviada, especie de input
        if dados_envio == "q" or dados_envio == "Q": #Caso a string enviada seja "Q" ou "q" fecha a conexao
            msg= nickname+": "+dados_envio
            conexao_servidor.send(msg)
            thread.interrupt_main()
            break
        else:#Envia os dados para o servidor
            msg= nickname+": "+dados_envio
            conexao_servidor.send(msg)
        
if __name__ == "__main__":



    print ("******* Cliente Chat Python 2.7 ********")
    print ("Conectando no servidor "+SERVIDOR+":"+str(PORTA)) #Informa o leitor que se esta conectador no servidor de IP "x" e de porta "y"

    conexao_servidor = socket.socket(socket.AF_INET, socket.SOCK_STREAM)#Cria um socket
    conexao_servidor.connect((SERVIDOR, PORTA))#Realiza a conexao com o servidor usando o IP e Porta como parametros

    print ("Conectado no servidor "+SERVIDOR+":"+str(PORTA))

    thread.start_new_thread(receber_dados,())#Starta uma thread para receber dados, executando a funcao receber_dados()
    thread.start_new_thread(enviar_dados,()) # Starta uma thread para enviar dados, executando a funcao enviar_dados()

    try:
        while 1:
            continue
    except:
        print ("Fim...")
        conexao_servidor.close()
        
