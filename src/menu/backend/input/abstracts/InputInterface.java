package menu.backend.input.abstracts;

public interface InputInterface {
    public String lerString();
    public String lerString(String texto);

    public char   lerChar();
    public char   lerChar(String texto);

    public int    lerInt();
    public int    lerInt(String texto);

    public byte   lerByte();
    public byte   lerByte(String texto);

    public void   esperarUsuario();
}
