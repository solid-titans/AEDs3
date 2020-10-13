public class Teste {
    public static void main(String[] args) {
        // Instânciar a classe do GFG com o número máximo de interações
        GFG gfg = new GFG(100);

        // Fazendo o hash da senha
        String senha = gfg.hash(args[0]);
        System.out.println("Hash Final: " + senha);

        // Verificando uma senha com o hash que esta no banco de dados
        if(gfg.verificarHash(senha, args[0])) 
            System.out.println("As senhas são iguais!");

    }
}