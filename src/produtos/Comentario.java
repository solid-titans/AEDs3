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

import produtos.abstracts.RegistroVisualComentario;

public class Comentario implements RegistroVisualComentario{
    
    private byte   tipo;
    private int    idUsuario;
    private int    idComentario;
    private int    idPR;
    private long   criacao;
    private String comentario;

    //Construtor de votos vazia
    public Comentario() {
        this.tipo         = -1;
        this.idComentario = -1;
        this.idUsuario    = -1;
        this.idPR         = -1;
        this.criacao      =  0;
        this.comentario   = "";
    }

    //Construtor de votos
    public Comentario(byte tipo, int idComentario, int idUsuario, int idPR, long criacao, String comentario) {
        this.tipo         = tipo;
        this.idComentario = idComentario;
        this.idUsuario    = idUsuario;
        this.idPR         = idPR;
        this.criacao      = criacao;
        this.comentario   = comentario;
    }

    public Comentario(int idUsuario, int idPR, String comentario, byte tipo) {
        this(tipo,-1,idUsuario,idPR,new Date().getTime(),comentario);
    }

    public String chaveSecundaria() {
        return null;
    }

    //Funções 'get'
    public byte getTipo() {
        return this.tipo;
    }
    
    public char getTipoChar() {
        return getTipo() == 0 ? 'R':'P';
    }

    public int getId() {
        return this.idComentario;
    }

    public int getIdUsuario() {
        return this.idUsuario;
    }

    public int getIdPR() {
        return this.idPR;
    }

    public long getCriacao(){
        return this.criacao;
    }

    public String getComentario() {
        return this.comentario;
    }

    //Funções 'set'
    public void setTipo(byte tipo) {
        this.tipo = tipo;
    }

    public void setId(int idComentario) {
        this.idComentario = idComentario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdPR(int idPR) {
        this.idPR = idPR;
    }

    public void setCriacao(long criacao){
        this.criacao = criacao;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
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

        data.writeByte(this.tipo);
        data.writeInt(this.idComentario);
        data.writeInt(this.idPR);
        data.writeInt(this.idUsuario);
        data.writeLong(this.criacao);
        data.writeUTF(this.comentario);

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

        this.tipo         = data.readByte();
        this.idComentario = data.readInt();
        this.idPR         = data.readInt();
        this.idUsuario    = data.readInt();
        this.criacao      = data.readLong();
        this.comentario   = data.readUTF();
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
        return "([" + getData()+"]) \n["+ comentario +"]" ;
    }
    
    public String imprimirSimplificado() {
        return "([" + getData()+"]) \n{"+ comentario +"}";
    }

    public String imprimir(String nome) {
        return "([" + getData()+"]) \n["+ comentario +"] {Resposta criada por: " + nome + "}";
    }

    @Override
    public boolean getAtiva() {
        return true;
    }

}