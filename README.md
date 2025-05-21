# Trabalho Prático AEDS III

## Nome dos integrantes

 - Antônio Drumond Cota de Sousa
 - Laura Menezes Heráclito Alves
 - Davi Ferreira Puddo
 - Raquel de Parde Motta
   
## Links dos trabalhos anteriores
- [TP1](https://github.com/AntonioDrumond/AED3_TP01/)
- [TP2](https://github.com/AntonioDrumond/AED3_TP02/)

## Introdução

Neste terceiro trabalho prático da disciplina, o sistema PUCFlix foi expandido mais uma vez, desta vez com foco na implementação de índices invertidos para permitir buscas por termos nas entidades principais: séries, episódios e atores. Enquanto os trabalhos anteriores estavam centrados no relacionamento entre entidades e na modelagem de dados usando Árvores B+, este TP teve como objetivo principal viabilizar a busca textual eficiente por nomes e títulos, utilizando a lógica de TF-IDF (Term Frequency – Inverse Document Frequency) e listas invertidas. Cada uma das três entidades passou a contar com seu próprio índice, possibilitando ao usuário pesquisar por termos ou frases e receber como resposta as entidades mais relevantes de acordo com os cálculos realizados.

## Experiência

Este trabalho consolidou e expandiu a experiência adquirida nas etapas anteriores do projeto PUCFlix. Tendo já estabelecido a base do sistema com os relacionamentos 1:N e o uso de estruturas como Tabela Hash e Árvore B+, o foco agora foi voltado para uma camada adicional de complexidade: a implementação da busca textual com base em índices invertidos. A construção e manipulação das listas invertidas para séries, episódios e atores trouxe uma perspectiva nova sobre o papel dos índices em sistemas de informação, em especial no contexto de recuperação de dados não estruturados, como textos.


## Desafios

Entre os principais desafios enfrentados, o primeiro foi adaptar a estrutura do sistema para comportar a indexação e a busca de forma genérica e reutilizável para diferentes entidades. Precisamos modificar e estender a classe ListaInvertida para que ela pudesse lidar com séries, episódios e atores de maneira independente, sem comprometer a integridade do sistema. Outro ponto delicado foi garantir que qualquer modificação nas entidades, como inserções, exclusões ou alterações, fosse refletida corretamente no índice invertido. Isso exigiu cuidado redobrado no controle das operações, além de diversos testes para validar que os índices estavam sempre atualizados. Por fim, lidar com a normalização de texto — remoção de acentos, conversão para minúsculas e filtragem de stop words — foi uma parte fundamental para garantir a consistência das buscas, mas também uma fonte de bugs sutis quando não tratada com rigor.

## Testes e validação

Durante o desenvolvimento, realizamos testes manuais e automatizados para garantir a integridade das operações e a consistência dos dados. Foram simulados diferentes cenários de uso, como tentativa de exclusão de atores vinculados, vinculações duplicadas e remoção de séries com múltiplos atores associados. 

## Resultados
Nesta terceira etapa do trabalho prático, conseguimos implementar com sucesso o mecanismo de busca textual utilizando listas invertidas e ranqueamento com TF-IDF, conforme proposto. O sistema PUCFlix passou a oferecer uma nova camada de funcionalidade, agora permitindo buscas mais inteligentes e relevantes por séries, episódios e atores a partir de palavras-chave digitadas pelo usuário. Isso tornou o sistema mais completo e alinhado com o funcionamento real de plataformas de streaming comerciais.

As listas invertidas foram geradas corretamente para os três domínios de busca, indexando os textos de sinopses e nomes com remoção de stopwords e normalização, o que aumentou a precisão das buscas. O cálculo do TF-IDF foi integrado ao processo de consulta e permitiu ranquear os resultados de forma eficiente, priorizando os itens mais relevantes de acordo com a frequência das palavras na base e no documento. Os resultados são apresentados de forma ordenada, oferecendo ao usuário uma experiência de busca clara e funcional.

## Checklist

- [x] O índice invertido com os termos dos títulos das séries foi criado usando a classe ListaInvertida? **SIM**.

- [x] O índice invertido com os termos dos títulos dos episódios foi criado usando a classe ListaInvertida? **SIM**.

- [x] O índice invertido com os termos dos nomes dos atores foi criado usando a classe ListaInvertida? **SIM**.

- [x] É possível buscar séries por palavras usando o índice invertido? **SIM**.

- [x] É possível buscar episódios por palavras usando o índice invertido? **SIM**.
      
- [x] É possível buscar atores por palavras usando o índice invertido? **SIM**.

- [x] O trabalho está completo? **SIM**

- [x] O trabalho é original e não a cópia de um trabalho de outro grupo? **SIM**
