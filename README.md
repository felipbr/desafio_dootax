# Projeto desafio

Projeto spring boot que compreende três funcionalidades:

 1) Um processo para ler uma pasta pré-definida no sistema operacional e carregar o conteúdo dos arquivos dessa pasta para uma tabela do banco de dados. Esse processo é executado automaticamente a cada 180 segundos.

Obs: Um arquivo válido possui dois campos, código de empresa e chave do documento, onde a chave dos documentos possui 44 caracteres numéricos. O formato do arquivo segue o exemplo abaixo:

0060;35170501512104006006550010001597471100000014
0060;35170501512104006006550010001599431100000016
0090;35170501512104006006550010001592701100000015

2) Uma API que recebe uma lista de chaves e altera o status dessas chaves na tabela para "Validado". A resposta da API deve é síncrona.
A lista de chaves possui no máximo 20 itens, e cada um deles é validado para possuir 44 caracteres numéricos.
A resposta da API lista as chaves cujo status foram alterados com sucesso, e as que não foram por algum erro (chave inexistente, incompleta, etc)

Brecha no desafio: No cenário acima, caso exista duas empresas com o mesmo código de documento, ambos serão validados.

 3) Uma API que faz a listagem das chaves existentes no banco de dados e seu status atual. É possível buscar as chaves de uma empresa específica, informada na request.
A reposta da API deve apresenta uma lista de no máximo 20 chaves por request, e possibilita que o usuário consulte todas as chaves existentes na tabela.

Código criado:
- Utilizando preferencialmente funcionalidades do Java 8.
- API Restful.
- Banco de dados Postgres utilizando de Spring Data JPA para a interação com a tabela.
- Foi analisado a performance, considerando arquivo com milhares de linhas e milhares de arquivos.

## Obs: Foi utilizado de JUnit para criar alguns testes sobre os serviços.

# Projeto frontEnd

Criado de forma simplória com o intuito de testar os serviços de validação das chaves (2) e listagem das chaves (3), e com isso validar visualmente a funcionalidade.
