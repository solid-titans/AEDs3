# Interface gráfica em terminal, usando ASCII e ANSI

Criada por: **Gustavo Lopes Rodrigues**

Versão: 0.0.2 _(beta)_

# 1 - Definição 

_ASCIInterface_ e _ANSILibrary_ são duas classes que eu criei
para poder implementar uma interface gráfica apenas usando o terminal.

## 1.1 - ANSILibrary 

_ANSI_ é uma forma de codificação de caracteres que basicamente
é uma extensão da biblioteca _ASCII_ e adiciona 128 caracteres a 
mais. A _ANSILibrary_ é uma classe que pega esses caracteres e os
utiliza para personalizar o texto no terminal, seja adicionando
**destaque** , **cor** , ou **cor de fundo**.

### 1.2 - Como utilizar

Como já dito anteriormente, a classe ANSI precisa de três argumentos:

* Cor
  
* Cor de fundo

* Destaque

A classe possui quatro construtores, cada um para definir de imediato o que
o usuário de imediato quer, além de funções 'set' e 'get' para pegar os dados do objeto.

#### 1.2.1 Cor e Cor de Fundo

Tanto a cor quanto a cor de fundo levam como argumento um número que vai de 0 a 255, o resultado será uma cor que pode variar desde constraste até intensidade(olhe no final desse documento para ver que número corresponde
a qual cor).

#### 1.2.2 Destaque

O destaque se refere a textos em _itálico_, **negrito** ou sublinhado, para
configurar isso, o código já vem com variáveis estáticas (static) para definir o tipo de destaque.

* TEXTO_ITÁLICO    - \u001B[3m

* TEXTO_SUBLINHADO - \u001B[4m

* TEXTO_NEGRITO - \u001B[1m

## 2.1 - ASCIInterface

A classe _ASCIInterface_ integra a funcionalidade da _ANSILibrary_ para criar caixas, tabelas além de outras coisas customizáves, para deixar mais interessante a navegação pelo terminal.

