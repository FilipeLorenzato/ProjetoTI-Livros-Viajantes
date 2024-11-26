# Livros Viajantes

 Uma plataforma de troca de livros, onde os usuários podem cadastrar livros que não utilizam mais e trocá-los com outros membros da comunidade. A proposta incentiva o compartilhamento de conhecimento e promove o acesso a novos livros sem a necessidade de compra.

## Alunos integrantes da equipe

* Filipe Lorenzato Cardoso Rodrigues.
* Gabryelle Franco Xavier.
* Iuri Saad Furtunato Fialho.
* Rodrigo Oliveira Andrade de Vasconcelos.

## Professores responsáveis

* Amália Soares Vieira de Vasconcelos.
* Max do Val Machado.

## Instruções de utilização

1- Para executar o programa, é necessário instalar as dependências do Maven, sendo o uso da IDE Eclipse altamente recomendado para facilitar a integração. Todas as dependências Maven descritas no arquivo pom.xml, localizado no diretório Codigo/Projeto/src/main/java, devem ser instaladas.

2- Em seguida, é preciso executar o script SQL encontrado em Codigo/Projeto/src/main/resources/sql. Esse script será responsável por criar as tabelas exigidas no banco de dados, garantindo o funcionamento adequado do sistema.

3- O próximo passo envolve a configuração das credenciais relacionadas ao banco de dados PostgreSQL. Para isso, deve-se acessar cada arquivo da pasta dao, localizada em Codigo/Projeto/src/main/java/dao. Em cada um desses arquivos há um método chamado conectar, onde as credenciais e informações do banco de dados devem ser configuradas.

4- Depois disso, o próximo passo é iniciar o servidor Spark, que é responsável pelo funcionamento do back-end. No Eclipse, localize o arquivo Main.java no diretório Codigo/Projeto/src/main/java/app e execute-o.

5- Com o servidor Spark em execução, é necessário configurar e iniciar o servidor Node.js, essencial para o sistema inteligente de extração de informações de imagens. 1- Caso ainda não tenha o Node.js, faça a instalação da versão mais recente na sua máquina. 2- No terminal, acesse o diretório Codigo/Projeto/main/resources/java/service e execute o comando npm install, que instalará todas as dependências necessárias. 3- Após a instalação, inicie o servidor Node.js. Para isso, no Eclipse, encontre o arquivo script.js, clique com o botão direito, selecione "Run As" e, em seguida, "Node Application". 4- Agora o servidor Node.js estará em pleno funcionamento.

6- O último passo consiste em rodar o front-end. Para isso, siga estas etapas: 1- Caso ainda não tenha, instale o Visual Studio Code (VS Code) em sua máquina. 2- No VS Code, instale a extensão Live Server. 3- Abra a pasta Codigo/Projeto/src/main/resources como um projeto no VS Code. 4- Localize o arquivo login.html, clique com o botão direito sobre ele e selecione "Open With Live Server". 5- Pronto! O front-end já está configurado e pronto para uso.

7- Por fim, para utilizar o sistema, faça login com as credenciais iniciais ou registre-se. Informações de login padrão:
Usuário: usuario1
Senha: usuario123

8- Pronto! Agora você pode aproveitar tudo o que a plataforma Livros Viajantes tem a oferecer!

