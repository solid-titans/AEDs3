package produtos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import menu.sistema.graficos.*;
import crud.Registro;

public class Pergunta implements Registro {

	//Atributos
    private static ASCIInterface graficos  = new ASCIInterface(199, 231 , 232, 184);
    private static ANSILibrary   destaque = new ANSILibrary(15, 27, ANSILibrary.TEXTO_SUBLINHADO);

    private int idPergunta;
    private int idUsuario;
    private short nota;
    private boolean ativa;
    private long criacao;
    private String pergunta;
    private String palavrasChave;

    //Construtor de uma pergunta vazia
    public Pergunta(){
        this.idPergunta    = -1;
        this.idUsuario     = -1;
        this.nota          = 0;
        this.ativa         = false;
        this.criacao       = -1;
        this.pergunta      = "";
        this.palavrasChave = "";
    }

    //Construtor de uma pergunta
    public Pergunta(int idUsuario,String pergunta,String palavrasChaves) {
        this.idPergunta    = -1;
        this.idUsuario     = idUsuario;
        this.nota          = 0;
        this.ativa         = true;
        this.criacao       = new Date().getTime();
        this.pergunta      = pergunta;
        this.palavrasChave = consertarPalavrasChave(palavrasChaves);
    }

    //Funções 'set'
    public void setId(int idPergunta) {
        this.idPergunta = idPergunta;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setCriacao(long criacao) {
        this.criacao = criacao;
    }

    public void setNota(short nota) {
        this.nota = nota;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public void setPalavrasChave(String palavrasChave) {
        this.palavrasChave = palavrasChave;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    //Funções 'get'
    public String chaveSecundaria() {
        return null;
    }

    public int getId() {
        return this.idPergunta;
    }

    public int getIdUsuario() {
        return this.idUsuario;
    }

    public long getCriacao() {
        return this.criacao;
    }

    public short getNota() {
        return this.nota;
    }

    public String getPergunta() {
        return this.pergunta;
    }

    public String getPalavrasChave() {
        return this.palavrasChave;
    }

    public boolean getAtiva() {
        return this.ativa;
    }

    public String toString() {
        return        destaque.imprimir(getData()) + 
               "\n" + graficos.caixa(getPergunta()) + 
               "Palavras-chave: " + destaque.imprimir(getPalavrasChave()) + "\n";
    }

    // Serializar objeto
    /*
    *   Faz uso do ByteArrayOutputStream e do DataOutputStream
    *   para armazenar os dados do usuário dentro de um array
    *   bytes e depois retornar esse array.
    */
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        DataOutputStream      data      = new DataOutputStream(byteArray);

        data.writeInt(this.idPergunta);
        data.writeInt(this.idUsuario);
        data.writeBoolean(this.ativa);
        data.writeShort((int)this.nota); //O unico jeito de escrever short é usando int
        data.writeLong(this.criacao);
        data.writeUTF(this.pergunta);
        data.writeUTF(this.palavrasChave);

        return byteArray.toByteArray();
    }

    // Desserializar objeto
    /*
    *   Recebe um array de bytes, que a partir do uso do 
    *   ByteArrayInputStream e o DataInputStream, consegue
    *   receber os dados do array e armazenar no objeto.
    */
    public void fromByteArray(byte[] arrayObjeto) throws IOException {
        ByteArrayInputStream byteArray = new ByteArrayInputStream(arrayObjeto);
        DataInputStream      data      = new DataInputStream(byteArray);

        this.idPergunta    = data.readInt();
        this.idUsuario     = data.readInt();
        this.ativa         = data.readBoolean();
        this.nota          = data.readShort();
        this.criacao       = data.readLong();
        this.pergunta      = data.readUTF();
        this.palavrasChave = data.readUTF();
    }

    public String getData() {
        String   resp = "";
        String[] array = null;

        array = ofLongToStringData(this.criacao).split(" ");
        resp += "Data: " + array[0] + " às " + array[1];
        return resp;
    }
    /** ofLongToStringData - a contribution by Homecas
     *  
     * @param data
     * @return
     */
    private String ofLongToStringData(long data){

        // Definir o tempo Sistema
        Date SistemaData = new Date(data);
        // Caledario para setar o fuso horario
        Calendar caledario = Calendar.getInstance();
        // Formatacao de como data 
        DateFormat dataFormatada = new SimpleDateFormat ("dd/MM/yyyy HH:mm");
                
        // Formanto a formatacao da data apatir do fuso horario do sistema 
        dataFormatada.setTimeZone(caledario.getTimeZone());
                
        
        return dataFormatada.format(SistemaData);
    }

    public static String consertarPalavrasChave(String palavrasChaves) {

        String resp = palavrasChaves.trim().toLowerCase();

        resp = resp.replaceAll("\\s+"," ");

        resp = resp.replaceAll("[áàãâ]","a");
        resp = resp.replaceAll("[éèẽê]","e");
        resp = resp.replaceAll("[íìĩî]","i");
        resp = resp.replaceAll("[óòõô]","o");
        resp = resp.replaceAll("[úùũû]","u");

        resp = resp.replaceAll("[ńǹñ]","n");
        resp = resp.replaceAll("[ĺ]","l");
        resp = resp.replaceAll("ç","c");

        return resp;
    }

} 