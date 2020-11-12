/*
// Registro de resposta para ser usado no CRUD
*/

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

import produtos.abstracts.RegistroVisualplus;

public class Resposta implements RegistroVisualplus {

    private int idResposta;
    private int idPergunta;
    private int idUsuario;
    private short nota;
    private boolean ativa;
    private long criacao;
    private String resposta;

    // Construtor de resposta vazia
    public Resposta() {
        this.idResposta    = -1;
        this.idPergunta    = -1;
        this.idUsuario     = -1;
        this.nota          = 0;
        this.ativa         = false;
        this.criacao       = 0;
        this.resposta      = "";
    }

    /**
     * Construtor de resposta com todos atributos
     * @param idResposta
     * @param idPergunta
     * @param idUsuario
     * @param nota
     * @param ativa
     * @param criacao
     * @param resposta
     */
    public Resposta(int idResposta, int idPergunta, int idUsuario, short nota, boolean ativa, long criacao, String resposta){
        this.idResposta    = idResposta;
        this.idPergunta    = idPergunta;
        this.idUsuario     = idUsuario;
        this.nota          = nota;
        this.ativa         = ativa;
        this.criacao       = criacao;
        this.resposta      = resposta;
    }

    /**
     * Construtor de resposta com apenas alguns atributos
     * @param idPergunta
     * @param idUsuario
     * @param resposta
     */
    public Resposta(int idPergunta,int idUsuario,String resposta) {
        this(-1,idPergunta,idUsuario,(short)0,true,new Date().getTime(),resposta);
    }

    // Funções set
    public void setId(int idResposta){
        this.idResposta = idResposta;
    }

    public void setIdPergunta(int idPergunta){
        this.idPergunta = idPergunta;
    }

    public void setIdUsuario(int idUsuario){
        this.idUsuario = idUsuario;
    }

    public void setNota(short nota){
        this.nota = nota;
    }

    public void setAtiva(boolean ativa){
        this.ativa = ativa;
    }

    public void setCriacao(long criacao){
        this.criacao = criacao;
    }

    public void setResposta(String resposta){
        this.resposta = resposta;
    }

    // Funções get
    public int getId(){
        return this.idResposta;
    }

    public int getIdPergunta(){
        return this.idPergunta;
    }

    public int getIdUsuario(){
        return this.idUsuario;
    }

    public short getNota(){
        return this.nota;
    }

    public boolean getAtiva(){
        return this.ativa;
    }

    public long getCriacao(){
        return this.criacao;
    }

    public String getResposta(){
        return this.resposta;
    }

    public String chaveSecundaria(){
        return null;
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

        data.writeInt(this.idResposta);
        data.writeInt(this.idPergunta);
        data.writeInt(this.idUsuario);
        data.writeShort((int)this.nota); 
        data.writeBoolean(this.ativa);
        data.writeLong(this.criacao);
        data.writeUTF(this.resposta);

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

        this.idResposta    = data.readInt();
        this.idPergunta    = data.readInt();
        this.idUsuario     = data.readInt();
        this.nota          = data.readShort();
        this.ativa         = data.readBoolean();
        this.criacao       = data.readLong();
        this.resposta      = data.readUTF();
    }

    /**
     * Função que pega o conteúdo de ofLongToStringData e o ajusta 
     * @return a Data de criação da pergunta
     */
    public String getData() {
        String   resp = "";
        String[] array = null;

        array = ofLongToStringData(this.criacao).split(" ");
        resp += array[0] + " às " + array[1];
        return resp;
    }
    
    /** 
     * Função para converter o horário de milisegundos para o formato: dia/mes/ano - hora:minutos em uma String
     * Obrigado a Homenique Vieira por ter feito essa função
     * 
     * @param data é os mililisegundos em long
     * @return uma String contendo a data representada por data
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

    public String imprimir() {
        return "([" + getData()+"]) \n["+ resposta +"] (nota :" + this.nota + ")";
    }
    
    public String imprimirSimplificado() {
        return "([" + getData()+"]) \n{"+ resposta +"}";
    }

    public String imprimir(String nome) {
        return "([" + getData()+"]) \n["+ resposta +"] {Resposta criada por: " + nome + "} \n (nota :" + this.nota + ")";
    }
}