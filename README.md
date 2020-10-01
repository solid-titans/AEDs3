# Crud AEDs 3

![Markdown Logo](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "Markdown Logo")
![Crud](https://img.shields.io/badge/Crud-FUNCIONAL!-brightgreen "Situação do Crud")
![License](https://img.shields.io/badge/openSource-MIT-blue "License")

Este repositório é dedicado a fazer o CRUD de AEDs 3.

# Quer testar?

Entre no meu [Replit](https://repl.it/github/solid-titans/AEDs3)!

## Já está no meu Replit?

Para usar você precisa apenas clicar no botão ```RUN``` e o código irá compilar e executar o programa!

### [Github do Projeto](https://github.com/lusantisuper/AEDs3/)

# O que já está funcionando:
- [x] Crud Funcional
- [x] Método de criar novos objetos em disco.
- [x] Método de leitura dos objetos do disco.
- [x] Método de atualizar registros em disco.
- [x] Método de deletar objetos do disco.
- [x] Menu iterativo de usuário
- [x] Desafio 1: Limite percentual máximo para esse espaço deve provocar a movimentação do registro caso excedido.
- [x] Desafio 2: Crud de Lixo para aproveitar melhor os registros excluídos
- [x] Desafio 3: Simular um envio de email para trocar a senha do usuário
- [x] Desafio opcional: Usar algum algoritmo de hashing para escrever a senha do usuário no banco de dados. Algoritmo usado: SHA3-512 bits.

Considerações: **Crud suficiente!**

###
| Métodos | Retorno | Explicação do Método |
| ------- | ------- | -------------------- |
| Crud(String nomeArquivo, Constructor<T> constructor)| Crud | Método de criar um Crud no disco.
| create(T Entidade)| int | Método create retorna o id que foi usado para inserir seu objeto no banco de dados |
| read(String chave) | T | Retorna o objeto caso a chave secundária for encontrada no disco, não encontrar resulta em ```null```|
|update(T entidade, int id) | boolean | Atualiza no disco um objeto ( Por enquanto precisa da ```id``` para encontra-lo)
| delete(String chave) | boolean | Função deleta do disco um registro |

# Acompanha meu repositório?

Explicação das ```branchs``` do meu projeto. 

|Branch | O que ela possui |
|---    |---               |
| [Master](https://github.com/lusantisuper/AEDs3/) | Todos os códigos estão estáveis e funcionando! |
| [Beta](https://github.com/lusantisuper/AEDs3/tree/beta) | Updates em tempo real! Veja tudo que está por vir antes de ser estável! :)

Quer ver as grandes atualizações do meu projeto.

| Release | Lançamento | Link |
|---      |---         |---       |
| V1.0    | 01/08/2020 | [Download](https://github.com/lusantisuper/AEDs3/releases/tag/V1.0) |
| V2.0    | 08/08/2020 | [Download](https://github.com/lusantisuper/AEDs3/releases/tag/V2.0) |
| V3.0    | 22/08/2020 | [Download](https://github.com/solid-titans/AEDs3/releases/tag/V3.0) |
| V4.0    | 29/08/2020 | [Download](https://github.com/solid-titans/AEDs3/releases/tag/V4.0) |


# License
[MIT](https://choosealicense.com/licenses/mit/)
