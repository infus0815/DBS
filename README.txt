
COMO COMPILAR:
	javac -d bin src/Chunk/*.java src/DB/*.java src/Filesystem/*.java src/Initiators/*.java src/Listeners/*.java src/Message/*.java src/Peer/*.java src/RMI/*.java src/Utils/*.java src/*.java


COMO EXECUTAR:
	Este projeto usa RMI como “protocolo de transporte”.
	Exemplo de utilização:

	1)LANÇAR RMI - TERMINAL 1:
		rmiregistry & (UNIX)
		start rmiregistry (Windows)

	2)TERMINAL 2:
		cd bin
		java -Djava.net.preferIPv4Stack=true Peer.Peer 1 224.0.0.233 2201 224.0.0.234 2202 224.0.0.235 2203 74
		(java (-Djava.net.preferIPv4Stack=true) Peer.Peer <id> <mcaddress> <mcport> <mdbaddress> <mdbport> <mdraddress> <mdrport> <size>)

	3) TERMINAL 3:
		cd bin
		java -Djava.net.preferIPv4Stack=true Peer.Peer 2 224.0.0.233 2201 224.0.0.234 2202 224.0.0.235 2203 74
		(java (-Djava.net.preferIPv4Stack=true) Peer.Peer <id> <mcaddress> <mcport> <mdbaddress> <mdbport> <mdraddress> <mdrport> <size>)

	[desta forma, temos 2 peers; para aumentar o numero de peers, devemos repetir o passo 3 tendo em atenção de não repetir os ids]

	4) TERMINAL 4:
		cd bin
		PARA BACKUP:
			java RMI.TestApp 1 BACKUP Tulips.jpg 1
			(java RMI.TestApp <id> BACKUP <file> <replication_degree>)
		PARA RESTORE:
			java RMI.TestApp 1 RESTORE Tulips.jpg
			(java RMI.TestApp <id> RESTORE <file>)
		PARA DELETE:
			java RMI.TestApp 1 DELETE Tulips.jpg
			(java RMI.TestApp 1 DELETE <file>)